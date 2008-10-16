package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;

/**
 * Validator that validates input against a regular expression.
 * 
 * @author renato.gallart
 * 
 */
public class PatternValidationRule implements EditUI {

	private String field;

	private String pattern;

	public PatternValidationRule(String field, String pattern) {
		this.field = field;
		this.pattern = pattern;
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterUI<?>> widgets) throws ValidationException,
			XmlException {

		// get the widget from context using field name
		ParameterUI<?> widget = widgets.get(field);
		if (widget == null) {
			throw new XmlException("No widget defined for field \"" + field
					+ "\" in this context");
		}

		String value = widget.getValue().toString();
		if (!Pattern.matches(this.pattern, value)) {
			throw new ValidationException(widget);
		}
	}
}
