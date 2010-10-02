package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

public class SwingDropDownListWidget
		extends AbstractSwingWidget<String>
{

	private JComboBox dropDownList;
	private JLabel label;

	public void createWidget(Container parent)
	{
		String tooltip = getTooltip();
		
		// label
		if ( control.getLabel() != null ) {
			label = new JLabel();
			label.setText( control.getLabel() );
			if ( tooltip != null ) label.setToolTipText( tooltip );
			parent.add(label);
		}
		
		// dropDownList
		dropDownList = new JComboBox();
		
		// set editable
		dropDownList.setEditable(control instanceof EditableDropDownListT);
		
		// dropDownList items
		List<ListItemT> listItems = ( control instanceof EditableDropDownListT ) ? ( (EditableDropDownListT) control ).getListItem()
				: ( (DropDownListT) control ).getListItem();
		// TODO: throw error if there are no list items
		for ( ListItemT listItem : listItems )
			dropDownList.addItem(listItem.getUiRep() != null ? listItem.getUiRep() : "");

		// tooltip
		if ( tooltip != null ) dropDownList.setToolTipText( tooltip );

		// default initializer
		dropDownList.setSelectedIndex(0);

		// select initValue if available
		String initValue = (String) ControlHelper.getInitValue( control, getAtdl4jOptions() );
		if ( initValue != null )
			setValue( initValue, true );
		
		parent.add(dropDownList);
	}

	// Helper to get list items
	protected List<ListItemT> getListItems()
	{
		return (control instanceof EditableDropDownListT) ? 
				((EditableDropDownListT)control).getListItem() : ((DropDownListT)control).getListItem();
	}	

	public String getControlValueRaw()
	{
		int selection = dropDownList.getSelectedIndex();
		if (selection >= 0)
			return getListItems().get(selection).getEnumID();
		
		else if (control instanceof EditableDropDownListT && 
				dropDownList.getSelectedItem() != null && 
				(String)dropDownList.getSelectedItem() != "")
		{
			// use the enumID if the text matches a combo box item,
			// even if the dropdown was not used to select it
			for (int i = 0; i <  dropDownList.getItemCount(); i++)
			{
				if (((String)dropDownList.getItemAt(i)).equals((String)dropDownList.getSelectedItem()))
					return getListItems().get(i+1).getEnumID();
			}
			// else use the manually entered text string
			return (String)dropDownList.getSelectedItem();
		}
		return null;
	}
	
	public String getParameterValue()
	{
		int selection = dropDownList.getSelectedIndex();
		if ( selection >= 0 )
			return getParameterValueAsEnumWireValue();
		if ( control instanceof EditableDropDownListT &&
				dropDownList.getSelectedItem() != null &&
				dropDownList.getSelectedItem() != "" )
		{
			// use the Parameter's Enum wireValue if the text matches a combo box
			// item, even if the dropdown was not used to select it
			for (int i = 0; i < dropDownList.getItemCount(); i++)
			{
				if (dropDownList.getItemAt(i).equals((String)dropDownList.getSelectedItem()))
					return getEnumWireValue(getListItems().get(i+1).getEnumID());
			}
			// else use the manually entered text string
			return (String)dropDownList.getSelectedItem();
		}
		return null;

	}
	
	public void setValue(String value)
	{
		this.setValue( value, false );
	}

	public void setValue(String value, boolean setValueAsControl)
	{
		for (int i = 0; i < getListItems().size(); i++)
		{
			if (setValueAsControl || parameter == null)
			{
				if (getListItems().get(i).getEnumID().equals(value))
				{
					dropDownList.setSelectedIndex(i);
					break;
				}
			} else {
				if (parameter.getEnumPair().get(i).getWireValue().equals(value))
				{
					dropDownList.setSelectedIndex(i);
					break;
				}
			}
		}
		// TODO: needs to handle case of editable dropdown list;
	}
	
	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<Component>();
		if (label != null) widgets.add(label);
		widgets.add(dropDownList);
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.add(dropDownList);
		return widgets;
	}
	
	public void addListener(SwingListener listener) {
		dropDownList.addActionListener(listener);
	}

	public void removeListener(SwingListener listener) {
		dropDownList.removeActionListener(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( dropDownList != null )
		{
			if ( aControlInitValue != null )
			{
				// -- apply initValue if one has been specified --
				setValue( (String) aControlInitValue, true );
			}
			else
			{
				// -- set to first when no initValue exists --
				if ( dropDownList.getItemCount() > 0 )
				{
					dropDownList.setSelectedIndex( 0 );
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