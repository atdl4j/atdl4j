package org.atdl4j.ui.swt.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.AbstractStrategiesPanel;
import org.atdl4j.ui.swt.impl.SWTStrategyUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Represents the SWT-specific available strategy choices GUI component.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 */
public class SWTStrategiesPanel
		extends AbstractStrategiesPanel
{
	private final Logger logger = Logger.getLogger( SWTStrategiesPanel.class );

	private Composite strategiesPanel;
	private List<StrategyUI> strategyUIList;

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
//		GridLayout strategiesLayout = new GridLayout( 1, false );
//		strategiesLayout.verticalSpacing = 0;
//		strategiesPanel.setLayout( strategiesLayout );
//		strategiesPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
// 4/16/2010 Scott Atwell switched to StackLayout (equivalent of Swing's CardLayout)		
		StackLayout strategiesLayout = new StackLayout();
		strategiesPanel.setLayout( strategiesLayout );
		
		return strategiesPanel;
	}

	public void removeAllStrategyPanels()
	{
		// remove all strategy panels
		for ( Control control : strategiesPanel.getChildren() )
			control.dispose();
	}

// 6/23/2010 Scott Atwell added moving logic from createStrategiesPanels() loop	
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
// TODO 6/23/2010 Scott Atwell				continue;
				return null;
			} 

 			
// TODO 6/23/2010 Scott Atwell		getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );
////4/2/2010 Scott Atwell added
// TODO 6/23/2010 Scott Atwell		strategyUIList.add( ui );

		ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );

		return ui;
	}

	public void createStrategyPanels(List<StrategyT> aFilteredStrategyList)
	{
		StrategiesUI<?> strategiesUI = null;
		setPreCached( false );
		
		try
		{
			StrategiesUIFactory factory = getAtdl4jConfig().getStrategiesUIFactory();
			strategiesUI = factory.create( getAtdl4jConfig().getStrategies(), getAtdl4jConfig() );
		}
		catch (Exception e)
		{
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
					"Error creating StrategiesUIFactory and StrategisUI", e );
			return;
		}
		
		getAtdl4jConfig().setStrategyUIMap( new HashMap<StrategyT, StrategyUI>() );
// 4/2/2010 Scott Atwell added		
		strategyUIList = new ArrayList<StrategyUI>();		
		
		for ( StrategyT strategy : aFilteredStrategyList )
		{
/*** 6/23/2010 Scott Atwell moved logic to createStrategyPanel()			
			// create composite
			Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
			//strategyParent.setLayout( new FillLayout() );
			
// 4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			GridLayout strategyParentLayout = new GridLayout( 1, false );
// 4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParentLayout.verticalSpacing = 0;
// 4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParent.setLayout( strategyParentLayout );
// 4/17/2010 Scott Atwell (using StackLayout vs. GridLayout)			strategyParent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
// 4/17/2010 Scott Atwell use GridLayout of 2 columns to support additional component to occupy the right and bottom space to support proper fit of main component			
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
// 4/2/2010 Scott Atwell added
			strategyUIList.add( ui );

			ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
***  6/23/2010 Scott Atwell ****/
			StrategyUI ui = createStrategyPanel( strategiesUI, strategy );
			if ( ui == null )
			{
				// skip to next strategy
				continue;
			}
			getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );
// 4/2/2010 Scott Atwell added
			strategyUIList.add( ui );
			
		}

/** 4/2/2010 Scott Atwell - this is already been handled by AbstractAtdl4jCompositePanel.loadScreenWithFilteredStrategies()		
		// -- Force the display to only show the Composite panels for the first strategy, otherwise the first screen is a jumbled mess of all strategy's parameters sequentially --
		if ( getAtdl4jConfig().getStrategyUIMap().size() > 0 )
		{
			adjustLayoutForSelectedStrategy( 0 );
		}
**/		
		setPreCached( true );
	}  

/***** 4/16/2010 Scott Atwell -- before switch to use StackLayout
// 4/16/2010 Scott Atwell	public void adjustLayoutForSelectedStrategy(int aIndex)
	public void adjustLayoutForSelectedStrategy( StrategyT aStrategy )
	{
		if ( strategiesPanel != null )
		{
// 4/16/2010 Scott Atwell added to obtain index for aStrategy			
			int tempIndex = getIndexOfStrategyUIPanel( aStrategy );
			logger.debug("adjustLayoutForSelectedStrategy() for aStrategy.getName(): " + aStrategy.getName() + " StrategyUI panel index: " + tempIndex );
			
			if ( tempIndex < 0 )
			{
				logger.info("ERROR:  getIndexOfStrategyUIPanel() for: " + aStrategy.getName() + " (aStrategy: " + aStrategy + " was not found (" + tempIndex + " was returned)." );
				return;
			}
			
// 4/2/2010 Scott Atwell added
			// -- Reduce screen re-draw/flash (doesn't really work for SWT, though) --
			setVisible( false );

			// -- These were the remnants from selectDropDownStrategy(int index) that did not become part of StrategySelectionPanel
			for (int i = 0; i < strategiesPanel.getChildren().length; i++) 
			{
				Control tempControl = strategiesPanel.getChildren()[i];
				if ( tempControl != null ) 
				{
					GridData tempGridData = (GridData) tempControl.getLayoutData();
					if ( tempGridData != null )
					{
						tempGridData.heightHint = (i != tempIndex) ? 0 : -1;
						tempGridData.widthHint = (i != tempIndex) ? 0 : -1;

						if (i == tempIndex) 
						{
							Composite tempComposite = (Composite) tempControl;

// 4/2/2010 Scott Atwell added							
							if ( ( strategyUIList != null ) && ( strategyUIList.size() > tempIndex ) )
							{
								StrategyUI tempStrategyUI = strategyUIList.get(  tempIndex ); 
								if ( tempStrategyUI != null )
								{
									logger.debug( "Invoking  tempStrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );								
									tempStrategyUI.reinitStrategyPanel();
								}
							}
							
							
							//tempComposite.pack();
							tempComposite.getParent().layout(true, true);
						}
					}
				}
			}

// 4/2/2010 Scott Atwell added
			// -- Reduce screen re-draw/flash (doesn't really work for SWT, though) --
			setVisible( true );
			
			
			strategiesPanel.layout();
		}
	}
****/

// 4/16/2010 Scott Atwell - added to use with StackLayout	
	public void adjustLayoutForSelectedStrategy( StrategyT aStrategy )
	{
		if ( strategiesPanel != null )
		{
			StrategyUI tempStrategyUI = getAtdl4jConfig().getStrategyUIMap().get( aStrategy );
			
			if ( tempStrategyUI == null  )
			{
				logger.info("ERROR:  Strategy name: " + aStrategy.getName() + " not found in getAtdl4jConfig().getStrategyUIMap().  (aStrategy: " + aStrategy + ")" );
				return;
			}

			SWTStrategyUI tempSWTStrategyUI = (SWTStrategyUI) tempStrategyUI;
			
			((StackLayout) strategiesPanel.getLayout()).topControl = tempSWTStrategyUI.getParent();

			logger.debug( "Invoking  tempStrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );								
			tempStrategyUI.reinitStrategyPanel();

			strategiesPanel.layout();
		}
	}

	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategiesPanel#reinitStrategyPanels()
	 */
	@Override
	public void reinitStrategyPanels()
	{
		for ( StrategyUI tempStrategyUI : getAtdl4jConfig().getStrategyUIMap().values() )
		{
			logger.info( "Invoking StrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );

			tempStrategyUI.reinitStrategyPanel();
		}

/*** 4/2/2010 Scott Atwell		
		// -- Force the display to only show the Composite panels for the first strategy, otherwise the first screen is a jumbled mess of all strategy's parameters sequentially --
		if ( getAtdl4jConfig().getStrategyUIMap().size() > 0 )
		{
			adjustLayoutForSelectedStrategy( 0 );
		}
***/		
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
	
/*** 4/16/2010 Scott Atwell
	protected int getIndexOfStrategyUIPanel( StrategyT aStrategy )
	{
		StrategyUI tempMatchingStrategyUI = null;
		
		for ( StrategyUI tempStrategyUI : getAtdl4jConfig().getStrategyUIMap().values() )
		{
			if ( tempStrategyUI.getStrategy().equals( aStrategy ) )
			{
				tempMatchingStrategyUI = tempStrategyUI;
				break;
			}
		}

		if ( tempMatchingStrategyUI != null )
		{
			return strategyUIList.indexOf( tempMatchingStrategyUI );
		}
		else
		{
			return -1;  // -- not found --
		}
	}
***/	
}
