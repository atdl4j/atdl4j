package org.atdl4j.ui.swt;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.widgets.Composite;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.layout.ControlT;

public interface SWTWidgetFactory {

	public SWTWidget<?> create(Composite parent, ControlT control, ParameterT parameter, int style) throws JAXBException;
}