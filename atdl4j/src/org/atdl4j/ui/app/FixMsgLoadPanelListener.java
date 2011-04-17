package org.atdl4j.ui.app;

import org.atdl4j.data.exception.Atdl4jClassLoadException;

/**
 * Represents FixMsgLoadPanel events.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface FixMsgLoadPanelListener
{
	public void fixMsgLoadSelected(String aFixMsg) throws Atdl4jClassLoadException;
}
