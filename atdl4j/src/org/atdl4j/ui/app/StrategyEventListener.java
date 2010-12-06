package org.atdl4j.ui.app;

import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.impl.SelectedStrategyDetails;

/**
 * 
 * This class contains the data associated with the <code>StrategyEventListener</code>.
 * 
 * Creation date: (Nov 25, 2010 8:15:17 AM)
 * @author Scott Atwell
 * @version 1.0, Nov 25, 2010
 */
public interface StrategyEventListener
{
	/**
	 * @param aStrategy
	 * @param aSelectedViaLoadFixMsg
	 */
	public void strategySelected( StrategyT aStrategy, boolean aSelectedViaLoadFixMsg );
	
	/**
	 * @param aStrategy
	 * @param aSelectedStrategyDetails
	 */
	public void strategyValidated( StrategyT aStrategy, SelectedStrategyDetails aSelectedStrategyDetails );
	
	/**
	 * @param aStrategy  (may be null)
	 * @param aMessageText
	 */
	public void strategyNotValidated( StrategyT aStrategy, String aMessageText );
	
	/**
	 * @param aStrategy  (may be null)
	 * @param aException
	 */
	public void strategyValidationFailed( StrategyT aStrategy, Throwable aException );
}
