package org.atdl4j.ui.swing.impl;

import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Event;



public class SwingControlEvent extends Event {

	private SWTWidget<?> widget;

	public SwingControlEvent(SWTWidget<?> widget) {
		this.widget = widget;
	}

	public SWTWidget<?> getWidget() {
		return widget;
	}
}