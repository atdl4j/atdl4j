package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;


/**
 * Represents the Atdl4jConfig and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface Atdl4jInputAndFilterDataPanel
{
	public Object buildAtdl4jInputAndFilterDataPanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();
	
	public void addListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public void removeListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public boolean extractAtdl4jConfigFromScreen();
	
	public boolean loadScreenWithAtdl4jConfig();
}
