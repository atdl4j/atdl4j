package org.atdl4j.data.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.AbstractHiddenFieldWidget;

/**
 * Validator that validates input against another existing field.
 * 
 * @author renato.gallart
 * @author john.shields
 */
public class Field2OperatorValidationRule extends
		AbstractOperatorValidationRule {

	private static final Logger logger = Logger.getLogger(Field2OperatorValidationRule.class);
	
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
		
		String tempMsg = "Field2OperatorValidationRule constructor: field: " + field + " operator: " + operator + " field2: " + field2 + " parent: " + parent;
		logger.debug( tempMsg );
		logger.trace( tempMsg, new Exception("Stack trace") ); 
	}

	public void validate(Map<String, ValidationRule> rules,
			Map<String, Atdl4jWidget<?>> targets) 
		throws ValidationException
	{
		// get the widget from context using field name
		Atdl4jWidget<?> target = targets.get(field);
		if (target == null) {
			String tempMsg = "No parameter defined for field \"" + field + "\" in this context (Field2OperatorValidationRule) field: " + field + " operator: " + operator + " field2: " + field2 + " parent: " + parent;
			String tempMsg2 = tempMsg + " targets: " + targets;
			logger.debug( tempMsg2 );
			logger.trace( tempMsg2, new Exception("Stack trace") ); 
			
			throw new ValidationException( null, tempMsg );
		}
		Object fieldValue = parent instanceof StateRuleT ? target.getControlValue() : target.getParameterValue();

		// get the widget from context using field2 name
		Atdl4jWidget<?> target2 = targets.get(field2);
		if (target2 == null) {
			String tempMsg = "No parameter defined for field \"" + field2 + "\" in this context (Field2OperatorValidationRule) field: " + field + " operator: " + operator + " field2: " + field2 + " parent: " + parent;
			String tempMsg2 = tempMsg + " targets: " + targets;
			logger.debug( tempMsg2 );
			logger.trace( tempMsg2, new Exception("Stack trace") ); 
			
			throw new ValidationException( null, tempMsg );
		}
		Object fieldValue2 = parent instanceof StateRuleT ? target2.getControlValue() : target2.getParameterValue();
		
		// -- handles converting data type to "expected" for FIX_ "standard" input values --
		if ( ( fieldValue != null ) && 
			  ( fieldValue2 != null ) && 
			  ( fieldValue.getClass() != fieldValue2.getClass() ) )
		{
			if ( field.startsWith( InputAndFilterData.FIX_DEFINED_FIELD_PREFIX ) )
			{
				fieldValue = convertFieldValueToDesiredType( fieldValue2, fieldValue );
			}
			else if ( field2.startsWith( InputAndFilterData.FIX_DEFINED_FIELD_PREFIX ) )
			{
				fieldValue2 = convertFieldValueToDesiredType( fieldValue, fieldValue2 );
			}
			else if ( ( target != null ) && ( target instanceof AbstractHiddenFieldWidget ) )
			{
				fieldValue = convertFieldValueToDesiredType( fieldValue2, fieldValue );
			}
			else if ( ( target2 != null ) && ( target2 instanceof AbstractHiddenFieldWidget ) )
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
			logger.warn( "convertFieldValueToDesiredType() unhandled Class type: " + aFieldWithDesiredType.getClass().getName() );			
			return aFieldValue;
		}
	}
	
	
}
