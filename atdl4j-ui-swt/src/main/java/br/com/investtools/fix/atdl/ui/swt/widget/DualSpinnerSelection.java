package br.com.investtools.fix.atdl.ui.swt.widget;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Spinner;

/**
 * SelectionListener that implements the dual spinner behaviour.
 * 
 */
public class DualSpinnerSelection implements SelectionListener {

	private Spinner spinner;

	private int increment;

	public DualSpinnerSelection(Spinner spinner, int increment) {
		this.spinner = spinner;
		this.increment = increment;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		spinner.setSelection(spinner.getSelection() + increment);
	}

}
