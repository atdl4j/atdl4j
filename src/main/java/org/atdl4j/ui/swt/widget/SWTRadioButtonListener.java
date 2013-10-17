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

public class SWTRadioButtonListener
		implements Listener
{
	private static final Logger logger = Logger.getLogger( SWTRadioButtonListener.class );

	private List<Button> buttons;

	public SWTRadioButtonListener()
	{
		buttons = new Vector<Button>();
	}

	public void addButton(Button button)
	{
		buttons.add( button );
		button.addListener( SWT.Selection, this );
	}

	public void addButton(SWTButtonWidget sWTButtonWidget)
	{
		addButton( sWTButtonWidget.getButton() );
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
			logger.info("Warning: no buttons were selected for SWTRadioButtonListener.  Selecting first button in list: " + buttons.get(0) );
			buttons.get( 0 ).setSelection( true );
		}
	}
}