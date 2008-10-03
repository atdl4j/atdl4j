package br.com.investtools.fix.atdl.ui.swt.impl;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.ui.swt.StrategiesUI;
import br.com.investtools.fix.atdl.ui.swt.StrategiesUIFactory;

public class SWTStrategiesUIFactory implements StrategiesUIFactory {

	@Override
	public StrategiesUI<?> create(StrategiesDocument document)
			throws XmlException {
		SWTStrategiesUI strategiesUI = new SWTStrategiesUI(document);
		return strategiesUI;
	}

}
