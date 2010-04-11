/*
 * Created on Jan 19, 2010
 *
 */
package org.atdl4j.config;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * This class contains the "input" data provided by the external application which drive
 * Strategy eligibility (eg SecurityType_name, Market_MICCode, Region_name, Country_CountryCode) filtering logic 
 * (see INPUT_FIELD_NAME_STRATEGY_FILTER_* constants for exact names).
 * 
 * The input data may also contain external values from "standard FIX fields" (eg OrderQty, Side, Price, TimeInForce, etc).
 * These values are passed by prefixing the name of each fieldname/value pair (<String, String>) with "FIX_" (eg "FIX_OrderQty", "FIX_Side").
 * The values are FIX wire values.  These can then be used as part of StateRule or ValidationRule logic.
 * 
 * All of the input data is stored within a Map<String, String> consisting of fieldname and value (String wirevalue).
 *  
 * Creation date: (Jan 19, 2010 8:56:36 AM)
 * @author Scott Atwell
 */
public class InputAndFilterData
{
	private static final Logger logger = Logger.getLogger(InputAndFilterData.class);

	public static String FIX_DEFINED_FIELD_PREFIX = "FIX_";
	
	// -- Constants for "Hidden" parameters passed to app used to control eligibility of each Strategy -- 
	// -- ("FIX_" + "FIXatdl_" + XmlElement + "_" + XmlAttribute) --
	
/*** 4/6/2010 Scott Atwell moved to StrategyFilterInputData
// 3/4/2010 Scott Atwell Added	
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_FIX_MSG_TYPE = "FIX_FIXatdl_FixMsgType";  // @see Strategy/@fixMsgType
	
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_SECURITY_TYPE_name = "FIX_FIXatdl_SecurityType_name";
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_MARKET_MICCode = "FIX_FIXatdl_Market_MICCode";
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_REGION_name = "FIX_FIXatdl_Region_name";
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_COUNTRY_CountryCode = "FIX_FIXatdl_Country_CountryCode";
***/
	private List<StrategyFilterInputData> strategyFilterInputDataList = null;  // -- Use more than one when multiple individual orders are associated with a single algo panel display --
	
	public static String INPUT_FIELD_NAME_INCREMENT_POLICY_LotSize = "FIX_FIXatdl_LotSize";
	public static String INPUT_FIELD_NAME_INCREMENT_POLICY_Tick = "FIX_FIXatdl_Tick";
	
	private boolean inputCxlReplaceMode = false;
	
	// -- Used to pass "other standard FIX message fields (eg FIX_OrderQty, FIX_Price, FIX_TimeInForce, etc)" and Strategy-eligibility ones (eg FIX_FIXatdl_Region, FIX_FIXatdl_Country, etc) -- 
	private Map<String, String> inputHiddenFieldNameValueMap = null;
	
	private List<String> inputStrategyUiRepOrNameList = null;  // -- if specified, controls the order strategy name choices presented to the user --
	private Boolean applyInputStrategyUiRepOrNameListAsFilter = null; // -- if specified, exclude any strategyNames not in inputStrategyUiRepOrNameList -- 

	private String inputSelectStrategyName = null;

	/**
	 * 
	 */
	public InputAndFilterData()
	{
		init();
	}
	
	/**
	 * 
	 */
	public void init()
	{
		setInputHiddenFieldNameValueMap( null );
		setInputCxlReplaceMode( false );
	}
	
	/**
	 * @param aInputCxlReplaceMode
	 */
	public void setInputCxlReplaceMode( boolean aInputCxlReplaceMode )
	{
		inputCxlReplaceMode = aInputCxlReplaceMode;
	}

	/**
	 * @param inputSelectStrategyName the inputSelectStrategyName to set
	 */
	public void setInputSelectStrategyName(String inputSelectStrategyName)
	{
		this.inputSelectStrategyName = inputSelectStrategyName;
	}

	/**
	 * @return the inputSelectStrategyName
	 */
	public String getInputSelectStrategyName()
	{
		return inputSelectStrategyName;
	}

	/**
	 * @return
	 */
	public boolean getInputCxlReplaceMode()
	{
		return inputCxlReplaceMode;
	}

	/**
	 * @return the inputHiddenFieldNameValueMap
	 */
	public Map<String, String> getInputHiddenFieldNameValueMap()
	{
		return inputHiddenFieldNameValueMap;
	}

	/**
	 * Helper method
	 * @param aFieldName
	 * @return
	 */
	public String getInputHiddenFieldValue( String aFieldName )
	{
		if ( getInputHiddenFieldNameValueMap() != null )
		{
			return getInputHiddenFieldNameValueMap().get( aFieldName );
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param aInputHiddenFieldNameValueMap the inputHiddenFieldNameValueMap to set
	 */
	protected void setInputHiddenFieldNameValueMap(Map<String, String> aInputHiddenFieldNameValueMap)
	{
		inputHiddenFieldNameValueMap = aInputHiddenFieldNameValueMap;
	}


	/**
	 * @param aMap
	 */
	public void addMapToInputHiddenFieldNameValueMap( Map<String, String> aMap )
	{
		if ( getInputHiddenFieldNameValueMap() == null )
		{
			setInputHiddenFieldNameValueMap( new HashMap<String, String>() );
		}
		
		getInputHiddenFieldNameValueMap().putAll( aMap );
	}
	
	
	/**
	 * Checks applicability of aStrategy against each StrategyFilterInputData within getStrategyFilterInputDataList().
	 * Returns true only if aStrategy meets criteria for all (least common denominator).
	 * 
	 * @param strategy
	 * @return
	 */
	public boolean isStrategySupported(StrategyT aStrategy)
	{
		if ( aStrategy == null )
		{
			return false;
		}
		
		if ( getStrategyFilterInputDataList() != null )
		{
			for ( StrategyFilterInputData tempStrategyFilterInputData : getStrategyFilterInputDataList() )
			{
				if ( tempStrategyFilterInputData.isStrategySupported( aStrategy ) == false )
				{
					return false;
				}
			}
		}
		
		return true;
	}


	/**
	 * @param aFieldName
	 * @param aFieldValue
	 */
	public void setInputHiddenFieldNameValuePair( String aFieldName, String aFieldValue )
	{
		if ( getInputHiddenFieldNameValueMap() == null )
		{
			setInputHiddenFieldNameValueMap( new HashMap<String, String>() );
		}
		
		if ( aFieldValue != null )
		{
			getInputHiddenFieldNameValueMap().put( aFieldName, aFieldValue );
		}
		else
		{
			// - attempt to remove existing entry if aFieldValue is null --
			getInputHiddenFieldNameValueMap().remove( aFieldName );
		}
	}
	
	/**
	 * This will prefix aFieldName with FIX_STANDARD_FIELD_INPUT_FIELD_NAME_PREFIX
	 * @param aFieldName
	 * @param aFieldValue
	 */
	public void setInputStandardFixFieldNameValuePair( String aFieldName, String aFieldValue )
	{
		if ( getInputHiddenFieldNameValueMap() == null )
		{
			setInputHiddenFieldNameValueMap( new HashMap<String, String>() );
		}
		
		getInputHiddenFieldNameValueMap().put( (FIX_DEFINED_FIELD_PREFIX + aFieldName), aFieldValue );
	}
	
	/**
	 */
	public BigInteger getInputIncrementPolicy_LotSize()
	{
		String tempValue = getInputHiddenFieldValue( INPUT_FIELD_NAME_INCREMENT_POLICY_LotSize );
		if ( tempValue != null )
		{
			return new BigInteger( tempValue );
		}
		else
		{
			return null;
		}
	}
	
	/**
	 */
	public BigInteger getInputIncrementPolicy_Tick()
	{
		String tempValue = getInputHiddenFieldValue( INPUT_FIELD_NAME_INCREMENT_POLICY_Tick );
		if ( tempValue != null )
		{
			return new BigInteger( tempValue );
		}
		else
		{
			return null;
		}
	}

	/**
	 */
	public void setInputIncrementPolicy_LotSize( BigInteger aLotSize )
	{
		if ( aLotSize != null )
		{
			setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_INCREMENT_POLICY_LotSize, aLotSize.toString() );
		}
		else
		{
			setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_INCREMENT_POLICY_LotSize, null);
		}
	}
	
	/**
	 */
	public void setInputIncrementPolicy_Tick( BigInteger aTick )
	{
		if ( aTick != null )
		{
			setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_INCREMENT_POLICY_Tick, aTick.toString() );
		}
		else
		{
			setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_INCREMENT_POLICY_Tick, null );
		}
	}


	/**
	 * If specified, controls the order strategy name choices presented to the user
	 * @param inputStrategyUiRepOrNameList the inputStrategyUiRepOrNameList to set
	 */
	public void setInputStrategyUiRepOrNameList(List<String> inputStrategyUiRepOrNameList)
	{
		this.inputStrategyUiRepOrNameList = inputStrategyUiRepOrNameList;
	}

	/**
	 * If specified, controls the order strategy name choices presented to the user
	 * @return the inputStrategyUiRepOrNameList
	 */
	public List<String> getInputStrategyUiRepOrNameList()
	{
		return inputStrategyUiRepOrNameList;
	}

	/**
	 * If specified, exclude any strategyNames not in inputStrategyUiRepOrNameList
	 * @param applyInputStrategyUiRepOrNameListAsFilter the applyInputStrategyUiRepOrNameListAsFilter to set
	 */
	public void setApplyInputStrategyUiRepOrNameListAsFilter(Boolean applyInputStrategyUiRepOrNameListAsFilter)
	{
		this.applyInputStrategyUiRepOrNameListAsFilter = applyInputStrategyUiRepOrNameListAsFilter;
	}

	/**
	 * If specified, exclude any strategyNames not in inputStrategyUiRepOrNameList
	 * @return the applyInputStrategyUiRepOrNameListAsFilter
	 */
	public Boolean getApplyInputStrategyUiRepOrNameListAsFilter()
	{
		return applyInputStrategyUiRepOrNameListAsFilter;
	}

	/**
	 * Use more than one when multiple individual orders are associated with a single algo panel display
	 * @return the strategyFilterInputDataList
	 */
	public List<StrategyFilterInputData> getStrategyFilterInputDataList()
	{
		return this.strategyFilterInputDataList;
	}

	/**
	 * Use more than one when multiple individual orders are associated with a single algo panel display
	 * @param aStrategyFilterInputDataList the strategyFilterInputDataList to set
	 */
	public void setStrategyFilterInputDataList(List<StrategyFilterInputData> aStrategyFilterInputDataList)
	{
		this.strategyFilterInputDataList = aStrategyFilterInputDataList;
	}
	
	/**
	 * Convenience method for single order for algo panel.
	 * @param aStrategyFilterInputData
	 */
	public void setStrategyFilterInputData(StrategyFilterInputData aStrategyFilterInputData)
	{
		if ( aStrategyFilterInputData != null )
		{
			List<StrategyFilterInputData> tempList = new ArrayList<StrategyFilterInputData>();
			tempList.add( aStrategyFilterInputData );
			setStrategyFilterInputDataList( tempList );
		}
		else
		{
			setStrategyFilterInputDataList( null );
		}
	}

}
