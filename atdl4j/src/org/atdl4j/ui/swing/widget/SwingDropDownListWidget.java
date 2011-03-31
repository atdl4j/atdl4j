package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

public class SwingDropDownListWidget
		extends AbstractSwingWidget<String>
{

	private JComboBox dropDownList;
	private JLabel label;
	private JPanel wrapper;

	public void createWidget(JPanel parent)
	{
		String tooltip = getTooltip();
		
		// label
		if ( control.getLabel() != null ) {
			label = new JLabel();
			label.setText( control.getLabel());
			if ( tooltip != null ) label.setToolTipText( tooltip );
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
		
		
		if (label != null){
			wrapper = new JPanel(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridx = 0;
	    c.gridy = 0;   
	    c.gridwidth = 1;
	    c.weightx = 1.0;
	    c.weighty = 1.0;
	    c.insets = new Insets(0, 0, 0, 0);
	    wrapper.add( label, c);
			c.gridx = 1;
	    c.gridy = 0;
	    c.insets = new Insets(0, 0, 0, 0);
	    wrapper.add( dropDownList, c);
			parent.add(wrapper);
		}
		else {
			parent.add(dropDownList);
		}
	}

	public void setVisible(boolean visible){
		if (wrapper != null)
			wrapper.setVisible(visible);
		else 
			super.setVisible(visible);
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
                            if (parameter.getEnumPair().get( i ).getWireValue().equals( value ))
                            {
                                    String enumID = parameter.getEnumPair().get( i ).getEnumID();
                                    for (int a = 0; a < getListItems().size(); a++) {
                                            if (getListItems().get(a).getEnumID().equals(enumID)){
                                                    dropDownList.setSelectedIndex(a);
                                                    break;
                                            }
                                    }
                                    break;
                            }
                    }
            }

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

	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.impl.AbstractAtdl4jWidget#applyConstOrInitValues()
	 */
	@Override
	public void applyConstOrInitValues() {
		super.applyConstOrInitValues();
		dropDownList.setMaximumSize(new Dimension(dropDownList.getPreferredSize().width + 5, dropDownList.getPreferredSize().height));
		dropDownList.revalidate();
	}
	
	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
}