package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.ui.StrategyUI;

/**
 * Represents the base, non-GUI system-specific "TesterApp" core GUI component (without a main() line).
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractAtdl4jTesterPanel
	implements Atdl4jTesterPanel,
			Atdl4jCompositePanelListener,
			FixMsgLoadPanelListener,
			Atdl4jInputAndFilterDataPanelListener
{ 

	Atdl4jConfig atdl4jConfig;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private Atdl4jInputAndFilterDataSelectionPanel atdl4jInputAndFilterDataSelectionPanel;
	
	private FixMsgLoadPanel fixMsgLoadPanel;
	
	private Atdl4jCompositePanel atdl4jCompositePanel;

	protected void init( Object aParentOrShell, Atdl4jConfig aAtdl4jConfig )
	{
		setAtdl4jConfig( aAtdl4jConfig );
		setParentOrShell( aParentOrShell );
		
		// -- Init the Atdl4jUserMessageHandler --
		if ( ( getAtdl4jConfig() != null ) && 
			  ( getAtdl4jConfig().getAtdl4jUserMessageHandler() != null ) && 
			  ( getAtdl4jConfig().getAtdl4jUserMessageHandler().isInitReqd() ) )
		{
			getAtdl4jConfig().initAtdl4jUserMessageHandler( aParentOrShell );
		}

		// -- Atdl4jInputAndFilterDataSelectionPanel (Input And Filter Data button/text field) - build() method called via concrete class --
		setAtdl4jInputAndFilterDataSelectionPanel( getAtdl4jConfig().getAtdl4jInputAndFilterDataSelectionPanel() );
		getAtdl4jInputAndFilterDataSelectionPanel().addListener( this );
		
		// -- FixMsgLoadPanel (Load Message button/text field) - build() method called via concrete class --
		setFixMsgLoadPanel( getAtdl4jConfig().getFixMsgLoadPanel() );
		getFixMsgLoadPanel().addListener( this );
		
		// -- Init the Atdl4jCompositePanel --
		setAtdl4jCompositePanel( getAtdl4jConfig().getAtdl4jCompositePanel() );
		getAtdl4jCompositePanel().addListener( this );
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return this.atdl4jConfig;
	}

	/**
	 * @param aAtdl4jConfig the atdl4jConfig to set
	 */
	private void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		this.atdl4jConfig = aAtdl4jConfig;
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
	 * @return the atdl4jCompositePanel
	 */
	public Atdl4jCompositePanel getAtdl4jCompositePanel()
	{
		return this.atdl4jCompositePanel;
	}

	/**
	 * @param aAtdl4jCompositePanel the atdl4jCompositePanel to set
	 */
	private void setAtdl4jCompositePanel(Atdl4jCompositePanel aAtdl4jCompositePanel)
	{
		this.atdl4jCompositePanel = aAtdl4jCompositePanel;
	}

	/**
	 * @return the atdl4jInputAndFilterDataSelectionPanel
	 */
	public Atdl4jInputAndFilterDataSelectionPanel getAtdl4jInputAndFilterDataSelectionPanel()
	{
		return this.atdl4jInputAndFilterDataSelectionPanel;
	}

	/**
	 * @param atdl4jInputAndFilterDataSelectionPanel the atdl4jInputAndFilterDataSelectionPanel to set
	 */
	private void setAtdl4jInputAndFilterDataSelectionPanel(Atdl4jInputAndFilterDataSelectionPanel aAtdl4jInputAndFilterDataSelectionPanel)
	{
		this.atdl4jInputAndFilterDataSelectionPanel = aAtdl4jInputAndFilterDataSelectionPanel;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixMsgLoadPanelListener#fixMsgLoadSelected(java.lang.String)
	 */
	@Override
	public void fixMsgLoadSelected(String aFixMsg)
	{
		if ( getAtdl4jCompositePanel() != null )
		{
			if ( ( aFixMsg == null ) || ( "".equals( aFixMsg ) ) )
			{
				getAtdl4jConfig().getAtdl4jUserMessageHandler().displayMessage( "Error", "No Fix Message provided to load.");
				return;
			}
			
			getAtdl4jCompositePanel().loadFixMessage( aFixMsg );
		} 
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanelListener#inputAndFilterDataSpecified(org.atdl4j.config.InputAndFilterData)
	 */
	@Override
	public void inputAndFilterDataSpecified(InputAndFilterData aInputAndFilterData)
	{
		try
		{
			// -- Reloads the screen for the pre-loaded/cached FIXatdl file (if specified and cached) --
			getAtdl4jCompositePanel().loadScreenWithFilteredStrategies();
		}
		catch (Throwable e)
		{
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Error", "ERROR during loadScreenWithFilteredStrategies()", e );
			return;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jCompositePanelListener#cancelButtonSelected()
	 */
	@Override
	public void cancelButtonSelected()
	{
		closePanel();
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jCompositePanelListener#okButtonSelected()
	 */
	@Override
	public void okButtonSelected()
	{
		if ( getAtdl4jConfig().getSelectedStrategy() != null )
		{
			try
			{
// 6/23/2010 Scott Atwell				StrategyUI ui = getAtdl4jConfig().getStrategyUIMap().get( getAtdl4jConfig().getSelectedStrategy() );
				StrategyUI ui = getAtdl4jConfig().getStrategyUI( getAtdl4jConfig().getSelectedStrategy() );
				ui.validate();
				String tempFixMsgFragment = ui.getFIXMessage();

				getAtdl4jConfig().getAtdl4jUserMessageHandler().displayMessage( "Strategy Selected", 
						"Strategy selected: " + Atdl4jHelper.getStrategyUiRepOrName( getAtdl4jConfig().getSelectedStrategy() ) 
						+ "\nFIX msg: " + tempFixMsgFragment );
				
				closePanel();
			}
			catch ( Throwable e )
			{
				getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Validation/FIX Message Extraction Error", 
						"Error during Validation/FIX Message extraction.", e );
			}
		}
		else
		{
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayMessage( "Select Strategy", "Please select a Strategy" );
		}
	}

	/**
	 * @param fixMsgLoadPanel the fixMsgLoadPanel to set
	 */
	private void setFixMsgLoadPanel(FixMsgLoadPanel fixMsgLoadPanel)
	{
		this.fixMsgLoadPanel = fixMsgLoadPanel;
	}

	/**
	 * @return the fixMsgLoadPanel
	 */
	public FixMsgLoadPanel getFixMsgLoadPanel()
	{
		return fixMsgLoadPanel;
	}
	
}
