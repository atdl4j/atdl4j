package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jOptions;


/**
 * Represents the Atdl4jOptions and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface Atdl4jInputAndFilterDataPanel
{
	public Object buildAtdl4jInputAndFilterDataPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler );

	public Atdl4jOptions getAtdl4jOptions();
	
	public void addListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public void removeListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public boolean extractAtdl4jOptionsFromScreen();
	
	public boolean loadScreenWithAtdl4jOptions();
}
