package org.atdl4j.ui.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.ControlTypeConverter;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.ParameterHelper;
import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.data.fix.PlainFIXMessageBuilder;
import org.atdl4j.data.fix.Tag959Helper;
import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.ui.ControlHelper;
import org.atdl4j.ui.ControlUI;

/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractControlUI<E extends Comparable<?>>
		implements ControlUI<E>
{
	private final Logger logger = Logger.getLogger( AbstractControlUI.class );

	protected ParameterT parameter;
	protected ControlT control;
// 3/10/2010 Scott Atwell	protected AbstractTypeConverter<E> controlConverter;
// 3/10/2010 Scott Atwell	protected AbstractTypeConverter<?> parameterConverter;
	protected ControlTypeConverter<E> controlConverter;
	protected ParameterTypeConverter<?> parameterConverter;

	private Atdl4jConfig atdl4jConfig;

	// 2/10/2010 Scott Atwell added
	Boolean nullValue = null; // undefined state

	// 2/11/2010 Scott Atwell
	E lastNonNullStateControlValueRaw;

	// 3/13/2010 Scott Atwell
	private boolean hiddenFieldForInputAndFilterData = false;


	public void init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig)
	{
		control = aControl;
		parameter = aParameter;
		setAtdl4jConfig( aAtdl4jConfig );

		// -- This method can be overridden/implemented --
		initPreCheck();

// 3/10/2010 Scott Atwell		controlConverter = (AbstractTypeConverter<E>) getAtdl4jConfig().getTypeConverterFactory().create( control, parameter );
// 3/11/2010 Scott Atwell moved after parameterConverter		controlConverter = (ControlTypeConverter<E>) getAtdl4jConfig().getTypeConverterFactory().createControlTypeConverter( control, parameter );

		if ( parameter != null )
		{
// 3/10/2010 Scott Atwell			parameterConverter = (AbstractTypeConverter<?>) getAtdl4jConfig().getTypeConverterFactory().create( parameter );
			parameterConverter = (ParameterTypeConverter<?>) getAtdl4jConfig().getTypeConverterFactory().createParameterTypeConverter( parameter );
		}

		// -- Pass parameterConverter (which may be null if parameter is null) --
// 3/11/2010 Scott Atwell changed to pass parameterConverter vs. parameter as arg		
		controlConverter = (ControlTypeConverter<E>) getAtdl4jConfig().getTypeConverterFactory().createControlTypeConverter( control, parameterConverter );

		validateEnumPairs();
		
// too early in process, Control does not yet have widget built		applyConstValue( parameter );
		
		// -- This method can be overridden/implemented --
		initPostCheck();
	}

	// -- Can be overridden --
	protected void initPreCheck()
	{
	}

	// -- Can be overridden --
	protected void initPostCheck()
	{
	}

	public void reinit()
	{
		// -- clear our "last value" state --
		setLastNonNullStateControlValueRaw( null );
	
		// -- check for Controls containing Parameter with constValue --
		if ( applyConstValue() )
		{
			return;  // -- return if constValue has been applied --
		}
		
		// -- reset what is displayed to the user --
// 3/14/2010 Scott Atwell		processReinit( ControlHelper.getInitValue( getControl(), getAtdl4jConfig() ) );
		processReinit( ControlHelper.getReinitValue( getControl(), getAtdl4jConfig() ) );
	}
	
	protected abstract void processReinit( Object aControlInitValue );
	
	/**
	 * Should be invoked after Control's Widget has been fully established.  Applies Parameter's constValue to the Control
	 * @return true if const value set
	 */
	private boolean applyConstValue()
	{
// Parameter/@const has been removed		
//		if ( ( getParameter() != null ) && ( getParameter().isConst() ) )
		if ( getParameter() != null ) 
		{
			Object tempConstValue = ParameterHelper.getConstValue( getParameter() );
			
			if ( tempConstValue != null )
			{
// 3/9/2010 Scott Atwell				E tempComparable = controlConverter.convertValueToControlComparable( tempConstValue );
// 3/10/2010 Scott Atwell				E tempComparable = controlConverter.convertParameterValueToControlComparable( tempConstValue );
//	3/10/2010 Scott Atwell			if ( tempComparable != null )
//	3/10/2010 Scott Atwell			{
//	3/10/2010 Scott Atwell				setValue( tempComparable );
				E tempControlValue = controlConverter.convertParameterValueToControlValue( tempConstValue );
				if ( tempControlValue != null )
				{
					setValue( tempControlValue );
					processConstValueHasBeenSet();
					return true;
				}
				else
				{
//					throw new IllegalArgumentException( "Unable to convert constValue or dailyConstValue [" + tempConstValue + "] -- required when Parameter@const=true [Parameter: " + parameter.getName() + "]");
					throw new IllegalArgumentException( "Unable to convert constValue [" + tempConstValue + "] -- required when Parameter@constValue is non-null [Parameter: " + parameter.getName() + "]");
				}
			}
//			else
//			{
//				throw new IllegalArgumentException( "constValue or dailyConstValue is required when Parameter@const=true [Parameter: " + parameter.getName() + "]");
//			}
		}
	
		return false;
	}
	
	/**
	 * Should be invoked after Control's Widget has been fully established.  Applies Parameter's constValue to the Control
	 */
	private void applyInitValue()
	{
//		if ( getInitValue() != null )
//		{
//			E tempComparable = controlConverter.convertValueToControlComparable( tempInitValue );
//			setValue( tempComparable );
//			processInitValueHasBeenSet();
//		}
	}

	public void applyConstOrInitValues()
	{
		applyConstValue();
		applyInitValue();
	}
	
	/**
	 * Will return null if isNullValue() is true, otherwise returns
	 * getControlValueRaw()
	 * 
	 * @return
	 */
	public E getControlValue()
	{
		if ( isNullValue() )
		{
			return null;
		}
		else
		{
			return getControlValueRaw();
		}
	}

	public Comparable<?> getControlValueAsComparable()
	{
// 3/10/2010 Scott Atwell		return controlConverter.convertValueToControlComparable( getControlValue() );
		return controlConverter.convertControlValueToControlComparable( getControlValue() );
	}

// 3/10/2010 Scott Atwell	public String getParameterValueAsString() throws JAXBException
	public String getParameterFixWireValue()
	{
// 3/10/2010 Scott Atwell		return parameter == null ? null : parameterConverter.convertValueToParameterString( getParameterValue() );
		return parameter == null ? null : parameterConverter.convertParameterValueToFixWireValue( getParameterValue() );
	}

	public Comparable<?> getParameterValueAsComparable()
	{
// 3/10/2010 Scott Atwell		return parameter == null ? null : parameterConverter.convertValueToParameterComparable( getParameterValue() );
		return parameter == null ? null : parameterConverter.convertParameterValueToParameterComparable( getParameterValue() );
	}

	/*
	 * This method handles string matching Atdl4jConstants.VALUE_NULL_INDICATOR
	 * and invoking setNullValue().
	 */
	public void setValueAsString(String aString)
	{
		if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( aString ) )
		{
			setNullValue( Boolean.TRUE );
			// -- note that this has no effect on the internal value which may have already been set --
		}
		else
		// -- not null --
		{
// 3/10/2010 Scott Atwell			E tempValue = controlConverter.convertValueToControlComparable( string );
			E tempValue = controlConverter.convertStringToControlValue( aString );
			setValue( tempValue );
			
			if ( ( tempValue == null ) && ( getNullValue() != null ) )
			{
				setNullValue( Boolean.TRUE );
			}
			else
			{
				setNullValue( Boolean.FALSE );
			}
		}
	}

	public Comparable<?> convertStringToControlComparable(String aString)
	{
// 3/10/2010 Scott Atwell		return controlConverter.convertValueToControlComparable( string );
		E tempControlValue = controlConverter.convertStringToControlValue( aString );
		return controlConverter.convertControlValueToControlComparable( tempControlValue );
	}

	
// 3/10/2010 Scott Atwell	public Comparable<?> convertStringToParameterComparable(String string)
	public Comparable<?> convertParameterStringToParameterComparable(String aParameterString)
	{
		if ( parameterConverter != null )
		{
// 3/10/2010 Scott Atwell			return parameterConverter.convertValueToParameterComparable( string );
			Object tempParameterValue = parameterConverter.convertParameterStringToParameterValue( aParameterString );
			return parameterConverter.convertParameterValueToParameterComparable( tempParameterValue );
		}
		else
			return null;
	}

	public ParameterT getParameter()
	{
		return parameter;
	}

	public boolean hasParameter()
	{
		return ( this.parameter != null );
	}

	public ControlT getControl()
	{
		return control;
	}

	public String getFIXValue()
	{
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		builder.onStart();
		getFIXValue( builder );
		builder.onEnd();
		return builder.getMessage();
	}

	// 2/1/2010 John Shields added
	public String getTooltip()
	{
		if ( control.getTooltip() != null )
			return control.getTooltip();
		else if ( parameter != null && parameter.getDescription() != null )
			return parameter.getDescription();
		return null;
	}

	public int getFIXType()
	{
		return Tag959Helper.toInteger( getParameter() );
	}

	public void getFIXValue(FIXMessageBuilder builder)
	{
// 3/10/2010 Scott Atwell		String value = getParameterValueAsString();
		String value = getParameterFixWireValue();
		if ( value != null )
		{
			if ( getParameter().getFixTag() != null )
			{
// Scott Atwell 1/31/2010 added (FixTag=0 to indicate valid parameter but DO NOT INCLUDE in FIX Message)
// 2/15/2010 Scott Atwell -- ???? remove the FixTag=0 part				
//				if ( getParameter().getFixTag().intValue() == 0 )
//				{
//					// ignore
//				}
//				else
//				{
					builder.onField( getParameter().getFixTag().intValue(), value.toString() );
//				}

			}
			else
			{
				if ( getParameter().getName().startsWith( InputAndFilterData.FIX_DEFINED_FIELD_PREFIX ) )
				{
					// bypass Hidden "standard fields" (eg "FIX_OrderQty")
				}
				else
				{
					String name = getParameter().getName();
					String type = Integer.toString( getFIXType() );
					builder.onField( Atdl4jConstants.TAG_STRATEGY_PARAMETER_NAME, name );
					builder.onField( Atdl4jConstants.TAG_STRATEGY_PARAMETER_TYPE, type );
					builder.onField( Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE, value.toString() );
				}
			}
		}
	}

	// Helper method to validate that EnumPairs and ListItems match for
	// the given Parameter and Control pair.
	protected void validateEnumPairs()
	{
		if ( parameter != null )
		{
			List<EnumPairT> enumPairs = parameter.getEnumPair();
			List<ListItemT> listItems = getListItems();

			if ( control instanceof HiddenFieldT )
			{
				// don't need to validate in case of HiddenFieldT
			}
			else if ( control instanceof RadioButtonT || control instanceof CheckBoxT )
			{
				// validate checkedEnumRef and uncheckedEnumRef
			}
			else if ( listItems == null || listItems.size() == 0 )
			{
				if ( enumPairs != null && enumPairs.size() > 0 )
				{
// 3/10/2010 Scott Atwell					throw new JAXBException( "Parameter \"" + parameter.getName() + "\" has EnumPairs but Control \"" + control.getID()
					throw new IllegalArgumentException( "Parameter \"" + parameter.getName() + "\" has EnumPairs but Control \"" + control.getID()
							+ "\" does not have ListItems." );
				}
			}
			else if ( parameter.getEnumPair() != null )
			{
				if ( listItems.size() != enumPairs.size() )
// 3/10/2010 Scott Atwell					throw new JAXBException( "Parameter \"" + parameter.getName() + "\" has " + enumPairs.size() + " EnumPairs but Control \""
					throw new IllegalArgumentException( "Parameter \"" + parameter.getName() + "\" has " + enumPairs.size() + " EnumPairs but Control \""
							+ control.getID() + "\" has " + listItems.size() + " ListItems." );

				for ( ListItemT listItem : listItems )
				{
					boolean match = false;
					for ( EnumPairT enumPair : enumPairs )
					{
						if ( listItem.getEnumID().equals( enumPair.getEnumID() ) )
						{
							match = true;
							break;
						}
					}
					if ( !match )
// 3/10/2010 Scott Atwell						throw new JAXBException( "ListItem \"" + listItem.getEnumID() + "\" on Control \"" + control.getID()
						throw new IllegalArgumentException( "ListItem \"" + listItem.getEnumID() + "\" on Control \"" + control.getID()
								+ "\" does not have a matching EnumPair on Parameter \"" + parameter.getName() + "\"." );
				}
			}
		}
	}

	// Helper method to lookup a parameter string where the EnumID is matched
	// across the ListItemTs and EnumPairTs
	protected String getEnumWireValue(String enumID)
	{
		if ( parameter != null )
		{
			java.util.List<EnumPairT> enumPairs = parameter.getEnumPair();
			for ( EnumPairT enumPair : enumPairs )
			{
//				if ( enumPair.getEnumID().equals( enumID ) )
// 3/19/2010 Scott Atwell handle VALUE_NULL_INDICATOR					return enumPair.getWireValue();
				if ( enumPair.getEnumID().equals( enumID ) )
				{
					if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( enumPair.getWireValue() ) )
					{
						return null;
					}
					else
					{
						return enumPair.getWireValue();
					}
				}
			}
			// throw error?
		}
		return null;
	}

	// Helper method to lookup a parameter string where the EnumID is matched
	// across the ListItemTs and EnumPairTs
	protected String getParameterValueAsEnumWireValue()
	{
		if ( getControlValue() != null )
		{
			return getEnumWireValue( getControlValue().toString() );
		}
		else
		{
			return null;
		}
	}

	// Helper method to convert MultipleValueChar / MultipleValueString Control
	// values to ParameterValues
	protected String getParameterValueAsMultipleValueString()
	{
		String value = "";
		if ( getControlValue() != null )
		{
			String enumIDs = getControlValue().toString();
			if ( enumIDs != null && parameter != null )
			{
				for ( String enumID : enumIDs.split( "\\s" ) )
				{
					if ( "".equals( value ) )
					{
						value += getEnumWireValue( enumID );
					}
					else
					{
						value += " " + getEnumWireValue( enumID );
					}
				}
			}
		}
		return "".equals( value ) ? null : value;
	}

	// Helper method to get control ListItems
	protected List<ListItemT> getListItems()
	{
		if ( control instanceof DropDownListT )
		{
			return ( (DropDownListT) control ).getListItem();
		}
		else if ( control instanceof EditableDropDownListT )
		{
			return ( (EditableDropDownListT) control ).getListItem();
		}
		else if ( control instanceof RadioButtonListT )
		{
			return ( (RadioButtonListT) control ).getListItem();
		}
		else if ( control instanceof CheckBoxListT )
		{
			return ( (CheckBoxListT) control ).getListItem();
		}
		else if ( control instanceof SingleSelectListT )
		{
			return ( (SingleSelectListT) control ).getListItem();
		}
		else if ( control instanceof MultiSelectListT )
		{
			return ( (MultiSelectListT) control ).getListItem();
		}
		else if ( control instanceof SliderT )
		{
			return ( (SliderT) control ).getListItem();
		}
		else
		{
			// TODO: this should maybe throw a runtime error???
			// return an empty list
			// return new Vector<ListItemT>();
			return null;
		}
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return this.atdl4jConfig;
	}

	/**
	 * @param aAtdl4jConfig
	 *           the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		this.atdl4jConfig = aAtdl4jConfig;
	}

	/**
	 * Note contains special logic to support returning false if: (
	 * getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( ! isVisible()
	 * ) or ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( !
	 * isEnabled() ) ) if those configs are set and nullValue is false.
	 * 
	 * @return the nullValue
	 */
	public boolean isNullValue()
	{
		if ( getNullValue() != null )
		{
			// -- If we have it set, use it --
			return getNullValue().booleanValue();
		}
		else
		// -- our nullValue is in an undefined state --
		{
			// -- Special logic to treat non-visible and/or non-enabled as "null"
			// if nullValue is false --
			if ( getAtdl4jConfig() != null )
			{
				if ( ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( !isVisible() ) )
// 2/15/2010 Scott Atwell						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( !isEnabled() ) ) )
						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( !isControlExcludingLabelEnabled() ) ) )
				{
					return false;
				}
				else if ( ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( isVisible() ) )
// 2/15/2010 Scott Atwell						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( isEnabled() ) ) )
						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( isControlExcludingLabelEnabled() ) ) )
				{
					return true;
				}
			}

			// -- Treat getNullValue() == null as FALSE --
			return false;
		}
	}

	/**
	 * @return
	 */
	public Boolean getNullValue()
	{
		return this.nullValue;
	}

	/**
	 * 
	 */
	abstract protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd);


	/**
	 * @param aNullValue
	 *           the nullValue to set
	 */
	// 2/11/2010 Scott Atwell public void setNullValue(boolean aNullValue)
	public void setNullValue(Boolean aNullValue)
	{
		// 2/11/2010 Scott Atwell this.nullValue = aNullValue;
		Boolean tempPreExistingNullValue = this.nullValue;

		// -- Assign the value --
		this.nullValue = aNullValue;

		logger.debug( "setNullValue() control ID:" + getControl().getID() + " tempPreExistingNullValue: " + tempPreExistingNullValue + " aNullValue: "
				+ aNullValue );

		// -- Check to see if aNullValue provided is different than the
		// pre-existing value --
		if ( ( ( aNullValue != null ) && ( !aNullValue.equals( tempPreExistingNullValue ) ) )
				|| ( ( tempPreExistingNullValue != null ) && ( !tempPreExistingNullValue.equals( aNullValue ) ) ) )
		{
			// -- "retain" the Control's last non-null raw value when changing to
			// aNullValue of true --
			if ( ( ( tempPreExistingNullValue == null ) || ( Boolean.FALSE.equals( tempPreExistingNullValue ) ) )
					&& ( Boolean.TRUE.equals( aNullValue ) ) )
			{
				logger.debug( "setNullValue() control ID:" + getControl().getID() + " tempPreExistingNullValue: " + tempPreExistingNullValue
						+ " aNullValue: " + aNullValue + " invoking setLastNonNullStateControlValueRaw( " + getControlValueRaw() + " )" );
				setLastNonNullStateControlValueRaw( getControlValueRaw() );
			}

			// -- value has changed, notify --
			processNullValueIndicatorChange( tempPreExistingNullValue, aNullValue );

			// -- "restore" the Control's raw value if so configured when going
			// from aNullValue of true to non-null --
			if ( ( Boolean.FALSE.equals( aNullValue ) ) && ( getLastNonNullStateControlValueRaw() != null ) )
			{
				if ( getAtdl4jConfig().isRestoreLastNonNullStateControlValueBehavior() )
				{
					logger
							.debug( "setNullValue() control ID:" + getControl().getID() + " tempPreExistingNullValue: " + tempPreExistingNullValue
									+ " aNullValue: " + aNullValue + " invoking restoreLastNonNullStateControlValue( " + getLastNonNullStateControlValueRaw()
									+ " )" );
					restoreLastNonNullStateControlValue( getLastNonNullStateControlValueRaw() );
				}
			}
		}
	}

	/**
	 * This method could be overridden for specific controls, if so desired.
	 * 
	 * @param aLastNonNullStateControlValue
	 */
	protected void restoreLastNonNullStateControlValue(E aLastNonNullStateControlValue)
	{
		setValue( aLastNonNullStateControlValue );
	}

	/**
	 * @return the lastNonNullStateControlValueRaw
	 */
	public E getLastNonNullStateControlValueRaw()
	{
		return this.lastNonNullStateControlValueRaw;
	}

	/**
	 * @param aLastNonNullStateControlValueRaw
	 *           the lastNonNullStateControlValueRaw to set
	 */
	protected void setLastNonNullStateControlValueRaw(E aLastNonNullStateControlValueRaw)
	{
		this.lastNonNullStateControlValueRaw = aLastNonNullStateControlValueRaw;
	}
	
	/**
	 * Used when pre-populating a control with its FIX message wire value 
	 * For example: PercentageT with isMultiplyBy100() == true would have ".1234" on the wire for "12.34" displayed/stored by the control (for 12.34%). 
	 * @param aFIXValue
	 */
	public void setFIXValue( String aFIXValue )
	{
		// -- Must use parameterConverter's convertToControlString (eg TextField's controlConverter is a StringConverter, not a DecimalConverter like the Parameter's would be) --
//	3/9/2010 Scott Atwell	setValueAsString( parameterConverter.convertValueToControlString( aFIXValue ) );
// 3/10/2010 Scott Atwell		Object tempParameterValue = parameterConverter.convertValueToParameterComparable( aFIXValue ); 
		Object tempParameterValue = parameterConverter.convertFixWireValueToParameterValue( aFIXValue ); 
//		setValueAsString( controlConverter.convertParameterValueToControlString( tempParameterValue ) );
// 3/10/2010 Scott Atwell		E tempValue = controlConverter.convertParameterValueToControlComparable( tempParameterValue );
		E tempValue = controlConverter.convertParameterValueToControlValue( tempParameterValue );
// 3/13/2010 Scott Atwell moved after setNullValue()		setValue( tempValue );
		
		if ( ( tempValue == null ) && ( getNullValue() != null ) )
		{
			setNullValue( Boolean.TRUE );
		}
		else
		{
			setNullValue( Boolean.FALSE );
		}
		
		setValue( tempValue );
		
	}
	

	/**
	 * Default implementation.  Can be overridden if so desired.
	 */
	public void processConstValueHasBeenSet()
	{
//		setEnabled( false );
		setControlExcludingLabelEnabled( false );
	}
	
	
	public Object getParameterValue()
	{
		E tempControlValue = getControlValue();
		return controlConverter.convertControlValueToParameterValue( tempControlValue );
	}

	/**
	 * @return the hiddenFieldForInputAndFilterData
	 */
	public boolean isHiddenFieldForInputAndFilterData()
	{
		return this.hiddenFieldForInputAndFilterData;
	}

	/**
	 * @param aHiddenFieldForInputAndFilterData the hiddenFieldForInputAndFilterData to set
	 */
	public void setHiddenFieldForInputAndFilterData(boolean aHiddenFieldForInputAndFilterData)
	{
		this.hiddenFieldForInputAndFilterData = aHiddenFieldForInputAndFilterData;
	}

	/* Overriden to delegate to ControlUI's ControlT data member.  (note ControlUI map may be shallow copied resulting in new identity for ControlUI objects)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aObj)
	{
		if ( ( getControl() != null ) &&
			  ( aObj instanceof ControlUI<?> ) )
		{
			return getControl().equals( ((ControlUI<?>) aObj).getControl() );
		}
		else
		{
			return super.equals( aObj );
		}
	}

	/* Overriden to delegate to ControlUI's ControlT data member.  (note ControlUI map may be shallow copied resulting in new identity for ControlUI objects)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		if ( getControl() != null )
		{
			return getControl().hashCode();
		}
		else
		{
			return super.hashCode();
		}
	}
}