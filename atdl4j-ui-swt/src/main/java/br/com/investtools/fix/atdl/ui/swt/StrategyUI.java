package br.com.investtools.fix.atdl.ui.swt;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.ui.FIXMessageBuilder;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;

public interface StrategyUI {

	public void validate() throws ValidationException, XmlException;

	public String getFIXMessage();

	public void getFIXMessage(FIXMessageBuilder builder);

	public StrategyT getStrategy();

	public ParameterUI<?> getParameterWidget(String name);

}
