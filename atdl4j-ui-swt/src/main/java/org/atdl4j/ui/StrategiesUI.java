package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategyT;

public interface StrategiesUI<T> {

	public StrategyUI createUI(StrategyT strategy, T parent)
			throws JAXBException;

}
