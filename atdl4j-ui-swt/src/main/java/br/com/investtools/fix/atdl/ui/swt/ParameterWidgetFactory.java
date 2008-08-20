package br.com.investtools.fix.atdl.ui.swt;

import br.com.investtools.fix.atdl.layout.xmlbeans.ComponentT;

public interface ParameterWidgetFactory {

	public ParameterWidget create(ComponentT.Enum type);

}
