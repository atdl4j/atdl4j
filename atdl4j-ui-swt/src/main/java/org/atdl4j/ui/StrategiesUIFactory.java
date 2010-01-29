package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.fixprotocol.atdl_1_1.core.StrategiesT;

public interface StrategiesUIFactory {

	public StrategiesUI<?> create(StrategiesT strategies)
			throws JAXBException;

}
