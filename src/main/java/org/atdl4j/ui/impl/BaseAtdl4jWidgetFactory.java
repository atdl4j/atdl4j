package org.atdl4j.ui.impl;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
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
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.Atdl4jWidgetFactory;

/*
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.

 * Note that this class does not need a SWT or Swing-specific implementation as it 
 * solely uses: ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance()
 * 
 * @author Scott Atwell 
 */
public class BaseAtdl4jWidgetFactory
		implements Atdl4jWidgetFactory
{	
	private Atdl4jOptions atdl4jOptions;
	
	// Invoke init() following no-arg constructor
	public BaseAtdl4jWidgetFactory() 
	{
	}

	public void init(Atdl4jOptions aAtdl4jOptions)
	{
		setAtdl4jOptions( aAtdl4jOptions );
	}
	
 	/* 
 	 * @param control
 	 * @param parameter
 	 * @return (for SWT returns SWTWidget<?>)
 	 */
 	public Atdl4jWidget<?> create(ControlT control, ParameterT parameter) 
	{
		if ( control instanceof CheckBoxT )
		{
			return createCheckBoxT( (CheckBoxT) control, parameter );
		}
		else if ( control instanceof DropDownListT )
		{
			return createDropDownListT( (DropDownListT) control, parameter );
		}
		else if ( control instanceof EditableDropDownListT )
		{
			return createEditableDropDownListT( (EditableDropDownListT) control, parameter );
		}
		else if ( control instanceof RadioButtonListT )
		{
			return createRadioButtonListT( (RadioButtonListT) control, parameter );
		}
		else if ( control instanceof TextFieldT )
		{
			return createTextFieldT( (TextFieldT) control, parameter );
		}
		else if ( control instanceof SliderT )
		{
			return createSliderT( (SliderT) control, parameter );
		}
		else if ( control instanceof CheckBoxListT )
		{
			// CheckBoxList must use a multiple value parameter
			if ( parameter == null || parameter instanceof MultipleStringValueT || parameter instanceof MultipleCharValueT )
			{
				return createCheckBoxListT( (CheckBoxListT) control, parameter );
			}
		}
		else if ( control instanceof ClockT )
		{
			if ( parameter == null || parameter instanceof LocalMktDateT || parameter instanceof MonthYearT || parameter instanceof UTCDateOnlyT
					|| parameter instanceof UTCTimeOnlyT || parameter instanceof UTCTimestampT )
			{ 
				return createClockT( (ClockT) control, parameter );
			}
		}
		else if ( control instanceof SingleSpinnerT )
		{
			// SingleSpinner must use a number parameter
			if ( parameter == null || parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT
					|| parameter instanceof SeqNumT || parameter instanceof NumInGroupT || parameter instanceof NumericT )
			{
				return createSingleSpinnerT( (SingleSpinnerT) control, parameter );
			}
		}
		else if ( control instanceof DoubleSpinnerT )
		{
			// DoubleSpinner must use a number parameter
			if ( parameter == null || parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT
					|| parameter instanceof SeqNumT || parameter instanceof NumInGroupT || parameter instanceof NumericT )
			{
				return createDoubleSpinnerT( (DoubleSpinnerT) control, parameter );
			}
		}
		else if ( control instanceof SingleSelectListT )
		{
			return createSingleSelectListT( (SingleSelectListT) control, parameter );
		}
		else if ( control instanceof MultiSelectListT )
		{
			// MultiSelectList must use a multiple value parameter
			if ( parameter == null || parameter instanceof MultipleStringValueT || parameter instanceof MultipleCharValueT )
			{
				return createMultiSelectListT( (MultiSelectListT) control, parameter );
			}
		}
		else if ( control instanceof HiddenFieldT )
		{
			return createHiddenFieldT( (HiddenFieldT) control, parameter );
		}
		else if ( control instanceof LabelT )
		{
			return createLabelT( (LabelT) control, parameter );
		}
		else if ( control instanceof RadioButtonT )
		{
			return createRadioButtonT( (RadioButtonT) control, parameter );
		}

		throw new IllegalStateException( "Control ID: \"" + control.getID() + "\" has unsupported Control type \"" + control.getClass().getSimpleName() + "\""
				+ ( parameter == null ? "" : " for Parameter type \"" + parameter.getClass().getSimpleName() + "\"" ) );

	}

	/**
	 * @param atdl4jOptions the atdl4jOptions to set
	 */
	protected void setAtdl4jOptions(Atdl4jOptions atdl4jOptions)
	{
		this.atdl4jOptions = atdl4jOptions;
	}

	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return atdl4jOptions;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createCheckBoxListT(CheckBoxListT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForCheckBoxListT = Atdl4jConfig.getConfig().createAtdl4jWidgetForCheckBoxListT();
	    atdl4jWidgetForCheckBoxListT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForCheckBoxListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createCheckBoxT(CheckBoxT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForCheckBoxT = Atdl4jConfig.getConfig().createAtdl4jWidgetForCheckBoxT();
	    atdl4jWidgetForCheckBoxT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForCheckBoxT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createClockT(ClockT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForClockT = Atdl4jConfig.getConfig().createAtdl4jWidgetForClockT();
	    atdl4jWidgetForClockT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForClockT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForDoubleSpinnerT = Atdl4jConfig.getConfig().createAtdl4jWidgetForDoubleSpinnerT();
	    atdl4jWidgetForDoubleSpinnerT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForDoubleSpinnerT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createDropDownListT(DropDownListT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForDropDownListT = Atdl4jConfig.getConfig().createAtdl4jWidgetForDropDownListT();
	    atdl4jWidgetForDropDownListT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForDropDownListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createEditableDropDownListT(EditableDropDownListT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForEditableDropDownListT = Atdl4jConfig.getConfig().createAtdl4jWidgetForEditableDropDownListT();
	    atdl4jWidgetForEditableDropDownListT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForEditableDropDownListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createHiddenFieldT(HiddenFieldT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForHiddenFieldT = Atdl4jConfig.getConfig().createAtdl4jWidgetForHiddenFieldT();
	    atdl4jWidgetForHiddenFieldT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForHiddenFieldT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createLabelT(LabelT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForLabelT = Atdl4jConfig.getConfig().createAtdl4jWidgetForLabelT();
	    atdl4jWidgetForLabelT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForLabelT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createMultiSelectListT(MultiSelectListT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForMultiSelectListT = Atdl4jConfig.getConfig().createAtdl4jWidgetForMultiSelectListT();
	    atdl4jWidgetForMultiSelectListT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForMultiSelectListT;
	}
	
	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createRadioButtonListT(RadioButtonListT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForRadioButtonListT = Atdl4jConfig.getConfig().createAtdl4jWidgetForRadioButtonListT();
	    atdl4jWidgetForRadioButtonListT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForRadioButtonListT;
	}
	
	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createRadioButtonT(RadioButtonT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForRadioButtonT = Atdl4jConfig.getConfig().createAtdl4jWidgetForRadioButtonT();
	    atdl4jWidgetForRadioButtonT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForRadioButtonT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createSingleSelectListT(SingleSelectListT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForSingleSelectListT = Atdl4jConfig.getConfig().createAtdl4jWidgetForSingleSelectListT();
	    atdl4jWidgetForSingleSelectListT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForSingleSelectListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createSingleSpinnerT(SingleSpinnerT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForSingleSpinnerT = Atdl4jConfig.getConfig().createAtdl4jWidgetForSingleSpinnerT();
	    atdl4jWidgetForSingleSpinnerT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForSingleSpinnerT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createSliderT(SliderT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForSliderT = Atdl4jConfig.getConfig().createAtdl4jWidgetForSliderT();
	    atdl4jWidgetForSliderT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForSliderT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget<?> createTextFieldT(TextFieldT control, ParameterT parameter)
	{
	    Atdl4jWidget<?> atdl4jWidgetForTextFieldT = Atdl4jConfig.getConfig().createAtdl4jWidgetForTextFieldT();
	    atdl4jWidgetForTextFieldT.init( control, parameter, getAtdl4jOptions() );
	    return atdl4jWidgetForTextFieldT;
	}
}
