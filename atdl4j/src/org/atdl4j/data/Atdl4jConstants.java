/*
 * Created on Feb 10, 2010
 *
 */
package org.atdl4j.data;

/**
 * 
 * This class contains the data associated with the <code>Atdl4jConstants</code>.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 10, 2010
 */
public class Atdl4jConstants
{
	public static final String VALUE_NULL_INDICATOR = "{NULL}";
	
	// -- StrategyParametersGrp --
	public static final int TAG_NO_STRATEGY_PARAMETERS = 957;  // -- Repeating Group count --
	public static final int TAG_STRATEGY_PARAMETER_NAME = 958;
	public static final int TAG_STRATEGY_PARAMETER_TYPE = 959;
	public static final int TAG_STRATEGY_PARAMETER_VALUE = 960;

	public static int CLOCK_INIT_VALUE_MODE_USE_AS_IS = 0;  // default
	public static int CLOCK_INIT_VALUE_MODE_USE_CURRENT_TIME_IF_LATER = 1;  
	
	public static String INCREMENT_POLICY_STATIC = "Static";  // -- use value from increment attribute --
	public static String INCREMENT_POLICY_LOT_SIZE = "LotSize";  // -- use the round lot size of symbol --
	public static String INCREMENT_POLICY_TICK = "Tick";  // -- use symbol minimum tick size --
	
	public static String INIT_POLICY_USE_VALUE = "UseValue";  // -- use value from ControlT/@initValue --
	public static String INIT_POLICY_USE_FIX_FIELD = "UseFixField";  // -- use value from ControlT/@initFixField if available, else ControlT/@initValue --
	
	public static String STRATEGY_FILTER_REGION_TheAmericas = "TheAmericas";  // @see org.atdl4j.core.RegionT
	public static String STRATEGY_FILTER_REGION_EuropeMiddleEastAfricas = "EuropeMiddleEastAfrica";  // @see org.atdl4j.core.RegionT
	public static String STRATEGY_FILTER_REGION_AsiaPacificJapan = "AsiaPacificJapan";  // @see org.atdl4j.core.RegionT
	public static String[] STRATEGY_FILTER_REGIONS = new String[] { 
				STRATEGY_FILTER_REGION_TheAmericas,
				STRATEGY_FILTER_REGION_EuropeMiddleEastAfricas,
				STRATEGY_FILTER_REGION_AsiaPacificJapan };
				
	public static String STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderSingle = "D";
	public static String STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderList = "E";
	public static String STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderMultileg = "AB";
	public static String STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderCross = "s";
	public static String[] STRATEGY_FILTER_FIX_MSG_TYPES = new String[] { 
		STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderSingle,
		STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderList,
		STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderMultileg,
		STRATEGY_FILTER_FIX_MSG_TYPE_NewOrderCross };  // @see Strategy/@fixMsgType

	public static String STRATEGY_FILTER_SECURITY_TYPE_CommonStock = "CS";
	public static String STRATEGY_FILTER_SECURITY_TYPE_Future = "FUT";
	public static String STRATEGY_FILTER_SECURITY_TYPE_Option = "OPT";
	public static String STRATEGY_FILTER_SECURITY_TYPE_ForexSpot = "FXSPOT";
	public static String STRATEGY_FILTER_SECURITY_TYPE_ForexForward = "FXFWD";
	public static String[] STRATEGY_FILTER_SECURITY_TYPES = new String[] { 
		STRATEGY_FILTER_SECURITY_TYPE_CommonStock,
		STRATEGY_FILTER_SECURITY_TYPE_Future,
		STRATEGY_FILTER_SECURITY_TYPE_Option,
		STRATEGY_FILTER_SECURITY_TYPE_ForexSpot,
		STRATEGY_FILTER_SECURITY_TYPE_ForexForward };  // @see Strategy/@securityType

}
