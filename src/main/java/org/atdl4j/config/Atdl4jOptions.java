/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.PercentageT;
import org.atdl4j.fixatdl.core.QtyT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.fixatdl.layout.ControlT;

/**
 * This class contains the data associated with the <code>Atdl4jOptions</code>.
 * 
 * Creation date: (Feb 7, 2010 6:12:35 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public class Atdl4jOptions
{
	private final Logger logger = Logger.getLogger(Atdl4jOptions.class);

	private InputAndFilterData inputAndFilterData;
	
	private boolean treatControlVisibleFalseAsNull = false;
	private boolean treatControlEnabledFalseAsNull = false;	
	private boolean restoreLastNonNullStateControlValueBehavior = true;	
	private boolean accommodateMixOfStrategyPanelsAndControls = false;  // FIXatdl 1.1 spec recommends against vs. prohibits
	
	private boolean showEnabledCheckboxOnOptionalClockControl = false;
	private boolean showDateInputOnTimestampClockControl = false;
	
	private int defaultDigitsForSpinnerControlForPercentage = 0;
	private int defaultDigitsForSpinnerControlForQty = 0;
	private int defaultDigitsForSpinnerControl = 2;

	private BigDecimal defaultIncrementValue = null;
	private BigDecimal defaultLotSizeIncrementValue = new BigDecimal( "1.0" );
	private BigDecimal defaultTickIncrementValue = new BigDecimal( "0.0001" );

	// -- Controls Clock control's behavior when FIX message timestamp (eg "StartTime" or "EffectiveTime") is older than current time --
	public static final Integer CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS = new Integer( 0 );
	public static final Integer CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT = new Integer( 1 );
	public static final Integer CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL = new Integer( 2 );

	
	// -- Controls Clock control's behavior when FIX message timestamp (eg "StartTime" or "EffectiveTime") is older than current time --
	private Integer clockStartTimeSetFIXValueWithPastTimeRule = CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT;
	private Integer clockEndTimeSetFIXValueWithPastTimeRule = CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL;
	private Integer clockUnknownSetFIXValueWithPastTimeRule = CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS;
	// -- Used by isClockControlStartTime() and isClockControlEndTime() to check aControl.getID() to see if it contains any of the String "IDValueFragments" within these lists --
	private String[] clockControlStartTimeIDValueFragmentList = new String[]{ "Start", "Effective", "Begin" };
	private String[] clockControlEndTimeIDValueFragmentList = new String[]{ "End", "Expire", "Stop" };

	/**
	 * 
	 */
	public Atdl4jOptions()
	{
	}
	
	/**
	 * @param inputAndFilterData the inputAndFilterData to set
	 */
	public void setInputAndFilterData(InputAndFilterData inputAndFilterData)
	{
		this.inputAndFilterData = inputAndFilterData;
	}

	/**
	 * @return the inputAndFilterData
	 */
	public InputAndFilterData getInputAndFilterData()
	{
		return inputAndFilterData;
	}

	/**
	 * @return the treatControlVisibleFalseAsNull
	 */
	public boolean isTreatControlVisibleFalseAsNull()
	{
		return this.treatControlVisibleFalseAsNull;
	}

	/**
	 * @param aTreatControlVisibleFalseAsNull the treatControlVisibleFalseAsNull to set
	 */
	public void setTreatControlVisibleFalseAsNull(boolean aTreatControlVisibleFalseAsNull)
	{
		this.treatControlVisibleFalseAsNull = aTreatControlVisibleFalseAsNull;
	}

	/**
	 * @return the treatControlEnabledFalseAsNull
	 */
	public boolean isTreatControlEnabledFalseAsNull()
	{
		return this.treatControlEnabledFalseAsNull;
	}

	/**
	 * @param aTreatControlEnabledFalseAsNull the treatControlEnabledFalseAsNull to set
	 */
	public void setTreatControlEnabledFalseAsNull(boolean aTreatControlEnabledFalseAsNull)
	{
		this.treatControlEnabledFalseAsNull = aTreatControlEnabledFalseAsNull;
	}

	/**
	 * @return the showEnabledCheckboxOnOptionalClockControl
	 */
	public boolean isShowEnabledCheckboxOnOptionalClockControl()
	{
		return this.showEnabledCheckboxOnOptionalClockControl;
	}

	/**
	 * @param aShowEnabledCheckboxOnOptionalClockControl the showEnabledCheckboxOnOptionalClockControl to set
	 */
	public void setShowEnabledCheckboxOnOptionalClockControl(boolean aShowEnabledCheckboxOnOptionalClockControl)
	{
		this.showEnabledCheckboxOnOptionalClockControl = aShowEnabledCheckboxOnOptionalClockControl;
	}

	/**
	 * @return the restoreLastNonNullStateControlValueBehavior
	 */
	public boolean isRestoreLastNonNullStateControlValueBehavior()
	{
		return this.restoreLastNonNullStateControlValueBehavior;
	}

	/**
	 * @param aRestoreLastNonNullStateControlValueBehavior the restoreLastNonNullStateControlValueBehavior to set
	 */
	public void setRestoreLastNonNullStateControlValueBehavior(boolean aRestoreLastNonNullStateControlValueBehavior)
	{
		this.restoreLastNonNullStateControlValueBehavior = aRestoreLastNonNullStateControlValueBehavior;
	}

	/* 
	 * Returns the Spinner control's "digits" value for the specified aParameter (eg 0 vs. 2 for Percentage)
	 */
	public int getDefaultDigitsForSpinnerControl( ParameterT aParameter )
	{
		if ( aParameter != null )
		{
			if ( aParameter instanceof PercentageT )
			{
				return getDefaultDigitsForSpinnerControlForPercentage();
			}
			else if ( aParameter instanceof QtyT )
			{
				return getDefaultDigitsForSpinnerControlForQty();
			}
			
			// -- use Atdl4jOptions.getDefaultDigitsForSpinnerControl() for FloatT, AmtT, PriceOffsetT, PriceT --			
		}
		
		// -- not specified via rule above, use default if we have one within Atdl4jOptions --
		return getDefaultDigitsForSpinnerControl();
	}

	/**
	 * @return the defaultDigitsForSpinnerControlForPercentage
	 */
	protected int getDefaultDigitsForSpinnerControlForPercentage()
	{
		return this.defaultDigitsForSpinnerControlForPercentage;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControlForPercentage the defaultDigitsForSpinnerControlForPercentage to set
	 */
	protected void setDefaultDigitsForSpinnerControlForPercentage(int aDefaultDigitsForSpinnerControlForPercentage)
	{
		this.defaultDigitsForSpinnerControlForPercentage = aDefaultDigitsForSpinnerControlForPercentage;
	}

	/**
	 * @return the defaultDigitsForSpinnerControlForQty
	 */
	protected int getDefaultDigitsForSpinnerControlForQty()
	{
		return this.defaultDigitsForSpinnerControlForQty;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControlForQty the defaultDigitsForSpinnerControlForQty to set
	 */
	protected void setDefaultDigitsForSpinnerControlForQty(int aDefaultDigitsForSpinnerControlForQty)
	{
		this.defaultDigitsForSpinnerControlForQty = aDefaultDigitsForSpinnerControlForQty;
	}

	/**
	 * @return the defaultDigitsForSpinnerControl
	 */
	protected int getDefaultDigitsForSpinnerControl()
	{
		return this.defaultDigitsForSpinnerControl;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControl the defaultDigitsForSpinnerControl to set
	 */
	protected void setDefaultDigitsForSpinnerControl(int aDefaultDigitsForSpinnerControl)
	{
		this.defaultDigitsForSpinnerControl = aDefaultDigitsForSpinnerControl;
	}
	
	/**
	 * Uses, if specified, InputAndFilterData.getInputStrategyNameList() 
	 * and InputAndFilterData.getApplyInputStrategyNameListAsFilter() 
	 * to control the order presented to the user and, if so desired, exclude strategies against the available aStrategyList
	 * 
	 * @param aStrategyList
	 * @return
	 */
	public List<StrategyT> getStrategyListUsingInputStrategyNameListFilter( List<StrategyT> aStrategyList )
	{
		if ( aStrategyList == null )
		{
			return null;
		}

		if ( ( getInputAndFilterData() != null ) &&
			  ( getInputAndFilterData().getInputStrategyNameList() != null ) && 
			  ( getInputAndFilterData().getInputStrategyNameList().size() > 0  ) )
		{
			List<StrategyT> tempAvailableStrategyList = new ArrayList<StrategyT>();

			// -- Add the strategies according to their order in the specified InputStrategyNameList --
			for ( String tempStrategyName : getInputAndFilterData().getInputStrategyNameList() )
			{
				for ( StrategyT tempStrategy : aStrategyList )
				{
					if ( tempStrategyName.equals(  tempStrategy.getName() ) )
					{
						// -- Strategy is in the InputStrategyNameList --
						tempAvailableStrategyList.add( tempStrategy );
					}
				}				
			}
			
			// -- Add any other strategyNames (in their order within the FIXatdl file) to the end unless setting specifies the input list is a filter --
			if ( ! Boolean.TRUE.equals( getInputAndFilterData().getApplyInputStrategyNameListAsFilter() ) )
			{
				for ( StrategyT tempStrategy : aStrategyList )
				{
					if ( ! tempAvailableStrategyList.contains( tempStrategy ) )
					{
						tempAvailableStrategyList.add( tempStrategy );
					}
				}
			}
			
			logger.debug("getStrategyListUsingInputStrategyNameListFilter() returning: " + tempAvailableStrategyList);
			return tempAvailableStrategyList;
		}
		else
		{
			// -- Return the original list unfiltered --
			return aStrategyList;
		}
	}

	/**
	 * @return the defaultIncrementValue
	 */
	public BigDecimal getDefaultIncrementValue()
	{
		return this.defaultIncrementValue;
	}

	/**
	 * @param aDefaultIncrementValue the defaultIncrementValue to set
	 */
	public void setDefaultIncrementValue(BigDecimal aDefaultIncrementValue)
	{
		this.defaultIncrementValue = aDefaultIncrementValue;
	}

	/**
	 * @return the defaultLotSizeIncrementValue
	 */
	public BigDecimal getDefaultLotSizeIncrementValue()
	{
		return this.defaultLotSizeIncrementValue;
	}

	/**
	 * @param aDefaultLotSizeIncrementValue the defaultLotSizeIncrementValue to set
	 */
	public void setDefaultLotSizeIncrementValue(BigDecimal aDefaultLotSizeIncrementValue)
	{
		this.defaultLotSizeIncrementValue = aDefaultLotSizeIncrementValue;
	}

	/**
	 * @return the defaultTickIncrementValue
	 */
	public BigDecimal getDefaultTickIncrementValue()
	{
		return this.defaultTickIncrementValue;
	}

	/**
	 * @param aDefaultTickIncrementValue the defaultTickIncrementValue to set
	 */
	public void setDefaultTickIncrementValue(BigDecimal aDefaultTickIncrementValue)
	{
		this.defaultTickIncrementValue = aDefaultTickIncrementValue;
	}

	/**
	 * @param clockStartTimeSetFIXValueWithPastTimeRule the clockStartTimeSetFIXValueWithPastTimeRule to set
	 */
	public void setClockStartTimeSetFIXValueWithPastTimeRule(Integer aClockPastTimeSetFIXValueRule)
	{
		this.clockStartTimeSetFIXValueWithPastTimeRule = aClockPastTimeSetFIXValueRule;
	}

	/**
	 * @return the clockStartTimeSetFIXValueWithPastTimeRule
	 */
	public Integer getClockStartTimeSetFIXValueWithPastTimeRule()
	{
		return clockStartTimeSetFIXValueWithPastTimeRule;
	}

	/**
	 * @param clockEndTimeSetFIXValueWithPastTimeRule the clockEndTimeSetFIXValueWithPastTimeRule to set
	 */
	public void setClockEndTimeSetFIXValueWithPastTimeRule(Integer aClockPastTimeSetFIXValueRule)
	{
		this.clockEndTimeSetFIXValueWithPastTimeRule = aClockPastTimeSetFIXValueRule;
	}

	/**
	 * @return the clockEndTimeSetFIXValueWithPastTimeRule
	 */
	public Integer getClockEndTimeSetFIXValueWithPastTimeRule()
	{
		return clockEndTimeSetFIXValueWithPastTimeRule;
	}

	/**
	 * @param clockUnknownSetFIXValueWithPastTimeRule the clockUnknownSetFIXValueWithPastTimeRule to set
	 */
	public void setClockUnknownSetFIXValueWithPastTimeRule(Integer aClockPastTimeSetFIXValueRule)
	{
		this.clockUnknownSetFIXValueWithPastTimeRule = aClockPastTimeSetFIXValueRule;
	}

	/**
	 * @return the clockUnknownSetFIXValueWithPastTimeRule
	 */
	public Integer getClockUnknownSetFIXValueWithPastTimeRule()
	{
		return clockUnknownSetFIXValueWithPastTimeRule;
	}

	/**
	 * 'Identifies' Clock controls (by string within Control/@ID) as a 'Start', 'End', or 'Unknown' control 
	 * and returns the appropriate getClock___SetFIXValueWithPastTimeRule() value
	 * @param aControl
	 * @return
	 */
	public Integer getClockPastTimeSetFIXValueRule( ControlT aControl )
	{
		if ( aControl == null )
		{
			throw new IllegalStateException( "aControl provided was null.");
		}
		
		if ( ( aControl instanceof ClockT ) == false )
		{
			throw new IllegalStateException( "aControl: " + aControl + " ID: " + aControl.getID() + " was not a ClockT" );
		}
		
		if ( isClockControlStartTime( aControl ) )
		{
			logger.debug( "aControl: " + aControl.getID() + " identified as 'StartTime'.  Returning: " + getClockStartTimeSetFIXValueWithPastTimeRule() );
			return getClockStartTimeSetFIXValueWithPastTimeRule();
		}
		else if ( isClockControlEndTime( aControl ) )
		{
			logger.debug( "aControl: " + aControl.getID() + " identified as 'EndTime'.  Returning: " + getClockEndTimeSetFIXValueWithPastTimeRule() );
			return getClockEndTimeSetFIXValueWithPastTimeRule();
		}
		else
		{
			logger.debug( "aControl: " + aControl.getID() + " WAS NOT identified as either 'StartTime' or 'EndTime'.  Returning: " + getClockUnknownSetFIXValueWithPastTimeRule() );
			return getClockUnknownSetFIXValueWithPastTimeRule();
		}
	}
	
	/**
	 * @return the clockControlStartTimeIDValueFragmentList
	 */
	public String[] getClockControlStartTimeIDValueFragmentList()
	{
		return this.clockControlStartTimeIDValueFragmentList;
	}

	/**
	 * @param aClockControlStartTimeIDValueFragmentList the clockControlStartTimeIDValueFragmentList to set
	 */
	public void setClockControlStartTimeIDValueFragmentList(String[] aClockControlStartTimeIDValueFragmentList)
	{
		this.clockControlStartTimeIDValueFragmentList = aClockControlStartTimeIDValueFragmentList;
	}

	/**
	 * @return the clockControlEndTimeIDValueFragmentList
	 */
	public String[] getClockControlEndTimeIDValueFragmentList()
	{
		return this.clockControlEndTimeIDValueFragmentList;
	}

	/**
	 * @param aClockControlEndTimeIDValueFragmentList the clockControlEndTimeIDValueFragmentList to set
	 */
	public void setClockControlEndTimeIDValueFragmentList(String[] aClockControlEndTimeIDValueFragmentList)
	{
		this.clockControlEndTimeIDValueFragmentList = aClockControlEndTimeIDValueFragmentList;
	}

	/**
	 * Checks aControl.getID() to see if it contains any of the String "IDValueFragments" within getClockControlStartTimeIDValueFragmentList()
	 * @param aControl
	 * @return
	 */
	protected boolean isClockControlStartTime( ControlT aControl )
	{
		if ( ( aControl != null ) && ( aControl instanceof ClockT ) )
		{
			String tempControlID = aControl.getID();
			
			if ( getClockControlStartTimeIDValueFragmentList() != null )
			{
				for ( String tempIDValueFragment : getClockControlStartTimeIDValueFragmentList() )
				{
					if ( tempControlID.contains( tempIDValueFragment ) )
					{
						logger.debug( "aControl: " + aControl.getID() + " identified as 'StartTime' via IDValueFragment: " + tempIDValueFragment );
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks aControl.getID() to see if it contains any of the String "IDValueFragments" within getClockControlEndTimeIDValueFragmentList()
	 * @param aControl
	 * @return
	 */
	protected boolean isClockControlEndTime( ControlT aControl )
	{
		if ( ( aControl != null ) && ( aControl instanceof ClockT ) )
		{
			String tempControlID = aControl.getID();
			
			if ( getClockControlEndTimeIDValueFragmentList() != null )
			{
				for ( String tempIDValueFragment : getClockControlEndTimeIDValueFragmentList() )
				{
					if ( tempControlID.contains( tempIDValueFragment ) )
					{
						logger.debug( "aControl: " + aControl.getID() + " identified as 'EndTime' via IDValueFragment: " + tempIDValueFragment );
						return true;
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * Determines whether ClockT controls with Parameter type TZTimestamp
	 * 
	 * @return the showDateInputOnTimestampClockControl
	 */
	public boolean isShowDateInputOnTimestampClockControl() {
	    return showDateInputOnTimestampClockControl;
	}

	/**
	 * 
	 * 
	 * @param showDateInputOnTimestampClockControl the showDateInputOnTimestampClockControl to set
	 */
	public void setShowDateInputOnTimestampClockControl(
		boolean showDateInputOnTimestampClockControl) {
	    this.showDateInputOnTimestampClockControl = showDateInputOnTimestampClockControl;
	}

	/**
	 * FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file.
	 *  
	 * @param accommodateMixOfStrategyPanelsAndControls the accommodateMixOfStrategyPanelsAndControls to set
	 */
	public void setAccommodateMixOfStrategyPanelsAndControls(boolean accommodateMixOfStrategyPanelsAndControls)
	{
		this.accommodateMixOfStrategyPanelsAndControls = accommodateMixOfStrategyPanelsAndControls;
	}

	/**
	 * FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file.
	 * 
	 * @return the accommodateMixOfStrategyPanelsAndControls
	 */
	public boolean isAccommodateMixOfStrategyPanelsAndControls()
	{
		return accommodateMixOfStrategyPanelsAndControls;
	}

}
