
package org.atdl4j.ui.swing;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.atdl4j.ui.ControlUI;

/**
 * An interface for an algorithmic parameter which will be 
 * rendered as an Swing Widget.
 */
public interface SwingWidget<E extends Comparable<?>> extends ControlUI<E> 
{
	public void createWidget(JPanel parent);
	
	// This gets all child SWT UI controls, do not confuse with ControlT
	public List<JComponent> getComponents();
		
	// This gets all child SWT UI controls (excluding preceding label), do not confuse with ControlT
	public List<JComponent> getComponentsExcludingLabel();
		
	public void addListener(SwingListener listener);

	public void removeListener(SwingListener listener);

}