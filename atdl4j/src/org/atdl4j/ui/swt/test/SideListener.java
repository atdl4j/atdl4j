package org.atdl4j.ui.swt.test;

import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.impl.SWTControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class SideListener implements Listener {

	private Composite composite;
	private Color buyColor;
	private Color sellColor;

	public SideListener(Composite composite, Color buyColor, Color sellColor) {
		this.composite = composite;
		this.buyColor = buyColor;
		this.sellColor = sellColor;
	}

	public void handleEvent(Event event) {
		if (event instanceof SWTControlEvent) {
			SWTWidget<?> pw = ((SWTControlEvent) event).getWidget();
			Object value = pw.getControlValue();
			if (value != null) {
				if (value.toString().equals("0")) {
					for (int i = 0; i < composite.getChildren().length; i++) {
						composite.getChildren()[i].setBackground(buyColor);
						if (composite.getChildren()[i] instanceof Composite) {
							Composite innerComposite = (Composite) composite
									.getChildren()[i];
							for (int j = 0; j < innerComposite.getChildren().length; j++) {
								innerComposite.getChildren()[j]
										.setBackground(buyColor);
							}
						}
					}
					composite.getParent().setBackground(buyColor);
				} else if (value.toString().equals("1")) {
					for (int i = 0; i < composite.getChildren().length; i++) {
						composite.getChildren()[i].setBackground(sellColor);
						if (composite.getChildren()[i] instanceof Composite) {
							Composite innerComposite = (Composite) composite
									.getChildren()[i];
							for (int j = 0; j < innerComposite.getChildren().length; j++) {
								innerComposite.getChildren()[j]
										.setBackground(sellColor);
							}
						}
					}
					composite.getParent().setBackground(sellColor);
				}
			}
		}
	}
}
