package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import org.atdl4j.atdl.core.BooleanT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.RadioButtonT;

/*
 * Implements either a CheckBox or a RadioButton
 */
public class ButtonWidget extends AbstractSWTWidget<Boolean> {

    	// TODO move this to app constanst
	public static final String NULL_STRING = "{NULL}";
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
		if (getTooltip() != null) button.setToolTipText(getTooltip());

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

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(button);
		return widgets;
	}
	
	public Button getButton() {
		return button;
	}

	public void addListener(Listener listener) {
		button.addListener(SWT.Selection, listener);
	}

	public void removeListener(Listener listener) {
		button.removeListener(SWT.Selection, listener);
	}

	public Boolean getControlValue() {
	    // 1/24/2010 Scott Atwell added
	    if (!button.isVisible() || !button.isEnabled()) return null;
	    return button.getSelection() ? Boolean.TRUE : Boolean.FALSE;
	}

    // 2/1/2010 John Shields added
    // Parameter value looks up checkedEnumRef and uncheckedEnumRef
    public Object getParameterValue()
    {
	if (getControlValue() == null) return null;
	else if (parameter instanceof BooleanT)
	{
	    return getControlValue(); // Short-circuit for Boolean parameters
	}
	else if (getControlValue() == Boolean.TRUE)
	{
	    String checkedEnumRef = control instanceof RadioButtonT ?
		    ((RadioButtonT)control).getCheckedEnumRef() :
		    ((CheckBoxT)control).getCheckedEnumRef();
	    if (checkedEnumRef != null &&
		!checkedEnumRef.equals(""))
	    {
		if (checkedEnumRef.equals(ButtonWidget.NULL_STRING)) return null;
		else return getEnumWireValue(checkedEnumRef);
	    } else return Boolean.TRUE;
	}
    	else if (getControlValue() == Boolean.FALSE)
	{
	    String uncheckedEnumRef = control instanceof RadioButtonT ?
		    ((RadioButtonT)control).getUncheckedEnumRef() :
		    ((CheckBoxT)control).getUncheckedEnumRef();
	    if (uncheckedEnumRef != null &&
		!uncheckedEnumRef.equals(""))
	    {
		if (uncheckedEnumRef.equals(ButtonWidget.NULL_STRING)) return null;
		else return getEnumWireValue(uncheckedEnumRef);
	    } else return Boolean.FALSE;
	}
	return null;
    }
}
