package org.atdl4j.ui.swing.widget;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.ui.ControlHelper;
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
		
		// set label and tooltips
		if ( control.getLabel() != null ) button.setText( control.getLabel() );
		if ( getTooltip() != null ) button.setToolTipText( getTooltip() );
		// if (control.getTooltip() != null) button.setToolTipText(control.getTooltip());
				
		// init value		
		/*if (control instanceof RadioButtonT) {
			if (((RadioButtonT)control).isInitValue() != null) 
				button.setEnabled(((RadioButtonT)control).isInitValue());
		} else {
			if (((CheckBoxT)control).isInitValue() != null)
				button.setEnabled(((CheckBoxT)control).isInitValue());
		}*/
		Boolean tempInitValue = (Boolean) ControlHelper.getInitValue( control, getAtdl4jConfig() );
		if ( tempInitValue != null )
		{
			setValue( tempInitValue );
		}
		
		parent.add(button);
	}
	
	public void setValue(Boolean value)
	{
		button.setEnabled(value.booleanValue());
	}

	public List<JComponent> getComponents()
	{
		List<JComponent> widgets = new ArrayList<JComponent>();
		widgets.add(button);
		return widgets;
	}

	public List<JComponent> getComponentsExcludingLabel() {
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
