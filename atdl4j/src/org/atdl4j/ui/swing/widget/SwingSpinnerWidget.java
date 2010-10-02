package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicArrowButton;

import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

public class SwingSpinnerWidget extends AbstractSwingWidget<BigDecimal> {
	private JSpinner spinner;
	private JLabel label;
	private JButton buttonUp;
	private JButton buttonDown;
	private static final BigDecimal DEFAULT_INNER_INCREMENT = new BigDecimal(1);
	private static final BigDecimal DEFAULT_OUTER_INCREMENT = new BigDecimal(1);
	
	/**
	 * SelectionListener that implements the dual spinner behavior.
	 * 
	 */
	public class DoubleSpinnerListener implements ActionListener {

		private JSpinner spinner;
		private BigDecimal increment;

		public DoubleSpinnerListener(JSpinner spinner, BigDecimal increment) {
			this.spinner = spinner;
			this.increment = increment;
		}
	
		public void actionPerformed(ActionEvent arg0) {
			spinner.setValue(new BigDecimal((Integer)spinner.getValue()).add(increment));
		}
	}
	
	public void createWidget(Container parent) {
		String tooltip = getTooltip();

		// label
		if (control.getLabel() != null) {
			label = new JLabel();
			label.setText(control.getLabel());
			if (tooltip != null) label.setToolTipText(tooltip);
			parent.add(label);
		}
		
		if (control instanceof SingleSpinnerT)
		{
			// spinner
			spinner = new JSpinner();			
			
			// tooltip
			if (tooltip != null) spinner.setToolTipText(tooltip);
			
			// layout
			parent.add(spinner);
		}
		else if (control instanceof DoubleSpinnerT)
		{
			// doubleSpinnerGrid
			JPanel wrapper = new JPanel();
	
			// doubleSpinner
			spinner = new JSpinner();	
			buttonUp = new BasicArrowButton(BasicArrowButton.NORTH);
			buttonDown = new BasicArrowButton(BasicArrowButton.SOUTH);
			
			// tooltip
			if (tooltip != null)
			{
				spinner.setToolTipText(tooltip);
				buttonUp.setToolTipText(tooltip);
				buttonDown.setToolTipText(tooltip);
			}
			
			// layout
			wrapper.add(spinner);
			wrapper.add(buttonUp);
			wrapper.add(buttonDown);
			parent.add(wrapper);
		}

		// number model
		SpinnerNumberModel model = new SpinnerNumberModel();
		
		// set min/max value		
		if (parameter != null && parameterConverter != null) {
			if (parameterConverter instanceof IntegerConverter) {
				IntegerConverter pc = (IntegerConverter) parameterConverter;
				if (pc.getMinValue() != null)
					model.setMinimum(pc.getMinValue());
				if (pc.getMaxValue() != null)
					model.setMaximum(pc.getMaxValue());
			} else if (parameterConverter instanceof DecimalConverter) {
				DecimalConverter pc = (DecimalConverter) parameterConverter;
				if (pc.getMinValue() != null)
					model.setMinimum(pc.getMinValue());
				if (pc.getMaxValue() != null)
					model.setMaximum(pc.getMaxValue());
			}
		}

		// set step sizes
		BigDecimal tempInnerIncrement;		
		if (control instanceof SingleSpinnerT)
		{
			tempInnerIncrement = ControlHelper.getIncrementValue(control, getAtdl4jOptions());
			model.setStepSize( tempInnerIncrement != null ? tempInnerIncrement : DEFAULT_INNER_INCREMENT);
		}
		else if (control instanceof DoubleSpinnerT)
		{
			tempInnerIncrement = ControlHelper.getInnerIncrementValue(control, getAtdl4jOptions());
			model.setStepSize( tempInnerIncrement != null ? tempInnerIncrement : DEFAULT_INNER_INCREMENT);
			
			BigDecimal tempOuterIncrement = ControlHelper.getOuterIncrementValue(control, getAtdl4jOptions());			
			buttonUp.addActionListener(new DoubleSpinnerListener(spinner,
					(tempOuterIncrement != null) ? tempOuterIncrement : DEFAULT_OUTER_INCREMENT));
			buttonDown.addActionListener(new DoubleSpinnerListener(spinner,
					(tempOuterIncrement != null) ? tempOuterIncrement.negate() : DEFAULT_OUTER_INCREMENT.negate()));
		}  

		applyInitialValue();		
	}

	public BigDecimal getControlValueRaw() {
		try {
			return BigDecimal.valueOf((Integer)spinner.getValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void setValue(BigDecimal value) {
		spinner.setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit(Object aControlInitValue) {
		if (spinner != null) {
			applyInitialValue();
		}
		// TODO: all reinit methods should apply initial value???
		// TODO: make applyInitialValue a method for all widgets;
	}

	/**
	 * Invokes spinner.setSelection() assigning - Control/@initValue if non-null
	 * - Parameter/@minValue if non-null - otherwise 0
	 */
	protected void applyInitialValue() {
		Double initValue = (Double) ControlHelper.getInitValue(control, getAtdl4jOptions());
		if (initValue != null) {
			setValue(new BigDecimal(initValue));
		}
	}

	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd,
			Boolean aNewNullValueInd) {
		// TODO ?? adjust the visual appearance of the control ??
	}

	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.add(label);
		widgets.add(spinner);
		if (control instanceof DoubleSpinnerT)
		{
			widgets.add(buttonUp);
			widgets.add(buttonDown);
		}
		return widgets;
	}
	
	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.add(spinner);
		if (control instanceof DoubleSpinnerT)
		{
			widgets.add(buttonUp);
			widgets.add(buttonDown);
		}
		return widgets;
	}
	
	public void addListener(SwingListener listener) {
		spinner.addChangeListener(listener);
		if (control instanceof DoubleSpinnerT)
		{
			buttonUp.addActionListener(listener);
			buttonDown.addActionListener(listener);
		}
	}
	
	public void removeListener(SwingListener listener) {
		spinner.removeChangeListener(listener);
		if (control instanceof DoubleSpinnerT)
		{
			buttonUp.removeActionListener(listener);
			buttonDown.removeActionListener(listener);
		}
	}	
}