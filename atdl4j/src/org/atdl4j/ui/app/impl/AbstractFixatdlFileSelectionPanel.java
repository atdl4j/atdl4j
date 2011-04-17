package org.atdl4j.ui.app.impl;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.data.exception.FIXatdlFormatException;
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
	private final Logger logger = Logger.getLogger(AbstractFixatdlFileSelectionPanel.class);
    
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
	    try {
		for ( FixatdlFileSelectionPanelListener tempListener : listenerList )
		{
			tempListener.fixatdlFileSelected( aFilename );
		}
	    } catch (Atdl4jClassLoadException ex) {
		logger.info( "Atdl4jClassLoadException occured while loading file: " + aFilename );
		if (Atdl4jConfig.getConfig().isThrowEventRuntimeExceptions())
		    throw new RuntimeException("Atdl4jClassLoadException while loading file: " + aFilename, ex);
	    } catch (FIXatdlFormatException ex) {
		logger.info( "FIXatdlFormatException occured while loading file: " + aFilename );
		if (Atdl4jConfig.getConfig().isThrowEventRuntimeExceptions())
		    throw new RuntimeException("FIXatdlFormatException while loading file: " + aFilename, ex);
	    }
	}
}
