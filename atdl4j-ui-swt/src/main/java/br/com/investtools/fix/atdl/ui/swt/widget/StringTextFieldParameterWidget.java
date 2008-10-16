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

import br.com.investtools.fix.atdl.core.xmlbeans.MultipleCharValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleStringValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class StringTextFieldParameterWidget extends
		AbstractParameterWidget<String> {

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

		if (parameter instanceof MultipleCharValueT) {
			MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
			if (multipleCharValueT.isSetInitValue())
				textField.setText(multipleCharValueT.getInitValue());
		} else if (parameter instanceof MultipleStringValueT) {
			MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
			if (multipleStringValueT.isSetInitValue())
				textField.setText(multipleStringValueT.getInitValue());
		}

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
		String value = textField.getText();

		if ("".equals(value)) {
			return null;
		} else {
			return value;
		}
	}

	@Override
	public String getValueAsString() {
		if (parameter instanceof MultipleCharValueT) {
			MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
			if (multipleCharValueT.isSetInvertOnWire())
				return WidgetHelper.invertOnWire(getValue());
		} else if (parameter instanceof MultipleStringValueT) {
			MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
			if (multipleStringValueT.isSetInvertOnWire())
				return WidgetHelper.invertOnWire(getValue());
		}
		return getValue();
	}

	@Override
	public void setValue(String value) {
		textField.setText(value);
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
