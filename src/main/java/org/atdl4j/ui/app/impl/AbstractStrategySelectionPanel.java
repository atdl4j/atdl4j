/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.app.impl;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.StrategySelectionPanel;
import org.atdl4j.ui.app.StrategySelectionPanelListener;

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

	private Atdl4jOptions atdl4jOptions = null;
	
	private List<StrategySelectionPanelListener> listenerList = new Vector<StrategySelectionPanelListener>();

	private List<StrategyT> strategiesList;

	/**
	 * @param atdl4jOptions the atdl4jOptions to set
	 */
	protected void setAtdl4jOptions(Atdl4jOptions atdl4jOptions)
	{
		this.atdl4jOptions = atdl4jOptions;
	}


	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return atdl4jOptions;
	}
	

	public void addListener( StrategySelectionPanelListener aStrategySelectionPanelListener )
	{
		listenerList.add( aStrategySelectionPanelListener );
	}

	public void removeListener( StrategySelectionPanelListener aStrategySelectionPanelListener )
	{
		listenerList.remove( aStrategySelectionPanelListener );
	}	
	
	protected void fireStrategySelectedEvent( StrategyT aStrategy )
	{  
		for ( StrategySelectionPanelListener tempListener : listenerList )
        	{
        		tempListener.strategySelected( aStrategy );
        	}
	}


	/**
	 * @param strategiesList the strategiesList to set
	 */
	protected void setStrategiesList(List<StrategyT> strategiesList)
	{
		this.strategiesList = strategiesList;
	}


	/**
	 * @return the strategiesList
	 */
	protected List<StrategyT> getStrategiesList()
	{
		return strategiesList;
	}
}
