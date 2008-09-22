package br.com.investtools.fix.atdl.ui.swt.test;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import br.com.investtools.fix.atdl.ui.swt.ParameterEvent;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class SideListener implements Listener {

	private Composite composite;
	private Color buyColor;
	private Color sellColor;

	public SideListener(Composite composite, Color buyColor, Color sellColor) {
		this.composite = composite;
		this.buyColor = buyColor;
		this.sellColor = sellColor;
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof ParameterEvent) {
			ParameterWidget<?> pw = ((ParameterEvent) event).getParameter();
			Object value = pw.getValue();
			if (value != null) {
				if (value.toString().equals("0")) {
					composite.setBackground(buyColor);
					composite.getParent().setBackground(buyColor);
				} else if (value.toString().equals("1")) {
					composite.setBackground(sellColor);
					composite.getParent().setBackground(sellColor);
				}
			}
		}
	}
}
