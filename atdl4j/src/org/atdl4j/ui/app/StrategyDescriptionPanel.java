package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Represents the Strategy Description GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface StrategyDescriptionPanel
{
	public Object buildStrategyDescriptionPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig);
	
	public void loadStrategyDescriptionVisible( StrategyT aStrategy );
	
	public void loadStrategyDescriptionText( StrategyT aStrategy );
	
	public void setVisible( boolean aVisible );
	
	public Atdl4jConfig getAtdl4jConfig();
	
}
