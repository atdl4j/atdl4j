package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.flow.StateRuleT;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.util.RuleFactory;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class SWTStateListener implements Listener {

	private SWTWidget<?> affectedWidget;
	private StateRuleT stateRule;
	private Map<String, SWTWidget<?>> controls;
	private Map<String, ValidationRule> refRules;
	private ValidationRule rule;
	private boolean cxlReplaceMode = false;

	public SWTStateListener(SWTWidget<?> affectedWidget, StateRuleT stateRule,
			Map<String, SWTWidget<?>> controls, Map<String, ValidationRule> refRules)
			throws JAXBException {
		this.affectedWidget = affectedWidget;
		this.stateRule = stateRule;
		this.controls = controls;
		this.refRules = refRules;
		if (stateRule.getEdit() != null)
			this.rule = RuleFactory.createRule(stateRule.getEdit(), refRules, stateRule);
		if (stateRule.getEditRef() != null)
			this.rule = RuleFactory.createRule(stateRule.getEditRef());
	}
	
	public ValidationRule getRule()
	{
		return rule;
	}

	public void handleEvent(Event event) {
		
		// Create a casted map so that Validatable<?> can be used
		Map<String, ControlUI<?>> targets = new HashMap<String, ControlUI<?>>(controls);
		
		try {
			rule.validate(refRules, targets);
		} catch (ValidationException e) {
			setBehaviorAsStateRule(false);
			return;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		setBehaviorAsStateRule(true);
	}

	private void setBehaviorAsStateRule(Boolean state) {
		
		// set visible
		if (stateRule.isVisible() != null) {
			affectedWidget.setVisible(!(stateRule.isVisible() ^ state));
		}
		
		// enabled and value setting rules are only valid if not in
		// Cancel Replace mode
		if (cxlReplaceMode &&
			affectedWidget.getParameter() != null &&
			!affectedWidget.getParameter().isMutableOnCxlRpl())
		{
			affectedWidget.setEnabled(false);
		}
	    else
		{
	    		// set enabled
			    if (stateRule.isEnabled() != null) {
					affectedWidget.setEnabled(!(stateRule.isEnabled() ^ state));
				}
			    
				// set value
				if (stateRule.getValue() != null) {
					if (state) {
						String value = stateRule.getValue();
						try {
							affectedWidget.setValueAsString(value);
						} catch (JAXBException e) {
							// TODO: Clean up this
							throw new RuntimeException(e);
						}
					}
				}
		}
	}

	public void setCxlReplaceMode(boolean cxlReplaceMode) {
		this.cxlReplaceMode = cxlReplaceMode;
	}
	
}
