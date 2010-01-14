package org.atdl4j.ui.impl;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.HiddenFieldT;

public abstract class HiddenFieldUI extends AbstractControlUI<String> {

	protected String value;

	public HiddenFieldUI(HiddenFieldT control, ParameterT parameter)
			throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		this.setValue(getConstInitValue());
		init();
	}

	private String getConstInitValue() {
		return ((HiddenFieldT) control).getInitValue();
	}

	public String getControlValue() {
		return value;
	}

	public Object getParameterValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setEnabled(boolean enabled) {
		// Do nothing
	}

	public void setVisible(boolean visible) {
		// Do nothing
	}
}
