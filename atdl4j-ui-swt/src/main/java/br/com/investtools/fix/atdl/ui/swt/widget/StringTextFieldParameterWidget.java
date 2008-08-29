package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.FloatT;
import br.com.investtools.fix.atdl.core.xmlbeans.IntT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class StringTextFieldParameterWidget implements ParameterWidget<String> {

	private ParameterT parameter;

	private Text textField;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());

		// label
		Label l = new Label(c, SWT.NONE);
		l.setText(getLabelText(parameter));

		// textField
		Text textField = new Text(c, style | SWT.BORDER);
		
		switch (parameter.getType()) {
		
			// type Int_T
			case 1:
				if (parameter instanceof IntT) {
					IntT intt = (IntT) parameter;
					Integer minValue = null;
					Integer maxValue = null;
					
					if (intt.isSetMinValue())
						minValue = intt.getMinValue();
					
					if (intt.isSetMaxValue())
						maxValue = intt.getMaxValue();
					
					textField.addVerifyListener(new IntTTypeListener(new DecimalFormat("#"), false, minValue, maxValue));
					
					if (intt.isSetInitValue())
						textField.setText(Integer.toString(intt.getInitValue()));
				} else {
					// XXX not an IntT type
				}
			break;

			// type Float_T
			case 2:
				if (parameter instanceof FloatT) {
					FloatT floatt = (FloatT) parameter;
					Float minValue = null;
					Float maxValue = null;

					if (floatt.isSetMinValue())
						minValue = floatt.getMinValue();
					
					if (floatt.isSetMaxValue())
						maxValue = floatt.getMaxValue();

					textField.addVerifyListener(new FloatTTypeListener(new DecimalFormat("0.0"), false, minValue, maxValue));
					
					if (floatt.isSetInitValue())
						textField.setText(Float.toString(floatt.getInitValue()));

					
				} else {
					// XXX not an FloatT type
				}

				
			break;
	
			default:
				break;
		}
		

		
		
		this.textField = textField;

		// tooltip
		String tooltip = parameter.getTooltip();
		textField.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return c;
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
		// TODO Auto-generated method stub
		return null;
	}

}
