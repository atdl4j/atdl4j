/*
 * Created on Feb 26, 2010
 *
 */
package org.atdl4j.ui.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.AtdlWidgetListener;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.StrategiesUIListener;

/**
 * Represents the base, non-GUI system-specific display panel of availabel FIXatdl strategies (StrategiesUI).
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public abstract class AbstractStrategiesUI
		implements StrategiesUI
{
	protected static final Logger logger = Logger.getLogger( AbstractStrategiesUI.class );
	
	private Atdl4jOptions atdl4jOptions = null;

	private List<StrategiesUIListener> listenerList = new Vector<StrategiesUIListener>();
	
	private List<AtdlWidgetListener> widgetListeners = new ArrayList<AtdlWidgetListener>();

	private boolean preCached = false;
	
	private Atdl4jUserMessageHandler atdl4jUserMessageHandler = null;

	private StrategyUI currentlyDisplayedStrategyUI = null;
	private Map<String, ValidationRule> strategiesRules;
	private StrategiesT strategies;

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
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler()
	{
		return atdl4jUserMessageHandler;
	}


	/**
	 * @return the currentlyDisplayedStrategyUI
	 */
	public StrategyUI getCurrentlyDisplayedStrategyUI()
	{
		return this.currentlyDisplayedStrategyUI;
	}


	/**
	 * @param aCurrentlyDisplayedStrategyUI the currentlyDisplayedStrategyUI to set
	 */
	protected void setCurrentlyDisplayedStrategyUI(StrategyUI aCurrentlyDisplayedStrategyUI)
	{
		this.currentlyDisplayedStrategyUI = aCurrentlyDisplayedStrategyUI;
	}


	/**
	 * @return the strategiesRules
	 */
	public Map<String, ValidationRule> getStrategiesRules()
	{
		return this.strategiesRules;
	}


	/**
	 * @param aStrategiesRules the strategiesRules to set
	 */
	protected void setStrategiesRules(Map<String, ValidationRule> aStrategiesRules)
	{
		this.strategiesRules = aStrategiesRules;
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
	
	@Override
	public void addWidgetListener(AtdlWidgetListener listener) {
	  widgetListeners.add(listener);
	}
	
	@Override
	public void removeWidgetListener(AtdlWidgetListener listener) {
	  widgetListeners.remove(listener);
	}
	
	protected void fireWidgetChanged(Atdl4jWidget w)
	{
	  for (AtdlWidgetListener listener: widgetListeners)
	  {
	    listener.widgetChanged(w);
	  }
	}
	
	
	
}
