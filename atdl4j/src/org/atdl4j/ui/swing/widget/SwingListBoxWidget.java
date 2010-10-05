package org.atdl4j.ui.swing.widget;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

public class SwingListBoxWidget
		extends AbstractSwingWidget<String>
{
	private JList listBox;
	private JLabel label;
	private Vector<String> list = new Vector<String>();

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

		// listbox
		listBox =  new JList(list);
		if (control instanceof MultiSelectListT) {
			listBox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else if (control instanceof SingleSelectListT) {
			listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		
		// listBox items
		java.util.List<ListItemT> listItems = control instanceof MultiSelectListT ? ( (MultiSelectListT) control ).getListItem()
				: ( (SingleSelectListT) control ).getListItem();
		for ( ListItemT listItem : listItems )
		{
			list.add(listItem.getUiRep() != null ? listItem.getUiRep() : "");
		}

		// tooltip
		if ( tooltip != null ) listBox.setToolTipText( tooltip );

		// init value
		String initValue = (String) ControlHelper.getInitValue( control, getAtdl4jOptions() );
		if ( initValue != null ) setValue( initValue, true );

	}
	
	public String getControlValueRaw()
	{
		String value = "";
		List<ListItemT> listItems = control instanceof MultiSelectListT ? ( (MultiSelectListT) control ).getListItem()
				: ( (SingleSelectListT) control ).getListItem();
		int[] selection = listBox.getSelectedIndices();

		for ( int i = 0; i < selection.length; i++ )
		{
			value += listItems.get( selection[ i ] ).getEnumID();
			if ( i + 1 != selection.length )
				value += " ";
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
		// split string by spaces in case of MultiSelectList
		List<String> values = Arrays.asList( value.split( "\\s" ) );
		for ( String singleValue : values )
		{
			for ( int i = 0; i < getListItems().size(); i++ )
			{
				if ( setValueAsControl || parameter == null )
				{
					if ( getListItems().get( i ).getEnumID().equals( singleValue ) )
					{
						listBox.setSelectedIndex( i );
						break;
					}
				}
				else
				{
					if ( parameter.getEnumPair().get( i ).getWireValue().equals( singleValue ) )
					{
						listBox.setSelectedIndex( i );
						break;
					}
				}
			}
		}
	}
	
	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<Component>();
		if (label != null) widgets.add(label);
		widgets.add(listBox);
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel()
	{
		List<Component> widgets = new ArrayList<Component>();
		widgets.add( listBox );
		return widgets;
	}
	
	public void addListener(SwingListener listener) {
		listBox.addListSelectionListener(listener);
	}

	public void removeListener(SwingListener listener) {
		listBox.removeListSelectionListener(listener);
	}
	
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( listBox != null )
		{
			if ( aControlInitValue != null )
			{
				// -- apply initValue if one has been specified --
				setValue( (String) aControlInitValue, true );
			}
			else
			{
				// -- set to first when no initValue exists --
				if ( list.size() > 0 )
				{
					listBox.setSelectedIndex( 0 );
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