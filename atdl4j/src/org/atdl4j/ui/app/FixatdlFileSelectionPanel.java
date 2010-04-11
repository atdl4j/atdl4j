package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;

/**
 * Represents the FIXatdl file selection GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public interface FixatdlFileSelectionPanel
{
	public Object buildFixatdlFileSelectionPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig);
	
	public void selectFilename(String aFilename);

	public Atdl4jConfig getAtdl4jConfig();
	
	public void setVisible( boolean aVisible );
	
	public void addListener(FixatdlFileSelectionPanelListener aFixatdlFileSelectionPanelListener);
	
	public void removeListener(FixatdlFileSelectionPanelListener aFixatdlFileSelectionPanelListener);

}
