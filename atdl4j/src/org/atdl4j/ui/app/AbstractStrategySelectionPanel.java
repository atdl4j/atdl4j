/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
	private Atdl4jConfig atdl4jConfig = null;
	
	private List<StrategySelectionPanelListener> listenerList = new Vector<StrategySelectionPanelListener>();



	/**
	 * Uses, if specified, Atdl4jConfig.InputAndFilterData.getInputStrategyUiRepOrNameList() 
	 * and Atdl4jConfig.InputAndFilterData.getApplyInputStrategyUiRepOrNameListAsFilter() 
	 * to control the order presented to the user and, if so desired, exclude strategies against the available aStrategyList
	 * 
	 * @param aStrategyList
	 * @return
	 */
	public List<String> getStrategyUiRepOrNameList( List<StrategyT> aStrategyList )
	{
		if ( aStrategyList == null )
		{
			return null;
		}

		List<String> tempAvailableStrategyNameList = new ArrayList<String>();

		// -- Build list all strategy names --
		for (StrategyT strategy : aStrategyList) 
		{
			tempAvailableStrategyNameList.add(Atdl4jHelper.getStrategyUiRepOrName(strategy));
		}
		
		List tempStrategyNameList;
		
		if ( ( getAtdl4jConfig() != null ) && 
			  ( getAtdl4jConfig().getInputAndFilterData() != null ) &&
			  ( getAtdl4jConfig().getInputAndFilterData().getInputStrategyUiRepOrNameList() != null ) && 
			  ( getAtdl4jConfig().getInputAndFilterData().getInputStrategyUiRepOrNameList().size() > 0  ) )
		{
			tempStrategyNameList = new ArrayList<String>();
			
			for (String tempStrategy : getAtdl4jConfig().getInputAndFilterData().getInputStrategyUiRepOrNameList() )
			{
				// -- Ensure that tempStrategy is part of the available list --
				if ( tempAvailableStrategyNameList.contains( tempStrategy ) )
				{
					tempStrategyNameList.add( tempStrategy );
				}
			}

			// -- Add any other strategNames to the end unless setting specifies the input list is a filter --
			if ( ! Boolean.TRUE.equals( getAtdl4jConfig().getInputAndFilterData().getApplyInputStrategyUiRepOrNameListAsFilter() ) )
			{
				for (String tempStrategy : tempAvailableStrategyNameList )
				{
					// -- Only add to the end if not already in the list --
					if ( ! tempStrategyNameList.contains( tempStrategy ) )
					{
						tempStrategyNameList.add( tempStrategy );
					}
				}
			}
		}
		else
		{
			// -- use all available Strategies with order as-is -- 
			tempStrategyNameList = tempAvailableStrategyNameList;
		}
			  
		return tempStrategyNameList;
	}

	
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
	
	protected void fireStrategySelectedEvent( StrategyT aStrategy, int index )
	{
		for ( StrategySelectionPanelListener tempListener : listenerList )
		{
			tempListener.strategySelected( aStrategy, index );
		}
	}
}
