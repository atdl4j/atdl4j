package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;

public class StringTextFieldParameterWidget implements ParameterUI<String> {

	private ParameterT parameter;

	private Text textField;

	private Label label;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style)
			throws XmlException {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(getLabelText(parameter));
		this.label = l;

		// textField
		Text textField = new Text(parent, style | SWT.BORDER);
		textField
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		this.textField = textField;

		// tooltip
		String tooltip = parameter.getTooltip();
		textField.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public String getLabelText(ParameterT parameter) {
		if (parameter.getUiRep() != null) {
			return parameter.getUiRep();
		}
		return parameter.getName();
	}

	@Override
	public String getValue() {
		return textField.getText();
	}

	@Override
	public void setValue(String value) {
		textField.setText(value);
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
		textField.addListener(SWT.Modify, listener);
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(textField);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		textField.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	@Override
	public void removeListener(Listener listener) {
		textField.removeListener(SWT.Selection, listener);
	}

}
