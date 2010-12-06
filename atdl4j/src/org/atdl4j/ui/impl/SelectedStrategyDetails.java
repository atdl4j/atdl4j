package org.atdl4j.ui.impl;

import java.util.Collection;

import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.StrategyUI;

/**
 * 
 * This class contains the data associated with the <code>SelectedStrategyDetails</code>.
 * 
 * Creation date: (Dec 4, 2010 9:13:14 AM)
 * @author Scott Atwell
 * @version 1.0, Dec 4, 2010
 */
public class SelectedStrategyDetails
{
	private StrategiesT strategies;
	private StrategyT selectedStrategy;
	private StrategyUI strategyUI;
	
	public SelectedStrategyDetails(StrategiesT aStrategies, StrategyT aSelectedStrategy, StrategyUI aStrategyUI)
	{
		setStrategies( aStrategies );
		setSelectedStrategy( aSelectedStrategy );
		setStrategyUI( aStrategyUI );
	}
	
	/**
	 * @return the strategies
	 */
	public StrategiesT getStrategies()
	{
		return this.strategies;
	}
	/**
	 * @param aStrategies the strategies to set
	 */
	protected void setStrategies(StrategiesT aStrategies)
	{
		this.strategies = aStrategies;
	}
	/**
	 * @return the selectedStrategy
	 */
	public StrategyT getSelectedStrategy()
	{
		return this.selectedStrategy;
	}
	/**
	 * @param aSelectedStrategy the selectedStrategy to set
	 */
	protected void setSelectedStrategy(StrategyT aSelectedStrategy)
	{
		this.selectedStrategy = aSelectedStrategy;
	}
	/**
	 * @return the strategyUI
	 */
	protected StrategyUI getStrategyUI()
	{
		return this.strategyUI;
	}
	/**
	 * @param aStrategyUI the strategyUI to set
	 */
	protected void setStrategyUI(StrategyUI aStrategyUI)
	{
		this.strategyUI = aStrategyUI;
	}

	/**
	 * Returns tag=value FIX Message fragment corresponding to Strategies settings and 
	 * the selected Strategy's Parameters and bound Controls
	 * @return
	 */
	public String getFixMsgFragment()
	{
		if ( getStrategyUI() != null )
		{
			return getStrategyUI().getFIXMessage();
		}
		
		return null;
	}
	
	/**
	 * Returns Collection of Atdl4jWidget which contains both FIXatdl Control, 
	 * its bound Parameter, and Control's current wireValue.
	 * @return
	 */
	public Collection<Atdl4jWidget<?>> getAtdl4jWidgetList()
	{
		if ( ( getStrategyUI() != null ) && ( getStrategyUI().getAtdl4jWidgetMap() != null ) )
		{
			return getStrategyUI().getAtdl4jWidgetMap().values();
		}
		
		return null;
		
	}
	
	/**
	 * Helper method providing the Strategy/AuiRep, if provided, otherwise, the Strategy/@name
	 * @return
	 */
	public String getSelectedStrategyUiRepOrName()
	{
		return Atdl4jHelper.getStrategyUiRepOrName( getSelectedStrategy() );
	}
}
