package org.atdl4j.ui.app;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
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
	private final Logger logger = Logger.getLogger(AbstractAtdl4jTesterPanel.class);
	
	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private Atdl4jInputAndFilterDataSelectionPanel atdl4jInputAndFilterDataSelectionPanel;
	
	private FixMsgLoadPanel fixMsgLoadPanel;
	
	private Atdl4jCompositePanel atdl4jCompositePanel;

	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions )
	{
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		// -- Init the Atdl4jUserMessageHandler --
// 9/29/2010 handled by AbstractAtdl4jCompositePanel		
//		if ( ( getAtdl4jOptions() != null ) && 
//			  ( getAtdl4jOptions().getAtdl4jUserMessageHandler() != null ) && 
//			  ( getAtdl4jOptions().getAtdl4jUserMessageHandler().isInitReqd() ) )
//		{
//			getAtdl4jOptions().initAtdl4jUserMessageHandler( aParentOrShell );
//		}

		// -- Atdl4jInputAndFilterDataSelectionPanel (Input And Filter Data button/text field) - build() method called via concrete class --
//		setAtdl4jInputAndFilterDataSelectionPanel( getAtdl4jOptions().getAtdl4jInputAndFilterDataSelectionPanel() );
		setAtdl4jInputAndFilterDataSelectionPanel( getAtdl4jInputAndFilterDataSelectionPanel() );
		getAtdl4jInputAndFilterDataSelectionPanel().addListener( this );
		
		// -- FixMsgLoadPanel (Load Message button/text field) - build() method called via concrete class --
//		setFixMsgLoadPanel( getAtdl4jOptions().getFixMsgLoadPanel() );
		setFixMsgLoadPanel( getFixMsgLoadPanel() );
		getFixMsgLoadPanel().addListener( this );
		
		// -- Init the Atdl4jCompositePanel --
//		setAtdl4jCompositePanel( getAtdl4jOptions().getAtdl4jCompositePanel() );
		setAtdl4jCompositePanel( getAtdl4jCompositePanel() );
		getAtdl4jCompositePanel().addListener( this );
	}

	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return this.atdl4jOptions;
	}

	/**
	 * @param aAtdl4jOptions the atdl4jOptions to set
	 */
	private void setAtdl4jOptions(Atdl4jOptions aAtdl4jOptions)
	{
		this.atdl4jOptions = aAtdl4jOptions;
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
//	public Atdl4jCompositePanel getAtdl4jCompositePanel()
//	{
//		return this.atdl4jCompositePanel;
//	}

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
//	public Atdl4jInputAndFilterDataSelectionPanel getAtdl4jInputAndFilterDataSelectionPanel()
//	{
//		return this.atdl4jInputAndFilterDataSelectionPanel;
//	}

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
//				getAtdl4jOptions().getAtdl4jUserMessageHandler().displayMessage( "Error", "No Fix Message provided to load.");
				getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayMessage( "Error", "No Fix Message provided to load.");
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
			getAtdl4jCompositePanel().setVisibleFileSelectionSection( getAtdl4jOptions().isShowFileSelectionSection() );
			getAtdl4jCompositePanel().setVisibleValidateOutputSection( getAtdl4jOptions().isShowValidateOutputSection() );
			getAtdl4jCompositePanel().setVisibleOkCancelButtonSection( getAtdl4jOptions().isShowCompositePanelOkCancelButtonSection() );
			
			// -- Reloads the screen for the pre-loaded/cached FIXatdl file (if specified and cached) --
			getAtdl4jCompositePanel().loadScreenWithFilteredStrategies();
		}
		catch (Throwable e)
		{
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "Error", "ERROR during loadScreenWithFilteredStrategies()", e );
			getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayException( "Error", "ERROR during loadScreenWithFilteredStrategies()", e );
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
// 9/27/2010 Scott Atwell		if ( getAtdl4jOptions().getSelectedStrategy() != null )
		if ( getAtdl4jCompositePanel().getSelectedStrategy() != null )
		{
			try
			{
// 6/23/2010 Scott Atwell				StrategyUI ui = getAtdl4jOptions().getStrategyUIMap().get( getAtdl4jOptions().getSelectedStrategy() );
// 9/29/2010 Scott Atwell				StrategyUI ui = getAtdl4jOptions().getStrategyUI( getAtdl4jCompositePanel().getSelectedStrategy() );
				StrategyUI ui = getAtdl4jCompositePanel().getStrategiesUI().getStrategyUI( getAtdl4jCompositePanel().getSelectedStrategy() );
				ui.validate();
				String tempFixMsgFragment = ui.getFIXMessage();

//				getAtdl4jOptions().getAtdl4jUserMessageHandler().displayMessage( "Strategy Selected", 
				getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayMessage( "Strategy Selected", 
						"Strategy selected: " + Atdl4jHelper.getStrategyUiRepOrName( getAtdl4jCompositePanel().getSelectedStrategy() ) 
						+ "\nFIX msg: " + tempFixMsgFragment );
				
				closePanel();
			}
			catch ( Throwable e )
			{
//				getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "Validation/FIX Message Extraction Error", 
				getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayException( "Validation/FIX Message Extraction Error", 
						"Error during Validation/FIX Message extraction.", e );
			}
		}
		else
		{
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayMessage( "Select Strategy", "Please select a Strategy" );
			getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayMessage( "Select Strategy", "Please select a Strategy" );
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
//	public FixMsgLoadPanel getFixMsgLoadPanel()
//	{
//		return fixMsgLoadPanel;
//	}

	
	/**
	 * @return the Atdl4jInputAndFilterDataSelectionPanel
	 */
	public Atdl4jInputAndFilterDataSelectionPanel getAtdl4jInputAndFilterDataSelectionPanel() 
	{
		if ( ( atdl4jInputAndFilterDataSelectionPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameAtdl4jInputAndFilterDataSelectionPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jInputAndFilterDataSelectionPanel();
			logger.debug( "getAtdl4jInputAndFilterDataSelectionPanel() loading class named: " + tempClassName );
			try
			{
				atdl4jInputAndFilterDataSelectionPanel = ((Class<Atdl4jInputAndFilterDataSelectionPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jInputAndFilterDataSelectionPanel;
	}
	
	public Atdl4jCompositePanel getAtdl4jCompositePanel() 
	{
//		if ( ( atdl4jCompositePanel == null ) && ( Atdl4jConfig.getConfig().getClassNameAtdl4jCompositePanel() != null ) )
//		{
//			String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jCompositePanel();
//			logger.debug( "getAtdl4jCompositePanel() loading class named: " + tempClassName );
//			try
//			{
//				atdl4jCompositePanel = ((Class<Atdl4jCompositePanel>) Class.forName( tempClassName ) ).newInstance();
//			}
//			catch ( Exception e )
//			{
//				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
//				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
//			}
//		}
		if ( atdl4jCompositePanel == null )
		{
			atdl4jCompositePanel = Atdl4jConfig.createAtdl4jCompositePanel();
		}
		
		return atdl4jCompositePanel;
	}
	
	/**
	 * @return the FixMsgLoadPanel
	 */
	public FixMsgLoadPanel getFixMsgLoadPanel() 
	{
		if ( ( fixMsgLoadPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameFixMsgLoadPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameFixMsgLoadPanel();
			logger.debug( "getFixMsgLoadPanel() loading class named: " + tempClassName );
			try
			{
				fixMsgLoadPanel = ((Class<FixMsgLoadPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return fixMsgLoadPanel;
	}

}
