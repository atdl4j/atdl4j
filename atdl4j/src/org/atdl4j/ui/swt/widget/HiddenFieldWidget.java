package org.atdl4j.ui.swt.widget;

import java.util.List;

import org.atdl4j.ui.impl.HiddenFieldUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class HiddenFieldWidget
		extends HiddenFieldUI
		implements SWTWidget<String>
{
/** 2/9/2010 Scott Atwell	@see AbstractControlUI.init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	public HiddenFieldWidget(HiddenFieldT control, ParameterT parameter) throws JAXBException {
		super(control, parameter);
	}
	 **/

	public Widget createWidget(Composite parent, int style)
	{
		return null;
	}

	public void generateStateRuleListener(Listener listener)
	{
	}

	public List<Control> getControls()
	{
		return null;
	}

	public List<Control> getControlsExcludingLabel()
	{
		return getControls();
	}

	public void addListener(Listener listener)
	{
		// do nothing
	}

	public void removeListener(Listener listener)
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
