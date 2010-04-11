
package org.atdl4j.data;


/**
 * An interface for an algorithmic parameter container class. Classes which implement
 * this interface hold parameter descriptor data but do not store a value (see the 
 * ControlUI class which stores the underlying FIX value.)
 */
public interface ControlTypeConverter<E extends Comparable<?>> 
{
	/**
	 * Converts Control's value to Parameter value.
	 * 
Used by: 
	- ClockWidget.getParameterValue()
	- HiddenFieldUI.getParameterValue()
	- SpinnerWidget.getParameterValue()
	- TextFieldWidget.getParameterValue()
	
	 * @param value
	 * @return
	 */
	public Object convertControlValueToParameterValue(Object value);
	
	
	/**
	 * Converts Parameter value to Control value
	 * 
Used by: 
	- AbstractControlUI.applyConstValue()
	- AbstractControlUI.setFIXValue()
		- AbstractStrategyUI.setFIXMessage()
	
	 * @param value
	 * @return
	 */
	public E convertParameterValueToControlValue(Object value);

	
	/**
	 * Converts Control's value to Comparable for Control
	 * 
Used by:
	- AbstractControlUI.convertStringToControlComparable()
		- ValueOperatorValidationRule.validate()
	- AbstractControlUI.getControlValueAsComparable()
		- ValueOperatorValidationRule.validate()

	 * @param value
	 * @return
	 */
	public E convertControlValueToControlComparable(Object value);

	
	/**
	 * Converts aString (eg Control/@initValue or StateRule/@value) to Control value
	 * 
Used by:
	- AbstractControlUI.convertStringToControlComparable()
		- ValueOperatorValidationRule.validate()
	- AbstractControlUI.setValueAsString(String)
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
	public Object getParameterDatatype( Object aDatatypeIfNull );

}