package org.atdl4j.ui.swt.widget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.IntT;
import org.atdl4j.atdl.core.LengthT;
import org.atdl4j.atdl.core.NumInGroupT;
import org.atdl4j.atdl.core.NumericT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.SeqNumT;
import org.atdl4j.atdl.core.TagNumT;
import org.atdl4j.atdl.layout.TextFieldT;
import org.atdl4j.ui.swt.util.NumberFormatVerifyListener;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class TextFieldWidget extends AbstractSWTWidget<String> {

	private Text textField;
	private Label label;

	public TextFieldWidget(TextFieldT control, ParameterT parameter)
			throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style)
			throws JAXBException {

		// label
		Label l = new Label(parent, SWT.NONE);
		this.label = l;
		l.setText(control.getLabel());

		// textField
		Text textField = new Text(parent, style | SWT.BORDER);
		this.textField = textField;
		textField
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// type validation
		if (parameter instanceof IntT || parameter instanceof TagNumT
				|| parameter instanceof LengthT || parameter instanceof SeqNumT
				|| parameter instanceof NumInGroupT) {
			// Integer types
			textField.addVerifyListener(new NumberFormatVerifyListener(
					new DecimalFormat("#"), false));
		} else if (parameter instanceof NumericT) {
			// Decimal types
			textField.addVerifyListener(new NumberFormatVerifyListener(
					new DecimalFormat("0.0"), false));
		}
		// TODO: add regex verifier for MultipleCharValueT and
		// MultipleStringValueT

		// init value
		if (((TextFieldT) control).getInitValue() != null)
			textField.setText(((TextFieldT) control).getInitValue());

		// tooltip
		String tooltip = control.getTooltip();
		textField.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public String getControlValue() {
		String value = textField.getText();

		if ("".equals(value)) {
			return null;
		} else {
			return value;
		}
	}

	public String getParameterValue() {
		return getControlValue();
	}

	public void setValue(String value) {
		textField.setText((value == null) ? "" : value.toString());
	}

	public void generateStateRuleListener(Listener listener) {
		textField.addListener(SWT.Modify, listener);
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(textField);
		return widgets;
	}

	public void addListener(Listener listener) {
		textField.addListener(SWT.Modify, new ParameterListenerWrapper(this,
				listener)); // TODO: SWT.Selection??
	}

	public void removeListener(Listener listener) {
		textField.removeListener(SWT.Selection, listener); // TODO: SWT.Modify??
	}
}
