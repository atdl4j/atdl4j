package org.atdl4j.ui.swt;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ControlT;
import org.eclipse.swt.widgets.Composite;

public interface SWTWidgetFactory {

	public SWTWidget<?> create(Composite parent, ControlT control, ParameterT parameter, int style) throws JAXBException;
}