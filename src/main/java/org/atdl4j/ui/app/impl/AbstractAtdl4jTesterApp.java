package org.atdl4j.ui.app.impl;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.ValidationException;
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
	static Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, JDialog, etc
	
	Atdl4jTesterPanel atdl4jTesterPanel;

	protected void init( String[] args, Atdl4jConfiguration aAtdl4jConfiguration, Atdl4jOptions aAtdl4jOptions, Object aParentOrShell )
	{
		Atdl4jConfig.setConfig( aAtdl4jConfiguration );
		
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
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
	private static void setAtdl4jOptions(Atdl4jOptions aAtdl4jOptions)
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
			atdl4jTesterPanel = Atdl4jConfig.getConfig().createAtdl4jTesterPanel();
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
			} catch (ValidationException ex) {
				getAtdl4jTesterPanel().getAtdl4jUserMessageHandler().displayException( "Validation/FIX Message Extraction Error", 
				"Error during Validation/FIX Message extraction.", ex );
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

