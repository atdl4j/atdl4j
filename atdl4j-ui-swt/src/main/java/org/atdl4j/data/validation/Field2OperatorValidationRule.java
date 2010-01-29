package org.atdl4j.data.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.xml.bind.JAXBException;


import org.atdl4j.ui.ControlUI;
import org.atdl4j.data.InputAndFilterData;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.fixprotocol.atdl_1_1.flow.StateRuleT;
import org.fixprotocol.atdl_1_1.validation.OperatorT;

/**
 * Validator that validates input against another existing field.
 * 
 * @author renato.gallart
 * @author john.shields
 */
public class Field2OperatorValidationRule extends
		AbstractOperatorValidationRule {

	private String field;

	private OperatorT operator;

	private String field2;
	
	private Object parent; // Can be either StrategyEdit or StateRule

	public Field2OperatorValidationRule(String field, OperatorT operator,
			String field2, Object parent) {
		this.field = field;
		this.operator = operator;
		this.field2 = field2;
		this.parent = parent;
	}

	public void validate(Map<String, ValidationRule> rules,
			Map<String, ControlUI<?>> targets) throws ValidationException,
			JAXBException {

		// get the widget from context using field name
		ControlUI<?> target = targets.get(field);
		if (target == null) {
			throw new JAXBException("No parameter defined for field \"" + field
					+ "\" in this context");
		}
		Object fieldValue = parent instanceof StateRuleT ? target.getControlValue() : target.getParameterValue();

		// get the widget from context using field2 name
		ControlUI<?> target2 = targets.get(field2);
		if (target2 == null) {
			throw new JAXBException("No parameter defined for field2 \"" + field2
					+ "\" in this context");
		}
		Object fieldValue2 = parent instanceof StateRuleT ? target2.getControlValue() : target2.getParameterValue();
		
//TODO 1/17/2010 Scott Atwell need to handle converting data type to "expected" for FIX_ "standard" input values
if ( ( fieldValue != null ) && 
	  ( fieldValue2 != null ) && 
	  ( fieldValue.getClass() != fieldValue2.getClass() ) )
{
	if ( field.startsWith( InputAndFilterData.FIX_STANDARD_FIELD_INPUT_FIELD_NAME_PREFIX ) )
	{
		fieldValue = convertFieldValueToDesiredType( fieldValue2, fieldValue );
	}
	else if ( field2.startsWith( InputAndFilterData.FIX_STANDARD_FIELD_INPUT_FIELD_NAME_PREFIX ) )
	{
		fieldValue2 = convertFieldValueToDesiredType( fieldValue, fieldValue2 );
	}
}
		
		
		// compare both values
		validateValues(target, fieldValue, operator, fieldValue2);
	}

	public String getField() {
		return field;
	}
	public String getField2() {
		return field2;
	}
	
	
	
//TODO 1/17/2010 Scott Atwell added
	public static Object convertFieldValueToDesiredType( Object aFieldWithDesiredType, Object aFieldValue )
	{
		if ( aFieldWithDesiredType instanceof String )
		{
			return aFieldValue.toString();
		}
		else if( aFieldWithDesiredType instanceof BigDecimal ) 
		{
			return new BigDecimal( aFieldValue.toString() );
		}
		else if( aFieldWithDesiredType instanceof BigInteger ) 
		{
			return new BigInteger( aFieldValue.toString() );
		}
		else if( aFieldWithDesiredType instanceof Double ) 
		{
			return new Double( aFieldValue.toString() );
		}
		else if( aFieldWithDesiredType instanceof Integer ) 
		{
			return new Integer( aFieldValue.toString() );
		}
		else
		{
System.out.println("convertFieldValueToDesiredType() unhandled Class type: " + aFieldWithDesiredType.getClass().getName() );			
			return aFieldValue;
		}
	}
	
	
}
