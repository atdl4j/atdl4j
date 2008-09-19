package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class StrategyEditUI {

	private Map<StrategyEdit, EditUI> rules;

	private List<EditUI> requiredFieldRules;

	public void addRequiredFieldRule(EditUI editUI) {
		requiredFieldRules.add(editUI);
	}

	public Collection<EditUI> getRules() {
		return rules.values();
	}

	public StrategyEditUI() {
		this.rules = new HashMap<StrategyEdit, EditUI>();
		this.requiredFieldRules = new ArrayList<EditUI>();
	}

	public void putRule(StrategyEdit strategyEdit, EditUI rule) {
		this.rules.put(strategyEdit, rule);
	}

	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterWidget<?>> widgets)
			throws ValidationException, XmlException {

		for (EditUI requiredFieldRule : requiredFieldRules) {
			try {
				requiredFieldRule.validate(rules, widgets);
			} catch (ValidationException e) {
				String parameterName = e.getWidget().getParameter().getName();
				throw new ValidationException(e.getWidget(), "Field " + parameterName
						+ " is required.");
			}

		}

		for (Entry<StrategyEdit, EditUI> entry : this.rules.entrySet()) {
			StrategyEdit strategyEdit = entry.getKey();
			EditUI rule = entry.getValue();
			try {
				rule.validate(rules, widgets);
			} catch (ValidationException e) {
				throw new ValidationException(e.getWidget(), strategyEdit
						.getErrorMessage());
			}

		}
	}

}
