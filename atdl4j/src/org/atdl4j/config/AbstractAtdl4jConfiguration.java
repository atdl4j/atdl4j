package org.atdl4j.config;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Typical setup (for class named XXX):
 * 	private String classNameXXX;
 * 	public void setClassNameXXX(String);
 * 	public String getClassNameXXX();
 *    abstract protected String getDefaultClassNameXXX();
 *    public XXX getXXX() throws ;
 * 	add to constructor:  setClassNameXXX( getDefaultClassNameXXX() );
 * 	NOTE:  add public XXX getXXX() to Atdl4jOptions
 * 	NOTE:  implement protected String getDefaultClassNameXXX(); within derived classes
 * 
 * Note that Class.forName()'s InstantiationException, IllegalAccessException, ClassNotFoundException will be caught and 
 * handled as a run-time error (IllegalStateException)
 * 
 * This class contains the data associated with the <code>AbstractAtdl4jConfiguration</code>.
 * 
 * Creation date: (Feb 7, 2010 6:12:35 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public abstract class AbstractAtdl4jConfiguration
	implements Atdl4jConfiguration
{
	private final Logger logger = Logger.getLogger(AbstractAtdl4jConfiguration.class);

	public static String ATDL4J_PACKAGE_NAME_PATH_FOR_DEBUG_LOGGING = "org.atdl4j";
//TODO 9/26/2010 Scott Atwell	public static String DEFAULT_CLASS_NAME_STRATEGIES_UI_FACTORY = "org.atdl4j.ui.impl.BaseStrategiesUIFactory";
	public static String DEFAULT_CLASS_NAME_CONTROL_UI_FACTORY = "org.atdl4j.ui.impl.BaseControlUIFactory";
	public static String DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY = "org.atdl4j.data.TypeConverterFactory";
	
	
	private String classNameStrategiesUI;
	private String classNameStrategyUI;

	private String classNameControlUIFactory;
	private String classNameTypeConverterFactory;
	private String classNameStrategyPanelHelper;

	
	// -- Controls/Widgets -- 
	private String classNameControlUIForCheckBoxT;
	private String classNameControlUIForDropDownListT;
	private String classNameControlUIForEditableDropDownListT;
	private String classNameControlUIForRadioButtonListT;
	private String classNameControlUIForTextFieldT;
	private String classNameControlUIForSliderT;
	private String classNameControlUIForCheckBoxListT;
	private String classNameControlUIForClockT;
	private String classNameControlUIForSingleSpinnerT;
	private String classNameControlUIForDoubleSpinnerT;
	private String classNameControlUIForSingleSelectListT;
	private String classNameControlUIForMultiSelectListT;
	private String classNameControlUIForHiddenFieldT;
	private String classNameControlUIForLabelT;
	private String classNameControlUIForRadioButtonT;
	
	// -- App Components --
	private String classNameAtdl4jTesterPanel;
	private String classNameAtdl4jInputAndFilterDataSelectionPanel;
	private String classNameAtdl4jInputAndFilterDataPanel;
	private String classNameAtdl4jCompositePanel;
	private String classNameAtdl4jUserMessageHandler;
	private String classNameFixatdlFileSelectionPanel;
	private String classNameFixMsgLoadPanel;
	private String classNameStrategySelectionPanel;
	private String classNameStrategyDescriptionPanel;
	protected String getDefaultClassNameControlUIFactory()
	{ 
		return DEFAULT_CLASS_NAME_CONTROL_UI_FACTORY;
	}
	
	protected String getDefaultClassNameTypeConverterFactory()
	{ 
		return DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY;
	}
	
		// -- UI Infrastructure --
	abstract protected String getDefaultClassNameStrategiesUI();
	abstract protected String getDefaultClassNameStrategyUI();
	abstract protected String getDefaultClassNameStrategyPanelHelper();
	
	// -- Controls/Widgets -- 
	abstract protected String getDefaultClassNameControlUIForCheckBoxT();
	abstract protected String getDefaultClassNameControlUIForDropDownListT();
	abstract protected String getDefaultClassNameControlUIForEditableDropDownListT();
	abstract protected String getDefaultClassNameControlUIForRadioButtonListT();
	abstract protected String getDefaultClassNameControlUIForTextFieldT();
	abstract protected String getDefaultClassNameControlUIForSliderT();
	abstract protected String getDefaultClassNameControlUIForCheckBoxListT();
	abstract protected String getDefaultClassNameControlUIForClockT();
	abstract protected String getDefaultClassNameControlUIForSingleSpinnerT();
	abstract protected String getDefaultClassNameControlUIForDoubleSpinnerT();
	abstract protected String getDefaultClassNameControlUIForSingleSelectListT();
	abstract protected String getDefaultClassNameControlUIForMultiSelectListT();
	abstract protected String getDefaultClassNameControlUIForHiddenFieldT();
	abstract protected String getDefaultClassNameControlUIForLabelT();
	abstract protected String getDefaultClassNameControlUIForRadioButtonT();

	// -- App Components --
	abstract protected String getDefaultClassNameAtdl4jTesterPanel();
	abstract protected String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel();
	abstract protected String getDefaultClassNameAtdl4jInputAndFilterDataPanel();
	abstract protected String getDefaultClassNameAtdl4jCompositePanel();
	abstract protected String getDefaultClassNameAtdl4jUserMessageHandler();
	abstract protected String getDefaultClassNameFixatdlFileSelectionPanel();
	abstract protected String getDefaultClassNameFixMsgLoadPanel();
	abstract protected String getDefaultClassNameStrategySelectionPanel();
	abstract protected String getDefaultClassNameStrategyDescriptionPanel();
	
	/** 
	 * 
	 */
	public AbstractAtdl4jConfiguration()
	{
		// -- UI Infrastructure
//TODO 9/26/2010 Scott Atwell		setClassNameStrategiesUIFactory( getDefaultClassNameStrategiesUIFactory() );
		setClassNameStrategiesUI( getDefaultClassNameStrategiesUI() );
		setClassNameStrategyUI( getDefaultClassNameStrategyUI() );
		setClassNameControlUIFactory( getDefaultClassNameControlUIFactory() );
		setClassNameTypeConverterFactory( getDefaultClassNameTypeConverterFactory() );
		setClassNameStrategyPanelHelper( getDefaultClassNameStrategyPanelHelper() );

		// -- Controls/Widgets -- 
		setClassNameControlUIForCheckBoxT( getDefaultClassNameControlUIForCheckBoxT() );
		setClassNameControlUIForDropDownListT( getDefaultClassNameControlUIForDropDownListT() );
		setClassNameControlUIForEditableDropDownListT( getDefaultClassNameControlUIForEditableDropDownListT() );
		setClassNameControlUIForRadioButtonListT( getDefaultClassNameControlUIForRadioButtonListT() );
		setClassNameControlUIForTextFieldT( getDefaultClassNameControlUIForTextFieldT() );
		setClassNameControlUIForSliderT( getDefaultClassNameControlUIForSliderT() );
		setClassNameControlUIForCheckBoxListT( getDefaultClassNameControlUIForCheckBoxListT() );
		setClassNameControlUIForClockT( getDefaultClassNameControlUIForClockT() );
		setClassNameControlUIForSingleSpinnerT( getDefaultClassNameControlUIForSingleSpinnerT() );
		setClassNameControlUIForDoubleSpinnerT( getDefaultClassNameControlUIForDoubleSpinnerT() );
		setClassNameControlUIForSingleSelectListT( getDefaultClassNameControlUIForSingleSelectListT() );
		setClassNameControlUIForMultiSelectListT( getDefaultClassNameControlUIForMultiSelectListT() );
		setClassNameControlUIForHiddenFieldT( getDefaultClassNameControlUIForHiddenFieldT() );
		setClassNameControlUIForLabelT( getDefaultClassNameControlUIForLabelT() );
		setClassNameControlUIForRadioButtonT( getDefaultClassNameControlUIForRadioButtonT() );

		// -- App Components --
		setClassNameAtdl4jTesterPanel( getDefaultClassNameAtdl4jTesterPanel() );
		setClassNameAtdl4jInputAndFilterDataSelectionPanel( getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel() );
		setClassNameAtdl4jInputAndFilterDataPanel( getDefaultClassNameAtdl4jInputAndFilterDataPanel() );
		setClassNameAtdl4jCompositePanel( getDefaultClassNameAtdl4jCompositePanel() );
		setClassNameAtdl4jUserMessageHandler( getDefaultClassNameAtdl4jUserMessageHandler() );
		setClassNameFixatdlFileSelectionPanel( getDefaultClassNameFixatdlFileSelectionPanel() );
		setClassNameFixMsgLoadPanel( getDefaultClassNameFixMsgLoadPanel() );
		setClassNameStrategySelectionPanel( getDefaultClassNameStrategySelectionPanel() );
		setClassNameStrategyDescriptionPanel( getDefaultClassNameStrategyDescriptionPanel() );
	}
	
	
	/**
	 * @param classNameStrategiesUIFactory the classNameStrategiesUIFactory to set
	 */
//TODO 9/26/2010 Scott Atwell	public void setClassNameStrategiesUIFactory(String classNameStrategiesUIFactory)
//	{
//		this.classNameStrategiesUIFactory = classNameStrategiesUIFactory;
//		setStrategiesUIFactory( null );
//	}

	/**
	 * @return the classNameStrategiesUIFactory
	 */
//TODO 9/26/2010 Scott Atwell	public String getClassNameStrategiesUIFactory()
//	{
//		return classNameStrategiesUIFactory;
//	}
	
	/**
	 * @param strategiesUIFactory the strategiesUIFactory to set
	 */
//TODO 9/26/2010 Scott Atwell	public void setStrategiesUIFactory(StrategiesUIFactory strategiesUIFactory)
//	{
//		this.strategiesUIFactory = strategiesUIFactory;
//	}

	/**
	 * @return the strategiesUIFactory
	 */
//TODO 9/26/2010 Scott Atwell	public StrategiesUIFactory getStrategiesUIFactory() 
/**	{
		if ( ( strategiesUIFactory == null ) && ( getClassNameStrategiesUIFactory() != null ) )
		{
			String tempClassName = getClassNameStrategiesUIFactory();
			logger.debug( "getStrategiesUIFactory() loading class named: " + tempClassName );
			try
			{
				strategiesUIFactory = ((Class<StrategiesUIFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return strategiesUIFactory;
	}
**/
	
	/**
	 * @param classNameStrategiesUI the classNameStrategiesUI to set
	 */
	public void setClassNameStrategiesUI(String classNameStrategiesUI)
	{
		this.classNameStrategiesUI = classNameStrategiesUI;
	}

	/**
	 * @return the classNameStrategiesUI
	 */
	public String getClassNameStrategiesUI()
	{
		return classNameStrategiesUI;
	}
	
	/**
	 * @param classNameStrategyUI the classNameStrategyUI to set
	 */
	public void setClassNameStrategyUI(String classNameStrategyUI)
	{
		this.classNameStrategyUI = classNameStrategyUI;
	}

	/**
	 * @return the classNameStrategyUI
	 */
	public String getClassNameStrategyUI()
	{
		return classNameStrategyUI;
	}
	

	/**
	 * @param classNameControlUIFactory the classNameControlUIFactory to set
	 */
	public void setClassNameControlUIFactory(String classNameControlUIFactory)
	{
		this.classNameControlUIFactory = classNameControlUIFactory;
	}

	/**
	 * @return the classNameControlUIFactory
	 */
	public String getClassNameControlUIFactory()
	{
		return classNameControlUIFactory;
	}
	
	/**
	 * @param classNameTypeConverterFactory the classNameTypeConverterFactory to set
	 */
	public void setClassNameTypeConverterFactory(String classNameTypeConverterFactory)
	{
		this.classNameTypeConverterFactory = classNameTypeConverterFactory;
	}

	/**
	 * @return the classNameTypeConverterFactory
	 */
	public String getClassNameTypeConverterFactory()
	{
		return classNameTypeConverterFactory;
	}
	
	/**
	 * @param classNameControlUIForCheckBoxT the classNameControlUIForCheckBoxT to set
	 */
	public void setClassNameControlUIForCheckBoxT(String classNameControlUIForCheckBoxT)
	{
		this.classNameControlUIForCheckBoxT = classNameControlUIForCheckBoxT;
	}

	/**
	 * @return the classNameControlUIForCheckBoxT
	 */
	public String getClassNameControlUIForCheckBoxT()
	{
		return classNameControlUIForCheckBoxT;
	}
	
	/**
	 * @param classNameControlUIForDropDownListT the classNameControlUIForDropDownListT to set
	 */
	public void setClassNameControlUIForDropDownListT(String classNameControlUIForDropDownListT)
	{
		this.classNameControlUIForDropDownListT = classNameControlUIForDropDownListT;
	}

	/**
	 * @return the classNameControlUIForDropDownListT
	 */
	public String getClassNameControlUIForDropDownListT()
	{
		return classNameControlUIForDropDownListT;
	}
	
	/**
	 * @param classNameControlUIForEditableDropDownListT the classNameControlUIForEditableDropDownListT to set
	 */
	public void setClassNameControlUIForEditableDropDownListT(String classNameControlUIForEditableDropDownListT)
	{
		this.classNameControlUIForEditableDropDownListT = classNameControlUIForEditableDropDownListT;
	}

	/**
	 * @return the classNameControlUIForEditableDropDownListT
	 */
	public String getClassNameControlUIForEditableDropDownListT()
	{
		return classNameControlUIForEditableDropDownListT;
	}
	
	/**
	 * @param classNameControlUIForRadioButtonListT the classNameControlUIForRadioButtonListT to set
	 */
	public void setClassNameControlUIForRadioButtonListT(String classNameControlUIForRadioButtonListT)
	{
		this.classNameControlUIForRadioButtonListT = classNameControlUIForRadioButtonListT;
	}

	/**
	 * @return the classNameControlUIForRadioButtonListT
	 */
	public String getClassNameControlUIForRadioButtonListT()
	{
		return classNameControlUIForRadioButtonListT;
	}
	
	/**
	 * @param classNameControlUIForTextFieldT the classNameControlUIForTextFieldT to set
	 */
	public void setClassNameControlUIForTextFieldT(String classNameControlUIForTextFieldT)
	{
		this.classNameControlUIForTextFieldT = classNameControlUIForTextFieldT;
	}

	/**
	 * @return the classNameControlUIForTextFieldT
	 */
	public String getClassNameControlUIForTextFieldT()
	{
		return classNameControlUIForTextFieldT;
	}
	
	/**
	 * @param classNameControlUIForSliderT the classNameControlUIForSliderT to set
	 */
	public void setClassNameControlUIForSliderT(String classNameControlUIForSliderT)
	{
		this.classNameControlUIForSliderT = classNameControlUIForSliderT;
	}

	/**
	 * @return the classNameControlUIForSliderT
	 */
	public String getClassNameControlUIForSliderT()
	{
		return classNameControlUIForSliderT;
	}
	
	/**
	 * @param classNameControlUIForCheckBoxListT the classNameControlUIForCheckBoxListT to set
	 */
	public void setClassNameControlUIForCheckBoxListT(String classNameControlUIForCheckBoxListT)
	{
		this.classNameControlUIForCheckBoxListT = classNameControlUIForCheckBoxListT;
	}

	/**
	 * @return the classNameControlUIForCheckBoxListT
	 */
	public String getClassNameControlUIForCheckBoxListT()
	{
		return classNameControlUIForCheckBoxListT;
	}
	
	/**
	 * @param classNameControlUIForClockT the classNameControlUIForClockT to set
	 */
	public void setClassNameControlUIForClockT(String classNameControlUIForClockT)
	{
		this.classNameControlUIForClockT = classNameControlUIForClockT;
	}

	/**
	 * @return the classNameControlUIForClockT
	 */
	public String getClassNameControlUIForClockT()
	{
		return classNameControlUIForClockT;
	}
	
	/**
	 * @param classNameControlUIForSingleSpinnerT the classNameControlUIForSingleSpinnerT to set
	 */
	public void setClassNameControlUIForSingleSpinnerT(String classNameControlUIForSingleSpinnerT)
	{
		this.classNameControlUIForSingleSpinnerT = classNameControlUIForSingleSpinnerT;
	}

	/**
	 * @return the classNameControlUIForSingleSpinnerT
	 */
	public String getClassNameControlUIForSingleSpinnerT()
	{
		return classNameControlUIForSingleSpinnerT;
	}
	
	/**
	 * @param classNameControlUIForDoubleSpinnerT the classNameControlUIForDoubleSpinnerT to set
	 */
	public void setClassNameControlUIForDoubleSpinnerT(String classNameControlUIForDoubleSpinnerT)
	{
		this.classNameControlUIForDoubleSpinnerT = classNameControlUIForDoubleSpinnerT;
	}

	/**
	 * @return the classNameControlUIForDoubleSpinnerT
	 */
	public String getClassNameControlUIForDoubleSpinnerT()
	{
		return classNameControlUIForDoubleSpinnerT;
	}
	
	/**
	 * @param classNameControlUIForSingleSelectListT the classNameControlUIForSingleSelectListT to set
	 */
	public void setClassNameControlUIForSingleSelectListT(String classNameControlUIForSingleSelectListT)
	{
		this.classNameControlUIForSingleSelectListT = classNameControlUIForSingleSelectListT;
	}

	/**
	 * @return the classNameControlUIForSingleSelectListT
	 */
	public String getClassNameControlUIForSingleSelectListT()
	{
		return classNameControlUIForSingleSelectListT;
	}
	
	/**
	 * @param classNameControlUIForMultiSelectListT the classNameControlUIForMultiSelectListT to set
	 */
	public void setClassNameControlUIForMultiSelectListT(String classNameControlUIForMultiSelectListT)
	{
		this.classNameControlUIForMultiSelectListT = classNameControlUIForMultiSelectListT;
	}

	/**
	 * @return the classNameControlUIForMultiSelectListT
	 */
	public String getClassNameControlUIForMultiSelectListT()
	{
		return classNameControlUIForMultiSelectListT;
	}
	
	/**
	 * @param classNameControlUIForHiddenFieldT the classNameControlUIForHiddenFieldT to set
	 */
	public void setClassNameControlUIForHiddenFieldT(String classNameControlUIForHiddenFieldT)
	{
		this.classNameControlUIForHiddenFieldT = classNameControlUIForHiddenFieldT;
	}

	/**
	 * @return the classNameControlUIForHiddenFieldT
	 */
	public String getClassNameControlUIForHiddenFieldT()
	{
		return classNameControlUIForHiddenFieldT;
	}
	
	/**
	 * @param classNameControlUIForLabelT the classNameControlUIForLabelT to set
	 */
	public void setClassNameControlUIForLabelT(String classNameControlUIForLabelT)
	{
		this.classNameControlUIForLabelT = classNameControlUIForLabelT;
	}

	/**
	 * @return the classNameControlUIForLabelT
	 */
	public String getClassNameControlUIForLabelT()
	{
		return classNameControlUIForLabelT;
	}
	
	/**
	 * @param classNameControlUIForRadioButtonT the classNameControlUIForRadioButtonT to set
	 */
	public void setClassNameControlUIForRadioButtonT(String classNameControlUIForRadioButtonT)
	{
		this.classNameControlUIForRadioButtonT = classNameControlUIForRadioButtonT;
	}

	/**
	 * @return the classNameControlUIForRadioButtonT
	 */
	public String getClassNameControlUIForRadioButtonT()
	{
		return classNameControlUIForRadioButtonT;
	}
	
	/** TODO 9/27/2010 Scott Atwell removed/moved to AbstractAtdl4jCompositePanel	
	public List<StrategyT> getStrategiesFilteredStrategyList()
	{
		if ( ( getStrategies() == null ) || ( getStrategies().getStrategy() == null ) )
		{
			return null;
		}
		
		if ( getInputAndFilterData() == null )
		{
			return getStrategies().getStrategy();
		}
		
		List<StrategyT> tempFilteredList = new ArrayList<StrategyT>();
		
		for ( StrategyT strategy : getStrategies().getStrategy() ) 
		{
			if ( !getInputAndFilterData().isStrategySupported( strategy ) )
			{
				logger.info("Excluding strategy: " + strategy.getName() + " as inputAndFilterData.isStrategySupported() returned false." );
				continue; // skip it 
			}
			
			tempFilteredList.add( strategy );
		}
		
		return tempFilteredList;
	}
**/
	/**
	 * @param classNameStrategySelectionPanel the classNameStrategySelectionPanel to set
	 */
	public void setClassNameStrategySelectionPanel(String classNameStrategySelectionPanel)
	{
		this.classNameStrategySelectionPanel = classNameStrategySelectionPanel;
	}

	/**
	 * @return the classNameStrategySelectionPanel
	 */
	public String getClassNameStrategySelectionPanel()
	{
		return classNameStrategySelectionPanel;
	}

	/**
	 * @param classNameStrategyDescriptionPanel the classNameStrategyDescriptionPanel to set
	 */
	public void setClassNameStrategyDescriptionPanel(String classNameStrategyDescriptionPanel)
	{
		this.classNameStrategyDescriptionPanel = classNameStrategyDescriptionPanel;
	}

	/**
	 * @return the classNameStrategyDescriptionPanel
	 */
	public String getClassNameStrategyDescriptionPanel()
	{
		return classNameStrategyDescriptionPanel;
	}

	/**
	 * @param classNameFixatdlFileSelectionPanel the classNameFixatdlFileSelectionPanel to set
	 */
	public void setClassNameFixatdlFileSelectionPanel(String classNameFixatdlFileSelectionPanel)
	{
		this.classNameFixatdlFileSelectionPanel = classNameFixatdlFileSelectionPanel;
	}

	/**
	 * @return the classNameFixatdlFileSelectionPanel
	 */
	public String getClassNameFixatdlFileSelectionPanel()
	{
		return classNameFixatdlFileSelectionPanel;
	}

	/**
	 * @param classNameFixMsgLoadPanel the classNameFixMsgLoadPanel to set
	 */
	public void setClassNameFixMsgLoadPanel(String classNameFixMsgLoadPanel)
	{
		this.classNameFixMsgLoadPanel = classNameFixMsgLoadPanel;
	}

	/**
	 * @return the classNameFixMsgLoadPanel
	 */
	public String getClassNameFixMsgLoadPanel()
	{
		return classNameFixMsgLoadPanel;
	}

	/**
	 * @param classNameAtdl4jUserMessageHandler the classNameAtdl4jUserMessageHandler to set
	 */
	public void setClassNameAtdl4jUserMessageHandler(String classNameAtdl4jUserMessageHandler)
	{
		this.classNameAtdl4jUserMessageHandler = classNameAtdl4jUserMessageHandler;
	}

	/**
	 * @return the classNameAtdl4jUserMessageHandler
	 */
	public String getClassNameAtdl4jUserMessageHandler()
	{
		return classNameAtdl4jUserMessageHandler;
	}

	/**
	 * @param classNameAtdl4jTesterPanel the classNameAtdl4jTesterPanel to set
	 */
	public void setClassNameAtdl4jTesterPanel(String classNameAtdl4jTesterPanel)
	{
		this.classNameAtdl4jTesterPanel = classNameAtdl4jTesterPanel;
	}

	/**
	 * @return the classNameAtdl4jTesterPanel
	 */
	public String getClassNameAtdl4jTesterPanel()
	{
		return classNameAtdl4jTesterPanel;
	}

	/**
	 * @param classNameAtdl4jInputAndFilterDataPanel the classNameAtdl4jInputAndFilterDataPanel to set
	 */
	public void setClassNameAtdl4jInputAndFilterDataPanel(String classNameAtdl4jInputAndFilterDataPanel)
	{
		this.classNameAtdl4jInputAndFilterDataPanel = classNameAtdl4jInputAndFilterDataPanel;
	}

	/**
	 * @return the classNameAtdl4jInputAndFilterDataPanel
	 */
	public String getClassNameAtdl4jInputAndFilterDataPanel()
	{
		return classNameAtdl4jInputAndFilterDataPanel;
	}

	/**
	 * @param classNameAtdl4jInputAndFilterDataSelectionPanel the classNameAtdl4jInputAndFilterDataSelectionPanel to set
	 */
	public void setClassNameAtdl4jInputAndFilterDataSelectionPanel(String classNameAtdl4jInputAndFilterDataSelectionPanel)
	{
		this.classNameAtdl4jInputAndFilterDataSelectionPanel = classNameAtdl4jInputAndFilterDataSelectionPanel;
	}

	/**
	 * @return the classNameAtdl4jInputAndFilterDataSelectionPanel
	 */
	public String getClassNameAtdl4jInputAndFilterDataSelectionPanel()
	{
		return classNameAtdl4jInputAndFilterDataSelectionPanel;
	}

	/**
	 * @param classNameAtdl4jCompositePanel the classNameAtdl4jCompositePanel to set
	 */
	public void setClassNameAtdl4jCompositePanel(String classNameAtdl4jCompositePanel)
	{
		this.classNameAtdl4jCompositePanel = classNameAtdl4jCompositePanel;
	}

	/**
	 * @return the classNameAtdl4jCompositePanel
	 */
	public String getClassNameAtdl4jCompositePanel()
	{
		return classNameAtdl4jCompositePanel;
	}

	/* 
	 * Sets Logger's logging level to Level.DEBUG if aDebugLevelFlag is true, otherwise to Level.INFO.
	 * Sets the logging level for ATDL4J_PACKAGE_NAME_PATH_FOR_DEBUG_LOGGING package/path.
	 */
	public void setDebugLoggingLevel( boolean aDebugLevelFlag )
	{
		Level tempLevel = Level.INFO; 
		if ( aDebugLevelFlag )
		{
			tempLevel = Level.DEBUG;
		}
		
		logger.info( "setDebugLoggingLevel( " + aDebugLevelFlag + " ) invoking org.apache.log4j.Logger.getLogger( " + ATDL4J_PACKAGE_NAME_PATH_FOR_DEBUG_LOGGING + " ).setLevel( " + tempLevel + " )" );
		Logger.getLogger( ATDL4J_PACKAGE_NAME_PATH_FOR_DEBUG_LOGGING ).setLevel( tempLevel );
		// -- explicitly set ourself, too --
		logger.setLevel( tempLevel );
	}

	/* 
	 * Returns true if this class' Logger's logging level is Level.DEBUG or higher, otherwise returns false.
	 */
	public boolean isDebugLoggingLevel()
	{
		// -- Use this class' own logger's level as guide --
		Level tempLevel = logger.getLevel(); 
		
		if ( ( tempLevel != null ) && ( Level.DEBUG.isGreaterOrEqual( tempLevel ) ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @return the classNameStrategyPanelHelper
	 */
	public String getClassNameStrategyPanelHelper()
	{
		return this.classNameStrategyPanelHelper;
	}

	/**
	 * @param aClassNameStrategyPanelHelper the classNameStrategyPanelHelper to set
	 */
	public void setClassNameStrategyPanelHelper(String aClassNameStrategyPanelHelper)
	{
		this.classNameStrategyPanelHelper = aClassNameStrategyPanelHelper;
	}

}
