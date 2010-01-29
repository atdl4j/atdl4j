package org.atdl4j.data.validation;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.flow.StateRuleT;
import org.atdl4j.atdl.validation.OperatorT;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;

/**
 * Validator that validates input against another existing field.
 * 
 * @author renato.gallart
 * @author john.shields
 */
public class Field2OperatorValidationRule extends
		AbstractOperatorValidationRule {

	private String field;

	private OperatorT operator;

	private String field2;

	private Object parent; // Can be either StrategyEdit or StateRule

	public Field2OperatorValidationRule(String field, OperatorT operator,
			String field2, Object parent) {
		this.field = field;
		this.operator = operator;
		this.field2 = field2;
		this.parent = parent;
	}

	public void validate(Map<String, ValidationRule> rules,
			Map<String, ControlUI<?>> targets) throws ValidationException,
			JAXBException {

		// get the widget from context using field name
		ControlUI<?> target = targets.get(field);
		if (target == null) {
			throw new JAXBException("No parameter defined for field \"" + field
					+ "\" in this context");
		}
		Object fieldValue = parent instanceof StateRuleT ? target
				.getControlValue() : target.getParameterValue();

		// get the widget from context using field2 name
		ControlUI<?> target2 = targets.get(field2);
		if (target2 == null) {
			throw new JAXBException("No parameter defined for field2 \""
					+ field2 + "\" in this context");
		}
		Object fieldValue2 = parent instanceof StateRuleT ? target2
				.getControlValue() : target2.getParameterValue();

		// compare both values
		validateValues(target, fieldValue, operator, fieldValue2);
	}

	public String getField() {
		return field;
	}

	public String getField2() {
		return field2;
	}
}
