package org.atdl4j.data.converter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBException;

import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.core.LocalMktTimeT;
import org.fixprotocol.atdl_1_1.core.MonthYearT;
import org.fixprotocol.atdl_1_1.core.UTCDateT;
import org.fixprotocol.atdl_1_1.core.UTCTimeOnlyT;
import org.fixprotocol.atdl_1_1.core.UTCTimeStampT;

public class DateTimeConverter extends AbstractTypeConverter<DateTime> {

	// TODO: implement Tz handling
	// private String localMktTz;

	//public DateConverter() {
	//}

	public DateTimeConverter(ParameterT parameter) {
		this.parameter = parameter;
	}
	
	private String getFormatString()
	{
		if (parameter != null)
		{
			if (parameter instanceof LocalMktTimeT)
			{
				return "yyyyMMdd";
			}
			else if (parameter instanceof MonthYearT)
			{
				return "yyyyMM";
			}
			else if (parameter instanceof UTCDateT)
			{
				return "yyyyMMdd";
			}
			else if (parameter instanceof UTCTimeOnlyT)
			{
				return "HH:mm:ss";
			}
			else if (parameter instanceof UTCTimeStampT)
			{
				return "yyyyMMdd-HH:mm:ss";
			}
			// TODO: Uncomment when TZTimestamp / TZTimeOnly becomes available
			/*			else if (parameter instanceof TZTimeOnlyT)
			{
				return "HH:mm:ssZZ";
			}
			else if (parameter instanceof TZTimestampT)
			{
				return "yyyyMMdd-HH:mm:ssZZ";
			}*/
		}
		return "yyyyMMdd-HH:mm:ss";
	}
	
	public DateTime convertValueToComparable(Object value) throws JAXBException
	{
		if (value instanceof DateTime)
		{
			return (DateTime) value;
		}
		if (value instanceof String)
		{
			String str = (String) value;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
			
			try {

				if (parameter == null ||
					parameter instanceof UTCTimeOnlyT ||
					parameter instanceof UTCTimeStampT)	{
					return fmt.withZone(DateTimeZone.UTC).parseDateTime(str);
				}

				/*else if (parameter instanceof TZTimestamp ||
						parameter instanceof TZTimeOnlyT)
				{
					return fmt.withOffsetParsed().parseDateTime(str);
				}*/				
				else {
					return fmt.parseDateTime(str);
				}
				
			} catch (IllegalArgumentException e) {
				throw new JAXBException("Unable to parse \"" + str
						+ "\" with format \"" + format + "\"");
			}
		}
		return null;
	}
	
	public String convertValueToString(Object value) throws JAXBException
	{		
		DateTime date = convertValueToComparable(value); // TODO: this doesn't currently return null
		if (date != null)
		{
			DateTimeFormatter fmt = DateTimeFormat.forPattern(getFormatString());
			return fmt.print(date);
		}
		return null;
	}
}
