package org.atdl4j.data.converter;

import java.math.BigInteger;

import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.fixatdl.core.IntT;
import org.atdl4j.fixatdl.core.LengthT;
import org.atdl4j.fixatdl.core.NumInGroupT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.SeqNumT;
import org.atdl4j.fixatdl.core.TagNumT;

/*
 * Supports Integer or BigInteger-based Parameter types:
 * 		IntT
 * 		NumInGroupT
 * 		SeqNumT
 * 		TagNumT
 * 		LengthT
 *
 * @author Scott Atwell
 */
public class IntegerConverter
		extends AbstractTypeConverter<BigInteger>
{
	public IntegerConverter(ParameterT aParameter)
	{
		super( aParameter );
	}

	public IntegerConverter(ParameterTypeConverter<?> aParameterTypeConverter)
	{
		super( aParameterTypeConverter );
	}
	
	/**
	 * Returns the value of Parameter.getMinValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigInteger getMinValue()
	{
		if ( getParameter() instanceof IntT )
		{
			// -- upcast IntT from Integer to BigInteger --
			if ( ( (IntT) getParameter() ).getMinValue() != null )
			{
				return new BigInteger( ( (IntT) getParameter() ).getMinValue().toString() );
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	
	/**
	 * Returns the value of Parameter.getMaxValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigInteger getMaxValue()
	{
		if ( getParameter() instanceof IntT )
		{
			// -- upcast IntT from Integer to BigInteger --
			if ( ( (IntT) getParameter() ).getMaxValue() != null )
			{
				return new BigInteger( ( (IntT) getParameter() ).getMaxValue().toString() );
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the value of Parameter.getConstValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigInteger getConstValue()
	{
		if ( getParameter() instanceof IntT )
		{
			// -- upcast IntT from Integer to BigInteger --
			if ( ( (IntT) getParameter() ).getConstValue() != null )
			{
				return new BigInteger( ( (IntT) getParameter() ).getConstValue().toString() );
			}
			else
			{
				return null;
			}
		}
		else if ( getParameter() instanceof NumInGroupT )
		{
			return ( (NumInGroupT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof SeqNumT )
		{
			return ( (SeqNumT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof TagNumT )
		{
			return ( (TagNumT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof LengthT )
		{
			return ( (LengthT) getParameter() ).getConstValue();
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertFixWireValueToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertFixWireValueToParameterValue(String aFixWireValue)
	{
		return convertStringToParameterValue( aFixWireValue );
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
	protected BigInteger convertStringToParameterValue(String aValue)
	{
		if ( aValue != null )
		{
			String str = (String) aValue;
			if ( ( str == null ) || ( str.trim().length() == 0 ) )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					return new BigInteger( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Integer Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToFixWireValue(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToFixWireValue(Object aParameterValue)
	{
		BigInteger tempBigInteger = convertParameterValueToParameterComparable( aParameterValue ); 
		if ( tempBigInteger != null )
		{
			return tempBigInteger.toString();
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
	public BigInteger convertParameterValueToParameterComparable(Object aParameterValue)
	{
		if ( aParameterValue instanceof BigInteger )
		{
			return (BigInteger) aParameterValue;
		}
		else if ( aParameterValue instanceof Integer )
		{
			return new BigInteger( ((Integer) aParameterValue).toString() );
		}
		else if ( aParameterValue instanceof String )
		{
			String str = (String) aParameterValue;
			if ( ( str == null ) || ( str.trim().length() == 0 ) )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					return new BigInteger( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Integer Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}

		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToControlComparable(java.lang.Object)
	 */
	@Override
	public BigInteger convertControlValueToControlComparable(Object aValue)
	{
		if ( aValue instanceof BigInteger )
		{
			return (BigInteger) aValue;
		}
		else if ( aValue instanceof Integer )
		{
			return new BigInteger( ((Integer) aValue).toString() );
		}
		else if ( aValue instanceof String )
		{
			String str = (String) aValue;
			if ( ( str == null ) || ( str.trim().length() == 0 ) )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					return new BigInteger( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Integer Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}

		}
		else if ( aValue instanceof Boolean )
		{
			Boolean bool = (Boolean) aValue;
			return new BigInteger( bool ? "1" : "0" );
		}
		else
		{
			return null;
		}
	}

	/* No conversion applicable for this type.  Returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToParameterValue(java.lang.Object)
	 */
	@Override
	public Object convertControlValueToParameterValue(Object aValue)
	{
		// -- aDatatypeIfNull=DATATYPE_BIG_INTEGER --
		return DatatypeConverter.convertValueToDatatype( aValue, getParameterDatatype( BigInteger.class ) );
	}

	/* No conversion applicable for this type.  Returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertParameterValueToControlValue(java.lang.Object)
	 */
	@Override
	public BigInteger convertParameterValueToControlValue(Object aValue)
	{
		return DatatypeConverter.convertValueToBigIntegerDatatype( aValue );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertStringToControlValue(java.lang.String)
	 */
	@Override
	public BigInteger convertStringToControlValue(String aString)
	{
		return convertControlValueToControlComparable( aString );
	}	
	
}