package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.impl.AbstractStrategiesUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

// 9/13/2010 Scott Atwell public class SWTStrategiesUI implements StrategiesUI<Composite> {
public class SWTStrategiesUI
	extends AbstractStrategiesUI
{
	private final Logger logger = Logger.getLogger( SWTStrategiesUI.class );

// -- 9/13/2010 Scott Atwell below from SWTStrategiesPanel	
	private Composite strategiesPanel;
//TODO	StrategiesUI<?> strategiesUI = null;
	StrategyUI currentlyDisplayedStrategyUI = null;

	
	private Map<String, ValidationRule> strategiesRules;
	private StrategiesT strategies;
	private Atdl4jConfig atdl4jConfig;

	
	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SWTStrategiesUI()
	{
	}

/** TODO 9/27/2010 Scott Atwell	
	public SWTStrategiesUI(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
	{
		init(strategies, aAtdl4jConfig);
	}
**/	
	public SWTStrategiesUI(Atdl4jConfig aAtdl4jConfig)
	{
		init(aAtdl4jConfig);
	}

/** TODO 9/27/2010 Scott Atwell	
	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
	{
		this.strategies = strategies;
		setAtdl4jConfig( aAtdl4jConfig );

		strategiesRules = new HashMap<String, ValidationRule>();
		for (EditT edit : strategies.getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit,
						strategiesRules, strategies);
				strategiesRules.put(id, rule);
			} else {
				throw new IllegalArgumentException("Strategies-scoped edit without id");
			}
		}
	}
**/
public void init(Atdl4jConfig aAtdl4jConfig)
{
	setAtdl4jConfig( aAtdl4jConfig );
}
	
public StrategyUI createUI(StrategyT strategy, Composite parent)
{
//TODO 9/27/2010 Scott Atwell	return getAtdl4jConfig().getStrategyUI( strategy, strategiesRules, parent );
	return getAtdl4jConfig().getStrategyUI( strategy, strategies, strategiesRules, parent );
}

public StrategyUI createUI(StrategyT strategy, Object parent)
{
	return createUI( strategy, (Composite) parent);
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



public Object buildStrategiesPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig)
{
	return buildStrategiesPanel( (Composite) parentOrShell, atdl4jConfig );
}

public Composite buildStrategiesPanel(Composite aParentComposite, Atdl4jConfig atdl4jConfig)
{
	setAtdl4jConfig( atdl4jConfig );

	if ( ( atdl4jConfig != null ) && ( atdl4jConfig.getAtdl4jUserMessageHandler() != null )
			&& ( atdl4jConfig.getAtdl4jUserMessageHandler().isInitReqd() ) )
	{
		atdl4jConfig.initAtdl4jUserMessageHandler( aParentComposite );
	}

	// Main strategies panel
	strategiesPanel = new Composite( aParentComposite, SWT.NONE );
//	GridLayout strategiesLayout = new GridLayout( 1, false );
//	strategiesLayout.verticalSpacing = 0;
//	strategiesPanel.setLayout( strategiesLayout );
//	strategiesPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
//4/16/2010 Scott Atwell switched to StackLayout (equivalent of Swing's CardLayout)		
//6/23/2010 Scott Atwell abandoned StackLayout due to No More Handles		StackLayout strategiesLayout = new StackLayout();
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

//6/23/2010 Scott Atwell added, moving logic from createStrategiesPanels() loop	
public StrategyUI createStrategyPanel(StrategiesUI aStrategiesUI, StrategyT aStrategy)
{
	// create composite
	Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
	//strategyParent.setLayout( new FillLayout() );
	
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			GridLayout strategyParentLayout = new GridLayout( 1, false );
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParentLayout.verticalSpacing = 0;
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParent.setLayout( strategyParentLayout );
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
//4/17/2010 Scott Atwell use GridLayout of 2 columns to support additional component to occupy the right and bottom space to support proper fit of main component			
	GridLayout strategyParentLayout = new GridLayout( 2, false );
	strategyParentLayout.verticalSpacing = 0;
	strategyParent.setLayout( strategyParentLayout );
	
	StrategyUI ui;

	// build strategy and catch strategy-specific errors
		try 
		{
			ui = aStrategiesUI.createUI( aStrategy, strategyParent );
			
			// 4/17/2010 Scott Atwell -- add additional components to take up space on left and bottom
			Label tempLabel = new Label( strategyParent, SWT.NONE );
			tempLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );					
			Label tempLabel2 = new Label( strategyParent, SWT.NONE );
			tempLabel2.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );					
			Label tempLabel3 = new Label( strategyParent, SWT.NONE );
			tempLabel3.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );					
		} 
		catch (Throwable e) 
		{
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
					"Error in Strategy: " + Atdl4jHelper.getStrategyUiRepOrName( aStrategy ), e );

			// rollback changes
			strategyParent.dispose();

			// skip to next strategy
//TODO 6/23/2010 Scott Atwell				continue;
			return null;
		} 

			
//TODO 6/23/2010 Scott Atwell		getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );

	ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );

//6/23/2010 Scott Atwell added		
	currentlyDisplayedStrategyUI = ui;
	
	return ui;
}

/** TODO 9/27/2010 Scott Atwell rewrote
 * public void createStrategyPanels(List<StrategyT> aFilteredStrategyList)
{
//6/23/2010 Scott Atwell moved up		StrategiesUI<?> strategiesUI = null;
	setPreCached( false );
	currentlyDisplayedStrategyUI = null;

*//** TODO 9/26/2010 Scott Atwell	
	try
	{
		StrategiesUIFactory factory = getAtdl4jConfig().getStrategiesUIFactory();
		strategiesUI = factory.create( getAtdl4jConfig().getStrategies(), getAtdl4jConfig() );
	}
	catch (Exception e)
	{
		getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
				"Error creating StrategiesUIFactory and StrategiesUI", e );
		return;
	}
**//*
	
//6/23/2010 Scott Atwell		getAtdl4jConfig().setStrategyUIMap( new HashMap<StrategyT, StrategyUI>() );

	for ( StrategyT strategy : aFilteredStrategyList )
	{
*//*** 6/23/2010 Scott Atwell moved logic to createStrategyPanel()			
		// create composite
		Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
		//strategyParent.setLayout( new FillLayout() );
		
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			GridLayout strategyParentLayout = new GridLayout( 1, false );
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParentLayout.verticalSpacing = 0;
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParent.setLayout( strategyParentLayout );
//4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
//4/17/2010 Scott Atwell use GridLayout of 2 columns to support additional component to occupy the right and bottom space to support proper fit of main component			
		GridLayout strategyParentLayout = new GridLayout( 2, false );
		strategyParentLayout.verticalSpacing = 0;
		strategyParent.setLayout( strategyParentLayout );
		
		StrategyUI ui;

		// build strategy and catch strategy-specific errors
			try 
			{
				ui = strategiesUI.createUI( strategy, strategyParent );
				
				// 4/17/2010 Scott Atwell -- add additional components to take up space on left and bottom
				Label tempLabel = new Label( strategyParent, SWT.NONE );
				tempLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );					
				Label tempLabel2 = new Label( strategyParent, SWT.NONE );
				tempLabel2.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );					
				Label tempLabel3 = new Label( strategyParent, SWT.NONE );
				tempLabel3.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );					
			} 
			catch (Throwable e) 
			{
				getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
						"Error in Strategy: " + Atdl4jHelper.getStrategyUiRepOrName( strategy ), e );

				// rollback changes
				strategyParent.dispose();

				// skip to next strategy
				continue;
			} 

		getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );
//4/2/2010 Scott Atwell added
		strategyUIList.add( ui );

		ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
***  6/23/2010 Scott Atwell ****//*
		*//********************************************************************************************
		 * 6/23/2010 Scott Atwell
		 * Note that we are creating the StrategyUI for each Strategy in the file, however, we are 
		 * disposing/discarding the previously built StrategyUI instances.  
		 * SWTStrategiesPanel will have at most only one StrategyUI that has not been disposed.
		 * This is required to avoid very large FIXatdl instance files (or multiple instances of SWTAtdl4jCompositePanel with own file)
		 * consuming tons of Windows USER Objects and generating "org.eclipse.swt.SWTError: No more handles"
		 *********************************************************************************************//*
		removeAllStrategyPanels();
//TODO		StrategyUI ui = createStrategyPanel( strategiesUI, strategy );
		StrategyUI ui = createStrategyPanel( this, strategy );
		if ( ui == null )
		{
			// skip to next strategy
			continue;
		}
//TODO 6/23/2010			getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );
		
	}
	setPreCached( true );
}  
*/

//TODO 9/27/2010 Scott Atwell rewrote
public void createStrategyPanels(StrategiesT aStrategies, List<StrategyT> aFilteredStrategyList)
{
	// -- Check to see if StrategiesT has changed (eg new file loaded) --
	if ( ( strategies == null ) || ( ! strategies.equals( aStrategies ) ) )
	{
		this.strategies = aStrategies;
		
		strategiesRules = new HashMap<String, ValidationRule>();
		for (EditT edit : strategies.getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit,
						strategiesRules, strategies);
				strategiesRules.put(id, rule);
			} else {
				throw new IllegalArgumentException("Strategies-scoped edit without id");
			}
		}
	}
			
	setPreCached( false );
	currentlyDisplayedStrategyUI = null;

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
//TODO		StrategyUI ui = createStrategyPanel( strategiesUI, strategy );
		StrategyUI ui = createStrategyPanel( this, strategy );
		if ( ui == null )
		{
			// skip to next strategy
			continue;
		}
	}
	setPreCached( true );
}  



//6/23/2010 Scott Atwell added
public StrategyUI getCurrentlyDisplayedStrategyUI()
{
	return currentlyDisplayedStrategyUI;
}


//4/16/2010 Scott Atwell - added to use with StackLayout	
//6/23/2010 Scott Atwell abandoned StackLayout due to No More Handles
public void adjustLayoutForSelectedStrategy( StrategyT aStrategy )
{
	if ( strategiesPanel != null )
	{
//6/23/2010 Scott Atwell			StrategyUI tempStrategyUI = getAtdl4jConfig().getStrategyUIMap().get( aStrategy );
		StrategyUI tempStrategyUI = getAtdl4jConfig().getStrategyUI( aStrategy );
		
		if ( tempStrategyUI == null  )
		{
			logger.info("ERROR:  Strategy name: " + aStrategy.getName() + " was not found.  (aStrategy: " + aStrategy + ")" );
			return;
		}

		SWTStrategyUI tempSWTStrategyUI = (SWTStrategyUI) tempStrategyUI;
		
//6/23/2010 Scott Atwell abandoned StackLayout due to No More Handles ((StackLayout) strategiesPanel.getLayout()).topControl = tempSWTStrategyUI.getParent();
//6/23/2010 Scott Atwell -- note that getStrategyUI(StrategyT) now handles the Composite population/layout
		
		logger.debug( "Invoking  tempStrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );								
		tempStrategyUI.reinitStrategyPanel();

		strategiesPanel.layout();
	}
}


/* (non-Javadoc)
 * @see org.atdl4j.ui.app.StrategiesPanel#reinitStrategyPanels()
 */
/*** 9/27/2010 Scott Atwell removed -- unused (and had reference to getAtdl4jConfig().getSelectedStrategy())
@Override
public void reinitStrategyPanels()
{
*//*** 6/23/2010 Scott Atwell
	for ( StrategyUI tempStrategyUI : getAtdl4jConfig().getStrategyUIMap().values() )
	{
		logger.info( "Invoking StrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );

		tempStrategyUI.reinitStrategyPanel();
	}
***//*
	// -- Only re-init for the selected strategy --
	if ( getAtdl4jConfig().getSelectedStrategy() != null )
	{
		StrategyUI tempStrategyUI = getAtdl4jConfig().getStrategyUI( getAtdl4jConfig().getSelectedStrategy() );
		if ( tempStrategyUI != null )
		{
			logger.info( "Invoking StrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );
			tempStrategyUI.reinitStrategyPanel();
		}
	}
}
***/


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


//6/23/2010 Scott Atwell added
public StrategyUI getStrategyUI( StrategyT aStrategy )
{
	if ( aStrategy.equals( getCurrentlyDisplayedStrategy() ) )
	{
		logger.debug("Strategy name: " + aStrategy.getName() + " is currently being displayed.  Returning getCurrentlyDisplayedStrategyUI()" );
		return getCurrentlyDisplayedStrategyUI();
	}
	else
	{
		logger.debug("Strategy name: " + aStrategy.getName() + " is not currently displayed.  Invoking removeAllStrategyPanels() and returning createStrategyPanel()" );
		removeAllStrategyPanels();

//8/27/2010 Scott Atwell			return createStrategyPanel( strategiesUI, aStrategy );
// TODO		StrategyUI tempStrategyUI = createStrategyPanel( strategiesUI, aStrategy );
		StrategyUI tempStrategyUI = createStrategyPanel( this, aStrategy );
		
//8/27/2010 Scott Atwell added
		logger.debug("Invoking relayoutCollapsibleStrategyPanels() for: " + aStrategy.getName() );
		tempStrategyUI.relayoutCollapsibleStrategyPanels();
		
		return tempStrategyUI;
	}
}



}
