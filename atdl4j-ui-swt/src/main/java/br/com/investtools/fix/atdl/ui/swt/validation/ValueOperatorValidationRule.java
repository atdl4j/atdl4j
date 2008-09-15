package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Map;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT.Enum;

/**
 * Validator that validates input against a constant value.
 * 
 * @author renato.gallart
 */
public class ValueOperatorValidationRule extends AbstractOperatorValidationRule {

	private String field;

	private Enum operator;

	private String value;

	public ValueOperatorValidationRule(String field, Enum operator, String value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException, XmlException {

		// get the widget from context using field name
		ParameterWidget<?> widget = widgets.get(field);
		if (widget == null) {
			throw new ValidationException(null,
					"No widget defined for field \"" + field
							+ "\" in this context");
		}

		Object fieldValue = widget.getValue();
		Object v = value != null ? widget.convertValue(value) : null;
		validateValues(widget, fieldValue, operator, v);
	}

}
