package org.atdl4j.ui.app.impl;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.FixMsgLoadPanel;
import org.atdl4j.ui.app.FixMsgLoadPanelListener;

/**
 * Represents the base, non-GUI system-specific tester's "Load Message" button and text field GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractFixMsgLoadPanel
		implements FixMsgLoadPanel
{
	private Atdl4jOptions atdl4jOptions = null;
	
	private List<FixMsgLoadPanelListener> listenerList = new ArrayList<>();

	
	/**
	 * @param atdl4jOptions the atdl4jOptions to set
	 */
	protected void setAtdl4jOptions(Atdl4jOptions atdl4jOptions)
	{
		this.atdl4jOptions = atdl4jOptions;
	}


	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return atdl4jOptions;
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
