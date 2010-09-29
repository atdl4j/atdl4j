package org.atdl4j.ui.app;

import java.io.File;
import java.io.IOException;
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
import org.atdl4j.data.FIXMessageParser;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;

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
	FixatdlFileSelectionPanelListener,
	StrategySelectionPanelListener
{
	public final Logger logger = Logger.getLogger(AbstractAtdl4jCompositePanel.class);

	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private List<Atdl4jCompositePanelListener> listenerList = new Vector<Atdl4jCompositePanelListener>();

	private String lastFixatdlFilename;
	
	private FixatdlFileSelectionPanel fixatdlFileSelectionPanel;
	private StrategySelectionPanel strategySelectionPanel;
	private StrategyDescriptionPanel strategyDescriptionPanel;
// TODO 9/26/2010 Scott Atwell	private StrategiesPanel strategiesPanel;
	private StrategiesUI strategiesUI;
	
//TODO 9/27/2010 Scott Atwell added (moved from Atdl4jOptions)
	private StrategiesT strategies; 
	private StrategyT selectedStrategy;
	private boolean selectedStrategyValidated = false;

	private Atdl4jUserMessageHandler atdl4jUserMessageHandler;

	abstract protected Object createValidateOutputSection();
	abstract protected void setValidateOutputText(String aText);
	abstract public void setVisibleValidateOutputSection( boolean aVisible );
	abstract public void setVisibleFileSelectionSection( boolean aVisible );
	abstract public void setVisibleOkCancelButtonSection( boolean aVisible );
	abstract protected void packLayout();


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
//		if ( ( getAtdl4jOptions() != null ) && 
//			  ( getAtdl4jOptions().getAtdl4jUserMessageHandler() != null ) && 
//			  ( getAtdl4jOptions().getAtdl4jUserMessageHandler().isInitReqd() ) )
		if ( ( getAtdl4jUserMessageHandler() != null ) && 
			  ( getAtdl4jUserMessageHandler().isInitReqd() ) )
		{
//			getAtdl4jOptions().initAtdl4jUserMessageHandler( aParentOrShell );
			initAtdl4jUserMessageHandler( aParentOrShell );
		}
	
		// ----- Setup internal components (the GUI-specific versions will be instantiated, and add listeners, but defer to concrete classes: "build____Panel()" ----
		
		// -- FixatdlFileSelectionPanel (filename / file dialog) - build() method called via concrete class --
//		setFixatdlFileSelectionPanel( getAtdl4jOptions().getFixatdlFileSelectionPanel() );
		setFixatdlFileSelectionPanel( getFixatdlFileSelectionPanel() );
		getFixatdlFileSelectionPanel().addListener( this );

		// -- StrategySelectionPanel (drop down with list of strategies to choose from) - build() method called via concrete class --
//		setStrategySelectionPanel( getAtdl4jOptions().getStrategySelectionPanel() );
		setStrategySelectionPanel( getStrategySelectionPanel() );
		getStrategySelectionPanel().addListener( this );

		// -- StrategyDescriptionPanel (drop down with list of strategies to choose from) - build() method called via concrete class --
//		setStrategyDescriptionPanel( getAtdl4jOptions().getStrategyDescriptionPanel() );
		setStrategyDescriptionPanel( getStrategyDescriptionPanel() );

		// -- StrategiesPanel (GUI display of each strategy's parameters) - build() method called via concrete class --
	// TODO 9/26/2010 Scott Atwell		setStrategiesPanel( getAtdl4jOptions().getStrategiesPanel() );
//TODO 9/29/2010		setStrategiesUI( getAtdl4jOptions().getStrategiesUI() );
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
	 * @return the strategySelectionPanel
	 */
//	public StrategySelectionPanel getStrategySelectionPanel()
//	{
//		return strategySelectionPanel;
//	}

	/**
	 * @param strategyDescriptionPanel the strategyDescriptionPanel to set
	 */
	protected void setStrategyDescriptionPanel(StrategyDescriptionPanel strategyDescriptionPanel)
	{
		this.strategyDescriptionPanel = strategyDescriptionPanel;
	}

	/**
	 * @return the strategyDescriptionPanel
	 */
//	public StrategyDescriptionPanel getStrategyDescriptionPanel()
//	{
//		return strategyDescriptionPanel;
//	}

	/**
	 * @param fixatdlFileSelectionPanel the fixatdlFileSelectionPanel to set
	 */
	protected void setFixatdlFileSelectionPanel(FixatdlFileSelectionPanel fixatdlFileSelectionPanel)
	{
		this.fixatdlFileSelectionPanel = fixatdlFileSelectionPanel;
	}

	/**
	 * @return the fixatdlFileSelectionPanel
	 */
//	public FixatdlFileSelectionPanel getFixatdlFileSelectionPanel()
//	{
//		return fixatdlFileSelectionPanel;
//	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategySelectionPanelListener#strategySelected(org.atdl4j.fixatdl.core.StrategyT, int)
	 */
	@Override
// 4/16/2010 Scott Atwell	public void strategySelected(StrategyT aStrategy, int aIndex)
	public void strategySelected(StrategyT aStrategy)
	{
// 9/27/2010 Scott Atwell - moved from SWTStrategySelectionPanel.selectDropDownStrategy()
		setSelectedStrategy( aStrategy );
		
// 9/27/2010		getAtdl4jOptions().setSelectedStrategyValidated( false );
		setSelectedStrategyValidated( false );
		setValidateOutputText( "" );
		getStrategyDescriptionPanel().loadStrategyDescriptionVisible( aStrategy );
// 4/16/2010 Scott Atwell		getStrategiesPanel().adjustLayoutForSelectedStrategy( aIndex );
// TODO 9/26/2010 Scott Atwell		getStrategiesPanel().adjustLayoutForSelectedStrategy( aStrategy );
		getStrategiesUI().adjustLayoutForSelectedStrategy( aStrategy );

		packLayout();
		getStrategyDescriptionPanel().loadStrategyDescriptionText( aStrategy );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanelListener#fixatdlFileSelected(java.lang.String)
	 */
	@Override
	public void fixatdlFileSelected(String aFilename)
	{
		if (getAtdl4jOptions() != null && 
			getAtdl4jOptions().isCatchAllStrategyLoadExceptions())
		{
			try {
				fixatdlFileSelectedNotCatchAllExceptions(aFilename);
			}
			catch (Exception e) {
						logger.warn( "parseFixatdlFile/loadScreenWithFilteredStrategies exception", e );
//						getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "FIXatdl Unknown Exception", "", e );
						getAtdl4jUserMessageHandler().displayException( "FIXatdl Unknown Exception", "", e );
			}
		} else {
			fixatdlFileSelectedNotCatchAllExceptions(aFilename);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanelListener#fixatdlFileSelected(java.lang.String)
	 */
	protected void fixatdlFileSelectedNotCatchAllExceptions(String aFilename)
	{
		try {
			parseFixatdlFile( aFilename );
			loadScreenWithFilteredStrategies();
		} catch (JAXBException e) {
			logger.warn( "parseFixatdlFile/loadScreenWithFilteredStrategies exception", e );
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "FIXatdl File Parse Exception", "", e );
			getAtdl4jUserMessageHandler().displayException( "FIXatdl File Parse Exception", "", e );
		} catch (NumberFormatException e) {
			logger.warn( "parseFixatdlFile/loadScreenWithFilteredStrategies exception", e );
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "FIXatdl Number Format Exception", "", e );
			getAtdl4jUserMessageHandler().displayException( "FIXatdl Number Format Exception", "", e );
		} catch (IOException e) {
			logger.warn( "parseFixatdlFile/loadScreenWithFilteredStrategies exception", e );
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "FIXatdl IO Exception", "", e );
			getAtdl4jUserMessageHandler().displayException( "FIXatdl IO Exception", "", e );
		}
	}


	/**
	 * @param strategiesUI the strategiesUI to set
	 */
// TODO 9/26/2010 Scott Atwell	protected void setStrategiesPanel(StrategiesPanel strategiesPanel)
	protected void setStrategiesUI(StrategiesUI strategiesUI)
	{
		this.strategiesUI = strategiesUI;
	}

	/**
	 * @return the strategiesUI
	 */
// TODO 9/26/2010 Scott Atwell	public StrategiesPanel getStrategiesPanel()
//	public StrategiesUI getStrategiesUI()
//	{
//		return strategiesUI;
//	}
	
	/* 
	 * @return StrategyT (non-null only if passes all validation)
	 */
	public StrategyT validateStrategy() {
		if (getAtdl4jOptions() != null
				&& getAtdl4jOptions().isCatchAllValidationExceptions()) {
			try {
				return validateStrategyWithoutCatchingAllExceptions();
			} catch (Exception ex) {

				setValidateOutputText("");
//				getAtdl4jOptions().getAtdl4jUserMessageHandler()
//						.displayException("Exception", "", ex);
				getAtdl4jUserMessageHandler().displayException("Exception", "", ex);
				logger.warn("Generic Exception", ex);
				return null;
			}

		} else {
			return validateStrategyWithoutCatchingAllExceptions();
		}
	}	
	
	public StrategyT validateStrategyWithoutCatchingAllExceptions() 
	{
// 9/27/2010 Scott Atwell		StrategyT tempSelectedStrategy = getAtdl4jOptions().getSelectedStrategy();
		StrategyT tempSelectedStrategy = getSelectedStrategy();
		
		if (tempSelectedStrategy == null)
		{
			setValidateOutputText("Please select a strategy");
			return null;
		}
		
// 9/27/2010 Scott Atwell		getAtdl4jOptions().setSelectedStrategyValidated( false );
		setSelectedStrategyValidated( false );
		
		logger.info("Validating strategy " + tempSelectedStrategy.getName());
		
		try 
		{
// 6/23/2010 Scott Atwell			StrategyUI ui = getAtdl4jOptions().getStrategyUIMap().get(tempSelectedStrategy);
// 9/29/2010 Scott Atwell			StrategyUI ui = getAtdl4jOptions().getStrategyUI(tempSelectedStrategy);
			StrategyUI ui = getStrategiesUI().getStrategyUI(tempSelectedStrategy);
			ui.validate();
			String tempUiFixMsg = ui.getFIXMessage();
			setValidateOutputText( tempUiFixMsg );
// 9/27/2010			getAtdl4jOptions().setSelectedStrategyValidated( true );
			setSelectedStrategyValidated( true );
			logger.info("Successfully Validated strategy " + tempSelectedStrategy.getName() + " FIXMessage: " + tempUiFixMsg );
			return tempSelectedStrategy;
		} 
		catch (ValidationException ex) 
		{
			setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( ex ));
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
			getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
			logger.info( "Validation Exception:", ex );
			return null;
		} 
	}
	

	/* 
	 * Parses the FIXatdl file aFilename into StrategiesT storing the result via Atdl4jOptions().setStrategies().
	 */
	public void parseFixatdlFile( String aFilename ) 
		throws JAXBException,
				 IOException, 
				 NumberFormatException 
	{
		setLastFixatdlFilename( null );
		setStrategies( null );
// 6/23/2010 Scott Atwell		getStrategiesPanel().setPreCached( false );
		
		// parses the XML document and build an object model
		JAXBContext jc = JAXBContext.newInstance(StrategiesT.class.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
	
		try 
		{
			// try to parse as URL
			URL url = new URL( aFilename );
			
			JAXBElement<?> element = (JAXBElement<?>) um.unmarshal(url);
			
// 4/18/2010 Scott Atwell added
			validateParsedFixatdlFileContents( (StrategiesT) element.getValue() );

			setStrategies( (StrategiesT) element.getValue() );
			
			setLastFixatdlFilename( aFilename );
		} 
		catch (MalformedURLException e) 
		{
			// try to parse as file
			File file = new File( aFilename );		
			
			JAXBElement<?> element = (JAXBElement<?>) um.unmarshal(file);
			
// 4/18/2010 Scott Atwell added
			validateParsedFixatdlFileContents( (StrategiesT) element.getValue() );

			setStrategies( (StrategiesT) element.getValue() );
			
			setLastFixatdlFilename( aFilename );
		}
	}
	
	public void validateParsedFixatdlFileContents( StrategiesT aStrategies )
		throws JAXBException
	{
		List<String> tempStrategyNameList = new ArrayList<String>();
		
		for ( StrategyT tempStrategy : aStrategies.getStrategy() )
		{
			if ( ! Atdl4jHelper.isStrategyNameValid( tempStrategy.getName() ) )
			{
				throw new JAXBException("Strategy/@name SYNTAX ERROR: \"" + tempStrategy.getName() + "\" does not match FIXatdl schema pattern: \"" + Atdl4jConstants.PATTERN_STRATEGY_NAME + "\"" );
			}
			
			// -- Check for duplicate Strategy/@name values --
			if ( tempStrategyNameList.contains( tempStrategy.getName() ) )
			{
				throw new JAXBException("DUPLICATE Strategy/@name ERROR: \"" + tempStrategy.getName() + "\" already exists." );
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
	 */
	public void loadScreenWithFilteredStrategies()
	{
		// obtain filtered StrategyList
// 9/27/2010 Scott Atwell		List<StrategyT> tempFilteredStrategyList = getAtdl4jOptions().getStrategiesFilteredStrategyList();
		List<StrategyT> tempFilteredStrategyList = getStrategiesFilteredStrategyList();
		
		if ( tempFilteredStrategyList == null )
		{
			if ( getStrategies() != null )
			{
				// -- Only generate the error message if Strategies have been parsed -- 
//				getAtdl4jOptions().getAtdl4jUserMessageHandler().displayMessage( "Unexpected Error", "Unexpected Error: getStrategiesFilteredStrategyList() was null." );
				getAtdl4jUserMessageHandler().displayMessage( "Unexpected Error", "Unexpected Error: getStrategiesFilteredStrategyList() was null." );
			}
			return;
		}

// 4/16/2010 Scott Atwell Added		
		// -- Optional, can control the order in which the strategies will appear in the list, and can be used to restrict the list to a subset --
		tempFilteredStrategyList = getAtdl4jOptions().getStrategyListUsingInputStrategyNameListFilter( tempFilteredStrategyList );

		if ( tempFilteredStrategyList == null )
		{
			if ( getStrategies() != null )
			{
				// -- Only generate the error message if Strategies have been parsed -- 
//				getAtdl4jOptions().getAtdl4jUserMessageHandler().displayMessage( "Unexpected Error", "Unexpected Error: getStrategyListUsingInputStrategyNameListFilter() was null." );
				getAtdl4jUserMessageHandler().displayMessage( "Unexpected Error", "Unexpected Error: getStrategyListUsingInputStrategyNameListFilter() was null." );
			}
			return;
		}
		
// 4/2/2010 Scott Atwell added
		// -- Reduce screen re-draw/flash (doesn't really work for SWT, though) --
// TODO 9/26/2010 Scott Atwell		getStrategiesPanel().setVisible( false );
		getStrategiesUI().setVisible( false );

		
// 6/23/2010 Scott Atwell		if ( getAtdl4jOptions().isUsePreCachedStrategyPanels() )
//		{
//			if ( ! getStrategiesPanel().isPreCached() )
//			{
//				// remove all strategy panels
//				getStrategiesPanel().removeAllStrategyPanels();
//				
//				List<StrategyT> tempUnfilteredStrategyList = getStrategies().getStrategy();
//				
//				// -- Need to Pre-cached panels, load complete, unfiltered list --
//				getStrategiesPanel().createStrategyPanels( tempUnfilteredStrategyList );
//			}
//		}
//		else
//		{
			// remove all strategy panels
// TODO 9/26/2010 Scott Atwell			getStrategiesPanel().removeAllStrategyPanels();
			getStrategiesUI().removeAllStrategyPanels();
			
			// -- Always build StrategyPanels anew (can be time intensive) --
// TODO 9/26/2010 Scott Atwell			getStrategiesPanel().createStrategyPanels( tempFilteredStrategyList );
// TODO 9/27/2010 Scott Atwell			getStrategiesUI().createStrategyPanels( tempFilteredStrategyList );
			getStrategiesUI().createStrategyPanels( getStrategies(), tempFilteredStrategyList );
//		}
		
		getStrategySelectionPanel().loadStrategyList( tempFilteredStrategyList );
		
		if ( ( getAtdl4jOptions().getInputAndFilterData() != null ) && 
			  ( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() != null ) )
		{
			getStrategySelectionPanel().selectDropDownStrategyByStrategyName( getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
		}
		else
		{
			getStrategySelectionPanel().selectFirstDropDownStrategy();
		}
		
// 4/2/2010 Scott Atwell added
		// -- Reduce screen re-draw/flash (doesn't really work for SWT, though) --
// 9/26/2010 Scott Atwell		getStrategiesPanel().setVisible( true );
		getStrategiesUI().setVisible( true );
	}
	
	public boolean loadFixMessage( String aFixMessage ) 
	{
		logger.info("Loading FIX string " + aFixMessage);
		try 
		{
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
/** 4/16/2010 Scott Atwell									
						if ( getStrategies().getStrategy() != null )
						{
							for ( StrategyT tempStrategy : getStrategies().getStrategy() )
							{
								if ( strategyWireValue.equals( tempStrategy.getWireValue() ) )
								{
									if ( tempStrategy.getUiRep() != null )
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getUiRep(): " + tempStrategy.getUiRep() );							
										getStrategySelectionPanel().selectDropDownStrategy( tempStrategy.getUiRep() );
									}
									else
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getName(): " + tempStrategy.getName() );							
										getStrategySelectionPanel().selectDropDownStrategy( tempStrategy.getName() );
									}
									break;
								}
							}
						}
***/									
						logger.info("Invoking selectDropDownStrategy for strategyWireValue: " + strategyWireValue );							
						getStrategySelectionPanel().selectDropDownStrategyByStrategyWireValue( strategyWireValue );
					}
					
				}
			}

// 9/27/2010 Scott Atwell			if (getAtdl4jOptions().getSelectedStrategy() == null)
			if (getSelectedStrategy() == null)
			{
				setValidateOutputText("Please select a strategy");
				return false;
			}
			
// 6/23/2010 Scott Atwell			StrategyUI ui = getAtdl4jOptions().getStrategyUIMap().get(getAtdl4jOptions().getSelectedStrategy());
// 9/27/2010 Scott Atwell			StrategyUI ui = getAtdl4jOptions().getStrategyUI(getAtdl4jOptions().getSelectedStrategy());
// 9/29/2010 Scott Atwell			StrategyUI ui = getAtdl4jOptions().getStrategyUI( getSelectedStrategy() );
			StrategyUI ui = getStrategiesUI().getStrategyUI( getSelectedStrategy() );
			
			// -- Note available getStrategies() may be filtered due to SecurityTypes, Markets, or Region/Country rules --  
			if ( ui != null )
			{
				logger.info( "Invoking ui.setFIXMessage() for: " + ui.getStrategy().getName() + " with FIX Message: " + aFixMessage );
				ui.setFIXMessage(aFixMessage);
				logger.info( "FIX string loaded successfully!" );
				setValidateOutputText("FIX string loaded successfully!");
				return true;
			}
			else
			{
// 9/27/2010 Scott Atwell				setValidateOutputText( getAtdl4jOptions().getSelectedStrategy().getName() + " is not available.");
				setValidateOutputText( getSelectedStrategy().getName() + " is not available.");
				return false;
			}
		} 
		catch (ValidationException ex) 
		{
			setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( ex ));
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
			getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
			logger.info( "Validation Exception:", ex );
			return false;
		} 
		catch (Exception ex) 
		{
			setValidateOutputText( "" );
//			getAtdl4jOptions().getAtdl4jUserMessageHandler().displayException( "Exception", "", ex );
			getAtdl4jUserMessageHandler().displayException( "Exception", "", ex );
			logger.warn( "Generic Exception", ex );
			return false;
		}

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
	
	/* 
	 * Invokes fixatdlFileSelected() with getLastFixatdlFilename() if non-null.  
	 * Re-reads the FIXatdl XML file and then re-loads the screen for StrategiesT.
	 */
	public void reloadFixatdlFile() throws Exception 
	{
		if ( getLastFixatdlFilename() != null )
		{
			fixatdlFileSelected( getLastFixatdlFilename() );
		}
	}

	protected void validateButtonSelected()
	{
		validateStrategy();
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
	 */
	public StrategyDescriptionPanel getStrategyDescriptionPanel() 
	{
		if ( ( strategyDescriptionPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameStrategyDescriptionPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameStrategyDescriptionPanel();
			logger.debug( "getStrategyDescriptionPanel() loading class named: " + tempClassName );
			try
			{
				strategyDescriptionPanel = ((Class<StrategyDescriptionPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return strategyDescriptionPanel;
	}	
	
	/**
	 * @return the StrategySelectionPanel
	 */
	public StrategySelectionPanel getStrategySelectionPanel() 
	{
		if ( ( strategySelectionPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameStrategySelectionPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameStrategySelectionPanel();
			logger.debug( "getStrategySelectionPanel() loading class named: " + tempClassName );
			try
			{
				strategySelectionPanel = ((Class<StrategySelectionPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return strategySelectionPanel;
	}

	/**
	 * @return the FixatdlFileSelectionPanel
	 */
	public FixatdlFileSelectionPanel getFixatdlFileSelectionPanel() 
	{
		if ( ( fixatdlFileSelectionPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameFixatdlFileSelectionPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameFixatdlFileSelectionPanel();
			logger.debug( "getFixatdlFileSelectionPanel() loading class named: " + tempClassName );
			try
			{
				fixatdlFileSelectionPanel = ((Class<FixatdlFileSelectionPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return fixatdlFileSelectionPanel;
	}
	
	/**
	 * @return
	 */
	public StrategiesUI getStrategiesUI()
	{
		if ( ( strategiesUI == null ) && ( Atdl4jConfig.getConfig().getClassNameStrategiesUI() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameStrategiesUI();
			logger.debug( "getStrategiesUI() loading class named: " + tempClassName );
			try
			{
				strategiesUI = ((Class<StrategiesUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( strategiesUI != null )
			{
// TODO 9/27/2010 Scott Atwell				strategiesUI.init( strategies, this );
				strategiesUI.init( getAtdl4jOptions() );
			}
		}
		
		return strategiesUI;
	}
	/**
	 * @return the Atdl4jUserMessageHandler
	 */
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler() 
	{
		if ( ( atdl4jUserMessageHandler == null ) && ( Atdl4jConfig.getConfig().getClassNameAtdl4jUserMessageHandler() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jUserMessageHandler();
			logger.debug( "getAtdl4jUserMessageHandler() loading class named: " + tempClassName );
			try
			{
				atdl4jUserMessageHandler = ((Class<Atdl4jUserMessageHandler>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
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
	 */
	public void initAtdl4jUserMessageHandler( Object parentOrShell )
	{
		getAtdl4jUserMessageHandler().init(  parentOrShell, getAtdl4jOptions() );
	}

}
