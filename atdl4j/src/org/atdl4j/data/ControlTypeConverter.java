
package org.atdl4j.data;


/**
 * An interface for an algorithmic parameter container class. Classes which implement
 * this interface hold parameter descriptor data but do not store a value (see the 
 * ControlUI class which stores the underlying FIX value.)
 */
public interface ControlTypeConverter<E extends Comparable<?>> 
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
// 3/10/2010 Scott Atwell	public E convertControlValueToParameterValue(Object value);
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
//	public E convertParameterValueToControlComparable(Object value);
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
// 3/10/2010 Scott Atwell	public E convertValueToControlComparable(Object value);
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
// 3/11/2010 Scott Atwell	public ParameterT getParameter();
	public ParameterTypeConverter<?> getParameterTypeConverter();
	
	/**
	 * Returns an Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc)
	 * Returns aDatatypeIfNull if Parameter is null
	 * @param aDatatypeIfNull
	 * @return
	 */
	public Object getParameterDatatype( Object aDatatypeIfNull );

}