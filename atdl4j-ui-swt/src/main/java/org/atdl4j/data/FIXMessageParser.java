/*
 * Created on Jan 16, 2010
 *
 */
package org.atdl4j.data;

import javax.xml.bind.JAXBException;

/**
 * This class contains the data associated with the <code>FIXMessageParser</code>.
 * 
 * Creation date: (Jan 16, 2010 8:02:42 PM)
 * @author Scott Atwell
 * @version 1.0, Jan 16, 2010
 */
public class FIXMessageParser
{
	public static String extractFieldValueFromFIXMessage(String fixMessage, int fixTag) 
		throws JAXBException {
		
		String[] fixParams = fixMessage.split("\\001");

		for (int i = 0; i < fixParams.length; i++)
		{
			String[] pair = fixParams[i].split("=");
			int tag = Integer.parseInt(pair[0]);
			String value = pair[1];

			if (tag == fixTag) 
			{
				return value;
			}
		}
		
		return null;
	}

}
