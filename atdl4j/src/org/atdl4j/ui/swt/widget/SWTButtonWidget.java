package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.ui.impl.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/*
 * Implements either a CheckBox or a RadioButton
 */
public class SWTButtonWidget
		extends AbstractSWTWidget<Boolean>
{
	private Button button;
	private Label label;
	private SWTRadioButtonListener sWTRadioButtonListener;

	public Widget createWidget(Composite parent, int style)
	{

		// button
		button = new Button( parent, style | ( control instanceof RadioButtonT ? SWT.RADIO : SWT.CHECK ) );
		GridData gd = new GridData( GridData.GRAB_HORIZONTAL );
		gd.horizontalSpan = 2;
		button.setLayoutData( gd );

		if ( control.getLabel() != null )
			button.setText( control.getLabel() );
		if ( getTooltip() != null )
			button.setToolTipText( getTooltip() );

/*** 4/11/2010 Scott Atwell	
		// init value
		if ( control instanceof RadioButtonT )
		{
			if ( ( (RadioButtonT) control ).isInitValue() != null )
				button.setSelection( ( (RadioButtonT) control ).isInitValue() );
		}
		else
		{
			if ( ( (CheckBoxT) control ).isInitValue() != null )
				button.setSelection( ( (CheckBoxT) control ).isInitValue() );
		}
***/
		Boolean tempInitValue = (Boolean) ControlHelper.getInitValue( control, getAtdl4jOptions() );
		if ( tempInitValue != null )
		{
			setValue( tempInitValue );
		}
		
		return parent;
	}

	public void setValue(Boolean value)
	{
		button.setSelection( value.booleanValue() );
		
		if ( getRadioButtonListener() != null )
		{
			getRadioButtonListener().handleEvent( button );
		}
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if ( label != null )
		{
			widgets.add( label );
		}
		widgets.add( button );
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
//		widgets.add( label );
		widgets.add( button );
		return widgets;
	}

	public Button getButton()
	{
		return button;
	}

	public void addListener(Listener listener)
	{
		button.addListener( SWT.Selection, listener );
	}

	public void removeListener(Listener listener)
	{
		button.removeListener( SWT.Selection, listener );
	}

	public Boolean getControlValueRaw()
	{
		return button.getSelection() ? Boolean.TRUE : Boolean.FALSE;
	}

	// Parameter value looks up checkedEnumRef and uncheckedEnumRef
	public Object getParameterValue()
	{
		if ( getControlValue() == null )
		{
			return null;
		}
		else if ( parameter instanceof BooleanT )
		{
			return getControlValue(); // Short-circuit for Boolean parameters
		}
		else if ( getControlValue().equals( Boolean.TRUE ) )
		{
			String checkedEnumRef = control instanceof RadioButtonT ? ( (RadioButtonT) control ).getCheckedEnumRef() : ( (CheckBoxT) control )
					.getCheckedEnumRef();
			if ( checkedEnumRef != null && !checkedEnumRef.equals( "" ) )
			{
				if ( checkedEnumRef.equals( Atdl4jConstants.VALUE_NULL_INDICATOR ) )
					return null;
				else
					return getEnumWireValue( checkedEnumRef );
			}
			else
				return Boolean.TRUE;
		}
		else if ( getControlValue().equals( Boolean.FALSE ) )
		{
			String uncheckedEnumRef = control instanceof RadioButtonT ? ( (RadioButtonT) control ).getUncheckedEnumRef() : ( (CheckBoxT) control )
					.getUncheckedEnumRef();
			if ( uncheckedEnumRef != null && !uncheckedEnumRef.equals( "" ) )
			{
				if ( uncheckedEnumRef.equals( Atdl4jConstants.VALUE_NULL_INDICATOR ) )
					return null;
				else
					return getEnumWireValue( uncheckedEnumRef );
			}
			else
				return Boolean.FALSE;
		}
		return null;
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.Atdl4jWidget#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( button != null ) && ( ! button.isDisposed() ) )
		{
			button.setSelection( (aControlInitValue != null ) ? ((Boolean) aControlInitValue).booleanValue() : false );
		}
	}

	/**
	 * @return the sWTRadioButtonListener
	 */
	public SWTRadioButtonListener getRadioButtonListener()
	{
		return this.sWTRadioButtonListener;
	}

	/**
	 * @param aSWTRadioButtonListener the sWTRadioButtonListener to set
	 */
	public void setRadioButtonListener(SWTRadioButtonListener aSWTRadioButtonListener)
	{
		this.sWTRadioButtonListener = aSWTRadioButtonListener;
	}
	
}
