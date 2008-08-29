package br.com.investtools.fix.atdl.ui.swt;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;

public interface PanelFactory {

	public Map<String, ParameterWidget<?>> create(Composite parent, StrategyPanel panel, int style);

}