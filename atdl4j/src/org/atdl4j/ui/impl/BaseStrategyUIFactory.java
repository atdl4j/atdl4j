
package org.atdl4j.ui.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;

/**
 * This class contains the data associated with the <code>BaseStrategyUIFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 10:19:52 PM)
 * @author Scott Atwell
 */
public class BaseStrategyUIFactory
{
	protected static final Logger logger = Logger.getLogger( BaseStrategyUIFactory.class );
	private static Atdl4jUserMessageHandler atdl4jUserMessageHandler = null;

//	public StrategyUI createStrategyUI(StrategyT strategy, StrategiesT aStrategies, Map<String, ValidationRule> strategiesRules, Object parentContainer)
	public static StrategyUI createStrategyUI(StrategyT strategy, StrategiesT aStrategies, Map<String, ValidationRule> strategiesRules, Object parentContainer, Atdl4jOptions aAtdl4jOptions)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameStrategyUI();
		logger.debug( "getStrategyUI() loading class named: " + tempClassName );
		StrategyUI strategyUI;
		try
		{
			strategyUI = ((Class<StrategyUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( strategyUI != null )
		{
// 9/27/2010 Scott Atwell			strategyUI.init( strategy, this, strategiesRules, parentContainer );
			strategyUI.init( strategy, aStrategies, aAtdl4jOptions, strategiesRules, parentContainer );
		}
		
		return strategyUI;
	}

	/**
	 * @param atdl4jUserMessageHandler the atdl4jUserMessageHandler to set
	 */
	public void setAtdl4jUserMessageHandler(Atdl4jUserMessageHandler atdl4jUserMessageHandler)
	{
		this.atdl4jUserMessageHandler = atdl4jUserMessageHandler;
	}

	/**
	 * @return the atdl4jUserMessageHandler
	 */
	public static Atdl4jUserMessageHandler getAtdl4jUserMessageHandler()
	{
		if ( ( atdl4jUserMessageHandler == null ) && ( Atdl4jConfig.getConfig().getClassNameAtdl4jUserMessageHandler() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jUserMessageHandler();
			logger.debug( "getAtdl4jUserMessageHandler() loading class named: " + tempClassName );
			try
			{
				atdl4jUserMessageHandler = ((Class<Atdl4jUserMessageHandler>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jUserMessageHandler;
	}
}
