package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.Container;
import java.util.List;

import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;
import org.atdl4j.ui.impl.AbstractHiddenFieldWidget;
import org.eclipse.swt.widgets.Listener;

public class SwingHiddenFieldWidget
		extends AbstractHiddenFieldWidget
		implements SwingWidget<String>
{
/** 2/9/2010 Scott Atwell	@see AbstractControlUI.init(ControlT aControl, ParameterT aParameter) throws JAXBException
	public HiddenFieldWidget(HiddenFieldT control, ParameterT parameter) throws JAXBException {
		super(control, parameter);
	}
	 **/

	public void createWidget(Container parent)
	{
		// do nothing
	}

	public void generateStateRuleListener(Listener listener)
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
}
