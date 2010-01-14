package org.atdl4j.ui.swt.widget;

import org.atdl4j.ui.impl.AbstractControlUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Control;

/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractSWTWidget<E extends Comparable<?>> extends
		AbstractControlUI<E> implements SWTWidget<E> {

	public void setVisible(boolean visible) {
		for (Control control : getControls()) {
			control.setVisible(visible);
		}
	}

	public void setEnabled(boolean enabled) {
		for (Control control : getControls()) {
			control.setEnabled(enabled);
		}
	}
}