package org.atdl4j.ui.swt.impl;

import org.apache.log4j.Logger;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.impl.BaseStrategyUIFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * This class contains the data associated with the <code>SWTStrategyUIFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 9:05:33 PM)
 * @author Scott Atwell
 */
public class SWTStrategyUIFactory
	extends BaseStrategyUIFactory
{
	protected static final Logger logger = Logger.getLogger( SWTStrategyUIFactory.class );

	//6/23/2010 Scott Atwell added, moving logic from createStrategiesPanels() loop	
// 10/5/2010 Scott Atwell moved from SWTStrategiesUI and renamed	public StrategyUI createStrategyPanel(StrategiesUI aStrategiesUI, StrategyT aStrategy)
	public static StrategyUI createStrategyUIAndContainer(SWTStrategiesUI aStrategiesUI, StrategyT aStrategy)
	{
		// create composite
// 10/5/2010 Scott Atwell		Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
		Composite strategyParent = new Composite( aStrategiesUI.getStrategiesPanel(), SWT.NONE );
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
//TODO 10/5/2010 Scott Atwell (below is what SWTStrategiesUI.createUI() did				ui = aStrategiesUI.createUI( aStrategy, strategyParent );
// 10/5/2010 Scott Atwell				ui = createStrategyUI( strategy, strategies, strategiesRules, parent );
				ui = createStrategyUI( aStrategy, aStrategiesUI.getStrategies(), aStrategiesUI.getStrategiesRules(), strategyParent, aStrategiesUI.getAtdl4jOptions() );
				
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
				getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
						"Error in Strategy: " + Atdl4jHelper.getStrategyUiRepOrName( aStrategy ), e );

				// rollback changes
				strategyParent.dispose();

				// skip to next strategy
	//TODO 6/23/2010 Scott Atwell				continue;
				return null;
			} 

				
	//TODO 6/23/2010 Scott Atwell		getAtdl4jOptions().getStrategyUIMap().put( strategy, ui );

// 10/5/2010 Scott Atwell		ui.setCxlReplaceMode( getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode() );
		ui.setCxlReplaceMode( aStrategiesUI.getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode() );

	//6/23/2010 Scott Atwell added		
// 10/5/2010 Scott Atwell moved back to caller		currentlyDisplayedStrategyUI = ui;
		
		return ui;
	}
}
