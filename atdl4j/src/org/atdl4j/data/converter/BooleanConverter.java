package org.atdl4j.data.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.fixatdl.core.BooleanT;

public class BooleanConverter
		extends AbstractTypeConverter<Boolean>
{

	public static final String BOOLEAN_FALSE = "N";
	public static final String BOOLEAN_TRUE = "Y";

	public BooleanConverter(BooleanT aParameter)
	{
		super( aParameter );
	}

	public BooleanConverter(ParameterTypeConverter<?> aParameterTypeConverter)
	{
		super( aParameterTypeConverter );
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToControlComparable(java.lang.Object)
	 */
	public Boolean convertControlValueToControlComparable(Object aValue)
	{
		if ( aValue == null )
			return null;

		if ( aValue instanceof Boolean )
		{
			return (Boolean) aValue;
		}
		else if ( aValue instanceof String )
		{
			String str = (String) aValue;
			if ( str.equalsIgnoreCase( "true" ) || str.equals( "1" ) || str.equals( BooleanConverter.BOOLEAN_TRUE ) )
			{
				return new Boolean( true );
			}
			else if ( str.equalsIgnoreCase( "false" ) || str.equals( "0" ) || str.equals( BooleanConverter.BOOLEAN_FALSE ) )
			{
				return new Boolean( false );
			}
			else if ( str.equals( "" ) )
			{
				return null;
			}
			else
			{
				return new Boolean( false );
			}
		}
		else if ( aValue instanceof BigDecimal || aValue instanceof BigInteger )
		{
			BigDecimal num = (BigDecimal) aValue;
			if ( num.intValue() == 1 )
			{
				return new Boolean( true );
			}
			else if ( num.intValue() == 0 )
			{
				return new Boolean( false );
			}
			else
			{
				return new Boolean( false );
			}
		}
//		else
//			return new Boolean( false );
		else
		{
			return null;
		}
	}

	/* No conversion applicable for this type.  Returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToParameterValue(java.lang.Object)
	 */
	public Object convertControlValueToParameterValue(Object aValue)
	{
// 3/12/2010 Scott Atwell		return (Boolean) aValue;
		// -- aDatatypeIfNull=DATATYPE_BOOLEAN --
		return DatatypeConverter.convertValueToDatatype( aValue, getParameterDatatype( DatatypeConverter.DATATYPE_BOOLEAN ) );
	}

	/* No conversion applicable for this type.  Returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertParameterValueToControlValue(java.lang.Object)
	 */
	@Override
	public Boolean convertParameterValueToControlValue(Object aValue)
	{
//	3/12/2010 Scott Atwell	return (Boolean) aValue;
		return DatatypeConverter.convertValueToBooleanDatatype( aValue );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertStringToControlValue(java.lang.String)
	 */
	@Override
	public Boolean convertStringToControlValue(String aString)
	{
		return convertControlValueToControlComparable( aString );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertFixWireValueToParameterValue(java.lang.String)
	 */
	@Override
	public Boolean convertFixWireValueToParameterValue(String aFixWireValue)
	{
		return convertStringToParameterValue( aFixWireValue );
	}

	/**
	 * Supports either FixWireValue or ParameterString values
	 * @param aValue
	 * @return
	 */
	protected Boolean convertStringToParameterValue(String aValue)
	{
		if ( aValue != null )
		{
			String str = (String) aValue;
			if ( str.equalsIgnoreCase( "true" ) || str.equals( "1" ) || str.equals( BOOLEAN_TRUE ) )
			{
				return new Boolean( true );
			}
			else if ( str.equalsIgnoreCase( "false" ) || str.equals( "0" ) || str.equals( BOOLEAN_FALSE ) )
			{
				return new Boolean( false );
			}
			else if ( str.equals( "" ) )
			{
				return null;
			}
			else
			{
				return new Boolean( false );
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterStringToParameterValue(java.lang.String)
	 */
	@Override
	public Boolean convertParameterStringToParameterValue(String aParameterString)
	{
		return convertStringToParameterValue( aParameterString );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToFixWireValue(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToFixWireValue(Object aParameterValue)
	{
		// TODO: cleanup
		BooleanT booleanT = null;
		if ( getParameter() != null && getParameter() instanceof BooleanT )
		{
			booleanT = (BooleanT) getParameter();
		}

//	3/10/2010 Scott Atwell	Boolean bool = convertValueToParameterComparable( aParameterValue ); 
		Boolean bool = convertParameterValueToParameterComparable( aParameterValue ); 

		// 2/1/2010 John Shields added
		if ( bool != null )
		{
			return bool.booleanValue() ? BOOLEAN_TRUE : BOOLEAN_FALSE;
		}
		else
			return null;

// 2/1/2010 John Shields deleted
// trueWireValue and falseWireValue are deprecated
		/*
		 * if (bool != null) { if (bool.booleanValue()) { if (booleanT != null &&
		 * booleanT.getTrueWireValue() != null) return
		 * booleanT.getTrueWireValue(); else return BOOLEAN_TRUE; } else { if
		 * (booleanT != null && booleanT.getFalseWireValue() != null) return
		 * booleanT.getFalseWireValue(); else return BOOLEAN_FALSE; } } else {
		 * return null; }
		 */
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToParameterComparable(java.lang.Object)
	 */
	@Override
	public Boolean convertParameterValueToParameterComparable(Object aParameterValue)
	{
		if ( aParameterValue == null )
			return null;

		if ( aParameterValue instanceof Boolean )
		{
			return (Boolean) aParameterValue;
		}
		else if ( aParameterValue instanceof String )
		{
			String str = (String) aParameterValue;
			if ( str.equalsIgnoreCase( "true" ) || str.equals( "1" ) || str.equals( BooleanConverter.BOOLEAN_TRUE ) )
			{
				return new Boolean( true );
			}
			else if ( str.equalsIgnoreCase( "false" ) || str.equals( "0" ) || str.equals( BooleanConverter.BOOLEAN_FALSE ) )
			{
				return new Boolean( false );
			}
			else if ( str.equals( "" ) )
			{
				return null;
			}
			else
			{
				return new Boolean( false );
			}
		}
		else if ( aParameterValue instanceof BigDecimal || aParameterValue instanceof BigInteger )
		{
			BigDecimal num = (BigDecimal) aParameterValue;
			if ( num.intValue() == 1 )
			{
				return new Boolean( true );
			}
			else if ( num.intValue() == 0 )
			{
				return new Boolean( false );
			}
			else
			{
				return new Boolean( false );
			}
		}
//		else
//			return new Boolean( false );
		else
		{
			return null;
		}
	}

}
