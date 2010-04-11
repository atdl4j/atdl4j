package org.atdl4j.ui.swt.widget;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/*
 * Implements radioGroup attribute for RadioButton. The SWT
 * framework can't handle linking RadioButtons that don't share
 * the same parent container.
 * 
 * @author johnnyshields
 */

public class RadioButtonListener
		implements Listener
{
	private static final Logger logger = Logger.getLogger( RadioButtonListener.class );

	private List<Button> buttons;

	public RadioButtonListener()
	{
		buttons = new Vector<Button>();
	}

	public void addButton(Button button)
	{
		buttons.add( button );
		button.addListener( SWT.Selection, this );
	}

	public void addButton(ButtonWidget buttonWidget)
	{
		addButton( buttonWidget.getButton() );
	}

	public void handleEvent(Event p_event)
	{
		handleEvent( (Button) p_event.widget );
	}

	public void handleEvent(Button aButton)
	{
		for ( Button b : buttons )
		{
			b.setSelection( aButton.equals( b ) );
		}
	}

	/*
	 * public Object getControlValue() { for (Button button : buttons) { if
	 * (button.getSelection()) return new Object(); // need a way to get
	 * value!!!!!!! break; } return null; }
	 */

	/**
	 * If no RadioButtons within the radioGroup are selected, then first one in
	 * list will be selected.
	 */
	public void processReinit()
	{
		Button tempSelectedButton = null;

		for ( Button b : buttons )
		{
			if ( b.getSelection() )
			{
				if ( tempSelectedButton == null )
				{
					tempSelectedButton = b;
				}
				else
				{
					// -- there is already a selected button!! --
					b.setSelection( false );
				}
			}
		}

		// -- Select first in list if no buttons are selected --
		if ( tempSelectedButton == null )
		{
logger.info("Warning: no buttons were selected for RadioButtonListener.  Selecting first button in list: " + buttons.get(0) );
			buttons.get( 0 ).setSelection( true );
		}
	}
}