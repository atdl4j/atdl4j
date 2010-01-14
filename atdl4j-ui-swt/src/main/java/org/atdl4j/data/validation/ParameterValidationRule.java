package org.atdl4j.data.validation;

import org.atdl4j.data.ValidationRule;
import org.atdl4j.ui.ControlUI;

/**
 * Validation rule based on a single Parameter.
 * 
 * @author renato.gallart
 */
public interface ParameterValidationRule extends ValidationRule {

	public ControlUI<?> getParameter();

}
