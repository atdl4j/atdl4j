package org.atdl4j.data.validation;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.Atdl4jWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for ValidationRule.
 * 
 * @author renato.gallart
 */
public abstract class AbstractOperatorValidationRule
		implements ValidationRule
{
	private static final Logger logger = LoggerFactory.getLogger( AbstractOperatorValidationRule.class );

	@SuppressWarnings("unchecked")
	protected void validateValues(Atdl4jWidget<?> target, Object value1, OperatorT operator, Object value2) throws ValidationException
	{
		// 3/11/2010 Scott Atwell added to normalize DateTime comparisons 
		// -- Work-around to 'handle' joda's DateTime.equals() failing if timezones are expressed differently 
		// -- for same underlying time in millis.  DateTime.isEqual() behaves as expected
		// -- using DateTimeZone.getDefault() will result in any values displayed in error messages to be in local zone (with "-0600" suffix if US CST) 
		if ( value1 instanceof ZonedDateTime )
		{
			value1 = ((ZonedDateTime) value1).withZoneSameInstant( ZoneId.systemDefault() );
		}
		
		if ( value2 instanceof ZonedDateTime )
		{
			value2 = ((ZonedDateTime) value2).withZoneSameInstant( ZoneId.systemDefault() );
		}

		switch ( operator )
		{
			case NE :
				if ( value1 == value2 || ( value2 != null && value2.equals( value1 ) ) )
				{
					throw new ValidationException( target, "Rule tested: [" + value1 + " NE " + value2 + "]" );
				}
				break;

			case EX :
				if ( value1 == null || "".equals( value1.toString() ) )
				{
					throw new ValidationException( target, "Rule tested: [" + value1 + " EX]" );
				}
				break;

			case LT :
				if ( value1 instanceof Comparable )
				{
					Comparable<Object> c = (Comparable<Object>) value1;
					if ( c.compareTo( value2 ) >= 0 )
					{
						throw new ValidationException( target, "Rule tested: [" + value1 + " LT " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + (value1 != null ? (value1 + " class: " + value1.getClass()) : "value is null") + "]" );
				}
				break;

			case LE :
				if ( value1 instanceof Comparable )
				{
					Comparable<Object> c = (Comparable<Object>) value1;
					if ( c.compareTo( value2 ) > 0 )
					{
						throw new ValidationException(target, "Rule tested: [" + value1 + " LE " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + (value1 != null ? (value1 + " class: " + value1.getClass()) : "value is null") + "]" );
				}
				break;

			case GT :
				if ( value1 instanceof Comparable )
				{
					Comparable<Object> c = (Comparable<Object>) value1;
					if ( c.compareTo( value2 ) <= 0 )
					{
						throw new ValidationException(target, "Rule tested: [" + value1 + " GT " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + (value1 != null ? (value1 + " class: " + value1.getClass()) : "value is null") + "]" );
				}
				break;

			case GE :
				if ( value1 instanceof Comparable )
				{
					Comparable<Object> c = (Comparable<Object>) value1;
					if ( c.compareTo( value2 ) < 0 )
					{
						throw new ValidationException(target, "Rule tested: [" + value1 + " GE " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + (value1 != null ? (value1 + " class: " + value1.getClass()) : "value is null") + "]" );
				}
				break;

			case EQ :
				if ( !value2.equals( value1 ) )
				{
					throw new ValidationException(target, "Rule tested: [" + value1 + " EQ " + value2 + "]" );
				}
				break;

			case NX :
				if ( value1 != null && !"".equals( value1.toString() ) )
				{
					throw new ValidationException(target, "Rule tested: [" + value1 + " NX]" );
				}
				break;

			default:
				// Supposed to never happen, since the schema enforces an enumerated
				// base restriction.
				logger.error( "Invalid operator: {}", operator );
				break;
		}
	}
}
