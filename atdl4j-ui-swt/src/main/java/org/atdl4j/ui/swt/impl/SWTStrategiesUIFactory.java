package org.atdl4j.ui.swt.impl;

import javax.xml.bind.JAXBException;


import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.fixprotocol.atdl_1_1.core.StrategiesT;

public class SWTStrategiesUIFactory implements StrategiesUIFactory {

	public StrategiesUI<?> create(StrategiesT strategies)
			throws JAXBException {
		SWTStrategiesUI strategiesUI = new SWTStrategiesUI(strategies);
		return strategiesUI;
	}

}
