package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.Map;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;
import org.eclipse.swt.widgets.Composite;

public class SWTStrategiesUI implements StrategiesUI<Composite> {

	private Map<String, ValidationRule> strategiesRules;
	private StrategiesT strategies;
	private Atdl4jConfig atdl4jConfig;

	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SWTStrategiesUI()
	{
	}

	public SWTStrategiesUI(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
	{
		init(strategies, aAtdl4jConfig);
	}

	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
	{
		this.strategies = strategies;
		setAtdl4jConfig( aAtdl4jConfig );

		strategiesRules = new HashMap<String, ValidationRule>();

		for (EditT edit : strategies.getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit,
						strategiesRules, strategies);
				strategiesRules.put(id, rule);
			} else {
				throw new IllegalArgumentException("Strategies-scoped edit without id");
			}
		}
	}

public StrategyUI createUI(StrategyT strategy, Composite parent)
{
	return getAtdl4jConfig().getStrategyUI( strategy, strategiesRules, parent );
}

public StrategyUI createUI(StrategyT strategy, Object parent)
{
	return createUI( strategy, (Composite) parent);
}

/**
 * @param atdl4jConfig the atdl4jConfig to set
 */
protected void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
{
	this.atdl4jConfig = atdl4jConfig;
}

/**
 * @return the atdl4jConfig
 */
public Atdl4jConfig getAtdl4jConfig()
{
	return atdl4jConfig;
}
}
