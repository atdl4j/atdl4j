package org.atdl4j.ui.swt.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	protected static final Logger logger = LoggerFactory.getLogger( SWTStrategyUIFactory.class );

	public static StrategyUI createStrategyUIAndContainer(SWTStrategiesUI aStrategiesUI, StrategyT aStrategy)
	{
		// create composite
		Composite strategyParent = new Composite( aStrategiesUI.getStrategiesPanel(), SWT.NONE );
		
		GridLayout strategyParentLayout = new GridLayout( 2, false );
		strategyParentLayout.verticalSpacing = 0;
		strategyParent.setLayout( strategyParentLayout );
		
		StrategyUI ui;

		// build strategy and catch strategy-specific errors
		try 
		{
			ui = createStrategyUI( aStrategy, aStrategiesUI.getStrategies(), aStrategiesUI.getStrategiesRules(), strategyParent, aStrategiesUI.getAtdl4jOptions() );
			
			// -- add additional components to take up space on left and bottom --
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
			return null;
		} 

		ui.setCxlReplaceMode( aStrategiesUI.getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode() );

		return ui;
	}
}
