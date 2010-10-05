package org.atdl4j.ui.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jTesterPanel;

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
	private final Logger logger = Logger.getLogger(AbstractAtdl4jTesterApp.class);

	static Atdl4jOptions atdl4jOptions;
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
//				getAtdl4jOptions().getInputAndFilterData().init();
				
				if (args.length >= 2)
				{
					// -- InputCxlReplaceMode = args[1] (eg "true" or "false")
					logger.info("args[1]: " + args[1] + " Boolean.parseBoolean() as inputCxlReplaceMode");
					getAtdl4jOptions().getInputAndFilterData().setInputCxlReplaceMode( Boolean.parseBoolean( args[1] ) );
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
						getAtdl4jOptions().getInputAndFilterData().addMapToInputHiddenFieldNameValueMap( tempInputHiddenFieldNameValueMap );
					}
				}
//TODO 1/18/2010 Scott Atwell added ABOVE
		
****/
	}
	

// 9/29/2010 Scott Atwell	protected void init( String[] args, Atdl4jOptions aAtdl4jOptions, Object aParentOrShell )
	protected void init( String[] args, Atdl4jConfiguration aAtdl4jConfiguration, Atdl4jOptions aAtdl4jOptions, Object aParentOrShell )
	{
// 9/29/2010 Scott Atwell added		
		Atdl4jConfig.setConfig( aAtdl4jConfiguration );
		
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		parseMainLineArgs( args );
		
		// -- Init the Atdl4jUserMessageHandler --
// 9/29/2010 handled by AbstractAtdl4jCompositePanel		
//		if ( ( getAtdl4jOptions() != null ) && 
//			  ( getAtdl4jOptions().getAtdl4jUserMessageHandler() != null ) && 
//			  ( getAtdl4jOptions().getAtdl4jUserMessageHandler().isInitReqd() ) )
//		{
//			getAtdl4jOptions().initAtdl4jUserMessageHandler( aParentOrShell );
//		}

		// -- ** Construct the core GUI component ** --
//		setAtdl4jTesterPanel( getAtdl4jOptions().getAtdl4jTesterPanel() );
		setAtdl4jTesterPanel( getAtdl4jTesterPanel() );
	}

	/**
	 * @return the atdl4jOptions
	 */
	public static Atdl4jOptions getAtdl4jOptions()
	{
		return atdl4jOptions;
	}

	/**
	 * @param aAtdl4jOptions the atdl4jOptions to set
	 */
	private void setAtdl4jOptions(Atdl4jOptions aAtdl4jOptions)
	{
		atdl4jOptions = aAtdl4jOptions;
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
//	public Atdl4jTesterPanel getAtdl4jTesterPanel()
//	{
//		return this.atdl4jTesterPanel;
//	}


	/**
	 * @param aAtdl4jTesterPanel the atdl4jTesterPanel to set
	 */
	private void setAtdl4jTesterPanel(Atdl4jTesterPanel aAtdl4jTesterPanel)
	{
		this.atdl4jTesterPanel = aAtdl4jTesterPanel;
	}

	/**
	 * @return the Atdl4jTesterPanel
	 */
	public Atdl4jTesterPanel getAtdl4jTesterPanel() 
	{
		if ( ( atdl4jTesterPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameAtdl4jTesterPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jTesterPanel();
			logger.debug( "getAtdl4jTesterPanel() loading class named: " + tempClassName );
			try
			{
				atdl4jTesterPanel = ((Class<Atdl4jTesterPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jTesterPanel;
	}	
}
