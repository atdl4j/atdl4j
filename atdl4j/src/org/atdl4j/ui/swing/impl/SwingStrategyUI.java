package org.atdl4j.ui.swing.impl;

import java.awt.Component;
import java.awt.Container;
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
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;
/**
 * Swing-specific UI representation for a Strategy object.
 * 
 */
public class SwingStrategyUI
extends AbstractStrategyUI
{
    protected static final Logger logger = Logger.getLogger( SwingStrategyUI.class );

    protected Map<String, SwingWidget<?>> controlMap;  // formerly:  controls

    Map<String, SwingWidget<?>> controlWithParameterMap;	// formerly:  controlWithParameter

    //	protected Map<String, SwingRadioButtonListener> radioGroupMap;	// formerly:  radioGroups

    protected List<SwingListener> stateListenerList;	// formerly:  stateListeners

    protected Map<SwingWidget<?>, Set<SwingListener>> widgetStateListenerMap; 	// formerly:  widgetStateListeners

    private Container parent;

    private SwingFactory controlFactory;  // note this is lazy-init'd (adjust getControlFactory() if you wish to override/substitute concrete class)


    /*
     * Call init() after invoking the no arg constructor
     */
    public SwingStrategyUI()
    {
    }


    /**
     * @param parentContainer (should be swt.Container)
     */
    public void initBegin(Object parentContainer)
    {
	setParent( (Container) parentContainer );

	setControlWithParameterMap( new HashMap<String, SwingWidget<?>>() );

	//TODO !!! verify, as it does not appear that getWidgetStateListenerMap() is ever being populated (though, is being initialized)		
	setWidgetStateListenerMap( new HashMap<SwingWidget<?>, Set<SwingListener>>() );
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
	for ( SwingWidget<?> widget : getControlWithParameterMap().values() )
	{
	    if ( !widget.getParameter().isMutableOnCxlRpl() )
		widget.setEnabled( !cxlReplaceMode );
	}

	// set all CxlRpl on all state listeners and fire
	// once for good measure
	for ( SwingListener stateListener : getStateListenerList() )
	{
	    stateListener.setCxlReplaceMode( cxlReplaceMode );
	    stateListener.handleEvent( );
	}
    }

    // Create a map of StateListeners to be added to each widget. This is to
    // ensure
    // that each StateListener is only added once to a given widget.
    private void putStateListener(SwingWidget<?> widget, SwingListener stateListener)
    {
	if ( !getWidgetStateListenerMap().containsKey( widget ) )
	    getWidgetStateListenerMap().put( widget, new HashSet<SwingListener>() );
	if ( !getWidgetStateListenerMap().get( widget ).contains( stateListener ) )
	    getWidgetStateListenerMap().get( widget ).add( stateListener );
    }

    private void attachRuleToControls(ValidationRule rule, SwingListener stateListener)
    {
	if ( rule instanceof LogicalOperatorValidationRule )
	{
	    for ( ValidationRule innerRule : ( (LogicalOperatorValidationRule) rule ).getRules() )
	    {
		attachRuleToControls( innerRule, stateListener );
	    }
	}
	else if ( rule instanceof ValueOperatorValidationRule )
	{
	    attachFieldToControls( ( (ValueOperatorValidationRule) rule ).getField(), stateListener );
	}
	else if ( rule instanceof Field2OperatorValidationRule )
	{
	    attachFieldToControls( ( (Field2OperatorValidationRule) rule ).getField(), stateListener );
	    attachFieldToControls( ( (Field2OperatorValidationRule) rule ).getField2(), stateListener );
	}
    }

    private void attachFieldToControls(String field, SwingListener stateListener)
    {
	if ( field != null )
	{
	    SwingWidget<?> targetParameterWidget = getControlMap().get( field );
	    if ( targetParameterWidget == null )
		throw new IllegalStateException( "Error generating a State Rule => Control: " + field + " does not exist in Strategy: " + getStrategy().getName() );
	    putStateListener( targetParameterWidget, stateListener );

	    // 2/1/2010 John Shields added
	    // RadioButtonT requires adding all associated RadioButtonTs in the
	    // ratioGroup
	    if ( targetParameterWidget.getControl() instanceof RadioButtonT
		    && ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() != null
		    && ( ! "".equals( ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() ) ) )
	    {
		String rg = ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup();
		for ( SwingWidget<?> widget : getControlMap().values() )
		{
		    if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
			    && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
			    && ( ! "".equals( ( (RadioButtonT) widget.getControl() ).getRadioGroup() ) )
			    && ( (RadioButtonT) widget.getControl() ).getRadioGroup().equals( rg ) )
		    {
			putStateListener( widget, stateListener );
		    }
		}
	    }
	}
    }


    /**
     * Note invoking this method results in object construction as a result of down-casting 
     * our own map of a specific templatized instance (SwingWidget<?>) of ControlUI<?> --
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
     * our own map of a specific templatized instance (SwingWidget<?>) of ControlUI<?> --
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
    public Map<String, SwingWidget<?>> getControlMap()
    {
	return this.controlMap;
    }

    /**
     * @param aControlMap the controlMap to set
     */
    protected void setControlMap(Map<String, SwingWidget<?>> aControlMap)
    {
	this.controlMap = aControlMap;
    }


    /**
     * @param aStrategyPanelList
     * @return
     */
    protected void buildControlMap( List<StrategyPanelT> aStrategyPanelList )
    {
	Map<String, SwingWidget<?>> tempControlMap = new HashMap<String, SwingWidget<?>>();

	// build panels and widgets recursively
	for ( StrategyPanelT panel : aStrategyPanelList )
	{
	    tempControlMap.putAll( getControlFactory().createStrategyPanelAndWidgets( getParent(), panel, getParameterMap() ) );
	}

	// 3/13/2010 John Shields HACK: make the first panel take the full width of the window
	/*Container c = getParent();
	for ( Component child : c.getComponents() )
	{
	    if (child instanceof Container) 
	    {
		Container composite = (Container) child;
		//composite.setLayoutData( new GridData( Swing.FILL, Swing.FILL, true, true ) );
	    }
	}*/

	setControlMap( tempControlMap );
    }


    protected void createRadioGroups()
    {
	// TODO: re-implement for Swing

	/*
		Map<String, SwingRadioButtonListener> tempRadioGroupMap = new HashMap<String, SwingRadioButtonListener>();

		for ( SwingWidget<?> widget : getControlMap().values() )
		{
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != "" )
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !tempRadioGroupMap.containsKey( rg ) )
					tempRadioGroupMap.put( rg, new SwingRadioButtonListener() );

				if ( widget instanceof SwingButtonWidget )
				{
					tempRadioGroupMap.get( rg ).addButton( (SwingButtonWidget) widget );
					((SwingButtonWidget) widget).setRadioButtonListener( tempRadioGroupMap.get( rg ) );
				}

			}
		}

		setRadioGroupMap( tempRadioGroupMap );
	 */
    }

    /**
     * @return the radioGroupMap
     */
    /*public Map<String, SwingRadioButtonListener> getRadioGroupMap()
	{
	    // TODO: re-implement for Swing
	//	return this.radioGroupMap;
	}
     */
    /**
     * @param aRadioGroupMap the radioGroupMap to set
     */
    /*
	protected void setRadioGroupMap(Map<String, SwingRadioButtonListener> aRadioGroupMap)
	{
		this.radioGroupMap = aRadioGroupMap;
	}
     */
    protected void addToControlMap( String aName, ControlUI aControlUI )
    {
	getControlMap().put( aName, (SwingWidget<?>) aControlUI );
    }

    protected void addToControlWithParameterMap( String aName, ControlUI aControlUI )
    {
	getControlWithParameterMap().put( aName, (SwingWidget<?>) aControlUI );
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
	Map<String, SwingWidget<?>> tempControlWithParameterMap = new HashMap<String, SwingWidget<?>>();

	// loop through all UI controls
	for ( SwingWidget<?> widget : getControlMap().values() )
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
	List<SwingListener> tempStateListenerList = new Vector<SwingListener>();

	// loop through all UI controls
	for ( SwingWidget<?> widget : getControlMap().values() )
	{
	    // parameter state rules that have an id should be included in
	    // the rules map
	    ControlT control = widget.getControl();

	    if ( control.getStateRule() != null )
	    {
		for ( StateRuleT stateRule : control.getStateRule() )
		{

		    SwingWidget<?> affectedWidget = getControlMap().get( control.getID() );
		    SwingListener stateListener = new SwingStateListener( affectedWidget, stateRule, getControlMap(), getCompleteValidationRuleMap() );

		    // attach the stateListener's rule to controls
		    attachRuleToControls( stateListener.getRule(), stateListener );

		    tempStateListenerList.add( stateListener );

		    // run the state rule to check if any parameter needs to be
		    // enabled/disabled or hidden/unhidden before being rendered
		    stateListener.handleEvent( );
		}
	    }
	}

	setStateListenerList( tempStateListenerList );
    }	

    /**
     * @return the controlWithParameterMap
     */
    public Map<String, SwingWidget<?>> getControlWithParameterMap()
    {
	return this.controlWithParameterMap;
    }

    /**
     * @param aControlWithParameterMap the controlWithParameterMap to set
     */
    protected void setControlWithParameterMap(Map<String, SwingWidget<?>> aControlWithParameterMap)
    {
	this.controlWithParameterMap = aControlWithParameterMap;
    }

    /**
     * @return the stateListenerList
     */
    protected List<SwingListener> getStateListenerList()
    {
	return this.stateListenerList;
    }

    /**
     * @param tempStateListenerList the stateListenerList to set
     */
    protected void setStateListenerList(List<SwingListener> tempStateListenerList)
    {
	this.stateListenerList = tempStateListenerList;
    }

    /**
     * @return the widgetStateListenerMap
     */
    protected Map<SwingWidget<?>, Set<SwingListener>> getWidgetStateListenerMap()
    {
	return this.widgetStateListenerMap;
    }

    /**
     * @param aWidgetStateListenerMap the widgetStateListenerMap to set
     */
    protected void setWidgetStateListenerMap(Map<SwingWidget<?>, Set<SwingListener>> aWidgetStateListenerMap)
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
	for ( Entry<SwingWidget<?>, Set<SwingListener>> entry : getWidgetStateListenerMap().entrySet() )
	{
	    for ( SwingListener listener : entry.getValue() )
		entry.getKey().addListener( listener );
	}
    }

    /**
     * @return the parent
     */
    public Container getParent()
    {
	return this.parent;
    }

    /**
     * @param aParent the parent to set
     */
    protected void setParent(Container aParent)
    {
	this.parent = aParent;
    }

    /**
     * Lazy-init'd
     * 
     * @return the controlFactory
     */
    public SwingFactory getControlFactory() 
    {
	if ( this.controlFactory == null )
	{
	    this.controlFactory = new SwingFactory( getAtdl4jConfig() );
	}
	return this.controlFactory;
    }

    /**
     * @param aControlFactory the controlFactory to set
     */
    protected void setControlFactory(SwingFactory aControlFactory)
    {
	this.controlFactory = aControlFactory;
    }

    protected void fireStateListeners()
    {
	// fire state listeners once for good measure
	for ( SwingListener stateListener : getStateListenerList() )
	    stateListener.handleEvent();
    }

    protected void fireStateListenersForControl( ControlUI aControl )
    {
	for ( SwingListener stateListener : getStateListenerList() )
	{
	    if ( aControl.equals( stateListener.getAffectedWidget() ) )
	    {
		stateListener.handleEvent();
	    }
	}
    }

    /**
     * Invokes SwingStateListener.handleLoadFixMessageEvent() for aControl
     * @param aControl
     */
    protected void fireLoadFixMessageStateListenersForControl( ControlUI aControl )
    {
	for ( SwingListener stateListener : getStateListenerList() )
	{
	    stateListener.handleLoadFixMessageEvent();
	}
    }


    @Override
    public void relayoutCollapsibleStrategyPanels() {
	// TODO Auto-generated method stub
    }


    @Override
    protected void applyRadioGroupRules() {
	// Not needed in Swing?	    
    }

    /* If no RadioButtons within a radioGroup are selected, then first one in list will be selected
     * @see org.atdl4j.ui.impl.AbstractStrategyUI#applyRadioGroupRules()
     */
    /*
	protected void applyRadioGroupRules()
	{
		if ( getRadioGroupMap() != null )
		{
			for ( SwingRadioButtonListener tempRadioButtonListener : getRadioGroupMap().values() )
			{
				// -- If no RadioButtons within the radioGroup are selected, then first one in list will be selected --
				tempRadioButtonListener.processReinit();
			}
		}
	}
     */
}
