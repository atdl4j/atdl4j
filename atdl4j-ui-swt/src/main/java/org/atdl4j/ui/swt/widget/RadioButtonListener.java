package org.atdl4j.ui.swt.widget;

import java.util.List;
import java.util.Vector;

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

public class RadioButtonListener implements Listener
{
    private List<Button> buttons;

    public RadioButtonListener()
    {
	buttons = new Vector<Button>();
    }
    
    public void addButton(Button button)
    {
	buttons.add(button);
	button.addListener(SWT.Selection, this);
    }

    public void addButton(ButtonWidget buttonWidget)
    {
	addButton(buttonWidget.getButton());
    }

    public void handleEvent(Event p_event)
    {
	Button sender = (Button) p_event.widget;
	for (Button b : buttons)
	{
	    b.setSelection(sender.equals(b));
	}
    }

    /*
    public Object getControlValue()
    {
	for (Button button : buttons)
	{
	    if (button.getSelection())
		return new Object(); // need a way to get value!!!!!!!
	    break;
	}
	return null;
    }*/
}