package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.layout.LabelT;
import org.atdl4j.ui.impl.LabelUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class LabelWidget extends LabelUI implements SWTWidget<String> {

	private Label label;

	public LabelWidget(LabelT control) {
		super(control);
	}

	public Widget createWidget(Composite parent, int style)
			throws JAXBException {

		// label
		label = new Label(parent, SWT.NONE);
		label.setText(control.getLabel());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		// tooltip
		String tooltip = control.getTooltip();
		label.setToolTipText(tooltip);

		return parent;
	}

	public void generateStateRuleListener(Listener listener) {
		// do nothing
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		return widgets;
	}

	public void addListener(Listener listener) {
		// do nothing
	}

	public void removeListener(Listener listener) {
		// do nothing
	}

	public void setVisible(boolean visible) {
		for (Control control : getControls()) {
			control.setVisible(visible);
		}
	}

	public void setEnabled(boolean enabled) {
		for (Control control : getControls()) {
			control.setEnabled(enabled);
		}
	}
}
