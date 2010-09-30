package org.atdl4j.ui.impl;

import org.apache.log4j.Logger;
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
	private final Logger logger = Logger.getLogger(BaseAtdl4jWidgetFactory.class);
	
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
	public Atdl4jWidget createCheckBoxListT(CheckBoxListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForCheckBoxListT();
		logger.debug( "createCheckBoxListT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForCheckBoxListT;
		try
		{
			atdl4jWidgetForCheckBoxListT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForCheckBoxListT != null )
		{
			atdl4jWidgetForCheckBoxListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForCheckBoxListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createCheckBoxT(CheckBoxT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForCheckBoxT();
		logger.debug( "createCheckBoxT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForCheckBoxT;
		try
		{
			atdl4jWidgetForCheckBoxT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForCheckBoxT != null )
		{
			atdl4jWidgetForCheckBoxT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForCheckBoxT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createClockT(ClockT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForClockT();
		logger.debug( "createClockT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForClockT;
		try
		{
			atdl4jWidgetForClockT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForClockT != null )
		{
			atdl4jWidgetForClockT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForClockT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForDoubleSpinnerT();
		logger.debug( "createDoubleSpinnerT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForDoubleSpinnerT;
		try
		{
			atdl4jWidgetForDoubleSpinnerT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForDoubleSpinnerT != null )
		{
			atdl4jWidgetForDoubleSpinnerT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForDoubleSpinnerT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createDropDownListT(DropDownListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForDropDownListT();
		logger.debug( "createDropDownListT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForDropDownListT;
		try
		{
			atdl4jWidgetForDropDownListT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForDropDownListT != null )
		{
			atdl4jWidgetForDropDownListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForDropDownListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createEditableDropDownListT(EditableDropDownListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForEditableDropDownListT();
		logger.debug( "createEditableDropDownListT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForEditableDropDownListT;
		try
		{
			atdl4jWidgetForEditableDropDownListT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForEditableDropDownListT != null )
		{
			atdl4jWidgetForEditableDropDownListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForEditableDropDownListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createHiddenFieldT(HiddenFieldT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForHiddenFieldT();
		logger.debug( "createHiddenFieldT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForHiddenFieldT;
		try
		{
			atdl4jWidgetForHiddenFieldT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForHiddenFieldT != null )
		{
			atdl4jWidgetForHiddenFieldT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForHiddenFieldT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createLabelT(LabelT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForLabelT();
		logger.debug( "createLabelT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForLabelT;
		try
		{
			atdl4jWidgetForLabelT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForLabelT != null )
		{
			atdl4jWidgetForLabelT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForLabelT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createMultiSelectListT(MultiSelectListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForMultiSelectListT();
		logger.debug( "createMultiSelectListT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForMultiSelectListT;
		try
		{
			atdl4jWidgetForMultiSelectListT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForMultiSelectListT != null )
		{
			atdl4jWidgetForMultiSelectListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForMultiSelectListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createRadioButtonListT(RadioButtonListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForRadioButtonListT();
		logger.debug( "createRadioButtonListT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForRadioButtonListT;
		try
		{
			atdl4jWidgetForRadioButtonListT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForRadioButtonListT != null )
		{
			atdl4jWidgetForRadioButtonListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForRadioButtonListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createRadioButtonT(RadioButtonT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForRadioButtonT();
		logger.debug( "createRadioButtonT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForRadioButtonT;
		try
		{
			atdl4jWidgetForRadioButtonT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForRadioButtonT != null )
		{
			atdl4jWidgetForRadioButtonT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForRadioButtonT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createSingleSelectListT(SingleSelectListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForSingleSelectListT();
		logger.debug( "createSingleSelectListT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForSingleSelectListT;
		try
		{
			atdl4jWidgetForSingleSelectListT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForSingleSelectListT != null )
		{
			atdl4jWidgetForSingleSelectListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForSingleSelectListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createSingleSpinnerT(SingleSpinnerT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForSingleSpinnerT();
		logger.debug( "createSingleSpinnerT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForSingleSpinnerT;
		try
		{
			atdl4jWidgetForSingleSpinnerT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForSingleSpinnerT != null )
		{
			atdl4jWidgetForSingleSpinnerT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForSingleSpinnerT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createSliderT(SliderT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForSliderT();
		logger.debug( "createSliderT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForSliderT;
		try
		{
			atdl4jWidgetForSliderT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForSliderT != null )
		{
			atdl4jWidgetForSliderT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForSliderT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public Atdl4jWidget createTextFieldT(TextFieldT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jWidgetForTextFieldT();
		logger.debug( "createTextFieldT() loading class named: " + tempClassName );
		Atdl4jWidget atdl4jWidgetForTextFieldT;
		try
		{
			atdl4jWidgetForTextFieldT = ((Class<Atdl4jWidget>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( atdl4jWidgetForTextFieldT != null )
		{
			atdl4jWidgetForTextFieldT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return atdl4jWidgetForTextFieldT;
	}

}
