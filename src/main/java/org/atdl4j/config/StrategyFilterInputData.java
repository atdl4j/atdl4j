package org.atdl4j.config;

import org.atdl4j.fixatdl.core.InclusionT;
import org.atdl4j.fixatdl.core.MarketT;
import org.atdl4j.fixatdl.core.RegionT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(StrategyFilterInputData.class);
	
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
	 * @param aMarketMICCode  Market's MICCode attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isMarketSupportedForStrategy(String aMarketMICCode, StrategyT aStrategy)
	{
		if ( ( aMarketMICCode != null ) &&
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
					if ( aMarketMICCode.equals( tempMarket.getMICCode() ) )
					{
						logger.info("isMarketSupportedForStrategy(): strategy: {} named exclusion for Market_MICCode: {}", aStrategy.getName(), aMarketMICCode );
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
					
					if ( aMarketMICCode.equals( tempMarket.getMICCode() ) )
					{
						logger.info("isMarketSupportedForStrategy(): strategy: {} named inclusion for Market_MICCode: {}", aStrategy.getName(), aMarketMICCode );
						return true;  // -- named inclusion --
					}
				}
			}
			
			if ( (!tempExcludesExist) &&
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
	 * @param aRegionName  Region's name attribute
	 * @param aCountryCountryCode  Country's countryCode attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isRegionCountrySupportedForStrategy(String aRegionName, String aCountryCountryCode, StrategyT aStrategy)
	{
		if ( ( aCountryCountryCode != null ) && ( aRegionName == null ) )
		{
			logger.warn( "ERROR: isRegionCountrySupportedForStrategy() received CountryCode: {}, however, Region was not specified.", aCountryCountryCode );
			return true;
		}
		
		if ( ( aRegionName != null ) &&
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
					if ( aRegionName.equals( tempRegion.getName() ) )
					{
						// -- Verify that the Country is not in the named Inclusion list for this Region --
						if ( ( aCountryCountryCode != null ) &&
							  ( tempRegion.getCountry() != null ) )
						{
							for ( RegionT.Country tempCountry : tempRegion.getCountry() )
							{
								if ( ( InclusionT.INCLUDE.equals( tempCountry.getInclusion() ) ) &&
									  ( aCountryCountryCode.equals( tempCountry.getCountryCode() ) ) )
								{
									logger.info("isRegionCountrySupportedForStrategy(): strategy: {} named inclusion for Country_CountryCode: {} despite Region_name: {} being excluded", aStrategy.getName(), aCountryCountryCode, aRegionName );
									return true;   // Country Inclusion Exists
								}
							}
						}
						
						logger.info("isRegionCountrySupportedForStrategy(): strategy: {} named exclusion for Region_name: {}", aStrategy.getName(), aRegionName );
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
					if ( aRegionName.equals( tempRegion.getName() ) )
					{
						boolean tempCountryInclusionsForARegionInclusionExist = false;
						
						// -- Verify that the Country is not in the named Exclusion list for this Region --
						if ( ( aCountryCountryCode != null ) &&
							  ( tempRegion.getCountry() != null ) )
						{
							for ( RegionT.Country tempCountry : tempRegion.getCountry() )
							{
								if ( ( InclusionT.EXCLUDE.equals( tempCountry.getInclusion() ) ) &&
									  ( aCountryCountryCode.equals( tempCountry.getCountryCode() ) ) )
								{
									logger.info("isRegionCountrySupportedForStrategy(): strategy: {} named exclusion for Country_CountryCode: {} despite Region_name: {} being included", aStrategy.getName(), aCountryCountryCode, aRegionName );
									return false;   // Country Exclusion Exists
								}
								else if ( InclusionT.INCLUDE.equals( tempCountry.getInclusion() ) )
								{
									tempCountryInclusionsForARegionInclusionExist = true;
									if ( aCountryCountryCode.equals( tempCountry.getCountryCode() ) )
									{
										logger.info("isRegionCountrySupportedForStrategy(): strategy: {} named inclusion for Country_CountryCode: {} for included Region_name: {}", aStrategy.getName(), aCountryCountryCode, aRegionName );
										return true; // Country named as Inclusion
									}
								}
							}
						}
						
						if ( tempCountryInclusionsForARegionInclusionExist )
						{
							logger.info("isRegionCountrySupportedForStrategy(): strategy: {} Country_CountryCode: {} was not part of included Countries for named inclusion for Region_name: {}", aStrategy.getName(), aCountryCountryCode, aRegionName );
							return false;  // -- Country not named as inclusion for included Region --
						}
						else
						{
							logger.info("isRegionCountrySupportedForStrategy(): strategy: {} named inclusion for Region_name: {}", aStrategy.getName(), aRegionName );
							return true;  // -- named inclusion for Region --
						}
					}
				}
			}
			
			if ( (!tempExcludesExist) &&
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
	 * @param aSecurityTypeName  SecurityType's name attribute
	 * @param aStrategy
	 * @return
	 */
	public boolean isSecurityTypeSupportedForStrategy(String aSecurityTypeName, StrategyT aStrategy)
	{
		if ( ( aSecurityTypeName != null ) &&
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
					if ( aSecurityTypeName.equals( tempSecurityType.getName() ) )
					{
						logger.info("isSecurityTypeSupportedForStrategy(): strategy: {} named exclusion for SecurityType_name: {}", aStrategy.getName(), aSecurityTypeName );
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
					
					if ( aSecurityTypeName.equals( tempSecurityType.getName() ) )
					{
						logger.info("isSecurityTypeSupportedForStrategy(): strategy: {} named inclusion for SecurityType_name: {}", aStrategy.getName(), aSecurityTypeName );
						return true;  // -- named inclusion --
					}
				}
			}
			
			if ( (!tempExcludesExist) &&
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
	 * @param aStrategy
	 * @return
	 */
	public boolean isStrategySupported(StrategyT aStrategy)
	{
		if ( aStrategy == null )
		{
			return false;
		}
		
		String tempFixMsgType = getFixMsgType();
		String tmpSecurityTypeName = getSecurityType_name();
		String tmpMarketMicCode = getMarket_MICCode();
		String tmpRegionName = getRegion_name();
		String tmpCountryCode = getCountry_CountryCode();

		if (!isFixMsgTypeSupportedForStrategy(tempFixMsgType, aStrategy))
		{
			logger.info("Excluding strategy: {} as isFixMsgTypeSupportedForStrategy() returned false for tempFixMsgType: {}", aStrategy.getName(), tempFixMsgType );
			return false;
		}
		
		if (!isSecurityTypeSupportedForStrategy(tmpSecurityTypeName, aStrategy))
		{
			logger.info("Excluding strategy: {} as isSecurityTypeSupportedForStrategy() returned false for tempSecurityType_name: {}", aStrategy.getName(), tmpSecurityTypeName );
			return false;
		}
		
		if (!isMarketSupportedForStrategy(tmpMarketMicCode, aStrategy))
		{
			logger.info("Excluding strategy: {} as isMarketSupportedForStrategy() returned false for tempMarket_MICCode: {}", aStrategy.getName(), tmpMarketMicCode );
			return false;
		}
		
		if (!isRegionCountrySupportedForStrategy(tmpRegionName, tmpCountryCode, aStrategy))
		{
			logger.info("Excluding strategy: {} as isRegionCountrySupportedForStrategy() returned false for tempRegion: {} tempCountry_CountryCode: {}", aStrategy.getName(), tmpRegionName, tmpCountryCode );
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
