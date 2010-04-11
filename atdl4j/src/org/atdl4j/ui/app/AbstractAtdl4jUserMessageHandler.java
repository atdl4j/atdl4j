/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.app;

import javax.xml.bind.JAXBException;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.exception.ValidationException;

/**
 * Represents the base, non-GUI system-specific GUI pop-up message screen support.
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public abstract class AbstractAtdl4jUserMessageHandler
		implements Atdl4jUserMessageHandler
{
	private Atdl4jConfig atdl4jConfig = null;

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jUserMessageHandler#isInitReqd()
	 */
	public boolean isInitReqd()
	{
		if ( atdl4jConfig != null )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @param atdl4jConfig the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
	{
		this.atdl4jConfig = atdl4jConfig;
	}


	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}

	/**
	 * Extracts the "message" value from Throwable.  Handles JAXBException.getLinkedException().getMessage().
	 * 
	 * @param e
	 * @return
	 */
	public static String extractExceptionMessage( Throwable e )
	{
		if ( e instanceof ValidationException )
		{
			ValidationException tempValidationException = (ValidationException) e;
			if ( tempValidationException.getMessage() != null )
			{
				// -- Add new lines after sentence (eg before "Rule Tested: [______]") --
				return tempValidationException.getMessage().replace( ".  ", ".\n\n" );
			}
			else
			{
				return tempValidationException.getMessage();
			}
		}
			// e.getMessage() is null if there is a JAXB parse error 
		else if (e.getMessage() != null)
		{
			return e.getMessage();
		}
		else if ( e instanceof JAXBException )
		{
			JAXBException tempJAXBException = (JAXBException) e;
			
			if ( ( tempJAXBException.getLinkedException() != null ) &&
				  ( tempJAXBException.getLinkedException().getMessage() != null ) )
			{ 
				return tempJAXBException.getLinkedException().getMessage();
			}
		}
		
			
		return e.toString();
	}
	

}
