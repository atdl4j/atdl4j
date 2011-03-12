package org.atdl4j.ui.swing.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractAtdl4jInputAndFilterDataPanel;

/**
 * Represents the Swing-specific Atdl4jOptions and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SwingAtdl4jInputAndFilterDataPanel
		extends AbstractAtdl4jInputAndFilterDataPanel
{
	public final Logger logger = Logger.getLogger(SwingAtdl4jInputAndFilterDataPanel.class);

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel#buildAtdl4jInputAndFilterDataPanel(java.lang.Object, org.atdl4j.config.Atdl4jOptions)
	 */
	@Override
	public Object buildAtdl4jInputAndFilterDataPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel#extractAtdl4jOptionsFromScreen()
	 */
	@Override
	public boolean extractAtdl4jOptionsFromScreen() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel#loadScreenWithAtdl4jOptions()
	 */
	@Override
	public boolean loadScreenWithAtdl4jOptions() {
		// TODO Auto-generated method stub
		return false;
	}

}
