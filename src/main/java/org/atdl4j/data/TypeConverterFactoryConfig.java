package org.atdl4j.data;

import org.atdl4j.config.Atdl4jConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Contains a local, static instance of TypeConverterFactory along with static getTypeConverterFactory() 
 * performing lazy init using Atdl4jConfig.getConfig().getClassNameTypeConverterFactory() 
 * (supports ability to override the class with a different classname)
 *
 */
public class TypeConverterFactoryConfig
{
	protected static final Logger logger = LoggerFactory.getLogger( TypeConverterFactoryConfig.class );
	private static TypeConverterFactory typeConverterFactory;
	
	/**
	 * @return
	 */
	public static TypeConverterFactory getTypeConverterFactory() 
	{
		if ( typeConverterFactory == null )
		{
		    typeConverterFactory = Atdl4jConfig.getConfig().createTypeConverterFactory();
		}
		return typeConverterFactory;
	}	
}