package org.atdl4j.data.exception;

import org.atdl4j.ui.Atdl4jWidget;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Atdl4jWidget<?> target;

	public ValidationException(Atdl4jWidget<?> target) {
		super();
		this.target = target;
	}

	public ValidationException(Atdl4jWidget<?> target, String message) {
		super(message);
		this.target = target;
	}

	public Atdl4jWidget<?> getTarget() {
		return target;
	}

}
