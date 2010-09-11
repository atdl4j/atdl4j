package org.atdl4j.ui.swing.impl;

import java.awt.Container;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.layout.BorderT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;

/**
 * 
 * Contains static SWT-specific methods to support building StrategyPanel
 * container and related panel/composite redrawing.
 * 
 * Creation date: (Mar 3, 2010 7:35:04 AM)
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 3, 2010
 */
public class SwingStrategyPanelHelper
{
	private static final Logger logger = Logger.getLogger( SwingStrategyPanelHelper.class );
	
	/**
	 * Builds the appropriate 'container' Container for aStrategyPanel.
	 * 
	 * Supports:
	 * 	- aStrategyPanel.isCollapsible() returning ExpandBar (collapsed state based upon aStrategyPanel.isCollapsed())
	 * 	- aStrategyPanel.getTitle() returning Group
	 * 	- aStrategyPanel.getBorder() of BorderT.LINE returning Group (without Text)
	 * 	- default returns Container
	 * 
	 * @param aStrategyPanel
	 * @param aParent
	 * @param aStyle
	 * @return
	 */
	public static Container createStrategyPanelContainer(StrategyPanelT aStrategyPanel, Container aParent)
	{
		Container c;

		// -- Check for Collapsible --
		if ( aStrategyPanel.isCollapsible() )
		{
		    // TODO: implement JIDE CollapsiblePane
		    
		    /*
			ExpandBar tempExpandBar = new ExpandBar( aParent, SWT.NONE );
			tempExpandBar.setSpacing( 1 );
//TODO if want to get rid of white background, likely want a border around whole thing...
			// TODO tempExpandBar.setBackground( aParent.getBackground() );

			if ( aParent.getLayout() instanceof GridLayout )
			{
				// mimics createStrategyPanelLayoutData() below
				GridData tempGridData = new GridData( SWT.FILL, SWT.FILL, true, false );
//				if ( aStrategyPanel.getOrientation() == PanelOrientationT.VERTICAL )
				// {
					tempGridData.horizontalSpan = 2;	// same as createStrategyPanelLayoutData
				// }
				tempGridData.horizontalAlignment = GridData.FILL;	// occupy the full available horizontal width

				tempExpandBar.setLayoutData( tempGridData );
			}

			Container tempExpandBarContainer = new Container( tempExpandBar, SWT.NONE );
			// "c" is 'standard' composite containing StrategyPanel as Data
			c = tempExpandBarContainer;

			// -- since we're using GridLayout, need to force the aParent with that
			// layout to resize via aParent.layout() when ExpandBar button is
			// expanded/collapsed --
			tempExpandBar.addExpandListener( new SwingExpandBarResizer( tempExpandBarContainer ) );

			ExpandItem tempExpandItem = new ExpandItem( tempExpandBar, SWT.NONE, 0 );
			if ( aStrategyPanel.getTitle() != null )
			{
				tempExpandItem.setText( aStrategyPanel.getTitle() );
			}
			tempExpandItem.setControl( tempExpandBarContainer );

			// -- not very helpful in this context, we need revalidateLayout( c )
			// executed _after_ the "c" component is loaded with Controls --
			tempExpandItem.setHeight( tempExpandItem.getControl().computeSize( SWT.DEFAULT, SWT.DEFAULT ).y );

			tempExpandItem.setExpanded( !aStrategyPanel.isCollapsed() );*/
		}
		// -- Check for titled border --
		else if ( aStrategyPanel.getTitle() != null )
		{
		    /*
			c = new Group( aParent, aStyle );
			( (Group) c ).setText( aStrategyPanel.getTitle() );
			*/
		}
		// -- Check for Line border (no title) --
		else if ( BorderT.LINE.equals( aStrategyPanel.getBorder() ) )
		{
		    //	c = new Group( aParent, aStyle );
		}
		else
		{
		   // -- normal, non-collapsible, non-bordered StrategyPanel --
		   // c = new Container( aParent, aStyle );
		}

		// -- Set the layout and layoutData for the SWT Container containing the
		// set of FIXatdl Controls --
		/*
		c.setLayout( createStrategyPanelLayout( aStrategyPanel ) );
		c.setLayoutData( createStrategyPanelLayoutData( c ) );
		*/

		// -- Keep StrategyPanel in data object --
		//c.setData( aStrategyPanel );
		// TODO: remove me!!
		c = new JPanel();
		return c;
	}

	/**
	 * Creates the layout for the SWT Container containing the set of FIXatdl Controls
	 * 
	 * @param c
	 * @return
	 */
	/*
	protected static Layout createStrategyPanelLayout(StrategyPanelT panel)
	{
		PanelOrientationT orientation = panel.getOrientation();
		if ( orientation == PanelOrientationT.HORIZONTAL )
		{
			int parameterCount = panel.getControl() != null ? panel.getControl().size() : 0;
			int panelCount = panel.getStrategyPanel() != null ? panel.getStrategyPanel().size() : 0;

			// make 2 columns for each parameter and 1 column for each sub-panel
			GridLayout l = new GridLayout( parameterCount * 2 + panelCount, false );
			return l;
		}
		else if ( orientation == PanelOrientationT.VERTICAL )
		{
// TODO ???? WHY TWO VS. ONE ?????			GridLayout l = new GridLayout( 2, false );
			// GridLayout l = new GridLayout( 1, false );
			GridLayout l = new GridLayout( 2, false );
			return l;
		}
		// 4/6/2010 Scott Atwell Added
		else
		{
			throw new IllegalStateException( "ERROR StrategyPanel (" + panel.getTitle() + ") is missing orientation attribute." );
		}
		// return null;
	}*/

	/**
	 * Creates the layoutData for the SWT Container containing the set of FIXatdl Controls
	 * 
	 * @param c
	 * @return
	 */
	/*
	protected static Object createStrategyPanelLayoutData(Container c)
	{
		// if parent is a strategy panel
		if ( c.getParent().getData() instanceof StrategyPanelT )
		{
			GridData layoutData = new GridData( SWT.FILL, SWT.FILL, true, false );
			c.setLayoutData( layoutData );

			StrategyPanelT parentPanel = (StrategyPanelT) c.getParent().getData();
			// if parent orientation is vertical, make this panel span 2 columns
			if ( parentPanel.getOrientation() == PanelOrientationT.VERTICAL )
			{
				layoutData.horizontalSpan = 2;
			}
			return layoutData;
		}
		// 3/2/2010 Scott Atwell added for ExpandBar
		else if ( ( c.getParent() instanceof ExpandBar ) && ( c.getData() instanceof StrategyPanelT ) )
		{
			GridData layoutData = new GridData( SWT.FILL, SWT.FILL, true, false );
			c.setLayoutData( layoutData );

			StrategyPanelT parentPanel = (StrategyPanelT) c.getData();
			// if parent orientation is vertical, make this panel span 2 columns
			if ( parentPanel.getOrientation() == PanelOrientationT.VERTICAL )
			{
				layoutData.horizontalSpan = 2;
			}
			return layoutData;
		}
		return null;
	}
	*/

}
