package org.atdl4j.data.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.fixatdl.core.AmtT;
import org.atdl4j.fixatdl.core.FloatT;
import org.atdl4j.fixatdl.core.NumericT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.PercentageT;
import org.atdl4j.fixatdl.core.PriceOffsetT;
import org.atdl4j.fixatdl.core.PriceT;
import org.atdl4j.fixatdl.core.QtyT;

/*
 * Supports BigDecimal-based Parameter types:
 * 		FloatT
 * 		AmtT
 * 		PercentageT
 * 		PriceOffsetT
 * 		PriceT
 * 		QtyT
 *
 * Note that isMultiplyBy100() is only supported for PercentageT
 * 
 * @author Scott Atwell
 */
public class DecimalConverter
		extends AbstractTypeConverter<BigDecimal>
{
	public DecimalConverter(ParameterT aParameter)
	{
		super( aParameter );
	}

	public DecimalConverter(ParameterTypeConverter<?> aParameterTypeConverter)
	{
		super( aParameterTypeConverter );
	}
	
	public static NumberFormat DECIMAL_FORMAT_0dp = new DecimalFormat( "#;-#" );
	public static NumberFormat DECIMAL_FORMAT_1dp = new DecimalFormat( "#.0;-#.0" );
	public static NumberFormat DECIMAL_FORMAT_2dp = new DecimalFormat( "#.00;-#.00" );
	public static NumberFormat DECIMAL_FORMAT_3dp = new DecimalFormat( "#.000;-#.000" );
	public static NumberFormat DECIMAL_FORMAT_4dp = new DecimalFormat( "#.0000;-#.0000" );
	public static NumberFormat DECIMAL_FORMAT_5dp = new DecimalFormat( "#.00000;-#.00000" );
	public static NumberFormat DECIMAL_FORMAT_6dp = new DecimalFormat( "#.000000;-#.000000" );
	public static NumberFormat DECIMAL_FORMAT_7dp = new DecimalFormat( "#.0000000;-#.0000000" );
	public static NumberFormat DECIMAL_FORMAT_8dp = new DecimalFormat( "#.00000000;-#.00000000" );
	public static NumberFormat DECIMAL_FORMAT_9dp = new DecimalFormat( "#.000000000;-#.000000000" );
	public static NumberFormat DECIMAL_FORMAT_10dp = new DecimalFormat( "#.0000000000;-#.0000000000" );
	public static NumberFormat DECIMAL_FORMAT_11dp = new DecimalFormat( "#.00000000000;-#.00000000000" );
	public static NumberFormat DECIMAL_FORMAT_12dp = new DecimalFormat( "#.000000000000;-#.000000000000" );
	public static NumberFormat DECIMAL_FORMAT_13dp = new DecimalFormat( "#.0000000000000;-#.0000000000000" );
	
	/**
	 * Applies precision rules, if specified, up to 13 decimal places.
	 * 
	 * @param aValue
	 * @param aPrecision
	 * @return
	 */
	public static String toString( BigDecimal aValue, BigInteger aPrecision )
	{
		if ( aValue != null )
		{
			if ( aPrecision != null )
			{
				switch ( aPrecision.intValue() )
				{
					case 0:
						return DECIMAL_FORMAT_0dp.format( aValue.doubleValue() );
						
					case 1:
						return DECIMAL_FORMAT_1dp.format( aValue.doubleValue() );
						
					case 2:
						return DECIMAL_FORMAT_2dp.format( aValue.doubleValue() );
						
					case 3:
						return DECIMAL_FORMAT_3dp.format( aValue.doubleValue() );
						
					case 4:
						return DECIMAL_FORMAT_4dp.format( aValue.doubleValue() );
						
					case 5:
						return DECIMAL_FORMAT_5dp.format( aValue.doubleValue() );
						
					case 6:
						return DECIMAL_FORMAT_6dp.format( aValue.doubleValue() );
						
					case 7:
						return DECIMAL_FORMAT_7dp.format( aValue.doubleValue() );
						
					case 8:
						return DECIMAL_FORMAT_8dp.format( aValue.doubleValue() );
						
					case 9:
						return DECIMAL_FORMAT_9dp.format( aValue.doubleValue() );
						
					case 10:
						return DECIMAL_FORMAT_10dp.format( aValue.doubleValue() );
						
					case 11:
						return DECIMAL_FORMAT_11dp.format( aValue.doubleValue() );
						
					case 12:
						return DECIMAL_FORMAT_12dp.format( aValue.doubleValue() );
						
					case 13:
						return DECIMAL_FORMAT_13dp.format( aValue.doubleValue() );

					default:
						return aValue.toPlainString();
				}
			}
			else  // -- No precision expressed --
			{
				return aValue.toPlainString();
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the value of Parameter.getPrecision() for NumericT assuming it has been set,
	 * otherwise returns null.
	 * 
	 * @return
	 */
	public BigInteger getPrecision()
	{
		if ( getParameter() instanceof NumericT )
		{
			return ( (NumericT) getParameter() ).getPrecision();
		}
		else if ( ( getParameterTypeConverter() != null ) &&
				    ( getParameterTypeConverter().getParameter() instanceof NumericT ) )
		{
			return ( (NumericT) getParameterTypeConverter().getParameter() ).getPrecision();
		}
		else
		{
			// -- Return null if Parameter does not have this value set --
			return null; 
		}
	}

	/**
	 * Returns the value of Parameter.getMinValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigDecimal getMinValue()
	{
		if ( getParameter() instanceof FloatT )
		{
			return ( (FloatT) getParameter() ).getMinValue();
		}
		else if ( getParameter() instanceof AmtT )
		{
			return ( (AmtT) getParameter() ).getMinValue();
		}
		else if ( getParameter() instanceof PercentageT )
		{
			return ( (PercentageT) getParameter() ).getMinValue();
		}
		else if ( getParameter() instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) getParameter() ).getMinValue();
		}
		else if ( getParameter() instanceof PriceT )
		{
			return ( (PriceT) getParameter() ).getMinValue();
		}
		else if ( getParameter() instanceof QtyT )
		{
			return ( (QtyT) getParameter() ).getMinValue();
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
	public BigDecimal getMaxValue()
	{
		if ( getParameter() instanceof FloatT )
		{
			return ( (FloatT) getParameter() ).getMaxValue();
		}
		else if ( getParameter() instanceof AmtT )
		{
			return ( (AmtT) getParameter() ).getMaxValue();
		}
		else if ( getParameter() instanceof PercentageT )
		{
			return ( (PercentageT) getParameter() ).getMaxValue();
		}
		else if ( getParameter() instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) getParameter() ).getMaxValue();
		}
		else if ( getParameter() instanceof PriceT )
		{
			return ( (PriceT) getParameter() ).getMaxValue();
		}
		else if ( getParameter() instanceof QtyT )
		{
			return ( (QtyT) getParameter() ).getMaxValue();
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
	public BigDecimal getConstValue()
	{
		if ( getParameter() instanceof FloatT )
		{
			return ( (FloatT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof AmtT )
		{
			return ( (AmtT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof PercentageT )
		{
			return ( (PercentageT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof PriceT )
		{
			return ( (PriceT) getParameter() ).getConstValue();
		}
		else if ( getParameter() instanceof QtyT )
		{
			return ( (QtyT) getParameter() ).getConstValue();
		}
		else
		{
			return null;
		}
	}

	/* If isControlMultiplyBy100() then converted value will be multiplied by 100 
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToControlComparable(java.lang.Object)
	 */
	@Override
	public BigDecimal convertControlValueToControlComparable(Object aValue)
	{
		BigDecimal tempBigDecimal = null;
		
		if ( aValue instanceof BigDecimal )
		{
			tempBigDecimal = (BigDecimal) aValue;
		}
		else if ( aValue instanceof String )
		{
			String str = (String) aValue;
			if ( str.trim().length() == 0 )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					tempBigDecimal = new BigDecimal( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Decimal Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}
		}
		else if ( aValue instanceof Boolean )
		{
			Boolean bool = (Boolean) aValue;
			tempBigDecimal =  new BigDecimal( bool ? 1 : 0 );
		}
		
		if ( ( tempBigDecimal != null ) && ( isControlMultiplyBy100() ) )
		{
			return tempBigDecimal.scaleByPowerOfTen( 2 );
		}
		else
		{
			return tempBigDecimal;
		}
	}

	/* If Control represents PercentageT getParameter(), then Control's value will be returned divided by 100.
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToParameterValue(java.lang.Object)
	 */
	@Override
	public Object convertControlValueToParameterValue(Object aValue)
	{
		BigDecimal tempBigDecimal = DatatypeConverter.convertValueToBigDecimalDatatype( aValue );
		if ( ( tempBigDecimal != null ) && ( isControlMultiplyBy100() ) )
		{
			// -- divide Control's value by 100 --
			return tempBigDecimal.scaleByPowerOfTen( -2 );
		}
		else
		{
			// -- aDatatypeIfNull=DATATYPE_BIG_DECIMAL --
			return DatatypeConverter.convertValueToDatatype( tempBigDecimal, getParameterDatatype( BigDecimal.class ) );
		}
	}

	/* If Control represents PercentageT getParameter(), then Parameter's value will be returned multiplied by 100.
	 * @see org.atdl4j.data.ControlTypeConverter#convertParameterValueToControlValue(java.lang.Object)
	 */
	@Override
	public BigDecimal convertParameterValueToControlValue(Object aValue)
	{
		BigDecimal tempBigDecimal = DatatypeConverter.convertValueToBigDecimalDatatype( aValue );
		
		if ( ( tempBigDecimal != null ) && ( isControlMultiplyBy100() ) )
		{
			// -- multiply Control's value by 100 --
			return tempBigDecimal.scaleByPowerOfTen( 2 );
		}
		else
		{	
			return tempBigDecimal;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertStringToControlValue(java.lang.String)
	 */
	@Override
	public BigDecimal convertStringToControlValue(String aString)
	{
		return convertControlValueToControlComparable( aString );
	}

	/* 
	 * If isParameterMultiplyBy100() then the wire value (which is represented on wire as x100 from original parameter value) 
	 * will be divided by 100 to get Parameter value.
	 * 
	 * @see org.atdl4j.data.ParameterTypeConverter#convertFixWireValueToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertFixWireValueToParameterValue(String aFixWireValue)
	{
		BigDecimal tempBigDecimal = null;
		
		if ( aFixWireValue != null )
		{
			String str = aFixWireValue;
			if ( str.trim().length() == 0 )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					tempBigDecimal = new BigDecimal( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Decimal Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}
		}

		if ( ( tempBigDecimal != null ) && ( isParameterMultiplyBy100() ) )
		{
			// -- divide the wire value (which is set to x100 from original parameter value) by 100 to get Parameter value --
			return tempBigDecimal.scaleByPowerOfTen( -2 );
		}
		else
		{
			return tempBigDecimal;
		}
	}

	
	/* Converts aParameterString to BigDecimal with no scaling changes.
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterStringToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertParameterStringToParameterValue(String aParameterString)
	{
		BigDecimal tempBigDecimal = null;
		
		if ( aParameterString != null )
		{
			String str = aParameterString;
			if ( str.trim().length() == 0 )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					tempBigDecimal = new BigDecimal( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Decimal Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}
		}

		return tempBigDecimal;
	}


	/* If isParameterMultiplyBy100() the Parameter value will be multiplied x100 for its wire value
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToFixWireValue(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToFixWireValue(Object aParameterValue)
	{
		BigDecimal tempBigDecimal = convertParameterValueToParameterComparable( aParameterValue );
		
		if ( ( tempBigDecimal != null ) && ( isParameterMultiplyBy100() ) )
		{
			// -- multiply the parameter value x100 for its wire value --
			tempBigDecimal = tempBigDecimal.scaleByPowerOfTen( 2 );
		}
		
		return toString( tempBigDecimal, getPrecision() );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToParameterComparable(java.lang.Object)
	 */
	@Override
	public BigDecimal convertParameterValueToParameterComparable(Object aParameterValue)
	{
		BigDecimal tempBigDecimal = null;
		
		if ( aParameterValue instanceof BigDecimal )
		{
			tempBigDecimal = (BigDecimal) aParameterValue;
		}
		else if ( aParameterValue instanceof String )
		{
			String str = (String) aParameterValue;
			if ( str.trim().length() == 0 )
			{
				return null;
			}
			else
			{
				try
				{
					// -- Trim leading and/or trailing spaces --
					str = str.trim();
					tempBigDecimal = new BigDecimal( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Decimal Number Format: [" + str + "] for Parameter: " + getParameterName() );
				}
			}
		}
		
		return tempBigDecimal;
	}

}