package org.atdl4j.ui;

import java.util.List;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.StrategiesUIListener;

// 9/26/2010 Scott Atwell  public interface StrategiesUI<T> {
public interface StrategiesUI {

	// -- Call this after constructor --
// 9/27/2010 Scott Atwell	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig);
	public void init(Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();
	
	public StrategyUI createUI(StrategyT strategy, Object parent);


// -- 9/13/2010 Scott Atwell - moved the below from StrategiesPanel --	
	
	public Object buildStrategiesPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig);

// 9/27/2010 Scott Atwell	public void createStrategyPanels(List<StrategyT> aFilteredStrategyList); // throws Exception;
	public void createStrategyPanels(StrategiesT strategies, List<StrategyT> aFilteredStrategyList); // throws Exception;
	 
	public void removeAllStrategyPanels();
	
// 4/16/2010 Scott Atwell	public void adjustLayoutForSelectedStrategy(int aIndex);
	public void adjustLayoutForSelectedStrategy( StrategyT aStrategy );
	
//	public Atdl4jConfig getAtdl4jConfig();

	public boolean isPreCached();
	
	public void setPreCached( boolean aPreCached );
	
// 9/27/2010 Scott Atwell removed/unused	public void reinitStrategyPanels();
	
	public void addListener(StrategiesUIListener strategiesUIListener);
	
	public void removeListener(StrategiesUIListener strategiesUIListener);

	public void setVisible( boolean aVisible );
	
// 6/23/2010 Scott Atwell
	public StrategyUI getStrategyUI( StrategyT aStrategy );
	public StrategyUI getCurrentlyDisplayedStrategyUI();
	public StrategyT getCurrentlyDisplayedStrategy();

}
