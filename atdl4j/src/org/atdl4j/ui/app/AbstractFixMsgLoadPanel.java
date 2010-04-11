package org.atdl4j.ui.app;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jConfig;

/**
 * Represents the base, non-GUI system-specific tester's "Load Message" button and text field GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractFixMsgLoadPanel
		implements FixMsgLoadPanel
{
	private Atdl4jConfig atdl4jConfig = null;
	
	private List<FixMsgLoadPanelListener> listenerList = new Vector<FixMsgLoadPanelListener>();

	
	/**
	 * @param atdl4jConfig the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
	{
		this.atdl4jConfig = atdl4jConfig;
	}


	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}
	

	public void addListener( FixMsgLoadPanelListener aFixMsgLoadPanelListener )
	{
		listenerList.add( aFixMsgLoadPanelListener );
	}

	public void removeListener( FixMsgLoadPanelListener aFixMsgLoadPanelListener )
	{
		listenerList.remove( aFixMsgLoadPanelListener );
	}	
	
	protected void fireFixMsgLoadSelectedEvent( String aFixMsg )
	{
		for ( FixMsgLoadPanelListener tempListener : listenerList )
		{
			tempListener.fixMsgLoadSelected( aFixMsg );
		}
	}
}
