package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;

/**
 * Represents the base, non-GUI system-specific "TesterApp" with a main() line.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractAtdl4jTesterApp
{

	static Atdl4jConfig atdl4jConfig;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	Atdl4jTesterPanel atdl4jTesterPanel;

	private void parseMainLineArgs( String[] args )
	{
//TODO		
/***
		// Load in a file if passed into the app arguments
		if (args.length > 0) {
			try {
//TODO 1/18/2010 Scott Atwell added BELOW
//				getAtdl4jConfig().getInputAndFilterData().init();
				
				if (args.length >= 2)
				{
					// -- InputCxlReplaceMode = args[1] (eg "true" or "false")
					logger.info("args[1]: " + args[1] + " Boolean.parseBoolean() as inputCxlReplaceMode");
					getAtdl4jConfig().getInputAndFilterData().setInputCxlReplaceMode( Boolean.parseBoolean( args[1] ) );
				}
				
				if ( args.length >= 3)
				{
					// -- InputHiddenFieldNameValueMap = args[2] (eg "FIX_OrderQty=10000|FIX_Side=1|FIX_OrdType=1") 
					String tempStringToParse = args[2];
					logger.info("args[2]: " + tempStringToParse + " parse as InputHiddenFieldNameValueMap (eg \"FIX_OrderQty=10000|FIX_Side=1|FIX_OrdType=1\")");
					String[] tempFieldAndValuesArray = tempStringToParse.split( "\\|" );
					if ( tempFieldAndValuesArray != null )
					{
						Map<String, String> tempInputHiddenFieldNameValueMap = new HashMap<String, String>();
						for (String tempFieldAndValue : tempFieldAndValuesArray )
						{
							String[] tempCombo = tempFieldAndValue.split( "=" );
							if ( ( tempCombo != null ) && ( tempCombo.length == 2 ) )
							{
								tempInputHiddenFieldNameValueMap.put( tempCombo[0], tempCombo[1] );
							}
						}
						
						logger.info("InputHiddenFieldNameValueMap: " + tempInputHiddenFieldNameValueMap);
						getAtdl4jConfig().getInputAndFilterData().addMapToInputHiddenFieldNameValueMap( tempInputHiddenFieldNameValueMap );
					}
				}
//TODO 1/18/2010 Scott Atwell added ABOVE
		
****/
	}
	

	protected void init( String[] args, Atdl4jConfig aAtdl4jConfig, Object aParentOrShell )
	{
		setAtdl4jConfig( aAtdl4jConfig );
		setParentOrShell( aParentOrShell );
		
		parseMainLineArgs( args );
		
		// -- Init the Atdl4jUserMessageHandler --
		if ( ( getAtdl4jConfig() != null ) && 
			  ( getAtdl4jConfig().getAtdl4jUserMessageHandler() != null ) && 
			  ( getAtdl4jConfig().getAtdl4jUserMessageHandler().isInitReqd() ) )
		{
			getAtdl4jConfig().initAtdl4jUserMessageHandler( aParentOrShell );
		}

		// -- ** Construct the core GUI component ** --
		setAtdl4jTesterPanel( getAtdl4jConfig().getAtdl4jTesterPanel() );
	}

	/**
	 * @return the atdl4jConfig
	 */
	public static Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}

	/**
	 * @param aAtdl4jConfig the atdl4jConfig to set
	 */
	private void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		atdl4jConfig = aAtdl4jConfig;
	}

	/**
	 * @return the parentOrShell
	 */
	public Object getParentOrShell()
	{
		return this.parentOrShell;
	}

	/**
	 * @param aParentOrShell the parentOrShell to set
	 */
	private void setParentOrShell(Object aParentOrShell)
	{
		this.parentOrShell = aParentOrShell;
	}


	/**
	 * @return the atdl4jTesterPanel
	 */
	public Atdl4jTesterPanel getAtdl4jTesterPanel()
	{
		return this.atdl4jTesterPanel;
	}


	/**
	 * @param aAtdl4jTesterPanel the atdl4jTesterPanel to set
	 */
	private void setAtdl4jTesterPanel(Atdl4jTesterPanel aAtdl4jTesterPanel)
	{
		this.atdl4jTesterPanel = aAtdl4jTesterPanel;
	}
	
}
