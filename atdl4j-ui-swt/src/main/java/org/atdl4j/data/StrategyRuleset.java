package org.atdl4j.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.validation.StrategyEditT;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;

public class StrategyRuleset {

	private Map<StrategyEditT, ValidationRule> refRules;

	private List<ValidationRule> requiredFieldRules;

	private List<ValidationRule> patternRules;

	public void addRequiredFieldRule(ValidationRule editUI) {
		requiredFieldRules.add(editUI);
	}

	public void addPatternRule(ValidationRule editUI) {
		patternRules.add(editUI);
	}

	public StrategyRuleset() {
		this.refRules = new HashMap<StrategyEditT, ValidationRule>();
		this.requiredFieldRules = new ArrayList<ValidationRule>();
		this.patternRules = new ArrayList<ValidationRule>();
	}

	public void putRefRule(StrategyEditT strategyEdit, ValidationRule rule) {
		this.refRules.put(strategyEdit, rule);
	}

	public void validate(Map<String, ValidationRule> refRules,
			Map<String, ControlUI<?>> parameters) throws ValidationException,
			JAXBException {

		for (ValidationRule requiredFieldRule : requiredFieldRules) {
			try {
				requiredFieldRule.validate(refRules, parameters);
			} catch (ValidationException e) {
				ControlUI<?> target = e.getTarget();
				String name = target.getParameter().getName();
				throw new ValidationException(target, "Parameter \"" + name
						+ "\" is required.");
			}
		}

		for (ValidationRule patternRule : patternRules) {
			try {
				patternRule.validate(refRules, parameters);
			} catch (ValidationException e) {
				ControlUI<?> target = e.getTarget();
				String name = target.getParameter().getName();
				String type = target.getClass().toString();
				throw new ValidationException(target, "Field \"" + name
						+ "\" of type " + type
						+ " does not follow the required pattern.");
			}
		}

		for (Entry<StrategyEditT, ValidationRule> entry : this.refRules
				.entrySet()) {
			StrategyEditT strategyEdit = entry.getKey();
			ValidationRule rule = entry.getValue();
			try {
				rule.validate(refRules, parameters);
			} catch (ValidationException e) {
				throw new ValidationException(e.getTarget(), strategyEdit
						.getErrorMessage());
			}
		}
	}

	/*
	 * private String getText(ControlT control) { return control.getUiRep() !=
	 * null ? control.getUiRep() : parameter .getName(); }
	 */

}
