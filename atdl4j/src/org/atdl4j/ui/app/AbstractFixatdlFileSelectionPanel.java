package org.atdl4j.ui.app;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jConfig;

/**
 * Represents the base, non-GUI system-specific FIXatdl file selection component.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractFixatdlFileSelectionPanel
		implements FixatdlFileSelectionPanel
{
	private Atdl4jConfig atdl4jConfig = null;
	
	private List<FixatdlFileSelectionPanelListener> listenerList = new Vector<FixatdlFileSelectionPanelListener>();

	
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
