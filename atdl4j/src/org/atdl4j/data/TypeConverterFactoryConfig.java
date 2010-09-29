package org.atdl4j.data;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;


/**
 * Contains a local, static instance of TypeConverterFactory along with static getTypeConverterFactory() 
 * performing lazy init using Atdl4jConfig.getConfig().getClassNameTypeConverterFactory() 
 * (supports ability to override the class with a different classname)
 *
 */
public class TypeConverterFactoryConfig
{
	protected static final Logger logger = Logger.getLogger( TypeConverterFactoryConfig.class );
	private static TypeConverterFactory typeConverterFactory;
	
	/**
	 * @return
	 */
	public static TypeConverterFactory getTypeConverterFactory() 
	{
		if ( ( typeConverterFactory == null ) && ( Atdl4jConfig.getConfig().getClassNameTypeConverterFactory() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameTypeConverterFactory();
			logger.debug( "getTypeConverterFactory() loading class named: " + tempClassName );
			TypeConverterFactory typeConverterFactory;
			try
			{
				typeConverterFactory = ((Class<TypeConverterFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return typeConverterFactory;
	}	
}
