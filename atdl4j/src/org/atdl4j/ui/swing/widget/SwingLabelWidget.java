package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.impl.LabelUI;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;
import org.eclipse.swt.widgets.Listener;

public class SwingLabelWidget
		extends LabelUI
		implements SwingWidget<String>
{

	private JLabel label;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public LabelWidget(LabelT control) { super(control); }
	 **/

	public void createWidget(Container parent)
	{
		// label
		label = new JLabel();

		if ( control.getLabel() != null )
		{
			label.setText( control.getLabel() );
		}
		else if ( ControlHelper.getInitValue( control, getAtdl4jConfig() ) != null )
		{
			label.setText( (String) ControlHelper.getInitValue( control, getAtdl4jConfig() ) );
		}
		else
		{
			label.setText( "" );
		}
		
		// tooltip
		if (getTooltip() != null) label.setToolTipText(getTooltip());

		parent.add(label);
	}

	public void generateStateRuleListener(Listener listener)
	{
		// do nothing
	}

	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.add(label);
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel() {
		return getComponents();
	}
	
	public void addListener(SwingListener listener) {
		// do nothing
	}
	
	public void removeListener(SwingListener listener) {
		// do nothing
	}
	
	public void setVisible(boolean visible)
	{
		for (Component control : getComponents()) {
			control.setVisible(visible);
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		for (Component control : getComponents()) {
			control.setEnabled(enabled);
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
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( label != null ) )
		{
			label.setText( (aControlInitValue != null ) ? (String) aControlInitValue : "" );
		}
	}
}
