package br.com.investtools.fix.atdl.ui.swt.test;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * Text example snippet: verify input (only allow digits)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import br.com.investtools.fix.atdl.ui.swt.widget.FloatTTypeListener;

public class TextTest {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Text text = new Text (shell, SWT.BORDER | SWT.V_SCROLL);
	text.setBounds (10, 10, 200, 200);
	
	text.addVerifyListener(new FloatTTypeListener(new DecimalFormat("0.0"), false, 0.1f, 0.9f));
	
/*	text.addListener (SWT.Verify, new Listener () {
		public void handleEvent (Event e) {
			String text = e.text;
			char [] chars = new char [text.length ()];
			text.getChars (0, chars.length, chars, 0);
			for (int i=0; i<chars.length; i++) {
				if (!('0' <= chars [i] && chars [i] <= '9')) {
					e.doit = false;
					return;
				}
			}
						
			// 
			Text textField = (Text) e.widget;
			String firstHalf = textField.getText().substring(e.start);
			String secondHalf = textField.getText().substring(e.start, textField.getText().length());
			String input = firstHalf + e.text + secondHalf;
			
			if (!"".equals(input)) {
				int value = new Integer(input);
				if ( value < 1 || value > 10000000 )
					e.doit = false;
					return;
			}					

		}
	});*/
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
