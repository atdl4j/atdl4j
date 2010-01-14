package org.atdl4j.data.exception;

import org.atdl4j.ui.ControlUI;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ControlUI<?> target;

	public ValidationException(ControlUI<?> target) {
		super();
		this.target = target;
	}

	public ValidationException(ControlUI<?> target, String message) {
		super(message);
		this.target = target;
	}

	public ControlUI<?> getTarget() {
		return target;
	}

}
