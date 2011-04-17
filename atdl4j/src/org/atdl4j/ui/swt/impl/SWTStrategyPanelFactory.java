package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;

/**
 * 
 * This class contains the data associated with the <code>SWTStrategyPanelFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 9:05:33 PM)
 * @author Scott Atwell
 */
public class SWTStrategyPanelFactory
{
	protected static final Logger logger = Logger.getLogger( SWTStrategyPanelFactory.class );

	// Given a panel, recursively populates a map of Panels and Parameter widgets
	// Can also process options for a group frame instead of a single panel
	public static Map<String, SWTWidget<?>> createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style, List<ExpandBar> aExpandBarList, Atdl4jWidgetFactory aAtdl4jWidgetFactory) throws Atdl4jClassLoadException
	{
		logger.debug( "createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style)" + " invoked with parms parent: "
				+ parent + " panel: " + panel + " parameters: " + parameters + " style: " + style );
	
		Map<String, SWTWidget<?>> controlWidgets = new HashMap<String, SWTWidget<?>>();
	
		// -- Handles StrategyPanel's Collapsible, Title, Border, etc.  Sets its layout and layoutData and data. --
		Composite c = SWTStrategyPanelHelper.createStrategyPanelContainer( panel, parent, style );
	
		if ( panel.getStrategyPanel().size() > 0 && panel.getControl().size() > 0 )
		{
			// -- Wrap each Control with an auto-built StrategyPanel if setting is true --
			if ( aAtdl4jWidgetFactory.getAtdl4jOptions().isAccommodateMixOfStrategyPanelsAndControls() )
			{
				// -- FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file. --
				logger.warn( "StrategyPanel contains both StrategyPanel (" + panel.getStrategyPanel().size() +") and Control ( " + panel.getControl().size() + " elements.\nSee Atdl4jOptions.setAccommodateMixOfStrategyPanelsAndControls() as potential work-around, though Controls will appear after StrategyPanels." );
				
				StrategyPanelT tempPanel = new StrategyPanelT();
				tempPanel.setCollapsible( Boolean.FALSE );
				tempPanel.setCollapsed( Boolean.FALSE );
				tempPanel.setOrientation( panel.getOrientation() );
				tempPanel.setColor( panel.getColor() );
				
				logger.warn( "Creating a StrategyPanel to contain " + panel.getControl().size() + " Controls." );
				tempPanel.getControl().addAll( panel.getControl() );
				panel.getControl().clear();
				panel.getStrategyPanel().add(  tempPanel );
			}
			else
			{
				// 7/20/2010 -- original behavior:
				throw new IllegalStateException( "StrategyPanel may not contain both StrategyPanel and Control elements." );
			}
		}
	
		// build panels widgets recursively
		for ( StrategyPanelT p : panel.getStrategyPanel() )
		{
			Map<String, SWTWidget<?>> widgets = createStrategyPanelAndWidgets( c, p, parameters, style, aExpandBarList, aAtdl4jWidgetFactory );
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
			SWTWidget<?> widget = SWTWidgetFactory.createWidget( c, control, parameter, style, aAtdl4jWidgetFactory );
			
			widget.setParentStrategyPanel( panel );
			widget.setParent( c );
	
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
	
		if ( c.getParent() instanceof ExpandBar )
		{
			aExpandBarList.add( (ExpandBar)c.getParent() );
		}
		
		return controlWidgets;
	}

}
