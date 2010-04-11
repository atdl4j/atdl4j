package org.atdl4j.data;

import java.util.Map;

import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;


/**
 * Models a edit rule or a StrategyEdit rule.
 * 
 * @author renato.gallart
 */
public interface ValidationRule {

	/**
	 * Called by the application to validate the user input. Throws a
	 * ValidationException if input is not valid according to the rules.
	 * 
	 * @param rules
	 * @throws ValidationException
	 */
	public void validate(Map<String, ValidationRule> refRules, Map<String, ControlUI<?>> targets) 
		throws ValidationException;
}