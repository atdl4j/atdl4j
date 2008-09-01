package br.com.investtools.fix.atdl.ui.swt;

import java.util.Map;

import br.com.investtools.fix.atdl.ui.swt.validation.ValidationRule;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class ReferencedValidationRule implements ValidationRule {

	private String ref;

	public ReferencedValidationRule(String ref) {
		this.ref = ref;
	}

	@Override
	public void validate(StrategyEdit strategyEdit,
			Map<String, ValidationRule> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException {
		ValidationRule rule = rules.get(ref);
		if (rule != null) {
			// delegate for referenced rule
			rule.validate(strategyEdit, rules, widgets);
		} else {
			throw new ValidationException(null, "Rule referenced by \"" + ref
					+ "\" not found");
		}
	}

}
