package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.BooleanT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class CheckBoxParameterWidget extends AbstractParameterWidget<Boolean> {

	private static final String BOOLEAN_FALSE = "N";

	private static final String BOOLEAN_TRUE = "Y";

	private ParameterT parameter;

	private Button checkBox;

	private Label label;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		// checkBox
		Button checkBox = new Button(parent, style | SWT.CHECK);
		this.checkBox = checkBox;

		// tooltip
		String tooltip = parameter.getTooltip();
		checkBox.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		// init value

		BooleanT booleanT = (BooleanT) parameter;

		if (booleanT.isSetInitValue()) {
			checkBox.setSelection(booleanT.getInitValue());
		}

		return parent;
	}

	@Override
	public Boolean getValue() {

		if (checkBox.getSelection())
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	@Override
	public void setValue(Boolean value) {
		checkBox.setSelection(value.booleanValue());
	}
	
	@Override
	public String getValueAsString() {
		return getBooleanAsString(getValue());
	}

	private String getBooleanAsString(Boolean value) {
		BooleanT booleanT = (BooleanT) parameter;

		if (value != null) {
			if (value.booleanValue()) {
				if (booleanT.isSetTrueWireValue())
					return booleanT.getTrueWireValue();
				else
					return BOOLEAN_TRUE;
			} else {
				if (booleanT.isSetFalseWireValue())
					return booleanT.getFalseWireValue();
				else
					return BOOLEAN_FALSE;
			}
		} else {
			return BOOLEAN_FALSE;
		}
	}

	@Override
	public Boolean convertValue(String value) {

		if (value.equalsIgnoreCase("true") || value.equals("1")) {
			return new Boolean(true);
		} else if (value.equalsIgnoreCase("false") || value.equals("0")) {
			return new Boolean(false);
		} else {
			return new Boolean(false);
		}

	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
		checkBox.addListener(SWT.Selection, listener);
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(checkBox);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		checkBox.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	@Override
	public void removeListener(Listener listener) {
		checkBox.removeListener(SWT.Selection, listener);
	}

}
