package br.com.investtools.fix.atdl.ui.swt;

import org.eclipse.swt.widgets.Composite;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;

public interface WidgetFactory {

	public ParameterWidget<?> create(Composite parent, ParameterT parameter, int style);

}
