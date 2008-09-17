package br.com.investtools.fix.atdl.ui;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;

public interface StrategiesUIFactory {

	public StrategiesUI<?> create(StrategiesDocument strategiesDocument)
			throws XmlException;

}
