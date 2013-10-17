package org.atdl4j.ui.swing.impl;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.impl.BaseStrategyUIFactory;

/**
 * 
 * This class contains the data associated with the <code>SwingStrategyUIFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 9:05:33 PM)
 * @author Scott Atwell
 */
public class SwingStrategyUIFactory extends BaseStrategyUIFactory
{
	protected static final Logger logger = Logger.getLogger( SwingStrategyUIFactory.class );

	public static StrategyUI createStrategyUIAndContainer(SwingStrategiesUI aStrategiesUI, StrategyT aStrategy)
	{
		// create composite
		
		JPanel strategyParent = aStrategiesUI.getStrategiesPanel();
		
		StrategyUI ui;

		// build strategy and catch strategy-specific errors
		try 
		{
			ui = createStrategyUI( aStrategy, aStrategiesUI.getStrategies(), aStrategiesUI.getStrategiesRules(), strategyParent, aStrategiesUI.getAtdl4jOptions() );
			
		} 
		catch (Throwable e) 
		{
			getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
					"Error in Strategy: " + Atdl4jHelper.getStrategyUiRepOrName( aStrategy ), e );

			// rollback changes
			strategyParent.removeAll();

			// skip to next strategy
			return null;
		} 

		ui.setCxlReplaceMode( aStrategiesUI.getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode() );

		return ui;
	}
}
