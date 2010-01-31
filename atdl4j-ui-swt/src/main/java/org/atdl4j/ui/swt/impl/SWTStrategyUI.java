package org.atdl4j.ui.swt.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.xml.bind.JAXBException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.apache.log4j.Logger;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.StrategyRuleset;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.fix.PlainFIXMessageBuilder;
import org.atdl4j.data.validation.Field2OperatorValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.PatternValidationRule;
import org.atdl4j.data.validation.ReferencedValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.widget.HiddenFieldWidget;

import org.atdl4j.atdl.core.MultipleCharValueT;
import org.atdl4j.atdl.core.MultipleStringValueT;
import org.atdl4j.atdl.core.ObjectFactory;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.core.UseT;
import org.atdl4j.atdl.flow.StateRuleT;

import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.atdl.layout.StrategyPanelT;
import org.atdl4j.atdl.validation.EditRefT;
import org.atdl4j.atdl.validation.OperatorT;
import org.atdl4j.atdl.validation.EditT;
import org.atdl4j.atdl.validation.StrategyEditT;

/**
 * UI representation for a Strategy object.
 * 
 */
public class SWTStrategyUI extends AbstractStrategyUI {
	
	protected Map<String, ParameterT> parameters;
	protected Map<String, SWTWidget<?>> controls;
	protected Map<String, SWTWidget<?>> controlWithParameters;
	protected List<SWTStateListener> stateListeners = new Vector<SWTStateListener>();
	protected Map<SWTWidget<?>, Set<SWTStateListener>> widgetStateListeners; // map of StateListeners to attach to control Widgets
	protected StrategyRuleset ruleset;
	protected Map<String, ValidationRule> refRules;
	protected static final Logger logger = Logger.getLogger(AbstractStrategyUI.class);
	protected StrategyT strategy;
//TODO 1/16/2010 Scott Atwell added
	protected StrategiesT strategies;

	public SWTStrategyUI(StrategyT strategy,
//TODO 1/16/2010 Scott Atwell added strategies			Map<String, ValidationRule> strategiesRules, Composite parent)
//TODO 1/17/2010 Scott Atwell added inputHiddenFieldNameValueMap			Map<String, ValidationRule> strategiesRules, Composite parent, StrategiesT strategies)
			Map<String, ValidationRule> strategiesRules, Composite parent, StrategiesT strategies, Map<String, String> inputHiddenFieldNameValueMap)
			throws JAXBException {
		this.strategy = strategy;
		
//TODO 1/16/2010 Scott Atwell added
		this.strategies = strategies;

		// ParameterFieldFactory parameterFactory = new ParameterFieldFactory();
		SWTFactory controlFactory = new SWTFactory();
		
		parameters = new HashMap<String, ParameterT>();
		controls = new HashMap<String,SWTWidget<?>>();
		controlWithParameters = new HashMap<String,SWTWidget<?>>();
		widgetStateListeners = new HashMap<SWTWidget<?>, Set<SWTStateListener>>(); 
		
		// initialize rules collection with global rules
		refRules = new HashMap<String, ValidationRule>(strategiesRules);

		ruleset = new StrategyRuleset();
		
		// build parameters
		for (ParameterT parameter : strategy.getParameter()) {
			
			// compile list of parameters (TODO: is this needed?)
			parameters.put(parameter.getName(), parameter);
			
			// required fields should be validated as well
			if (parameter.getUse() != null) {
				if (parameter.getUse().equals(UseT.REQUIRED)) {
					ValidationRule requiredFieldRule = new ValueOperatorValidationRule(
							parameter.getName(), OperatorT.EX, null, strategy);
					ruleset.addRequiredFieldRule(requiredFieldRule);
				}
			}
			
			// validate types based on patterns
			if (parameter instanceof MultipleCharValueT) {
				MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule(
						multipleCharValueT.getName(), "\\S?(\\s\\S?)*");
				ruleset.addPatternRule(patternBasedRule);

			} else if (parameter instanceof MultipleStringValueT) {
				MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule(
						multipleStringValueT.getName(), "\\S+(\\s\\S+)*");
				ruleset.addPatternRule(patternBasedRule);
			}
		}		
		
		// and add local rules
		for (EditT edit : strategy.getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit, refRules,
						strategy);
				refRules.put(id, rule);
			} else {
				throw new JAXBException("Strategy-scoped edit without id");
			}
		}

		// generate validation rules for StrategyEdit elements
		for (StrategyEditT se : strategy.getStrategyEdit()) {
			if (se.getEdit() != null) {
				EditT edit = se.getEdit();
				ValidationRule rule = ValidationRuleFactory
						.createRule(edit, refRules, se);
				String id = edit.getId();
				if (id != null)
					refRules.put(id, rule); // TODO: this line should be moved to RuleFactory?
				ruleset.putRefRule(se, rule); // TODO: this line should be moved to RuleFactory?
			}

			// reference for a previously defined rule
			if (se.getEditRef() != null) {
				EditRefT editRef = se.getEditRef();
				String id = editRef.getId();
				ruleset.putRefRule(se, new ReferencedValidationRule(id)); // TODO: this line should be moved to RuleFactory?
			}
		}

		// build panels and widgets recursively
		for (StrategyPanelT panel : strategy.getStrategyLayout().getStrategyPanel()) {
			controls.putAll(controlFactory.create(parent, panel, parameters, SWT.NONE));
		}		
		
		
		for (SWTWidget<?> widget : controls.values())
		{
			for (SWTWidget<?> widget2 : controls.values())
			{
				if (widget != widget2 && widget.getControl().getID().equals(widget2.getControl().getID()))
					   throw new JAXBException("Duplicate Control ID: \"" + widget.getControl().getID() + "\"");
			}
		}

// TODO 1/17/2010 Scott Atwell Added BELOW
		if ( inputHiddenFieldNameValueMap != null )
		{
			ObjectFactory tempObjectFactory = new ObjectFactory();
			
			for ( Map.Entry<String, String> tempMapEntry : inputHiddenFieldNameValueMap.entrySet() )
			{
				String tempName = tempMapEntry.getKey();
				Object tempValue = tempMapEntry.getValue();
				ParameterT parameter = tempObjectFactory.createStringT();
				parameter.setName( tempName );
				parameter.setUse( UseT.OPTIONAL );
				
				// compile list of parameters (TODO: is this needed?)
				parameters.put(parameter.getName(), parameter);
				
				HiddenFieldT hiddenField = new HiddenFieldT();
				hiddenField.setInitValue( tempValue.toString() );
				hiddenField.setParameterRef( tempName );
				
				HiddenFieldWidget hiddenFieldWidget = new HiddenFieldWidget( hiddenField, parameter );
				controls.put( tempName, hiddenFieldWidget );
			}
		}
// TODO 1/17/2010 Scott Atwell Added ABOVE
		
		// loop through all UI controls
		for (SWTWidget<?> widget : controls.values()) {
			
			if (widget.getParameter() != null)
			{
				// validate that a parameter is not being added twice
				for (String parameterRef : controlWithParameters.keySet()) {
					if (parameterRef.equals(widget.getParameter().getName()))
						throw new JAXBException("Cannot add parameter \""
								+ parameterRef + "\" to two separate controls.");
				}
				controlWithParameters.put(widget.getParameter().getName(), widget);
			}

			// parameter state rules that have an id should be included in
			// the rules map
			ControlT control = widget.getControl();

			if (control.getStateRule() != null) {
				for (StateRuleT stateRule : control.getStateRule()) {
					
					SWTWidget<?> affectedWidget = controls.get(control.getID());
					SWTStateListener stateListener =
						new SWTStateListener(affectedWidget, stateRule, controls, refRules);	
					
					// attach the stateListener's rule to controls
					attachRuleToControls(stateListener.getRule(), stateListener);
					
					stateListeners.add(stateListener);
					
					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
					stateListener.handleEvent(null);
				}
			}
		}
		
		// add all StateListeners to the controls
		for (Entry<SWTWidget<?>, Set<SWTStateListener>> entry : widgetStateListeners.entrySet())
		{
			for(SWTStateListener listener : entry.getValue())	
				entry.getKey().generateStateRuleListener(listener);
		}
	}

	// Set screen how it should look when in CxlRpl
	public void setCxlReplaceMode(boolean cxlReplaceMode) {
		
		// enable/disable non-mutable parameters
		for (SWTWidget<?> widget : controlWithParameters.values())
		{
			if (!widget.getParameter().isMutableOnCxlRpl())
				widget.setEnabled(!cxlReplaceMode);
		}
		
		// set all CxlRpl on all state listeners and fire
		// once for good measure
		for (SWTStateListener stateListener : stateListeners)
		{
			stateListener.setCxlReplaceMode(cxlReplaceMode);
			stateListener.handleEvent(null);
		}
	}
	
	// Create a map of StateListeners to be added to each widget. This is to ensure
	// that each StateListener is only added once to a given widget.
	private void putStateListener(SWTWidget<?> widget, SWTStateListener stateListener) {
		if (!widgetStateListeners.containsKey(widget))
			widgetStateListeners.put(widget, new HashSet<SWTStateListener>());
		if (!widgetStateListeners.get(widget).contains(stateListener))
			widgetStateListeners.get(widget).add(stateListener);
	}

	private void attachRuleToControls(ValidationRule rule, SWTStateListener stateRuleListener) throws JAXBException {
		if (rule instanceof LogicalOperatorValidationRule)
		{
			for (ValidationRule innerRule : ((LogicalOperatorValidationRule)rule).getRules()) {
				attachRuleToControls(innerRule, stateRuleListener);
			}
		} else if (rule instanceof ValueOperatorValidationRule) {
			attachFieldToControls(((ValueOperatorValidationRule)rule).getField(), stateRuleListener);
		} else if (rule instanceof Field2OperatorValidationRule) {
			attachFieldToControls(((Field2OperatorValidationRule)rule).getField(), stateRuleListener);
			attachFieldToControls(((Field2OperatorValidationRule)rule).getField2(), stateRuleListener);
		}
	}
	
	private void attachFieldToControls(String field, SWTStateListener stateRuleListener) throws JAXBException {
		if (field != null) {		
			SWTWidget<?> targetParameterWidget = controls.get(field);
			if (targetParameterWidget == null)
				throw new JAXBException(
						"Error generating a State Rule => Control: "
								+ field + " does not exist in Strategy: "
								+ strategy.getName());
			putStateListener(targetParameterWidget, stateRuleListener);
		}
	}

	public SWTWidget<?> getControl(String name) {
		return controls.get(name);
	}

	public StrategyT getStrategy() {
		return strategy;
	}

	public void validate() throws ValidationException, JAXBException {
		if (ruleset != null) {
			// delegate validation, passing all global and local rules as
			// context information, and all my parameters
			ruleset.validate(refRules, new HashMap<String,ControlUI<?>>(controlWithParameters));
		} else {
			logger.info("No validation rule defined for strategy " + strategy.getName());
		}
	}

	public String getFIXMessage() throws JAXBException {
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		getFIXMessage(builder);
		return builder.getMessage();
	}

	public void getFIXMessage(FIXMessageBuilder builder) throws JAXBException {
		builder.onStart();
		
//TODO Scott Atwell 1/16/2010 Added block BELOW		
		if ( ( getStrategy() != null ) && ( getStrategies() != null ) ) {
			String value = getStrategy().getWireValue();
			if ( value != null )	{
				if (getStrategies().getStrategyIdentifierTag() != null) {
					builder.onField(getStrategies().getStrategyIdentifierTag().intValue(), value.toString());
				} else {
					builder.onField(847, value);
				}				
			}
			
			String verValue = getStrategy().getVersion();
			if ( verValue != null )	{
				if (getStrategies().getVersionIdentifierTag() != null) {
					builder.onField(getStrategies().getVersionIdentifierTag().intValue(), verValue.toString());
				} else {
// n/a					builder.onField(847, value);
				}				
			}
		}
//TODO Scott Atwell 1/16/2010 Added block ABOVE		
		
		
		
		for (ControlUI<?> control : controls.values()) {
			if (control.getParameter() != null) control.getFIXValue(builder);
		}
		builder.onEnd();
	}

	// TODO: this doesn't know how to load custom repeating groups
	// or standard repeating groups aside from 957 StrategyParameters
	// TODO: would like to integrate with QuickFIX engine
	public void setFIXMessage(String fixMessage) throws JAXBException {
		
		// TODO: need to reverse engineer state groups
		
		String[] fixParams = fixMessage.split("\\001");

		for (int i = 0; i < fixParams.length; i++)
		{
			String[] pair = fixParams[i].split("=");
			int tag = Integer.parseInt(pair[0]);
			String value = pair[1];
			
			// not repeating group
			if (tag < 957 || tag > 960)
			{
				for (SWTWidget<?> widget : controlWithParameters.values())
				{
					if (widget.getParameter().getFixTag() != null &&
						widget.getParameter().getFixTag().equals(BigInteger.valueOf(tag)))
					{
						widget.setValueAsString(value);
					}
				}
			}
			// StrategyParams repeating group
			else if (tag == 957)
			{
				i++;
				for (int j = 0; j < Integer.parseInt(value); j++)
				{
					String name = fixParams[i].split("=")[1];
					String value2 = fixParams[i+2].split("=")[1];
					
					for (SWTWidget<?> widget : controlWithParameters.values())
					{
						if (widget.getParameter().getName() != null &&
							widget.getParameter().getName().equals(name))
						{
							widget.setValueAsString(value2);
						}
					}
					i = i+3;
				}
			}
		}
		
		// fire state listeners once for good measure
		for (SWTStateListener stateListener : stateListeners)
			stateListener.handleEvent(null);
	}
	
	
//TODO Scott Atwell added 1/14/2010
	public Map<String, ControlUI<?>> getControls()
	{
		return new HashMap<String,ControlUI<?>>(controlWithParameters);
//		return new HashMap<String,ControlUI<?>>(controls);
	}

	//TODO Scott Atwell added 1/16/2010
	public StrategiesT getStrategies() {
		return strategies;
	}
	
}
