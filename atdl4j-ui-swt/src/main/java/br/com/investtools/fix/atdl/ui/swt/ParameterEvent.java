package br.com.investtools.fix.atdl.ui.swt;

import org.eclipse.swt.widgets.Event;

public class ParameterEvent extends Event {

	private ParameterWidget<?> parameter;

	public ParameterEvent(ParameterWidget<?> parameter) {
		this.parameter = parameter;
	}

	public ParameterWidget<?> getParameter() {
		return parameter;
	}

}
