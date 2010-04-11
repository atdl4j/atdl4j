package org.atdl4j.ui.swt.widget;

import java.math.BigDecimal;
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
import org.eclipse.swt.widgets.Spinner;
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

		private int increment;

		public DoubleSpinnerSelection(NullableSpinner spinner2, int increment)
		{
			this.spinner = spinner2;
			this.increment = increment;
		}

		public void widgetDefaultSelected(SelectionEvent event)
		{
		}

		public void widgetSelected(SelectionEvent event)
		{
			spinner.setSelection( spinner.getSelection() + increment );
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
/*** 3/17/2010 Scott Atwell	
				// -- not specified in FIXatdl file, use default if we have one within Atdl4jConfig --
				if ( ( getAtdl4jConfig() != null ) && ( getAtdl4jConfig().getDefaultDigitsForSpinnerControl() != null ) )
				{
					spinner.setDigits( getAtdl4jConfig().getDefaultDigitsForSpinnerControl().intValue() );
				}
***/
				spinner.setDigits( ControlHelper.getDefaultDigitsForSpinnerControl( parameterConverter.getParameter(), getAtdl4jConfig() ) );
			}

// 3/17/2010 Scott Atwell moved these defaults above (avoid min value > 0 being ignored and set to 0)
// --- public void setMinimum ( int value ) 
//	--- Sets the minimum value that the receiver will allow. This new value 
// --> will be ignored if it is negative  or is not less than the receiver's current maximum value. 			
			spinner.setMinimum( -Integer.MAX_VALUE );
			spinner.setMaximum( Integer.MAX_VALUE );
			
			if ( tempDecimalConverter.getMinValue() != null )
			{
// 3/17/2010 Scott Atwell				spinner.setMinimum( tempDecimalConverter.getMinValue().intValue() );
// 3/17/2010 Scott Atwell (if value is 2.5 unscaled causes max to be 25 for FloatT)				int tempMin = tempDecimalConverter.getMinValue().unscaledValue().intValue() * (int) Math.pow( 10, spinner.getDigits() );
// 3/17/2010 Scott Atwell need to handle Percentage ("control value" representation)				int tempMin = new Double( tempDecimalConverter.getMinValue().doubleValue() *  Math.pow( 10, spinner.getDigits() ) ).intValue();
				BigDecimal tempParameterMin = tempDecimalConverter.getMinValue();
				BigDecimal tempControlMin = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMin );
				int tempMin = new Double( tempControlMin.doubleValue() *  Math.pow( 10, spinner.getDigits() ) ).intValue();
				spinner.setMinimum( tempMin );
			}
// 3/17/2010 Scott Atwell (moved above)			else
// 3/17/2010 Scott Atwell (moved above)			{
// 3/17/2010 Scott Atwell (moved above)				spinner.setMinimum( -Integer.MAX_VALUE );
// 3/17/2010 Scott Atwell (moved above)			}
			
			if ( tempDecimalConverter.getMaxValue() != null )
			{
// 3/17/2010 Scott Atwell				spinner.setMaximum( tempDecimalConverter.getMaxValue().intValue() );
// 3/17/2010 Scott Atwell (if value is 2.5 unscaled causes max to be 25 for FloatT)				int tempMax = tempDecimalConverter.getMaxValue().unscaledValue().intValue() * (int) Math.pow( 10, spinner.getDigits() );
// 3/17/2010 Scott Atwell need to handle Percentage ("control value" representation)				int tempMax = new Double( tempDecimalConverter.getMaxValue().doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue();
				BigDecimal tempParameterMax = tempDecimalConverter.getMaxValue();
				BigDecimal tempControlMax = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMax );
				int tempMax = new Double( tempControlMax.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue();
				spinner.setMaximum( tempMax );
			}
//3/17/2010 Scott Atwell (moved above)			else
//3/17/2010 Scott Atwell (moved above)			{
//3/17/2010 Scott Atwell (moved above)				spinner.setMaximum( Integer.MAX_VALUE );
//3/17/2010 Scott Atwell (moved above)			}
		}
		else if ( parameterConverter != null && parameterConverter instanceof IntegerConverter )
		{
			IntegerConverter tempIntegerConverter = (IntegerConverter) parameterConverter;
			
			// -- Integer always has 0 digits --
			spinner.setDigits( 0 );

			if ( tempIntegerConverter.getMinValue() != null )
			{
				spinner.setMinimum( tempIntegerConverter.getMinValue().intValue() );
			}
			else
			{
				spinner.setMinimum( -Integer.MAX_VALUE );
			}
			
			if ( tempIntegerConverter.getMaxValue() != null )
			{
				spinner.setMaximum( tempIntegerConverter.getMaxValue().intValue() );
			}
			else
			{
				spinner.setMaximum( Integer.MAX_VALUE );
			}
		}
		else
		{
			spinner.setDigits( 0 );
			spinner.setMinimum( -Integer.MAX_VALUE );
			spinner.setMaximum( Integer.MAX_VALUE );
		}

		

		if ( control instanceof DoubleSpinnerT )
		{
//			if ( ( (DoubleSpinnerT) control ).getInnerIncrement() != null )
//				spinner.setIncrement( ( (DoubleSpinnerT) control ).getInnerIncrement().intValue() );
// 2/21/2010 Scott Atwell -- this method supports innerIncrementPolicy (Static, LotSize, Tick) --			
			if ( ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() ) != null )
				spinner.setIncrement( ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() ).intValue() );

			int outerStepSize = 1 * (int) Math.pow( 10, spinner.getDigits() );
//			if ( ( (DoubleSpinnerT) control ).getOuterIncrement() != null )
//				outerStepSize = ( ( (DoubleSpinnerT) control ).getOuterIncrement().intValue() );
// 2/21/2010 Scott Atwell -- this method supports outerIncrementPolicy (Static, LotSize, Tick) --			
			if ( ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() ) != null )
				outerStepSize = ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() ).intValue();

			buttonUp.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize ) );
			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, -1 * outerStepSize ) );
		}
// 2/21/2010 Scott Atwell Added
		else if ( control instanceof SingleSpinnerT )
		{
//			if ( ( (SingleSpinnerT) control ).getIncrement() != null )
//				spinner.setIncrement( ( (SingleSpinnerT) control ).getIncrement().intValue() );
// 2/21/2010 Scott Atwell -- this method supports incrementPolicy (Static, LotSize, Tick) --			
			if ( ControlHelper.getIncrementValue( control, getAtdl4jConfig() ) != null )
				spinner.setIncrement( ControlHelper.getIncrementValue( control, getAtdl4jConfig() ).intValue() );
		}

/*** 3/17/2010 Scott Atwell		
//		Double initValue = control instanceof SingleSpinnerT ? ( (SingleSpinnerT) control ).getInitValue() : ( (DoubleSpinnerT) control )
//				.getInitValue();
		Double initValue = (Double) ControlHelper.getInitValue( control, getAtdl4jConfig() );
		if ( initValue != null )
			spinner.setSelection( initValue.intValue() * (int) Math.pow( 10, spinner.getDigits() ) );
***/
		applyInitialValue();
		
		return parent;
	}

	public BigDecimal getControlValueRaw()
	{
		if (spinner.getSelection()==null) return null;
		try
		{
			return BigDecimal.valueOf( spinner.getSelection(), spinner.getDigits() );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	public void setValue(BigDecimal value)
	{
		spinner.setSelection( value.unscaledValue().intValue() );
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
/*** 3/17/2010 Scott Atwell			
			if ( aControlInitValue != null )
			{
				// -- apply initValue if one has been specified --
				Double initValue = (Double) aControlInitValue;
				if ( initValue != null )
					spinner.setSelection( initValue.intValue() * (int) Math.pow( 10, spinner.getDigits() ) );
			}
			else
			{
				// -- set to minimum when no initValue exists --
				spinner.setSelection( spinner.getMinimum() );
			}
***/
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
// 3/17/2010 Scott Atwell			spinner.setSelection( initValue.intValue() * (int) Math.pow( 10, spinner.getDigits() ) );
			// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
			spinner.setSelection( new Double( initValue.doubleValue() * Math.pow( 10, spinner.getDigits() ) ).intValue() );
		}
// 3/21/2010 John Shields -- Don't need the below if we are using NullableSpinner
/*		else
		{
			if ( ( parameterConverter != null ) && 
				  ( parameterConverter instanceof DecimalConverter ) &&
				  ( ((DecimalConverter) parameterConverter).getMinValue() != null ) )
			{
				// -- set to minimum when no initValue exists --
				spinner.setSelection( spinner.getMinimum() );
			}
			else if ( ( parameterConverter != null ) && 
						 ( parameterConverter instanceof IntegerConverter ) &&
					    ( ((IntegerConverter) parameterConverter).getMinValue() != null ) )
			{
				// -- set to minimum when no initValue exists --
				spinner.setSelection( spinner.getMinimum() );
			}
			else
			{
				// -- set to 0 (avoid negative Integer.MAX_VALUE) --
				spinner.setSelection( 0 );
			}
		}
		*/
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
}
