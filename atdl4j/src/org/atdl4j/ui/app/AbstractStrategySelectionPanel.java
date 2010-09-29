/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
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

	private Atdl4jOptions atdl4jOptions = null;
	
	private List<StrategySelectionPanelListener> listenerList = new Vector<StrategySelectionPanelListener>();

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
