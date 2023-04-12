
package org.atdl4j.ui.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
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
	protected static final Logger logger = LoggerFactory.getLogger( BaseStrategyUIFactory.class );
	private static Atdl4jUserMessageHandler atdl4jUserMessageHandler = null;

	public static StrategyUI createStrategyUI(StrategyT strategy, StrategiesT aStrategies, Map<String, ValidationRule> strategiesRules, Object parentContainer, Atdl4jOptions aAtdl4jOptions) throws FIXatdlFormatException
	{
		StrategyUI strategyUI = Atdl4jConfig.getConfig().createStrategyUI();
		strategyUI.init( strategy, aStrategies, aAtdl4jOptions, strategiesRules, parentContainer );
		return strategyUI;
	}

	/**
	 * @return the atdl4jUserMessageHandler
	 */
	public static Atdl4jUserMessageHandler getAtdl4jUserMessageHandler()
	{
		if ( atdl4jUserMessageHandler == null )
		{
		    	atdl4jUserMessageHandler = Atdl4jConfig.getConfig().createAtdl4jUserMessageHandler();
		}		
		return atdl4jUserMessageHandler;
	}
}
