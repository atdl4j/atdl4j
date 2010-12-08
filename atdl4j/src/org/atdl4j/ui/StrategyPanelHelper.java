package org.atdl4j.ui;

import org.atdl4j.fixatdl.layout.StrategyPanelT;

/**
 * Creation date: (Aug 22, 2010 8:51:45 AM)
 * @author Scott Atwell
 */
public interface StrategyPanelHelper
{
	/**
	 * Navigates through aWidget's getParent() looking for containers (representing StrategyPanel isCollapsible()=true that are not-yet-expanded and expands those
	 * @param aWidget
	 * @return boolean indicating whether any StrategyPanel containers were expanded
	 */
	public boolean expandAtdl4jWidgetParentStrategyPanel( Atdl4jWidget<?> aWidget );
}
