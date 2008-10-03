package br.com.investtools.fix.atdl.ui.swt.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

import br.com.investtools.fix.atdl.core.xmlbeans.FloatT;
import br.com.investtools.fix.atdl.core.xmlbeans.IntT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.NumberFormatVerifyListener;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class NumberTextFieldParameterWidget implements ParameterUI<BigDecimal> {

	private ParameterT parameter;

	private Text textField;

	private Label label;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style)
			throws XmlException {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		this.label = l;
		l.setText(WidgetHelper.getLabelText(parameter));

		// textField
		Text textField = new Text(parent, style | SWT.BORDER);
		this.textField = textField;
		textField
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		switch (parameter.getType()) {

		// type Int_T
		case 1:
			if (parameter instanceof IntT) {
				IntT intt = (IntT) parameter;

				textField.addVerifyListener(new NumberFormatVerifyListener(
						new DecimalFormat("#"), false));

				if (intt.isSetInitValue())
					textField.setText(Integer.toString(intt.getInitValue()));
			} else {
				throw new XmlException(
						"Parameter \"type\" set as int but \"xsi:type\" not.");
			}
			break;

		// type Float_T
		case 6:
			if (parameter instanceof FloatT) {
				FloatT floatt = (FloatT) parameter;

				textField.addVerifyListener(new NumberFormatVerifyListener(
						new DecimalFormat("0.0"), false));

				if (floatt.isSetInitValue())
					textField.setText(Float.toString(floatt.getInitValue()));

			} else {
				throw new XmlException(
						"Parameter \"type\" set as float but \"xsi:type\" not.");
			}

			break;

		default:
			break;
		}

		// tooltip
		String tooltip = parameter.getTooltip();
		textField.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	@Override
	public BigDecimal getValue() {
		try {
			return new BigDecimal(textField.getText());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public void setValue(BigDecimal value) {
		textField.setText(value.toString());
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue();
		} else {
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			String value = getValue().toString();
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public BigDecimal convertValue(String value) {
		return new BigDecimal(value);
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
		textField.addListener(SWT.Modify, new ParameterListenerWrapper(this,
				listener));
	}

	@Override
	public void removeListener(Listener listener) {
		textField.removeListener(SWT.Selection, listener);
	}

}
