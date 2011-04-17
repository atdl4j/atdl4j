package org.atdl4j.data.converter;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.atdl4j.data.ParameterHelper;
import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.timezones.Timezone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeConverter
		extends AbstractTypeConverter<Comparable<DateTime>>
{
	Timezone timezone = null;
	public static DatatypeFactory javaxDatatypeFactory;

	
	public DateTimeConverter(ParameterT aParameter)
	{
		super( aParameter );
		
		setTimezone( ParameterHelper.getLocalMktTz( getParameter() ) );
	}

	public DateTimeConverter(ParameterTypeConverter<?> aParameterTypeConverter)
	{
		super( aParameterTypeConverter );

		if ( ( aParameterTypeConverter != null ) && ( aParameterTypeConverter.getParameter() != null ) ) 
		{
			setTimezone( ParameterHelper.getLocalMktTz( aParameterTypeConverter.getParameter() ) );
		}
	}
	
	private String getFormatString()
	{
		if ( getParameter() != null )
		{
			if ( getParameter() instanceof LocalMktDateT )
			{
				return "yyyyMMdd";
			}
			else if ( getParameter() instanceof MonthYearT )
			{
				return "yyyyMM";
			}
			else if ( getParameter() instanceof UTCDateOnlyT )
			{
				return "yyyyMMdd";
			}
			else if ( getParameter() instanceof UTCTimeOnlyT )
			{
				return "HH:mm:ss";
			}
			else if ( getParameter() instanceof UTCTimestampT )
			{
				return "yyyyMMdd-HH:mm:ss";
			}
			// TODO: Uncomment when TZTimestamp / TZTimeOnly becomes available
			/*
			 * else if (getParameter() instanceof TZTimeOnlyT) { return "HH:mm:ssZZ"; }
			 * else if (getParameter() instanceof TZTimestampT) { return
			 * "yyyyMMdd-HH:mm:ssZZ"; }
			 */
		}
		return "yyyyMMdd-HH:mm:ss";
	}

	public static DateTimeZone convertTimezoneToDateTimeZone( Timezone aTimezone )
	{
		if ( aTimezone != null )
		{
			return DateTimeZone.forID( aTimezone.value() );
		}
		else
		{
			return null;
		}
	}
	
	public static DateTime convertXMLGregorianCalendarToDateTime( XMLGregorianCalendar aXMLGregorianCalendar, Timezone aTimezone )
	{
		// -- DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) --
		int tempSubsecond = 0;
		if ( aXMLGregorianCalendar.getFractionalSecond() != null )
		{
			tempSubsecond = aXMLGregorianCalendar.getFractionalSecond().intValue();
		}
		
		DateTimeZone tempDateTimeZone = convertTimezoneToDateTimeZone( aTimezone );
		if ( tempDateTimeZone == null )
		{
			tempDateTimeZone = DateTimeZone.getDefault();
		}
		
		return new DateTime( aXMLGregorianCalendar.getYear(), 
									aXMLGregorianCalendar.getMonth(),
									aXMLGregorianCalendar.getDay(),
									aXMLGregorianCalendar.getHour(),
									aXMLGregorianCalendar.getMinute(),
									aXMLGregorianCalendar.getSecond(),
									tempSubsecond, 
									tempDateTimeZone );
	}

	public static XMLGregorianCalendar convertDailyValueToValue( XMLGregorianCalendar aDailyValue, Timezone aTimezone )
	{
		// -- Note that the XMLGregorianCalendar does not default to current month, day, year --
		if ( aDailyValue != null )
		{
			// -- Init calendar date portion equal to "current date" local/default --
			DateTime tempDateTime = new DateTime();
			
			if ( aTimezone != null )
			{
				DateTimeZone tempDateTimeZone = DateTimeZone.forID( aTimezone.value() );
				if ( tempDateTimeZone != null )
				{
					int tempOffsetMillis = tempDateTimeZone.getOffset( System.currentTimeMillis() );
					// -- convert milliseconds to minutes --
					aDailyValue.setTimezone( tempOffsetMillis / 60000 );
					
					// -- Set calendar date portion equal to "current date" of the Timezone --
					// -- (eg Asian security trading in Japan during the morning of Feb 15 might be local of 9pm ET Feb 14.  
					// -- 	Want to ensure we use Feb 15, not Feb 14 if localMktTz is for Japan) --
					tempDateTime = new DateTime( tempDateTimeZone );
				}
			}
			
			aDailyValue.setMonth( tempDateTime.getMonthOfYear() );
			aDailyValue.setDay( tempDateTime.getDayOfMonth() );
			aDailyValue.setYear( tempDateTime.getYear() );
		}
		
		return aDailyValue;
	}


	/**
	 * @return the timezone
	 */
	public Timezone getTimezone()
	{
		return this.timezone;
	}

	/**
	 * @param aTimezone the timezone to set
	 */
	public void setTimezone(Timezone aTimezone)
	{
		this.timezone = aTimezone;
	}
	
	protected static DatatypeFactory getJavaxDatatypeFactory()
	{
		// -- Lazy init --
		if ( javaxDatatypeFactory == null )
		{
			try {
				javaxDatatypeFactory = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
			    	// swallow, likely generate NPE   
			}
		}
		
		return javaxDatatypeFactory;
	}
	
	/**
	 * @return javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar()
	 */
	public static XMLGregorianCalendar constructNewXmlGregorianCalendar()
	{
		return getJavaxDatatypeFactory().newXMLGregorianCalendar( new GregorianCalendar() );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToControlComparable(java.lang.Object)
	 */
	@Override
	public DateTime convertControlValueToControlComparable(Object aValue)
	{
		if ( aValue instanceof DateTime )
		{
			return (DateTime) aValue;
		}
		else if ( aValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToDateTime( (XMLGregorianCalendar) aValue, getTimezone() );
		}
		else if ( aValue instanceof String )
		{
			String str = (String) aValue;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern( format );

			try
			{  
				if ( ( getParameterTypeConverter() == null ) ||
					  ( getParameterTypeConverter().getParameter() == null ) || 
					  ( getParameterTypeConverter().getParameter() instanceof UTCTimeOnlyT ) || 
					  ( getParameterTypeConverter().getParameter() instanceof UTCTimestampT ) )
				{
					DateTime tempDateTime = fmt.parseDateTime( str ); 
					return tempDateTime.withZone( DateTimeZone.UTC );
				}

				/*
				 * else if (getParameter() instanceof TZTimestamp || getParameter() instanceof
				 * TZTimeOnlyT) { return fmt.withOffsetParsed().parseDateTime(str);
				 * }
				 */
				else
				{
					return fmt.parseDateTime( str );
				}
			}
			catch (IllegalArgumentException e)
			{
				throw new IllegalArgumentException( "Unable to parse \"" + str + "\" with format \"" + format + "\"  Exception: " + e.getMessage() );
			}
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
		if ( aValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToDateTime( (XMLGregorianCalendar) aValue, getTimezone() ); 
		}
		else
		{
			return (DateTime) aValue;
		}
	}

	/* No conversion applicable for this type.  Returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertParameterValueToControlValue(java.lang.Object)
	 */
	@Override
	public DateTime convertParameterValueToControlValue(Object aValue)
	{
		if ( aValue instanceof DateTime )
		{
			return (DateTime) aValue;
		}
		else if ( aValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToDateTime( (XMLGregorianCalendar) aValue, getTimezone() );
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ControlTypeConverter#convertStringToControlValue(java.lang.String)
	 */
	@Override
	public DateTime convertStringToControlValue(String aString)
	{
		return convertControlValueToControlComparable( aString );
	}

	/* aFixWireValue value should contain time expressed in UTC for UTCTimestampT
	 * @see org.atdl4j.data.ParameterTypeConverter#convertFixWireValueToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertFixWireValueToParameterValue(String aFixWireValue)
	{
		if ( aFixWireValue != null )
		{
			String str = (String) aFixWireValue;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern( format );

			try
			{  
				if ( getParameter() == null || 
						getParameter() instanceof UTCTimeOnlyT || 
						getParameter() instanceof UTCTimestampT )
				{
					DateTime tempDateTime = fmt.withZone( DateTimeZone.UTC ).parseDateTime( str );
					return tempDateTime;
				}
				else
				{
					return fmt.parseDateTime( str );
				}
			}
			catch (IllegalArgumentException e)
			{
				throw new IllegalArgumentException( "Unable to parse \"" + str + "\" with format \"" + format + "\"  Execption: " + e.getMessage() );
			}
		}
		else
		{	
			return null;
		}	
	}

	/* aParameterString time value should be expressed in local or Parameter/@timezone
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterStringToParameterValue(java.lang.String)
	 */
	@Override
	public Object convertParameterStringToParameterValue(String aParameterString)
	{
		if ( aParameterString != null )
		{
			String str = (String) aParameterString;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern( format );

			try
			{  
				if ( getParameter() == null || 
						getParameter() instanceof UTCTimeOnlyT || 
						getParameter() instanceof UTCTimestampT )
				{
					return fmt.withZone( DateTimeZone.UTC ).parseDateTime( str ); 
				}
				else
				{
					return fmt.parseDateTime( str );
				}
			}
			catch (IllegalArgumentException e)
			{
				throw new IllegalArgumentException( "Unable to parse \"" + str + "\" with format \"" + format + "\"  Execption: " + e.getMessage() );
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
		DateTime date = convertParameterValueToParameterComparable( aParameterValue ); 
		
		if ( date != null )
		{
			DateTimeFormatter fmt = DateTimeFormat.forPattern( getFormatString() );
			return fmt.withZone( DateTimeZone.UTC ).print( date );
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
	public DateTime convertParameterValueToParameterComparable(Object aParameterValue)
	{
		if ( aParameterValue instanceof DateTime )
		{
			DateTime tempDateTime = (DateTime) aParameterValue;
			
			if ( getParameter() == null || 
					getParameter() instanceof UTCTimeOnlyT || 
					getParameter() instanceof UTCTimestampT )
			{
				return tempDateTime.withZone( DateTimeZone.UTC );
			}
			else
			{
				return tempDateTime;
			}
		}
		else if ( aParameterValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToDateTime( (XMLGregorianCalendar) aParameterValue, getTimezone() );
		}
		else
		{
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToComparisonString(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToComparisonString(Object aParameterValue)
	{
		DateTime tempDateTime = convertParameterValueToParameterComparable( aParameterValue );
	
		if ( tempDateTime != null )
		{
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern( format );
			
			if ( getParameter() == null || 
					getParameter() instanceof UTCTimeOnlyT || 
					getParameter() instanceof UTCTimestampT )
			{
				tempDateTime = tempDateTime.withZone( DateTimeZone.UTC );
			}
			return fmt.print( tempDateTime );
		}
		else
		{
			return null;
		}
	}

}
