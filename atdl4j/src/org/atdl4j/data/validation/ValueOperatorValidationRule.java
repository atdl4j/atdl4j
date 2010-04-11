package org.atdl4j.data.validation;

import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.ControlUI;

/**
 * Validator that validates input against a constant value.
 * 
 * @author renato.gallart
 */
public class ValueOperatorValidationRule
		extends AbstractOperatorValidationRule
{

	private static final Logger logger = Logger.getLogger( ValueOperatorValidationRule.class );

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

	public void validate(Map<String, ValidationRule> refRules, Map<String, ControlUI<?>> targets) 
		throws ValidationException
	{

		// get the widget from context using field name
		ControlUI<?> target = targets.get( field );
		if ( target == null )
		{
			String tempMsg = "No parameter defined for field \"" + field + "\" in this context (ValueOperatorValidationRule) field: " + field
					+ " operator: " + operator + " value: " + value + " parent: " + parent + " refRules: " + refRules;
			String tempMsg2 = tempMsg + " targets: " + targets;
			logger.debug( tempMsg2 );
			logger.trace( tempMsg2, new Exception( "Stack trace" ) );

			throw new ValidationException( null, tempMsg );
		}

		/**
		 * Object fieldValue = parent instanceof StateRuleT ?
		 * target.getControlValueAsComparable() :
		 * target.getParameterValueAsComparable(); // 2/1/2010 Scott Atwell
		 * (handle StateRule specifying "0" vs. displayed "0.00") Object v = value
		 * != null ? target.convertStringToComparable(value) : null;
		 * 
		 * AbstractControlUI had (thus returning Parameter value even when we are
		 * comparing against Control's value for StateRuleT (divided it into two
		 * methods): public Comparable<?> convertStringToComparable(String string)
		 * throws JAXBException { if (parameterConverter != null) return
		 * parameterConverter.convertValueToComparable(string); else return
		 * controlConverter.convertValueToComparable(string); }
		 **/
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
			// 3/10/2010 Scott Atwell v =
			// target.convertStringToParameterComparable( value );
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
