package br.com.investtools.fix.atdl.ui;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.ValidationException;

public interface StrategyUI {

	public void validate() throws ValidationException, XmlException;
	
	public String getFIXMessage();

}
