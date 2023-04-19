package org.atdl4j.data.converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

public class DateTimeConverter
		extends AbstractTypeConverter<Comparable<Instant>>
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

	public static ZoneId convertTimezoneToZoneId( Timezone aTimezone )
	{
		if ( aTimezone != null )
		{
			return ZoneId.of( aTimezone.value() );
		}
		else
		{
			return null;
		}
		
	}
	
	public static ZonedDateTime convertXMLGregorianCalendarToZonedDateTime( XMLGregorianCalendar aXMLGregorianCalendar, Timezone aTimezone )
	{
		int tempSubsecond = 0;
		if ( aXMLGregorianCalendar.getFractionalSecond() != null )
		{
			tempSubsecond = aXMLGregorianCalendar.getFractionalSecond().intValue();
		}
		
		ZoneId tempZoneId = convertTimezoneToZoneId( aTimezone );
		if ( tempZoneId == null )
		{
			tempZoneId = ZoneId.systemDefault();
		}		
		
		return ZonedDateTime.of( aXMLGregorianCalendar.getYear(), 
									aXMLGregorianCalendar.getMonth(),
									aXMLGregorianCalendar.getDay(),
									aXMLGregorianCalendar.getHour(),
									aXMLGregorianCalendar.getMinute(),
									aXMLGregorianCalendar.getSecond(),
									tempSubsecond, 
									tempZoneId );
	}

	public static XMLGregorianCalendar convertDailyValueToValue( XMLGregorianCalendar aDailyValue, Timezone aTimezone )
	{
		// -- Note that the XMLGregorianCalendar does not default to current month, day, year --
		if ( aDailyValue != null )
		{
			// -- Init calendar date portion equal to "current date" local/default --
			ZonedDateTime tempZonedDateTime = ZonedDateTime.now();
			
			if ( aTimezone != null )
			{
				ZoneId tempZoneId = ZoneId.of( aTimezone.value() );
				if ( tempZoneId != null )
				{
					int tempOffsetSeconds = tempZoneId.getRules().getOffset( Instant.now() ).getTotalSeconds();
					// -- convert seconds to minutes --
					aDailyValue.setTimezone( tempOffsetSeconds / 60 );
					
					// -- Set calendar date portion equal to "current date" of the Timezone --
					// -- (eg Asian security trading in Japan during the morning of Feb 15 might be local of 9pm ET Feb 14.  
					// -- 	Want to ensure we use Feb 15, not Feb 14 if localMktTz is for Japan) --
					tempZonedDateTime = ZonedDateTime.now( tempZoneId );
				}
			}
			
			aDailyValue.setMonth( tempZonedDateTime.getMonthValue() );
			aDailyValue.setDay( tempZonedDateTime.getDayOfMonth() );
			aDailyValue.setYear( tempZonedDateTime.getYear() );
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

	/**
	 * @see org.atdl4j.data.ControlTypeConverter#convertControlValueToControlComparable(java.lang.Object)
	 */
	@Override
	public Instant convertControlValueToControlComparable(Object aValue)
	{
		if ( aValue instanceof Instant )
		{
			return (Instant) aValue;
		}
		else if ( aValue instanceof ZonedDateTime )
		{
			return ((ZonedDateTime) aValue).toInstant();
		}
		else if ( aValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToZonedDateTime( (XMLGregorianCalendar) aValue, getTimezone() ).toInstant();
		}
		else if ( aValue instanceof String )
		{
			String str = (String) aValue;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern( format );

			try
			{
				return ZonedDateTime.parse(str, fmt).toInstant();
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
			return convertXMLGregorianCalendarToZonedDateTime( (XMLGregorianCalendar) aValue, getTimezone() ).toInstant(); 
		}
		else if ( aValue instanceof ZonedDateTime )
		{
			return ((ZonedDateTime) aValue).toInstant();
		}
		else
		{
			return aValue;
		}
	}

	/* No conversion applicable for this type.  Returns aValue.
	 * @see org.atdl4j.data.ControlTypeConverter#convertParameterValueToControlValue(java.lang.Object)
	 */
	@Override
	public Instant convertParameterValueToControlValue(Object aValue)
	{
		if ( aValue instanceof Instant )
		{
			return (Instant) aValue;
		}
		else if ( aValue instanceof ZonedDateTime )
		{
			return ((ZonedDateTime) aValue).toInstant();
		}
		else if ( aValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToZonedDateTime( (XMLGregorianCalendar) aValue, getTimezone() ).toInstant();
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
	public Instant convertStringToControlValue(String aString)
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
			String str = aFixWireValue;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern( format );

			try
			{  
				if ( getParameter() == null || 
						getParameter() instanceof UTCTimeOnlyT || 
						getParameter() instanceof UTCTimestampT )
				{
					return ZonedDateTime.parse(str, fmt.withZone( ZoneId.of("UTC"))).toInstant();
				}
				else
				{
					return ZonedDateTime.parse(str, fmt).toInstant();
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
			String str = aParameterString;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern( format );

			try
			{  
				if ( getParameter() == null || 
						getParameter() instanceof UTCTimeOnlyT || 
						getParameter() instanceof UTCTimestampT )
				{
					return ZonedDateTime.parse(str, fmt.withZone( ZoneId.of("UTC"))).toInstant();
				}
				else
				{
					return ZonedDateTime.parse(str, fmt).toInstant();
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
		if ( aParameterValue == null )
		{
			return null;
		}
		
		ZonedDateTime tempZonedDateTime = ZonedDateTime.ofInstant(convertParameterValueToParameterComparable( aParameterValue ), ZoneId.of("UTC" ) ); 
		
		if ( tempZonedDateTime != null )
		{
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern( getFormatString() );
			return tempZonedDateTime.format( fmt );
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
	public Instant convertParameterValueToParameterComparable(Object aParameterValue)
	{
		if ( aParameterValue instanceof Instant )
		{
			return (Instant) aParameterValue;
		}
		else if ( aParameterValue instanceof ZonedDateTime )
		{
			ZonedDateTime tempZonedDateTime = (ZonedDateTime) aParameterValue;
			
			if ( getParameter() == null || 
					getParameter() instanceof UTCTimeOnlyT || 
					getParameter() instanceof UTCTimestampT )
			{
				return tempZonedDateTime.withZoneSameInstant( ZoneId.of( "UTC" ) ).toInstant();
			}
			else
			{
				return tempZonedDateTime.toInstant();
			}
		}
		else if ( aParameterValue instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToZonedDateTime( (XMLGregorianCalendar) aParameterValue, getTimezone() ).toInstant();
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
		if ( aParameterValue == null )
		{
			return null;
		}
		
		ZonedDateTime tempDateTime = ZonedDateTime.ofInstant( convertParameterValueToParameterComparable( aParameterValue ), ZoneId.systemDefault() );
	
		if ( tempDateTime != null )
		{
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern( format );
			
			if ( getParameter() == null || 
					getParameter() instanceof UTCTimeOnlyT || 
					getParameter() instanceof UTCTimestampT )
			{
				tempDateTime = tempDateTime.withZoneSameInstant( ZoneId.of ("UTC" ) );
			}
			return tempDateTime.format(fmt);
		}
		else
		{
			return null;
		}
	}

}
