package org.atdl4j.ui.swing.impl;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.atdl4j.fixatdl.layout.BorderT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.StrategyPanelHelper;


/**
 * 
 * Contains static Swing-specific methods to support building StrategyPanel
 * container and related panel/composite redrawing.
 * 
 */
public class SwingStrategyPanelHelper
	implements StrategyPanelHelper
{
	
	
	/**
	 * Builds the appropriate 'container' JPanel for aStrategyPanel.
	 * 
	 * Supports:
	 * 	- aStrategyPanel.isCollapsible() returning ExpandBar (collapsed state based upon aStrategyPanel.isCollapsed())
	 * 	- aStrategyPanel.getTitle() returning Group
	 * 	- aStrategyPanel.getBorder() of BorderT.LINE returning Group (without Text)
	 * 	- default returns Composite
	 * 
	 * @param aStrategyPanel
	 * @param aParent
	 * @param aStyle
	 * @return
	 */
	public static JPanel createStrategyPanelContainer(StrategyPanelT aStrategyPanel, JPanel aParent, int aStyle)
	{
		JPanel c = new JPanel();

/*		if ( aStrategyPanel.isCollapsible() ){
			c = new CollapsiblePanel(aStrategyPanel.getTitle() );
		}
		// -- Check for titled border --
		else 
*/		
		if ( aStrategyPanel.getTitle() != null ){
			c.setBorder( BorderFactory.createTitledBorder(aStrategyPanel.getTitle()));
		}
		// -- Check for Line border (no title) --
		else if ( BorderT.LINE.equals( aStrategyPanel.getBorder() ) )	{
			c.setBorder( BorderFactory.createTitledBorder(aStrategyPanel.getTitle()));
		}
		else {
			c.setBorder(BorderFactory.createEmptyBorder());
		}

		c.setLayout(new SpringLayout());
		return c;
	}

	public static void createStrategyPanelSpringLayout(StrategyPanelT _panelT, JPanel _panel) {
		
		PanelOrientationT orientation = _panelT.getOrientation();
		if ( orientation == PanelOrientationT.HORIZONTAL )
		{
			// Lay out the panel.
			makeCompactGrid(_panel, 1, _panel.getComponentCount(), // rows, cols
				5, 2, // initX, initY
				5, 2); // xPad, yPad
		}
		else if ( orientation == PanelOrientationT.VERTICAL )
		{
			// Lay out the panel.
			makeCompactGrid(_panel, _panel.getComponentCount(), 1,// rows, cols
				5, 2, // initX, initY
				5, 2); // xPad, yPad
		}
		else
		{
			throw new IllegalStateException( "ERROR StrategyPanel (" + _panelT.getTitle() + ") is missing orientation attribute." );
		}
	}

	/**
	 * Navigates through aWidget's getParent() looking for ExpandBar with ExpandItems not-yet-expanded and expands those
	 * @param aWidget
	 * @return boolean indicating whether any ExpandBar ExpandItems were adjusted
	 */
	public boolean expandAtdl4jWidgetParentStrategyPanel( Atdl4jWidget<?> aWidget )
	{
		
		return true;
	}
	
	
	/**
	 * Aligns the first <code>rows</code> <code>cols</code> components of <code>parent</code> in a grid. Each component in
	 * a column is as wide as the maximum preferred width of the components in that column; height is similarly determined
	 * for each row. The parent is made just big enough to fit them all.
	 * 
	 * @param rows
	 *          number of rows
	 * @param cols
	 *          number of columns
	 * @param initialX
	 *          x location to start the grid at
	 * @param initialY
	 *          y location to start the grid at
	 * @param xPad
	 *          x padding between cells
	 * @param yPad
	 *          y padding between cells
	 */
	public static void makeCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad,
			int yPad) {
		
		parent.setMaximumSize(new Dimension(Integer.MAX_VALUE, parent.getPreferredSize().height));
		SpringLayout layout;
		try {
			layout = (SpringLayout) parent.getLayout();
		}
		catch (ClassCastException exc) {
			System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
			return;
		}
		
		// Align all cells in each column and make them the same width.
		Spring x = Spring.constant(initialX);
		for (int c = 0; c < cols; c++) {
			Spring width = Spring.constant(0);
			for (int r = 0; r < rows; r++) {
				width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
			}
			for (int r = 0; r < rows; r++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
				constraints.setX(x);
				constraints.setWidth(width);
			}
			x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
		}
		
		// Align all cells in each row and make them the same height.
		Spring y = Spring.constant(initialY);
		for (int r = 0; r < rows; r++) {
			Spring height = Spring.constant(0);
			for (int c = 0; c < cols; c++) {
				height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
			}
			for (int c = 0; c < cols; c++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
				constraints.setY(y);
				constraints.setHeight(height);
			}
			y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
		}
		
		// Set the parent's size.
		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH, y);
		pCons.setConstraint(SpringLayout.EAST, x);
		
	}
	
	
  private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
		SpringLayout layout = (SpringLayout) parent.getLayout();
		Component c = parent.getComponent(row * cols + col);
		return layout.getConstraints(c);
	}
}
