package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.CharT;
import br.com.investtools.fix.atdl.core.xmlbeans.EnumPairT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StringT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class ComboBoxParameterWidget implements ParameterUI<String> {

	private ParameterT parameter;

	private Combo comboBox;

	private Label label;

	private boolean editable;

	public ComboBoxParameterWidget(boolean editable) {
		this.editable = editable;
	}

	public ComboBoxParameterWidget() {
		this(false);
	}

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		// comboBox
		style = style | SWT.BORDER;
		if (!editable) {
			style |= SWT.READ_ONLY;
		}
		Combo comboBox = new Combo(parent, style);
		this.comboBox = comboBox;
		comboBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// comboBox itens
		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			comboBox.add(enumPair.getUiRep());
		}

		// tooltip
		String tooltip = parameter.getTooltip();
		comboBox.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		// init value
		String initValue = getInitValue(parameter);

		if (initValue != null) {
			String[] itens = comboBox.getItems();
			for (int i = 0; i < itens.length; i++) {
				if (initValue.equals(itens[i]))
					comboBox.select(i);
			}
		}

		return parent;
	}

	private String getInitValue(ParameterT parameter) {
		if (parameter instanceof CharT) {
			CharT charT = (CharT) parameter;
			if (charT.isSetInitValue())
				return charT.getInitValue();

		} else if (parameter instanceof StringT) {
			StringT stringT = (StringT) parameter;
			if (stringT.isSetInitValue())
				return stringT.getInitValue();

		}

		return null;
	}

	public String getValue() {
		int selection = comboBox.getSelectionIndex();

		if (selection > 0) {
			EnumPairT e = parameter.getEnumPairArray(selection);
			return e.getWireValue();
		} else
			return " ";

	}

	@Override
	public void setValue(String value) {
		int index = comboBox.indexOf(value);
		comboBox.select(index);
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
		return value;
	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
		comboBox.addListener(SWT.Modify, listener);
		comboBox.addListener(SWT.Selection, listener);
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(comboBox);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		comboBox.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	@Override
	public void removeListener(Listener listener) {
		comboBox.removeListener(SWT.Selection, listener);
	}

}
