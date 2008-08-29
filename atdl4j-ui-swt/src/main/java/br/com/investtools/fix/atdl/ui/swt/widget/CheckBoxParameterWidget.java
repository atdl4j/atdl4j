package br.com.investtools.fix.atdl.ui.swt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class CheckBoxParameterWidget implements ParameterWidget<Boolean> {

	private static final String BOOLEAN_FALSE = "0";

	private static final String BOOLEAN_TRUE = "1";

	private ParameterT parameter;

	private Button checkBox;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());

		// label
		Label l = new Label(c, SWT.NONE);
		l.setText(getLabelText(parameter));

		// checkBox
		Button checkBox = new Button(c, style | SWT.CHECK);
		this.checkBox = checkBox;

		// tooltip
		String tooltip = parameter.getTooltip();
		checkBox.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return c;
	}

	public String getLabelText(ParameterT parameter) {
		if (parameter.getUiRep() != null) {
			return parameter.getUiRep();
		}
		return parameter.getName();
	}

	public Boolean getValue() {
		if (checkBox.getSelection()) 
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}	

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue();
		} else {
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			String value = getBooleanAsString(getValue());
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	private String getBooleanAsString(Boolean value) {
		if (value != null) {
			if (value.booleanValue()) {
				return BOOLEAN_TRUE;
			} else {
				return BOOLEAN_FALSE;
			}
		} else {
			// TODO: what to do?
			return BOOLEAN_FALSE;
		}
	}

	@Override
	public Boolean convertValue(String value) {

		if (value.equalsIgnoreCase("true") || value.equals("1")) {
			return new Boolean(true);
		} else if (value.equalsIgnoreCase("false") || value.equals("0")){
			return new Boolean(false);
		} else {
			// TODO: what to do?
			return new Boolean(false);
		}
		
	}

	@Override
	public ParameterT getParameter() {
		// TODO Auto-generated method stub
		return parameter;
	}

}
