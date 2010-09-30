
package org.atdl4j.data;

import org.atdl4j.fixatdl.core.ParameterT;

/**
 * An interface for an algorithmic parameter container class. Classes which implement
 * this interface hold parameter descriptor data but do not store a value (see the 
 * Atdl4jWidget class which stores the underlying FIX value.)
 */
public interface ParameterTypeConverter<E extends Comparable<?>> 
{
	/**
	 * Convert a String value associated with the Parameter (eg Parameter/@minValue, Parameter/@maxValue, Parameter/@constValue, etc)
	 * to a raw Parameter value.
	 * 
Used by: AbstractAtdl4jWidget.convertParameterStringToParameterComparable()
	- ValueOperatorValidationRule.validate()

	 * @param aParameterString
	 * @return
	 */
	public Object convertParameterStringToParameterValue(String aParameterString);
	
	/**
	 * Convert the FIX field wire value (from FIX message) to a raw Parameter value.
	 * 
Used by: AbstractAtdl4jWidget.setFIXValue()

	 * @param aFixValue
	 * @return
	 */
	public Object convertFixWireValueToParameterValue(String aFixWireValue);
	
	/**
	 * Convert the raw Parameter value to FIX field's wire value (for a FIX message).
	 * 
Used by: AbstractAtdl4jWidget.getParameterFixWireValue()
	- AbstractAtdl4jWidget.getFIXValue()
	- LengthValidationRule.validate()
	- PatternValidationRule.validate()
	
	 * @param aParameterValue
	 * @return
	 */
	public String convertParameterValueToFixWireValue(Object aParameterValue);
	
	/**
	 * Convert the raw Parameter value to a Comparable (eg for StrategyEdit rule check)
	 * 
Used by: AbstractAtdl4jWidget.getParameterValueAsComparable()
	- ValueOperatorValidationRule.validate()
Used by: AbstractAtdl4jWidget.convertParameterStringToParameterComparable()
	- ValueOperatorValidationRule.validate()
	 
	 * @param aParameterValue
	 * @return
	 */
	public E convertParameterValueToParameterComparable(Object aParameterValue);

	
	
	/**
	 * Convert the raw Parameter value to a String for comparison (eg for StrategyEdit rule check)
	 * 
Used by:  AbstractStrategyUI.buildParameters() for const/min/max value rule setup

	 * @param aParameterValue
	 * @return
	 */
	public String convertParameterValueToComparisonString(Object aParameterValue);
	
	/**
	 * @return the ParameterT
	 */
	public ParameterT getParameter();
	
	/**
	 * @returns an Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc)
	 */
	public Object getParameterDatatype();
	
}