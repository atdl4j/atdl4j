package org.atdl4j.ui.swt.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.validation.Field2OperatorValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.widget.ButtonWidget;
import org.atdl4j.ui.swt.widget.RadioButtonListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;

/**
 * SWT-specific UI representation for a Strategy object.
 * 
 */
public class SWTStrategyUI
		extends AbstractStrategyUI
{
	protected static final Logger logger = Logger.getLogger( SWTStrategyUI.class );

	protected Map<String, SWTWidget<?>> controlMap;  // formerly:  controls
	
	Map<String, SWTWidget<?>> controlWithParameterMap;	// formerly:  controlWithParameter
	
	protected Map<String, RadioButtonListener> radioGroupMap;	// formerly:  radioGroups
	
	protected List<SWTStateListener> stateListenerList;	// formerly:  stateListeners
	
	protected Map<SWTWidget<?>, Set<SWTStateListener>> widgetStateListenerMap; 	// formerly:  widgetStateListeners
	
	private Composite parent;

	private SWTFactory controlFactory;  // note this is lazy-init'd (adjust getControlFactory() if you wish to override/substitute concrete class)

	private List<ExpandBar> expandBarList;  // 8/27/2010 Scott Atwell added

	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SWTStrategyUI()
	{
	}


	/**
	 * @param parentContainer (should be swt.Composite)
	 */
	public void initBegin(Object parentContainer)
	{
		setParent( (Composite) parentContainer );

		setControlWithParameterMap( new HashMap<String, SWTWidget<?>>() );
		
//TODO !!! verify, as it does not appear that getWidgetStateListenerMap() is ever being populated (though, is being initialized)		
		setWidgetStateListenerMap( new HashMap<SWTWidget<?>, Set<SWTStateListener>>() );
	}

	protected void buildControlMap()
	{
		if ( getStrategy() == null )
		{
			throw new IllegalStateException("Unexpected error: getStrategy() was null.");
		}
		
		if ( getStrategy().getStrategyLayout() == null )
		{
			throw new IllegalStateException("Unexpected error: getStrategy().getStrategyLayout() was null .  (verify  <lay:StrategyLayout>)");
		}
		
		if ( getStrategy().getStrategyLayout() == null )
		{
			throw new IllegalStateException("Unexpected error: getStrategy().getStrategyLayout().getStrategyPanel() was null .  (verify  <lay:StrategyLayout> <lay:StrategyPanel>)");
		}
		
		
		buildControlMap( getStrategy().getStrategyLayout().getStrategyPanel() );
	}

	public void initEnd()
	{
	}
	
	
	// Set screen how it should look when in CxlRpl
	public void setCxlReplaceMode(boolean cxlReplaceMode)
	{
		// enable/disable non-mutable parameters
		for ( SWTWidget<?> widget : getControlWithParameterMap().values() )
		{
			if ( !widget.getParameter().isMutableOnCxlRpl() )
				widget.setEnabled( !cxlReplaceMode );
		}

		// set all CxlRpl on all state listeners and fire
		// once for good measure
		for ( SWTStateListener stateListener : getStateListenerList() )
		{
			stateListener.setCxlReplaceMode( cxlReplaceMode );
			stateListener.handleEvent( null );
		}
	}

	// Create a map of StateListeners to be added to each widget. This is to
	// ensure
	// that each StateListener is only added once to a given widget.
	private void putStateListener(SWTWidget<?> widget, SWTStateListener stateListener)
	{
		if ( !getWidgetStateListenerMap().containsKey( widget ) )
			getWidgetStateListenerMap().put( widget, new HashSet<SWTStateListener>() );
		if ( !getWidgetStateListenerMap().get( widget ).contains( stateListener ) )
			getWidgetStateListenerMap().get( widget ).add( stateListener );
	}

	private void attachRuleToControls(ValidationRule rule, SWTStateListener stateRuleListener)
	{
		if ( rule instanceof LogicalOperatorValidationRule )
		{
			for ( ValidationRule innerRule : ( (LogicalOperatorValidationRule) rule ).getRules() )
			{
				attachRuleToControls( innerRule, stateRuleListener );
			}
		}
		else if ( rule instanceof ValueOperatorValidationRule )
		{
			attachFieldToControls( ( (ValueOperatorValidationRule) rule ).getField(), stateRuleListener );
		}
		else if ( rule instanceof Field2OperatorValidationRule )
		{
			attachFieldToControls( ( (Field2OperatorValidationRule) rule ).getField(), stateRuleListener );
			attachFieldToControls( ( (Field2OperatorValidationRule) rule ).getField2(), stateRuleListener );
		}
	}

	private void attachFieldToControls(String field, SWTStateListener stateRuleListener)
	{
		if ( field != null )
		{
			SWTWidget<?> targetParameterWidget = getControlMap().get( field );
			if ( targetParameterWidget == null )
				throw new IllegalStateException( "Error generating a State Rule => Control: " + field + " does not exist in Strategy: " + getStrategy().getName() );
			putStateListener( targetParameterWidget, stateRuleListener );

			// 2/1/2010 John Shields added
			// RadioButtonT requires adding all associated RadioButtonTs in the
			// ratioGroup
			if ( targetParameterWidget.getControl() instanceof RadioButtonT
					&& ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() != null
					&& ( ! "".equals( ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() ) ) )
			{
				String rg = ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup();
				for ( SWTWidget<?> widget : getControlMap().values() )
				{
					if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
							&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
							&& ( ! "".equals( ( (RadioButtonT) widget.getControl() ).getRadioGroup() ) )
							&& ( (RadioButtonT) widget.getControl() ).getRadioGroup().equals( rg ) )
					{
						putStateListener( widget, stateRuleListener );
					}
				}
			}
		}
	}


	/**
	 * Note invoking this method results in object construction as a result of down-casting 
	 * our own map of a specific templatized instance (SWTWidget<?>) of ControlUI<?> --
	 * @return
	 */
	public Map<String, ControlUI<?>> getControlUIWithParameterMap()
	{
		// return new HashMap<String,ControlUI<?>>(getControlMap());
		if ( getControlWithParameterMap() != null )
		{
			return new HashMap<String, ControlUI<?>>( getControlWithParameterMap()  );
		}
		else
		{
			return null;
		}
	}

	/**
	 * Note invoking this method results in object construction as a result of down-casting 
	 * our own map of a specific templatized instance (SWTWidget<?>) of ControlUI<?> --
	 * @return
	 */
	public Map<String, ControlUI<?>> getControlUIMap()
	{
		// return new HashMap<String,ControlUI<?>>(getControlMap());
		if ( getControlMap() != null )
		{
			return new HashMap<String, ControlUI<?>>( getControlMap()  );
		}
		else
		{
			return null;
		}
	}
	


	/**
	 * @return the controlMap
	 */
	public Map<String, SWTWidget<?>> getControlMap()
	{
		return this.controlMap;
	}

	/**
	 * @param aControlMap the controlMap to set
	 */
	protected void setControlMap(Map<String, SWTWidget<?>> aControlMap)
	{
		this.controlMap = aControlMap;
	}
	
	
	/**
	 * @param aStrategyPanelList
	 * @return
	 */
	protected void buildControlMap( List<StrategyPanelT> aStrategyPanelList )
	{
		Map<String, SWTWidget<?>> tempControlMap = new HashMap<String, SWTWidget<?>>();
		
// 8/24/2010 Scott Atwell		
		setExpandBarList( new ArrayList<ExpandBar>() );

		// build panels and widgets recursively
		for ( StrategyPanelT panel : aStrategyPanelList )
		{
//	8/24/2010 Scott Atwell		tempControlMap.putAll( getControlFactory().createStrategyPanelAndWidgets( getParent(), panel, getParameterMap(), SWT.NONE ) );
			tempControlMap.putAll( getControlFactory().createStrategyPanelAndWidgets( getParent(), panel, getParameterMap(), SWT.NONE, getExpandBarList()	) );
		}
	
		// 3/13/2010 John Shields HACK: make the first panel take the full width of the window
		Composite c = getParent();
		for ( Control child : c.getChildren() )
		{
	        if (child instanceof Composite) 
	        {
	      	  Composite composite = (Composite) child;
	      	  composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
	        }
	    }
		
		setControlMap( tempControlMap );
	}

	public void relayoutCollapsibleStrategyPanels()
	{
// 8/27/2010 Scott Atwell added (to avoid lots of 'thrash' during initial build/load of the various Strategy panels containing one or more collapsible panels --		
		for ( ExpandBar tempExpandBar : getExpandBarList() )
		{
			// -- Force re-sizing (to support non-collapsed, collapsible ExpandBar components) --
			if ( tempExpandBar.getItem( 0 ).getExpanded() )
			{
				SWTStrategyPanelHelper.relayoutExpandBar( tempExpandBar, false );
			}
		}
	}

	protected void createRadioGroups()
	{
		Map<String, RadioButtonListener> tempRadioGroupMap = new HashMap<String, RadioButtonListener>();
		
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != "" )
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !tempRadioGroupMap.containsKey( rg ) )
					tempRadioGroupMap.put( rg, new RadioButtonListener() );
				
				if ( widget instanceof ButtonWidget )
				{
					tempRadioGroupMap.get( rg ).addButton( (ButtonWidget) widget );
					((ButtonWidget) widget).setRadioButtonListener( tempRadioGroupMap.get( rg ) );
				}

			}
		}
		
		setRadioGroupMap( tempRadioGroupMap );
	}

	/**
	 * @return the radioGroupMap
	 */
	public Map<String, RadioButtonListener> getRadioGroupMap()
	{
		return this.radioGroupMap;
	}

	/**
	 * @param aRadioGroupMap the radioGroupMap to set
	 */
	protected void setRadioGroupMap(Map<String, RadioButtonListener> aRadioGroupMap)
	{
		this.radioGroupMap = aRadioGroupMap;
	}

	protected void addToControlMap( String aName, ControlUI aControlUI )
	{
		getControlMap().put( aName, (SWTWidget<?>) aControlUI );
	}

	protected void addToControlWithParameterMap( String aName, ControlUI aControlUI )
	{
		getControlWithParameterMap().put( aName, (SWTWidget<?>) aControlUI );
	}

	protected void removeFromControlMap( String aName )
	{
		getControlMap().remove( aName );
	}

	protected void removeFromControlWithParameterMap( String aName )
	{
		getControlWithParameterMap().remove( aName );
	}

	/**
	 * 
	 */
	protected void buildControlWithParameterMap()
	{
		Map<String, SWTWidget<?>> tempControlWithParameterMap = new HashMap<String, SWTWidget<?>>();
		
		// loop through all UI controls
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			if ( widget.getParameter() != null )
			{
				// validate that a parameter is not being added twice
				String tempParameterName = widget.getParameter().getName();
				if ( tempControlWithParameterMap.containsKey( tempParameterName ) )
				{
					throw new IllegalStateException( "Cannot add parameter \"" + tempParameterName + "\" to two separate controls." );
				}
				tempControlWithParameterMap.put( tempParameterName, widget );
			}
			
		}
		
		setControlWithParameterMap( tempControlWithParameterMap );
	}

	/**
	 */
	protected void attachGlobalStateRulesToControls()
	{
		List<SWTStateListener> tempStateListenerList = new Vector<SWTStateListener>();
		
		// loop through all UI controls
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			// parameter state rules that have an id should be included in
			// the rules map
			ControlT control = widget.getControl();
	
			if ( control.getStateRule() != null )
			{
				for ( StateRuleT stateRule : control.getStateRule() )
				{
	
					SWTWidget<?> affectedWidget = getControlMap().get( control.getID() );
					SWTStateListener stateListener = new SWTStateListener( affectedWidget, stateRule, getControlMap(), getCompleteValidationRuleMap() );
	
					// attach the stateListener's rule to controls
					attachRuleToControls( stateListener.getRule(), stateListener );
	
					tempStateListenerList.add( stateListener );
	
					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
					stateListener.handleEvent( null );
				}
			}
		}
		
		setStateListenerList( tempStateListenerList );
	}	
	
	/**
	 * @return the controlWithParameterMap
	 */
	public Map<String, SWTWidget<?>> getControlWithParameterMap()
	{
		return this.controlWithParameterMap;
	}

	/**
	 * @param aControlWithParameterMap the controlWithParameterMap to set
	 */
	protected void setControlWithParameterMap(Map<String, SWTWidget<?>> aControlWithParameterMap)
	{
		this.controlWithParameterMap = aControlWithParameterMap;
	}

	/**
	 * @return the stateListenerList
	 */
	protected List<SWTStateListener> getStateListenerList()
	{
		return this.stateListenerList;
	}

	/**
	 * @param aStateListenerList the stateListenerList to set
	 */
	protected void setStateListenerList(List<SWTStateListener> aStateListenerList)
	{
		this.stateListenerList = aStateListenerList;
	}

	/**
	 * @return the widgetStateListenerMap
	 */
	protected Map<SWTWidget<?>, Set<SWTStateListener>> getWidgetStateListenerMap()
	{
		return this.widgetStateListenerMap;
	}

	/**
	 * @param aWidgetStateListenerMap the widgetStateListenerMap to set
	 */
	protected void setWidgetStateListenerMap(Map<SWTWidget<?>, Set<SWTStateListener>> aWidgetStateListenerMap)
	{
		this.widgetStateListenerMap = aWidgetStateListenerMap;
	}

	
	/**
	 * 
	 */
	protected void	attachStateListenersToAllControls()
	{
//TODO !!! verify, as it does not appear that getWidgetStateListenerMap() is ever being populated (though, is being initialized)		
		// add all StateListeners to the controls
		for ( Entry<SWTWidget<?>, Set<SWTStateListener>> entry : getWidgetStateListenerMap().entrySet() )
		{
			for ( SWTStateListener listener : entry.getValue() )
				entry.getKey().addListener( listener );
		}
	}

	/**
	 * @return the parent
	 */
	public Composite getParent()
	{
		return this.parent;
	}

	/**
	 * @param aParent the parent to set
	 */
	protected void setParent(Composite aParent)
	{
		this.parent = aParent;
	}

	/**
	 * Lazy-init'd
	 * 
	 * @return the controlFactory
	 */
	public SWTFactory getControlFactory() 
	{
		if ( this.controlFactory == null )
		{
			this.controlFactory = new SWTFactory( getAtdl4jConfig() );
		}
		return this.controlFactory;
	}

	/**
	 * @param aControlFactory the controlFactory to set
	 */
	protected void setControlFactory(SWTFactory aControlFactory)
	{
		this.controlFactory = aControlFactory;
	}
	
	protected void fireStateListeners()
	{
		// fire state listeners once for good measure
		for ( SWTStateListener stateListener : getStateListenerList() )
			stateListener.handleEvent( null );
	}

	protected void fireStateListenersForControl( ControlUI aControl )
	{
		for ( SWTStateListener stateListener : getStateListenerList() )
		{
			if ( aControl.equals( stateListener.getAffectedWidget() ) )
			{
				stateListener.handleEvent( null );
			}
		}
	}

	/**
	 * Invokes SWTStateListener.handleLoadFixMessageEvent() for aControl
	 * @param aControl
	 */
	protected void fireLoadFixMessageStateListenersForControl( ControlUI aControl )
	{
		for ( SWTStateListener stateListener : getStateListenerList() )
		{
			stateListener.handleLoadFixMessageEvent( null );
		}
	}

	/* If no RadioButtons within a radioGroup are selected, then first one in list will be selected
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#applyRadioGroupRules()
	 */
	protected void applyRadioGroupRules()
	{
		if ( getRadioGroupMap() != null )
		{
			for ( RadioButtonListener tempRadioButtonListener : getRadioGroupMap().values() )
			{
				// -- If no RadioButtons within the radioGroup are selected, then first one in list will be selected --
				tempRadioButtonListener.processReinit();
			}
		}
	}


	/**
	 * @return the expandBarList
	 */
	protected List<ExpandBar> getExpandBarList()
	{
		return this.expandBarList;
	}


	/**
	 * @param aExpandBarList the expandBarList to set
	 */
	protected void setExpandBarList(List<ExpandBar> aExpandBarList)
	{
		this.expandBarList = aExpandBarList;
	}
}
