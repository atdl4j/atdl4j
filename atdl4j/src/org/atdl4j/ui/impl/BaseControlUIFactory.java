package org.atdl4j.ui.impl;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.IntT;
import org.atdl4j.fixatdl.core.LengthT;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.NumInGroupT;
import org.atdl4j.fixatdl.core.NumericT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.SeqNumT;
import org.atdl4j.fixatdl.core.TagNumT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.LabelT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.fixatdl.layout.TextFieldT;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.ControlUIFactory;

/*
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.

 * Note that this class does not need a SWT or Swing-specific implementation as it solely uses aAtdl4jConfig.get____().
 * @author Scott Atwell 
 */
public class BaseControlUIFactory
		implements ControlUIFactory
{
	private Atdl4jConfig atdl4jConfig;
	
	// Invoke init() following no-arg constructor
	public BaseControlUIFactory() 
	{
	}

	public void init(Atdl4jConfig aAtdl4jConfig)
	{
		setAtdl4jConfig( aAtdl4jConfig );
	}
	
 	/* 
 	 * @param control
 	 * @param parameter
 	 * @return (for SWT returns SWTWidget<?>)
 	 */
 	public ControlUI<?> create(ControlT control, ParameterT parameter) 
	{
		if ( control instanceof CheckBoxT )
		{
			return getAtdl4jConfig().getControlUIForCheckBoxT( (CheckBoxT) control, parameter );
		}
		else if ( control instanceof DropDownListT )
		{
			return getAtdl4jConfig().getControlUIForDropDownListT( (DropDownListT) control, parameter );
		}
		else if ( control instanceof EditableDropDownListT )
		{
			return getAtdl4jConfig().getControlUIForEditableDropDownListT( (EditableDropDownListT) control, parameter );
		}
		else if ( control instanceof RadioButtonListT )
		{
			return getAtdl4jConfig().getControlUIForRadioButtonListT( (RadioButtonListT) control, parameter );
		}
		else if ( control instanceof TextFieldT )
		{
			return getAtdl4jConfig().getControlUIForTextFieldT( (TextFieldT) control, parameter );
		}
		else if ( control instanceof SliderT )
		{
			return getAtdl4jConfig().getControlUIForSliderT( (SliderT) control, parameter );
		}
		else if ( control instanceof CheckBoxListT )
		{
			// CheckBoxList must use a multiple value parameter
			if ( parameter == null || parameter instanceof MultipleStringValueT || parameter instanceof MultipleCharValueT )
			{
				return getAtdl4jConfig().getControlUIForCheckBoxListT( (CheckBoxListT) control, parameter );
			}
		}
		else if ( control instanceof ClockT )
		{
			if ( parameter == null || parameter instanceof LocalMktDateT || parameter instanceof MonthYearT || parameter instanceof UTCDateOnlyT
					|| parameter instanceof UTCTimeOnlyT || parameter instanceof UTCTimestampT )
			{ 
				return getAtdl4jConfig().getControlUIForClockT( (ClockT) control, parameter );
			}
		}
		else if ( control instanceof SingleSpinnerT )
		{
			// SingleSpinner must use a number parameter
			if ( parameter == null || parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT
					|| parameter instanceof SeqNumT || parameter instanceof NumInGroupT || parameter instanceof NumericT )
			{
				return getAtdl4jConfig().getControlUIForSingleSpinnerT( (SingleSpinnerT) control, parameter );
			}
		}
		else if ( control instanceof DoubleSpinnerT )
		{
			// DoubleSpinner must use a number parameter
			if ( parameter == null || parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT
					|| parameter instanceof SeqNumT || parameter instanceof NumInGroupT || parameter instanceof NumericT )
			{
				return getAtdl4jConfig().getControlUIForDoubleSpinnerT( (DoubleSpinnerT) control, parameter );
			}
		}
		else if ( control instanceof SingleSelectListT )
		{
			return getAtdl4jConfig().getControlUIForSingleSelectListT( (SingleSelectListT) control, parameter );
		}
		else if ( control instanceof MultiSelectListT )
		{
			// MultiSelectList must use a multiple value parameter
			if ( parameter == null || parameter instanceof MultipleStringValueT || parameter instanceof MultipleCharValueT )
			{
				return getAtdl4jConfig().getControlUIForMultiSelectListT( (MultiSelectListT) control, parameter );
			}
		}
		else if ( control instanceof HiddenFieldT )
		{
			return getAtdl4jConfig().getControlUIForHiddenFieldT( (HiddenFieldT) control, parameter );
		}
		else if ( control instanceof LabelT )
		{
			return getAtdl4jConfig().getControlUIForLabelT( (LabelT) control, parameter );
		}
		else if ( control instanceof RadioButtonT )
		{
			return getAtdl4jConfig().getControlUIForRadioButtonT( (RadioButtonT) control, parameter );
		}

		throw new IllegalStateException( "Control ID: \"" + control.getID() + "\" has unsupported Control type \"" + control.getClass().getSimpleName() + "\""
				+ ( parameter == null ? "" : " for Parameter type \"" + parameter.getClass().getSimpleName() + "\"" ) );

	}

	/**
	 * @param atdl4jConfig the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
	{
		this.atdl4jConfig = atdl4jConfig;
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}

}
