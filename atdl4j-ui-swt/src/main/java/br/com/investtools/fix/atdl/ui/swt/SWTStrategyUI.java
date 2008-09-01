package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;
import br.com.investtools.fix.atdl.ui.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.validation.RootValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ValidationRule;
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

	private RootValidationRule rule;

	private Map<String, ValidationRule> rules;

	public SWTStrategyUI(StrategyT strategy,
			Map<String, ValidationRule> strategiesRules, Composite parent)
			throws XmlException {
		this.strategy = strategy;

		SWTFactory factory = new SWTFactory();
		parameters = new HashMap<String, ParameterWidget<?>>();

		// TODO: use iterator to traverse in correct order

		// build panels
		for (StrategyPanel panel : strategy.getStrategyLayout()
				.getStrategyPanelArray()) {
			parameters.putAll(factory.create(parent, panel, SWT.NONE));
		}

		// build parameters
		for (ParameterT parameter : strategy.getParameterArray()) {
			parameters.put(parameter.getName(), factory.create(parent,
					parameter, SWT.NONE));
		}

		// initialize rules collection with global rules
		rules = new HashMap<String, ValidationRule>(strategiesRules);
		
		// and add local rules
		for (Edit edit : strategy.getEditArray()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = RuleFactory.createRule(edit);
				rules.put(id, rule);
			} else {
				throw new XmlException("Strategy-scoped edit without id");
			}
		}

		// generate validation rules for StrategyEdit elements
		rule = new RootValidationRule();
		for (StrategyEdit se : strategy.getStrategyEditArray()) {
			if (se.isSetEdit()) {
				rule.putRule(se, RuleFactory.createRule(se.getEdit()));
			}

			// reference for a previously defined rule
			if (se.isSetEditRef()) {
				EditRefT editRef = se.getEditRef();
				String id = editRef.getId();
				rule.putRule(se, new ReferencedValidationRule(id));
			}
		}

		// TODO: handle flows
	}

	@Override
	public void validate() throws ValidationException {
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
