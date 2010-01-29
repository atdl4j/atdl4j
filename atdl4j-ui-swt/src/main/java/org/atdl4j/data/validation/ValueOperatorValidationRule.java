package org.atdl4j.data.validation;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.flow.StateRuleT;
import org.atdl4j.atdl.validation.OperatorT;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;

/**
 * Validator that validates input against a constant value.
 * 
 * @author renato.gallart
 */
public class ValueOperatorValidationRule extends AbstractOperatorValidationRule {

	private String field;

	private OperatorT operator;

	private String value;

	private Object parent; // Can be either StrategyEdit or StateRule

	public ValueOperatorValidationRule(String field, OperatorT operator,
			String value, Object parent) {
		this.field = field;
		this.operator = operator;
		this.value = value;
		this.parent = parent;
	}

	public void validate(Map<String, ValidationRule> refRules,
			Map<String, ControlUI<?>> targets) throws ValidationException,
			JAXBException {

		// get the widget from context using field name
		ControlUI<?> target = targets.get(field);
		if (target == null) {
			throw new JAXBException("No parameter defined for field \"" + field
					+ "\" in this context");
		}

		Object fieldValue = parent instanceof StateRuleT ? target
				.getControlValueAsComparable() : target
				.getParameterValueAsComparable();
		Object v = value != null ? target.convertStringToComparable(value)
				: null;
		validateValues(target, fieldValue, operator, v);
	}

	public String getField() {
		return field;
	}
}
