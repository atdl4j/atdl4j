package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
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
public class ButtonWidget
		extends AbstractSWTWidget<Boolean>
{

	// TODO move this to app constanst
	public static final String NULL_STRING = "{NULL}";
	private Button button;
	private Label label;
	private RadioButtonListener radioButtonListener;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public ButtonWidget(CheckBoxT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * init(); }
	 * 
	 * public ButtonWidget(RadioButtonT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * init(); }
	 **/
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

/** 2/10/2010 Scott Atwell	
	public Boolean getControlValue()
	{
		// 1/24/2010 Scott Atwell added
		if ( !button.isVisible() || !button.isEnabled() )
			return null;
		return button.getSelection() ? Boolean.TRUE : Boolean.FALSE;
	}
**/	
	public Boolean getControlValueRaw()
	{
		return button.getSelection() ? Boolean.TRUE : Boolean.FALSE;
	}

	// 2/1/2010 John Shields added
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
// 3/11/2010 Scott Atwell		else if ( getControlValue() == Boolean.TRUE )
		else if ( getControlValue().equals( Boolean.TRUE ) )
		{
			String checkedEnumRef = control instanceof RadioButtonT ? ( (RadioButtonT) control ).getCheckedEnumRef() : ( (CheckBoxT) control )
					.getCheckedEnumRef();
			if ( checkedEnumRef != null && !checkedEnumRef.equals( "" ) )
			{
				if ( checkedEnumRef.equals( ButtonWidget.NULL_STRING ) )
					return null;
				else
					return getEnumWireValue( checkedEnumRef );
			}
			else
				return Boolean.TRUE;
		}
// 3/11/2010 Scott Atwell		else if ( getControlValue() == Boolean.FALSE )
		else if ( getControlValue().equals( Boolean.FALSE ) )
		{
			String uncheckedEnumRef = control instanceof RadioButtonT ? ( (RadioButtonT) control ).getUncheckedEnumRef() : ( (CheckBoxT) control )
					.getUncheckedEnumRef();
			if ( uncheckedEnumRef != null && !uncheckedEnumRef.equals( "" ) )
			{
				if ( uncheckedEnumRef.equals( ButtonWidget.NULL_STRING ) )
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
	 * @see org.atdl4j.ui.ControlUI#reinit()
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
	 * @return the radioButtonListener
	 */
	public RadioButtonListener getRadioButtonListener()
	{
		return this.radioButtonListener;
	}

	/**
	 * @param aRadioButtonListener the radioButtonListener to set
	 */
	public void setRadioButtonListener(RadioButtonListener aRadioButtonListener)
	{
		this.radioButtonListener = aRadioButtonListener;
	}
	
}
