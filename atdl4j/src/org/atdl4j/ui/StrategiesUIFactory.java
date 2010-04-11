package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategiesT;

// @see org.atdl4j.ui.impl.BaseStrategiesUIFactory
public interface StrategiesUIFactory
{
	public StrategiesUI<?> create(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig);
}
