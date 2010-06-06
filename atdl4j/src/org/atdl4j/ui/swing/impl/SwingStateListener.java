package org.atdl4j.ui.swing.impl;

import java.util.HashMap;
import java.util.Map;

import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class SwingStateListener
		implements Listener
{
	private SWTWidget<?> affectedWidget;
	private StateRuleT stateRule;
	private Map<String, SWTWidget<?>> controls;
	private Map<String, ValidationRule> refRules;
	private ValidationRule rule;
	private boolean cxlReplaceMode = false;

	public SwingStateListener(SWTWidget<?> affectedWidget, StateRuleT stateRule, Map<String, SWTWidget<?>> controls, Map<String, ValidationRule> refRules)
	{
		this.affectedWidget = affectedWidget;
		this.stateRule = stateRule;
		this.controls = controls;
		this.refRules = refRules;
		if ( stateRule.getEdit() != null )
			this.rule = ValidationRuleFactory.createRule( stateRule.getEdit(), refRules, stateRule );
		if ( stateRule.getEditRef() != null )
			this.rule = ValidationRuleFactory.createRule( stateRule.getEditRef() );
	}

	public ValidationRule getRule()
	{
		return rule;
	}

	public void handleEvent(Event event)
	{

		// Create a casted map so that Validatable<?> can be used
		Map<String, ControlUI<?>> targets = new HashMap<String, ControlUI<?>>( controls );

		try
		{
			rule.validate( refRules, targets );
		}
		catch (ValidationException e)
		{
			setBehaviorAsStateRule( false );
			return;
		}
		setBehaviorAsStateRule( true );
	}

	protected void setBehaviorAsStateRule(Boolean state)
	{

		// set visible
		if ( stateRule.isVisible() != null )
		{
			affectedWidget.setVisible( !( stateRule.isVisible() ^ state ) );
		}

		// enabled and value setting rules are only valid if not in
		// Cancel Replace mode
		if ( cxlReplaceMode && affectedWidget.getParameter() != null && !affectedWidget.getParameter().isMutableOnCxlRpl() )
		{
			affectedWidget.setEnabled( false );
		}
		else
		{
			// set enabled
			if ( stateRule.isEnabled() != null )
			{
				affectedWidget.setEnabled( !( stateRule.isEnabled() ^ state ) );
			}

			// set value
			if ( stateRule.getValue() != null )
			{
				if ( state )
				{
					String value = stateRule.getValue();
					affectedWidget.setValueAsString( value );
				}
/*** 4/11/2010 Scott Atwell not necessary as setValueAsString() already handles Atdl4jConstants.VALUE_NULL_INDICATOR
***/			
// 2/10/2010 Scott Atwell added the else clause
				//  -- state arg is false and value involved is VALUE_NULL_INDICATOR --
				else if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( stateRule.getValue() ) )  
				{
					// -- This resets the widget (other widgets than value="{NULL}") to non-null --
// 2/11/2010 Scott Atwell					affectedWidget.setNullValue( false );
					affectedWidget.setNullValue( Boolean.FALSE );
				}
			}
		}
	}

	public void setCxlReplaceMode(boolean cxlReplaceMode)
	{
		this.cxlReplaceMode = cxlReplaceMode;
	}

	/**
	 * @return the affectedWidget
	 */
	public SWTWidget<?> getAffectedWidget()
	{
		return this.affectedWidget;
	}

	/**
	 * Returns true if StateRule has value="{NULL}" 
	 * @return
	 */
	public boolean hasSetValueNullStateRule()
	{
		if ( stateRule != null )
		{
			if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( stateRule.getValue() ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Used when panel is initially loaded with a FIX Message.
	 * Effectively fires the StateRule in reverse (eg selects radio button or checkbox that normally
	 * nulls the control's value to be selected when control has a value set)
	 * @param event
	 */
	public void handleLoadFixMessageEvent(Event event)
	{
		// -- If the StateRule sets value to VALUE_NULL_INDICATOR, however, the Control has 'externally' had 
		// -- its value set to non-null value, then 'toggle' the state of the control (eg checkbox or radio button)
		// -- associated with this StateRule
		if ( ( getAffectedWidget() != null ) &&
			  ( ! getAffectedWidget().isNullValue() ) &&
			  ( hasSetValueNullStateRule() ) )
		{
			if ( getRule() instanceof ValueOperatorValidationRule )
			{
				ValueOperatorValidationRule tempValueOperatorValidationRule = (ValueOperatorValidationRule) getRule();
				
				ControlUI<?> tempAssociatedControl = controls.get( tempValueOperatorValidationRule.getField() );
				
				if ( ( tempAssociatedControl != null ) &&
					  ( ControlHelper.isControlToggleable( tempAssociatedControl.getControl() ) ) ) 
				{
					String tempRuleNewValueAsString = null;
					if ( "true".equals( tempValueOperatorValidationRule.getValue() ) )
					{
						tempRuleNewValueAsString = "false";
					}
					else if ( "false".equals( tempValueOperatorValidationRule.getValue() ) )
					{
						tempRuleNewValueAsString = "true";
					}
					
					if ( tempRuleNewValueAsString != null )
					{
						// -- Toggle the value --
						if ( ( OperatorT.EQ.equals( tempValueOperatorValidationRule.getOperator() ) ) 
// ??									|| ( OperatorT.NE.equals( tempValueOperatorValidationRule.getOperator() ) ) 
						    )
						{
							tempAssociatedControl.setValueAsString( tempRuleNewValueAsString );
						}
					}
				}
			}
		}
	}

}
