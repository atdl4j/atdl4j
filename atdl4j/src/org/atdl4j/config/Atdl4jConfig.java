package org.atdl4j.config;

import org.atdl4j.data.exception.Atdl4jConfigNotFoundException;

/**
 * This class provides a static reference to Atdl4jConfiguration.
 * 
 * @author Scott Atwell
 * @version 1.0, Sep 28, 2010
 */
public class Atdl4jConfig
{	
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
	    	if (config != null) return config;
	    	else throw new Atdl4jConfigNotFoundException();
	}
}
