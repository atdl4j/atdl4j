package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

public class SwingTextFieldWidget
		extends AbstractSwingWidget<String>
{
	private JFormattedTextField textField;
	private JLabel label;

	public void createWidget(JPanel parent)
	{
		// tooltip
		String tooltip = control.getTooltip();
							
		// label		
		if ( control.getLabel() != null ) {
			label = new JLabel();
			label.setText(control.getLabel());
			if ( tooltip != null ) label.setToolTipText( tooltip );
			parent.add(label);
		}
				
		// textField
		textField = new JFormattedTextField();
		
		/*
		// type validation
		if (parameter instanceof IntT ||
			parameter instanceof TagNumT ||
			parameter instanceof LengthT ||	
			parameter instanceof SeqNumT ||
			parameter instanceof NumInGroupT) {
			// Integer types
			textField.setInputVerifier(new NumberFormatVerifier(new DecimalFormat("#"), false));
		} else if (parameter instanceof NumericT) {
			// Decimal types
			textField.setInputVerifier(new NumberFormatVerifier(new DecimalFormat("0.0"), false));
		}
		// TODO: add regex verifier for MultipleCharValueT and MultipleStringValueT
		*/
		
		// init value
		if ( ControlHelper.getInitValue( control, getAtdl4jOptions() ) != null )
			textField.setText( (String) ControlHelper.getInitValue( control, getAtdl4jOptions() ) );

		// tooltip
		if (tooltip != null) textField.setToolTipText(tooltip);

		parent.add(textField);
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
	
	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<Component>();
		if (label != null) widgets.add(label);
		widgets.add(textField);
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.add(textField);
		return widgets;
	}	
	
	public void addListener(SwingListener listener) {
		textField.addActionListener(listener);
	}

	public void removeListener(SwingListener listener) {
		textField.addActionListener(listener);
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
		if ( ( textField != null ) )
		{
			textField.setText( (aControlInitValue != null ) ? (String) aControlInitValue : "" );
		}
	}
}