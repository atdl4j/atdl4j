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
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class DropDownListParameterWidget extends
		AbstractParameterWidget<String> {

	private ParameterT parameter;

	private Combo dropDownList;

	private Label label;

	private boolean editable;

	public DropDownListParameterWidget(boolean editable) {
		this.editable = editable;
	}

	public DropDownListParameterWidget() {
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
		if (!editable) {
			style |= SWT.READ_ONLY;
		}
		Combo dropDownList = new Combo(parent, style);
		this.dropDownList = dropDownList;
		dropDownList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		// dropDownList itens
		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			dropDownList.add(enumPair.getUiRep());
		}

		// tooltip
		String tooltip = parameter.getTooltip();
		dropDownList.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		// init value
		String initValue = getInitValue(parameter);

		if (initValue != null) {
			String[] itens = dropDownList.getItems();
			for (int i = 0; i < itens.length; i++) {
				if (initValue.equals(itens[i]))
					dropDownList.select(i);
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

	@Override
	public String getValue() {
		int selection = dropDownList.getSelectionIndex();

		if (selection >= 0) {
			EnumPairT e = parameter.getEnumPairArray(selection);
			return e.getWireValue();
		} else {
			return null;
		}

	}

	@Override
	public void setValue(String value) {
		int index = dropDownList.indexOf(value);
		dropDownList.select(index);
	}

	@Override
	public String getValueAsString() {
		return getValue();
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
		dropDownList.addListener(SWT.Modify, listener);
		dropDownList.addListener(SWT.Selection, listener);
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(dropDownList);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		dropDownList.addListener(SWT.Selection, new ParameterListenerWrapper(
				this, listener));
	}

	@Override
	public void removeListener(Listener listener) {
		dropDownList.removeListener(SWT.Selection, listener);
	}

}
