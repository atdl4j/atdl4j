package br.com.investtools.fix.atdl.ui.swt;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.widgets.Composite;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;

public interface WidgetFactory {

	public ParameterUI<?> create(Composite parent, ParameterT parameter,
			int style) throws XmlException;

}
