package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.DropDownListT;
import org.atdl4j.atdl.layout.EditableDropDownListT;
import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class DropDownListWidget extends AbstractSWTWidget<String> {

	private Combo dropDownList;
	private Label label;

	public DropDownListWidget(DropDownListT control, ParameterT parameter)
			throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public DropDownListWidget(EditableDropDownListT control,
			ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(control.getLabel());
		this.label = l;

		// dropDownList
		style = style | SWT.BORDER;
		if (control instanceof DropDownListT) {
			style |= SWT.READ_ONLY;
		}
		Combo dropDownList = new Combo(parent, style);
		this.dropDownList = dropDownList;
		dropDownList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));

		// dropDownList itens
		java.util.List<ListItemT> listItems = (control instanceof EditableDropDownListT) ? ((EditableDropDownListT) control)
				.getListItem()
				: ((DropDownListT) control).getListItem();
		// TODO: throw error if there are no list items
		for (ListItemT listItem : listItems)
			dropDownList.add(listItem.getUiRep());

		// tooltip
		String tooltip = control.getTooltip();
		dropDownList.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		// init value
		String initValue = (control instanceof EditableDropDownListT) ? ((EditableDropDownListT) control)
				.getInitValue()
				: ((DropDownListT) control).getInitValue();

		// default initializer
		dropDownList.select(0);
		// look for init value
		if (initValue != null) {
			String[] items = dropDownList.getItems();
			for (int i = 0; i < items.length; i++) {
				if (parameter != null) {
					// match based on WireValue
					// getEnumWireValue(enumID);
				} else {
					// match based on UiRep -- TODO: should this match on
					// EnumID?
					if (initValue.equals(items[i]))
						dropDownList.select(i);
				}
			}
		}

		return parent;
	}

	// Helper to get list items
	protected List<ListItemT> getListItems() {
		return (control instanceof EditableDropDownListT) ? ((EditableDropDownListT) control)
				.getListItem()
				: ((DropDownListT) control).getListItem();
	}

	public String getControlValue() {
		int selection = dropDownList.getSelectionIndex();
		if (selection >= 0)
			return getListItems().get(selection).getEnumID();

		else if (control instanceof EditableDropDownListT
				&& dropDownList.getText() != null
				&& dropDownList.getText() != "") {
			// use the enumID if the text matches a combo box item,
			// even if the dropdown was not used to select it
			for (int i = 0; i < dropDownList.getItems().length; i++) {
				if (dropDownList.getItems()[i].equals(dropDownList.getText()))
					return getListItems().get(i + 1).getEnumID();
			}
			// else use the manually entered text string
			return dropDownList.getText();
		}
		return null;
	}

	public String getParameterValue() {
		int selection = dropDownList.getSelectionIndex();
		if (selection >= 0)
			return getParameterValueAsEnumWireValue();
		if (control instanceof EditableDropDownListT
				&& dropDownList.getText() != null
				&& dropDownList.getText() != "") {
			// use the Parameter's Enum wireValue if the text matches a combo
			// box item,
			// even if the dropdown was not used to select it
			for (int i = 0; i < dropDownList.getItems().length; i++) {
				if (dropDownList.getItems()[i].equals(dropDownList.getText()))
					return getEnumWireValue(getListItems().get(i + 1)
							.getEnumID());
			}
			// else use the manually entered text string
			return dropDownList.getText();
		}
		return null;

	}

	public void setValue(String value) {
		this.setValue(value, false);
	}

	public void setValue(String value, boolean setValueAsControl) {
		for (int i = 0; i < getListItems().size(); i++) {
			if (setValueAsControl || parameter == null) {
				if (getListItems().get(i).getEnumID().equals(value)) {
					dropDownList.select(i);
					break;
				}
			} else {
				if (parameter.getEnumPair().get(i).getWireValue().equals(value)) {
					dropDownList.select(i);
					break;
				}
			}
		}
		// TODO: needs to handle case of editable dropdown list;
	}

	public void generateStateRuleListener(Listener listener) {
		dropDownList.addListener(SWT.Modify, listener);
		dropDownList.addListener(SWT.Selection, listener);
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(dropDownList);
		return widgets;
	}

	public void addListener(Listener listener) {
		dropDownList.addListener(SWT.Selection, new ParameterListenerWrapper(
				this, listener));
	}

	public void removeListener(Listener listener) {
		dropDownList.removeListener(SWT.Selection, listener);
	}
}
