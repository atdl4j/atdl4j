package org.atdl4j.ui;

import java.util.Map;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.StrategyRuleset;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.fix.FIXMessageBuilder;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;

public interface StrategyUI 
{
	/**
	 * @param strategy
	 * @param aStrategies
	 * @param aAtdl4jOptions (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 * @throws Atdl4jClassLoadException 
	 */
	public void init(StrategyT strategy, StrategiesT aStrategies, Atdl4jOptions aAtdl4jOptions, Map<String, ValidationRule> strategiesRules, Object parentContainer) throws FIXatdlFormatException, Atdl4jClassLoadException;
   
	public void validate() throws ValidationException;

	public StrategyT getStrategy();

	public String getFIXMessage();

	public void getFIXMessage(FIXMessageBuilder builder);

	public void setFIXMessage(String text) throws Atdl4jClassLoadException;
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of Atdl4jWidget<?> --
	public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetMap();

	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of Atdl4jWidget<?> --
	public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetWithParameterMap();

   public void setCxlReplaceMode(boolean cxlReplaceMode);

   public Atdl4jOptions getAtdl4jOptions();
	public Map<String, ParameterT> getParameterMap();
	public StrategyRuleset getStrategyRuleset();
	public Map<String, ValidationRule> getCompleteValidationRuleMap();

	public void reinitStrategyPanel() throws Atdl4jClassLoadException;
	
	public void relayoutCollapsibleStrategyPanels();
}