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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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
		GridLayout strategiesLayout = new GridLayout( 1, false );
		strategiesLayout.verticalSpacing = 0;
		strategiesPanel.setLayout( strategiesLayout );
		strategiesPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

		return strategiesPanel;
	}

	public void removeAllStrategyPanels()
	{
		// remove all strategy panels
		for ( Control control : strategiesPanel.getChildren() )
			control.dispose();
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
			// create composite
			Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
			//strategyParent.setLayout( new FillLayout() );
			
			GridLayout strategyParentLayout = new GridLayout( 1, false );
			strategyParentLayout.verticalSpacing = 0;
			strategyParent.setLayout( strategyParentLayout );
			strategyParent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
			
			StrategyUI ui;

			// build strategy and catch strategy-specific errors
				try {
					ui = strategiesUI.createUI( strategy, strategyParent );
//	4/4/2010 Scott Atwell			} catch (JAXBException e) {
				} catch (Throwable e) {
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


	public void adjustLayoutForSelectedStrategy(int aIndex)
	{
		if ( strategiesPanel != null )
		{
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
						tempGridData.heightHint = (i != aIndex) ? 0 : -1;
						tempGridData.widthHint = (i != aIndex) ? 0 : -1;

						if (i == aIndex) 
						{
							Composite tempComposite = (Composite) tempControl;

// 4/2/2010 Scott Atwell added							
							if ( ( strategyUIList != null ) && ( strategyUIList.size() > aIndex ) )
							{
								StrategyUI tempStrategyUI = strategyUIList.get(  aIndex ); 
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
}
