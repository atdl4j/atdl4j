package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.ui.impl.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class SWTCheckBoxListWidget
		extends AbstractSWTWidget<String>
{

	private List<Button> multiCheckBox = new ArrayList<Button>();
	private Label label;

	// -- Overriden --
	protected void initPreCheck()
	{
		// validate ListItems and EnumPairs
		if ( parameter != null && ( (CheckBoxListT) control ).getListItem().size() != parameter.getEnumPair().size() )
			throw new IllegalArgumentException( "ListItems for Control \"" + control.getID() + "\" and EnumPairs for Parameter \"" + parameter.getName()
					+ "\" are not equal in number." );
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
		
		Composite c = new Composite( parent, SWT.NONE );
		c.setLayoutData(controlGD);
		
		if ( ((CheckBoxListT) control).getOrientation() != null &&
			 PanelOrientationT.VERTICAL.equals( ((CheckBoxListT) control).getOrientation() ) )
		{
			c.setLayout( new GridLayout( 1, false ) );
		} 
		else 
		{
			RowLayout rl = new RowLayout();
			rl.wrap = false;
			c.setLayout( rl );
		}
		
		// checkBoxes
		List<ListItemT> listItems = ( (CheckBoxListT) control ).getListItem();
		for ( ListItemT listItem : listItems )
		{
			Button checkBox = new Button( c, style | SWT.CHECK );
			checkBox.setText( listItem.getUiRep() );

			if ( parameter != null )
			{
				for ( EnumPairT enumPair : parameter.getEnumPair() )
				{
					if ( enumPair.getEnumID().equals( listItem.getEnumID() ) )
					{

						// set tooltip
						if ( enumPair.getDescription() != null )
							checkBox.setToolTipText( enumPair.getDescription() );
						else if ( tooltip != null )
							checkBox.setToolTipText( tooltip );
						break;
					}
				}
			}
			else
			{
				if ( tooltip != null )
					checkBox.setToolTipText( tooltip );
			}
			multiCheckBox.add( checkBox );
		}

		// set initValue
		if ( ControlHelper.getInitValue( control, getAtdl4jOptions() ) != null )
			setValue( (String) ControlHelper.getInitValue( control, getAtdl4jOptions() ), true );

		return parent;
	}

	public String getControlValueRaw()
	{
		String value = "";
		for ( int i = 0; i < multiCheckBox.size(); i++ )
		{
			Button b = multiCheckBox.get( i );
			if ( b.getSelection() )
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
		// Helper method from AbstractAtdl4jWidget
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
			Button b = multiCheckBox.get( i );
			if ( setValueAsControl || parameter == null )
			{
				String enumID = ( (CheckBoxListT) control ).getListItem().get( i ).getEnumID();
				b.setSelection( values.contains( enumID ) );
			}
			else
			{
				String wireValue = parameter.getEnumPair().get( i ).getWireValue();
				b.setSelection( values.contains( wireValue ) );
			}
		}
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if (label != null) widgets.add( label );
		widgets.addAll( multiCheckBox );
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.addAll( multiCheckBox );
		return widgets;
	}

	public void addListener(Listener listener)
	{
		for ( Button checkBox : multiCheckBox )
		{
			checkBox.addListener( SWT.Selection, listener );
		}
	}

	public void removeListener(Listener listener)
	{
		for ( Button b : multiCheckBox )
		{
			b.removeListener( SWT.Selection, listener );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.Atdl4jWidget#reinit()
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
			for ( Button tempButton : multiCheckBox )
			{
				if ( ( tempButton != null ) && ( ! tempButton.isDisposed() ) )
				{
					tempButton.setSelection( false );
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
