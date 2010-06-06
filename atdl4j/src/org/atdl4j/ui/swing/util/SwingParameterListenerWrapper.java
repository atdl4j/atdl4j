package org.atdl4j.ui.swing.util;

import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.impl.SWTControlEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;



public class SwingParameterListenerWrapper implements Listener {

	private SWTWidget<?> parameter;

	public Listener getDelegate() {
		return delegate;
	}

	private Listener delegate;

	public SwingParameterListenerWrapper(SWTWidget<?> parameter, Listener delegate) {
		this.parameter = parameter;
		this.delegate = delegate;
	}

	public void handleEvent(Event event) {
		delegate.handleEvent(new SWTControlEvent(parameter));
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof Listener) {
			Listener l = (Listener) obj;
			return l == delegate;
		}

		return super.equals(obj);
	}
}
