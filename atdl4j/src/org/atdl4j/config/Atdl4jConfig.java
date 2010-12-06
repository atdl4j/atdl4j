package org.atdl4j.config;

import org.apache.log4j.Logger;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jTesterPanel;

/**
 * This class provides a static reference to Atdl4jConfiguration.
 * 
 * @author Scott Atwell
 * @version 1.0, Sep 28, 2010
 */
public class Atdl4jConfig
{
	private static final Logger logger = Logger.getLogger(Atdl4jConfig.class);
	
	private static Atdl4jConfiguration config;

	/**
	 * @param config the config to set
	 */
	public static void setConfig(Atdl4jConfiguration config)
	{
		Atdl4jConfig.config = config;
	}

	/**
	 * @return the config
	 */
	public static Atdl4jConfiguration getConfig()
	{
		return config;
	}
	
	public static Atdl4jCompositePanel createAtdl4jCompositePanel() 
	{
		if ( ( getConfig() != null ) && ( getConfig().getClassNameAtdl4jCompositePanel() != null ) )
		{
			String tempClassName = getConfig().getClassNameAtdl4jCompositePanel();
			logger.debug( "getAtdl4jCompositePanel() loading class named: " + tempClassName );
			try
			{
				Atdl4jCompositePanel atdl4jCompositePanel = ((Class<Atdl4jCompositePanel>) Class.forName( tempClassName ) ).newInstance();
				return atdl4jCompositePanel;
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		else
		{
			throw new IllegalStateException( "Exception: getConfig() is null" );
		}
	}

	public static Atdl4jTesterPanel createAtdl4jTesterPanel() 
	{
		if ( ( getConfig() != null ) && ( getConfig().getClassNameAtdl4jTesterPanel() != null ) )
		{
			String tempClassName = getConfig().getClassNameAtdl4jTesterPanel();
			logger.debug( "getAtdl4jTesterPanel() loading class named: " + tempClassName );
			try
			{
				Atdl4jTesterPanel atdl4jTesterPanel = ((Class<Atdl4jTesterPanel>) Class.forName( tempClassName ) ).newInstance();
				return atdl4jTesterPanel;
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		else
		{
			throw new IllegalStateException( "Exception: getConfig() is null" );
		}
	}

}
