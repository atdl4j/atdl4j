package org.atdl4j.ui.swt.widget;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.ui.impl.HiddenFieldUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.layout.HiddenFieldT;


public class HiddenFieldWidget extends HiddenFieldUI implements SWTWidget<String> {

	public HiddenFieldWidget(HiddenFieldT control, ParameterT parameter) throws JAXBException {
		super(control, parameter);
	}

	public Widget createWidget(Composite parent, int style) {
		return null;
	}
	
	public void generateStateRuleListener(Listener listener) {
	}

	public List<Control> getControls() {
		return null;
	}

	public void addListener(Listener listener) {
		// do nothing
	}

	public void removeListener(Listener listener) {
		// do nothing
	}
	
}
