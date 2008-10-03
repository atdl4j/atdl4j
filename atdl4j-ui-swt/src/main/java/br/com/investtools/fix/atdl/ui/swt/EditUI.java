package br.com.investtools.fix.atdl.ui.swt;

import java.util.Map;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;

/**
 * Models a edit rule or a strategyEdit rule.
 * 
 * @author renato.gallart
 * 
 */
public interface EditUI {

	/**
	 * Called by the application to validate the user input. Throws a
	 * ValidationException if input is not valid according to the rules.
	 * 
	 * @param rules
	 * @throws ValidationException
	 * @throws XmlException
	 */
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterUI<?>> widgets) throws ValidationException,
			XmlException;

}
