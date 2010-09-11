package org.atdl4j.ui.swing.impl;

import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.ControlUIFactory;
import org.atdl4j.ui.swing.SwingPanelFactory;
import org.atdl4j.ui.swing.SwingWidget;
import org.atdl4j.ui.swing.SwingWidgetFactory;

public class SwingFactory
		implements SwingWidgetFactory, SwingPanelFactory
{
	private static final Logger logger = Logger.getLogger( SwingFactory.class );

	private ControlUIFactory controlWidgetFactory;

	public SwingFactory(Atdl4jConfig aAtdl4jConfig)
	{
		controlWidgetFactory = aAtdl4jConfig.getControlUIFactory();
	}

	// Used to create a single parameter widget
	public SwingWidget<?> createWidget(Container parent, ControlT control, ParameterT parameter)
	{
	    SwingWidget<?> parameterWidget = null;

		logger.debug( "createWidget() invoked " + "with params parent: " + parent
				+ " control: " + control + " parameter: " + parameter);
		parameterWidget = (SwingWidget<?>) controlWidgetFactory.create( control, parameter );

		logger.debug( "createWidget() returned parameterWidget: " + parameterWidget );
		parameterWidget.createWidget(parent);
		
		logger.debug( "createWidget() completed.  parameterWidget: " + parameterWidget );
		parameterWidget.applyConstOrInitValues();

		return parameterWidget;
	}

	// Given a panel, recursively populates a map of Panels and Parameter widgets
	// Can also process options for a group frame instead of a single panel
	public Map<String, SwingWidget<?>> createStrategyPanelAndWidgets(Container parent, StrategyPanelT panel, Map<String, ParameterT> parameters)
	{
		logger.debug( "createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters)" + " invoked with parms parent: "
				+ parent + " panel: " + panel + " parameters: " + parameters);

		Map<String, SwingWidget<?>> controlWidgets = new HashMap<String, SwingWidget<?>>();

		// -- Handles StrategyPanel's Collapsible, Title, Border, etc.  Sets its layout and layoutData and data. --
		Container c = SwingStrategyPanelHelper.createStrategyPanelContainer( panel, parent );


		if ( panel.getStrategyPanel().size() > 0 && panel.getControl().size() > 0 )
			throw new IllegalStateException( "StrategyPanel may not contain both StrategyPanel and Control elements." );

		// build panels widgets recursively
		for ( StrategyPanelT p : panel.getStrategyPanel() )
		{
			Map<String, SwingWidget<?>> widgets = createStrategyPanelAndWidgets( c, p, parameters );
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
			SwingWidget<?> widget = createWidget( c, control, parameter );

			// check for duplicate Control IDs
			if ( control.getID() != null )
			{
				// check for duplicate Control IDs
				for ( SwingWidget<?> w : controlWidgets.values() )
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
	//	if (c.getParent() instanceof ExpandBar) SwingStrategyPanelHelper.relayoutExpandBar( (ExpandBar)c.getParent(), false );
		
		return controlWidgets;
	}
}
