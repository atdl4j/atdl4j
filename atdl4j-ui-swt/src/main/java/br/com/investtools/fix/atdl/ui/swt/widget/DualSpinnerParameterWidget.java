package br.com.investtools.fix.atdl.ui.swt.widget;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.EnumPairT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class DualSpinnerParameterWidget implements ParameterWidget<BigDecimal> {

	private ParameterT parameter;

	private Spinner dualSpinner;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));

		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		c.setLayout(gridLayout);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// dualSpinner
		Spinner dualSpinner = new Spinner(c, style | SWT.BORDER);
		GridData spinnerData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		spinnerData.verticalSpan = 2;
		dualSpinner.setLayoutData(spinnerData);

		int outerStepSize = 0;

		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			if (enumPair.getUiRep().equals("InnerStepSize")) {
				dualSpinner.setIncrement(new Integer(enumPair.getWireValue()));
			} else if (enumPair.getUiRep().equals("OuterStepSize")) {
				outerStepSize = new Integer(enumPair.getWireValue());
			}
		}

		this.dualSpinner = dualSpinner;

		Button buttonUp = new Button(c, SWT.ARROW | SWT.UP);
		Button buttonDown = new Button(c, SWT.ARROW | SWT.DOWN);

		buttonUp.addSelectionListener(new DualSpinnerSelection(dualSpinner,
				outerStepSize));
		buttonDown.addSelectionListener(new DualSpinnerSelection(dualSpinner,
				outerStepSize));

		// tooltip
		String tooltip = parameter.getTooltip();
		dualSpinner.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public BigDecimal getValue() {
		return new BigDecimal(dualSpinner.getSelection());
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue().toString();
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

}
