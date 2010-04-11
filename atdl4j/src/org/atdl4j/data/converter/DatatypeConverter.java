/*
 * Created on Mar 12, 2010
 *
 */
package org.atdl4j.data.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

/**
 * 
 * Static method to convert from one datatype to another (eg ParameterConverter's type to ControlConverter's type and vice-versa)
 * 
 * Creation date: (Mar 12, 2010 8:06:12 AM)
 * @author Scott Atwell
 * @version 1.0, Mar 12, 2010
 */
public class DatatypeConverter
{
	// -- Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc) --
	// -- @see ParameterTypeConverter.getParameterDatatype() and DatatypeConverter
	public static String DATATYPE_STRING = new String();
	public static Boolean DATATYPE_BOOLEAN = new Boolean( false );
	public static DateTime DATATYPE_DATE_TIME = new DateTime();
	public static XMLGregorianCalendar DATATYPE_XML_GREGORIAN_CALENDAR = DateTimeConverter.constructNewXmlGregorianCalendar();
	public static BigDecimal DATATYPE_BIG_DECIMAL = new BigDecimal( "0" );
	public static BigInteger DATATYPE_BIG_INTEGER = new BigInteger( "0" );

	/**
	 * Convert from one datatype to another (eg ParameterConverter's type to ControlConverter's type and vice-versa)
	 * @param aValue
	 * @param aToDatatypeObject
	 * @return
	 */
	public static Object convertValueToDatatype( Object aValue, Object aToDatatypeObject )
	{
		if ( aToDatatypeObject == null )
		{
			throw new IllegalArgumentException( "aToDatatypeObject provided was null" );
		}
		
		if ( aToDatatypeObject instanceof String )
		{
			return convertValueToStringDatatype( aValue );
		}
		else if ( aToDatatypeObject instanceof Boolean )
		{
			return convertValueToBooleanDatatype( aValue );
		}
		else if ( aToDatatypeObject instanceof DateTime )
		{
			throw new IllegalArgumentException( "DateTime datatype conversion is not supported (due to timezone offests)." );
		}
		else if ( aToDatatypeObject instanceof XMLGregorianCalendar )
		{
			throw new IllegalArgumentException( "XMLGregorianCalendar datatype conversion is not supported (due to timezone offests)." );
		}
		else if ( aToDatatypeObject instanceof BigDecimal )
		{
			return convertValueToBigDecimalDatatype( aValue );
		}
		else if ( aToDatatypeObject instanceof BigInteger )
		{
			return convertValueToBigIntegerDatatype( aValue );
		}
		else
		{
			throw new IllegalArgumentException( "Unsupported aToDatatypeObject type: " + aToDatatypeObject.getClass() );
		}
	}

	/**
	 * @param aValue
	 * @return
	 */
	protected static String convertValueToStringDatatype( Object aValue )
	{
		if ( aValue == null )
		{
			return null;
		}
		else if ( aValue instanceof String )
		{
			return (String) aValue;
		}
		else
		{
			return aValue.toString();
		}
	}

	/**
	 * @param aValue
	 * @return
	 */
	protected static Boolean convertValueToBooleanDatatype( Object aValue )
	{
		if ( aValue == null )
		{
			return null;
		}
		else if ( aValue instanceof Boolean )
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
				throw new IllegalArgumentException( "Unsupported convertValueToBooleanDatatype() String value: " + aValue );
			}
		}
		else if ( aValue instanceof Number )
		{
			Number num = (Number) aValue;
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
				throw new IllegalArgumentException( "Unsupported convertValueToBooleanDatatype() Number value: " + aValue );
			}
		}
		else
		{
			throw new IllegalArgumentException( "Unsupported convertValueToBooleanDatatype() datatype " + aValue.getClass() + " value: " + aValue );
		}
	}

	/**
	 * @param aValue
	 * @return
	 */
	protected static BigDecimal convertValueToBigDecimalDatatype( Object aValue )
	{
		if ( aValue == null )
		{
			return null;
		}
		else if ( aValue instanceof BigDecimal )
		{
			return (BigDecimal) aValue;
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
				return new BigDecimal( str );
			}
		}
		else if ( aValue instanceof BigInteger )
		{
			return new BigDecimal( aValue.toString() );
		}
		else if ( aValue instanceof Boolean )
		{
			Boolean bool = (Boolean) aValue;
			if ( Boolean.TRUE.equals( bool ) )
				return new BigDecimal( 1 );
			else
				return new BigDecimal( 0 );
		}
		else
		{
			throw new IllegalArgumentException( "Unsupported convertValueToBigDecimalDatatype() datatype " + aValue.getClass() + " value: " + aValue );
		}
	}
	
	/**
	 * @param aValue
	 * @return
	 */
	protected static BigInteger convertValueToBigIntegerDatatype( Object aValue )
	{
		if ( aValue == null )
		{
			return null;
		}
		else if ( aValue instanceof BigInteger )
		{
			return (BigInteger) aValue;
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
				return new BigInteger( str );
			}
		}
		else if ( aValue instanceof BigDecimal )
		{
			return new BigInteger( aValue.toString() );
		}
		else if ( aValue instanceof Boolean )
		{
			Boolean bool = (Boolean) aValue;
			if ( Boolean.TRUE.equals( bool ) )
				return new BigInteger( "1" );
			else
				return new BigInteger( "0" );
		}
		else
		{
			throw new IllegalArgumentException( "Unsupported convertValueToBigIntegerDatatype() datatype " + aValue.getClass() + " value: " + aValue );
		}	
	}
	
}
