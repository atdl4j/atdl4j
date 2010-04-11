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
//TODO 1/16/2010 Scott Atwell added
	private StrategiesT strategies;
// 2/8/2010 Scott Atwell added
	private Atdl4jConfig atdl4jConfig;

	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SWTStrategiesUI()
	{
	}

// 2/8/2010 Scott Atwell (use Atdl4jConfig vs. inputHiddenFieldNameValueMap)	public SWTStrategiesUI(StrategiesT strategies)
	public SWTStrategiesUI(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
	{
		init(strategies, aAtdl4jConfig);
	}

// 2/8/2010 Scott Atwell (use Atdl4jConfig vs. inputHiddenFieldNameValueMap)	public SWTStrategiesUI(StrategiesT strategies)
	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
	{
//TODO 1/16/2010 Scott Atwell added
		this.strategies = strategies;
// 2/8/2010 Scott Atwell added
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

//TODO 1/17/2010 Scott Atwell	public SWTStrategyUI createUI(StrategyT strategy, Composite parent)
// 2/8/2010 Scott Atwell (use getAtdl4jConfig() vs. inputHiddenFieldNameValueMap)  public SWTStrategyUI createUI(StrategyT strategy, Composite parent, Map<String, String> inputHiddenFieldNameValueMap)
// 2/9/2010 Scott Atwell public SWTStrategyUI createUI(StrategyT strategy, Composite parent)
public StrategyUI createUI(StrategyT strategy, Composite parent)
{
//TODO 1/16/2010 Scott Atwell			return new SWTStrategyUI(strategy, strategiesRules, parent);
//TODO 1/17/2010 Scott Atwell			return new SWTStrategyUI(strategy, strategiesRules, parent, strategies);
// 2/8/2010 Scott Atwell (use getAtdl4jConfig() vs. inputHiddenFieldNameValueMap)			return new SWTStrategyUI(strategy, strategiesRules, parent, strategies, inputHiddenFieldNameValueMap);
// 2/9/2010 Scott Atwell			return new SWTStrategyUI(strategy, strategiesRules, parent, strategies, getAtdl4jConfig());
	return getAtdl4jConfig().getStrategyUI( strategy, strategiesRules, parent );
}

// 2/8/2010 Scott Atwell (use Atdl4jConfig vs. inputHiddenFieldNameValueMap)  public SWTStrategyUI createUI(StrategyT strategy, Object parent, Map<String, String> inputHiddenFieldNameValueMap)
// 2/9/2010 Scott Atwell public SWTStrategyUI createUI(StrategyT strategy, Object parent)
public StrategyUI createUI(StrategyT strategy, Object parent)
{
// 2/8/2010 Scott Atwell (use getAtdl4jConfig() vs. inputHiddenFieldNameValueMap)	return createUI( strategy, (Composite) parent, inputHiddenFieldNameValueMap);
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
