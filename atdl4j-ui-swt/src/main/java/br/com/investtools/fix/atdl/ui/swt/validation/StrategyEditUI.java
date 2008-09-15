package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class StrategyEditUI {

	private Map<StrategyEdit, EditUI> rules;

	public Collection<EditUI> getRules() {
		return rules.values();
	}

	public StrategyEditUI() {
		this.rules = new HashMap<StrategyEdit, EditUI>();
	}

	public void putRule(StrategyEdit strategyEdit, EditUI rule) {
		this.rules.put(strategyEdit, rule);
	}

	public void validate(StrategyEdit strategyEdit,
			Map<String, EditUI> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException {
	}

	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException, XmlException {
		for (Entry<StrategyEdit, EditUI> entry : this.rules.entrySet()) {
			StrategyEdit strategyEdit = entry.getKey();
			EditUI rule = entry.getValue();
			try {
				rule.validate(rules, widgets);
			} catch (ValidationException e) {
				throw new ValidationException(e.getWidget(), strategyEdit.getErrorMessage());
			}
			
		}
	}

}
