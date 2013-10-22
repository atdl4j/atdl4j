package org.atdl4j.ui.app.impl;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jCompositePanelListener;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanelListener;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataSelectionPanel;
import org.atdl4j.ui.app.Atdl4jTesterPanel;
import org.atdl4j.ui.app.Atdl4jTesterPanelListener;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.FixMsgLoadPanel;
import org.atdl4j.ui.app.FixMsgLoadPanelListener;
import org.atdl4j.ui.app.FixatdlFileSelectionPanel;
import org.atdl4j.ui.app.FixatdlFileSelectionPanelListener;
import org.atdl4j.ui.app.StrategyEventListener;
import org.atdl4j.ui.impl.SelectedStrategyDetails;

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
			Atdl4jInputAndFilterDataPanelListener,
			FixatdlFileSelectionPanelListener,
			StrategyEventListener
{ 
	private final Logger logger = Logger.getLogger(AbstractAtdl4jTesterPanel.class);
	
	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, JDialog, etc
	
	private Atdl4jInputAndFilterDataSelectionPanel atdl4jInputAndFilterDataSelectionPanel;
	
	private FixatdlFileSelectionPanel fixatdlFileSelectionPanel;
	private FixMsgLoadPanel fixMsgLoadPanel;

	abstract protected Object createValidateOutputSection();
	abstract protected void setValidateOutputText(String aText);

	private Atdl4jCompositePanel atdl4jCompositePanel;
	abstract public void setVisibleValidateOutputSection( boolean aVisible );
	abstract public void setVisibleFileSelectionSection( boolean aVisible );
	abstract public void setVisibleTestingInputSection( boolean aVisible );
	private List<Atdl4jTesterPanelListener> listenerList = new Vector<Atdl4jTesterPanelListener>();

	
	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions )
	{
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		// -- Atdl4jInputAndFilterDataSelectionPanel (Input And Filter Data button/text field) - build() method called via concrete class --
		setAtdl4jInputAndFilterDataSelectionPanel( getAtdl4jInputAndFilterDataSelectionPanel() );
		getAtdl4jInputAndFilterDataSelectionPanel().addListener( this );
		
		// -- FixMsgLoadPanel (Load Message button/text field) - build() method called via concrete class --
		setFixMsgLoadPanel( getFixMsgLoadPanel() );
		getFixMsgLoadPanel().addListener( this );
		
		// -- FixatdlFileSelectionPanel (filename / file dialog) - build() method called via concrete class --
		setFixatdlFileSelectionPanel( getFixatdlFileSelectionPanel() );
		getFixatdlFileSelectionPanel().addListener( this );
		
		// -- Init the Atdl4jCompositePanel --
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
	 * @param aAtdl4jCompositePanel the atdl4jCompositePanel to set
	 */
	private void setAtdl4jCompositePanel(Atdl4jCompositePanel aAtdl4jCompositePanel)
	{
		this.atdl4jCompositePanel = aAtdl4jCompositePanel;
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
			setVisibleFileSelectionSection( Atdl4jConfig.getConfig().isShowFileSelectionSection() );
			setVisibleValidateOutputSection( Atdl4jConfig.getConfig().isShowValidateOutputSection() );
			getAtdl4jCompositePanel().setVisibleOkCancelButtonSection( Atdl4jConfig.getConfig().isShowCompositePanelOkCancelButtonSection() );
			
			// -- Reloads the screen for the pre-loaded/cached FIXatdl file (if specified and cached) --
			getAtdl4jCompositePanel().loadScreenWithFilteredStrategies();
		}
		catch (FIXatdlFormatException ex) {
		    	logger.error( "FIXatdlFormatException occured while selecting inputAndFilterData");
			    getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayException( "Error", "ERROR during loadScreenWithFilteredStrategies()", ex );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jCompositePanelListener#cancelButtonSelected()
	 */
	@Override
	public void cancelButtonSelected()
	{
		fireCancelButtonSelectedEvent();
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jCompositePanelListener#okButtonSelected()
	 */
	@Override
	public void okButtonSelected()
	{
		fireOkButtonSelectedEvent();
	}

	/**
	 * @param fixMsgLoadPanel the fixMsgLoadPanel to set
	 */
	private void setFixMsgLoadPanel(FixMsgLoadPanel fixMsgLoadPanel)
	{
		this.fixMsgLoadPanel = fixMsgLoadPanel;
	}

	/**
	 * @return the Atdl4jInputAndFilterDataSelectionPanel
	 * @throws Atdl4jClassLoadException 
	 */
	public Atdl4jInputAndFilterDataSelectionPanel getAtdl4jInputAndFilterDataSelectionPanel() 
	{
		if ( atdl4jInputAndFilterDataSelectionPanel == null )
		{
		    atdl4jInputAndFilterDataSelectionPanel = Atdl4jConfig.getConfig().createAtdl4jInputAndFilterDataSelectionPanel();
		}
		
		return atdl4jInputAndFilterDataSelectionPanel;
	}
	
	public Atdl4jCompositePanel getAtdl4jCompositePanel() 
	{
		if ( atdl4jCompositePanel == null )
		{
			atdl4jCompositePanel = Atdl4jConfig.getConfig().createAtdl4jCompositePanel();
			atdl4jCompositePanel.setStrategyEventListener( this );
			// -- Default to hiding Composite's OK/Cancel buttons to avoid two sets (use Atdl4jTesterPanel's only) --
			Atdl4jConfig.getConfig().setShowCompositePanelOkCancelButtonSection( false );
			atdl4jCompositePanel.setVisibleOkCancelButtonSection( false );
		}		
		return atdl4jCompositePanel;
	}
	
	/**
	 * @return the FixMsgLoadPanel
	 * @throws Atdl4jClassLoadException 
	 */
	public FixMsgLoadPanel getFixMsgLoadPanel() 
	{
		if ( fixMsgLoadPanel == null )
		{
		    fixMsgLoadPanel = Atdl4jConfig.getConfig().createFixMsgLoadPanel();
		}		
		return fixMsgLoadPanel;
	}

	/**
	 * @param fixatdlFileSelectionPanel the fixatdlFileSelectionPanel to set
	 */
	protected void setFixatdlFileSelectionPanel(FixatdlFileSelectionPanel fixatdlFileSelectionPanel)
	{
		this.fixatdlFileSelectionPanel = fixatdlFileSelectionPanel;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanelListener#fixatdlFileSelected(java.lang.String)
	 */
	@Override
	public void fixatdlFileSelected(String aFilename) throws FIXatdlFormatException
	{
		if (Atdl4jConfig.getConfig().isCatchAllStrategyLoadExceptions())
		{
		    	try {
			    fixatdlFileSelectedNotCatchAllExceptions(aFilename);
			} catch (FIXatdlFormatException ex) {
				logger.warn( "Atdl4jClassLoadException while loading FIXatdl file", ex );
				getAtdl4jUserMessageHandler().displayException( "FIXatdl file load exception", "", ex );
			}
		} else {
			fixatdlFileSelectedNotCatchAllExceptions(aFilename);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanelListener#fixatdlFileSelected(java.lang.String)
	 */
	protected void fixatdlFileSelectedNotCatchAllExceptions(String aFilename)
		throws FIXatdlFormatException
	{
		getAtdl4jCompositePanel().parseFixatdlFile( aFilename );
		getAtdl4jCompositePanel().loadScreenWithFilteredStrategies();
	}

	/**
	 * @return the FixatdlFileSelectionPanel
	 * @throws Atdl4jClassLoadException 
	 */
	public FixatdlFileSelectionPanel getFixatdlFileSelectionPanel() 
	{
		if ( fixatdlFileSelectionPanel == null )
		{
		    fixatdlFileSelectionPanel = Atdl4jConfig.getConfig().createFixatdlFileSelectionPanel();
		}		
		return fixatdlFileSelectionPanel;
	}
	
	/**
	 * @return the Atdl4jUserMessageHandler
	 * @throws Atdl4jClassLoadException 
	 */
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler() 
	{
		return getAtdl4jCompositePanel().getAtdl4jUserMessageHandler();
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategyEventListener#strategyNotValidated(org.atdl4j.fixatdl.core.StrategyT, java.lang.String)
	 */
	@Override
	public void strategyNotValidated(StrategyT aStrategy, String aMessageText)
	{
		setValidateOutputText( aMessageText );
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategyEventListener#strategySelected(org.atdl4j.fixatdl.core.StrategyT)
	 */
	@Override
	public void strategySelected(StrategyT aStrategy, boolean aSelectedViaLoadFixMsg)
	{
		if ( aSelectedViaLoadFixMsg )
		{
			setValidateOutputText( "FIX string loaded successfully!" );
		}
		else
		{
			setValidateOutputText( "" );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategyEventListener#strategyValidated(org.atdl4j.fixatdl.core.StrategyT)
	 */
	@Override
	public void strategyValidated(StrategyT aStrategy, SelectedStrategyDetails aSelectedStrategyDetails)
	{
		setValidateOutputText( aSelectedStrategyDetails.getFixMsgFragment() );
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategyEventListener#strategyValidationFailed(org.atdl4j.fixatdl.core.StrategyT, java.lang.Throwable)
	 */
	@Override
	public void strategyValidationFailed(StrategyT aStrategy, Throwable aException)
	{
		setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( aException ));
	}
	
	public void addListener( Atdl4jTesterPanelListener aAtdl4jTesterPanelListener )
	{
		listenerList.add( aAtdl4jTesterPanelListener );
	}

	public void removeListener( Atdl4jTesterPanelListener aAtdl4jTesterPanelListener )
	{
		listenerList.remove( aAtdl4jTesterPanelListener );
	}	
	
	protected void fireOkButtonSelectedEvent()
	{
            	for ( Atdl4jTesterPanelListener tempListener : listenerList )
            	{
        		tempListener.okButtonSelected();
        	}
	}	
	
	protected void fireCancelButtonSelectedEvent()
	{
		for ( Atdl4jTesterPanelListener tempListener : listenerList )
		{
			tempListener.cancelButtonSelected();
		}
	}	
	
	protected void validateButtonSelected() throws ValidationException
	{
		getAtdl4jCompositePanel().validateStrategy();
	}
}
