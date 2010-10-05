/*
 * Created on Mar 7, 2010
 *
 */
package org.atdl4j.ui.swt.app.impl;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 * This class extends org.eclipse.swt.widgets.Group to provide proper setVisible(false) behavior
 * (eg avoid continuing to occupy vertical space within GridLayout when hidden)
 * by overriding computeSize() such that it returns 0,0 if isVisible() == false.
 * .
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 7, 2010
 */
public class SWTVisibleGroup
		extends Group
{
	public static Point POINT_0_0 = new Point( 0, 0 );

	/**
	 * @param aParent
	 * @param aStyle
	 */
	public SWTVisibleGroup(Composite aParent, int aStyle)
	{
		super( aParent, aStyle );
	}

	/* 
	 * Trick to ensure that set Visible results in size of 0,0. 
	 * @see http://dev.eclipse.org/newslists/news.eclipse.platform.swt/msg15546.html
	 * Override
	 * @see org.eclipse.swt.widgets.Group#computeSize(int, int, boolean)
	 */
	public Point computeSize(int wHint, int hHint, boolean changed)
	{
		if ( !isVisible() )
		{
// 6/23/2010 SWL avoid re-constructing			return new Point( 0, 0 );
			return POINT_0_0;
		}
		else
		{
			return super.computeSize( wHint, hHint, changed );
		}
	}

	/* 
	 * Override to avoid "SWTException(Subclassing not allowed)"
	 * @see http://www.eclipsezone.com/eclipse/forums/t88689.html
	 * @see org.eclipse.swt.widgets.Group#computeSize(int, int, boolean)
	 */
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}

}
