package br.com.investtools.fix.atdl.ui.swt.widget;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.AmtT;
import br.com.investtools.fix.atdl.core.xmlbeans.FloatT;
import br.com.investtools.fix.atdl.core.xmlbeans.IntT;
import br.com.investtools.fix.atdl.core.xmlbeans.NumericT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.PercentageT;
import br.com.investtools.fix.atdl.core.xmlbeans.PriceOffsetT;
import br.com.investtools.fix.atdl.core.xmlbeans.PriceT;
import br.com.investtools.fix.atdl.core.xmlbeans.QtyT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

/**
 * Widget for a spinner based parameters. The value is always represented by a
 * BigDecimal.
 * 
 */
public class SingleSpinnerParameterWidget implements
		ParameterWidget<BigDecimal> {

	private ParameterT parameter;

	private Spinner spinner;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));

		// spinner
		Spinner spinner = new Spinner(parent, style | SWT.BORDER);
		this.spinner = spinner;
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		// tooltips
		String tooltip = parameter.getTooltip();
		spinner.setToolTipText(tooltip);
		l.setToolTipText(tooltip);
		
		spinner.setDigits(getPrecision(parameter));
		
		Integer minimum = getMinimum(parameter);
		if (minimum != null)
			spinner.setMinimum(minimum);
		
		Integer maximum = getMaximum(parameter);
		if (maximum != null)
			spinner.setMaximum(maximum);
		
		Integer initValue = getInitValue(parameter);
		if (initValue != null)
			spinner.setSelection(initValue);

		return parent;
	}

	private Integer getPrecision(ParameterT parameter){
		
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
				int minValueAdjusted = (int) (minValue * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;
			
			if (amtT.isSetMinValue()) {
				BigDecimal minValue = amtT.getMinValue();
				
				int minValueAdjusted = (int) (minValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
			
		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;
			
			if (percentageT.isSetMinValue()) {
				BigDecimal minValue = percentageT.getMinValue();
				
				int minValueAdjusted = (int) (minValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
			
		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;
			
			if (priceOffsetT.isSetMinValue()) {
				BigDecimal minValue = priceOffsetT.getMinValue();
				
				int minValueAdjusted = (int) (minValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
			
		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;
			
			if (priceT.isSetMinValue()) {
				BigDecimal minValue = priceT.getMinValue();
				
				int minValueAdjusted = (int) (minValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
			
		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;
			
			if (priceT.isSetMinValue()) {
				BigDecimal minValue = priceT.getMinValue();
				
				int minValueAdjusted = (int) (minValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
			
		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;
			
			if (qtyT.isSetMinValue()) {
				BigDecimal minValue = qtyT.getMinValue();
				
				int minValueAdjusted = (int) (minValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return minValueAdjusted;
			}
		

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;
			return intT.getMinValue();

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
				int maxValueAdjusted = (int) (maxValue * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;
			
			if (amtT.isSetMaxValue()) {
				BigDecimal maxValue = amtT.getMaxValue();
				
				int maxValueAdjusted = (int) (maxValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
			
		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;
			
			if (percentageT.isSetMaxValue()) {
				BigDecimal maxValue = percentageT.getMaxValue();
				
				int maxValueAdjusted = (int) (maxValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
			
		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;
			
			if (priceOffsetT.isSetMaxValue()) {
				BigDecimal maxValue = priceOffsetT.getMaxValue();
				
				int maxValueAdjusted = (int) (maxValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
			
		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;
			
			if (priceT.isSetMaxValue()) {
				BigDecimal maxValue = priceT.getMaxValue();
				
				int maxValueAdjusted = (int) (maxValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
			
		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;
			
			if (priceT.isSetMaxValue()) {
				BigDecimal maxValue = priceT.getMaxValue();
				
				int maxValueAdjusted = (int) (maxValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
			
		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;
			
			if (qtyT.isSetMaxValue()) {
				BigDecimal maxValue = qtyT.getMaxValue();
				
				int maxValueAdjusted = (int) (maxValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return maxValueAdjusted;
			}
		

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;
			return intT.getMaxValue();

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
				int initValueAdjusted = (int) (initValue * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;
			
			if (amtT.isSetInitValue()) {
				BigDecimal initValue = amtT.getInitValue();
				
				int initValueAdjusted = (int) (initValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
			
		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;
			
			if (percentageT.isSetInitValue()) {
				BigDecimal initValue = percentageT.getInitValue();
				
				int initValueAdjusted = (int) (initValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
			
		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;
			
			if (priceOffsetT.isSetInitValue()) {
				BigDecimal initValue = priceOffsetT.getInitValue();
				
				int initValueAdjusted = (int) (initValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
			
		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;
			
			if (priceT.isSetInitValue()) {
				BigDecimal initValue = priceT.getInitValue();
				
				int initValueAdjusted = (int) (initValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
			
		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;
			
			if (priceT.isSetInitValue()) {
				BigDecimal initValue = priceT.getInitValue();
				
				int initValueAdjusted = (int) (initValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
			
		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;
			
			if (qtyT.isSetInitValue()) {
				BigDecimal initValue = qtyT.getInitValue();
				
				int initValueAdjusted = (int) (initValue.floatValue() * Math.pow(10, spinner.getDigits()
						));
				return initValueAdjusted;
			}
		

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;
			return intT.getInitValue();

		} 
		
		return null;
	}
	
	public BigDecimal getValue() {
		return new BigDecimal(spinner.getSelection());
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
