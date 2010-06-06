package org.atdl4j.ui.swing;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.eclipse.swt.widgets.Composite;

public interface SwingWidgetFactory 
{
	public SwingWidget<?> createWidget(Composite parent, ControlT control, ParameterT parameter, int style);
}