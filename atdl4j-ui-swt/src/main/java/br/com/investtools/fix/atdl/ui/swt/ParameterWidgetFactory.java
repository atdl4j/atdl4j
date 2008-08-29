package br.com.investtools.fix.atdl.ui.swt;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;

public interface ParameterWidgetFactory {

	public ParameterWidget<?> create(ParameterT parameter);

}
