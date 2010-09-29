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
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.ControlUIFactory;

/*
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.

 * Note that this class does not need a SWT or Swing-specific implementation as it solely uses aAtdl4jOptions.get____().
 * @author Scott Atwell 
 */
public class BaseControlUIFactory
		implements ControlUIFactory
{
	private final Logger logger = Logger.getLogger(BaseControlUIFactory.class);
	
	private Atdl4jOptions atdl4jOptions;
	
	// Invoke init() following no-arg constructor
	public BaseControlUIFactory() 
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
 	public ControlUI<?> create(ControlT control, ParameterT parameter) 
	{
		if ( control instanceof CheckBoxT )
		{
			return createControlUIForCheckBoxT( (CheckBoxT) control, parameter );
		}
		else if ( control instanceof DropDownListT )
		{
			return createControlUIForDropDownListT( (DropDownListT) control, parameter );
		}
		else if ( control instanceof EditableDropDownListT )
		{
			return createControlUIForEditableDropDownListT( (EditableDropDownListT) control, parameter );
		}
		else if ( control instanceof RadioButtonListT )
		{
			return createControlUIForRadioButtonListT( (RadioButtonListT) control, parameter );
		}
		else if ( control instanceof TextFieldT )
		{
			return createControlUIForTextFieldT( (TextFieldT) control, parameter );
		}
		else if ( control instanceof SliderT )
		{
			return createControlUIForSliderT( (SliderT) control, parameter );
		}
		else if ( control instanceof CheckBoxListT )
		{
			// CheckBoxList must use a multiple value parameter
			if ( parameter == null || parameter instanceof MultipleStringValueT || parameter instanceof MultipleCharValueT )
			{
				return createControlUIForCheckBoxListT( (CheckBoxListT) control, parameter );
			}
		}
		else if ( control instanceof ClockT )
		{
			if ( parameter == null || parameter instanceof LocalMktDateT || parameter instanceof MonthYearT || parameter instanceof UTCDateOnlyT
					|| parameter instanceof UTCTimeOnlyT || parameter instanceof UTCTimestampT )
			{ 
				return createControlUIForClockT( (ClockT) control, parameter );
			}
		}
		else if ( control instanceof SingleSpinnerT )
		{
			// SingleSpinner must use a number parameter
			if ( parameter == null || parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT
					|| parameter instanceof SeqNumT || parameter instanceof NumInGroupT || parameter instanceof NumericT )
			{
				return createControlUIForSingleSpinnerT( (SingleSpinnerT) control, parameter );
			}
		}
		else if ( control instanceof DoubleSpinnerT )
		{
			// DoubleSpinner must use a number parameter
			if ( parameter == null || parameter instanceof IntT || parameter instanceof TagNumT || parameter instanceof LengthT
					|| parameter instanceof SeqNumT || parameter instanceof NumInGroupT || parameter instanceof NumericT )
			{
				return createControlUIForDoubleSpinnerT( (DoubleSpinnerT) control, parameter );
			}
		}
		else if ( control instanceof SingleSelectListT )
		{
			return createControlUIForSingleSelectListT( (SingleSelectListT) control, parameter );
		}
		else if ( control instanceof MultiSelectListT )
		{
			// MultiSelectList must use a multiple value parameter
			if ( parameter == null || parameter instanceof MultipleStringValueT || parameter instanceof MultipleCharValueT )
			{
				return createControlUIForMultiSelectListT( (MultiSelectListT) control, parameter );
			}
		}
		else if ( control instanceof HiddenFieldT )
		{
			return createControlUIForHiddenFieldT( (HiddenFieldT) control, parameter );
		}
		else if ( control instanceof LabelT )
		{
			return createControlUIForLabelT( (LabelT) control, parameter );
		}
		else if ( control instanceof RadioButtonT )
		{
			return createControlUIForRadioButtonT( (RadioButtonT) control, parameter );
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
	public ControlUI createControlUIForCheckBoxListT(CheckBoxListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForCheckBoxListT();
		logger.debug( "createControlUIForCheckBoxListT() loading class named: " + tempClassName );
		ControlUI controlUIForCheckBoxListT;
		try
		{
			controlUIForCheckBoxListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForCheckBoxListT != null )
		{
			controlUIForCheckBoxListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForCheckBoxListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForCheckBoxT(CheckBoxT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForCheckBoxT();
		logger.debug( "createControlUIForCheckBoxT() loading class named: " + tempClassName );
		ControlUI controlUIForCheckBoxT;
		try
		{
			controlUIForCheckBoxT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForCheckBoxT != null )
		{
			controlUIForCheckBoxT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForCheckBoxT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForClockT(ClockT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForClockT();
		logger.debug( "createControlUIForClockT() loading class named: " + tempClassName );
		ControlUI controlUIForClockT;
		try
		{
			controlUIForClockT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForClockT != null )
		{
			controlUIForClockT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForClockT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForDoubleSpinnerT();
		logger.debug( "createControlUIForDoubleSpinnerT() loading class named: " + tempClassName );
		ControlUI controlUIForDoubleSpinnerT;
		try
		{
			controlUIForDoubleSpinnerT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForDoubleSpinnerT != null )
		{
			controlUIForDoubleSpinnerT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForDoubleSpinnerT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForDropDownListT(DropDownListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForDropDownListT();
		logger.debug( "createControlUIForDropDownListT() loading class named: " + tempClassName );
		ControlUI controlUIForDropDownListT;
		try
		{
			controlUIForDropDownListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForDropDownListT != null )
		{
			controlUIForDropDownListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForDropDownListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForEditableDropDownListT(EditableDropDownListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForEditableDropDownListT();
		logger.debug( "createControlUIForEditableDropDownListT() loading class named: " + tempClassName );
		ControlUI controlUIForEditableDropDownListT;
		try
		{
			controlUIForEditableDropDownListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForEditableDropDownListT != null )
		{
			controlUIForEditableDropDownListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForEditableDropDownListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForHiddenFieldT(HiddenFieldT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForHiddenFieldT();
		logger.debug( "createControlUIForHiddenFieldT() loading class named: " + tempClassName );
		ControlUI controlUIForHiddenFieldT;
		try
		{
			controlUIForHiddenFieldT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForHiddenFieldT != null )
		{
			controlUIForHiddenFieldT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForHiddenFieldT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForLabelT(LabelT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForLabelT();
		logger.debug( "createControlUIForLabelT() loading class named: " + tempClassName );
		ControlUI controlUIForLabelT;
		try
		{
			controlUIForLabelT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForLabelT != null )
		{
			controlUIForLabelT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForLabelT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForMultiSelectListT(MultiSelectListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForMultiSelectListT();
		logger.debug( "createControlUIForMultiSelectListT() loading class named: " + tempClassName );
		ControlUI controlUIForMultiSelectListT;
		try
		{
			controlUIForMultiSelectListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForMultiSelectListT != null )
		{
			controlUIForMultiSelectListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForMultiSelectListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForRadioButtonListT(RadioButtonListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForRadioButtonListT();
		logger.debug( "createControlUIForRadioButtonListT() loading class named: " + tempClassName );
		ControlUI controlUIForRadioButtonListT;
		try
		{
			controlUIForRadioButtonListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForRadioButtonListT != null )
		{
			controlUIForRadioButtonListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForRadioButtonListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForRadioButtonT(RadioButtonT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForRadioButtonT();
		logger.debug( "createControlUIForRadioButtonT() loading class named: " + tempClassName );
		ControlUI controlUIForRadioButtonT;
		try
		{
			controlUIForRadioButtonT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForRadioButtonT != null )
		{
			controlUIForRadioButtonT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForRadioButtonT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForSingleSelectListT(SingleSelectListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForSingleSelectListT();
		logger.debug( "createControlUIForSingleSelectListT() loading class named: " + tempClassName );
		ControlUI controlUIForSingleSelectListT;
		try
		{
			controlUIForSingleSelectListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForSingleSelectListT != null )
		{
			controlUIForSingleSelectListT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForSingleSelectListT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForSingleSpinnerT(SingleSpinnerT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForSingleSpinnerT();
		logger.debug( "createControlUIForSingleSpinnerT() loading class named: " + tempClassName );
		ControlUI controlUIForSingleSpinnerT;
		try
		{
			controlUIForSingleSpinnerT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForSingleSpinnerT != null )
		{
			controlUIForSingleSpinnerT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForSingleSpinnerT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForSliderT(SliderT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForSliderT();
		logger.debug( "createControlUIForSliderT() loading class named: " + tempClassName );
		ControlUI controlUIForSliderT;
		try
		{
			controlUIForSliderT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForSliderT != null )
		{
			controlUIForSliderT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForSliderT;
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI createControlUIForTextFieldT(TextFieldT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = Atdl4jConfig.getConfig().getClassNameControlUIForTextFieldT();
		logger.debug( "createControlUIForTextFieldT() loading class named: " + tempClassName );
		ControlUI controlUIForTextFieldT;
		try
		{
			controlUIForTextFieldT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForTextFieldT != null )
		{
			controlUIForTextFieldT.init( control, parameter, getAtdl4jOptions() );
		}
		
		return controlUIForTextFieldT;
	}

}
