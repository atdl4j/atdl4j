package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.atdl4j.ui.impl.AbstractHiddenFieldWidget;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;

public class SwingHiddenFieldWidget
		extends AbstractHiddenFieldWidget
		implements SwingWidget<String>
{
    private List<? extends Component> brickComponents;

/** 2/9/2010 Scott Atwell	@see AbstractControlUI.init(ControlT aControl, ParameterT aParameter) throws JAXBException
	public HiddenFieldWidget(HiddenFieldT control, ParameterT parameter) throws JAXBException {
		super(control, parameter);
	}
	 **/

	public void createWidget(JPanel parent)
	{
		// do nothing
	}

	public void generateStateRuleListener(SwingListener listener)
	{
		// do nothing
	}

	public List<Component> getComponents()
	{
		return null;
	}

	public List<Component> getComponentsExcludingLabel()
	{
		return getComponents();
	}

	public void addListener(SwingListener listener)
	{
		// do nothing
	}

	public void removeListener(SwingListener listener)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	public void processReinit( Object aControlInitValue )
	{
		// do nothing
	}

    public List< ? extends Component> createBrickComponents() {
      return new ArrayList<Component>();
    }
  
    @Override
    public List< ? extends Component> getBrickComponents() {
      if (brickComponents == null)
      {
        brickComponents = createBrickComponents();
      }
      return brickComponents;
    }
}
