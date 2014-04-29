package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.atdl4j.ui.impl.AbstractAtdl4jWidget;
import org.atdl4j.ui.swing.SwingWidget;
/**
 * Abstract class that represents a Parameter Swing Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractSwingWidget<E extends Comparable<?>>
		extends AbstractAtdl4jWidget<E>
		implements SwingWidget<E>
{
  
    protected List< ? extends Component> brickComponents;
  
	public void setVisible(boolean visible)
	{		
		for ( Component control : getComponents() )
		{
			if (control != null) {
				control.setVisible( visible );
			}
		}
	}

	public void setEnabled(boolean enabled)
	{
		for ( Component control : getComponents() )
		{
			if (control != null) {
				control.setEnabled( enabled );
			}
		}
	}

	public boolean isVisible()
	{
		for ( Component control : getComponents() )
		{
			if ((control != null) && control.isVisible() )
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
			if ( (control != null) && control.isEnabled() )
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
			if (control != null) {
				control.setEnabled( enabled );
			}
		}
	}

	public boolean isControlExcludingLabelEnabled()
	{
		for ( Component control : getComponentsExcludingLabel() )
		{
			if ( (control != null) && control.isEnabled() )
			{
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List< ? extends Component> getBrickComponents() {
	  if (brickComponents == null)
	  {
	    brickComponents = createBrickComponents();
	  }
	  return brickComponents;
	}

    protected List< ? extends Component> createBrickComponents() {
      return new ArrayList<Component>();
    }
    
    @Override
    public void createWidget(JPanel parent) {
      
      JPanel wrapper = new JPanel(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();
      parent.setLayout(new GridBagLayout());
      List< ? extends Component> components = getBrickComponents();
      boolean lastComponentUsesRemainingSpace = false;
      if  (components.size()>1)
      {
        lastComponentUsesRemainingSpace = true;
      }
      int componentCount = 0;
      for (Component component : components) {
        componentCount++;
        if (componentCount == components.size() && lastComponentUsesRemainingSpace)
        {
          gc.gridwidth = GridBagConstraints.REMAINDER;
          gc.fill = GridBagConstraints.BOTH;
        }
        wrapper.add(component, gc);
      }
      
      parent.add(wrapper);
    }
    
}