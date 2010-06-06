package org.atdl4j.ui.swing.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.ControlUIFactory;
import org.atdl4j.ui.swt.SWTPanelFactory;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.SWTWidgetFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;

public class SwingFactory
		implements SWTWidgetFactory, SWTPanelFactory
{
	private static final Logger logger = Logger.getLogger( SwingFactory.class );

	private ControlUIFactory controlWidgetFactory;

	public SwingFactory(Atdl4jConfig aAtdl4jConfig)
	{
		controlWidgetFactory = aAtdl4jConfig.getControlUIFactory();
	}

	// Used to create a single parameter widget
	public SWTWidget<?> createWidget(Composite parent, ControlT control, ParameterT parameter, int style)
	{
		SWTWidget<?> parameterWidget = null;

		logger.debug( "createWidget() invoked " + "with parms parent: " + parent
				+ " control: " + control + " parameter: " + parameter + " style: " + style );

		parameterWidget = (SWTWidget<?>) controlWidgetFactory.create( control, parameter );

		logger.debug( "createWidget() returned parameterWidget: " + parameterWidget );

		parameterWidget.createWidget( parent, style );
		logger.debug( "createWidget() completed.  parameterWidget: " + parameterWidget );

		parameterWidget.applyConstOrInitValues();

		return parameterWidget;
	}

	// Given a panel, recursively populates a map of Panels and Parameter widgets
	// Can also process options for a group frame instead of a single panel
	public Map<String, SWTWidget<?>> createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style)
	{
		logger.debug( "createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style)" + " invoked with parms parent: "
				+ parent + " panel: " + panel + " parameters: " + parameters + " style: " + style );

		Map<String, SWTWidget<?>> controlWidgets = new HashMap<String, SWTWidget<?>>();

		// -- Handles StrategyPanel's Collapsible, Title, Border, etc.  Sets its layout and layoutData and data. --
		Composite c = SwingStrategyPanelHelper.createStrategyPanelContainer( panel, parent, style );


		if ( panel.getStrategyPanel().size() > 0 && panel.getControl().size() > 0 )
			throw new IllegalStateException( "StrategyPanel may not contain both StrategyPanel and Control elements." );

		// build panels widgets recursively
		for ( StrategyPanelT p : panel.getStrategyPanel() )
		{
			Map<String, SWTWidget<?>> widgets = createStrategyPanelAndWidgets( c, p, parameters, style );
			// check for duplicate IDs
			for ( String newID : widgets.keySet() )
			{
				for ( String existingID : controlWidgets.keySet() )
				{
					if ( newID.equals( existingID ) )
						throw new IllegalStateException( "Duplicate Control ID: \"" + newID + "\"" );
				}
			}
			controlWidgets.putAll( widgets );
		}

		// build control widgets recursively
		for ( ControlT control : panel.getControl() )
		{
			ParameterT parameter = null;

			if ( control.getParameterRef() != null )
			{
				parameter = parameters.get( control.getParameterRef() );
				if ( parameter == null )
					throw new IllegalStateException( "Cannot find Parameter \"" + control.getParameterRef() + "\" for Control ID: \"" + control.getID() + "\"" );
			}
			SWTWidget<?> widget = createWidget( c, control, parameter, style );

			// check for duplicate Control IDs
			if ( control.getID() != null )
			{
				// check for duplicate Control IDs
				for ( SWTWidget<?> w : controlWidgets.values() )
				{
					if ( w.getControl().getID().equals( control.getID() ) )
						throw new IllegalStateException( "Duplicate Control ID: \"" + control.getID() + "\"" );
				}
				controlWidgets.put( control.getID(), widget );
			}
			else
			{
				throw new IllegalStateException( "Control Type: \"" + control.getClass().getSimpleName() + "\" is missing ID" );
			}
		}

		
		// -- Force re-sizing (to support non-collapsed, collapsible ExpandBar components) --
		if (c.getParent() instanceof ExpandBar) SwingStrategyPanelHelper.relayoutExpandBar( (ExpandBar)c.getParent(), false );
		
		return controlWidgets;
	}
}
