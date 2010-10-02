package org.atdl4j.ui.swing;

import java.awt.Container;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;

public interface SwingWidgetFactory 
{
	public SwingWidget<?> createWidget(Container parent, ControlT control, ParameterT parameter);
}