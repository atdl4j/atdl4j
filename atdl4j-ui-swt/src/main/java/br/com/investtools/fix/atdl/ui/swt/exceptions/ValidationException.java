package br.com.investtools.fix.atdl.ui.swt.exceptions;

import br.com.investtools.fix.atdl.ui.swt.ParameterUI;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ParameterUI<?> widget;

	public ValidationException(ParameterUI<?> widget) {
		super();
		this.widget = widget;
	}

	public ValidationException(ParameterUI<?> widget, String message) {
		super(message);
		this.widget = widget;
	}

	public ParameterUI<?> getWidget() {
		return widget;
	}

}
