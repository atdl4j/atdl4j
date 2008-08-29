package br.com.investtools.fix.atdl.ui.swt;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ParameterWidget<?> widget;

	public ValidationException(ParameterWidget<?> widget, String message) {
		super(message);
		this.widget = widget;
	}

	public ParameterWidget<?> getWidget() {
		return widget;
	}

}
