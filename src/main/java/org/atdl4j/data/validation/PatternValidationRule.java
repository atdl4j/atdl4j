package org.atdl4j.data.validation;

import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.Atdl4jWidget;

/**
 * Validator that validates input against a regular expression.
 * 
 * @author renato.gallart
 */
public class PatternValidationRule
		implements ValidationRule
{

	private static final Logger logger = LoggerFactory.getLogger( PatternValidationRule.class );

	private String field;

	private String pattern;

	public PatternValidationRule(String field, String pattern)
	{
		this.field = field;
		this.pattern = pattern;

		String tempMsg = "PatternValidationRule constructor: field: " + field + " pattern: " + pattern;
		logger.debug( tempMsg );
		logger.trace( tempMsg, new Exception( "Stack trace" ) );
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, Atdl4jWidget<?>> targets) 
		throws ValidationException
	{

		// get the widget from context using field name
		Atdl4jWidget<?> target = targets.get( field );
		if ( target == null )
		{
			String tempMsg = "No parameter defined for field \"" + field + "\" in this context (PatternValidationRule) field: " + field + " pattern: "
					+ pattern;
			logger.debug( tempMsg );
			logger.trace( tempMsg, new Exception( "Stack trace" ) );

			throw new ValidationException( null, tempMsg );
		}

		// PatternRules always validate against a parameter,
		// so no need to fetch control value
		String value = target.getParameterFixWireValue();
		if ( value != null && !Pattern.matches( this.pattern, value ) )
		{
			throw new ValidationException( target, "Rule tested: [" + value + " pattern match: " + this.pattern + "]" );
		}
	}
}
