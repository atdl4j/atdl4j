package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.EnumPairT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class RadioButtonParameterWidget implements ParameterUI<String> {

	private ParameterT parameter;

	private List<Button> radioButton = new ArrayList<Button>();

	private Label label;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());

		// tooltip
		String tooltip = parameter.getTooltip();
		l.setToolTipText(tooltip);

		// radioButton
		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			Button radioElement = new Button(c, style | SWT.RADIO);
			radioElement.setText(enumPair.getUiRep());
			radioElement.setToolTipText(tooltip);
			radioButton.add(radioElement);
		}

		return c;
	}

	@Override
	public String getValue() {
		for (int i = 0; i < this.radioButton.size(); i++) {
			Button b = radioButton.get(i);
			if (b.getSelection()) {
				return parameter.getEnumPairArray(i).getWireValue();
			}
		}
		return "";

	}

	@Override
	public void setValue(String value) {
		for (int i = 0; i < radioButton.size(); i++) {
			if (value.equals(parameter.getEnumPairArray(i).getWireValue())) {
				Button b = radioButton.get(i);
				b.setSelection(true);
			}
		}
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue();
		} else {
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			String value = getValue();
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public String convertValue(String value) {
		return null;
	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
		for (Button radioElement : radioButton) {
			radioElement.addListener(SWT.Selection, listener);
		}
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.addAll(radioButton);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		// wrap around ParameterListener which raises a ParameterEvent
		Listener wrapper = new ParameterListenerWrapper(this, listener);
		for (Button b : radioButton) {
			b.addListener(SWT.Selection, wrapper);
		}
	}

	@Override
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
