package br.com.investtools.fix.atdl.ui.swt.widget;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.AmtT;
import br.com.investtools.fix.atdl.core.xmlbeans.EnumPairT;
import br.com.investtools.fix.atdl.core.xmlbeans.FloatT;
import br.com.investtools.fix.atdl.core.xmlbeans.IntT;
import br.com.investtools.fix.atdl.core.xmlbeans.NumericT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.PercentageT;
import br.com.investtools.fix.atdl.core.xmlbeans.PriceOffsetT;
import br.com.investtools.fix.atdl.core.xmlbeans.PriceT;
import br.com.investtools.fix.atdl.core.xmlbeans.QtyT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class DualSpinnerParameterWidget implements ParameterUI<BigDecimal> {

	private ParameterT parameter;

	private Spinner dualSpinner;

	private Label label;

	private Button buttonUp;

	private Button buttonDown;

	/**
	 * SelectionListener that implements the dual spinner behaviour.
	 * 
	 */
	public class DualSpinnerSelection implements SelectionListener {

		private Spinner spinner;

		private int increment;

		public DualSpinnerSelection(Spinner spinner, int increment) {
			this.spinner = spinner;
			this.increment = increment;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}

		@Override
		public void widgetSelected(SelectionEvent event) {
			spinner.setSelection(spinner.getSelection() + increment);
		}

	}

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		c.setLayout(gridLayout);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// dualSpinner
		Spinner dualSpinner = new Spinner(c, style | SWT.BORDER);
		this.dualSpinner = dualSpinner;
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

		Button buttonUp = new Button(c, SWT.ARROW | SWT.UP);
		this.buttonUp = buttonUp;
		Button buttonDown = new Button(c, SWT.ARROW | SWT.DOWN);
		this.buttonDown = buttonDown;

		buttonUp.addSelectionListener(new DualSpinnerSelection(dualSpinner,
				outerStepSize));
		buttonDown.addSelectionListener(new DualSpinnerSelection(dualSpinner,
				outerStepSize));

		// tooltip
		String tooltip = parameter.getTooltip();
		dualSpinner.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		dualSpinner.setDigits(getPrecision(parameter));

		Integer minimum = getMinimum(parameter);
		if (minimum != null) {
			dualSpinner.setMinimum(minimum);
		} else {
			dualSpinner.setMinimum(-Integer.MAX_VALUE);
		}

		Integer maximum = getMaximum(parameter);
		if (maximum != null) {
			dualSpinner.setMaximum(maximum);
		} else {
			dualSpinner.setMaximum(Integer.MAX_VALUE);
		}

		Integer initValue = getInitValue(parameter);
		if (initValue != null)
			dualSpinner.setSelection(initValue);

		return parent;
	}

	private Integer getPrecision(ParameterT parameter) {

		BigInteger precision;

		if (parameter instanceof NumericT) {
			NumericT numeric = (NumericT) parameter;
			// default precision is 2 decimal places in case of FloatT
			precision = BigInteger.valueOf(2);

			// override precision if defined by user
			if (numeric.isSetPrecision()) {
				precision = numeric.getPrecision();
			}
		} else {
			precision = BigInteger.ZERO;
		}

		return precision.intValue();

	}

	private Integer getMinimum(ParameterT parameter) {

		if (parameter instanceof FloatT) {
			// get value defined by user
			FloatT floatT = (FloatT) parameter;

			if (floatT.isSetMinValue()) {
				float minValue = floatT.getMinValue();

				// adjust according to precision
				int minValueAdjusted = (int) (minValue * Math.pow(10,
						dualSpinner.getDigits()));
				return minValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;

			if (amtT.isSetMinValue()) {
				BigDecimal minValue = amtT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return minValue.intValue();
			}

		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;

			if (percentageT.isSetMinValue()) {
				BigDecimal minValue = percentageT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return minValue.intValue();
			}

		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;

			if (priceOffsetT.isSetMinValue()) {
				BigDecimal minValue = priceOffsetT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return minValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.isSetMinValue()) {
				BigDecimal minValue = priceT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return minValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.isSetMinValue()) {
				BigDecimal minValue = priceT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return minValue.intValue();
			}

		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;

			if (qtyT.isSetMinValue()) {
				BigDecimal minValue = qtyT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return minValue.intValue();
			}

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;

			if (intT.isSetMinValue()) {
				return intT.getMinValue();
			}

		}

		return null;
	}

	private Integer getMaximum(ParameterT parameter) {

		if (parameter instanceof FloatT) {
			// get value defined by user
			FloatT floatT = (FloatT) parameter;

			if (floatT.isSetMaxValue()) {
				float maxValue = floatT.getMaxValue();

				// adjust according to precision
				int maxValueAdjusted = (int) (maxValue * Math.pow(10,
						dualSpinner.getDigits()));
				return maxValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;

			if (amtT.isSetMaxValue()) {
				BigDecimal maxValue = amtT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;

			if (percentageT.isSetMaxValue()) {
				BigDecimal maxValue = percentageT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;

			if (priceOffsetT.isSetMaxValue()) {
				BigDecimal maxValue = priceOffsetT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.isSetMaxValue()) {
				BigDecimal maxValue = priceT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.isSetMaxValue()) {
				BigDecimal maxValue = priceT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return maxValue.intValue();
			}

		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;

			if (qtyT.isSetMaxValue()) {
				BigDecimal maxValue = qtyT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(dualSpinner.getDigits());
				return maxValue.intValue();
			}

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;

			if (intT.isSetMaxValue()) {
				return intT.getMaxValue();
			}

		}

		return null;
	}

	private Integer getInitValue(ParameterT parameter) {

		if (parameter instanceof FloatT) {
			// get value defined by user
			FloatT floatT = (FloatT) parameter;

			if (floatT.isSetInitValue()) {
				float initValue = floatT.getInitValue();

				// adjust according to precision
				int initValueAdjusted = (int) (initValue * Math.pow(10,
						dualSpinner.getDigits()));
				return initValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;

			if (amtT.isSetInitValue()) {
				BigDecimal initValue = amtT.getInitValue();
				initValue = initValue
						.scaleByPowerOfTen(dualSpinner.getDigits());
				return initValue.intValue();
			}

		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;

			if (percentageT.isSetInitValue()) {
				BigDecimal initValue = percentageT.getInitValue();
				initValue = initValue
						.scaleByPowerOfTen(dualSpinner.getDigits());
				return initValue.intValue();
			}

		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;

			if (priceOffsetT.isSetInitValue()) {
				BigDecimal initValue = priceOffsetT.getInitValue();
				initValue = initValue
						.scaleByPowerOfTen(dualSpinner.getDigits());
				return initValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.isSetInitValue()) {
				BigDecimal initValue = priceT.getInitValue();
				initValue = initValue
						.scaleByPowerOfTen(dualSpinner.getDigits());
				return initValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.isSetInitValue()) {
				BigDecimal initValue = priceT.getInitValue();
				initValue = initValue
						.scaleByPowerOfTen(dualSpinner.getDigits());
				return initValue.intValue();
			}

		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;

			if (qtyT.isSetInitValue()) {
				BigDecimal initValue = qtyT.getInitValue();
				initValue = initValue
						.scaleByPowerOfTen(dualSpinner.getDigits());
				return initValue.intValue();
			}

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;

			if (intT.isSetInitValue()) {
				return intT.getInitValue();
			}
		}

		return null;
	}

	public BigDecimal getValue() {
		return new BigDecimal(BigInteger.valueOf(dualSpinner.getSelection()),
				dualSpinner.getDigits());
	}

	@Override
	public void setValue(BigDecimal value) {
		dualSpinner.setSelection(value.unscaledValue().intValue());
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

	@Override
	public void generateStateRuleListener(Listener listener) {
		dualSpinner.addListener(SWT.Modify, listener);
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(dualSpinner);
		widgets.add(buttonUp);
		widgets.add(buttonDown);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		dualSpinner.addListener(SWT.Selection, new ParameterListenerWrapper(
				this, listener));
	}

	@Override
	public void removeListener(Listener listener) {
		dualSpinner.removeListener(SWT.Selection, listener);
	}

}
