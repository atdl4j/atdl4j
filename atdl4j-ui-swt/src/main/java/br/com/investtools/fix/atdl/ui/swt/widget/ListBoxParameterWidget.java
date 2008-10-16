package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.CharT;
import br.com.investtools.fix.atdl.core.xmlbeans.EnumPairT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleCharValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleStringValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StringT;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class ListBoxParameterWidget extends AbstractParameterWidget<String> {

	private ParameterT parameter;

	private List listBox;

	private Label label;

	private boolean multi;

	public ListBoxParameterWidget(boolean multi) {
		this.multi = multi;
	}

	public ListBoxParameterWidget() {
		this(false);
	}

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		// dropDownList
		style = style | SWT.BORDER;
		if (multi) {
			style |= SWT.MULTI;
		} else {
			style |= SWT.SINGLE;
		}
		List listBox = new List(parent, style);
		this.listBox = listBox;
		listBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// listBox itens
		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			listBox.add(enumPair.getUiRep());
		}

		// tooltip
		String tooltip = parameter.getTooltip();
		listBox.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		// init value
		String initValue = getInitValue(parameter);

		java.util.List<String> selectedValues = new ArrayList<String>(Arrays
				.asList(initValue.split("\\s")));

		if (initValue != null) {
			String[] itens = listBox.getItems();
			for (int i = 0; i < itens.length; i++) {
				if (selectedValues.contains(itens[i]))
					listBox.select(i);
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

		} else if (parameter instanceof MultipleCharValueT) {
			MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
			if (multipleCharValueT.isSetInitValue())
				return multipleCharValueT.getInitValue();

		} else if (parameter instanceof MultipleStringValueT) {
			MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
			if (multipleStringValueT.isSetInitValue())
				return multipleStringValueT.getInitValue();

		}

		return null;
	}

	@Override
	public String getValue() {
		int[] selection = listBox.getSelectionIndices();
		String value = "";

		for (int i = 0; i < selection.length; i++) {
			EnumPairT e = parameter.getEnumPairArray(selection[i]);
			value += e.getWireValue();
			if (i + 1 != selection.length)
				value += " ";

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
		int index = listBox.indexOf(value);
		listBox.select(index);
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
		listBox.addListener(SWT.Selection, listener);
	}

	@Override
	public java.util.List<Control> getControls() {
		java.util.List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(listBox);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		listBox.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	@Override
	public void removeListener(Listener listener) {
		listBox.removeListener(SWT.Selection, listener);
	}

}
