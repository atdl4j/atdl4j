package br.com.investtools.fix.atdl.ui.swt.test.candidates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class RootValidationRule implements ValidationRule {

	private Map<StrategyEdit, ValidationRule> rules;

	public Collection<ValidationRule> getRules() {
		return rules.values();
	}

	public RootValidationRule() {
		this.rules = new HashMap<StrategyEdit, ValidationRule>();
	}

	public void putRule(StrategyEdit strategyEdit, ValidationRule rule) {
		this.rules.put(strategyEdit, rule);
	}

	@Override
	public void validate(StrategyEdit strategyEdit,
			Map<String, ValidationRule> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException {
	}

	public void validate(Map<String, ValidationRule> rules,
			Map<String, ParameterWidget<?>> widgets) {
		for (Entry<StrategyEdit, ValidationRule> entry : this.rules.entrySet()) {
			entry.getValue().validate(entry.getKey(), rules, widgets);
		}
	}

}
