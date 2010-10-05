package org.atdl4j.ui;

import java.util.List;
import java.util.Map;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.StrategiesUIListener;

// 9/26/2010 Scott Atwell  public interface StrategiesUI<T> {
public interface StrategiesUI {

	// -- Call this after constructor --
// 9/27/2010 Scott Atwell	public void init(StrategiesT strategies, Atdl4jOptions aAtdl4jOptions);
	public void init(Atdl4jOptions aAtdl4jOptions);

	public Atdl4jOptions getAtdl4jOptions();
	
// 10/5/2010 Scott Atwell removed	public StrategyUI createUI(StrategyT strategy, Object parent);


// -- 9/13/2010 Scott Atwell - moved the below from StrategiesPanel --	
	
// 9/29/2010 Scott Atwell	public Object buildStrategiesPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions);
	public Object buildStrategiesPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler);

// 9/27/2010 Scott Atwell	public void createStrategyPanels(List<StrategyT> aFilteredStrategyList); // throws Exception;
	public void createStrategyPanels(StrategiesT strategies, List<StrategyT> aFilteredStrategyList); // throws Exception;
	 
	public void removeAllStrategyPanels();
	
// 4/16/2010 Scott Atwell	public void adjustLayoutForSelectedStrategy(int aIndex);
	public void adjustLayoutForSelectedStrategy( StrategyT aStrategy );
	
//	public Atdl4jOptions getAtdl4jOptions();

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

// 10/5/2010 Scott Atwell	public StrategyUI createStrategyUI(StrategyT strategy, StrategiesT aStrategies, Map<String, ValidationRule> strategiesRules, Object parentContainer);	
}
