package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;

public interface StrategiesUI<T> {

// 2/8/2010 Scott Atwell added	
	// -- Call this after constructor --
	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();
	
//TODO 1/17/2010 Scott Atwell	public StrategyUI createUI(StrategyT strategy, T parent)
//TODO?????? 2/7/2010 Scott Atwell	public StrategyUI createUI(StrategyT strategy, T parent, Map<String, String> inputHiddenFieldNameValueMap)
// 2/8/2010 Scott Atwell (use getAtdl4jConfig() vs. inputHiddenFieldNameValueMap) 	public StrategyUI createUI(StrategyT strategy, Object parent, Map<String, String> inputHiddenFieldNameValueMap)
	public StrategyUI createUI(StrategyT strategy, Object parent);
}
