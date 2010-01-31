package org.atdl4j.ui.swt;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.widgets.Composite;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ControlT;

public interface SWTWidgetFactory {

	public SWTWidget<?> create(Composite parent, ControlT control, ParameterT parameter, int style) throws JAXBException;
}