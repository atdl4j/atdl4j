package org.atdl4j.ui.swing.impl;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.swing.SwingWidget;


/**
 * 
 * This class contains the data associated with the <code>SwingStrategyPanelFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 9:05:33 PM)
 * @author Scott Atwell
 */
public class SwingStrategyPanelFactory
{
	protected static final Logger logger = Logger.getLogger( SwingStrategyPanelFactory.class );

	// Given a panel, recursively populates a map of Panels and Parameter widgets
	// Can also process options for a group frame instead of a single panel
	public static Map<String, SwingWidget<?>> createStrategyPanelAndWidgets(JPanel parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory) throws FIXatdlFormatException
	{
		logger.debug( "createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style)" + " invoked with parms parent: "
				+ parent + " panel: " + panel + " parameters: " + parameters + " style: " + style );
	
		Map<String, SwingWidget<?>> controlWidgets = new HashMap<String, SwingWidget<?>>();
	
		// -- Handles StrategyPanel's Collapsible, Title, Border, etc.  Sets its layout and layoutData and data. --
		JPanel c = SwingStrategyPanelHelper.createStrategyPanelContainer( panel, parent, style );
		parent.add(c);
		
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
				throw new FIXatdlFormatException( "StrategyPanel may not contain both StrategyPanel and Control elements." );
			}
		}
	
		// build panels widgets recursively
		for ( StrategyPanelT p : panel.getStrategyPanel() )
		{
			Map<String, SwingWidget<?>> widgets = createStrategyPanelAndWidgets( c, p, parameters, style, aAtdl4jWidgetFactory );
			// check for duplicate IDs
			for ( String newID : widgets.keySet() )
			{
				for ( String existingID : controlWidgets.keySet() )
				{
					if ( newID.equals( existingID ) )
						throw new FIXatdlFormatException( "Duplicate Control ID: \"" + newID + "\"" );
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
					throw new FIXatdlFormatException( "Cannot find Parameter \"" + control.getParameterRef() + "\" for Control ID: \"" + control.getID() + "\"" );
			}
			SwingWidget<?> widget = SwingWidgetFactory.createWidget( c, control, parameter, style, aAtdl4jWidgetFactory );
			
			widget.setParentStrategyPanel( panel );
			widget.setParent( c );
	
			// check for duplicate Control IDs
			if ( control.getID() != null )
			{
				// check for duplicate Control IDs
				for ( SwingWidget<?> w : controlWidgets.values() )
				{
					if ( w.getControl().getID().equals( control.getID() ) )
						throw new FIXatdlFormatException( "Duplicate Control ID: \"" + control.getID() + "\"" );
				}
				controlWidgets.put( control.getID(), widget );
			}
			else
			{
				throw new FIXatdlFormatException( "Control Type: \"" + control.getClass().getSimpleName() + "\" is missing ID" );
			}
		}
	
		SwingStrategyPanelHelper.createStrategyPanelSpringLayout(panel, c);
		
		return controlWidgets;
	}

}
