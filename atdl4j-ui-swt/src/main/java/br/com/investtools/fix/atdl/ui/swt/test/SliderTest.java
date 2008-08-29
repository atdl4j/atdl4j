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
 * Scale example snippet: create a scale (maximum 40, page increment 5)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SliderTest {

public static void main (String [] args) {
	
	Display display = new Display ();
	Shell shell = new Shell (display);

	GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = 13;
	gridLayout.makeColumnsEqualWidth = true;
	shell.setLayout(gridLayout);
	
	Scale slider = new Scale (shell, SWT.NONE);
	slider.setMaximum(12);
	slider.setIncrement(1);
	slider.setPageIncrement(1);
	
	GridData sliderData = new GridData();
	sliderData.widthHint = 930;
	sliderData.horizontalSpan = 13;
	slider.setLayoutData(sliderData);
	
	for (int i = 0; i < 13; i++) {
		Label label = new Label(shell, SWT.NONE);
		label.setText("Numbergggggggggggggggg " + Integer.toString(i));
		
		GridData labelData = new GridData();
		//labelData.widthHint = 55;
		labelData.horizontalIndent = 12;
		label.setLayoutData(labelData);
	}   

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
