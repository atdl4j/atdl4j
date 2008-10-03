package br.com.investtools.fix.atdl.ui.swt.impl;

import org.eclipse.swt.widgets.Event;

import br.com.investtools.fix.atdl.ui.swt.ParameterUI;

public class ParameterEvent extends Event {

	private ParameterUI<?> parameter;

	public ParameterEvent(ParameterUI<?> parameter) {
		this.parameter = parameter;
	}

	public ParameterUI<?> getParameter() {
		return parameter;
	}

}
