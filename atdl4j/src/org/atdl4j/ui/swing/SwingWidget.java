
package org.atdl4j.ui.swing;

import java.awt.Container;
import java.awt.Component;
import java.util.List;

import org.atdl4j.ui.ControlUI;

/**
 * An interface for an algorithmic parameter which will be 
 * rendered as an Swing Widget.
 */
public interface SwingWidget<E extends Comparable<?>> extends ControlUI<E> 
{
	public void createWidget(Container parent);
	
	// This gets all child SWT UI controls, do not confuse with ControlT
	public List<Component> getComponents();
		
	// This gets all child SWT UI controls (excluding preceding label), do not confuse with ControlT
	public List<Component> getComponentsExcludingLabel();
		
	public void addListener(SwingListener listener);

	public void removeListener(SwingListener listener);

}