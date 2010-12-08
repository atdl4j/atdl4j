package org.atdl4j.ui.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jTesterPanel;
import org.atdl4j.ui.app.Atdl4jTesterPanelListener;
import org.atdl4j.ui.impl.SelectedStrategyDetails;

/**
 * Represents the base, non-GUI system-specific "TesterApp" with a main() line.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractAtdl4jTesterApp
	implements Atdl4jTesterPanelListener
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
	

	protected void init( String[] args, Atdl4jConfiguration aAtdl4jConfiguration, Atdl4jOptions aAtdl4jOptions, Object aParentOrShell )
	{
		Atdl4jConfig.setConfig( aAtdl4jConfiguration );
		
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		parseMainLineArgs( args );
		
		// -- ** Construct the core GUI component ** --
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
		if ( atdl4jTesterPanel == null )
		{
			atdl4jTesterPanel = Atdl4jConfig.createAtdl4jTesterPanel();
			atdl4jTesterPanel.addListener( this );
		}
		
		return atdl4jTesterPanel;
	}	
	
	public void okButtonSelected()
	{
		if ( getAtdl4jTesterPanel().getAtdl4jCompositePanel().getSelectedStrategy() != null )
		{
			try
			{
				// -- (aPerformValidationFlag = true) --
				SelectedStrategyDetails tempSelectedStrategyDetails = getAtdl4jTesterPanel().getAtdl4jCompositePanel().getSelectedStrategyDetails( true );
				String tempFixMsgFragment = tempSelectedStrategyDetails.getFixMsgFragment();

				getAtdl4jTesterPanel().getAtdl4jUserMessageHandler().displayMessage( "Strategy Selected", 
						"Strategy selected: " + tempSelectedStrategyDetails.getSelectedStrategyUiRepOrName() 
						+ "\nFIX msg: " + tempFixMsgFragment );
				
				getAtdl4jTesterPanel().closePanel();
			}
			catch ( Throwable e )
			{
				getAtdl4jTesterPanel().getAtdl4jUserMessageHandler().displayException( "Validation/FIX Message Extraction Error", 
						"Error during Validation/FIX Message extraction.", e );
			}
		}
		else
		{
			getAtdl4jTesterPanel().getAtdl4jUserMessageHandler().displayMessage( "Select Strategy", "Please select a Strategy" );
		}
	}


	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jTesterPanelListener#cancelButtonSelected()
	 */
	@Override
	public void cancelButtonSelected()
	{
		getAtdl4jTesterPanel().closePanel();
	}
}

