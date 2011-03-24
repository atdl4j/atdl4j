package org.atdl4j.ui.swing.impl;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;


public class SwingStateListener implements SwingListener {
	private SwingWidget<?> affectedWidget;
	private StateRuleT stateRule;
	private Map<String, SwingWidget<?>> controls;
	private Map<String, ValidationRule> refRules;
	private ValidationRule rule;
	private boolean cxlReplaceMode = false;

	public SwingStateListener(SwingWidget<?> affectedWidget, StateRuleT stateRule, Map<String, SwingWidget<?>> controls, Map<String, ValidationRule> refRules)
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

	public void handleEvent()
	{

		// Create a casted map so that Validatable<?> can be used
		Map<String, Atdl4jWidget<?>> targets = new HashMap<String, Atdl4jWidget<?>>( controls );

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
			try {
				affectedWidget.setVisible( !( stateRule.isVisible() ^ state ) );
			}
			catch(Exception e) {
				e.printStackTrace();
			}
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
			if ( stateRule.isEnabled() != null ){
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
				//  -- state arg is false and value involved is VALUE_NULL_INDICATOR --
				else if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( stateRule.getValue() ) )  
				{
					// -- This resets the widget (other widgets than value="{NULL}") to non-null --
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
	public SwingWidget<?> getAffectedWidget()
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
	public void handleLoadFixMessageEvent()
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
				
				Atdl4jWidget<?> tempAssociatedControl = controls.get( tempValueOperatorValidationRule.getField() );
				
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
						if ( ( OperatorT.EQ.equals( tempValueOperatorValidationRule.getOperator() ) ) )
						{
							tempAssociatedControl.setValueAsString( tempRuleNewValueAsString );
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		handleEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		handleEvent();		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		handleEvent();
	}
}
