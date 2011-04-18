package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.impl.AbstractStrategiesUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTStrategiesUI
	extends AbstractStrategiesUI
{
	private final Logger logger = Logger.getLogger( SWTStrategiesUI.class );

	private Composite strategiesPanel;
	
	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SWTStrategiesUI()
	{
	}

	public SWTStrategiesUI(Atdl4jOptions aAtdl4jOptions)
	{
		init(aAtdl4jOptions);
	}

public void init(Atdl4jOptions aAtdl4jOptions)
{
	setAtdl4jOptions( aAtdl4jOptions );
}
	
public Object buildStrategiesPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
{
	return buildStrategiesPanel( (Composite) parentOrShell, atdl4jOptions, aAtdl4jUserMessageHandler );
}

public Composite buildStrategiesPanel(Composite aParentComposite, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
{
	setAtdl4jOptions( atdl4jOptions );

	setAtdl4jUserMessageHandler( aAtdl4jUserMessageHandler );

	// Main strategies panel
	strategiesPanel = new Composite( aParentComposite, SWT.NONE );
	FillLayout strategiesLayout = new FillLayout( SWT.VERTICAL );
	strategiesPanel.setLayout( strategiesLayout );
	
	return strategiesPanel;
}

public void removeAllStrategyPanels()
{
	// remove all strategy panels
	for ( Control control : strategiesPanel.getChildren() )
		control.dispose();
}

public void createStrategyPanels(StrategiesT aStrategies, List<StrategyT> aFilteredStrategyList) throws FIXatdlFormatException
{
	// -- Check to see if StrategiesT has changed (eg new file loaded) --
	if ( ( getStrategies() == null ) || ( ! getStrategies().equals( aStrategies ) ) )
	{
		setStrategies( aStrategies );
		
		setStrategiesRules( new HashMap<String, ValidationRule>() );
		for (EditT edit : getStrategies().getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit,
						getStrategiesRules(), getStrategies());
				getStrategiesRules().put(id, rule);
			} else {
				throw new IllegalArgumentException("Strategies-scoped edit without id");
			}
		}
	}
			
	setPreCached( false );
	setCurrentlyDisplayedStrategyUI( null );

	for ( StrategyT strategy : aFilteredStrategyList )
	{
		/********************************************************************************************
		 * 6/23/2010 Scott Atwell
		 * Note that we are creating the StrategyUI for each Strategy in the file, however, we are 
		 * disposing/discarding the previously built StrategyUI instances.  
		 * SWTStrategiesPanel will have at most only one StrategyUI that has not been disposed.
		 * This is required to avoid very large FIXatdl instance files (or multiple instances of SWTAtdl4jCompositePanel with own file)
		 * consuming tons of Windows USER Objects and generating "org.eclipse.swt.SWTError: No more handles"
		 *********************************************************************************************/
		removeAllStrategyPanels();
		StrategyUI ui = SWTStrategyUIFactory.createStrategyUIAndContainer( this, strategy );
		setCurrentlyDisplayedStrategyUI( ui );

		if ( ui == null )
		{
			// skip to next strategy
			continue;
		}
	}
	setPreCached( true );
}  


public void adjustLayoutForSelectedStrategy( StrategyT aStrategy )
{
	if ( strategiesPanel != null )
	{
		// -- (aReinitPanelFlag=true) --
		StrategyUI tempStrategyUI = getStrategyUI( aStrategy, true );
		
		if ( tempStrategyUI == null  )
		{
			logger.info("ERROR:  Strategy name: " + aStrategy.getName() + " was not found.  (aStrategy: " + aStrategy + ")" );
			return;
		}
		
		logger.debug( "Invoking  tempStrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );								
		tempStrategyUI.reinitStrategyPanel();

		strategiesPanel.layout();
	}
}


/* 
 * Doesn't really work for SWT.
 * (non-Javadoc)
 * @see org.atdl4j.ui.app.StrategiesPanel#setVisible(boolean)
 */
@Override
public void setVisible(boolean aVisible)
{
	if ( strategiesPanel != null )
	{
		strategiesPanel.setVisible( aVisible );
	}
}


// 12/15/2010 Scott Atwell public StrategyUI getStrategyUI( StrategyT aStrategy )
public StrategyUI getStrategyUI( StrategyT aStrategy, boolean aReinitPanelFlag )
{
	if ( aStrategy.equals( getCurrentlyDisplayedStrategy() ) )
	{
		logger.debug("Strategy name: " + aStrategy.getName() + " is currently being displayed.  Returning getCurrentlyDisplayedStrategyUI()" );
// 12/15/2010 Scott Atwell return getCurrentlyDisplayedStrategyUI();
		if ( aReinitPanelFlag )
		{
			getCurrentlyDisplayedStrategyUI().reinitStrategyPanel();
		}
		
		return getCurrentlyDisplayedStrategyUI();
	}
	else
	{
		logger.debug("Strategy name: " + aStrategy.getName() + " is not currently displayed.  Invoking removeAllStrategyPanels() and returning createStrategyPanel()" );
		removeAllStrategyPanels();

		StrategyUI tempStrategyUI = SWTStrategyUIFactory.createStrategyUIAndContainer( this, aStrategy );
		setCurrentlyDisplayedStrategyUI( tempStrategyUI );
		
		logger.debug("Invoking relayoutCollapsibleStrategyPanels() for: " + aStrategy.getName() );
		tempStrategyUI.relayoutCollapsibleStrategyPanels();
		
		return tempStrategyUI;
	}
}

/**
 * @return the strategiesPanel
 */
protected Composite getStrategiesPanel()
{
	return this.strategiesPanel;
}

/**
 * @param aStrategiesPanel the strategiesPanel to set
 */
protected void setStrategiesPanel(Composite aStrategiesPanel)
{
	this.strategiesPanel = aStrategiesPanel;
}

}
