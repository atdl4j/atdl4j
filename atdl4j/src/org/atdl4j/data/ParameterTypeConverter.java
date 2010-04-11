
package org.atdl4j.data;

import org.atdl4j.fixatdl.core.ParameterT;

/**
 * An interface for an algorithmic parameter container class. Classes which implement
 * this interface hold parameter descriptor data but do not store a value (see the 
 * ControlUI class which stores the underlying FIX value.)
 */
public interface ParameterTypeConverter<E extends Comparable<?>> 
{
/*** 3/10/2010 Scott Atwell - before when using common TypeConveter
// 2/12/2010 Scott Atwell - differentiate Parameter value (0.25) vs. Control (25%)	public E convertValueToComparable(Object value) throws JAXBException;
	public E convertValueToParameterComparable(Object value) throws JAXBException;
	public E convertValueToControlComparable(Object value) throws JAXBException;
	
// 2/12/2010 Scott Atwell - differentiate Parameter value (0.25) vs. Control (25%)	public String convertValueToString(Object value) throws JAXBException;
	public String convertValueToParameterString(Object value) throws JAXBException;
	
// 3/9/2010 Scott Atwell renamed		public String convertValueToControlString(Object value) throws JAXBException;
	public String convertParameterValueToControlString(Object value) throws JAXBException;
	
// 3/9/2010 Scott Atwell -- further changes for PercentageT to always be "25%" in Control and Parameter gets "0.25" but can put "25" on wire with Parameter/@multiplyBy100="true"
	public E convertControlValueToParameterValue(Object value);
	public E convertParameterValueToControlValue(Object value);
	public E convertParameterValueToControlComparable(Object value) throws JAXBException;
	
	public ParameterT getParameter();
***/
	
	/**
	 * Convert a String value associated with the Parameter (eg Parameter/@minValue, Parameter/@maxValue, Parameter/@constValue, etc)
	 * to a raw Parameter value.
	 * 
Used by: AbstractControlUI.convertParameterStringToParameterComparable()
	- ValueOperatorValidationRule.validate()

	 * @param aParameterString
	 * @return
	 */
	public Object convertParameterStringToParameterValue(String aParameterString);
	
	/**
	 * Convert the FIX field wire value (from FIX message) to a raw Parameter value.
	 * 
Used by: AbstractControlUI.setFIXValue()

	 * @param aFixValue
	 * @return
	 */
	public Object convertFixWireValueToParameterValue(String aFixWireValue);
	
	/**
	 * Convert the raw Parameter value to FIX field's wire value (for a FIX message).
	 * 
Used by: AbstractControlUI.getParameterFixWireValue()
	- AbstractControlUI.getFIXValue()
	- LengthValidationRule.validate()
	- PatternValidationRule.validate()
	
	 * @param aParameterValue
	 * @return
	 */
	public String convertParameterValueToFixWireValue(Object aParameterValue);
	
	/**
	 * Convert the raw Parameter value to a Comparable (eg for StrategyEdit rule check)
	 * 
Used by: AbstractControlUI.getParameterValueAsComparable()
	- ValueOperatorValidationRule.validate()
Used by: AbstractControlUI.convertParameterStringToParameterComparable()
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