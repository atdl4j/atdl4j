package org.atdl4j.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.validation.StrategyEditT;
import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.ControlUI;

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
		this.refRules = new HashMap<StrategyEditT, ValidationRule>();
		this.requiredFieldRules = new ArrayList<ValidationRule>();
		this.constFieldRules = new ArrayList<ValidationRule>();
		this.rangeFieldRules = new ArrayList<ValidationRule>();
		this.lengthFieldRules = new ArrayList<ValidationRule>();
		this.patternRules = new ArrayList<ValidationRule>();
	}

	public void putRefRule(StrategyEditT strategyEdit, ValidationRule rule)
	{
		this.refRules.put( strategyEdit, rule );
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, ControlUI<?>> parameters) 
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
// 3/10/2010 Scott Atwell				ControlUI<?> target = e.getTarget();
// 3/10/2010 Scott Atwell				String name = target.getParameter().getName();
// 3/10/2010 Scott Atwell				throw new ValidationException( target, "Parameter \"" + name + "\" is required." );
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
// 3/10/2010 Scott Atwell				ControlUI<?> target = e.getTarget();
// 3/10/2010 Scott Atwell				String name = target.getParameter().getName();
// 3/10/2010 Scott Atwell			throw new ValidationException( target, "Parameter \"" + name + "\" must remain a constant value." );
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
// 3/10/2010 Scott Atwell				ControlUI<?> target = e.getTarget();
// 3/10/2010 Scott Atwell				String name = target.getParameter().getName();
// 3/10/2010 Scott Atwell				throw new ValidationException( target, "Parameter \"" + name + "\" is out of range (min/max bounds)." );
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
// 3/10/2010 Scott Atwell				ControlUI<?> target = e.getTarget();
// 3/10/2010 Scott Atwell				String name = target.getParameter().getName();
// 3/10/2010 Scott Atwell				throw new ValidationException( target, "Parameter \"" + name + "\" length is out of range (min/max bounds)." );
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
				ControlUI<?> target = e.getTarget();
// 3/10/2010 Scott Atwell				String name = target.getParameter().getName();
				String type = target.getClass().toString();
// 3/10/2010 Scott Atwell				throw new ValidationException( target, "Field \"" + name + "\" of type " + type + " does not follow the required pattern." );
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
//				throw new ValidationException( e.getTarget(), strategyEdit.getErrorMessage() );
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
		ControlUI<?> tempControlUI = aOriginalValidationException.getTarget();
		
		return new ValidationException( tempControlUI, buildValidationExceptionText( aOriginalValidationException, aMsgText, tempControlUI ) );
	}

	protected String buildValidationExceptionText( ValidationException aOriginalValidationException, String aMsgText, ControlUI<?> aControlUI )
	{
		StringBuffer tempStringBuffer = new StringBuffer();
		
		tempStringBuffer.append( "Parameter \"" + aControlUI.getParameter().getName() + "\"" );
		tempStringBuffer.append( " (\"" + ControlHelper.getLabelOrID( aControlUI.getControl() ) + "\") " );
		tempStringBuffer.append(  aMsgText );
		if ( aOriginalValidationException.getMessage() != null )
		{ 
			tempStringBuffer.append( "  " + aOriginalValidationException.getMessage() );
		}
		
		return tempStringBuffer.toString();
	}

}
