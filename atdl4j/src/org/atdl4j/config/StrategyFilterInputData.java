package org.atdl4j.config;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.core.InclusionT;
import org.atdl4j.fixatdl.core.MarketT;
import org.atdl4j.fixatdl.core.RegionT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.core.SecurityTypesT.SecurityType;

/**
 * Data provided as input used to filter against <Strategy> filter criteria (eg Regions, Country, SecurityTypes, Markets, etc)
 * 
 * Creation date: (Apr 6, 2010 2:38:57 PM)
 * @author Scott atwell
 * @version 1.0, Apr 6, 2010
 */
public class StrategyFilterInputData
{
	private static final Logger logger = Logger.getLogger(StrategyFilterInputData.class);
	
	private String fixMsgType; 			// @see Strategy/@fixMsgType
	private String region_name;			// @see Strategy/Regions/Region/@name
	private String country_CountryCode;	// @see Strategy/Regions/Region/Country/@CountryCode
	private String securityType_name;	// @see Strategy/SecurityTypes/SecurityType/@name
	private String market_MICCode;		// @see Strategy/Markets/Market/@MICCode
	
	/**
	 * @return the fixMsgType
	 */
	public String getFixMsgType()
	{
		return this.fixMsgType;
	}
	/**
	 * @param aFixMsgType the fixMsgType to set
	 */
	public void setFixMsgType(String aFixMsgType)
	{
		this.fixMsgType = aFixMsgType;
	}
	/**
	 * @param aFixMsgType @see Strategy/@fixMsgType
	 * @param aStrategy
	 * @return
	 */
	public boolean isFixMsgTypeSupportedForStrategy(String aFixMsgType, StrategyT aStrategy)
	{
		if ( ( aFixMsgType != null ) &&
			  ( aStrategy.getFixMsgType() != null ) )
		{
			return aStrategy.getFixMsgType().equals( aFixMsgType );
		}
		else
		{
			return true;
		}
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
	 * @param aRegion_name  Region's name attribute
	 * @param aCountry_CountryCode  Country's countryCode attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isRegionCountrySupportedForStrategy(String aRegion_name, String aCountry_CountryCode, StrategyT aStrategy)
	{
		if ( ( aCountry_CountryCode != null ) && ( aRegion_name == null ) )
		{
			logger.warn( "ERROR: isRegionCountrySupportedForStrategy() received CountryCode: " + aCountry_CountryCode + ", however, Region was not specified." );
			return true;
		}
		
		if ( ( aRegion_name != null ) &&
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
							for ( RegionT.Country tempCountry : tempRegion.getCountry() )
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
							for ( RegionT.Country tempCountry : tempRegion.getCountry() )
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
	 * @param strategy
	 * @return
	 */
	public boolean isStrategySupported(StrategyT aStrategy)
	{
		if ( aStrategy == null )
		{
			return false;
		}
		
		String tempFixMsgType = getFixMsgType();
		String tempSecurityType_name = getSecurityType_name();
		String tempMarket_MICCode = getMarket_MICCode();
		String tempRegion_name = getRegion_name();
		String tempCountry_CountryCode = getCountry_CountryCode();

// 3/4/2010 Scott Atwell added			
		if ( isFixMsgTypeSupportedForStrategy( tempFixMsgType, aStrategy ) == false )
		{
			logger.info("Excluding strategy: " + aStrategy.getName() + " as isFixMsgTypeSupportedForStrategy() returned false for tempFixMsgType: " + tempFixMsgType );
			return false;
		}
		
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

		return true;
	}
	/**
	 * @return the region_name
	 */
	public String getRegion_name()
	{
		return this.region_name;
	}
	/**
	 * @param aRegionName the region_name to set
	 */
	public void setRegion_name(String aRegionName)
	{
		this.region_name = aRegionName;
	}
	/**
	 * @return the country_CountryCode
	 */
	public String getCountry_CountryCode()
	{
		return this.country_CountryCode;
	}
	/**
	 * @param aCountryCountryCode the country_CountryCode to set
	 */
	public void setCountry_CountryCode(String aCountryCountryCode)
	{
		this.country_CountryCode = aCountryCountryCode;
	}
	/**
	 * @return the securityType_name
	 */
	public String getSecurityType_name()
	{
		return this.securityType_name;
	}
	/**
	 * @param aSecurityTypeName the securityType_name to set
	 */
	public void setSecurityType_name(String aSecurityTypeName)
	{
		this.securityType_name = aSecurityTypeName;
	}
	/**
	 * @return the market_MICCode
	 */
	public String getMarket_MICCode()
	{
		return this.market_MICCode;
	}
	/**
	 * @param aMarketMICCode the market_MICCode to set
	 */
	public void setMarket_MICCode(String aMarketMICCode)
	{
		this.market_MICCode = aMarketMICCode;
	}
	/**
	 * @return the logger
	 */
	public static Logger getLogger()
	{
		return logger;
	}

}
