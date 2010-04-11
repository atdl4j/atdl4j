// Taken from http://www.java-forums.org/swt/9902-how-use-swt-spinner.html and modified.

package org.atdl4j.ui.swt.util;

import java.awt.Color;

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
	int minimum, maximum, increment, digits;

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
		minimum = 0;
		maximum = 9;
		increment = 1;
		digits = 0;
	}

	void verify(Event e) {
		try {
			if (e.text==null||e.text.equals("")) return;
			Integer.parseInt(e.text);
		} catch (NumberFormatException ex) {
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
	
	void up() {
		if (getSelection()==null) {
			setSelection(getMinimum());
		}
		else {
			setSelection(getSelection() + getIncrement());
		}
		notifyListeners(SWT.Selection, new Event());
	}

	void down() {
		if (getSelection()==null) {
			setSelection(getMaximum());
		}
		else {
			setSelection(getSelection() - getIncrement());
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

	public void setMaximum(int maximum) {
		checkWidget();
		this.maximum = maximum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMinimum() {
		return minimum;
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

	public void setIncrement(int anIncrement) {
		increment = anIncrement;
	}
	
	public int getIncrement() {
		return increment;	
	}
	
	public void setDigits(int aDigits) {
		digits = aDigits;
	}
	
	public int getDigits() {
		return digits;
	}
}