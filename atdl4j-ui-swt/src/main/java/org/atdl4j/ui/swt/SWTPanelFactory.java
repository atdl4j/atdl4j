package org.atdl4j.ui.swt;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.widgets.Composite;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.layout.StrategyPanelT;

public interface SWTPanelFactory {

	public Map<String, SWTWidget<?>> create(Composite parent,
			StrategyPanelT panel, Map<String, ParameterT> parameters, int style) throws JAXBException;

}