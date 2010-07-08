package org.atdl4j.ui.swt.widget;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.swt.util.NullableSpinner;
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
import org.eclipse.swt.widgets.Widget;

public class SpinnerWidget
		extends AbstractSWTWidget<BigDecimal>
{
	private NullableSpinner spinner;
	private Label label;
	private Button buttonUp;
	private Button buttonDown;

	/**
	 * SelectionListener that implements the dual spinner behavior.
	 * 
	 */
	public class DoubleSpinnerSelection
			implements SelectionListener
	{
		private NullableSpinner spinner;

// 7/7/2010 Scott Atwell use BigDecimal		private int increment;
		private BigDecimal increment;

// 7/7/2010 Scott Atwell use BigDecimal		public DoubleSpinnerSelection(NullableSpinner spinner2, int increment)
		public DoubleSpinnerSelection(NullableSpinner spinner2, BigDecimal increment)
		{
			this.spinner = spinner2;
			this.increment = increment;
		}

		public void widgetDefaultSelected(SelectionEvent event)
		{
		}

		public void widgetSelected(SelectionEvent event)
		{
/*** 7/7/2010 Scott Atwell moved logic to NullableSpinner			
			if ( spinner.getSelection() != null )
			{
				spinner.setSelection( spinner.getSelection() + increment );
			}
			else
			{
				spinner.setSelection( increment );
			}
***/
			spinner.increment( increment );
		}
	}

	public Widget createWidget(Composite parent, int style)
	{
		String tooltip = getTooltip();
		GridData controlGD = new GridData( SWT.FILL, SWT.FILL, false, false );
		
		// label
		if ( control.getLabel() != null ) {
			label = new Label( parent, SWT.NONE );
			label.setText( control.getLabel() );
			if ( tooltip != null ) label.setToolTipText( tooltip );
			controlGD.horizontalSpan = 1;
		} else {
			controlGD.horizontalSpan = 2;
		}

		if ( control instanceof SingleSpinnerT )
		{
			// spinner
			spinner = new NullableSpinner( parent, style | SWT.BORDER );
			spinner.setLayoutData( controlGD );
		}
		else if ( control instanceof DoubleSpinnerT )
		{
			// doubleSpinnerGrid
			Composite c = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			c.setLayout( gridLayout );
			c.setLayoutData( controlGD );

			// doubleSpinner
			spinner = new NullableSpinner( c, style | SWT.BORDER );
			GridData spinnerData = new GridData( SWT.FILL, SWT.CENTER, false, false );
			spinnerData.verticalSpan = 2;
			spinner.setLayoutData( spinnerData );

			this.buttonUp = new Button( c, SWT.ARROW | SWT.UP );
			this.buttonDown = new Button( c, SWT.ARROW | SWT.DOWN );
			if ( tooltip != null ) {
				buttonUp.setToolTipText( tooltip );
				buttonDown.setToolTipText( tooltip );
			}
		}
		
		// tooltip
		if ( tooltip != null ) spinner.setToolTipText( tooltip );

		// Set min/max/precision if a parameter is attached
		if ( parameterConverter != null && parameterConverter instanceof DecimalConverter )
		{
			DecimalConverter tempDecimalConverter = (DecimalConverter) parameterConverter;
			
			if ( tempDecimalConverter.getPrecision() != null )
			{
				spinner.setDigits( tempDecimalConverter.getPrecision().intValue() );
			}
			else
			{
				spinner.setDigits( ControlHelper.getDefaultDigitsForSpinnerControl( parameterConverter.getParameter(), getAtdl4jConfig() ) );
			}

// 3/17/2010 Scott Atwell moved these defaults above (avoid min value > 0 being ignored and set to 0)
// --- public void setMinimum ( int value ) 
//	--- Sets the minimum value that the receiver will allow. This new value 
// --> will be ignored if it is negative  or is not less than the receiver's current maximum value. 			
// 7/7/2010 Scott Atwell spinner now uses BigDecimal and defaults to null			spinner.setMinimum( -Integer.MAX_VALUE );
// 7/7/2010 Scott Atwell spinner now uses BigDecimal and defaults to null			spinner.setMaximum( Integer.MAX_VALUE );
			
			if ( tempDecimalConverter.getMinValue() != null )
			{
				// -- need to handle Percentage ("control value" representation) --
				BigDecimal tempParameterMin = tempDecimalConverter.getMinValue();
				BigDecimal tempControlMin = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMin );
// 7/7/2010 Scott Atwell spinner now uses BigDecimal
//				int tempMin = new Double( tempControlMin.doubleValue() *  Math.pow( 10, spinner.getDigits() ) ).intValue();
//				spinner.setMinimum( tempMin );
				spinner.setMinimum( tempControlMin.setScale( spinner.getDigits(), RoundingMode.HALF_UP )  );
			}
			
			if ( tempDecimalConverter.getMaxValue() != null )
			{
				// -- need to handle Percentage ("control value" representation) --
				BigDecimal tempParameterMax = tempDecimalConverter.getMaxValue();
				BigDecimal tempControlMax = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMax );
// 7/7/2010 Scott Atwell spinner now uses BigDecimal
//				int tempMax = new Double( tempControlMax.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue();
//				spinner.setMaximum( tempMax );
				spinner.setMaximum( tempControlMax.setScale( spinner.getDigits(), RoundingMode.HALF_UP )  );
			}
		}
		else if ( parameterConverter != null && parameterConverter instanceof IntegerConverter )
		{
			IntegerConverter tempIntegerConverter = (IntegerConverter) parameterConverter;
			
			// -- Integer always has 0 digits --
			spinner.setDigits( 0 );

			if ( tempIntegerConverter.getMinValue() != null )
			{
//				spinner.setMinimum( tempIntegerConverter.getMinValue().intValue() );
				BigInteger tempParameterMin = tempIntegerConverter.getMinValue();
				BigInteger tempControlMin = tempIntegerConverter.convertParameterValueToControlValue( tempParameterMin );
				spinner.setMinimum( new BigDecimal( tempControlMin ) );
			}
			else
			{
// 				spinner.setMinimum( -Integer.MAX_VALUE );
				spinner.setMinimum( NullableSpinner.MIN_INTEGER_VALUE_AS_BIG_DECIMAL );
			}
			
			if ( tempIntegerConverter.getMaxValue() != null )
			{
//				spinner.setMaximum( tempIntegerConverter.getMaxValue().intValue() );
				BigInteger tempParameterMax = tempIntegerConverter.getMaxValue();
				BigInteger tempControlMax = tempIntegerConverter.convertParameterValueToControlValue( tempParameterMax );
				spinner.setMaximum( new BigDecimal( tempControlMax ) );
			}
			else
			{
//				spinner.setMaximum( Integer.MAX_VALUE );
				spinner.setMaximum( NullableSpinner.MAX_INTEGER_VALUE_AS_BIG_DECIMAL );
			}
		}
// 7/7/2010 Scott Atwell - removed this as we're already defaulting  		else
//		{
//			spinner.setDigits( 0 );
//			spinner.setMinimum( -Integer.MAX_VALUE );
//			spinner.setMaximum( Integer.MAX_VALUE );
//		}

		

		if ( control instanceof DoubleSpinnerT )
		{
// 4/18/2010 Scott Atwell			if ( ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() ) != null )
//	4/18/2010 Scott Atwell				spinner.setIncrement( ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() ).intValue() );
			BigDecimal tempInnerIncrement = ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() );
			if ( tempInnerIncrement != null )
			{
				// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
// 7/7/2010 Scott Atwell				spinner.setIncrement( new Double( tempInnerIncrement.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue() );
				spinner.setIncrement( tempInnerIncrement );
			}
			
// 7/7/2010 Scott Atwell			int outerStepSize = 1 * (int) Math.pow( 10, spinner.getDigits() );
			BigDecimal outerStepSize = new BigDecimal( "1" );
			
// 4/18/2010 Scott Atwell			if ( ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() ) != null )
// 4/18/2010 Scott Atwell				outerStepSize = ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() ).intValue();
			BigDecimal tempOuterIncrement = ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() );
			if ( tempOuterIncrement != null )
			{
// TODO ?? verify				outerStepSize = (int) tempOuterIncrement.doubleValue();
// 7/17/2010 Scott Atwell				outerStepSize = new Double( tempOuterIncrement.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue();
				outerStepSize = tempOuterIncrement;
			}

			buttonUp.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize ) );
// 7/17/2010			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, -1 * outerStepSize ) );
			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize.negate() ) );
		}
		else if ( control instanceof SingleSpinnerT )
		{
// 4/18/2010 Scott Atwell			if ( ControlHelper.getIncrementValue( control, getAtdl4jConfig() ) != null )
// 4/18/2010 Scott Atwell				spinner.setIncrement( ControlHelper.getIncrementValue( control, getAtdl4jConfig() ).intValue() );
			BigDecimal tempIncrement = ControlHelper.getIncrementValue( control, getAtdl4jConfig() );
			if ( tempIncrement != null )
			{
				// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
// 7/17/2010 Scott Atwell				spinner.setIncrement( new Double( tempIncrement.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue() );
				spinner.setIncrement( tempIncrement );
			}
			else  // tempIncrement is null
			{
				if ( spinner.getDigits() != 0 )
				{
					// -- Set the increment to the precision associated with digits (eg ".01" when digits=2, ".001" when digits=3) --
					spinner.setIncrement( new BigDecimal( Math.pow( 10, -spinner.getDigits() ) ).setScale( spinner.getDigits(), RoundingMode.HALF_UP ) );
				}
				else
				{
					spinner.setIncrement( new BigDecimal( "1" ) );
				}
			}
				
		}

		applyInitialValue();
		
		return parent;
	}

	public BigDecimal getControlValueRaw()
	{
/*** 7/7/2010 Scott Atwell re-wrote		
		if (spinner.getSelection()==null) return null;
		try
		{
			return BigDecimal.valueOf( spinner.getSelection(), spinner.getDigits() );
		}
***/
		try
		{
			return spinner.getValue();
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	public void setValue(BigDecimal value)
	{
// 7/7/2010 Scott Atwell re-wrote		spinner.setSelection( value.unscaledValue().intValue() );
		spinner.setValue( value );
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if (label != null) widgets.add( label );
		widgets.add( spinner );
		if ( control instanceof DoubleSpinnerT )
		{
			widgets.add( buttonUp );
			widgets.add( buttonDown );
		}
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.add( spinner );
		if ( control instanceof DoubleSpinnerT )
		{
			widgets.add( buttonUp );
			widgets.add( buttonDown );
		}
		return widgets;
	}

	public void addListener(Listener listener)
	{
		spinner.addListener( SWT.Modify, listener );
		if ( control instanceof DoubleSpinnerT )
		{
			buttonUp.addListener( SWT.Selection, listener );
			buttonDown.addListener( SWT.Selection, listener );
		}
	}

	public void removeListener(Listener listener)
	{
		spinner.removeListener( SWT.Modify, listener );
		if ( control instanceof DoubleSpinnerT )
		{
			buttonUp.removeListener( SWT.Selection, listener );
			buttonDown.removeListener( SWT.Selection, listener );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( spinner != null ) && ( ! spinner.isDisposed() ) )
		{
			applyInitialValue();
		}
	}

	/**
	 * Invokes spinner.setSelection() assigning 
	 * - Control/@initValue if non-null
	 * - Parameter/@minValue if non-null
	 * - otherwise 0
	 */
	protected void applyInitialValue()
	{
		Double initValue = (Double) ControlHelper.getInitValue( control, getAtdl4jConfig() );
		
		if ( initValue != null )
		{
			// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
// 7/7/2010 Scott Atwell simplified			spinner.setSelection( new Double( initValue.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue() );
			setValue( new BigDecimal( initValue ) );
		}
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
}
