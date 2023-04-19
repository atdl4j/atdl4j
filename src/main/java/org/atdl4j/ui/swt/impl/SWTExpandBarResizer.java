package org.atdl4j.ui.swt.impl;

import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;

/**
 * Helper class used to respond to ExpandBar events invoking SWTStrategyPanelHelper.revalidateLayoutAsync() upon itemCollapsed() and itemExpanded()
 * 
 * Creation date: (Mar 3, 2010 8:29:09 AM)
 * @author  Scott Atwell
 * @version  1.0, Mar 3, 2010
 */
public class SWTExpandBarResizer
	extends ExpandAdapter
{
	protected Composite composite;
	
	public SWTExpandBarResizer( Composite aComposite )
	{
		composite = aComposite;
	}

	@Override
	public void itemCollapsed(ExpandEvent aE)
	{
		SWTStrategyPanelHelper.revalidateLayoutAsync( (ExpandBar)composite.getParent() );
	}
	
	@Override
	public void itemExpanded(ExpandEvent aE)
	{
		SWTStrategyPanelHelper.revalidateLayoutAsync( (ExpandBar)composite.getParent() );
	}
}