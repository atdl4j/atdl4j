package org.atdl4j.ui.swing.widget;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.atdl4j.ui.impl.HiddenFieldUI;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;
import org.eclipse.swt.widgets.Listener;

public class SwingHiddenFieldWidget
		extends HiddenFieldUI
		implements SwingWidget<String>
{
/** 2/9/2010 Scott Atwell	@see AbstractControlUI.init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	public HiddenFieldWidget(HiddenFieldT control, ParameterT parameter) throws JAXBException {
		super(control, parameter);
	}
	 **/

	public void createWidget(JPanel parent)
	{
		// do nothing
	}

	public void generateStateRuleListener(Listener listener)
	{
		// do nothing
	}

	public List<JComponent> getComponents()
	{
		return null;
	}

	public List<JComponent> getComponentsExcludingLabel()
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
	@Override
	public void processReinit( Object aControlInitValue )
	{
		// do nothing
	}
}
