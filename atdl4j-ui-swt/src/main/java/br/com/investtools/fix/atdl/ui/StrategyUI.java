package br.com.investtools.fix.atdl.ui;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;

public interface StrategyUI {

	public void validate() throws ValidationException, XmlException;

	public String getFIXMessage();
	
	public StrategyT getStrategy(); 

	public ParameterWidget<?> getParameterWidget(String name);

}
