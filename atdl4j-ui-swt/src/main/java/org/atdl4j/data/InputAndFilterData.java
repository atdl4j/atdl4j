/*
 * Created on Jan 19, 2010
 *
 */
package org.atdl4j.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.CountryT;
import org.atdl4j.atdl.core.InclusionT;
import org.atdl4j.atdl.core.MarketT;
import org.atdl4j.atdl.core.RegionT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.core.SecurityTypesT.SecurityType;

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
	/**
	 * 
	 */
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_SECURITY_TYPE_name = "FIX_FIXatdl_SecurityType_name";
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_MARKET_MICCode = "FIX_FIXatdl_Market_MICCode";
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_REGION_name = "FIX_FIXatdl_Region_name";
	public static String INPUT_FIELD_NAME_STRATEGY_FILTER_COUNTRY_CountryCode = "FIX_FIXatdl_Country_CountryCode";

	
	private boolean inputCxlReplaceMode = false;
	
	// -- Used to pass "other standard FIX message fields (eg FIX_OrderQty, FIX_Price, FIX_TimeInForce, etc)" and Strategy-eligibility ones (eg FIX_FIXatdl_Region, FIX_FIXatdl_Country, etc) -- 
	private Map<String, String> inputHiddenFieldNameValueMap = null;
	
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
	 * @param strategy
	 * @return
	 */
	public boolean isStrategySupported(StrategyT aStrategy)
	{
		if ( getInputHiddenFieldNameValueMap() != null )
		{
			String tempSecurityType_name = getInputStrategyFilterSecurityType_name();
			String tempMarket_MICCode = getInputStrategyFilterMarket_MICCode();
			String tempRegion_name = getInputStrategyFilterRegion_name();
			String tempCountry_CountryCode = getInputStrategyFilterCountry_CountryCode();
			
			if ( isSecurityTypeSupportedForStrategy( tempSecurityType_name, aStrategy ) == false )
			{
				logger.info("Excluding strategy: " + aStrategy.getName() + " as isSecurityTypeSupportedForStrategy() returned false for tempSecurityType_name: " + tempSecurityType_name );
				return false;
			}
			
			if ( isMarketSupportedForStrategy( tempMarket_MICCode, aStrategy ) == false )
			{
				logger.info("Excluding strategy: " + aStrategy.getName() + " as isMarketSupportedForStrategy() returned false for tempMarket_MICCode: " + tempMarket_MICCode );
				return false;
			}
			
			if ( isRegionCountrySupportedForStrategy( tempRegion_name, tempCountry_CountryCode, aStrategy ) == false )
			{
				logger.info("Excluding strategy: " + aStrategy.getName() + " as isRegionCountrySupportedForStrategy() returned false for tempRegion: " + tempRegion_name + " tempCountry_CountryCode: " + tempCountry_CountryCode );
				return false;
			}
		}

		return true;
	}


	/**
	 * @param aSecurityType_name  SecurityType's name attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isSecurityTypeSupportedForStrategy(String aSecurityType_name, StrategyT aStrategy)
	{
		if ( ( aSecurityType_name != null ) &&
			  ( aStrategy.getSecurityTypes() != null ) && 
			  ( aStrategy.getSecurityTypes().getSecurityType() != null ) )
		{
			boolean tempExcludesExist = false;
			
			//  -- Check for exclusions --
			for (SecurityType tempSecurityType : aStrategy.getSecurityTypes().getSecurityType() )
			{
				if ( InclusionT.EXCLUDE.equals( tempSecurityType.getInclusion() ) )
				{
					tempExcludesExist = true;
					if ( aSecurityType_name.equals( tempSecurityType.getName() ) )
					{
						logger.info("isSecurityTypeSupportedForStrategy(): strategy: " + aStrategy.getName() + " named exclusion for SecurityType_name: " + aSecurityType_name );
						return false;  // -- named exclusion --
					}
				}
			}
			
			boolean tempIncludesExist = false;
			//  -- Verify inclusions  --
			for (SecurityType tempSecurityType : aStrategy.getSecurityTypes().getSecurityType() )
			{
				if ( InclusionT.INCLUDE.equals( tempSecurityType.getInclusion() ) )
				{
					tempIncludesExist = true;
					
					if ( aSecurityType_name.equals( tempSecurityType.getName() ) )
					{
						logger.info("isSecurityTypeSupportedForStrategy(): strategy: " + aStrategy.getName() + " named inclusion for SecurityType_name: " + aSecurityType_name );
						return true;  // -- named inclusion --
					}
				}
			}
			
			if ( ( tempExcludesExist == false ) &&
				  ( tempIncludesExist ) )
			{
				return false;  // -- only inclusions exist and matching inclusion was not found above --
			}
			else
			{
				return true;
			}
		}
		
		return true;
	}


	/**
	 * @param aRegion_name  Region's name attribute
	 * @param aCountry_CountryCode  Country's countryCode attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isRegionCountrySupportedForStrategy(String aRegion_name, String aCountry_CountryCode, StrategyT aStrategy)
	{
		if ( ( ( aRegion_name != null ) || ( aCountry_CountryCode != null ) ) &&
			  ( aStrategy.getRegions() != null ) && 
			  ( aStrategy.getRegions().getRegion() != null ) )
		{
			boolean tempExcludesExist = false;
			
			//  -- Check for exclusions --
			for (RegionT tempRegion : aStrategy.getRegions().getRegion() )
			{
				if ( InclusionT.EXCLUDE.equals( tempRegion.getInclusion() ) )
				{
					tempExcludesExist = true;
					if ( aRegion_name.equals( tempRegion.getName() ) )
					{
						// -- Verify that the Country is not in the named Inclusion list for this Region --
						if ( ( aCountry_CountryCode != null ) &&
							  ( tempRegion.getCountry() != null ) )
						{
							for ( CountryT tempCountry : tempRegion.getCountry() )
							{
								if ( ( InclusionT.INCLUDE.equals( tempCountry.getInclusion() ) ) &&
									  ( aCountry_CountryCode.equals( tempCountry.getCountryCode() ) ) )
								{
									logger.info("isRegionCountrySupportedForStrategy(): strategy: " + aStrategy.getName() + " named inclusion for Country_CountryCode: " + aCountry_CountryCode + " despite Region_name: " + aRegion_name + " being excluded" );
									return true;   // Country Inclusion Exists
								}
							}
						}
						
						logger.info("isRegionCountrySupportedForStrategy(): strategy: " + aStrategy.getName() + " named exclusion for Region_name: " + aRegion_name );
						return false;  // -- named exclusion for Region --
					}
				}
			}
			
			boolean tempIncludesExist = false;
			//  -- Verify inclusions  --
			for (RegionT tempRegion : aStrategy.getRegions().getRegion() )
			{
				if ( InclusionT.INCLUDE.equals( tempRegion.getInclusion() ) )
				{
					tempIncludesExist = true;
					if ( aRegion_name.equals( tempRegion.getName() ) )
					{
						boolean tempCountryInclusionsForARegionInclusionExist = false;
						
						// -- Verify that the Country is not in the named Exclusion list for this Region --
						if ( ( aCountry_CountryCode != null ) &&
							  ( tempRegion.getCountry() != null ) )
						{
							for ( CountryT tempCountry : tempRegion.getCountry() )
							{
								if ( ( InclusionT.EXCLUDE.equals( tempCountry.getInclusion() ) ) &&
									  ( aCountry_CountryCode.equals( tempCountry.getCountryCode() ) ) )
								{
									logger.info("isRegionCountrySupportedForStrategy(): strategy: " + aStrategy.getName() + " named exclusion for Country_CountryCode: " + aCountry_CountryCode + " despite Region_name: " + aRegion_name + " being included" );
									return false;   // Country Exclusion Exists
								}
								else if ( InclusionT.INCLUDE.equals( tempCountry.getInclusion() ) )
								{
									tempCountryInclusionsForARegionInclusionExist = true;
									if ( aCountry_CountryCode.equals( tempCountry.getCountryCode() ) )
									{
										logger.info("isRegionCountrySupportedForStrategy(): strategy: " + aStrategy.getName() + " named inclusion for Country_CountryCode: " + aCountry_CountryCode + " for included Region_name: " + aRegion_name );
										return true; // Country named as Inclusion
									}
								}
							}
						}
						
						if ( tempCountryInclusionsForARegionInclusionExist )
						{
							logger.info("isRegionCountrySupportedForStrategy(): strategy: " + aStrategy.getName() + " Country_CountryCode: " + aCountry_CountryCode + " was not part of included Countries for named inclusion for Region_name: " + aRegion_name );
							return false;  // -- Country not named as inclusion for included Region --
						}
						else
						{
							logger.info("isRegionCountrySupportedForStrategy(): strategy: " + aStrategy.getName() + " named inclusion for Region_name: " + aRegion_name );
							return true;  // -- named inclusion for Region --
						}
					}
				}
			}
			
			if ( ( tempExcludesExist == false ) &&
				  ( tempIncludesExist ) )
			{
				return false;  // -- only inclusions exist and matching inclusion was not found above --
			}
			else
			{
				return true;
			}
		}
		
		return true;
	}

	/**
	 * @param aMarket_MICCode  Market's MICCode attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isMarketSupportedForStrategy(String aMarket_MICCode, StrategyT aStrategy)
	{
		if ( ( aMarket_MICCode != null ) &&
			  ( aStrategy.getMarkets() != null ) && 
			  ( aStrategy.getMarkets().getMarket() != null ) )
		{
			boolean tempExcludesExist = false;
			
			//  -- Check for exclusions --
			for (MarketT tempMarket : aStrategy.getMarkets().getMarket() )
			{
				if ( InclusionT.EXCLUDE.equals( tempMarket.getInclusion() ) )
				{
					tempExcludesExist = true;
					if ( aMarket_MICCode.equals( tempMarket.getMICCode() ) )
					{
						logger.info("isMarketSupportedForStrategy(): strategy: " + aStrategy.getName() + " named exclusion for Market_CFICode: " + aMarket_MICCode );
						return false;  // -- named exclusion --
					}
				}
			}
			
			boolean tempIncludesExist = false;
			//  -- Verify inclusions  --
			for (MarketT tempMarket : aStrategy.getMarkets().getMarket() )
			{
				if ( InclusionT.INCLUDE.equals( tempMarket.getInclusion() ) )
				{
					tempIncludesExist = true;
					
					if ( aMarket_MICCode.equals( tempMarket.getMICCode() ) )
					{
						logger.info("isMarketSupportedForStrategy(): strategy: " + aStrategy.getName() + " named inclusion for Market_MICCode: " + aMarket_MICCode );
						return true;  // -- named inclusion --
					}
				}
			}
			
			if ( ( tempExcludesExist == false ) &&
				  ( tempIncludesExist ) )
			{
				return false;  // -- only inclusions exist and matching inclusion was not found above --
			}
			else
			{
				return true;
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
		
		getInputHiddenFieldNameValueMap().put( aFieldName, aFieldValue );
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
	 * @param aFieldValue
	 */
	public void setInputStrategyFilterSecurityType_name( String aFieldValue )
	{
		setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_STRATEGY_FILTER_SECURITY_TYPE_name, aFieldValue );
	}
	
	/**
	 * @param aFieldValue
	 */
	public String getInputStrategyFilterSecurityType_name()
	{
		return getInputHiddenFieldValue( INPUT_FIELD_NAME_STRATEGY_FILTER_SECURITY_TYPE_name );
	}
	
	/**
	 * @param aFieldValue
	 */
	public void setInputStrategyFilterMarket_MICCode( String aFieldValue )
	{
		setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_STRATEGY_FILTER_MARKET_MICCode, aFieldValue );
	}
	
	/**
	 * @param aFieldValue
	 */
	public String getInputStrategyFilterMarket_MICCode()
	{
		return getInputHiddenFieldValue( INPUT_FIELD_NAME_STRATEGY_FILTER_MARKET_MICCode );
	}
	
	/**
	 * @param aFieldValue
	 */
	public void setInputStrategyFilterRegion_name( String aFieldValue )
	{
		setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_STRATEGY_FILTER_REGION_name, aFieldValue );
	}
	
	/**
	 * @param aFieldValue
	 */
	public String getInputStrategyFilterRegion_name()
	{
		return getInputHiddenFieldValue( INPUT_FIELD_NAME_STRATEGY_FILTER_REGION_name );
	}
	
	/**
	 * @param aFieldValue
	 */
	public void setInputStrategyFilterCountry_CountryCode( String aFieldValue )
	{
		setInputHiddenFieldNameValuePair( INPUT_FIELD_NAME_STRATEGY_FILTER_COUNTRY_CountryCode, aFieldValue );
	}
	
	/**
	 * @param aFieldValue
	 */
	public String getInputStrategyFilterCountry_CountryCode()
	{
		return getInputHiddenFieldValue( INPUT_FIELD_NAME_STRATEGY_FILTER_COUNTRY_CountryCode );
	}
	
}
