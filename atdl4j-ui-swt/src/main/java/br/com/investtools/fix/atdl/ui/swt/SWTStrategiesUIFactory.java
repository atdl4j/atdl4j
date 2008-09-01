package br.com.investtools.fix.atdl.ui.swt;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.widgets.TabFolder;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.ui.StrategiesUI;
import br.com.investtools.fix.atdl.ui.StrategiesUIFactory;

public class SWTStrategiesUIFactory implements StrategiesUIFactory {

	private TabFolder tabFolder;

	public SWTStrategiesUIFactory(TabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}

	@Override
	public StrategiesUI create(StrategiesDocument document) throws XmlException {
		StrategiesUI strategiesUI = new SWTStrategiesUI(document, tabFolder);
		return strategiesUI;
	}

}
