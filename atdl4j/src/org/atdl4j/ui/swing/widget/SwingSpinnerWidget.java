package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.plaf.basic.BasicArrowButton;

import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.widget.SwingNullableSpinner.SpinnerNumberModelNull;

public class SwingSpinnerWidget extends AbstractSwingWidget<BigDecimal> {
	private SwingNullableSpinner spinner;
	private JLabel label;
	private JButton buttonUp;
	private JButton buttonDown;
	private static final BigDecimal DEFAULT_INNER_INCREMENT = new BigDecimal(1);
	private static final BigDecimal DEFAULT_OUTER_INCREMENT = new BigDecimal(1);
	private int digits = 0;
	private JPanel wrapper;

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
	
	
	public void setVisible(boolean visible){
		if (wrapper != null)
			wrapper.setVisible(visible);
		else 
			super.setVisible(visible);
	}
	
	
	public void createWidget(JPanel parent) {
		String tooltip = getTooltip();

		
		// label
		if (control.getLabel() != null) {
			label = new JLabel();
			label.setText(control.getLabel());
			if (tooltip != null) label.setToolTipText(tooltip);
		}
		
		Component comp = null;
		
		if (control instanceof SingleSpinnerT)
		{
			// spinner
			spinner = new SwingNullableSpinner();			
			
			// tooltip
			if (tooltip != null) spinner.setToolTipText(tooltip);
			
			comp = spinner;
		}
		else if (control instanceof DoubleSpinnerT)
		{
			// doubleSpinnerGrid
			JPanel w = new JPanel();
	
			// doubleSpinner
			spinner = new SwingNullableSpinner();	
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
			w.add(spinner);
			w.add(buttonUp);
			w.add(buttonDown);

			comp = w;
		}

		if (comp != null) {
			if (label != null){
				wrapper = new JPanel(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;   
				c.weightx = 1.0;
				c.weighty = 1.0;
				c.insets = new Insets(0, 0, 0, 0);
				wrapper.add( label, c);
				c.gridx = 1;
				c.gridy = 0;
				c.insets = new Insets(0, 0, 0, 0);
				wrapper.add( comp, c);
				parent.add(wrapper);
			}
			else {
				parent.add(comp);
			}
		}
		
		// number model
		SpinnerNumberModelNull model = (SpinnerNumberModelNull)spinner.getModel();
		
	// Set min/max/precision if a parameter is attached
		if ( parameterConverter != null && parameterConverter instanceof DecimalConverter )
		{
			DecimalConverter tempDecimalConverter = (DecimalConverter) parameterConverter;
			
			if ( tempDecimalConverter.getPrecision() != null )
			{
				digits = tempDecimalConverter.getPrecision().intValue();
			}
			else
			{
				digits = ControlHelper.getDefaultDigitsForSpinnerControl( parameterConverter.getParameter(), getAtdl4jOptions());
			}

			if ( tempDecimalConverter.getMinValue() != null )
			{
				// -- need to handle Percentage ("control value" representation) --
				BigDecimal tempParameterMin = tempDecimalConverter.getMinValue();
				BigDecimal tempControlMin = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMin );
				model.setMinimum( tempControlMin.setScale( digits, RoundingMode.HALF_UP )  );
			}
			
			if ( tempDecimalConverter.getMaxValue() != null )
			{
				// -- need to handle Percentage ("control value" representation) --
				BigDecimal tempParameterMax = tempDecimalConverter.getMaxValue();
				BigDecimal tempControlMax = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMax );
				model.setMaximum( tempControlMax.setScale( digits, RoundingMode.HALF_UP )  );
			}
		}
		else if ( parameterConverter != null && parameterConverter instanceof IntegerConverter )
		{
			IntegerConverter tempIntegerConverter = (IntegerConverter) parameterConverter;
			
			// -- Integer always has 0 digits --
			digits = 0;

			if ( tempIntegerConverter.getMinValue() != null )
			{
				BigInteger tempParameterMin = tempIntegerConverter.getMinValue();
				BigInteger tempControlMin = tempIntegerConverter.convertParameterValueToControlValue( tempParameterMin );
				model.setMinimum( new BigDecimal( tempControlMin ) );
			}
			else
			{
				model.setMinimum( SwingNullableSpinner.MIN_INTEGER_VALUE_AS_BIG_DECIMAL );
			}
			
			if ( tempIntegerConverter.getMaxValue() != null )
			{
				BigInteger tempParameterMax = tempIntegerConverter.getMaxValue();
				BigInteger tempControlMax = tempIntegerConverter.convertParameterValueToControlValue( tempParameterMax );
				model.setMaximum( new BigDecimal( tempControlMax ) );
			}
			else
			{
				model.setMaximum( SwingNullableSpinner.MAX_INTEGER_VALUE_AS_BIG_DECIMAL );
			}
		}

		if ( control instanceof DoubleSpinnerT )
		{
			BigDecimal tempInnerIncrement = ControlHelper.getInnerIncrementValue( control, getAtdl4jOptions(), digits );
			if ( tempInnerIncrement != null )
			{
				// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
				model.setStepSize( tempInnerIncrement );
			}
			
			BigDecimal outerStepSize = new BigDecimal( "1" );
			
			BigDecimal tempOuterIncrement = ControlHelper.getOuterIncrementValue( control, getAtdl4jOptions() );
			if ( tempOuterIncrement != null )
			{
				outerStepSize = tempOuterIncrement;
			}

//			buttonUp.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize ) );
//			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize.negate() ) );
		}
		else if ( control instanceof SingleSpinnerT )
		{
			BigDecimal tempIncrement = ControlHelper.getIncrementValue( control, getAtdl4jOptions(), digits );
			if ( tempIncrement != null )
			{
				// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
				model.setStepSize( tempIncrement );
			}
			else  // tempIncrement is null
			{
				if ( digits != 0 )
				{
					// -- Set the increment to the precision associated with digits (eg ".01" when digits=2, ".001" when digits=3) --
					model.setStepSize( new BigDecimal( Math.pow( 10, -digits ) ).setScale( digits, RoundingMode.HALF_UP ) );
				}
				else
				{
					model.setStepSize( new BigDecimal( "1" ) );
				}
			}
				
		}

		spinner.setPreferredSize(new Dimension(100, 22));
		spinner.revalidate();
		
		applyInitialValue();		
	}

	public BigDecimal getControlValueRaw() {
		BigDecimal val = (BigDecimal)spinner.getValue();
		if (val != null) {
			val = val.setScale(digits, RoundingMode.HALF_UP );
		}
		return val;
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