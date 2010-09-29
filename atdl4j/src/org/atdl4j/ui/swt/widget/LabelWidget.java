package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.impl.LabelUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class LabelWidget
		extends LabelUI
		implements SWTWidget<String>
{

	private Label label;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jOptions aAtdl4jOptions) throws JAXBException
	 * public LabelWidget(LabelT control) { super(control); }
	 **/

	public Widget createWidget(Composite parent, int style)
	{

		// label
		label = new Label( parent, SWT.NONE );

		if ( control.getLabel() != null )
		{
			label.setText( control.getLabel() );
		}
		else if ( ControlHelper.getInitValue( control, getAtdl4jOptions() ) != null )
		{
			label.setText( (String) ControlHelper.getInitValue( control, getAtdl4jOptions() ) );
		}
		else
		{
			label.setText( "" );
		}
		GridData gd = new GridData( SWT.LEFT, SWT.TOP, false, false );
		gd.horizontalSpan = 2;
		label.setLayoutData( gd );

		// tooltip
		String tooltip = getTooltip();
		if ( tooltip != null )
			label.setToolTipText( tooltip );

		return parent;
	}

	public void generateStateRuleListener(Listener listener)
	{
		// do nothing
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.add( label );
		return widgets;
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
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( label != null ) && ( ! label.isDisposed() ) )
		{
			label.setText( (aControlInitValue != null ) ? (String) aControlInitValue : "" );
		}
	}
}
