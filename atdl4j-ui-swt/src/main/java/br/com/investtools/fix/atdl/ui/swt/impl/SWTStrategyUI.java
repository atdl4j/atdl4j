package br.com.investtools.fix.atdl.ui.swt.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.investtools.fix.atdl.core.xmlbeans.MultipleCharValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleStringValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.core.xmlbeans.UseT;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateGroupDocument.StateGroup;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateGroupDocument.StateGroup.EnumItem;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateGroupDocument.StateGroup.TargetParameter;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateRuleDocument.StateRule;
import br.com.investtools.fix.atdl.iterators.StrategyIterator;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyLayoutDocument.StrategyLayout;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;
import br.com.investtools.fix.atdl.ui.FIXMessageBuilder;
import br.com.investtools.fix.atdl.ui.impl.PlainFIXMessageBuilder;
import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;
import br.com.investtools.fix.atdl.ui.swt.impl.SWTStateGroupUI.EnumItemUI;
import br.com.investtools.fix.atdl.ui.swt.impl.SWTStateGroupUI.TargetParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.RuleFactory;
import br.com.investtools.fix.atdl.ui.swt.validation.PatternValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ReferencedValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ValueOperatorValidationRule;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditRefT;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT;
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

	@Override
	public StrategyT getStrategy() {
		return strategy;
	}

	private Map<String, ParameterUI<?>> parameters;

	private SWTStrategyEditUI rule;

	private Map<String, EditUI> rules;

	public SWTStrategyUI(StrategyT strategy,
			Map<String, EditUI> strategiesRules, Composite parent)
			throws XmlException {
		this.strategy = strategy;

		SWTFactory factory = new SWTFactory();
		parameters = new HashMap<String, ParameterUI<?>>();

		// initialize rules collection with global rules
		rules = new HashMap<String, EditUI>(strategiesRules);

		rule = new SWTStrategyEditUI();

		// use iterator to traverse in correct order
		Iterator<Object> it = new StrategyIterator(strategy);
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof ParameterT) {
				ParameterT parameter = (ParameterT) obj;
				parameters.put(parameter.getName(), factory.create(parent,
						parameter, SWT.NONE));

			} else if (obj instanceof StrategyLayout) {
				for (StrategyPanel panel : strategy.getStrategyLayout()
						.getStrategyPanelArray()) {
					parameters.putAll(factory.create(parent, panel, SWT.NONE));
				}
			}
		}

		for (ParameterUI<?> parameterWidget : parameters.values()) {

			// parameter state rules that have an id should be included in
			// the rules map
			ParameterT parameter = parameterWidget.getParameter();

			if (parameter.getStateRuleArray() != null) {
				for (StateRule stateRule : parameter.getStateRuleArray()) {
					Edit edit = stateRule.getEdit();
					if (edit.isSetId()) {
						EditUI rule = RuleFactory.createRule(edit, rules);
						String id = edit.getId();
						rules.put(id, rule);
					}
				}
			}

			// required fields should be validated as well
			if (parameter.isSetUse()) {
				if (parameter.getUse().equals(UseT.REQUIRED)) {
					EditUI requiredFieldRule = new ValueOperatorValidationRule(
							parameter.getName(), OperatorT.NX, null);
					rule.addRequiredFieldRule(requiredFieldRule);
				}
			}

			// validate types based on patterns
			if (parameter instanceof MultipleCharValueT) {
				MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
				EditUI patternBasedRule = new PatternValidationRule(
						multipleCharValueT.getName(), "\\S?(\\s\\S?)*");
				rule.addPatternRule(patternBasedRule);

			} else if (parameter instanceof MultipleStringValueT) {
				MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
				EditUI patternBasedRule = new PatternValidationRule(
						multipleStringValueT.getName(), "\\S+(\\s\\S+)*");
				rule.addPatternRule(patternBasedRule);

			}

			if (parameter.isSetUse()) {
				if (parameter.getUse().equals(UseT.REQUIRED)) {
					EditUI requiredFieldRule = new ValueOperatorValidationRule(
							parameter.getName(), OperatorT.NX, null);
					rule.addRequiredFieldRule(requiredFieldRule);
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

		// strategy state rules that have an id (thus are going to be
		// referenced)
		// are included in the rules map
		for (StateGroup stateGroup : strategy.getStateGroupArray()) {
			for (TargetParameter targetParameter : stateGroup
					.getTargetParameterArray()) {
				if (targetParameter.isSetEdit()) {
					Edit edit = targetParameter.getEdit();
					if (edit.isSetId()) {
						EditUI rule = RuleFactory.createRule(edit, rules);
						String id = edit.getId();
						rules.put(id, rule);
					}
				}
			}
			for (EnumItem ei : stateGroup.getEnumItemArray()) {
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

		// look for fields and enumItens state rules under the strategy's
		// state groups
		if (strategy.getStateGroupArray() != null) {

			for (StateGroup stateGroup : strategy.getStateGroupArray()) {

				SWTStateGroupUI stateGroupUI = new SWTStateGroupUI();

				for (TargetParameter targetParameter : stateGroup
						.getTargetParameterArray()) {
					ParameterUI<?> affectedWidget = parameters
							.get(targetParameter.getName());
					Edit edit = targetParameter.getEdit();
					if (affectedWidget == null)
						throw new XmlException(
								"Error generating a State Group Rule => Parameter: "
										+ edit.getField()
										+ " does not exist in Strategy: "
										+ strategy.getName());
					ParameterUI<?> targetParameterWidget = parameters.get(edit
							.getField());
					if (targetParameterWidget == null)
						throw new XmlException(
								"Error generating a State Rule => Parameter: "
										+ edit.getField()
										+ " does not exist in Strategy: "
										+ strategy.getName());
					TargetParameterUI fieldUI = stateGroupUI.new TargetParameterUI(
							affectedWidget, targetParameter, parameters, rules);
					targetParameterWidget.generateStateRuleListener(fieldUI);
					if (edit.isSetField2()) {
						ParameterUI<?> targetParameterWidget2 = parameters
								.get(edit.getField2());
						if (targetParameterWidget2 == null)
							throw new XmlException(
									"Error generating a State Rule => Parameter: "
											+ edit.getField2()
											+ " does not exist in Strategy: "
											+ strategy.getName());
						targetParameterWidget2
								.generateStateRuleListener(fieldUI);
					}

					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
					fieldUI.handleEvent(null);

				}
				for (EnumItem ei : stateGroup.getEnumItemArray()) {

					ParameterUI<?> affectedWidget = parameters.get(ei
							.getParameterName());
					Edit edit = ei.getEdit();
					if (affectedWidget == null)
						throw new XmlException(
								"Error generating a State Group Rule => Parameter: "
										+ edit.getField()
										+ " does not exist in Strategy: "
										+ strategy.getName());

					/*
					 * if (!(affectedWidget instanceof
					 * MultiCheckBoxParameterWidget) && !(affectedWidget
					 * instanceof RadioButtonParameterWidget) ) throw new
					 * XmlException(
					 * "Error generating a State Group Rule => Parameter: " +
					 * edit.getField() +
					 * " should be either a MultiCheckBoxParameterWidget or a RadioButtonParameterWidget."
					 * );
					 */

					ParameterUI<?> targetParameterWidget = parameters.get(edit
							.getField());
					if (targetParameterWidget == null)
						throw new XmlException(
								"Error generating a State Rule => Parameter: "
										+ edit.getField()
										+ " does not exist in Strategy: "
										+ strategy.getName());
					EnumItemUI enumItemUI = stateGroupUI.new EnumItemUI(
							affectedWidget, ei, parameters, rules);
					targetParameterWidget.generateStateRuleListener(enumItemUI);
					if (edit.isSetField2()) {
						ParameterUI<?> targetParameterWidget2 = parameters
								.get(edit.getField2());
						if (targetParameterWidget2 == null)
							throw new XmlException(
									"Error generating a State Rule => Parameter: "
											+ edit.getField2()
											+ " does not exist in Strategy: "
											+ strategy.getName());
						targetParameterWidget2
								.generateStateRuleListener(enumItemUI);
					}

					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
					enumItemUI.handleEvent(null);

				}
			}

		}

		// look for state rules under the strategy's parameters
		if (strategy.getParameterArray() != null) {
			for (ParameterT parameter : strategy.getParameterArray()) {
				stateRuleGenerator(parameter);
			}
		}

		// look for state rules under the strategy panel's parameters
		if (strategy.isSetStrategyLayout()) {
			if (strategy.getStrategyLayout().getStrategyPanelArray() != null) {
				for (StrategyPanel panel : strategy.getStrategyLayout()
						.getStrategyPanelArray()) {
					parameterProcessor(panel);
				}
			}
		}
	}

	private void parameterProcessor(StrategyPanel panel) throws XmlException {

		if (panel.getStrategyPanelArray() != null) {
			for (StrategyPanel innerPanel : panel.getStrategyPanelArray()) {
				parameterProcessor(innerPanel);
			}
		}

		if (panel.getParameterArray() != null) {
			for (ParameterT parameter : panel.getParameterArray()) {
				stateRuleGenerator(parameter);
			}
		}
	}

	private void stateRuleGenerator(ParameterT parameter) throws XmlException {

		if (parameter.getStateRuleArray() != null) {

			for (StateRule stateRule : parameter.getStateRuleArray()) {

				Edit edit = stateRule.getEdit();
				ParameterUI<?> affectedWidget = parameters.get(parameter
						.getName());
				ParameterUI<?> targetParameterWidget = parameters.get(edit
						.getField());
				if (targetParameterWidget == null)
					throw new XmlException(
							"Error generating a State Rule => Parameter: "
									+ edit.getField()
									+ " does not exist in Strategy: "
									+ strategy.getName());
				SWTStateRuleUI stateRuleUI = new SWTStateRuleUI(affectedWidget,
						stateRule, parameters, rules);
				targetParameterWidget.generateStateRuleListener(stateRuleUI);
				if (edit.isSetField2()) {
					ParameterUI<?> targetParameterWidget2 = parameters.get(edit
							.getField2());
					if (targetParameterWidget2 == null)
						throw new XmlException(
								"Error generating a State Rule => Parameter: "
										+ edit.getField2()
										+ " does not exist in Strategy: "
										+ strategy.getName());
					targetParameterWidget2
							.generateStateRuleListener(stateRuleUI);
				}

				// run the state rule to check if any parameter needs to be
				// enabled/disabled or hidden/unhidden before being rendered
				stateRuleUI.handleEvent(null);

			}

		}
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

	@Override
	public String getFIXMessage() {
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		getFIXMessage(builder);
		return builder.getMessage();
	}

	@Override
	public void getFIXMessage(FIXMessageBuilder builder) {
		builder.onStart();
		for (ParameterUI<?> widget : parameters.values()) {
			widget.getFIXValue(builder);
		}
		builder.onEnd();
	}

	@Override
	public ParameterUI<?> getParameterWidget(String name) {
		return parameters.get(name);
	}

}
