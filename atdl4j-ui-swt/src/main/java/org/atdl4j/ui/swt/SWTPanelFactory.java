package org.atdl4j.ui.swt;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.widgets.Composite;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.StrategyPanelT;

public interface SWTPanelFactory {

	public Map<String, SWTWidget<?>> create(Composite parent,
			StrategyPanelT panel, Map<String, ParameterT> parameters, int style) throws JAXBException;

}