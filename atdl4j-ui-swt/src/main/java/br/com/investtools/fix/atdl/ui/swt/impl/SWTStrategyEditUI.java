package br.com.investtools.fix.atdl.ui.swt.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class SWTStrategyEditUI {

	private Map<StrategyEdit, EditUI> rules;

	private List<EditUI> requiredFieldRules;

	public void addRequiredFieldRule(EditUI editUI) {
		requiredFieldRules.add(editUI);
	}

	public Collection<EditUI> getRules() {
		return rules.values();
	}

	public SWTStrategyEditUI() {
		this.rules = new HashMap<StrategyEdit, EditUI>();
		this.requiredFieldRules = new ArrayList<EditUI>();
	}

	public void putRule(StrategyEdit strategyEdit, EditUI rule) {
		this.rules.put(strategyEdit, rule);
	}

	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterUI<?>> widgets) throws ValidationException,
			XmlException {

		for (EditUI requiredFieldRule : requiredFieldRules) {
			try {
				requiredFieldRule.validate(rules, widgets);
			} catch (ValidationException e) {
				ParameterT parameter = e.getWidget().getParameter();
				String text = getText(parameter);
				throw new ValidationException(e.getWidget(), "Field \"" + text
						+ "\" is required.");
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

	private String getText(ParameterT parameter) {
		return parameter.getUiRep() != null ? parameter.getUiRep() : parameter
				.getName();
	}

}
