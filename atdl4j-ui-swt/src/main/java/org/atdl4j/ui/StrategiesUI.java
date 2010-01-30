package org.atdl4j.ui;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategyT;

public interface StrategiesUI<T> {

//TODO 1/17/2010 Scott Atwell	public StrategyUI createUI(StrategyT strategy, T parent)
	public StrategyUI createUI(StrategyT strategy, T parent, Map<String, String> inputHiddenFieldNameValueMap)
			throws JAXBException;

}
