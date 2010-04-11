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

public class TextFieldWidget
		extends AbstractSWTWidget<String>
{

	private Text textField;
	private Label label;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public TextFieldWidget(TextFieldT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * init(); }
	 **/

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

/*** 2/12/2010 Scott Atwell	
This is prone to issues:
1) IntT could be negative in some cases -- would need "DecimalFormat( "#;-#" )
2) NumericT can be a number of derived types, may very well not need/want a single decimal place to the right (the "0.0")
3) may need to support negative (add a ";____" to the format
4) may want thousand separators which can be locale specific, etc
5) would need to address a variety of precision
6) Presently is only "attached" to TextField but would be applicable on EditableDropDownList, spinner etc. if of that type
	
		// type validation
		if ( parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT || parameter instanceof SeqNumT
				|| parameter instanceof NumInGroupT )
		{
			// Integer types
			textField.addVerifyListener( new NumberFormatVerifyListener( new DecimalFormat( "#" ), false ) );
		}
		else if ( parameter instanceof NumericT )
		{
			// Decimal types
			textField.addVerifyListener( new NumberFormatVerifyListener( new DecimalFormat( "0.0" ), false ) );
		}
		// TODO: add regex verifier for MultipleCharValueT and
		// MultipleStringValueT
***/
		
		// init value
//		if ( ( (TextFieldT) control ).getInitValue() != null )
//			textField.setText( ( (TextFieldT) control ).getInitValue() );
		if ( ControlHelper.getInitValue( control, getAtdl4jConfig() ) != null )
			textField.setText( (String) ControlHelper.getInitValue( control, getAtdl4jConfig() ) );

		// tooltip
		if ( tooltip != null ) textField.setToolTipText( tooltip );

		return parent;
	}

/** 2/10/2010 Scott Atwell	
	public String getControlValue()
	{
		// 1/24/2010 Scott Atwell added
		if ( !textField.isVisible() || !textField.isEnabled() )
			return null;

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
**/
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
//		setControlExcludingLabelEnabled( false );
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
