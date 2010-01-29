package org.atdl4j.ui.impl;

import org.fixprotocol.atdl_1_1.layout.LabelT;

/* 
 * This class intentionally does not support parameterRef or initValue
 * which LabelT inherits from ControlT.
 * 
 * Use HiddenField if you'd like to have control associated with a parameter. 
 */

public abstract class LabelUI extends AbstractControlUI<String> {

	public LabelUI(LabelT control) {
		this.control = control;
		this.parameter = null;
	}
	
	public void setValue(String value) {
		// do nothing
	}
	
	public String getControlValue() {
		return null; // Labels cannot store values
	}

	public Object getParameterValue() {
		return null; // Labels cannot store values
	}
}
