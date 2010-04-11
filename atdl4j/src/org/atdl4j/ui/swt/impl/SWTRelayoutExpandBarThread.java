package org.atdl4j.ui.swt.impl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;

/**
 * 
 * Helper class used to wrap call to SWTStrategyPanelHelper.revalidateLayout() with Display.getCurrent().asyncExec(new Runnable()...)
 * 
 * Creation date: (Mar 3, 2010 7:56:21 AM)
 * @author Scott Atwell
 * @version 1.0, Mar 3, 2010
 */
public class SWTRelayoutExpandBarThread
		extends Thread
{
	private ExpandBar expandBar;
	
	public SWTRelayoutExpandBarThread( ExpandBar anExpandBar )
	{
		expandBar = anExpandBar;
		
		Display.getCurrent().asyncExec(new Runnable()
		{
         public void run()
         {
         	SWTStrategyPanelHelper.relayoutExpandBar( expandBar );
         }
     });
	}
}
