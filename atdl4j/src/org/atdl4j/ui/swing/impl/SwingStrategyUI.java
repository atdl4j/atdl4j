package org.atdl4j.ui.swing.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.validation.Field2OperatorValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.swing.SwingWidget;
import org.atdl4j.ui.swing.widget.SwingButtonWidget;


/**
 * Swing-specific UI representation for a Strategy object.
 * 
 */
public class SwingStrategyUI extends AbstractStrategyUI {
	protected static final Logger					logger	= Logger.getLogger(SwingStrategyUI.class);
	
	protected Map<String, SwingWidget<?>>	swingWidgetMap;
	
	protected Map<String, SwingWidget<?>>						swingWidgetWithParameterMap;
	
	private JPanel												parentComponent;
	
	protected List<SwingStateListener> stateListenerList;
	
	protected Map<SwingWidget<?>, Set<SwingStateListener>> widgetStateListenerMap;
	
	protected Map<String, ButtonGroup> radioGroupMap;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.StrategyUI#relayoutCollapsibleStrategyPanels()
	 */
	@Override
	public void relayoutCollapsibleStrategyPanels() {
		// TODO Auto-generated method stub
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#buildAtdl4jWidgetMap(java.util.List)
	 */
	@Override
	protected void buildAtdl4jWidgetMap(List<StrategyPanelT> aStrategyPanelList) {
		Map<String, SwingWidget<?>> tempSwingWidgetMap = new HashMap<String, SwingWidget<?>>();
		// build panels and widgets recursively
		for (StrategyPanelT panel : aStrategyPanelList) {
			tempSwingWidgetMap.putAll(SwingStrategyPanelFactory.createStrategyPanelAndWidgets(parentComponent, panel,
					getParameterMap(), 0, getAtdl4jWidgetFactory()));
		}
		swingWidgetMap = tempSwingWidgetMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#getAtdl4jWidgetMap()
	 */
	@Override
	public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetMap() {
		if ( swingWidgetMap != null )	{
			return new HashMap<String, Atdl4jWidget<?>>( swingWidgetMap  );
		}
		else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#getAtdl4jWidgetWithParameterMap()
	 */
	@Override
	public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetWithParameterMap() {
		if ( swingWidgetWithParameterMap != null )
		{
			return new HashMap<String, Atdl4jWidget<?>>( swingWidgetWithParameterMap  );
		}
		else
		{
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#initBegin(java.lang.Object)
	 */
	@Override
	protected void initBegin(Object parentContainer) {
		parentComponent = (JPanel) parentContainer;
		swingWidgetWithParameterMap = new HashMap<String, SwingWidget<?>>();
		widgetStateListenerMap = new HashMap<SwingWidget<?>, Set<SwingStateListener>>();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#buildAtdl4jWidgetMap()
	 */
	@Override
	protected void buildAtdl4jWidgetMap() {
		if (getStrategy() == null) {
			throw new IllegalStateException("Unexpected error: getStrategy() was null.");
		}
		
		if (getStrategy().getStrategyLayout() == null) {
			throw new IllegalStateException(
					"Unexpected error: getStrategy().getStrategyLayout() was null .  (verify  <lay:StrategyLayout>)");
		}
		
		if (getStrategy().getStrategyLayout() == null) {
			throw new IllegalStateException(
					"Unexpected error: getStrategy().getStrategyLayout().getStrategyPanel() was null .  (verify  <lay:StrategyLayout> <lay:StrategyPanel>)");
		}
		
		buildAtdl4jWidgetMap(getStrategy().getStrategyLayout().getStrategyPanel());
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#createRadioGroups()
	 */
	@Override
	protected void createRadioGroups() {
		Map<String, ButtonGroup> tempRadioGroupMap = new HashMap<String, ButtonGroup>();
		
		for ( SwingWidget<?> widget : swingWidgetMap.values() )
		{
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != "" )
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !tempRadioGroupMap.containsKey( rg ) )
					tempRadioGroupMap.put( rg, new ButtonGroup() );
				
				if ( widget instanceof SwingButtonWidget )
				{
					tempRadioGroupMap.get( rg ).add(((SwingButtonWidget) widget).getButton() );
				}
			}
		}
		radioGroupMap = tempRadioGroupMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#buildAtdl4jWidgetWithParameterMap()
	 */
	@Override
	protected void buildAtdl4jWidgetWithParameterMap() {
		Map<String, SwingWidget<?>> tempSwingWidgetWithParameterMap = new HashMap<String, SwingWidget<?>>();
		// loop through all UI controls
		for ( SwingWidget<?> widget : swingWidgetMap.values() )
		{
			if ( widget.getParameter() != null )
			{
				// validate that a parameter is not being added twice
				String tempParameterName = widget.getParameter().getName();
				if ( tempSwingWidgetWithParameterMap.containsKey( tempParameterName ) )
				{
					throw new IllegalStateException( "Cannot add parameter \"" + tempParameterName + "\" to two separate controls." );
				}
				tempSwingWidgetWithParameterMap.put( tempParameterName, widget );
			}
			
		}
		swingWidgetWithParameterMap = tempSwingWidgetWithParameterMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#attachGlobalStateRulesToControls()
	 */
	@Override
	protected void attachGlobalStateRulesToControls() {
		List<SwingStateListener> tempStateListenerList = new Vector<SwingStateListener>();
		
		// loop through all UI controls
		for ( SwingWidget<?> widget : swingWidgetMap.values() )
		{
			// parameter state rules that have an id should be included in
			// the rules map
			ControlT control = widget.getControl();
	
			if ( control.getStateRule() != null )
			{
				for ( StateRuleT stateRule : control.getStateRule() )
				{
	
					SwingWidget<?> affectedWidget = swingWidgetMap.get( control.getID() );
					SwingStateListener stateListener = new SwingStateListener( affectedWidget, stateRule, swingWidgetMap, getCompleteValidationRuleMap() );
	
					// attach the stateListener's rule to controls
					attachRuleToControls( stateListener.getRule(), stateListener );
	
					tempStateListenerList.add( stateListener );
	
					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
  				stateListener.handleEvent();
				}
			}
		}
		
		stateListenerList = tempStateListenerList;
	}
	
	private void attachRuleToControls(ValidationRule rule, SwingStateListener stateRuleListener)
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
	
	private void attachFieldToControls(String field, SwingStateListener stateRuleListener)
	{
		if ( field != null )
		{
			SwingWidget<?> targetParameterWidget = swingWidgetMap.get( field );
			if ( targetParameterWidget == null )
				throw new IllegalStateException( "Error generating a State Rule => Control: " + field + " does not exist in Strategy: " + getStrategy().getName() );
			putStateListener( targetParameterWidget, stateRuleListener );

			// -- RadioButtonT requires adding all associated RadioButtonTs in the radioGroup --
			if ( targetParameterWidget.getControl() instanceof RadioButtonT
					&& ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() != null
					&& ( ! "".equals( ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() ) ) )
			{
				String rg = ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup();
				for ( SwingWidget<?> widget : swingWidgetMap.values() )
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
	
	private void putStateListener(SwingWidget<?> widget, SwingStateListener stateListener)
	{
		if ( !widgetStateListenerMap.containsKey( widget ) )
			widgetStateListenerMap.put( widget, new HashSet<SwingStateListener>() );
		if ( !widgetStateListenerMap.get( widget ).contains( stateListener ) )
			widgetStateListenerMap.get( widget ).add( stateListener );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#attachStateListenersToAllAtdl4jWidgets()
	 */
	@Override
	protected void attachStateListenersToAllAtdl4jWidgets() {
		for ( Entry<SwingWidget<?>, Set<SwingStateListener>> entry : widgetStateListenerMap.entrySet() )
		{
			for ( SwingStateListener listener : entry.getValue() )
				entry.getKey().addListener( listener );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#initEnd()
	 */
	@Override
	protected void initEnd() {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#addToAtdl4jWidgetMap(java.lang.String, org.atdl4j.ui.Atdl4jWidget)
	 */
	@Override
	protected void addToAtdl4jWidgetMap(String aName, Atdl4jWidget aAtdl4jWidget) {
		swingWidgetMap.put(aName, (SwingWidget)aAtdl4jWidget);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#addToAtdl4jWidgetWithParameterMap(java.lang.String,
	 * org.atdl4j.ui.Atdl4jWidget)
	 */
	@Override
	protected void addToAtdl4jWidgetWithParameterMap(String aName, Atdl4jWidget aAtdl4jWidget) {
		swingWidgetWithParameterMap.put(aName, (SwingWidget)aAtdl4jWidget);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#removeFromAtdl4jWidgetMap(java.lang.String)
	 */
	@Override
	protected void removeFromAtdl4jWidgetMap(String aName) {
		swingWidgetMap.remove(aName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#removeFromAtdl4jWidgetWithParameterMap(java.lang.String)
	 */
	@Override
	protected void removeFromAtdl4jWidgetWithParameterMap(String aName) {
		swingWidgetWithParameterMap.remove(aName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#setCxlReplaceMode(boolean)
	 */
	@Override
	public void setCxlReplaceMode(boolean cxlReplaceMode) {
		// enable/disable non-mutable parameters
		for ( SwingWidget<?> widget : swingWidgetWithParameterMap.values() )
		{
			if ( !widget.getParameter().isMutableOnCxlRpl() ){
				widget.setEnabled( !cxlReplaceMode );
			}
		}

		// set all CxlRpl on all state listeners and fire
		// once for good measure
		for ( SwingStateListener stateListener : stateListenerList )
		{
			stateListener.setCxlReplaceMode( cxlReplaceMode );
			stateListener.handleEvent();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#fireStateListeners()
	 */
	@Override
	protected void fireStateListeners() {
		// fire state listeners once for good measure
		for ( SwingStateListener stateListener : stateListenerList )
			stateListener.handleEvent();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#fireStateListenersForAtdl4jWidget(org.atdl4j.ui.Atdl4jWidget)
	 */
	@Override
	protected void fireStateListenersForAtdl4jWidget(Atdl4jWidget aAtdl4jWidget) {
		for ( SwingStateListener stateListener : stateListenerList )
		{
			if ( aAtdl4jWidget.equals( stateListener.getAffectedWidget() ) )
			{
				stateListener.handleEvent();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.atdl4j.ui.impl.AbstractStrategyUI#fireLoadFixMessageStateListenersForAtdl4jWidget(org.atdl4j.ui.Atdl4jWidget)
	 */
	@Override
	protected void fireLoadFixMessageStateListenersForAtdl4jWidget(Atdl4jWidget aAtdl4jWidget) {
		for ( SwingStateListener stateListener : stateListenerList )
		{
			if ( aAtdl4jWidget.equals( stateListener.getAffectedWidget() ) )
			{
				stateListener.handleLoadFixMessageEvent();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.atdl4j.ui.impl.AbstractStrategyUI#applyRadioGroupRules()
	 */
	@Override
	protected void applyRadioGroupRules() {
		if ( radioGroupMap != null )
		{
			for ( ButtonGroup tempSwingRadioButtonListener : radioGroupMap.values() )
			{
				// -- If no RadioButtons within the radioGroup are selected, then first one in list will be selected --
				if (tempSwingRadioButtonListener.getSelection() == null){
					AbstractButton ab = tempSwingRadioButtonListener.getElements().nextElement();
					if (ab != null) {
						ab.setSelected(true);
					}
				}
			}
		}
	}
}
