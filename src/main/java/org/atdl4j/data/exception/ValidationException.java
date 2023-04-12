package org.atdl4j.data.exception;

import org.atdl4j.ui.Atdl4jWidget;

/** 
 * ValidationException is thrown whenever a control validation
 * fails during a user-initiated validation event (e.g. when 
 * outputting a FIX message from the screen.)
 */
public class ValidationException extends Exception
{
	private static final long serialVersionUID = -156407004102010541L;
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
