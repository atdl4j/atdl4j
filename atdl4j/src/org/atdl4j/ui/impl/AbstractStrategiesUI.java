/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.impl;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.StrategiesUIListener;

/**
 * Represents the base, non-GUI system-specific display panel of availabel FIXatdl strategies (StrategiesUI).
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public abstract class AbstractStrategiesUI
// 9/13/2010 Scott Atwell		implements StrategiesPanel
		implements StrategiesUI
{
	private Atdl4jConfig atdl4jConfig = null;

	private List<StrategiesUIListener> listenerList = new Vector<StrategiesUIListener>();

	private boolean preCached = false;

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
	
	public void addListener( StrategiesUIListener aStrategiesUIListener )
	{
		listenerList.add( aStrategiesUIListener );
	}

	public void removeListener( StrategiesUIListener aStrategiesUIListener )
	{
		listenerList.remove( aStrategiesUIListener );
	}	
	
	protected void fireStrategySelectedEvent( StrategyT aStrategy, int index )
	{
		for ( StrategiesUIListener tempListener : listenerList )
		{
			tempListener.strategySelected( aStrategy, index );
		}
	}


	/**
	 * @return the preCached
	 */
	public boolean isPreCached()
	{
		return this.preCached;
	}


	/**
	 * @param aPreCached the preCached to set
	 */
	public void setPreCached(boolean aPreCached)
	{
		this.preCached = aPreCached;
	}
	
// 6/23/2010 Scott Atwell added
	public StrategyT getCurrentlyDisplayedStrategy()
	{
		StrategyUI tempStrategyUI = getCurrentlyDisplayedStrategyUI();
		if ( tempStrategyUI != null )
		{
			return tempStrategyUI.getStrategy();
		}
		else
		{
			return null;
		}
	}

}
