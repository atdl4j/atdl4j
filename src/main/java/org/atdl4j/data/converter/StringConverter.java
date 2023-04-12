package org.atdl4j.data.converter;

import java.math.BigDecimal;

import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.ParameterT;

public class StringConverter
		extends AbstractTypeConverter<String>
{
	public StringConverter(ParameterT aParameter)
	{
		super( aParameter );
	}

	public StringConverter(ParameterTypeConverter<?> aParameterTypeConverter)
	{
		super( aParameterTypeConverter );
	}
	
	private static String invertOnWire(String text)
	{
		StringBuilder invertedString = new StringBuilder();

		int startIndex = text.lastIndexOf( " " );
		int endIndex = text.length();

		do
		{
			invertedString.append( text.substring( startIndex + 1, endIndex ) );
			if ( startIndex == -1 )
			{
				return invertedString.toString();
			}
			else
			{
				invertedString.append( " " );
			}
			endIndex = startIndex;
			startIndex = ( text.substring( 0, endIndex ) ).lastIndexOf( " " );
		}
		while ( endIndex != -1 );

		return invertedString.toString();
	}

	protected String convertValueToComparable(Object value)
	{
		return ( value == null || "".equals( value ) ) ? null : value.toString();
	}

	/* Handles de-inverting FixWireValue for Parameter value if applicable.
	 * @see org.atdl4j.data.ParameterTypeConverter#convertFixWireValueToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertFixWireValueToParameterValue(String aFixWireValue)
	{
		String str = convertStringToParameterValue( aFixWireValue );
		
		if ( ( getParameter() instanceof MultipleCharValueT && ( (MultipleCharValueT) getParameter() ).isInvertOnWire() )
		|| ( getParameter() instanceof MultipleStringValueT && ( (MultipleStringValueT) getParameter() ).isInvertOnWire() ) )
		{
			return invertOnWire( str );
		}
		
		return str;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterStringToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertParameterStringToParameterValue(String aParameterString)
	{
		return convertStringToParameterValue( aParameterString );
	}

	/**
	 * Supports either FixWireValue or ParameterString values
	 * @param aValue
	 * @return
	 */
	protected String convertStringToParameterValue(String aValue)
	{
		if ( ( aValue != null ) && ( ! "".equals( aValue ) ) )
		{
			return aValue;
		}
		else
		{
			return null;
		}
	}
	
	/* Handles inverting ParameterValue when building FixWireValue if applicable.
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToFixWireValue(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToFixWireValue(Object aParameterValue)
	{
		String str = convertParameterValueToParameterComparable( aParameterValue );
		if ( str != null )
		{
			if ( getParameter() instanceof MultipleCharValueT && ( (MultipleCharValueT) getParameter() ).isInvertOnWire() )
			{
				return invertOnWire( str );
			}
			else if ( getParameter() instanceof MultipleStringValueT && ( (MultipleStringValueT) getParameter() ).isInvertOnWire() )
			{
				return invertOnWire( str );
			}
			else
			{
				return str;
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToParameterComparable(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToParameterComparable(Object aParameterValue)
	{
		return convertValueToComparable( aParameterValue );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToControlComparable(java.lang.Object)
	 */
	@Override
	public String convertControlValueToControlComparable(Object aValue)
	{
		return convertValueToComparable( aValue );
	}

	/* 
	 * Special handling for isControlMultiplyBy100() as aValue is converted to
	 * BigDecimal and divided by 100, otherwise returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToParameterValue(java.lang.Object)
	 */
	@Override
	public Object convertControlValueToParameterValue(Object aValue)
	{
		// -- handle PercentageT getParameter() coming through as String (eg minValue, maxValue) --
		if ( ( aValue != null ) && ( isControlMultiplyBy100() ) )
		{
			BigDecimal tempBigDecimal;
			try
			{
				tempBigDecimal = DatatypeConverter.convertValueToBigDecimalDatatype( aValue );
			}
			catch (NumberFormatException e)
			{
				throw new NumberFormatException( "Invalid Decimal Number Format: [" + aValue + "] for Parameter: " + getParameterName() );
			}

			// -- Divide Control's value by 100 --
			tempBigDecimal = tempBigDecimal.scaleByPowerOfTen( -2 );
			
			return tempBigDecimal;
		}
		else
		{
			// -- aDatatypeIfNull=DatatypeConverter.DATATYPE_STRING --
			return DatatypeConverter.convertValueToDatatype( aValue, getParameterDatatype( String.class ) );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertParameterValueToControlValue(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToControlValue(Object aValue)
	{
		if ( ( aValue == null ) || ( "".equals( aValue ) ) )
		{
			return null;
		}
		// -- handle PercentageT getParameter() coming through with BigDecial (eg from Load FIX Message) --
		else if ( isControlMultiplyBy100() )
		{
			BigDecimal tempBigDecimal;
			try
			{
				if ( aValue instanceof BigDecimal )
				{
					tempBigDecimal = (BigDecimal) aValue;
				}
				else
				{
					tempBigDecimal = new BigDecimal( (String) aValue );
				}
			}
			catch (NumberFormatException e)
			{
				throw new NumberFormatException( "Invalid Decimal Number Format: [" + aValue + "] for Parameter: " + getParameterName() );
			}
		
			// -- Multiply Control's value by 100 --
			tempBigDecimal = tempBigDecimal.scaleByPowerOfTen( 2 );
			return tempBigDecimal.toString();
		}
		else
		{
			return DatatypeConverter.convertValueToStringDatatype( aValue );
			
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertStringToControlValue(java.lang.String)
	 */
	@Override
	public String convertStringToControlValue(String aString)
	{
		return convertControlValueToControlComparable( aString );
	}

}
