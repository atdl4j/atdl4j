package org.atdl4j.ui.swt.impl;

import java.util.*;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.validation.Field2OperatorValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.widget.SWTButtonWidget;
import org.atdl4j.ui.swt.widget.SWTRadioButtonListener;
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
	protected static final Logger logger = LoggerFactory.getLogger( SWTStrategyUI.class );

	protected Map<String, SWTWidget<?>> swtWidgetMap;  
	
	Map<String, SWTWidget<?>> swtWidgetWithParameterMap;	
	
	protected Map<String, SWTRadioButtonListener> radioGroupMap;
	
	protected List<SWTStateListener> stateListenerList;
	
	protected Map<SWTWidget<?>, Set<SWTStateListener>> widgetStateListenerMap;
	
	private Composite parent;

	private List<ExpandBar> expandBarList;

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

		setSWTWidgetWithParameterMap( new HashMap<>() );

		setWidgetStateListenerMap( new HashMap<>() );
	}

	protected void buildAtdl4jWidgetMap()
	{
		if ( getStrategy() == null )
		{
			throw new IllegalStateException("Unexpected error: getStrategy() was null.");
		}
		
		if ( getStrategy().getStrategyLayout() == null )
		{
			throw new IllegalStateException("Unexpected error: getStrategy().getStrategyLayout() was null .  (verify  <lay:StrategyLayout>)");
		}
		
		if ( getStrategy().getStrategyLayout().getStrategyPanel() == null )
		{
			throw new IllegalStateException("Unexpected error: getStrategy().getStrategyLayout().getStrategyPanel() was null .  (verify  <lay:StrategyLayout> <lay:StrategyPanel>)");
		}
		
		
		buildAtdl4jWidgetMap( getStrategy().getStrategyLayout().getStrategyPanel() );
	}

	public void initEnd()
	{
	}
	
	
	// Set screen how it should look when in CxlRpl
	public void setCxlReplaceMode(boolean cxlReplaceMode)
	{
		// enable/disable non-mutable parameters
		for ( SWTWidget<?> widget : getSWTWidgetWithParameterMap().values() )
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
	// ensure that each StateListener is only added once to a given widget.
	private void putStateListener(SWTWidget<?> widget, SWTStateListener stateListener)
	{
		if ( !getWidgetStateListenerMap().containsKey( widget ) )
			getWidgetStateListenerMap().put( widget, new HashSet<>() );
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
			SWTWidget<?> targetParameterWidget = getSWTWidgetMap().get( field );
			if ( targetParameterWidget == null )
				throw new IllegalStateException( "Error generating a State Rule => Control: " + field + " does not exist in Strategy: " + getStrategy().getName() );
			putStateListener( targetParameterWidget, stateRuleListener );

			// -- RadioButtonT requires adding all associated RadioButtonTs in the radioGroup --
			if ( targetParameterWidget.getControl() instanceof RadioButtonT
					&& ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() != null
					&& ( ! "".equals( ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() ) ) )
			{
				String rg = ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup();
				for ( SWTWidget<?> widget : getSWTWidgetMap().values() )
				{
					if ( widget.getControl() instanceof RadioButtonT
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
	 * our own map of a specific templatized instance (SWTWidget<?>) of Atdl4jWidget<?> --
	 * @return
	 */
	public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetWithParameterMap()
	{
		if ( getSWTWidgetWithParameterMap() != null )
		{
			return new HashMap<>( getSWTWidgetWithParameterMap()  );
		}
		else
		{
			return null;
		}
	}

	/**
	 * Note invoking this method results in object construction as a result of down-casting 
	 * our own map of a specific templatized instance (SWTWidget<?>) of Atdl4jWidget<?> --
	 * @return
	 */
	public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetMap()
	{
		if ( getSWTWidgetMap() != null )
		{
			return new HashMap<>( getSWTWidgetMap()  );
		}
		else
		{
			return null;
		}
	}
	


	/**
	 * @return the swtWidgetMap
	 */
	public Map<String, SWTWidget<?>> getSWTWidgetMap()
	{
		return this.swtWidgetMap;
	}

	/**
	 * @param aSWTWidgetMap the swtWidgetMap to set
	 */
	protected void setSWTWidgetMap(Map<String, SWTWidget<?>> aSWTWidgetMap)
	{
		this.swtWidgetMap = aSWTWidgetMap;
	}
	
	
	/**
	 * @param aStrategyPanelList
	 * @return
	 */
	protected void buildAtdl4jWidgetMap( List<StrategyPanelT> aStrategyPanelList )
	{
		Map<String, SWTWidget<?>> tempSWTWidgetMap = new HashMap<>();
		
		setExpandBarList( new ArrayList<>() );

		// build panels and widgets recursively
		for ( StrategyPanelT panel : aStrategyPanelList )
		{
			tempSWTWidgetMap.putAll( SWTStrategyPanelFactory.createStrategyPanelAndWidgets( getParent(), panel, getParameterMap(), SWT.NONE, getExpandBarList(), getAtdl4jWidgetFactory() ) );
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
		
		setSWTWidgetMap( tempSWTWidgetMap );
	}

	public void relayoutCollapsibleStrategyPanels()
	{
		// -- avoid lots of 'thrash' during initial build/load of the various Strategy panels containing one or more collapsible panels --		
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
		Map<String, SWTRadioButtonListener> tempRadioGroupMap = new HashMap<>();
		
		for ( SWTWidget<?> widget : getSWTWidgetMap().values() )
		{
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& !Objects.equals(((RadioButtonT) widget.getControl()).getRadioGroup(), ""))
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !tempRadioGroupMap.containsKey( rg ) )
					tempRadioGroupMap.put( rg, new SWTRadioButtonListener() );
				
				if ( widget instanceof SWTButtonWidget )
				{
					tempRadioGroupMap.get( rg ).addButton( (SWTButtonWidget) widget );
					((SWTButtonWidget) widget).setRadioButtonListener( tempRadioGroupMap.get( rg ) );
				}

			}
		}
		
		setRadioGroupMap( tempRadioGroupMap );
	}

	/**
	 * @return the radioGroupMap
	 */
	public Map<String, SWTRadioButtonListener> getRadioGroupMap()
	{
		return this.radioGroupMap;
	}

	/**
	 * @param aRadioGroupMap the radioGroupMap to set
	 */
	protected void setRadioGroupMap(Map<String, SWTRadioButtonListener> aRadioGroupMap)
	{
		this.radioGroupMap = aRadioGroupMap;
	}

	protected void addToAtdl4jWidgetMap( String aName, Atdl4jWidget<?> aAtdl4jWidget )
	{
		getSWTWidgetMap().put( aName, (SWTWidget<?>) aAtdl4jWidget );
	}

	protected void addToAtdl4jWidgetWithParameterMap( String aName, Atdl4jWidget<?> aAtdl4jWidget )
	{
		getSWTWidgetWithParameterMap().put( aName, (SWTWidget<?>) aAtdl4jWidget );
	}

	protected void removeFromAtdl4jWidgetMap( String aName )
	{
		getSWTWidgetMap().remove( aName );
	}

	protected void removeFromAtdl4jWidgetWithParameterMap( String aName )
	{
		getSWTWidgetWithParameterMap().remove( aName );
	}

	/**
	 * 
	 */
	protected void buildAtdl4jWidgetWithParameterMap()
	{
		Map<String, SWTWidget<?>> tempSWTWidgetWithParameterMap = new HashMap<>();
		
		// loop through all UI controls
		for ( SWTWidget<?> widget : getSWTWidgetMap().values() )
		{
			if ( widget.getParameter() != null )
			{
				// validate that a parameter is not being added twice
				String tempParameterName = widget.getParameter().getName();
				if ( tempSWTWidgetWithParameterMap.containsKey( tempParameterName ) )
				{
					throw new IllegalStateException( "Cannot add parameter \"" + tempParameterName + "\" to two separate controls." );
				}
				tempSWTWidgetWithParameterMap.put( tempParameterName, widget );
			}
			
		}
		
		setSWTWidgetWithParameterMap( tempSWTWidgetWithParameterMap );
	}

	/**
	 * @throws FIXatdlFormatException 
	 */
	protected void attachGlobalStateRulesToControls() throws FIXatdlFormatException
	{
		List<SWTStateListener> tempStateListenerList = new ArrayList<>();
		
		// loop through all UI controls
		for ( SWTWidget<?> widget : getSWTWidgetMap().values() )
		{
			// parameter state rules that have an id should be included in
			// the rules map
			ControlT control = widget.getControl();
	
			if ( control.getStateRule() != null )
			{
				for ( StateRuleT stateRule : control.getStateRule() )
				{
	
					SWTWidget<?> affectedWidget = getSWTWidgetMap().get( control.getID() );
					SWTStateListener stateListener = new SWTStateListener( affectedWidget, stateRule, getSWTWidgetMap(), getCompleteValidationRuleMap() );
	
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
	 * @return the swtWidgetWithParameterMap
	 */
	public Map<String, SWTWidget<?>> getSWTWidgetWithParameterMap()
	{
		return this.swtWidgetWithParameterMap;
	}

	/**
	 * @param aSWTWidgetWithParameterMap the swtWidgetWithParameterMap to set
	 */
	protected void setSWTWidgetWithParameterMap(Map<String, SWTWidget<?>> aSWTWidgetWithParameterMap)
	{
		this.swtWidgetWithParameterMap = aSWTWidgetWithParameterMap;
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
	protected void	attachStateListenersToAllAtdl4jWidgets()
	{
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

	protected void fireStateListeners()
	{
		// fire state listeners once for good measure
		for ( SWTStateListener stateListener : getStateListenerList() )
			stateListener.handleEvent( null );
	}

	protected void fireStateListenersForAtdl4jWidget( Atdl4jWidget<?> aAtdl4jWidget )
	{
		for ( SWTStateListener stateListener : getStateListenerList() )
		{
			if ( aAtdl4jWidget.equals( stateListener.getAffectedWidget() ) )
			{
				stateListener.handleEvent( null );
			}
		}
	}

	/**
	 * Invokes SWTStateListener.handleLoadFixMessageEvent() for aAtdl4jWidget
	 * @param aAtdl4jWidget
	 */
	protected void fireLoadFixMessageStateListenersForAtdl4jWidget( Atdl4jWidget<?> aAtdl4jWidget )
	{
		for ( SWTStateListener stateListener : getStateListenerList() )
		{
			if ( aAtdl4jWidget.equals( stateListener.getAffectedWidget() ) )
			{
				stateListener.handleLoadFixMessageEvent( null );
			}
		}
	}

	/* If no RadioButtons within a radioGroup are selected, then first one in list will be selected
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#applyRadioGroupRules()
	 */
	protected void applyRadioGroupRules()
	{
		if ( getRadioGroupMap() != null )
		{
			for ( SWTRadioButtonListener tempSWTRadioButtonListener : getRadioGroupMap().values() )
			{
				// -- If no RadioButtons within the radioGroup are selected, then first one in list will be selected --
				tempSWTRadioButtonListener.processReinit();
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
