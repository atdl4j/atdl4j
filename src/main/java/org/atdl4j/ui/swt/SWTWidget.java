
package org.atdl4j.ui.swt;

import java.util.List;

import org.atdl4j.ui.Atdl4jWidget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * An interface for an algorithmic parameter which will be 
 * rendered as an SWT Widget.
 */
public interface SWTWidget<E extends Comparable<?>> extends Atdl4jWidget<E> 
{
	public Widget createWidget(Composite parent, int style);
	
	// This gets all child SWT UI controls, do not confuse with ControlT
	public List<Control> getControls();
		
	// This gets all child SWT UI controls (excluding preceding label), do not confuse with ControlT
	public List<Control> getControlsExcludingLabel();
		
	public void addListener(Listener listener);

	public void removeListener(Listener listener);

}