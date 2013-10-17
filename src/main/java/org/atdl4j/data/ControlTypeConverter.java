
package org.atdl4j.data;

import org.atdl4j.fixatdl.layout.ControlT;


/**
 * An interface for an algorithmic parameter container class. Classes which implement
 * this interface hold parameter descriptor data but do not store a value (see the 
 * Atdl4jWidget class which stores the underlying FIX value.)
 */
public interface ControlTypeConverter<E extends Comparable<?>> 
{
	/**
	 * Converts Control's value to Parameter value.
	 * 
Used by: 
	- SWTClockWidget.getParameterValue()
	- AbstractHiddenFieldWidget.getParameterValue()
	- SWTSpinnerWidget.getParameterValue()
	- SWTTextFieldWidget.getParameterValue()
	
	 * @param value
	 * @return
	 */
	public Object convertControlValueToParameterValue(Object value);
	
	
	/**
	 * Converts Parameter value to Control value
	 * 
Used by: 
	- AbstractAtdl4jWidget.applyConstValue()
	- AbstractAtdl4jWidget.setFIXValue()
		- AbstractStrategyUI.setFIXMessage()
	
	 * @param value
	 * @param aControl
	 * @return
	 */
// 7/11/2010 Scott Atwell need to handle CheckBox control checkedEnumRef and uncheckedEnumRef (eg "100" -> true, "0" -> false)	public E convertParameterValueToControlValue(Object value);
	public E convertParameterValueToControlValue(Object value, ControlT aControl);

	
	/**
	 * Converts Control's value to Comparable for Control
	 * 
Used by:
	- AbstractAtdl4jWidget.convertStringToControlComparable()
		- ValueOperatorValidationRule.validate()
	- AbstractAtdl4jWidget.getControlValueAsComparable()
		- ValueOperatorValidationRule.validate()

	 * @param value
	 * @return
	 */
	public E convertControlValueToControlComparable(Object value);

	
	/**
	 * Converts aString (eg Control/@initValue or StateRule/@value) to Control value
	 * 
Used by:
	- AbstractAtdl4jWidget.convertStringToControlComparable()
		- ValueOperatorValidationRule.validate()
	- AbstractAtdl4jWidget.setValueAsString(String)
		- SWTStateListener.setBehaviorAsStateRule()
		
	 * @param aString
	 * @return
	 */
	public E convertStringToControlValue(String aString);

	/**
	 * @return the ParameterTypeConverter if Control has a Parameter
	 */
	public ParameterTypeConverter<?> getParameterTypeConverter();
	
	/**
	 * Returns an Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc)
	 * Returns aDatatypeIfNull if Parameter is null
	 * @param aDatatypeIfNull
	 * @return
	 */
	public Class<?> getParameterDatatype( Class<?> aClassIfNull );

}