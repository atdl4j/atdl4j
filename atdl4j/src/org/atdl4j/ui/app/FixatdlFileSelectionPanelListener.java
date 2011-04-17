package org.atdl4j.ui.app;

import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.data.exception.FIXatdlFormatException;

/**
 * Represents FixatdlFileSelectionPanel events.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public interface FixatdlFileSelectionPanelListener
{
	public void fixatdlFileSelected(String aFilename) throws Atdl4jClassLoadException, FIXatdlFormatException;
}