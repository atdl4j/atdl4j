package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jOptions;


/**
 * Represents the GUI component used to invoke Atdl4jInputAndFilterDataPanel pop-up.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface Atdl4jInputAndFilterDataSelectionPanel
{
	public Object buildAtdl4jInputAndFilterDataSelectionPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler);

	public Atdl4jOptions getAtdl4jOptions();
	
	public void addListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public void removeListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );
}
