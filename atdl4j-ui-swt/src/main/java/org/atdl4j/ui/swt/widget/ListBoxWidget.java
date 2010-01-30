package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBException;


import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.atdl.layout.MultiSelectListT;
import org.atdl4j.atdl.layout.SingleSelectListT;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;


public class ListBoxWidget extends AbstractSWTWidget<String> {
	
	private List listBox;
	private Label label;

	public ListBoxWidget(SingleSelectListT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public ListBoxWidget(MultiSelectListT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {
		
		// label
		label = new Label(parent, SWT.NONE);
// 1/20/2010 Scott Atwell avoid NPE as label is not required on Control		label.setText(control.getLabel());
		if ( control.getLabel() != null )
		{
			label.setText(control.getLabel());
		}
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		// dropDownList
		style = style | SWT.BORDER;
		if (control instanceof MultiSelectListT) {
			style |= SWT.MULTI;
		} else if (control instanceof SingleSelectListT) {
			style |= SWT.SINGLE;
		}
		listBox =  new List(parent, style);
		listBox.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		// listBox items
		java.util.List<ListItemT> listItems = 
			control instanceof MultiSelectListT ? ((MultiSelectListT)control).getListItem() : 
						 ((SingleSelectListT)control).getListItem();
		for (ListItemT listItem : listItems) {
			listBox.add(listItem.getUiRep());
		}
		
		// tooltip
		String tooltip = control.getTooltip();
		listBox.setToolTipText(tooltip);
		label.setToolTipText(tooltip);

		// init value
		String initValue = 
			control instanceof MultiSelectListT ? ((MultiSelectListT)control).getInitValue() : 
				 ((SingleSelectListT)control).getInitValue();
		if (initValue != null)
			setValue(initValue, true);
		
		return parent;
	}

	public String getControlValue() {
//TODO 1/24/2010 Scott Atwell added
		if ( ( listBox.isVisible() == false ) || ( listBox.isEnabled() == false ) )
		{
			return null;
		}
		
		String value = "";
		java.util.List<ListItemT> listItems = 
			control instanceof MultiSelectListT ? ((MultiSelectListT)control).getListItem() : 
						 ((SingleSelectListT)control).getListItem();
		int[] selection = listBox.getSelectionIndices();

		for (int i = 0; i < selection.length; i++) {
			value += listItems.get(selection[i]).getEnumID();
			if (i + 1 != selection.length)
				value += " ";
		}
		return "".equals(value) ? null : value;
	}

	public String getParameterValue() {
		// Helper method from AbstractControlUI
		return getParameterValueAsMultipleValueString();
	}
	
	public void setValue(String value)
	{
		this.setValue(value, false);
	}
	
	public void setValue(String value, boolean setValueAsControl) {
		// split string by spaces in case of MultiSelectList
		java.util.List<String> values = Arrays.asList(value.split("\\s"));
		for (String singleValue : values)
		{
			for (int i = 0; i < getListItems().size(); i++)
			{
				if (setValueAsControl || parameter == null)
				{
					if (getListItems().get(i).getEnumID().equals(singleValue))
					{
						listBox.select(i);
						break;
					}
				} else {
					if (parameter.getEnumPair().get(i).getWireValue().equals(singleValue))
					{
						listBox.select(i);
						break;
					}
				}
			}
		}
	}	
	
	public void generateStateRuleListener(Listener listener) {
		listBox.addListener(SWT.Selection, listener);
	}

	public java.util.List<Control> getControls() {
		java.util.List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(listBox);
		return widgets;
	}

	public void addListener(Listener listener) {
		listBox.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	public void removeListener(Listener listener) {
		listBox.removeListener(SWT.Selection, listener);
	}
}
