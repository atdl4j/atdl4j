package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.EnumPairT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class CheckBoxListWidget extends AbstractSWTWidget<String> {

	private List<Button> multiCheckBox = new ArrayList<Button>();
	private Label label;

	public CheckBoxListWidget(CheckBoxListT control, ParameterT parameter)
			throws JAXBException {

		// validate ListItems and EnumPairs
		if (parameter != null
				&& control.getListItem().size() != parameter.getEnumPair()
						.size())
			throw new JAXBException("ListItems for Control \""
					+ control.getID() + "\" and EnumPairs for Parameter \""
					+ parameter.getName() + "\" are not equal in number.");

		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {
		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(control.getLabel());
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());

		// tooltip
		String tooltip = control.getTooltip();
		l.setToolTipText(tooltip);

		// checkBoxes
		List<ListItemT> listItems = ((CheckBoxListT) control).getListItem();
		for (ListItemT listItem : listItems) {
			Button checkBox = new Button(c, style | SWT.CHECK);
			checkBox.setText(listItem.getUiRep());
			if (parameter != null) {
				for (EnumPairT enumPair : parameter.getEnumPair()) {
					if (enumPair.getEnumID() == listItem.getEnumID()) {
						checkBox.setToolTipText(enumPair.getDescription());
						break;
					}
				}
			} else
				checkBox.setToolTipText(control.getTooltip());
			multiCheckBox.add(checkBox);
		}

		// set initValue
		if (((CheckBoxListT) control).getInitValue() != null)
			setValue(((CheckBoxListT) control).getInitValue(), true);

		return parent;
	}

	public String getControlValue() {
		String value = "";
		for (int i = 0; i < multiCheckBox.size(); i++) {
			Button b = multiCheckBox.get(i);
			if (b.getSelection()) {
				if ("".equals(value)) {
					value += ((CheckBoxListT) control).getListItem().get(i)
							.getEnumID();
				} else {
					value += " "
							+ ((CheckBoxListT) control).getListItem().get(i)
									.getEnumID();
				}
			}
		}
		return "".equals(value) ? null : value;
	}

	public String getParameterValue() {
		// Helper method from AbstractControlUI
		return getParameterValueAsMultipleValueString();
	}

	public void setValue(String value) {
		this.setValue(value, false);
	}

	public void setValue(String value, boolean setValueAsControl) {
		List<String> values = Arrays.asList(value.split("\\s"));
		for (int i = 0; i < multiCheckBox.size(); i++) {
			Button b = multiCheckBox.get(i);
			if (setValueAsControl || parameter == null) {
				String enumID = ((CheckBoxListT) control).getListItem().get(i)
						.getEnumID();
				b.setSelection(values.contains(enumID));
			} else {
				String wireValue = parameter.getEnumPair().get(i)
						.getWireValue();
				b.setSelection(values.contains(wireValue));
			}
		}
	}

	public void generateStateRuleListener(Listener listener) {
		for (Button checkBox : multiCheckBox) {
			checkBox.addListener(SWT.Selection, listener);
		}
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.addAll(multiCheckBox);
		return widgets;
	}

	public void addListener(Listener listener) {
		Listener wrapper = new ParameterListenerWrapper(this, listener);
		for (Button b : multiCheckBox) {
			b.addListener(SWT.Selection, wrapper);
		}
	}

	public void removeListener(Listener listener) {
		for (Button b : multiCheckBox) {
			b.removeListener(SWT.Selection, listener);
		}
	}
}
