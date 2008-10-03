package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Map;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;

public class ReferencedValidationRule implements EditUI {

	private String ref;

	public ReferencedValidationRule(String ref) {
		this.ref = ref;
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterUI<?>> widgets) throws ValidationException,
			XmlException {
		EditUI rule = rules.get(ref);
		if (rule != null) {
			// delegate for referenced rule
			rule.validate(rules, widgets);
		} else {
			throw new ValidationException(null, "Rule referenced by \"" + ref
					+ "\" not found");
		}
	}

}
