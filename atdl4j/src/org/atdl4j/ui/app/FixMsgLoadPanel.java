package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;

/**
 * Represents the tester's "Load Message" button and text field GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface FixMsgLoadPanel
{
	public Object buildFixMsgLoadPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig);
	
	public void setFixMsg(String aFixMsg);

	public Atdl4jConfig getAtdl4jConfig();
	
	public void addListener(FixMsgLoadPanelListener aFixMsgLoadPanelListener);
	
	public void removeListener(FixMsgLoadPanelListener aFixMsgLoadPanelListener);

}
