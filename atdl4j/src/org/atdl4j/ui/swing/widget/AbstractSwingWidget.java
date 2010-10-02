package org.atdl4j.ui.swing.widget;

import java.awt.Component;

import org.atdl4j.ui.swing.SwingWidget;
import org.atdl4j.ui.impl.AbstractAtdl4jWidget;
/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractSwingWidget<E extends Comparable<?>>
		extends AbstractAtdl4jWidget<E>
		implements SwingWidget<E>
{
	public void setVisible(boolean visible)
	{		
		for ( Component control : getComponents() )
		{
			control.setVisible( visible );
		}
	}

	public void setEnabled(boolean enabled)
	{
		for ( Component control : getComponents() )
		{
			control.setEnabled( enabled );
		}
	}

	public boolean isVisible()
	{
		for ( Component control : getComponents() )
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
		for ( Component control : getComponents() )
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
		for ( Component control : getComponentsExcludingLabel() )
		{
			control.setEnabled( enabled );
		}
	}

	public boolean isControlExcludingLabelEnabled()
	{
		for ( Component control : getComponentsExcludingLabel() )
		{
			if ( control.isEnabled() )
			{
				return true;
			}
		}
		
		return false;
	}

}