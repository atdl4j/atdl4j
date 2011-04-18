package org.atdl4j.ui.app.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.fix.FIXMessageParser;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jCompositePanelListener;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.StrategyDescriptionPanel;
import org.atdl4j.ui.app.StrategyEventListener;
import org.atdl4j.ui.app.StrategySelectionPanel;
import org.atdl4j.ui.app.StrategySelectionPanelListener;
import org.atdl4j.ui.impl.SelectedStrategyDetails;

/**
 * Represents the base, non-GUI system-specific CORE strategy selection and display GUI component.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractAtdl4jCompositePanel
	implements Atdl4jCompositePanel, 
	StrategySelectionPanelListener
{
	public final Logger logger = Logger.getLogger(AbstractAtdl4jCompositePanel.class);

	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private List<Atdl4jCompositePanelListener> listenerList = new Vector<Atdl4jCompositePanelListener>();

	private String lastFixatdlFilename;
	
	private StrategySelectionPanel strategySelectionPanel;
	private StrategyDescriptionPanel strategyDescriptionPanel;
	private StrategiesUI strategiesUI;
	
	private StrategiesT strategies; 
	private StrategyT selectedStrategy;
	private boolean selectedStrategyValidated = false;

	private Atdl4jUserMessageHandler atdl4jUserMessageHandler;
	
	private StrategyEventListener strategyEventListener;

	abstract public void setVisibleOkCancelButtonSection( boolean aVisible );
	abstract protected void packLayout();

// 12/8/2010 Scott Atwell added
	List<StrategyT> lastFilteredStrategyList = null;

	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions )
	{
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		// -- Init InputAndFilterData if null --
		if ( getAtdl4jOptions().getInputAndFilterData() == null )
		{
			getAtdl4jOptions().setInputAndFilterData(  new InputAndFilterData() );
			getAtdl4jOptions().getInputAndFilterData().init();
		}

		// -- Init the Atdl4jUserMessageHandler --
		if ( ( getAtdl4jUserMessageHandler() != null ) && 
			  ( getAtdl4jUserMessageHandler().isInitReqd() ) )
		{
			initAtdl4jUserMessageHandler( aParentOrShell );
		}
	
		// ----- Setup internal components (the GUI-specific versions will be instantiated, and add listeners, but defer to concrete classes: "build____Panel()" ----

		// -- StrategySelectionPanel (drop down with list of strategies to choose from) - build() method called via concrete class --
		setStrategySelectionPanel( getStrategySelectionPanel() );
		getStrategySelectionPanel().addListener( this );

		// -- StrategyDescriptionPanel (drop down with list of strategies to choose from) - build() method called via concrete class --
		setStrategyDescriptionPanel( getStrategyDescriptionPanel() );

		// -- StrategiesPanel (GUI display of each strategy's parameters) - build() method called via concrete class --
		setStrategiesUI( getStrategiesUI() );
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
	 * @param strategySelectionPanel the strategySelectionPanel to set
	 */
	protected void setStrategySelectionPanel(StrategySelectionPanel strategySelectionPanel)
	{
		this.strategySelectionPanel = strategySelectionPanel;
	}

	/**
	 * @param strategyDescriptionPanel the strategyDescriptionPanel to set
	 */
	protected void setStrategyDescriptionPanel(StrategyDescriptionPanel strategyDescriptionPanel)
	{
		this.strategyDescriptionPanel = strategyDescriptionPanel;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategySelectionPanelListener#strategySelected(org.atdl4j.fixatdl.core.StrategyT, int)
	 */
	@Override
	public void strategySelected(StrategyT aStrategy)
	{
		setSelectedStrategy( aStrategy );
		setSelectedStrategyValidated( false );
		getStrategyDescriptionPanel().loadStrategyDescriptionVisible( aStrategy );
		getStrategiesUI().adjustLayoutForSelectedStrategy( aStrategy );

		packLayout();
		getStrategyDescriptionPanel().loadStrategyDescriptionText( aStrategy );
	
		// -- Notify StrategyEventListener (eg Atdl4jTesterPanel), aSelectedViaLoadFixMsg=false --
		fireStrategyEventListenerStrategySelected( aStrategy, false );
	}

	/**
	 * @param strategiesUI the strategiesUI to set
	 */
	protected void setStrategiesUI(StrategiesUI strategiesUI)
	{
		this.strategiesUI = strategiesUI;
	}

	/* 
	 * @return StrategyT (non-null only if passes all validation)
	 */
	public StrategyT validateStrategy() throws ValidationException 
	{
				
		if ( Atdl4jConfig.getConfig().isCatchAllValidationExceptions() )
		{
			try 
			{
				return validateStrategyWithoutCatchingAllExceptions();
			}
			catch (ValidationException ex) 
			{
				getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
				logger.info( "Validation Exception:", ex );
				
				// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
				fireStrategyEventListenerStrategyValidationFailed( getSelectedStrategy(), ex );
				return null;
			}
		} 
		else 
		{
			return validateStrategyWithoutCatchingAllExceptions();
		}
	}	
	
	public StrategyT validateStrategyWithoutCatchingAllExceptions() throws ValidationException 
	{
		StrategyT tempSelectedStrategy = getSelectedStrategy();
		
		if (tempSelectedStrategy == null)
		{
			// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
			fireStrategyEventListenerStrategyNotValidated( null, "Please select a strategy" );
			return null;
		}
		
		setSelectedStrategyValidated( false );
		
		logger.info("Validating strategy " + tempSelectedStrategy.getName());
		
		// -- (aPerformValidationFlag = true) --
		SelectedStrategyDetails tempSelectedStrategyDetails = getSelectedStrategyDetails( true );
		String tempUiFixMsg = tempSelectedStrategyDetails.getFixMsgFragment();
		setSelectedStrategyValidated( true );
		logger.info("Successfully Validated strategy " + tempSelectedStrategy.getName() + " FIXMessage: " + tempUiFixMsg );
				
		// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
		fireStrategyEventListenerStrategyValidated( tempSelectedStrategy, tempSelectedStrategyDetails );
		return tempSelectedStrategy;
	}
	
	public SelectedStrategyDetails getSelectedStrategyDetails( boolean aPerformValidationFlag ) 
		throws ValidationException
	{
		StrategyT tempSelectedStrategy = getSelectedStrategy();
		
		if (tempSelectedStrategy == null)
		{
			return null;
		}
		
		// -- (aReinitPanelFlag=false) --
		StrategyUI tempStrategyUI = getStrategiesUI().getStrategyUI(tempSelectedStrategy, false);
		
		if ( aPerformValidationFlag )
		{
			setSelectedStrategyValidated( false );
			tempStrategyUI.validate();
			setSelectedStrategyValidated( true );
		}
		
		return new SelectedStrategyDetails( getStrategies(), tempSelectedStrategy, tempStrategyUI );
	}
	
	/* 
	 * Parses the FIXatdl file aFilename into StrategiesT storing the result via Atdl4jOptions().setStrategies().
	 */
	public void parseFixatdlFile( String aFilename ) throws FIXatdlFormatException 
	{
		setLastFixatdlFilename( null );
		setLastFilteredStrategyList( null );
		setStrategies( null );
				
		// Create a JAXB unmarshaller object from StrategiesT class
		// TODO: Perhaps this should be done statically (e.g. once at app load time)
		Unmarshaller um;
		try {
		    JAXBContext jc = JAXBContext.newInstance(StrategiesT.class.getPackage().getName());
		    um = jc.createUnmarshaller();
		} catch (JAXBException e) {
		    // if the above fails, this is a critical exception
		    throw new RuntimeException(e);
		}

		JAXBElement<?> element;
        	try
        	{
        		// try to parse as URL
        		URL url = new URL( aFilename );
        		try
        		{
        		    element = (JAXBElement<?>) um.unmarshal(url);
        	       	} catch (JAXBException ex) {
        	       	    	throw new FIXatdlFormatException("Could not parse URL: " + aFilename);
        	       	}
        	} 
        	catch (MalformedURLException e) 
        	{
        		try
        		{
                		// try to parse as file
                		File file = new File( aFilename );
                		element = (JAXBElement<?>) um.unmarshal(file);	
        	       	} catch (JAXBException ex) {
        	       	    	throw new FIXatdlFormatException("Could not parse File: " + aFilename);
        	     	}
		}		
		validateParsedFixatdlFileContents( (StrategiesT) element.getValue() );
		setStrategies( (StrategiesT) element.getValue() );
		setLastFixatdlFilename( aFilename );
	}
	
	public void validateParsedFixatdlFileContents( StrategiesT aStrategies )
		throws FIXatdlFormatException
	{
		List<String> tempStrategyNameList = new ArrayList<String>();
		
		for ( StrategyT tempStrategy : aStrategies.getStrategy() )
		{
			if ( ! Atdl4jHelper.isStrategyNameValid( tempStrategy.getName() ) )
			{
				throw new FIXatdlFormatException("Strategy/@name SYNTAX ERROR: \"" + tempStrategy.getName() + "\" does not match FIXatdl schema pattern: \"" + Atdl4jConstants.PATTERN_STRATEGY_NAME + "\"" );
			}
			
			// -- Check for duplicate Strategy/@name values --
			if ( tempStrategyNameList.contains( tempStrategy.getName() ) )
			{
				throw new FIXatdlFormatException("DUPLICATE Strategy/@name ERROR: \"" + tempStrategy.getName() + "\" already exists." );
			}
			else
			{
				tempStrategyNameList.add( tempStrategy.getName() );
			}
		}
	}
	
	
	/**
	 * Can be invoked/re-invoked at anytime provided that parseFixatdlFile() has successfully parsed the
	 * FIXatdl file contents into Atdl4jOptions().setStrategies().  Re-generates the display.
	 * @throws Atdl4jClassLoadException 
	 * @throws FIXatdlFormatException 
	 */
	public void loadScreenWithFilteredStrategies()
	{
		// -- (aSelectStrategyName=null) --
		loadScreenWithFilteredStrategies( null );
	}
	/**
	 * Can be invoked/re-invoked at anytime provided that parseFixatdlFile() has successfully parsed the
	 * FIXatdl file contents into Atdl4jOptions().setStrategies().  Re-generates the display.
	 * @throws Atdl4jClassLoadException 
	 * @throws FIXatdlFormatException 
	 * @parm aSelectStrategyName
	 */
	public void loadScreenWithFilteredStrategies( String aSelectStrategyName )
	{
		// obtain filtered StrategyList
		List<StrategyT> tempFilteredStrategyList = getStrategiesFilteredStrategyList();
		
		if ( tempFilteredStrategyList == null )
		{
			if ( getStrategies() != null )
			{
				// -- Only generate the error message if Strategies have been parsed -- 
				getAtdl4jUserMessageHandler().displayMessage( "Unexpected Error", "Unexpected Error: getStrategiesFilteredStrategyList() was null." );
			}
			return;
		}

		// -- Optional, can control the order in which the strategies will appear in the list, and can be used to restrict the list to a subset --
		tempFilteredStrategyList = getAtdl4jOptions().getStrategyListUsingInputStrategyNameListFilter( tempFilteredStrategyList );

		if ( tempFilteredStrategyList == null )
		{
			if ( getStrategies() != null )
			{
				// -- Only generate the error message if Strategies have been parsed -- 
				getAtdl4jUserMessageHandler().displayMessage( "Unexpected Error", "Unexpected Error: getStrategyListUsingInputStrategyNameListFilter() was null." );
			}
			return;
		}

		// -- Avoid re-building the screen if the filtered StrategyList is unchanged --
		if ( ( getLastFilteredStrategyList() != null ) &&
			  ( isMatchingStrategyList( getLastFilteredStrategyList(), tempFilteredStrategyList ) ) )
		{
			if ( aSelectStrategyName != null )
			{
				getStrategySelectionPanel().selectDropDownStrategyByStrategyName( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
			}
			else
			{
				if ( ( getAtdl4jOptions().getInputAndFilterData() != null ) && 
						  ( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() != null ) )
				{
					getStrategySelectionPanel().selectDropDownStrategyByStrategyName( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
				}
				else
				{
					getStrategySelectionPanel().selectFirstDropDownStrategy();
				}
			}
		}
		else  // -- filtered StrategyList is new or has changed --
		{
			// -- Reduce screen re-draw/flash (doesn't really work for SWT, though) --
			getStrategiesUI().setVisible( false );
	
			// remove all strategy panels
			getStrategiesUI().removeAllStrategyPanels();
				
			// -- Always build StrategyPanels anew (can be time intensive) --
			try {
    				getStrategiesUI().createStrategyPanels( getStrategies(), tempFilteredStrategyList );	
        			getStrategySelectionPanel().loadStrategyList( tempFilteredStrategyList );
        			
        			if ( aSelectStrategyName != null )
        			{
        				getStrategySelectionPanel().selectDropDownStrategyByStrategyName( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
        			}
        			else
        			{
        				if ( ( getAtdl4jOptions().getInputAndFilterData() != null ) && 
        						  ( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() != null ) )
        				{
        					getStrategySelectionPanel().selectDropDownStrategyByStrategyName( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
        				}
        				else
        				{
        					getStrategySelectionPanel().selectFirstDropDownStrategy();
        				}
        			}
			} catch (FIXatdlFormatException ex) {
			    	logger.error( "FIXatdlFormatException occured while loading Strategies");
			    	getAtdl4jUserMessageHandler().displayException( "Error", "ERROR during loadScreenWithFilteredStrategies()", ex );
			}
			
			// -- Reduce screen re-draw/flash (doesn't really work for SWT, though) --
			getStrategiesUI().setVisible( true );
		}
		
		// -- Keep track of filtered StrategyList --
		setLastFilteredStrategyList( tempFilteredStrategyList );
	}
	
	public boolean loadFixMessage( String aFixMessage ) 
	{
		logger.info("Loading FIX string " + aFixMessage);
		
		if ( ( getAtdl4jOptions().getInputAndFilterData() != null ) &&
			  ( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() != null ) )
		{
			logger.info("getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName(): " + getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName());			
			logger.info("Invoking selectDropDownStrategy: " + getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );							
			getStrategySelectionPanel().selectDropDownStrategyByStrategyName( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
		}
		else  // Match getWireValue() and then use getUiRep() if avail, otherwise getName()
		{
			if ( ( getStrategies() != null ) && ( getStrategies().getStrategyIdentifierTag() != null ) )
			{
				String strategyWireValue = FIXMessageParser.extractFieldValueFromFIXMessage( aFixMessage, getStrategies().getStrategyIdentifierTag().intValue() );
				
				logger.info("strategyWireValue: " + strategyWireValue);			
				if ( strategyWireValue != null )
					{
					logger.info("Invoking selectDropDownStrategy for strategyWireValue: " + strategyWireValue );							
					getStrategySelectionPanel().selectDropDownStrategyByStrategyWireValue( strategyWireValue );
				}
				
			}
		}

		if (getSelectedStrategy() == null)
		{
			// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
			fireStrategyEventListenerStrategyNotValidated( null, "Please select a strategy" );
			return false;
		}
		
		// -- (aReinitPanelFlag=true) --
		StrategyUI ui = getStrategiesUI().getStrategyUI( getSelectedStrategy(), true );
		
		// -- Note available getStrategies() may be filtered due to SecurityTypes, Markets, or Region/Country rules --  
		if ( ui != null )
		{
			logger.info( "Invoking ui.setFIXMessage() for: " + ui.getStrategy().getName() + " with FIX Message: " + aFixMessage );
			ui.setFIXMessage(aFixMessage);
			logger.info( "FIX string loaded successfully!" );
			
			// -- Notify StrategyEventListener (eg Atdl4jTesterPanel), aSelectedViaInputFixMsg=true --
			fireStrategyEventListenerStrategySelected( getSelectedStrategy(), true );
			return true;
		}
		else
		{
			// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
			fireStrategyEventListenerStrategyNotValidated( getSelectedStrategy(), getSelectedStrategy().getName() + " is not available." );
			return false;
		}
//		catch (ValidationException ex) 
//		{
//			getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
//			logger.info( "Validation Exception:", ex );
//
//			// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
//			fireStrategyEventListenerStrategyValidationFailed( getSelectedStrategy(), ex );
//			return false;
//		}
//		catch (Exception ex) 
//		{
//			getAtdl4jUserMessageHandler().displayException( "Exception", "", ex );
//			logger.warn( "Generic Exception", ex );
//			
//			// -- Notify StrategyEventListener (eg Atdl4jTesterPanel) --
//			fireStrategyEventListenerStrategyNotValidated( getSelectedStrategy(), "" );
//			return false;
//		}

	}
	/**
	 * @param lastFixatdlFilename the lastFixatdlFilename to set
	 */
	protected void setLastFixatdlFilename(String lastFixatdlFilename)
	{
		this.lastFixatdlFilename = lastFixatdlFilename;
	}
	
	/**
	 * @return the lastFixatdlFilename
	 */
	public String getLastFixatdlFilename()
	{
		return lastFixatdlFilename;
	}
	
	protected void okButtonSelected()
	{
		fireOkButtonSelectedEvent();
	}
	
	protected void cancelButtonSelected()
	{
		fireCancelButtonSelectedEvent();
	}
	
	public void addListener( Atdl4jCompositePanelListener aAtdl4jCompositePanelListener )
	{
		listenerList.add( aAtdl4jCompositePanelListener );
	}

	public void removeListener( Atdl4jCompositePanelListener aAtdl4jCompositePanelListener )
	{
		listenerList.remove( aAtdl4jCompositePanelListener );
	}	
	
	protected void fireOkButtonSelectedEvent()
	{
		for ( Atdl4jCompositePanelListener tempListener : listenerList )
		{
			tempListener.okButtonSelected();
		}
	}	
	
	protected void fireCancelButtonSelectedEvent()
	{
		for ( Atdl4jCompositePanelListener tempListener : listenerList )
		{
			tempListener.cancelButtonSelected();
		}
	}
	/**
	 * @return the strategies
	 */
	public StrategiesT getStrategies()
	{
		return this.strategies;
	}
	/**
	 * @param aStrategies the strategies to set
	 */
	public void setStrategies(StrategiesT aStrategies)
	{
		this.strategies = aStrategies;
	}
	
	public List<StrategyT> getStrategiesFilteredStrategyList()
	{
		if ( ( getStrategies() == null ) || ( getStrategies().getStrategy() == null ) )
		{
			return null;
		}
		
		if ( ( getAtdl4jOptions() != null ) && ( getAtdl4jOptions().getInputAndFilterData() == null ) )
		{
			return getStrategies().getStrategy();
		}
		
		List<StrategyT> tempFilteredList = new ArrayList<StrategyT>();
		
		for ( StrategyT strategy : getStrategies().getStrategy() ) 
		{
			if ( ! getAtdl4jOptions().getInputAndFilterData().isStrategySupported( strategy ) )
			{
				logger.info("Excluding strategy: " + strategy.getName() + " as inputAndFilterData.isStrategySupported() returned false." );
				continue; // skip it 
			}
			
			tempFilteredList.add( strategy );
		}
		
		return tempFilteredList;
	}
	/**
	 * @param selectedStrategy the selectedStrategy to set
	 */
	public void setSelectedStrategy(StrategyT selectedStrategy)
	{
		this.selectedStrategy = selectedStrategy;
	}
	/**
	 * @return the selectedStrategy
	 */
	public StrategyT getSelectedStrategy()
	{
		return selectedStrategy;
	}
	/**
	 * @param selectedStrategyValidated the selectedStrategyValidated to set
	 */
	public void setSelectedStrategyValidated(boolean selectedStrategyValidated)
	{
		this.selectedStrategyValidated = selectedStrategyValidated;
	}
	/**
	 * @return the selectedStrategyValidated
	 */
	public boolean isSelectedStrategyValidated()
	{
		return selectedStrategyValidated;
	}	
	
	
	
	/**
	 * @return the StrategyDescriptionPanel
	 * @throws Atdl4jClassLoadException 
	 */
	public StrategyDescriptionPanel getStrategyDescriptionPanel() 
	{
		if ( strategyDescriptionPanel == null )
		{
		        strategyDescriptionPanel = Atdl4jConfig.getConfig().createStrategyDescriptionPanel();
		}		
		return strategyDescriptionPanel;
	}	
	
	/**
	 * @return the StrategySelectionPanel
	 * @throws Atdl4jClassLoadException 
	 */
	public StrategySelectionPanel getStrategySelectionPanel() 
	{
		if ( strategySelectionPanel == null )
		{
		    	strategySelectionPanel = Atdl4jConfig.getConfig().createStrategySelectionPanel();
		}
		return strategySelectionPanel;
	}

	/**
	 * @return
	 * @throws Atdl4jClassLoadException 
	 */
	public StrategiesUI getStrategiesUI()
	{
		if ( strategiesUI == null )
		{
		    strategiesUI = Atdl4jConfig.getConfig().createStrategiesUI();
		    strategiesUI.init( getAtdl4jOptions() );
		}
		return strategiesUI;
	}
	
	/**
	 * @return the Atdl4jUserMessageHandler
	 * @throws Atdl4jClassLoadException 
	 */
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler() 
	{
		if ( atdl4jUserMessageHandler == null )
		{
		    atdl4jUserMessageHandler = Atdl4jConfig.getConfig().createAtdl4jUserMessageHandler();
		}
		return atdl4jUserMessageHandler;
	}
	/**
	 * @param atdl4jUserMessageHandler the atdl4jUserMessageHandler to set
	 */
	public void setAtdl4jUserMessageHandler(Atdl4jUserMessageHandler atdl4jUserMessageHandler)
	{
		this.atdl4jUserMessageHandler = atdl4jUserMessageHandler;
	}
	
	/**
	 * @param parentOrShell
	 * @throws Atdl4jClassLoadException 
	 */
	public void initAtdl4jUserMessageHandler( Object parentOrShell )
	{
		getAtdl4jUserMessageHandler().init(  parentOrShell, getAtdl4jOptions() );
	}
	/**
	 * @return the strategyEventListener
	 */
	protected StrategyEventListener getStrategyEventListener()
	{
		return this.strategyEventListener;
	}
	/**
	 * @param aStrategyEventListener the strategyEventListener to set
	 */
	public void setStrategyEventListener(StrategyEventListener aStrategyEventListener)
	{
		this.strategyEventListener = aStrategyEventListener;
	}


	/**
	 * @param aStrategy
	 */
	public void fireStrategyEventListenerStrategySelected( StrategyT aStrategy, boolean aSelectedViaLoadFixMsg )
	{
		if ( getStrategyEventListener() != null )
		{
			getStrategyEventListener().strategySelected( aStrategy, aSelectedViaLoadFixMsg );
		}
	}
	
	/**
	 * @param aStrategy
	 * @param aSelectedStrategyDetails
	 */
	public void fireStrategyEventListenerStrategyValidated( StrategyT aStrategy, SelectedStrategyDetails aSelectedStrategyDetails  )
	{
		if ( getStrategyEventListener() != null )
		{
			getStrategyEventListener().strategyValidated( aStrategy, aSelectedStrategyDetails  );
		}
	}
	
	/**
	 * @param aStrategy  (may be null)
	 * @param aMessageText
	 */
	public void fireStrategyEventListenerStrategyNotValidated( StrategyT aStrategy, String aMessageText )
	{
		if ( getStrategyEventListener() != null )
		{
			getStrategyEventListener().strategyNotValidated( aStrategy, aMessageText );
		}
	}
	
	/**
	 * @param aStrategy  (may be null)
	 * @param aException
	 */
	public void fireStrategyEventListenerStrategyValidationFailed( StrategyT aStrategy, Throwable aException )
	{
		if ( getStrategyEventListener() != null )
		{
			getStrategyEventListener().strategyValidationFailed( aStrategy, aException );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jCompositePanel#loadScreenWithFilteredStrategiesAndLoadFixMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean loadScreenWithFilteredStrategiesAndLoadFixMessage(String aFixMessage, String aInputSelectStrategyName) throws FIXatdlFormatException
	{
		getAtdl4jOptions().getInputAndFilterData().setInputSelectStrategyName( aInputSelectStrategyName );
// TODO 11/29/2010 Scott Atwell -- ?? further opportunity to improve the behavior/performance of combination of both of these...
		loadScreenWithFilteredStrategies( aInputSelectStrategyName );
		if ( aFixMessage != null )
		{
			return loadFixMessage( aFixMessage );
		}
		else
		{
			return true;
		}
	}
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jCompositePanel#loadScreenWithFilteredStrategiesAndLoadFixMessage(java.lang.String)
	 */
	@Override
	public boolean loadScreenWithFilteredStrategiesAndLoadFixMessage(String aFixMessage)
	{
// TODO 11/29/2010 Scott Atwell -- ?? further opportunity to improve the behavior/performance of combination of both of these...
		loadScreenWithFilteredStrategies();
		if ( aFixMessage != null )
		{
			return loadFixMessage( aFixMessage );
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * @param aStrategyList1
	 * @param aStrategyList2
	 * @return
	 */
	protected boolean isMatchingStrategyList( List<StrategyT> aStrategyList1, List<StrategyT> aStrategyList2 )
	{
		if ( ( aStrategyList1 != null ) && ( aStrategyList2 != null ) )
		{
			if ( aStrategyList1.size() == aStrategyList2.size() )
			{
				for ( int i=0; i < aStrategyList1.size(); i++ )
				{
					if ( aStrategyList1.get( i ).getName().equals( aStrategyList2.get( i ).getName() ) == false )
					{
						return false;
					}
				}
				
				return true;  // -- list matches in size and names/order --
			}
			
			return false;
		}
		else if ( ( aStrategyList1 == null ) && ( aStrategyList2 == null ) )
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/**
	 * @return the lastFilteredStrategyList
	 */
	protected List<StrategyT> getLastFilteredStrategyList()
	{
		return this.lastFilteredStrategyList;
	}
	
	/**
	 * @param aLastFilteredStrategyList the lastFilteredStrategyList to set
	 */
	protected void setLastFilteredStrategyList(List<StrategyT> aLastFilteredStrategyList)
	{
		this.lastFilteredStrategyList = aLastFilteredStrategyList;
	}		
}
