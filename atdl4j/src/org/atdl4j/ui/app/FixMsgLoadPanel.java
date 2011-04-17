package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.Atdl4jClassLoadException;

/**
 * Represents the tester's "Load Message" button and text field GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface FixMsgLoadPanel
{
	public Object buildFixMsgLoadPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions);
	
	public void setFixMsg(String aFixMsg) throws Atdl4jClassLoadException;

	public Atdl4jOptions getAtdl4jOptions();
	
	public void addListener(FixMsgLoadPanelListener aFixMsgLoadPanelListener);
	
	public void removeListener(FixMsgLoadPanelListener aFixMsgLoadPanelListener);

}
