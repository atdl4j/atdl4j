package br.com.investtools.fix.atdl.ui.swt.widget;

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

public class DualSpinnerParameterWidget implements ParameterWidget {

	private ParameterT parameter;

	private Spinner dualSpinner;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		c.setLayout(gridLayout);

		// label
		Label l = new Label(c, SWT.NONE);
		l.setText(getLabelText(parameter));
		
		GridData labelData = new GridData();
		labelData.verticalSpan = 2;
		l.setLayoutData(labelData);


		// dualSpinner
		Spinner dualSpinner = new Spinner(c, style | SWT.BORDER);
		
		GridData spinnerData = new GridData();
		spinnerData.verticalSpan = 2;
		dualSpinner.setLayoutData(spinnerData);
		
		int outerStepSize = 0;
		
		EnumPairT[] enumPairArray = parameter.getEnumPairArray();
		for (EnumPairT enumPair : enumPairArray) {
			if (enumPair.getUiRep().equals("InnerStepSize") ){
				dualSpinner.setIncrement(new Integer(enumPair.getWireValue()));
			} else if (enumPair.getUiRep().equals("OuterStepSize") ){
				outerStepSize = new Integer(enumPair.getWireValue());
			}
		}
		
		this.dualSpinner = dualSpinner;
		
		Button buttonUp = new Button(c, SWT.ARROW | SWT.UP);
		Button buttonDown = new Button(c, SWT.ARROW | SWT.DOWN);

		buttonUp.addSelectionListener(new DualSpinnerSelection(dualSpinner, outerStepSize));
		buttonDown.addSelectionListener(new DualSpinnerSelection(dualSpinner, outerStepSize));



		// tooltip
		String tooltip = parameter.getTooltip();
		dualSpinner.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return c;
	}

	public String getLabelText(ParameterT parameter) {
		if (parameter.getUiRep() != null) {
			return parameter.getUiRep();
		}
		return parameter.getName();
	}

	private String getValue() {
		// TODO: nao eh exatamente isso, pois tem casas decimais
		return Integer.toString(dualSpinner.getSelection());
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

}
