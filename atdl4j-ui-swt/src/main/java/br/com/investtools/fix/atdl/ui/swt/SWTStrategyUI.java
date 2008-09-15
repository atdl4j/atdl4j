package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateGroupDocument.StateGroup;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateGroupDocument.StateGroup.EnumItem;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateGroupDocument.StateGroup.Field;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateRuleDocument.StateRule;
import br.com.investtools.fix.atdl.iterators.StrategyIterator;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyLayoutDocument.StrategyLayout;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;
import br.com.investtools.fix.atdl.ui.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.validation.EditUI;
import br.com.investtools.fix.atdl.ui.swt.validation.StrategyEditUI;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditRefT;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

/**
 * UI representation for a Strategy object.
 * 
 */
public class SWTStrategyUI implements StrategyUI {

	private static final Logger logger = LoggerFactory
			.getLogger(SWTStrategyUI.class);

	private StrategyT strategy;

	private Map<String, ParameterWidget<?>> parameters;

	private StrategyEditUI rule;

	private Map<String, EditUI> rules;

	public SWTStrategyUI(StrategyT strategy,
			Map<String, EditUI> strategiesRules, Composite parent)
			throws XmlException {
		this.strategy = strategy;

		SWTFactory factory = new SWTFactory();
		parameters = new HashMap<String, ParameterWidget<?>>();

		// initialize rules collection with global rules
		rules = new HashMap<String, EditUI>(strategiesRules);
		
		// use iterator to traverse in correct order
		Iterator<Object> it = new StrategyIterator(strategy);
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof ParameterT) {
				ParameterT parameter = (ParameterT) obj;
				parameters.put(parameter.getName(), factory.create(parent,
						parameter, SWT.NONE));
				// parameter flow rules that have an id should be included in the rules map
				if (parameter.isSetStateRule()) {
					StateRule stateRule = parameter.getStateRule();
					Edit edit = stateRule.getEdit();
					if (edit.isSetId()) {
						EditUI rule = RuleFactory.createRule(edit, rules);
						String id = edit.getId();
						rules.put(id, rule);
					}
				}
				
			} else if (obj instanceof StrategyLayout) {
				for (StrategyPanel panel : strategy.getStrategyLayout()
						.getStrategyPanelArray()) {
					parameters.putAll(factory.create(parent, panel, SWT.NONE));
				}
			}
		}

		// and add local rules
		for (Edit edit : strategy.getEditArray()) {
			String id = edit.getId();
			if (id != null) {
				EditUI rule = RuleFactory.createRule(edit, rules);
				rules.put(id, rule);
			} else {
				throw new XmlException("Strategy-scoped edit without id");
			}
		}

		// generate validation rules for StrategyEdit elements
		rule = new StrategyEditUI();
		for (StrategyEdit se : strategy.getStrategyEditArray()) {
			if (se.isSetEdit()) {
				Edit edit = se.getEdit();
				EditUI rule = RuleFactory.createRule(edit, rules);
				String id = edit.getId();
				if (id != null) 
					rules.put(id, rule);
				
				this.rule.putRule(se, rule);
			}

			// reference for a previously defined rule
			if (se.isSetEditRef()) {
				EditRefT editRef = se.getEditRef();
				String id = editRef.getId();
				rule.putRule(se, new ReferencedValidationRule(id));
			}
		}

		// strategy flow rules that have an id (thus are going to be referenced)
		// are included in the rules map
		for (StateGroup stateGroup : strategy.getStateGroupArray()) {
			for (Field field : stateGroup.getFieldArray()){
				if (field.isSetEdit()) {
					Edit edit = field.getEdit();
					if (edit.isSetId()) {
						EditUI rule = RuleFactory.createRule(edit, rules);
						String id = edit.getId();
						rules.put(id, rule);
					}
				}
			}
			for (EnumItem ei : stateGroup.getEnumItemArray()){
				if (ei.isSetEdit()) {
					Edit edit = ei.getEdit();
					if (edit.isSetId()) {
						EditUI rule = RuleFactory.createRule(edit, rules);
						String id = edit.getId();
						rules.put(id, rule);
					}
				}
			}
		}
		
		
		// TODO handle flows

		/*for (StateGroup stateGroup : strategy.getStateGroupArray()) {
			for (Field field : stateGroup.getFieldArray()){
			}
			for (EnumItem ei : stateGroup.getEnumItemArray()){
			}
		}

		ParameterWidget<?> testWidget = parameters.get("Lambda");
		
		if (testWidget != null) {
			
			ParameterT testParameter = testWidget.getParameter();
			if (testParameter.isSetStateRule()) {
				StateRule stateRule = testParameter.getStateRule();
				Edit edit = stateRule.getEdit();
				String testField = edit.getField();
				ParameterWidget<?> targetParameterWidget = parameters.get(testField);
				StateRuleUI flowStateRule = new StateRuleUI(stateRule, parameters, rules);
				targetParameterWidget.generateStateRule(flowStateRule);
			}
			
		}*/
		
	}

	@Override
	public void validate() throws ValidationException, XmlException {
		if (rule != null) {
			// delegate validation, passing all global and local rules as
			// context information, and all my parameters
			rule.validate(rules, parameters);
		} else {
			logger.info("No validation rule defined for strategy \"{}\"",
					strategy.getName());
		}
	}

}
