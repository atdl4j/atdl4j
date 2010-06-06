package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.ui.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class SWTTextFieldWidget
		extends AbstractSWTWidget<String>
{

	private Text textField;
	private Label label;


	public Widget createWidget(Composite parent, int style)
	{
		String tooltip = getTooltip();
		GridData controlGD = new GridData( SWT.FILL, SWT.CENTER, true, false );
		
		// label
		if ( control.getLabel() != null ) {
			label = new Label( parent, SWT.NONE );
			label.setText( control.getLabel() );
			if ( tooltip != null ) label.setToolTipText( tooltip );
			controlGD.horizontalSpan = 1;
		} else {
			controlGD.horizontalSpan = 2;
		}
				
		// textField
		textField = new Text( parent, style | SWT.BORDER );
		textField.setLayoutData( controlGD );


		// init value
		if ( ControlHelper.getInitValue( control, getAtdl4jConfig() ) != null )
			textField.setText( (String) ControlHelper.getInitValue( control, getAtdl4jConfig() ) );

		// tooltip
		if ( tooltip != null ) textField.setToolTipText( tooltip );

		return parent;
	}


	public String getControlValueRaw()
	{
		String value = textField.getText();

		if ( "".equals( value ) )
		{
			return null;
		}
		else
		{
			return value;
		}
	}

	public void setValue(String value)
	{
		textField.setText( ( value == null ) ? "" : value.toString() );
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if (label != null) widgets.add( label );
		widgets.add( textField );
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.add( textField );
		return widgets;
	}

	public void addListener(Listener listener)
	{
		textField.addListener( SWT.Modify, listener );
	}

	public void removeListener(Listener listener)
	{
		textField.removeListener( SWT.Modify, listener );
	}

	/**
	 * Overridden -- makes the textField appear non-editable vs. the default of disabled.
	 */
	public void processConstValueHasBeenSet()
	{
		textField.setEditable( false );
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		if ( Boolean.FALSE.equals( aNewNullValueInd ) )
		{
			textField.setText( "" );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( textField != null ) && ( ! textField.isDisposed() ) )
		{
			textField.setText( (aControlInitValue != null ) ? (String) aControlInitValue : "" );
		}
	}
}
