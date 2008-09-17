package br.com.investtools.fix.atdl.ui.swt;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.ui.StrategiesUI;
import br.com.investtools.fix.atdl.ui.StrategiesUIFactory;

public class SWTStrategiesUIFactory implements StrategiesUIFactory {

	@Override
	public StrategiesUI<?> create(StrategiesDocument document) throws XmlException {
		SWTStrategiesUI strategiesUI = new SWTStrategiesUI(document);
		return strategiesUI;
	}

}
