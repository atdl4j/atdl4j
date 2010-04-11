package org.atdl4j.ui.swt;

import java.util.Map;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.eclipse.swt.widgets.Composite;

public interface SWTPanelFactory 
{
	public Map<String, SWTWidget<?>> createStrategyPanelAndWidgets(Composite parent,
			StrategyPanelT panel, Map<String, ParameterT> parameters, int style);

}