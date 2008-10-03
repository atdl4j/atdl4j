package br.com.investtools.fix.atdl.ui.swt.validation;

import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;

/**
 * Validation rule based on a single Parameter.
 * 
 * @author renato.gallart
 * 
 */
public interface ParameterValidationRule extends EditUI {

	public ParameterUI<?> getParameter();

}
