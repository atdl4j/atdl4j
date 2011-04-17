package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.Atdl4jClassLoadException;


/**
 * Represents the core GUI component (without main() line) used by the Tester application
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public interface Atdl4jTesterPanel
{
	public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) throws Atdl4jClassLoadException;

	public Atdl4jOptions getAtdl4jOptions();

	public void closePanel();
	
	public Atdl4jCompositePanel getAtdl4jCompositePanel() throws Atdl4jClassLoadException;
	
	public void setVisibleFileSelectionSection( boolean aVisible );
	
	public void setVisibleValidateOutputSection( boolean aVisible );
	
	public void setVisibleTestingInputSection( boolean aVisible );
		
	public void addListener(Atdl4jTesterPanelListener aAtdl4jTesterPanelListener);
	
	public void removeListener(Atdl4jTesterPanelListener aAtdl4jTesterPanelListener);

	public void setVisibleOkCancelButtonSection( boolean aVisible );	
	
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler() throws Atdl4jClassLoadException;
}
