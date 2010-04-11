package org.atdl4j.data.validation;

import java.math.BigInteger;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.ControlUI;

/**
 * Validator that validates input against a min/max length value.
 * 
 * @author Scott Atwell
 */
public class LengthValidationRule
	implements ValidationRule
{
	private static final Logger logger = Logger.getLogger( LengthValidationRule.class );

	private String field;

	private OperatorT operator;

	private BigInteger length;

	private Object parent; // Can be either StrategyEdit or StateRule

	public LengthValidationRule(String field, OperatorT operator, BigInteger length, Object parent)
	{
		this.field = field;
		this.operator = operator;
		this.length = length;
		this.parent = parent;

		String tempMsg = "ValueOperatorValidationRule constructor: field: " + field + " operator: " + operator + " length: " + length + " parent: "
				+ parent;
		logger.debug( tempMsg );
		logger.trace( tempMsg, new Exception( "Stack trace" ) );
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, ControlUI<?>> targets) 
		throws ValidationException
	{
		// get the widget from context using field name
		ControlUI<?> target = targets.get( field );
		if ( target == null )
		{
			String tempMsg = "No parameter defined for field \"" + field + "\" in this context (ValueOperatorValidationRule) field: " + field
					+ " operator: " + operator + " length: " + length + " parent: " + parent + " refRules: " + refRules;
			String tempMsg2 = tempMsg + " targets: " + targets;
			logger.debug( tempMsg2 );
			logger.trace( tempMsg2, new Exception( "Stack trace" ) );

			throw new ValidationException( null, tempMsg );
		}

		String fieldValue = target.getParameterFixWireValue();
		if ( fieldValue != null )
		{
			validateLength( target, fieldValue, operator );
		}
	}

	public String getField()
	{
		return field;
	}

	protected void validateLength(ControlUI<?> aTarget, String aValue, OperatorT aOperator)
	{
		switch ( aOperator )
		{
			case NE :
				if ( aValue.length() == length.intValue() )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " NE " + length.intValue() + "]" );
				}
				break;

			case EX :
				if ( ( aValue != null ) && ( aValue.length() != length.intValue() ) )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " EX " + length.intValue() + "]" );
				}
				break;

			case LT :
				if ( aValue.length() >= length.intValue() )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " LT " + length.intValue() + "]" );
				}
				break;

			case LE :
				if ( aValue.length() > length.intValue() )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " LE " + length.intValue() + "]" );
				}
				break;

			case GT :
				if ( aValue.length() <= length.intValue() )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " GT " + length.intValue() + "]" );
				}
				break;

			case GE :
				if ( aValue.length() < length.intValue() )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " GE " + length.intValue() + "]" );
				}
				break;

			case EQ :
				if ( aValue.length() != length.intValue() )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " EQ " + length.intValue() + "]" );
				}
				break;

			case NX :
				if ( ( aValue != null ) && ( aValue.length() == length.intValue() ) )
				{
					throw new ValidationException( aTarget, "Rule tested: [for: " + aValue + " length: " + aValue.length() + " NX " + length.intValue() + "]" );
				}
				break;

			default:
				// Supposed to never happen, since the schema enforces an enumerated
				// base restriction.
				logger.error( "Invalid operator." );
				break;
		}
	}
}
