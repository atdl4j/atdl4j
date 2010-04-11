package org.atdl4j.ui.swt.widget;

import org.atdl4j.ui.impl.AbstractControlUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Control;

/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractSWTWidget<E extends Comparable<?>>
		extends AbstractControlUI<E>
		implements SWTWidget<E>
{
	public void setVisible(boolean visible)
	{
		for ( Control control : getControls() )
		{
			control.setVisible( visible );
		}
	}

	public void setEnabled(boolean enabled)
	{
		for ( Control control : getControls() )
		{
			control.setEnabled( enabled );
		}
	}

	public boolean isVisible()
	{
		for ( Control control : getControls() )
		{
			if ( control.isVisible() )
			{
				return true;
			}
		}
		
		return false;
	}

	public boolean isEnabled()
	{
		for ( Control control : getControls() )
		{
			if ( control.isEnabled() )
			{
				return true;
			}
		}
		
		return false;
	}

	public void setControlExcludingLabelEnabled(boolean enabled)
	{
		for ( Control control : getControlsExcludingLabel() )
		{
			control.setEnabled( enabled );
		}
	}

	public boolean isControlExcludingLabelEnabled()
	{
		for ( Control control : getControlsExcludingLabel() )
		{
			if ( control.isEnabled() )
			{
				return true;
			}
		}
		
		return false;
	}

}