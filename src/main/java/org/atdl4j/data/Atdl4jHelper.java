/*
 * Created on Feb 27, 2010
 *
 */
package org.atdl4j.data;

import org.atdl4j.fixatdl.core.StrategyT;

/**
 * 
 * This class contains the data associated with the <code>Atdl4jHelper</code>.
 * 
 * Creation date: (Feb 27, 2010 12:34:30 AM)
 * @author Scott Atwell
 * @version 1.0, Feb 27, 2010
 */
public class Atdl4jHelper
{
	/**
	 * @param strategy
	 * @return
	 */
	public static String getStrategyUiRepOrName(StrategyT strategy) 
	{
		if ( strategy == null )
		{
			return null;
		}
		
		if (strategy.getUiRep() != null) 
		{
			return strategy.getUiRep();
		} 
		else 
		{
			return strategy.getName();
		}
	}


	public static boolean isStrategyNameValid( String aStrategyName )
	{
		return aStrategyName.matches( Atdl4jConstants.PATTERN_STRATEGY_NAME );
	}
}
