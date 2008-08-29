package br.com.investtools.fix.atdl.ui.swt.test.candidates;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

/**
 * Validation rule based on a single Parameter.
 * 
 * @author renato.gallart
 * 
 */
public interface ParameterValidationRule extends ValidationRule {

	public ParameterWidget<?> getParameter();

}
