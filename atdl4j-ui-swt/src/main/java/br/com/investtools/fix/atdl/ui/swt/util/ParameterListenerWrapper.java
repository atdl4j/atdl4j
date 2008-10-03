package br.com.investtools.fix.atdl.ui.swt.util;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.impl.ParameterEvent;

public class ParameterListenerWrapper implements Listener {

	private ParameterUI<?> parameter;

	public Listener getDelegate() {
		return delegate;
	}

	private Listener delegate;

	public ParameterListenerWrapper(ParameterUI<?> parameter, Listener delegate) {
		this.parameter = parameter;
		this.delegate = delegate;
	}

	@Override
	public void handleEvent(Event event) {
		delegate.handleEvent(new ParameterEvent(parameter));
	}

	@Override
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
