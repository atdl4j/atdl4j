
package org.atdl4j.ui.swt;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.ui.ControlUI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * An interface for an algorithmic parameter which will be 
 * rendered as an SWT Widget.
 */
public interface SWTWidget<E extends Comparable<?>> extends ControlUI<E> {
	
	public Widget createWidget(Composite parent, int style) throws JAXBException;
	
	// This gets all child SWT UI controls, do not confuse with ControlT
	public List<Control> getControls();
		
	public void generateStateRuleListener(Listener listener);

	public void addListener(Listener listener);

	public void removeListener(Listener listener);

}