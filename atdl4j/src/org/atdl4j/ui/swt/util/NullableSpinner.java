// Taken from http://www.java-forums.org/swt/9902-how-use-swt-spinner.html and modified.

package org.atdl4j.ui.swt.util;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

public class NullableSpinner extends Composite {

	static final int BUTTON_WIDTH = 20;
	Text text;
	Button up, down;
// 7/7/2010 Scott Atwell changed from int to BigDecimal	int minimum, maximum, increment, digits;
	BigDecimal minimum, maximum;
	BigDecimal increment = new BigDecimal( 1 );
	int digits = 0;
	public static BigDecimal MIN_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal( -Integer.MAX_VALUE );
	public static BigDecimal MAX_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal( Integer.MAX_VALUE );

	public NullableSpinner(Composite parent, int style) {
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
// 7/7/2010 use null		minimum = 0;
//	7/7/2010 use null	maximum = 9;
//	7/7/2010 use data member default	increment = 1;
//	7/7/2010 use data member default		digits = 0;
	}

	void verify(Event e) 
	{
		try 
		{
			if (e.text==null||e.text.equals("")) return;
// 7/7/2010 Scott Atwell re-wrote   	Integer.parseInt(e.text);
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
/*** 7/7/2010 Scott Atwell re-wrote		
		if (getSelection()==null) {
// 7/7/2010 Scott Atwell (do not want -Integer.MAX_VALUE) 			setSelection(getMinimum());
			if ( getMinimum() != -Integer.MAX_VALUE )
			{
				setSelection(getMinimum());
			}
			else
			{
				setSelection( -getIncrement() );
			}
		}
		else {
			setSelection(getSelection() + getIncrement());
		}
***/		
		BigDecimal tempValue = getValue();
		if ( tempValue == null )
		{
// 7/7/2010 Scott Atwell (do not want -Integer.MAX_VALUE) 			setSelection(getMinimum());
// 7/7/2010			if ( getMinimum() != -Integer.MAX_VALUE )
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
// 7/7/2010			setSelection(getSelection() + getIncrement());
			increment( getIncrement() );
		}
		
		notifyListeners(SWT.Selection, new Event());
	}

	void down() 
	{
/*** 7/7/2010 Scott Atwell re-wrote		
		if (getSelection()==null) {
// 7/7/2010 Scott Atwell (do not want -Integer.MAX_VALUE)			setSelection(getMaximum());
			if ( getMaximum() != Integer.MAX_VALUE )
			{
				setSelection(getMaximum());
			}
			else
			{
				setSelection( getIncrement() );
			}
		}
		else {
			setSelection(getSelection() - getIncrement());
		}
***/
		BigDecimal tempValue = getValue();
		if ( tempValue == null )
		{
// 7/7/2010 Scott Atwell (do not want -Integer.MAX_VALUE)			setSelection(getMaximum());
// 7/7/2010			if ( getMaximum() != Integer.MAX_VALUE )
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
// 7/7/2010			setSelection(getSelection() - getIncrement());
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

/**** 7/7/2010 SWL replaced setSelection() and getSelection() with BigDecimal-based setValue() and getValue()	
	public void setSelection(int selection) {
		if (selection < minimum) {
			selection = minimum;
		} else if (selection > maximum) {
			selection = maximum;
		}
		text.setText(String.valueOf(selection));
		text.selectAll();
		text.setFocus();
	}

	public Integer getSelection() {
		if (text.getText()==null||text.getText().equals("")) return null;
		return Integer.parseInt(text.getText());
	}
****/
	public void setValue(BigDecimal aValue) 
	{
		BigDecimal tempValue = aValue;
		
		if ( ( getMinimum() != null ) && ( tempValue.compareTo( getMinimum() ) < 0 ) )
		{
			tempValue = getMinimum();
		}
		else if ( ( getMaximum() != null ) && ( tempValue.compareTo( getMaximum() ) > 0 ) )
		{
			tempValue = getMaximum();
		} 
			
		text.setText( tempValue.toPlainString() );
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
// note this is what we had to use in SpinnerWidget when getSelection() returned equiv of BigDecimal.unscaledValue()
//	return BigDecimal.valueOf( spinner.getSelection(), spinner.getDigits() );
			BigDecimal tempValue = new BigDecimal( text.getText() );
			BigDecimal tempValueScaled = tempValue.setScale( getDigits(), RoundingMode.HALF_UP );
			if ( ! tempValue.equals( tempValueScaled ) )
			{
				text.setText( tempValueScaled.toPlainString() );
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
		// 7/7/2010 Scott Atwell Moved logic from SpinnerWidget.DoubleSpinnerSelection.widgetSelected()
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
}