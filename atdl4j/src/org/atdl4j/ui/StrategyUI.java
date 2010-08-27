package org.atdl4j.ui;

import java.util.Map;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.StrategyRuleset;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.StrategyT;

public interface StrategyUI 
{
	/**
	 * @param strategy
	 * @param aAtdl4jConfig (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 */
	public void init(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Object parentContainer);
   
	public void validate() throws ValidationException;

	public StrategyT getStrategy();

	public String getFIXMessage();

	public void getFIXMessage(FIXMessageBuilder builder);

	public void setFIXMessage(String text);
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	public Map<String, ControlUI<?>> getControlUIMap();

	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	public Map<String, ControlUI<?>> getControlUIWithParameterMap();

   public void setCxlReplaceMode(boolean cxlReplaceMode);

   
   public Atdl4jConfig getAtdl4jConfig();
	public Map<String, ParameterT> getParameterMap();
	public StrategyRuleset getStrategyRuleset();
	public Map<String, ValidationRule> getCompleteValidationRuleMap();

	public void reinitStrategyPanel();
	
// 8/27/2010 Scott Atwell added
	public void relayoutCollapsibleStrategyPanels();
}