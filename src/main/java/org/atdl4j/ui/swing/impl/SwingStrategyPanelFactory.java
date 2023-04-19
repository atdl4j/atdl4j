package org.atdl4j.ui.swing.impl;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
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
	protected static final Logger logger = LoggerFactory.getLogger( SwingStrategyPanelFactory.class );
	
	private static SwingWidgetFactory swingWidgetFactory = new SwingWidgetFactory();

	// Given a panel, recursively populates a map of Panels and Parameter widgets
	// Can also process options for a group frame instead of a single panel
	public Map<String, SwingWidget<?>> createStrategyPanelAndWidgets(JPanel parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory) throws FIXatdlFormatException
	{
		logger.debug( "createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style) invoked with parms parent: {} panel: {} parameters: {} style: {}", parent, panel, parameters, style );
	
		Map<String, SwingWidget<?>> controlWidgets = new HashMap<>();
	
		// -- Handles StrategyPanel's Collapsible, Title, Border, etc.  Sets its layout and layoutData and data. --
		JPanel c = SwingStrategyPanelHelper.createStrategyPanelContainer( panel, parent, style );
		parent.add(c);
		
		if ( !panel.getStrategyPanel().isEmpty() && !panel.getControl().isEmpty() )
		{
			// -- Wrap each Control with an auto-built StrategyPanel if setting is true --
			if ( aAtdl4jWidgetFactory.getAtdl4jOptions().isAccommodateMixOfStrategyPanelsAndControls() )
			{
				// -- FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file. --
				logger.warn( "StrategyPanel contains both StrategyPanel ({}) and Control ({}) elements.\nSee Atdl4jOptions.setAccommodateMixOfStrategyPanelsAndControls() as potential work-around, though Controls will appear after StrategyPanels.", panel.getStrategyPanel().size(), panel.getControl().size() );

				StrategyPanelT tempPanel = new StrategyPanelT();
				tempPanel.setCollapsible( Boolean.FALSE );
				tempPanel.setCollapsed( Boolean.FALSE );
				tempPanel.setOrientation( panel.getOrientation() );
				tempPanel.setColor( panel.getColor() );
				
				logger.warn( "Creating a StrategyPanel to contain {} Controls.", panel.getControl().size() );
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
			SwingWidget<?> widget = swingWidgetFactory.createWidget( c, control, parameter, style, aAtdl4jWidgetFactory );
			
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
	
		return controlWidgets;
	}
	
	
  /**
   * Display panel's children inside the given parent
   * @param parent
   * @param panel
   * @param style
   * @param widgets
   * @param depth
   * @param gcUpdater
   * @return
   * @throws FIXatdlFormatException
   */
  public JPanel layoutStrategyPanel(JPanel parent, StrategyPanelT panel,
                                    int style,
                                    Map<String, SwingWidget< ? >> widgets,
                                    int depth, ConstraintsUpdater gcUpdater)
      throws FIXatdlFormatException
  {
    GridBagConstraints gc = new GridBagConstraints();

    // Pre-requisite : Given StrategyPanelT parameter should contain
    // either child StrategyPanelTs OR child controls, not both
    int rowIndex = 0;

    // Call recursively on sub-panels
    for (StrategyPanelT sp : panel.getStrategyPanel()) {
      gc = gcUpdater.panel(rowIndex, gc);
      gc.weightx = 1;
      gc.weighty = 1;
      gc.fill = GridBagConstraints.BOTH;
      gc = gcUpdater.remainder(gc);

      // create a container
      JPanel c = SwingStrategyPanelHelper.createStrategyPanelContainer(sp,
                                                                       parent,
                                                                       style);
      c.setLayout(new GridBagLayout());

      // recursive call
      JPanel childPanel = layoutStrategyPanel(c,
                                              sp,
                                              style,
                                              widgets,
                                              depth + 1,
                                              new ConstraintsUpdater(sp
                                                  .getOrientation()));
      parent.add(childPanel, gc);
      rowIndex++;
    }

    gc = gcUpdater.size(1, gc);
    for (ControlT c : panel.getControl()) {
      gc = gcUpdater.panel(rowIndex, gc);
      if (logger.isDebugEnabled())
      {
        logger.debug("Control :{} rowIndex={}", c.getLabel(), rowIndex );
      }

      SwingWidget< ? > swingWidget = widgets.get(c.getID());

      List< ? extends Component> brickComponents = swingWidget
          .getBrickComponents();

      if (!brickComponents.isEmpty()) {

        gc.insets = new Insets(1, 2, 1, 3);

        gc = gcUpdater.fill(gc);
        int size = brickComponents.size();
        int compIndex = 0;

        for (Component comp : brickComponents) {
          if ( ( logger.isDebugEnabled() )
	       && ( comp instanceof JLabel ) )
          {
            logger.debug("Label {}", ((JLabel) comp).getText() );
          }
        
          if (compIndex == (size - 1)) {
            // last component : use remainder space
            gc = gcUpdater.weight(1, gc);
            gc = gcUpdater.remainder(gc);
          }
          else {
            gc = gcUpdater.weight(0, gc);
            gc = gcUpdater.size(1, gc);
          }
          
          gc = gcUpdater.widget(compIndex, gc);
          parent.add(comp, gc);
          compIndex++;
          gc = gcUpdater.relative(gc);
        }
        rowIndex++;
      }
    }
    gc.fill = GridBagConstraints.BOTH;
    gc.weightx = 1;
    gc.weighty = 1;
    
    gc = gcUpdater.panel(rowIndex, gc);
    gc = gcUpdater.remainder(gc);
    parent.add(new JPanel(), gc);

    return parent;
  }
	
  public JPanel createStrategyPanel(JPanel parent, StrategyPanelT panel,
                                    int style,
                                    Map<String, SwingWidget< ? >> widgets,
                                    int depth)
      throws FIXatdlFormatException
  {
    return layoutStrategyPanel(parent, panel, style, widgets, depth, new ConstraintsUpdater(panel.getOrientation()));
  }
	

  static class ConstraintsUpdater
  {

    PanelOrientationT orientation;

    public ConstraintsUpdater(PanelOrientationT orientation) {
      super();
      this.orientation = orientation;
    }

    public GridBagConstraints fill(GridBagConstraints gc) {
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.fill = GridBagConstraints.HORIZONTAL;
      }
      else {
        gc.gridheight = GridBagConstraints.VERTICAL;
      }
      return gc;
    }

    public GridBagConstraints size(int i, GridBagConstraints gc) {
      
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.gridwidth = i;
      }
      else {
        gc.gridheight = i;
      }
      return gc;
    }

    public GridBagConstraints remainder(GridBagConstraints gc) {
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.gridwidth = GridBagConstraints.REMAINDER;
      }
      else {
        gc.gridheight = GridBagConstraints.REMAINDER;
      }
      return gc;
    }

    public GridBagConstraints weight(double w, GridBagConstraints gc) {
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.weightx = w;
      }
      else {
        gc.weighty = w;
      }
      return gc;
    }

    public GridBagConstraints relative(GridBagConstraints gc) {
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.gridy = GridBagConstraints.RELATIVE;
      }
      else {
        gc.gridx = GridBagConstraints.RELATIVE;
      }
      return gc;
    }

    GridBagConstraints panel(int i, GridBagConstraints gc) {
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.gridy = i;
      }
      else {
        gc.gridx = i;
      }
      return gc;
    }

    GridBagConstraints widget(int i, GridBagConstraints gc) {
      if (orientation == PanelOrientationT.VERTICAL) {
        gc.gridx = i;
      }
      else {
        gc.gridy = i;
      }
      return gc;
    }

  }


}
