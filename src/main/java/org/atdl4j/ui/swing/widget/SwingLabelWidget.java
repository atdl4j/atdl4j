package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.atdl4j.ui.impl.AbstractLabelWidget;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;

public class SwingLabelWidget
		extends AbstractLabelWidget
		implements SwingWidget<String>
{

	private JLabel label;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter) throws JAXBException
	 * public LabelWidget(LabelT control) { super(control); }
	 **/

	public void createWidget(JPanel parent)
	{
		// label
		label = new JLabel();
		label.setName(getName()+"/label");

		if ( control.getLabel() != null )
		{
			label.setText( control.getLabel() );
		}
		else if ( ControlHelper.getInitValue(control, getAtdl4jOptions()) != null )
		{
			label.setText( (String) ControlHelper.getInitValue( control, getAtdl4jOptions() ));
		}
		else
		{
			label.setText( "" );
		}
		
		// tooltip
		if (getTooltip() != null) label.setToolTipText(getTooltip());

		parent.add(label);
	}

	public void generateStateRuleListener(SwingListener listener)
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
