package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategiesT;

public interface StrategiesUIFactory {

	public StrategiesUI<?> create(StrategiesT strategies) throws JAXBException;

}
