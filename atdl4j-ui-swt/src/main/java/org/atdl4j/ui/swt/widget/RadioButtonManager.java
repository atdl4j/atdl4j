package org.atdl4j.ui.swt.widget;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/*
 * THIS CLASS IS NOT USED CURRENTLY. I've included the code as a stub; in a future
 * version of ATDL there should be a way to create RadioButton groups and we'll
 * need a class like this to create a group of them (Java Swing does this natively;
 * SWT requires a class like below).
 */

// TODO: should this class extend listener???
public class RadioButtonManager {

	private List<Button> radioButtons;
	private Listener radioGroupListener;

	public RadioButtonManager() {
		radioGroupListener = new Listener() {

			public void handleEvent(Event p_event) {

				Button sender = (Button) p_event.widget;
				for (Button button : radioButtons) {
					if (sender.equals(button))
						button.setSelection(true);
					else
						button.setSelection(false);
				}
			}
		};
	}

	public void addButton(Button button) {
		radioButtons.add(button);
		button.addListener(SWT.Selection, radioGroupListener);
	}

	public void addButton(ButtonWidget buttonWidget) {
		// Button button = buttonWidget.getButton();
		// radioButtons.add(button);
		// button.addListener(SWT.Selection, radioGroupListener);
	}

	public Object getControlValue() {
		for (Button button : radioButtons) {
			if (button.getSelection())
				return new Object(); // need a way to get value!!!!!!!
			break;
		}
		return null;
	}
}