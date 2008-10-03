package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Map;

import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT.Enum;

/**
 * Validator that validates input against another existing field.
 * 
 * @author renato.gallart
 * 
 */
public class Field2OperatorValidationRule extends
		AbstractOperatorValidationRule {

	private String field;

	private Enum operator;

	private String field2;

	public Field2OperatorValidationRule(String field, Enum operator,
			String field2) {
		this.field = field;
		this.operator = operator;
		this.field2 = field2;
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterUI<?>> widgets) throws ValidationException {

		// get the widget from context using field name
		ParameterUI<?> widget = widgets.get(field);
		if (widget == null) {
			throw new ValidationException(null,
					"No widget defined for field \"" + field
							+ "\" in this context");
		}
		Object fieldValue = widget.getValue();

		// get the widget from context using field2 name
		ParameterUI<?> widget2 = widgets.get(field2);
		if (widget2 == null) {
			throw new ValidationException(null,
					"No widget defined for field2 \"" + field2
							+ "\" in this context");
		}
		Object fieldValue2 = widget2.getValue();

		// compare both values
		validateValues(widget, fieldValue, operator, fieldValue2);
	}

}
