package br.com.investtools.fix.atdl.ui.swt;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;

public interface StrategiesUI<T> {

	public StrategyUI createUI(StrategyT strategy, T parent)
			throws XmlException;

}
