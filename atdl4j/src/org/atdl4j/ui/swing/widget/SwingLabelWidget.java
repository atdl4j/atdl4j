package org.atdl4j.ui.swing.widget;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	public void createWidget(JPanel parent)
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

	public List<JComponent> getComponents() {
		List<JComponent> widgets = new ArrayList<JComponent>();
		widgets.add(label);
		return widgets;
	}

	public List<JComponent> getComponentsExcludingLabel() {
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
		for (JComponent control : getComponents()) {
			control.setVisible(visible);
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		for (JComponent control : getComponents()) {
			control.setEnabled(enabled);
		}
	}

	public boolean isVisible()
	{
		for ( JComponent control : getComponents() )
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
		for ( JComponent control : getComponents() )
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
