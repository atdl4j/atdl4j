package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Map;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

/**
 * Models a edit rule or a strategyEdit rule.
 * 
 * @author renato.gallart
 * 
 */
public interface ValidationRule {

	/**
	 * Called by the application to validate the user input. Throws a
	 * ValidationException if input is not valid according to the rules.
	 * 
	 * @param rules
	 * @throws ValidationException
	 */
	public void validate(StrategyEdit strategyEdit,
			Map<String, ValidationRule> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException;

}
