package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jOptions;


/**
 * Represents the core GUI component (without main() line) used by the Tester application
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public interface Atdl4jTesterPanel
{
	public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions);

	public Atdl4jOptions getAtdl4jOptions();

	public void closePanel();
	
	public Atdl4jCompositePanel getAtdl4jCompositePanel();
}
