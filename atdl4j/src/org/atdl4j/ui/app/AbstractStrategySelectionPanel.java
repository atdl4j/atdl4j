/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Represents the base, non-GUI system-specific available strategy choices component.
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public abstract class AbstractStrategySelectionPanel
		implements StrategySelectionPanel
{
	public final Logger logger = Logger.getLogger(AbstractStrategySelectionPanel.class);

	private Atdl4jConfig atdl4jConfig = null;
	
	private List<StrategySelectionPanelListener> listenerList = new Vector<StrategySelectionPanelListener>();

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
	

	public void addListener( StrategySelectionPanelListener aStrategySelectionPanelListener )
	{
		listenerList.add( aStrategySelectionPanelListener );
	}

	public void removeListener( StrategySelectionPanelListener aStrategySelectionPanelListener )
	{
		listenerList.remove( aStrategySelectionPanelListener );
	}	
	
// 4/16/2010 Scott Atwell	protected void fireStrategySelectedEvent( StrategyT aStrategy, int index )
	protected void fireStrategySelectedEvent( StrategyT aStrategy )
	{
		for ( StrategySelectionPanelListener tempListener : listenerList )
		{
// 4/16/2010 Scott Atwell			tempListener.strategySelected( aStrategy, index );
			tempListener.strategySelected( aStrategy );
		}
	}
}
