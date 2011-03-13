package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.ui.swing.SwingListener;

public class SwingCheckBoxListWidget
		extends AbstractSwingWidget<String>
{
	private List<JCheckBox> multiCheckBox = new ArrayList<JCheckBox>();
	private JLabel label;

	// -- Overriden --
	protected void initPreCheck()
	{
		// validate ListItems and EnumPairs
		if ( parameter != null && ( (CheckBoxListT) control ).getListItem().size() != parameter.getEnumPair().size() )
			throw new IllegalArgumentException( "ListItems for Control \"" + control.getID() + "\" and EnumPairs for Parameter \"" + parameter.getName()
					+ "\" are not equal in number." );
	}

	public void createWidget(JPanel parent)
	{
		// wrapper
		JPanel wrapper = new JPanel();
		
		// tooltip
		String tooltip = control.getTooltip();
				
		// label
		if (control.getLabel() != null)
		{
			label = new JLabel();
			label.setText(control.getLabel());
			if (tooltip != null) label.setToolTipText(tooltip);
			wrapper.add(label);
		}
		
		if ( ((CheckBoxListT) control).getOrientation() != null &&
				 PanelOrientationT.VERTICAL.equals( ((CheckBoxListT) control).getOrientation() ) )
		{
			// TODO: NOT IMPLEMENTED
		} 
		else 
		{
			// TODO: NOT IMPLEMENTED
		}
		
		// checkBoxes
		List<ListItemT> listItems = ((CheckBoxListT)control).getListItem();
		for (ListItemT listItem : listItems) {
			JCheckBox checkBox = new JCheckBox();
			if (listItem.getUiRep() != null) checkBox.setText(listItem.getUiRep());
			
			if (parameter != null) {
				for (EnumPairT enumPair :  parameter.getEnumPair()) {
					if (enumPair.getEnumID() == listItem.getEnumID()) {
						
						// set tooltip
						if (enumPair.getDescription() != null)
							checkBox.setToolTipText(enumPair.getDescription());
						else if (tooltip != null)
							checkBox.setToolTipText(tooltip);
						break;
					}
				}
			} else {
				if (tooltip != null) checkBox.setToolTipText(tooltip);
			}
			multiCheckBox.add(checkBox);
			wrapper.add(checkBox);
		}
		
		// set initValue
		if  (((CheckBoxListT)control).getInitValue() != null)
			setValue(((CheckBoxListT)control).getInitValue(), true);
		
		parent.add(wrapper);
	}

	public String getControlValueRaw()
	{
		String value = "";
		for ( int i = 0; i < multiCheckBox.size(); i++ )
		{
			JCheckBox b = multiCheckBox.get( i );
			if ( b.isSelected() )
			{
				if ( "".equals( value ) )
				{
					value += ( (CheckBoxListT) control ).getListItem().get( i ).getEnumID();
				}
				else
				{
					value += " " + ( (CheckBoxListT) control ).getListItem().get( i ).getEnumID();
				}
			}
		}
		return "".equals( value ) ? null : value;
	}

	public String getParameterValue()
	{
		// Helper method from AbstractControlUI
		return getParameterValueAsMultipleValueString();
	}

	public void setValue(String value)
	{
		this.setValue( value, false );
	}

	public void setValue(String value, boolean setValueAsControl)
	{
		List<String> values = Arrays.asList( value.split( "\\s" ) );
		for ( int i = 0; i < multiCheckBox.size(); i++ )
		{
			JCheckBox b = multiCheckBox.get( i );
			if ( setValueAsControl || parameter == null )
			{
				String enumID = ( (CheckBoxListT) control ).getListItem().get( i ).getEnumID();
				b.setSelected( values.contains( enumID ) );
			}
			else
			{
				String wireValue = parameter.getEnumPair().get( i ).getWireValue();
				b.setSelected( values.contains( wireValue ) );
			}
		}
	}
	
	public List<Component> getComponents()
	{
		List<Component> widgets = new ArrayList<Component>();
		if (label != null) widgets.add( label );
		widgets.addAll( multiCheckBox );
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.addAll( multiCheckBox );
		return widgets;
	}	
	
	public void addListener(SwingListener listener) {
		for ( JCheckBox b : multiCheckBox )
		{
			b.addActionListener(listener);
		}
	}

	public void removeListener(SwingListener listener) {
		for ( JCheckBox b : multiCheckBox )
		{
			b.removeActionListener(listener);
		}
	}	

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( aControlInitValue != null )
		{
			// -- apply initValue if one has been specified --
			setValue( (String) aControlInitValue, true );
		}
		else
		{
			// -- reset each when no initValue exists --
			for ( JCheckBox tempButton : multiCheckBox )
			{
				if ( ( tempButton != null ) )
				{
					tempButton.setSelected( false );
				}
			}
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
