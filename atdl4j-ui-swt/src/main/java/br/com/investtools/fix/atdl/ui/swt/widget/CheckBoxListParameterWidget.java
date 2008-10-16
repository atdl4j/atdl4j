package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class CheckBoxListParameterWidget extends
		AbstractParameterWidget<String> {

	private ParameterT parameter;

	private List<Button> multiCheckBox = new ArrayList<Button>();

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

		// checkBoxes
		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			Button checkBox = new Button(c, style | SWT.CHECK);
			checkBox.setText(enumPair.getUiRep());
			checkBox.setToolTipText(tooltip);
			multiCheckBox.add(checkBox);
		}

		return parent;
	}

	@Override
	public String getValue() {
		String value = "";
		for (int i = 0; i < multiCheckBox.size(); i++) {
			Button b = multiCheckBox.get(i);
			if (b.getSelection()) {
				if ("".equals(value)) {
					value += parameter.getEnumPairArray(i).getWireValue();
				} else {
					value += " " + parameter.getEnumPairArray(i).getWireValue();
				}
			}
		}
		if ("".equals(value)) {
			return null;
		} else {
			return value;
		}
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
	public void setValue(String value) {
		List<String> values = Arrays.asList(value.split(" "));
		for (int i = 0; i < multiCheckBox.size(); i++) {
			String wireValue = parameter.getEnumPairArray(i).getWireValue();
			Button b = multiCheckBox.get(i);
			b.setSelection(values.contains(wireValue));
		}
	}

	@Override
	public String convertValue(String value) {
		return value;
	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
		for (Button checkBox : multiCheckBox) {
			checkBox.addListener(SWT.Selection, listener);
		}
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.addAll(multiCheckBox);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		Listener wrapper = new ParameterListenerWrapper(this, listener);
		for (Button b : multiCheckBox) {
			b.addListener(SWT.Selection, wrapper);
		}
	}

	@Override
	public void removeListener(Listener listener) {
		for (Button b : multiCheckBox) {
			b.removeListener(SWT.Selection, listener);
		}
	}

}
