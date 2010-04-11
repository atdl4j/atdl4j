package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;

public interface StrategiesUI<T> {

	// -- Call this after constructor --
	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();
	
	public StrategyUI createUI(StrategyT strategy, Object parent);
}
