package org.atdl4j.data.validation;

import java.util.Map;

import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.fixatdl.validation.EditRefT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.fixatdl.validation.LogicOperatorT;
import org.atdl4j.fixatdl.validation.OperatorT;

public abstract class ValidationRuleFactory
{
	// TODO: refactor so outer method adds to refRules, not inner
	public static ValidationRule createRule(EditT edit, Map<String, ValidationRule> refRules, Object parent) throws FIXatdlFormatException
	{
		if ( edit.getLogicOperator() != null )
		{
			// edit represents a logical operator [AND|OR|NOT|XOR]
			LogicOperatorT operator = edit.getLogicOperator();
			LogicalOperatorValidationRule rule = new LogicalOperatorValidationRule( operator, parent );
			if ( edit.getId() != null )
				refRules.put( edit.getId(), rule );

			if ( edit.getEdit() != null )
			{
				for ( EditT innerEdit : edit.getEdit() )
				{
					ValidationRule innerRule = createRule( innerEdit, refRules, parent );
					rule.addRule( innerRule );
				}
			}
			if ( edit.getEditRef() != null )
			{
				for ( EditRefT innerRefEdit : edit.getEditRef() )
				{
					rule.addRule( createRule( innerRefEdit ) );
				}
			}
			return rule;

		}
		else if ( edit.getOperator() != null )
		{
			// edit represents a simple operator [EX|NX|EQ|LT|GT|NE|LE|GE]
			OperatorT operator = edit.getOperator();
			if ( edit.getField() != null )
			{
				String field = edit.getField();
				if ( edit.getValue() != null )
				{
					// validates against a constant value
					String value = edit.getValue();
					ValidationRule rule = new ValueOperatorValidationRule( field, operator, value, parent );
					if ( edit.getId() != null )
						refRules.put( edit.getId(), rule );

					return rule;

				}
				else if ( edit.getField2() != null )
				{
					// validates against another field value
					String field2 = edit.getField2();
					ValidationRule rule = new Field2OperatorValidationRule( field, operator, field2, parent );
					if ( edit.getId() != null )
						refRules.put( edit.getId(), rule );

					return rule;

				}
				else
				{
					// must be EX or NX
					if ( operator == OperatorT.EX || operator == OperatorT.NX )
					{

						ValidationRule rule = new ValueOperatorValidationRule( field, operator, null, parent );
						if ( edit.getId() != null )
							refRules.put( edit.getId(), rule );

						return rule;
					}
					else
					{
						throw new FIXatdlFormatException( "Operator must be EX or NX when there is no \"value\" of \"field2\" attribute" );
					}
				}
			}
			else
			{
				throw new FIXatdlFormatException( "No field set for edit  \"" + edit.getId() + "\"" );
			}
		}
		else
		{
			throw new FIXatdlFormatException( "No logic operator or operator set for edit \"" + edit.getId() + "\"" );
		}
	}

	public static ValidationRule createRule(EditRefT editRef) throws FIXatdlFormatException
	{
		if ( editRef.getId() != null )
		{
			String id = editRef.getId();
			return new ReferencedValidationRule( id );
		}
		else
		{
			throw new FIXatdlFormatException( "EditRef without an id" );
		}
	}

}
