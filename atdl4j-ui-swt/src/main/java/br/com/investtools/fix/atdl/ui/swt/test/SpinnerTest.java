package br.com.investtools.fix.atdl.ui.swt.test;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * Spinner example snippet: create and initialize a spinner widget
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import br.com.investtools.fix.atdl.ui.swt.widget.DualSpinnerSelection;

public class SpinnerTest {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		shell.setLayout(gridLayout);
		
		Label l = new Label(shell, SWT.NONE);
		l.setText("lorem ipsum");

		GridData labelData = new GridData();
		labelData.verticalSpan = 2;
		l.setLayoutData(labelData);
		
		
		Spinner spinner = new Spinner (shell, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(1000);
		spinner.setSelection(500);
		spinner.setIncrement(1);
		spinner.setPageIncrement(100);
		
		GridData spinnerData = new GridData();
		spinnerData.verticalSpan = 2;
		spinner.setLayoutData(spinnerData);
		
		Button buttonUp = new Button(shell, SWT.ARROW | SWT.UP);
		Button buttonDown = new Button(shell, SWT.ARROW | SWT.DOWN);
		
		buttonUp.addSelectionListener(new DualSpinnerSelection(spinner, 5));
		buttonDown.addSelectionListener(new DualSpinnerSelection(spinner, -5));
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}