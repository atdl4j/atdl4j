package org.atdl4j.ui.swt;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.eclipse.swt.widgets.Composite;

public interface SWTWidgetFactory 
{
	public SWTWidget<?> createWidget(Composite parent, ControlT control, ParameterT parameter, int style);
}