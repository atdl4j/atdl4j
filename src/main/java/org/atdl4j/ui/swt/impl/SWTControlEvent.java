package org.atdl4j.ui.swt.impl;

import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Event;



public class SWTControlEvent extends Event {

	private SWTWidget<?> widget;

	public SWTControlEvent(SWTWidget<?> aWidget) {
		this.widget = aWidget;
	}

	public SWTWidget<?> getWidget() {
		return widget;
	}
}