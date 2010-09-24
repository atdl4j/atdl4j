package org.atdl4j.ui.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * This class contains the data associated with the <code>SWTMenuHelper</code>.
 * 
 * Creation date: (Sep 3, 2010 1:40:42 PM)
 * @author Scott Atwell
 */
public class SWTMenuHelper
{
	/**
	 * Creates a checkbox MenuItem adding it to aShell's pop-up Menu.  The Menu will be created if it does not yet exist.
	 * 
	 * @param aShell
	 * @param aText
	 * @return MenuItem
	 */
	public static MenuItem addShellPopupCheckMenuItem( Shell aShell, String aText )
	{
		return addShellPopupMenuItem( aShell, aText, SWT.CHECK );
	}

	/**
	 * Creates a MenuItem adding it to aShell's pop-up Menu.  The Menu will be created if it does not yet exist.
	 * 
	 * @param aShell
	 * @param aText
	 * @param aStyle
	 * @return MenuItem
	 */
	public static MenuItem addShellPopupMenuItem( Shell aShell, String aText, int aStyle )
	{
		MenuItem tempMenuItem = new MenuItem( getShellPopupMenu( aShell ), aStyle );
		tempMenuItem.setText( aText );
		
		return tempMenuItem;
	}
	
	/**
	 * Returns aShell's pop-up menu, or if it does exist, creates one.
	 * @param aShell
	 */
	public static Menu getShellPopupMenu( Shell aShell )
	{
		if ( aShell != null ) 
		{
			Menu tempMenu = aShell.getMenu();
			
			if ( tempMenu == null )
			{
				// -- Create the Menu as it does not yet exist --
				tempMenu = new Menu( aShell, SWT.POP_UP );
				aShell.setMenu( tempMenu );
			}
			
			return tempMenu;
		}
		else
		{
			return null;
		}
	}
}
