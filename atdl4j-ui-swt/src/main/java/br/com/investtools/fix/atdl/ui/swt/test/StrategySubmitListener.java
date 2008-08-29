package br.com.investtools.fix.atdl.ui.swt.test;

import java.util.Map;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class StrategySubmitListener implements SelectionListener {

	private StrategyEdit[] se;
	private Map<String, ParameterWidget<?>> pwMap;

	public StrategySubmitListener(Map<String, ParameterWidget<?>> pwMap,
			StrategyEdit[] se) {
		this.se = se;
		this.pwMap = pwMap;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		System.out.print("botao apertado");

	}

}
