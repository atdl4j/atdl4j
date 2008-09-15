package br.com.investtools.fix.atdl.ui.swt;

import java.util.Map;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.validation.EditUI;

public class ReferencedValidationRule implements EditUI {

	private String ref;

	public ReferencedValidationRule(String ref) {
		this.ref = ref;
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException, XmlException {
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
