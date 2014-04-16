/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.app;

import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Listener for StrategySelectionPanel
 * 
 * Creation date: (Feb 7, 2010 9:52:59 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface StrategySelectionPanelListener
{
	public void strategySelected(StrategyT strategy);

    public void beforeStrategyIsSelected(StrategySelectionEvent selectionEvent);
}
