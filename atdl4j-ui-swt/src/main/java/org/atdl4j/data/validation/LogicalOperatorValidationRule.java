package org.atdl4j.data.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.validation.LogicOperatorT;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;

/**
 * ValidationRule that behaves as a composite, using on of the following
 * operators: AND, OR, XOR, NOT.
 * 
 * @author renato.gallart
 * @author john.shields
 */
public class LogicalOperatorValidationRule implements ValidationRule {

	private LogicOperatorT operator;

	private List<ValidationRule> rules;

	// private Object parent; // Can be either StrategyEdit or StateRule

	public LogicalOperatorValidationRule(LogicOperatorT operator, Object parent) {
		this.operator = operator;
		this.rules = new ArrayList<ValidationRule>();
		// this.parent = parent;
	}

	public List<ValidationRule> getRules() {
		return rules;
	}

	public void addRule(ValidationRule rule) {
		this.rules.add(rule);
	}

	public void validate(Map<String, ValidationRule> rules,
			Map<String, ControlUI<?>> targets) throws ValidationException,
			JAXBException {

		ValidationException ex = null;
		boolean valid = false;

		switch (operator) {
		case OR:

			// OR - new interpretation
			for (ValidationRule rule : this.rules) {
				try {
					rule.validate(rules, targets);
					valid = true;
					break;
				} catch (ValidationException e) {
					ex = e;
				}
			}
			if (!valid) {
				throw ex;
			}

			break;

		case AND:

			// AND - behaves as short circuit
			for (ValidationRule rule : this.rules) {
				try {
					rule.validate(rules, targets);
				} catch (ValidationException e) {
					throw e;
				}
			}

			break;

		case XOR:

			boolean state = false;
			// XOR - defined as true if exactly one argument is true
			// TODO: this is overly complex
			for (ValidationRule rule : this.rules) {
				try {
					rule.validate(rules, targets);
					if (state) {
						valid = false;
						ControlUI<?> parameter = getParameterFromRule(rule);
						throw new ValidationException(parameter);
					}
					state = true;
					valid = true;
				} catch (ValidationException e) {
					ex = e;
					if (state)
						break;
				}
			}
			if (!valid) {
				throw ex;
			}

			break;

		case NOT:

			// NOT - interpreted as 'NOR'; will throw exception if any of the
			// rules validate as true
			for (ValidationRule rule : this.rules) {
				try {
					rule.validate(rules, targets);
					// if above does not trigger an exception
					ControlUI<?> parameter = getParameterFromRule(rule);
					throw new ValidationException(parameter);

				} catch (ValidationException e) {
					ex = e;
				}
			}

			break;

		}

	}

	private ControlUI<?> getParameterFromRule(ValidationRule rule) {
		ControlUI<?> parameter = null;
		if (rule instanceof ParameterValidationRule) {
			parameter = ((ParameterValidationRule) rule).getParameter();
		}
		return parameter;
	}
}
