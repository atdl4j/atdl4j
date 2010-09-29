package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.Arrays;

import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.ui.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class ListBoxWidget
		extends AbstractSWTWidget<String>
{

	private List listBox;
	private Label label;

	public Widget createWidget(Composite parent, int style)
	{
		String tooltip = getTooltip();
		GridData controlGD = new GridData( SWT.FILL, SWT.TOP, false, false );
		
		// label
		if ( control.getLabel() != null ) {
			label = new Label( parent, SWT.NONE );
			label.setText( control.getLabel() );
			if ( tooltip != null ) label.setToolTipText( tooltip );
			label.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, false ) );
			controlGD.horizontalSpan = 1;
		} else {
			controlGD.horizontalSpan = 2;
		}

		// dropDownList
		style = style | SWT.BORDER;
		if ( control instanceof MultiSelectListT )
		{
			style |= SWT.MULTI;
		}
		else if ( control instanceof SingleSelectListT )
		{
			style |= SWT.SINGLE;
		}
		listBox = new List( parent, style );
		listBox.setLayoutData( controlGD );

		// listBox items
		java.util.List<ListItemT> listItems = control instanceof MultiSelectListT ? ( (MultiSelectListT) control ).getListItem()
				: ( (SingleSelectListT) control ).getListItem();
		for ( ListItemT listItem : listItems )
		{
			listBox.add( listItem.getUiRep() );
		}

		// tooltip
		if ( tooltip != null ) listBox.setToolTipText( tooltip );

		// init value
		String initValue = (String) ControlHelper.getInitValue( control, getAtdl4jOptions() );
		if ( initValue != null )
			setValue( initValue, true );

		return parent;
	}


	public String getControlValueRaw()
	{
		String value = "";
		java.util.List<ListItemT> listItems = control instanceof MultiSelectListT ? ( (MultiSelectListT) control ).getListItem()
				: ( (SingleSelectListT) control ).getListItem();
		int[] selection = listBox.getSelectionIndices();

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
		java.util.List<String> values = Arrays.asList( value.split( "\\s" ) );
		for ( String singleValue : values )
		{
			for ( int i = 0; i < getListItems().size(); i++ )
			{
				if ( setValueAsControl || parameter == null )
				{
					if ( getListItems().get( i ).getEnumID().equals( singleValue ) )
					{
						listBox.select( i );
						break;
					}
				}
				else
				{
					if ( parameter.getEnumPair().get( i ).getWireValue().equals( singleValue ) )
					{
						listBox.select( i );
						break;
					}
				}
			}
		}
	}

	public java.util.List<Control> getControls()
	{
		java.util.List<Control> widgets = new ArrayList<Control>();
		if (label != null) widgets.add( label );
		widgets.add( listBox );
		return widgets;
	}

	public java.util.List<Control> getControlsExcludingLabel()
	{
		java.util.List<Control> widgets = new ArrayList<Control>();
		widgets.add( listBox );
		return widgets;
	}

	public void addListener(Listener listener)
	{
		listBox.addListener( SWT.Selection, listener );
	}

	public void removeListener(Listener listener)
	{
		listBox.removeListener( SWT.Selection, listener );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( listBox != null ) && ( ! listBox.isDisposed() ) )
		{
			if ( aControlInitValue != null )
			{
				// -- apply initValue if one has been specified --
				setValue( (String) aControlInitValue, true );
			}
			else
			{
				// -- set to first when no initValue exists --
				if ( listBox.getItemCount() > 0 )
				{
					listBox.select( 0 );
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
