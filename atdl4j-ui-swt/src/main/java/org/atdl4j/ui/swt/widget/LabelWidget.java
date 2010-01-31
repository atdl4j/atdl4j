package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.ui.impl.LabelUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import org.atdl4j.atdl.layout.LabelT;

public class LabelWidget extends LabelUI implements SWTWidget<String> {

	private Label label;

	public LabelWidget(LabelT control) {
		super(control);
	}

	public Widget createWidget(Composite parent, int style)
			throws JAXBException {
				
		// label
		label = new Label(parent, SWT.NONE);
		
//TODO 1/19/2010 Scott Atwell (some brokers provide initValue only, fails if null)		label.setText(control.getLabel());
//TODO 1/19/2010 Scott Atwell changes BELOW
		if ( control.getLabel() != null )
		{
			label.setText(control.getLabel());
		}
		else if ( ( control instanceof LabelT ) &&
				    ( ((LabelT) control).getInitValue() != null ) )
		{
			label.setText( ((LabelT) control).getInitValue() );
		}
		else
		{
			label.setText( "" );
		}
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
				
		// tooltip
		String tooltip = control.getTooltip();
		if (tooltip != null) label.setToolTipText(tooltip);

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

	public void setVisible(boolean visible)
	{
		for (Control control : getControls()) {
			control.setVisible(visible);
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		for (Control control : getControls()) {
			control.setEnabled(enabled);
		}
	}
}
