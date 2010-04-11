/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;


/**
 * Represents the GUI pop-up message screen support. 
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public interface Atdl4jUserMessageHandler
{
	public boolean isInitReqd();
	
	public void init(Object parentOrShell, Atdl4jConfig atdl4jConfig);

	public void displayException( String aTitle, String aMsgText, Throwable e );
	
	public void displayMessage(String aTitle, String aMsgText);

	public Atdl4jConfig getAtdl4jConfig();
}
