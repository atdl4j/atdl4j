// Taken from http://www.java-forums.org/swt/9902-how-use-swt-spinner.html and modified.

package org.atdl4j.ui.swt.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.log4j.Logger;
import org.atdl4j.ui.swt.impl.SWTStrategyPanelHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

public class SWTNullableSpinner extends Composite 
{
	private static final Logger logger = Logger.getLogger( SWTNullableSpinner.class );
	
	static final int BUTTON_WIDTH = 20;
	Text text;
	Button up, down;
	BigDecimal minimum, maximum;
	BigDecimal increment = new BigDecimal( 1 );
	int digits = 0;
	public static BigDecimal MIN_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal( -Integer.MAX_VALUE );
	public static BigDecimal MAX_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal( Integer.MAX_VALUE );
	
	public SWTNullableSpinner(Composite parent, int style) {
		super(parent, style);
		
		/*
		GridData gd = new GridData(GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.grabExcessHorizontalSpace = false;
		this.setLayoutData(gd);*/
		
		text = new Text(this, SWT.SINGLE | SWT.NONE);    		
		up = new Button(this, SWT.ARROW | SWT.UP);
		down = new Button(this, SWT.ARROW | SWT.DOWN);
				
		text.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				verify(e);
			}
		});

		text.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				stripExtraDecimalPoints();
			}
		});


		text.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				traverse(e);
			}
		});

		up.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				up();
			}
		});

		down.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				down();
			}
		});

		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				resize();
			}
		});

		addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event e) {
				focusIn();
			}
		});

		text.setFont(getFont());
	}

	void verify(Event e) 
	{
		try 
		{
			if (e.text==null||e.text.equals("")) return;
			
			if ( ( e.text.equals( "." ) )	|| ( e.text.endsWith( "." ) ) )
			{
				// -- Let it go, assume user is entering the decimal place portion and just entered the "." part thus far --
			}
			else
			{
				BigDecimal tempBigDecimal = new BigDecimal( e.text );
			}
		} 
		catch (NumberFormatException ex) 
		{
			e.doit = false;
		}
	}

	void traverse(Event e) {
		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			if (e.keyCode == SWT.ARROW_UP) {
				e.doit = true;
				e.detail = SWT.NULL;
				up();
			}
			break;

		case SWT.TRAVERSE_ARROW_NEXT:
			if (e.keyCode == SWT.ARROW_DOWN) {
				e.doit = true;
				e.detail = SWT.NULL;
				down();
			}
			break;
		}
	}
	
	void up() 
	{		
		BigDecimal tempValue = getValue();
		if ( tempValue == null )
		{
			if ( ( getMinimum() != null ) && ( ! MIN_INTEGER_VALUE_AS_BIG_DECIMAL.equals( getMinimum() ) ) )
			{
				setValue( getMinimum() );
			}
			else
			{
				setValue( getIncrement() );
			}
		}
		else 
		{
			increment( getIncrement() );
		}
		
		notifyListeners(SWT.Selection, new Event());
	}

	void down() 
	{
		BigDecimal tempValue = getValue();
		if ( tempValue == null )
		{
			if ( ( getMaximum() != null ) && ( ! MAX_INTEGER_VALUE_AS_BIG_DECIMAL.equals( getMaximum() ) ) )
			{
				setValue( getMaximum() );
			}
			else
			{
				setValue( getIncrement() );
			}
		}
		else 
		{
			decrement( getIncrement() );
		}
		
		notifyListeners(SWT.Selection, new Event());
	}

	void focusIn() {
		text.setFocus();
	}

	public void setFont(Font font) {
		super.setFont(font);
		text.setFont(font);
	}

	public void setValue(BigDecimal aValue) 
	{
		BigDecimal tempValue = aValue;
		
// 12/15/2010 Scott Atwell		if ( ( getMinimum() != null ) && ( tempValue.compareTo( getMinimum() ) < 0 ) )
		if ( ( getMinimum() != null ) && ( tempValue != null ) && ( tempValue.compareTo( getMinimum() ) < 0 ) )
		{
			tempValue = getMinimum();
		}
// 12/15/2010 Scott Atwell		else if ( ( getMaximum() != null ) && ( tempValue.compareTo( getMaximum() ) > 0 ) )
		else if ( ( getMaximum() != null ) && ( tempValue != null ) && ( tempValue.compareTo( getMaximum() ) > 0 ) )
		{
			tempValue = getMaximum();
		} 
			
// 12/15/2010 Scott Atwell		text.setText( tempValue.toPlainString() );
		if ( tempValue != null )
		{
			text.setText( tempValue.toPlainString() );
		}
		else
		{
			text.setText( "" );
		}
		text.selectAll();
		text.setFocus();
	}

	public BigDecimal getValue() 
	{
		if ( ( text.getText()==null ) || ( text.getText().equals("") ) )
		{
			return null;
		}
		else
		{
// note this is what we had to use in SWTSpinnerWidget when getSelection() returned equiv of BigDecimal.unscaledValue()
//	return BigDecimal.valueOf( spinner.getSelection(), spinner.getDigits() );
			BigDecimal tempValue = new BigDecimal( text.getText() );
			BigDecimal tempValueScaled = tempValue.setScale( getDigits(), RoundingMode.HALF_UP );			
			if ( ! tempValue.equals( tempValueScaled ) )
			{
				String tempNewValueString = tempValueScaled.toPlainString();
				logger.debug( "tempValue: " + tempValue + " tempValueScaled: " + tempValueScaled + " tempValueScaled.toPlainString(): " + tempNewValueString );				
				int tempCaretPosition = text.getCaretPosition();
				logger.debug( "getCaretPosition(): " + text.getCaretPosition() );
				// -- Handle user entering ".12" and formatter converting to "0.12" -- avoid result of "0.21") --
				if ( ( tempCaretPosition == 2 ) && 
					  ( text.getText().charAt(0) == '.' ) && 
					  ( tempNewValueString.startsWith( "0." ) ) )
				{
					// -- we're notified at ".1" but formatted value is converting to "0.1" --
					logger.debug("Incrementing CaretPosition (was " + tempCaretPosition + ") to account for formatted string having \".\" converted to \"0.\" automatically." );		
					tempCaretPosition++;
				}
				
				text.setText( tempNewValueString );
				text.setSelection( tempCaretPosition, tempCaretPosition );
			}
			
			return tempValueScaled;
		}
	}
	
	
	void resize() {
		Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int textWidth = pt.x - BUTTON_WIDTH;
		int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		up.setBounds(textWidth, -3, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, pt.y - buttonHeight - 3, BUTTON_WIDTH,
				buttonHeight);
	}
	
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = 100;
		int height = 20;
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		return new Point(width, height);
	}

	public void addSelectionListener(SelectionListener listener) {
		if (listener == null)
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		addListener(SWT.Selection, new TypedListener(listener));
	}

	public void setDigits(int aDigits) {
		digits = aDigits;
	}
	
	public int getDigits() {
		return digits;
	}

	public void increment( BigDecimal aIncrement )
	{
		if ( getValue() != null )
		{
			setValue( getValue().add( aIncrement ) );
		}
		else if ( aIncrement != null )
		{
			setValue( aIncrement );
		}
	}

	public void decrement( BigDecimal aDecrement )
	{
		if ( getValue() != null )
		{
			setValue( getValue().subtract( aDecrement ) );
		}
		else if ( aDecrement != null )
		{
			setValue( aDecrement );
		}
	}

	/**
	 * @return the minimum
	 */
	public BigDecimal getMinimum()
	{
		return this.minimum;
	}

	/**
	 * @param aMinimum the minimum to set
	 */
	public void setMinimum(BigDecimal aMinimum)
	{
		this.minimum = aMinimum;
	}

	/**
	 * @return the maximum
	 */
	public BigDecimal getMaximum()
	{
		return this.maximum;
	}

	/**
	 * @param aMaximum the maximum to set
	 */
	public void setMaximum(BigDecimal aMaximum)
	{
		this.maximum = aMaximum;
	}

	/**
	 * @return the increment
	 */
	public BigDecimal getIncrement()
	{
		return this.increment;
	}

	/**
	 * @param aIncrement the increment to set
	 */
	public void setIncrement(BigDecimal aIncrement)
	{
		this.increment = aIncrement;
	}
	
	public void addListener(Listener listener)
	{
		text.addListener( SWT.Modify, listener );
	}

	public void removeListener(Listener listener)
	{
		text.removeListener( SWT.Modify, listener );
	}
	
	/**
    * Spinners with decimal places use DecimalFormatter to automatically render/re-render the string value.  
    * When entering a decimal value via keyboard within an empty Spinner text field, the displayed value
    * will automatically adjust (eg entering "1.23" in a field with 2 decimal places will render "1.23.00")
    * without the use of this adjustment method.
    *
	 * @return true if extra decimal point characters had to be stripped
	 */
	private boolean stripExtraDecimalPoints()
	{
		String tempText = text.getText();
		if ( tempText.length() > 0 )
		{
			int tempFirstDecimalPoint = tempText.indexOf( '.', 0 );
			if ( tempFirstDecimalPoint >= 0 )
			{
				int tempNextDecimalPoint = tempText.indexOf( '.', (tempFirstDecimalPoint + 1) );
				if ( tempNextDecimalPoint >= 0 )
				{
					logger.debug(" Multiple decimal points...  tempText: " + tempText );					
					boolean tempDecimalPointFound = false;
					StringBuffer tempStringBuffer = new StringBuffer();
					for ( int i=0; i < tempText.length(); i++ )
					{
						if ( tempText.charAt( i ) == '.' )
						{
							if ( ! tempDecimalPointFound )
							{
								tempDecimalPointFound = true;
							}
							else
							{
								// skip/omit the character
								continue;
							}
						}
						
						tempStringBuffer.append( tempText.charAt( i ) );
					}
					
					logger.debug(" text.setText( tempStringBuffer.toString() ): " + tempStringBuffer.toString() );		
					int tempCaretPosition = text.getCaretPosition();
					text.setText( tempStringBuffer.toString() );
					text.setSelection( tempCaretPosition, tempCaretPosition );
					return true;
				}
			}
		}
		
		return false;
	}
}