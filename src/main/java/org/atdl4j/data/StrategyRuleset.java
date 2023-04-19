package org.atdl4j.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.validation.StrategyEditT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.ControlHelper;

public class StrategyRuleset
{
 
	private Map<StrategyEditT, ValidationRule> refRules;

	private List<ValidationRule> requiredFieldRules;
	private List<ValidationRule> constFieldRules;
	private List<ValidationRule> rangeFieldRules;
	private List<ValidationRule> lengthFieldRules;

	private List<ValidationRule> patternRules;

	public void addRequiredFieldRule(ValidationRule editUI)
	{
		requiredFieldRules.add( editUI );
	}

	public void addConstFieldRule(ValidationRule editUI)
	{
		constFieldRules.add( editUI );
	}

	public void addRangeFieldRule(ValidationRule editUI)
	{
		rangeFieldRules.add( editUI );
	}

	public void addLengthFieldRule(ValidationRule editUI)
	{
		lengthFieldRules.add( editUI );
	}

	public void addPatternRule(ValidationRule editUI)
	{
		patternRules.add( editUI );
	}

	public StrategyRuleset()
	{
		this.refRules = new HashMap<>();
		this.requiredFieldRules = new ArrayList<>();
		this.constFieldRules = new ArrayList<>();
		this.rangeFieldRules = new ArrayList<>();
		this.lengthFieldRules = new ArrayList<>();
		this.patternRules = new ArrayList<>();
	}

	public void putRefRule(StrategyEditT strategyEdit, ValidationRule rule)
	{
		this.refRules.put( strategyEdit, rule );
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, Atdl4jWidget<?>> parameters) 
		throws ValidationException
	{

		for ( ValidationRule requiredFieldRule : requiredFieldRules )
		{
			try
			{
				requiredFieldRule.validate( refRules, parameters );
			}
			catch (ValidationException e)
			{
				throw buildValidationException( e, " is required." );
			}
		}

		for ( ValidationRule constFieldRule : constFieldRules )
		{
			try
			{
				constFieldRule.validate( refRules, parameters );
			}
			catch (ValidationException e)
			{
				throw buildValidationException( e, " must remain a constant value." );
			}
		}

		for ( ValidationRule rangeFieldRule : rangeFieldRules )
		{
			try
			{
				rangeFieldRule.validate( refRules, parameters );
			}
			catch (ValidationException e)
			{
				throw buildValidationException( e, " is out of range (min/max bounds)." );
			}
		}

		for ( ValidationRule lengthFieldRule : lengthFieldRules )
		{
			try
			{
				lengthFieldRule.validate( refRules, parameters );
			}
			catch (ValidationException e)
			{
				throw buildValidationException( e, " length is out of range (min/max bounds)." );
			}
		}

		for ( ValidationRule patternRule : patternRules )
		{
			try
			{
				patternRule.validate( refRules, parameters );
			}
			catch (ValidationException e)
			{
				Atdl4jWidget<?> target = e.getTarget();
				String type = target.getClass().toString();
				throw buildValidationException( e, " of type " + type + " does not follow the required pattern." );
			}
		}

		for ( Entry<StrategyEditT, ValidationRule> entry : this.refRules.entrySet() )
		{
			StrategyEditT strategyEdit = entry.getKey();
			ValidationRule rule = entry.getValue();
			try
			{
				rule.validate( refRules, parameters );
			}
			catch (ValidationException e)
			{
				String tempValuesChecked = "";
				if ( e.getMessage() != null )
				{
					tempValuesChecked = "  " + e.getMessage();
				}
				throw new ValidationException( e.getTarget(), strategyEdit.getErrorMessage() + tempValuesChecked );
			}
		}
	}

	
	protected ValidationException buildValidationException( ValidationException aOriginalValidationException, String aMsgText )
	{
		Atdl4jWidget<?> tempAtdl4jWidget = aOriginalValidationException.getTarget();
		
		return new ValidationException( tempAtdl4jWidget, buildValidationExceptionText( aOriginalValidationException, aMsgText, tempAtdl4jWidget ) );
	}

	protected String buildValidationExceptionText( ValidationException aOriginalValidationException, String aMsgText, Atdl4jWidget<?> aAtdl4jWidget )
	{
		StringBuilder tmpStringBuilder = new StringBuilder();

		tmpStringBuilder.append("Parameter \"").append(aAtdl4jWidget.getParameter().getName()).append("\"");
		tmpStringBuilder.append(" (\"").append(ControlHelper.getLabelOrID(aAtdl4jWidget.getControl())).append("\") ");
		tmpStringBuilder.append(  aMsgText );
		if ( aOriginalValidationException.getMessage() != null )
		{
			tmpStringBuilder.append("  ").append(aOriginalValidationException.getMessage());
		}
		
		return tmpStringBuilder.toString();
	}

}
