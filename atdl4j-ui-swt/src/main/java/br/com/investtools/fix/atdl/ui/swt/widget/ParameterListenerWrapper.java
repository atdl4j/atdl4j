package br.com.investtools.fix.atdl.ui.swt.widget;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import br.com.investtools.fix.atdl.ui.swt.ParameterEvent;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class ParameterListenerWrapper implements Listener {

	private ParameterWidget<?> parameter;

	public Listener getDelegate() {
		return delegate;
	}

	private Listener delegate;

	public ParameterListenerWrapper(ParameterWidget<?> parameter,
			Listener delegate) {
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
