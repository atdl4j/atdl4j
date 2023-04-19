package org.atdl4j.data.validation;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.Atdl4jWidget;

/**
 * Validator that validates input against a constant value.
 * 
 * @author renato.gallart
 */
public class ValueOperatorValidationRule
		extends AbstractOperatorValidationRule
{

	private static final Logger logger = LoggerFactory.getLogger( ValueOperatorValidationRule.class );

	private String field;

	private OperatorT operator;

	private String value;

	private Object parent; // Can be either StrategyEdit or StateRule

	public ValueOperatorValidationRule(String field, OperatorT operator, String value, Object parent)
	{
		this.field = field;
		this.operator = operator;
		this.value = value;
		this.parent = parent;

		String tempMsg = "ValueOperatorValidationRule constructor: field: " + field + " operator: " + operator + " value: " + value + " parent: "
				+ parent;
		logger.debug( tempMsg );
		logger.trace( tempMsg, new Exception( "Stack trace" ) );
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, Atdl4jWidget<?>> targets) 
		throws ValidationException
	{

		// get the widget from context using field name
		Atdl4jWidget<?> target = targets.get( field );
		if ( target == null )
		{
			// -- Handle "FIX_fieldname" of "NX" --
			if ( ( field != null ) && 
				  ( field.startsWith( InputAndFilterData.FIX_DEFINED_FIELD_PREFIX ) )&&
				  ( operator == OperatorT.NX ) )
			{
				logger.debug( "Special handling to accept NOT EXISTS (\"NX\") check for FIX_DEFINED_FIELD_PREFIX field: {} avoiding: \"No parameter defined for field\".", field );
				return;
			}

			String tempMsg = "No parameter defined for field \"" + field + "\" in this context (ValueOperatorValidationRule) field: " + field
					+ " operator: " + operator + " value: " + value + " parent: " + parent + " refRules: " + refRules;
			String tempMsg2 = tempMsg + " targets: " + targets;
			logger.debug( tempMsg2 );
			logger.trace( tempMsg2, new Exception( "Stack trace" ) );

			throw new ValidationException( null, tempMsg );
		}

		Object fieldValue;
		Object v;
		if ( parent instanceof StateRuleT ) // Uses Control "display" values
		{
			fieldValue = target.getControlValueAsComparable();
			v = target.convertStringToControlComparable( value );
		}
		else
		// StrategyEditT uses Parameter values
		{
			fieldValue = target.getParameterValueAsComparable();
			v = target.convertParameterStringToParameterComparable( value );
		}

		validateValues( target, fieldValue, operator, v );
	}

	public String getField()
	{
		return field;
	}

	/**
	 * @return the operator
	 */
	public OperatorT getOperator()
	{
		return this.operator;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return this.value;
	}
}
