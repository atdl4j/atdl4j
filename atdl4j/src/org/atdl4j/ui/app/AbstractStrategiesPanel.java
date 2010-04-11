/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Represents the base, non-GUI system-specific display panel of availabel FIXatdl strategies (StrategiesUI).
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public abstract class AbstractStrategiesPanel
		implements StrategiesPanel
{
	private Atdl4jConfig atdl4jConfig = null;

	private List<StrategiesPanelListener> listenerList = new Vector<StrategiesPanelListener>();

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
	
	public void addListener( StrategiesPanelListener aStrategiesPanelListener )
	{
		listenerList.add( aStrategiesPanelListener );
	}

	public void removeListener( StrategiesPanelListener aStrategiesPanelListener )
	{
		listenerList.remove( aStrategiesPanelListener );
	}	
	
	protected void fireStrategySelectedEvent( StrategyT aStrategy, int index )
	{
		for ( StrategiesPanelListener tempListener : listenerList )
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
}
