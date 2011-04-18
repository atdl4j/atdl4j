/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Represents the available strategy choices GUI component. 
 * 
 * Creation date: (Feb 7, 2010 9:49:04 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface StrategySelectionPanel
{
	public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions);
	
	public void loadStrategyList( List<StrategyT> aStrategyList );

	public void selectDropDownStrategyByStrategyName( String aStrategyName );
	
	public void selectDropDownStrategyByStrategyWireValue( String aStrategyWireValue ); 
	
	public void selectFirstDropDownStrategy();

	public Atdl4jOptions getAtdl4jOptions();
	
	public void addListener(StrategySelectionPanelListener aStrategySelectionPanelListener);
	
	public void removeListener(StrategySelectionPanelListener aStrategySelectionPanelListener);

}
