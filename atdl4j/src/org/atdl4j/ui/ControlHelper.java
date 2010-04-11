/*
 * Created on Feb 21, 2010
 *
 */
package org.atdl4j.ui;

import java.math.BigInteger;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.fixatdl.core.AmtT;
import org.atdl4j.fixatdl.core.FloatT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.PercentageT;
import org.atdl4j.fixatdl.core.PriceOffsetT;
import org.atdl4j.fixatdl.core.PriceT;
import org.atdl4j.fixatdl.core.QtyT;
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
	 * @return
	 */
	public static BigInteger getIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		if ( aControl instanceof SingleSpinnerT )
		{
			return determineIncrementValue( ((SingleSpinnerT) aControl).getIncrement(), 
													  ((SingleSpinnerT) aControl).getIncrementPolicy(),
													  aAtdl4jConfig );
		}
		
		return null;
	}
	
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static BigInteger getInnerIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		if ( aControl instanceof DoubleSpinnerT )
		{
			return determineIncrementValue( ((DoubleSpinnerT) aControl).getInnerIncrement(), 
													  ((DoubleSpinnerT) aControl).getInnerIncrementPolicy(),
													  aAtdl4jConfig );
		}
		
		return null;
	}
	
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static BigInteger getOuterIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
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
	 * @return
	 */
	public static BigInteger determineIncrementValue( Double aIncrement, String aIncrementPolicy, Atdl4jConfig aAtdl4jConfig )
	{
		if ( Atdl4jConstants.INCREMENT_POLICY_LOT_SIZE.equals( aIncrementPolicy ) ) 
		{
			if ( ( aAtdl4jConfig != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData() != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData().getInputIncrementPolicy_LotSize() != null ) )
			{
				return aAtdl4jConfig.getInputAndFilterData().getInputIncrementPolicy_LotSize();
			}
			else
			{
				throw new IllegalArgumentException( "LotSize for security was not specified.  Unable to support IncrementPolicy=" + aIncrementPolicy );
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
			else
			{
				throw new IllegalArgumentException( "Tick size for security was not specified.  Unable to support IncrementPolicy=" + aIncrementPolicy );
			}
		} 
		else  // -- Use aIncrement value when aIncrementPolicy null or Atdl4jConstants.INCREMENT_POLICY_STATIC --
		{
			if ( aIncrement != null )
			{
				return BigInteger.valueOf( aIncrement.longValue() ); 
			}
			else
			{
				return null;
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
//			return ((CheckBoxT) aControl).getInitValue();
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
//			return ((RadioButtonT) aControl).getInitValue();
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
// 3/14/2010 Scott Atwell don't want ""		return aControl.getLabel() != null ? aControl.getLabel() : aControl.getID();
		return (aControl.getLabel() != null && !"".equals(aControl.getLabel())) ? aControl.getLabel() : aControl.getID();
	}

/*** 3/9/2010 Scott Atwell - moved away from using this via common, AbstractControlUI, and rather forcing each Control concrete instance to support reinit()	
	/ **
	 * Like getInitValue() although it returns a non-null value for each aControl type (avoid issues with Control's setValue(null) ). 
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return 
	 * /
	public static Object getReinitValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		// -- note that this returns null if initValue is Atdl4jConstants.VALUE_NULL_INDICATOR --
		Object tempReinitValue = getInitValue( aControl, aAtdl4jConfig );
		
		if ( tempReinitValue != null )
		{
			return tempReinitValue;
		}
		else
		{
			// -- (aOkToReturnValueNullIndicatorString=false) --
			return getReinitValueRaw( aControl, false );
		}
	}
	
	/ **
	 * Returns raw/actual value (if aOkToReturnValueNullIndicatorString=true then could be "{NULL}" string) from ControlT/@initValue
	 * Ensures that Object value returned is not null by 'defaulting' to a value of datatype compatible with the Control
	 * 
	 * @param aControl
	 * @return 
	 * /
	public static Object getReinitValueRaw( ControlT aControl, boolean aOkToReturnValueNullIndicatorString )
	{
		Object tempInitValueRaw = getInitValueRaw( aControl );
		
		if ( tempInitValueRaw != null )
		{
			// -- Special handling to check for VALUE_NULL_INDICATOR -- 
			if ( ( ! aOkToReturnValueNullIndicatorString ) ||
				  ( ! Atdl4jConstants.VALUE_NULL_INDICATOR.equals( tempInitValueRaw ) ) )
			{
				return tempInitValueRaw;
			}
		}
		

		// -- Need to return a non-null value to force the Control to display/re-display in an initialized state --
		if ( aControl instanceof CheckBoxT )
		{
			return Boolean.FALSE;
		}
		else if ( aControl instanceof CheckBoxListT )
		{
			return "";
		}
		else if ( aControl instanceof ClockT )
		{
			return DateTimeConverter.constructNewXmlGregorianCalendar();
		}
		else if ( aControl instanceof DoubleSpinnerT )
		{
			return new Double( 0.0d );
		}
		else if ( aControl instanceof DropDownListT )
		{
			return "";
		}
		else if ( aControl instanceof EditableDropDownListT )
		{
			return "";
		}
		else if ( aControl instanceof HiddenFieldT )
		{
// leave as-is			return "";
		}
		else if ( aControl instanceof LabelT )
		{
			return "";
		}
		else if ( aControl instanceof MultiSelectListT )
		{
			return "";
		}
		else if ( aControl instanceof RadioButtonT )
		{
			return Boolean.FALSE;
		}
		else if ( aControl instanceof RadioButtonListT )
		{
			return "";
		}
		else if ( aControl instanceof SingleSelectListT )
		{
			return "";
		}
		else if ( aControl instanceof SingleSpinnerT )
		{
			return new Double( 0.0d );
		}
		else if ( aControl instanceof SliderT )
		{
			return "";
		}
		else if ( aControl instanceof TextFieldT )
		{
			return "";
		}
		
		return null;
	}
****/

/*** 3/14/2010 Scott Atwell - Johnny renamed to getLabelOrID()	
	public static String getUiRepOrID( ControlT aControl )
	{
		if ( aControl == null )
		{
			return null;
		}
		
		String tempUiRep = null;
		
		if ( aControl instanceof CheckBoxT )
		{
			tempUiRep = ((CheckBoxT) aControl).getLabel();
		}
		else if ( aControl instanceof CheckBoxListT )
		{
			tempUiRep = ((CheckBoxListT) aControl).getLabel();
		}
		else if ( aControl instanceof ClockT )
		{
			tempUiRep = ((ClockT) aControl).getLabel();
		}
		else if ( aControl instanceof DoubleSpinnerT )
		{
			tempUiRep = ((DoubleSpinnerT) aControl).getLabel();
		}
		else if ( aControl instanceof DropDownListT )
		{
			tempUiRep = ((DropDownListT) aControl).getLabel();
		}
		else if ( aControl instanceof EditableDropDownListT )
		{
			tempUiRep = ((EditableDropDownListT) aControl).getLabel();
		}
		else if ( aControl instanceof HiddenFieldT )
		{
//			tempUiRep = ((HiddenFieldT) aControl).getLabel();
		}
		else if ( aControl instanceof LabelT )
		{
			tempUiRep = ((LabelT) aControl).getLabel();
		}
		else if ( aControl instanceof MultiSelectListT )
		{
			tempUiRep = ((MultiSelectListT) aControl).getLabel();
		}
		else if ( aControl instanceof RadioButtonT )
		{
			tempUiRep = ((RadioButtonT) aControl).getLabel();
		}
		else if ( aControl instanceof RadioButtonListT )
		{
			tempUiRep = ((RadioButtonListT) aControl).getLabel();
		}
		else if ( aControl instanceof SingleSelectListT )
		{
			tempUiRep = ((SingleSelectListT) aControl).getLabel();
		}
		else if ( aControl instanceof SingleSpinnerT )
		{
			tempUiRep = ((SingleSpinnerT) aControl).getLabel();
		}
		else if ( aControl instanceof SliderT )
		{
			tempUiRep = ((SliderT) aControl).getLabel();
		}
		else if ( aControl instanceof TextFieldT )
		{
			tempUiRep = ((TextFieldT) aControl).getLabel();
		}
		
		// -- Return Control's ID if no uiRep --
		if ( ( tempUiRep == null ) || ( "".equals( tempUiRep ) ) )
		{
			return aControl.getID();
		}
		else
		{
			return tempUiRep;
		}
	}
***/	

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
