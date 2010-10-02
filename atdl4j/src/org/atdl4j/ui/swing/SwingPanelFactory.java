package org.atdl4j.ui.swing;

import java.awt.Container;
import java.util.Map;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;

public interface SwingPanelFactory 
{
	public Map<String, SwingWidget<?>> createStrategyPanelAndWidgets(Container parent,
			StrategyPanelT panel, Map<String, ParameterT> parameters);

}