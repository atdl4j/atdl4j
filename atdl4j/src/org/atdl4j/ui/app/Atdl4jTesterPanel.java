package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;


/**
 * Represents the core GUI component (without main() line) used by the Tester application
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public interface Atdl4jTesterPanel
{
	public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();

	public void closePanel();
}
