package org.atdl4j.data.validation;

import java.util.Map;

import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.Atdl4jWidget;

/**
 * @author renato.gallart
 */
public class ReferencedValidationRule
		implements ValidationRule
{

	private String ref;

	public ReferencedValidationRule(String ref)
	{
		this.ref = ref;
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, Atdl4jWidget<?>> targets) 
		throws ValidationException
	{
		ValidationRule rule = refRules.get( ref );
		if ( rule != null )
		{
			// delegate for referenced rule
			rule.validate( refRules, targets );
		}
		else
		{
			throw new ValidationException( null, "Rule referenced by \"" + ref + "\" not found" );
		}
	}

}
