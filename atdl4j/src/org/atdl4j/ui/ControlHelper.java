/*
 * Created on Feb 21, 2010
 *
 */
package org.atdl4j.ui;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.fixatdl.core.ParameterT;
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

/**
 * 
 * This class contains the data associated with the <code>ControlHelper</code>.
 * 
 * Creation date: (Feb 21, 2010 5:44:02 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 21, 2010
 */
public class ControlHelper
{
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @param aDigits
	 * @return
	 */
// 4/18/2010 Scott Atwell	public static BigInteger getIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
// 8/17/2010 Scott Atwell	public static BigDecimal getIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	public static BigDecimal getIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig, int aDigits )
	{
      if ( aControl instanceof SingleSpinnerT )
		{
			return determineIncrementValue( ((SingleSpinnerT) aControl).getIncrement(), 
													  ((SingleSpinnerT) aControl).getIncrementPolicy(),
													  aAtdl4jConfig,
													  aDigits );
		}
		
		return null;
	}
	
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @param aDigits
	 * @return
	 */
// 4/18/2010 Scott Atwell	public static BigInteger getInnerIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
// 8/17/2010 Scott Atwell	public static BigDecimal getInnerIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig, int aDigits  )
	public static BigDecimal getInnerIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig, int aDigits  )
	{
		if ( aControl instanceof DoubleSpinnerT )
		{
			return determineIncrementValue( ((DoubleSpinnerT) aControl).getInnerIncrement(), 
													  ((DoubleSpinnerT) aControl).getInnerIncrementPolicy(),
													  aAtdl4jConfig,
													  aDigits );
		}
		
		return null;
	}
	
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return
	 */
// 4/18/2010 Scott Atwell	public static BigInteger getOuterIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	public static BigDecimal getOuterIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		if ( aControl instanceof DoubleSpinnerT )
		{
			return determineIncrementValue( ((DoubleSpinnerT) aControl).getOuterIncrement(), 
													  ((DoubleSpinnerT) aControl).getOuterIncrementPolicy(),
													  aAtdl4jConfig );
		}
		
		return null;
	}
	

	/**
	 * @param aIncrement
	 * @param aIncrementPolicy
	 * @param aAtdl4jConfig
	 * @param aDigits
	 * @return
	 */
	public static BigDecimal determineIncrementValue( Double aIncrement, String aIncrementPolicy, Atdl4jConfig aAtdl4jConfig, int aDigits )
	{ 
		BigDecimal tempBigDecimal = determineIncrementValue( aIncrement, aIncrementPolicy, aAtdl4jConfig );
		
		if ( tempBigDecimal != null )
		{
			return tempBigDecimal.setScale( aDigits );
		}
		else
		{
			return tempBigDecimal;
		}
	}
	
	
	/**
	 * @param aIncrement
	 * @param aIncrementPolicy
	 * @param aAtdl4jConfig
	 * @return
	 */
// 4/18/2010 Scott Atwell	public static BigInteger determineIncrementValue( Double aIncrement, String aIncrementPolicy, Atdl4jConfig aAtdl4jConfig )
	public static BigDecimal determineIncrementValue( Double aIncrement, String aIncrementPolicy, Atdl4jConfig aAtdl4jConfig )
	{ 
		// -- FIXatdl 1.1 Schema documentation: --
		//		<xs:documentation>
		//      Describes how to use increment. If undefined then take value from increment
		//      attribute, if LotSize use value based on symbol lot size. (If lot size is not available use value of
		//      increment attribute.) If Tick use value based on symbol tick size. (If tick size is not available
		//      use value of increment attribute.) If increment is to be used and is not defined then use a system
		//      default value.
		//    </xs:documentation>

		if ( Atdl4jConstants.INCREMENT_POLICY_LOT_SIZE.equals( aIncrementPolicy ) ) 
		{
			if ( ( aAtdl4jConfig != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData() != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData().getInputIncrementPolicy_LotSize() != null ) )
			{
				return aAtdl4jConfig.getInputAndFilterData().getInputIncrementPolicy_LotSize();
			}
//	4/18/2010 Scott Atwell removed
//			else
//			{
//				throw new IllegalArgumentException( "LotSize for security was not specified.  Unable to support IncrementPolicy=" + aIncrementPolicy );
//			}
			else if ( aIncrement != null )
			{
// 7/18/2010 Scott Atwell				return new BigDecimal( aIncrement );
				return new BigDecimal( aIncrement.toString() );
			}
			else
			{
				return aAtdl4jConfig.getDefaultLotSizeIncrementValue();
			}
		}
		else if ( Atdl4jConstants.INCREMENT_POLICY_TICK.equals( aIncrementPolicy ) ) 
		{
			if ( ( aAtdl4jConfig != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData() != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData().getInputIncrementPolicy_Tick() != null ) )
			{
				return aAtdl4jConfig.getInputAndFilterData().getInputIncrementPolicy_Tick();
			}
//	4/18/2010 Scott Atwell removed
//			else
//			{
//				throw new IllegalArgumentException( "Tick size for security was not specified.  Unable to support IncrementPolicy=" + aIncrementPolicy );
//			}
			else if ( aIncrement != null )
			{
// 7/18/2010 Scott Atwell				return new BigDecimal( aIncrement );
				return new BigDecimal( aIncrement.toString() );
			}
			else
			{
				return aAtdl4jConfig.getDefaultTickIncrementValue();
			}
		} 
		else  // -- Use aIncrement value when aIncrementPolicy null or Atdl4jConstants.INCREMENT_POLICY_STATIC --
		{
			if ( aIncrement != null )
			{
// 4/18/2010 Scott Atwell replaced				return BigInteger.valueOf( aIncrement.longValue() ); 
// 7/18/2010 Scott Atwell				return new BigDecimal( aIncrement );
				return new BigDecimal( aIncrement.toString() );
			}
			else
			{
// 4/18/2010 Scott Atwell replaced				return null;
				return aAtdl4jConfig.getDefaultIncrementValue();
			}
		}
	}
	
	/**
	 * Handles ControlT/@initPolicy ("UseValue" or "UseFixField") logic in conjunction with ControlT/@initValue and ControlT/@initFixField
	 * Returns null if ControlT/@initValue is the special null indicator: VALUE_NULL_INDICATOR
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return 
	 */
	public static Object getInitValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		// INIT_POLICY_USE_VALUE = "UseValue";  // -- use value from ControlT/@initValue --
		// INIT_POLICY_USE_FIX_FIELD = "UseFixField";  // -- use value from ControlT/@initFixField if available, else ControlT/@initValue --		
		
		if ( Atdl4jConstants.INIT_POLICY_USE_FIX_FIELD.equals( aControl.getInitPolicy() ) )
		{
			if ( aControl.getInitFixField() == null )
			{
				throw new IllegalArgumentException( "ERROR: Control: " + aControl.getID() + " has initPolicy=\"" + aControl.getInitPolicy() + "\" but does not have initFixField set." );
			}
			
			if ( ( aAtdl4jConfig == null ) || ( aAtdl4jConfig.getInputAndFilterData() == null ) )
			{
				throw new IllegalArgumentException( "ERROR: Control: " + aControl.getID() + " has initPolicy=\"" + aControl.getInitPolicy() + "\" but Atdl4jConfig is null." );
			}
			
			String tempFixFieldValue = aAtdl4jConfig.getInputAndFilterData().getInputHiddenFieldValue( aControl.getInitFixField().toString() );
			
			if ( tempFixFieldValue != null )
			{
				// -- "FIX_[fieldname]" value found, return it --
				return tempFixFieldValue;
			}
		}

		// -- Get the 'raw' ControlT/@initValue --
		Object tempInitValueRaw = getInitValueRaw( aControl );
		
		// -- Special handling to check for VALUE_NULL_INDICATOR -- 
		if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( tempInitValueRaw ) )
		{
			return null;
		}
		else
		{
			return tempInitValueRaw;
		}
	}
	
	/**
	 * Returns raw/actual value (could be "{NULL}" string) from ControlT/@initValue
	 * @param aControl
	 * @return 
	 */
	public static Object getInitValueRaw( ControlT aControl )
	{
		// -- initPolicy is INIT_POLICY_USE_VALUE  or  no input field value supplied/found for ControlT/@initFixField --
		if ( aControl instanceof CheckBoxT )
		{
			return ((CheckBoxT) aControl).isInitValue();
		}
		else if ( aControl instanceof CheckBoxListT )
		{
			return ((CheckBoxListT) aControl).getInitValue();
		}
		else if ( aControl instanceof ClockT )
		{
			return ((ClockT) aControl).getInitValue();
		}
		else if ( aControl instanceof DoubleSpinnerT )
		{
			return ((DoubleSpinnerT) aControl).getInitValue();
		}
		else if ( aControl instanceof DropDownListT )
		{
			return ((DropDownListT) aControl).getInitValue();
		}
		else if ( aControl instanceof EditableDropDownListT )
		{
			return ((EditableDropDownListT) aControl).getInitValue();
		}
		else if ( aControl instanceof HiddenFieldT )
		{
			return ((HiddenFieldT) aControl).getInitValue();
		}
		else if ( aControl instanceof LabelT )
		{
			return ((LabelT) aControl).getInitValue();
		}
		else if ( aControl instanceof MultiSelectListT )
		{
			return ((MultiSelectListT) aControl).getInitValue();
		}
		else if ( aControl instanceof RadioButtonT )
		{
			return ((RadioButtonT) aControl).isInitValue();
		}
		else if ( aControl instanceof RadioButtonListT )
		{
			return ((RadioButtonListT) aControl).getInitValue();
		}
		else if ( aControl instanceof SingleSelectListT )
		{
			return ((SingleSelectListT) aControl).getInitValue();
		}
		else if ( aControl instanceof SingleSpinnerT )
		{
			return ((SingleSpinnerT) aControl).getInitValue();
		}
		else if ( aControl instanceof SliderT )
		{
			return ((SliderT) aControl).getInitValue();
		}
		else if ( aControl instanceof TextFieldT )
		{
			return ((TextFieldT) aControl).getInitValue();
		}
		
		return null;
	}

	public static String getLabelOrID( ControlT aControl ) 
	{
		return (aControl.getLabel() != null && !"".equals(aControl.getLabel())) ? aControl.getLabel() : aControl.getID();
	}


	/**
	 * Handles ControlT/@initPolicy ("UseValue" or "UseFixField") logic in conjunction with ControlT/@initValue and ControlT/@initFixField
	 * Returns null if ControlT/@initValue is the special null indicator: VALUE_NULL_INDICATOR
	 * Also has special handling for LabelT which may simply use "label=" vs. "initValue=" for its value.
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return 
	 */
	public static Object getReinitValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		Object tempInitValue = getInitValue( aControl, aAtdl4jConfig );
		
		if ( ( tempInitValue == null ) && ( aControl instanceof LabelT ) )
		{
			// 3/14/2010 Scott Atwell LabelT may simply use "label=" and do not want to lose it upon reinit
			return ((LabelT) aControl).getLabel();
		}
		else
		{
			return tempInitValue;
		}
	}

	/**
	 * Returns the default number of Spinner control "digits" for the specified aParameter 
	 * @param aParameter
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static int getDefaultDigitsForSpinnerControl( ParameterT aParameter, Atdl4jConfig aAtdl4jConfig )
	{
		// -- not specified via rule above, use default if we have one within Atdl4jConfig --
		if ( aAtdl4jConfig != null )
		{
			return aAtdl4jConfig.getDefaultDigitsForSpinnerControl( aParameter );
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Returns true if Control can toggle between two boolean states.
	 * @param aControl
	 * @return
	 */
	public static boolean isControlToggleable( ControlT aControl )
	{
		if ( ( aControl instanceof CheckBoxT ) ||
			  ( aControl instanceof RadioButtonT ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
