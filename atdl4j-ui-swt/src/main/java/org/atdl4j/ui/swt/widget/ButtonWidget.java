package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.RadioButtonT;

/*
 * Implements either a CheckBox or a RadioButton
 */
public class ButtonWidget extends AbstractSWTWidget<Boolean> {

	private Button button;
	private Label label;

	public ButtonWidget(CheckBoxT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init(); 
	}
	
	public ButtonWidget(RadioButtonT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init(); 
	}

	public Widget createWidget(Composite parent, int style) {

		// button
		button = new Button(parent, style
				| (control instanceof RadioButtonT ? SWT.RADIO : SWT.CHECK));
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		button.setLayoutData(gd);
				
		if (control.getLabel() != null) button.setText(control.getLabel());
		if (control.getTooltip() != null) button.setToolTipText(control.getTooltip());

		// init value
		if (control instanceof RadioButtonT) {
			if (((RadioButtonT)control).isInitValue() != null) 
				button.setSelection(((RadioButtonT)control).isInitValue());
		} else {
			if (((CheckBoxT)control).isInitValue() != null)
				button.setSelection(((CheckBoxT)control).isInitValue());
		}
		return parent;
	}

	public void setValue(Boolean value) {
		button.setSelection(value.booleanValue());
	}

	public void generateStateRuleListener(Listener listener) {
		button.addListener(SWT.Selection, listener);
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(button);
		return widgets;
	}

	public void addListener(Listener listener) {
		button.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	public void removeListener(Listener listener) {
		button.removeListener(SWT.Selection, listener);
	}

	public Boolean getControlValue() {
//TODO 1/24/2010 Scott Atwell added
		if ( ( button.isVisible() == false ) || ( button.isEnabled() == false ) )
		{
			return null;
		}
		
		if (button.getSelection())
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	public Boolean getParameterValue() {
		return getControlValue();
	}
}
