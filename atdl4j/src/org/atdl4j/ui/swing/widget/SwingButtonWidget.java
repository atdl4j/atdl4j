package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

/*
 * Implements either a CheckBox or a RadioButton
 */
public class SwingButtonWidget
		extends AbstractSwingWidget<Boolean>
{
	private JToggleButton button;
	//private SwingRadioButtonListener radioButtonListener;

	public void createWidget(JPanel parent) {
		
		// button
		if (control instanceof RadioButtonT) {
			button = new JRadioButton();
		} else {
			button = new JCheckBox();
		}
		button.setName(getName()+"/button");
		
		// set label and tooltips
		if ( control.getLabel() != null ) button.setText( control.getLabel() );
		if ( getTooltip() != null ) button.setToolTipText( getTooltip() );
		// if (control.getTooltip() != null) button.setToolTipText(control.getTooltip());
				
		Boolean tempInitValue = (Boolean) ControlHelper.getInitValue( control, getAtdl4jOptions() );
		if ( tempInitValue != null )
		{
			setValue( tempInitValue );
		}
		
		parent.add(button);
	}
	
	public void setValue(Boolean value)
	{
		if (control instanceof CheckBoxT) {
			if (button.isSelected() != value.booleanValue()){
				button.doClick();
			}
		}
		else if (control instanceof RadioButtonT) {
			if (button.isSelected() != value.booleanValue()){
				button.doClick();
			}
		}
		else {
			button.setEnabled(value.booleanValue());
		}
	}

	public List<Component> getComponents()
	{
		List<Component> widgets = new ArrayList<Component>();
		widgets.add(button);
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel() {
		// Label is part of the button control
		return getComponents();
	}	
	
	public JToggleButton getButton()
	{
		return button;
	}

	public void addListener(SwingListener listener) {
		button.addActionListener(listener);
	}

	public void removeListener(SwingListener listener) {
		button.removeActionListener(listener);
	}	
	
	public Boolean getControlValueRaw()
	{
		return button.isSelected() ? Boolean.TRUE : Boolean.FALSE;
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
			String checkedEnumRef = control instanceof RadioButtonT ? ((RadioButtonT) control).getCheckedEnumRef() : ( (CheckBoxT) control ).getCheckedEnumRef();
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
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( button != null ) )
		{
			button.setSelected( (aControlInitValue != null ) ? ((Boolean) aControlInitValue).booleanValue() : false );
		}
	}
}
