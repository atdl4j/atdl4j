package org.atdl4j.ui.app.impl;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.FixatdlFileSelectionPanel;
import org.atdl4j.ui.app.FixatdlFileSelectionPanelListener;

/**
 * Represents the base, non-GUI system-specific FIXatdl file selection component.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractFixatdlFileSelectionPanel
		implements FixatdlFileSelectionPanel
{
	private Atdl4jOptions atdl4jOptions = null;
	
	private List<FixatdlFileSelectionPanelListener> listenerList = new Vector<FixatdlFileSelectionPanelListener>();

	
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
	

	public void addListener( FixatdlFileSelectionPanelListener aFixatdlFileSelectionPanelListener )
	{
		listenerList.add( aFixatdlFileSelectionPanelListener );
	}

	public void removeListener( FixatdlFileSelectionPanelListener aFixatdlFileSelectionPanelListener )
	{
		listenerList.remove( aFixatdlFileSelectionPanelListener );
	}	
	
	protected void fireFixatdlFileSelectedEvent( String aFilename )
	{
		for ( FixatdlFileSelectionPanelListener tempListener : listenerList )
		{
			tempListener.fixatdlFileSelected( aFilename );
		}
	}
}
