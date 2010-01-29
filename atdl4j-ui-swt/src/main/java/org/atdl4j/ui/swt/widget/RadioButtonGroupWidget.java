package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.EnumPairT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.atdl.layout.RadioButtonGroupT;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class RadioButtonGroupWidget extends AbstractSWTWidget<String> {

	private List<Button> radioButton = new ArrayList<Button>();
	private Label label;

	public RadioButtonGroupWidget(RadioButtonGroupT control,
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

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());

		// tooltip
		String tooltip = control.getTooltip();
		l.setToolTipText(tooltip);

		// radioButton
		for (ListItemT listItem : ((RadioButtonGroupT) control).getListItem()) {

			Button radioElement = new Button(c, style | SWT.RADIO);
			radioElement.setText(listItem.getUiRep());
			if (parameter != null) {
				for (EnumPairT enumPair : parameter.getEnumPair()) {
					if (enumPair.getEnumID() == listItem.getEnumID()) {
						radioElement.setToolTipText(enumPair.getDescription());
						break;
					}
				}
			} else
				radioElement.setToolTipText(tooltip);
			radioButton.add(radioElement);
		}

		// set initValue
		if (((RadioButtonGroupT) control).getInitValue() != null)
			setValue(((RadioButtonGroupT) control).getInitValue(), true);

		return c;
	}

	public String getControlValue() {
		for (int i = 0; i < this.radioButton.size(); i++) {
			Button b = radioButton.get(i);
			if (b.getSelection()) {
				return ((RadioButtonGroupT) control).getListItem().get(i)
						.getEnumID();
			}
		}
		return null;
	}

	public String getParameterValue() {
		return getParameterValueAsEnumWireValue();
	}

	public void setValue(String value) {
		this.setValue(value, false);
	}

	public void setValue(String value, boolean setValueAsControl) {
		for (int i = 0; i < radioButton.size(); i++) {
			Button b = radioButton.get(i);
			if (setValueAsControl || parameter == null) {
				b.setSelection(value.equals(getListItems().get(i).getEnumID()));
			} else {
				b.setSelection(value.equals(parameter.getEnumPair().get(i)
						.getWireValue()));
			}
		}
	}

	public void generateStateRuleListener(Listener listener) {
		for (Button radioElement : radioButton) {
			radioElement.addListener(SWT.Selection, listener);
		}
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.addAll(radioButton);
		return widgets;
	}

	public void addListener(Listener listener) {
		// wrap around ParameterListener which raises a ParameterEvent
		Listener wrapper = new ParameterListenerWrapper(this, listener);
		for (Button b : radioButton) {
			b.addListener(SWT.Selection, wrapper);
		}
	}

	public void removeListener(Listener listener) {
		for (Button b : radioButton) {
			b.removeListener(SWT.Selection, listener);
			// Listener[] listeners = radio.getListeners(SWT.Selection);
			// for (int i = 0; i < listeners.length; i++) {
			// Listener l = listeners[i];
			// if (l instanceof ParameterListenerWrapper) {
			// ParameterListenerWrapper wrapper = (ParameterListenerWrapper) l;
			// if (wrapper.getDelegate() == listener) {
			// radio.removeListener(SWT.Selection, l);
			// return;
			// }
			// }
			// }
		}
	}

}
