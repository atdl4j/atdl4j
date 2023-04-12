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
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swt.util.SWTNullableSpinner;
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

public class SWTSpinnerWidget
		extends AbstractSWTWidget<BigDecimal>
{
	private SWTNullableSpinner spinner;
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
		private SWTNullableSpinner spinner;

		private BigDecimal increment;

		public DoubleSpinnerSelection(SWTNullableSpinner spinner2, BigDecimal increment)
		{
			this.spinner = spinner2;
			this.increment = increment;
		}

		public void widgetDefaultSelected(SelectionEvent event)
		{
		}

		public void widgetSelected(SelectionEvent event)
		{
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
			Composite tempSpinnerComposite = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			tempSpinnerComposite.setLayout( gridLayout );
			tempSpinnerComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
			
			// spinner
			spinner = new SWTNullableSpinner( tempSpinnerComposite, SWT.BORDER );
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
			spinner = new SWTNullableSpinner( c, style | SWT.BORDER );
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
		if ( parameterConverter instanceof DecimalConverter )
		{
			DecimalConverter tempDecimalConverter = (DecimalConverter) parameterConverter;
			
			if ( tempDecimalConverter.getPrecision() != null )
			{
				spinner.setDigits( tempDecimalConverter.getPrecision().intValue() );
			}
			else
			{
				spinner.setDigits( ControlHelper.getDefaultDigitsForSpinnerControl( parameterConverter.getParameter(), getAtdl4jOptions() ) );
			}

			if ( tempDecimalConverter.getMinValue() != null )
			{
				// -- need to handle Percentage ("control value" representation) --
				BigDecimal tempParameterMin = tempDecimalConverter.getMinValue();
				BigDecimal tempControlMin = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMin );
				spinner.setMinimum( tempControlMin.setScale( spinner.getDigits(), RoundingMode.HALF_UP )  );
			}
			
			if ( tempDecimalConverter.getMaxValue() != null )
			{
				// -- need to handle Percentage ("control value" representation) --
				BigDecimal tempParameterMax = tempDecimalConverter.getMaxValue();
				BigDecimal tempControlMax = tempDecimalConverter.convertParameterValueToControlValue( tempParameterMax );
				spinner.setMaximum( tempControlMax.setScale( spinner.getDigits(), RoundingMode.HALF_UP )  );
			}
		}
		else if ( parameterConverter instanceof IntegerConverter )
		{
			IntegerConverter tempIntegerConverter = (IntegerConverter) parameterConverter;
			
			// -- Integer always has 0 digits --
			spinner.setDigits( 0 );

			if ( tempIntegerConverter.getMinValue() != null )
			{
				BigInteger tempParameterMin = tempIntegerConverter.getMinValue();
				BigInteger tempControlMin = tempIntegerConverter.convertParameterValueToControlValue( tempParameterMin );
				spinner.setMinimum( new BigDecimal( tempControlMin ) );
			}
			else
			{
				spinner.setMinimum( SWTNullableSpinner.MIN_INTEGER_VALUE_AS_BIG_DECIMAL );
			}
			
			if ( tempIntegerConverter.getMaxValue() != null )
			{
				BigInteger tempParameterMax = tempIntegerConverter.getMaxValue();
				BigInteger tempControlMax = tempIntegerConverter.convertParameterValueToControlValue( tempParameterMax );
				spinner.setMaximum( new BigDecimal( tempControlMax ) );
			}
			else
			{
				spinner.setMaximum( SWTNullableSpinner.MAX_INTEGER_VALUE_AS_BIG_DECIMAL );
			}
		}

		if ( control instanceof DoubleSpinnerT )
		{
			BigDecimal tempInnerIncrement = ControlHelper.getInnerIncrementValue( control, getAtdl4jOptions(), spinner.getDigits() );
			if ( tempInnerIncrement != null )
			{
				// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
				spinner.setIncrement( tempInnerIncrement );
			}
			
			BigDecimal outerStepSize = new BigDecimal( "1" );
			
			BigDecimal tempOuterIncrement = ControlHelper.getOuterIncrementValue( control, getAtdl4jOptions() );
			if ( tempOuterIncrement != null )
			{
				outerStepSize = tempOuterIncrement;
			}

			buttonUp.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize ) );
			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize.negate() ) );
		}
		else if ( control instanceof SingleSpinnerT )
		{
			BigDecimal tempIncrement = ControlHelper.getIncrementValue( control, getAtdl4jOptions(), spinner.getDigits() );
			if ( tempIncrement != null )
			{
				// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
				spinner.setIncrement( tempIncrement );
			}
			else  // tempIncrement is null
			{
				if ( spinner.getDigits() != 0 )
				{
					// -- Set the increment to the precision associated with digits (eg ".01" when digits=2, ".001" when digits=3) --
					spinner.setIncrement( BigDecimal.valueOf( Math.pow( 10, -spinner.getDigits() ) ).setScale( spinner.getDigits(), RoundingMode.HALF_UP ) );
				}
				else
				{
					spinner.setIncrement( BigDecimal.ONE );
				}
			}
				
		}

		applyInitialValue();
		
		return parent;
	}

	public BigDecimal getControlValueRaw()
	{
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
		spinner.setValue( value );
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<>();
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
		List<Control> widgets = new ArrayList<>();
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
		spinner.addListener( listener );
		if ( control instanceof DoubleSpinnerT )
		{
			buttonUp.addListener( SWT.Selection, listener );
			buttonDown.addListener( SWT.Selection, listener );
		}
	}

	public void removeListener(Listener listener)
	{
		spinner.removeListener( listener );
		if ( control instanceof DoubleSpinnerT )
		{
			buttonUp.removeListener( SWT.Selection, listener );
			buttonDown.removeListener( SWT.Selection, listener );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.Atdl4jWidget#reinit()
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
		Double initValue = (Double) ControlHelper.getInitValue( control, getAtdl4jOptions() );
		
		if ( initValue != null )
		{
			// -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
			setValue( BigDecimal.valueOf( initValue ) );
		}
		else
		{
			setValue( null );
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
