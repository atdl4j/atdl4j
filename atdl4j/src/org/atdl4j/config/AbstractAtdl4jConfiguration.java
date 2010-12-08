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
	public static String DEFAULT_CLASS_NAME_ATDL4j_WIDGET_FACTORY = "org.atdl4j.ui.impl.BaseAtdl4jWidgetFactory";
	public static String DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY = "org.atdl4j.data.TypeConverterFactory";
	
	
	private String classNameStrategiesUI;
	private String classNameStrategyUI;

	private String classNameAtdl4jWidgetFactory;
	private String classNameTypeConverterFactory;
	private String classNameStrategyPanelHelper;

	
	// -- Controls/Widgets -- 
	private String classNameAtdl4jWidgetForCheckBoxT;
	private String classNameAtdl4jWidgetForDropDownListT;
	private String classNameAtdl4jWidgetForEditableDropDownListT;
	private String classNameAtdl4jWidgetForRadioButtonListT;
	private String classNameAtdl4jWidgetForTextFieldT;
	private String classNameAtdl4jWidgetForSliderT;
	private String classNameAtdl4jWidgetForCheckBoxListT;
	private String classNameAtdl4jWidgetForClockT;
	private String classNameAtdl4jWidgetForSingleSpinnerT;
	private String classNameAtdl4jWidgetForDoubleSpinnerT;
	private String classNameAtdl4jWidgetForSingleSelectListT;
	private String classNameAtdl4jWidgetForMultiSelectListT;
	private String classNameAtdl4jWidgetForHiddenFieldT;
	private String classNameAtdl4jWidgetForLabelT;
	private String classNameAtdl4jWidgetForRadioButtonT;
	
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

	private boolean catchAllMainlineExceptions  = false;
	private boolean catchAllRuntimeExceptions  = false;
	private boolean catchAllStrategyLoadExceptions  = false;
	private boolean catchAllValidationExceptions  = false;
	
	private boolean showStrategyDescription = true;
	private boolean showTimezoneSelector = false;
	private boolean showFileSelectionSection = true;
	private boolean showValidateOutputSection = true;
	private boolean showCompositePanelOkCancelButtonSection = true;
	private boolean showTesterPanelOkCancelButtonSection = true;
	private Integer strategyDropDownItemDepth = new Integer( 15 );  // ComboBox drop down 'depth' (aka VisibleItemCount)

	
	protected String getDefaultClassNameAtdl4jWidgetFactory()
	{ 
		return DEFAULT_CLASS_NAME_ATDL4j_WIDGET_FACTORY;
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
	abstract protected String getDefaultClassNameAtdl4jWidgetForCheckBoxT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForDropDownListT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForEditableDropDownListT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForRadioButtonListT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForTextFieldT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForSliderT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForCheckBoxListT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForClockT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForSingleSpinnerT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForDoubleSpinnerT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForSingleSelectListT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForMultiSelectListT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForHiddenFieldT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForLabelT();
	abstract protected String getDefaultClassNameAtdl4jWidgetForRadioButtonT();

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
		setClassNameStrategiesUI( getDefaultClassNameStrategiesUI() );
		setClassNameStrategyUI( getDefaultClassNameStrategyUI() );
		setClassNameAtdl4jWidgetFactory( getDefaultClassNameAtdl4jWidgetFactory() );
		setClassNameTypeConverterFactory( getDefaultClassNameTypeConverterFactory() );
		setClassNameStrategyPanelHelper( getDefaultClassNameStrategyPanelHelper() );

		// -- Controls/Widgets -- 
		setClassNameAtdl4jWidgetForCheckBoxT( getDefaultClassNameAtdl4jWidgetForCheckBoxT() );
		setClassNameAtdl4jWidgetForDropDownListT( getDefaultClassNameAtdl4jWidgetForDropDownListT() );
		setClassNameAtdl4jWidgetForEditableDropDownListT( getDefaultClassNameAtdl4jWidgetForEditableDropDownListT() );
		setClassNameAtdl4jWidgetForRadioButtonListT( getDefaultClassNameAtdl4jWidgetForRadioButtonListT() );
		setClassNameAtdl4jWidgetForTextFieldT( getDefaultClassNameAtdl4jWidgetForTextFieldT() );
		setClassNameAtdl4jWidgetForSliderT( getDefaultClassNameAtdl4jWidgetForSliderT() );
		setClassNameAtdl4jWidgetForCheckBoxListT( getDefaultClassNameAtdl4jWidgetForCheckBoxListT() );
		setClassNameAtdl4jWidgetForClockT( getDefaultClassNameAtdl4jWidgetForClockT() );
		setClassNameAtdl4jWidgetForSingleSpinnerT( getDefaultClassNameAtdl4jWidgetForSingleSpinnerT() );
		setClassNameAtdl4jWidgetForDoubleSpinnerT( getDefaultClassNameAtdl4jWidgetForDoubleSpinnerT() );
		setClassNameAtdl4jWidgetForSingleSelectListT( getDefaultClassNameAtdl4jWidgetForSingleSelectListT() );
		setClassNameAtdl4jWidgetForMultiSelectListT( getDefaultClassNameAtdl4jWidgetForMultiSelectListT() );
		setClassNameAtdl4jWidgetForHiddenFieldT( getDefaultClassNameAtdl4jWidgetForHiddenFieldT() );
		setClassNameAtdl4jWidgetForLabelT( getDefaultClassNameAtdl4jWidgetForLabelT() );
		setClassNameAtdl4jWidgetForRadioButtonT( getDefaultClassNameAtdl4jWidgetForRadioButtonT() );

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
	 * @param classNameAtdl4jWidgetFactory the classNameAtdl4jWidgetFactory to set
	 */
	public void setClassNameAtdl4jWidgetFactory(String classNameAtdl4jWidgetFactory)
	{
		this.classNameAtdl4jWidgetFactory = classNameAtdl4jWidgetFactory;
	}

	/**
	 * @return the classNameAtdl4jWidgetFactory
	 */
	public String getClassNameAtdl4jWidgetFactory()
	{
		return classNameAtdl4jWidgetFactory;
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
	 * @param classNameAtdl4jWidgetForCheckBoxT the classNameAtdl4jWidgetForCheckBoxT to set
	 */
	public void setClassNameAtdl4jWidgetForCheckBoxT(String classNameAtdl4jWidgetForCheckBoxT)
	{
		this.classNameAtdl4jWidgetForCheckBoxT = classNameAtdl4jWidgetForCheckBoxT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForCheckBoxT
	 */
	public String getClassNameAtdl4jWidgetForCheckBoxT()
	{
		return classNameAtdl4jWidgetForCheckBoxT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForDropDownListT the classNameAtdl4jWidgetForDropDownListT to set
	 */
	public void setClassNameAtdl4jWidgetForDropDownListT(String classNameAtdl4jWidgetForDropDownListT)
	{
		this.classNameAtdl4jWidgetForDropDownListT = classNameAtdl4jWidgetForDropDownListT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForDropDownListT
	 */
	public String getClassNameAtdl4jWidgetForDropDownListT()
	{
		return classNameAtdl4jWidgetForDropDownListT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForEditableDropDownListT the classNameAtdl4jWidgetForEditableDropDownListT to set
	 */
	public void setClassNameAtdl4jWidgetForEditableDropDownListT(String classNameAtdl4jWidgetForEditableDropDownListT)
	{
		this.classNameAtdl4jWidgetForEditableDropDownListT = classNameAtdl4jWidgetForEditableDropDownListT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForEditableDropDownListT
	 */
	public String getClassNameAtdl4jWidgetForEditableDropDownListT()
	{
		return classNameAtdl4jWidgetForEditableDropDownListT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForRadioButtonListT the classNameAtdl4jWidgetForRadioButtonListT to set
	 */
	public void setClassNameAtdl4jWidgetForRadioButtonListT(String classNameAtdl4jWidgetForRadioButtonListT)
	{
		this.classNameAtdl4jWidgetForRadioButtonListT = classNameAtdl4jWidgetForRadioButtonListT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForRadioButtonListT
	 */
	public String getClassNameAtdl4jWidgetForRadioButtonListT()
	{
		return classNameAtdl4jWidgetForRadioButtonListT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForTextFieldT the classNameAtdl4jWidgetForTextFieldT to set
	 */
	public void setClassNameAtdl4jWidgetForTextFieldT(String classNameAtdl4jWidgetForTextFieldT)
	{
		this.classNameAtdl4jWidgetForTextFieldT = classNameAtdl4jWidgetForTextFieldT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForTextFieldT
	 */
	public String getClassNameAtdl4jWidgetForTextFieldT()
	{
		return classNameAtdl4jWidgetForTextFieldT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForSliderT the classNameAtdl4jWidgetForSliderT to set
	 */
	public void setClassNameAtdl4jWidgetForSliderT(String classNameAtdl4jWidgetForSliderT)
	{
		this.classNameAtdl4jWidgetForSliderT = classNameAtdl4jWidgetForSliderT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForSliderT
	 */
	public String getClassNameAtdl4jWidgetForSliderT()
	{
		return classNameAtdl4jWidgetForSliderT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForCheckBoxListT the classNameAtdl4jWidgetForCheckBoxListT to set
	 */
	public void setClassNameAtdl4jWidgetForCheckBoxListT(String classNameAtdl4jWidgetForCheckBoxListT)
	{
		this.classNameAtdl4jWidgetForCheckBoxListT = classNameAtdl4jWidgetForCheckBoxListT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForCheckBoxListT
	 */
	public String getClassNameAtdl4jWidgetForCheckBoxListT()
	{
		return classNameAtdl4jWidgetForCheckBoxListT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForClockT the classNameAtdl4jWidgetForClockT to set
	 */
	public void setClassNameAtdl4jWidgetForClockT(String classNameAtdl4jWidgetForClockT)
	{
		this.classNameAtdl4jWidgetForClockT = classNameAtdl4jWidgetForClockT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForClockT
	 */
	public String getClassNameAtdl4jWidgetForClockT()
	{
		return classNameAtdl4jWidgetForClockT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForSingleSpinnerT the classNameAtdl4jWidgetForSingleSpinnerT to set
	 */
	public void setClassNameAtdl4jWidgetForSingleSpinnerT(String classNameAtdl4jWidgetForSingleSpinnerT)
	{
		this.classNameAtdl4jWidgetForSingleSpinnerT = classNameAtdl4jWidgetForSingleSpinnerT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForSingleSpinnerT
	 */
	public String getClassNameAtdl4jWidgetForSingleSpinnerT()
	{
		return classNameAtdl4jWidgetForSingleSpinnerT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForDoubleSpinnerT the classNameAtdl4jWidgetForDoubleSpinnerT to set
	 */
	public void setClassNameAtdl4jWidgetForDoubleSpinnerT(String classNameAtdl4jWidgetForDoubleSpinnerT)
	{
		this.classNameAtdl4jWidgetForDoubleSpinnerT = classNameAtdl4jWidgetForDoubleSpinnerT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForDoubleSpinnerT
	 */
	public String getClassNameAtdl4jWidgetForDoubleSpinnerT()
	{
		return classNameAtdl4jWidgetForDoubleSpinnerT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForSingleSelectListT the classNameAtdl4jWidgetForSingleSelectListT to set
	 */
	public void setClassNameAtdl4jWidgetForSingleSelectListT(String classNameAtdl4jWidgetForSingleSelectListT)
	{
		this.classNameAtdl4jWidgetForSingleSelectListT = classNameAtdl4jWidgetForSingleSelectListT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForSingleSelectListT
	 */
	public String getClassNameAtdl4jWidgetForSingleSelectListT()
	{
		return classNameAtdl4jWidgetForSingleSelectListT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForMultiSelectListT the classNameAtdl4jWidgetForMultiSelectListT to set
	 */
	public void setClassNameAtdl4jWidgetForMultiSelectListT(String classNameAtdl4jWidgetForMultiSelectListT)
	{
		this.classNameAtdl4jWidgetForMultiSelectListT = classNameAtdl4jWidgetForMultiSelectListT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForMultiSelectListT
	 */
	public String getClassNameAtdl4jWidgetForMultiSelectListT()
	{
		return classNameAtdl4jWidgetForMultiSelectListT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForHiddenFieldT the classNameAtdl4jWidgetForHiddenFieldT to set
	 */
	public void setClassNameAtdl4jWidgetForHiddenFieldT(String classNameAtdl4jWidgetForHiddenFieldT)
	{
		this.classNameAtdl4jWidgetForHiddenFieldT = classNameAtdl4jWidgetForHiddenFieldT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForHiddenFieldT
	 */
	public String getClassNameAtdl4jWidgetForHiddenFieldT()
	{
		return classNameAtdl4jWidgetForHiddenFieldT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForLabelT the classNameAtdl4jWidgetForLabelT to set
	 */
	public void setClassNameAtdl4jWidgetForLabelT(String classNameAtdl4jWidgetForLabelT)
	{
		this.classNameAtdl4jWidgetForLabelT = classNameAtdl4jWidgetForLabelT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForLabelT
	 */
	public String getClassNameAtdl4jWidgetForLabelT()
	{
		return classNameAtdl4jWidgetForLabelT;
	}
	
	/**
	 * @param classNameAtdl4jWidgetForRadioButtonT the classNameAtdl4jWidgetForRadioButtonT to set
	 */
	public void setClassNameAtdl4jWidgetForRadioButtonT(String classNameAtdl4jWidgetForRadioButtonT)
	{
		this.classNameAtdl4jWidgetForRadioButtonT = classNameAtdl4jWidgetForRadioButtonT;
	}

	/**
	 * @return the classNameAtdl4jWidgetForRadioButtonT
	 */
	public String getClassNameAtdl4jWidgetForRadioButtonT()
	{
		return classNameAtdl4jWidgetForRadioButtonT;
	}

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

	/**
	 * @return the catchAllMainlineExceptions
	 */
	public boolean isCatchAllMainlineExceptions()
	{
		return this.catchAllMainlineExceptions;
	}

	/**
	 * @return the catchAllRuntimeExceptions
	 */
	public boolean isCatchAllRuntimeExceptions()
	{
		return this.catchAllRuntimeExceptions;
	}

	/**
	 * @return the catchAllStrategyLoadExceptions
	 */
	public boolean isCatchAllStrategyLoadExceptions()
	{
		return this.catchAllStrategyLoadExceptions;
	}

	/**
	 * @return the catchAllStrategyLoadExceptions
	 */
	public boolean isCatchAllValidationExceptions()
	{
		return this.catchAllValidationExceptions;
	}

	/**
	 * @param aCatchAllMainlineExceptions the catchAllMainlineExceptions to set
	 */
	public void setCatchAllMainlineExceptions(boolean aCatchAllMainlineExceptions)
	{
		this.catchAllMainlineExceptions = aCatchAllMainlineExceptions;
	}

	/**
	 * @param aCatchAllRuntimeExceptions the catchAllRuntimeExceptions to set
	 */
	public void setCatchAllRuntimeExceptions(boolean aCatchAllRuntimeExceptions)
	{
		this.catchAllRuntimeExceptions = aCatchAllRuntimeExceptions;
	}

	/**
	 * @param aCatchAllStrategyLoadExceptions the catchAllStrategyLoadExceptions to set
	 */
	public void setCatchAllStrategyLoadExceptions(boolean aCatchAllStrategyLoadExceptions)
	{
		this.catchAllStrategyLoadExceptions = aCatchAllStrategyLoadExceptions;
	}

	/**
	 * @param aCatchAllStrategyLoadExceptions the catchAllStrategyLoadExceptions to set
	 */
	public void setCatchAllValidationExceptions(boolean aCatchAllValidationExceptions)
	{
		this.catchAllValidationExceptions = aCatchAllValidationExceptions;
	}

	/**
	 * @return the showCompositePanelOkCancelButtonSection
	 */
	public boolean isShowCompositePanelOkCancelButtonSection()
	{
		return showCompositePanelOkCancelButtonSection;
	}

	/**
	 * @return the showTesterPanelOkCancelButtonSection
	 */
	public boolean isShowTesterPanelOkCancelButtonSection()
	{
		return showTesterPanelOkCancelButtonSection;
	}

	/**
	 * @return the showFileSelectionSection
	 */
	public boolean isShowFileSelectionSection()
	{
		return showFileSelectionSection;
	}

	/**
	 * @return the showStrategyDescription
	 */
	public boolean isShowStrategyDescription()
	{
		return showStrategyDescription;
	}

	/**
	 * @return the showTimezoneSelector
	 */
	public boolean isShowTimezoneSelector()
	{
		return showTimezoneSelector;
	}

	/**
	 * @return the showValidateOutputSection
	 */
	public boolean isShowValidateOutputSection()
	{
		return showValidateOutputSection;
	}

	/**
	 * @param showCompositePanelOkCancelButtonSection the showCompositePanelOkCancelButtonSection to set
	 */
	public void setShowCompositePanelOkCancelButtonSection(boolean showCompositePanelOkCancelButtonSection)
	{
		this.showCompositePanelOkCancelButtonSection = showCompositePanelOkCancelButtonSection;
	}

	/**
	 * @param showTesterPanelOkCancelButtonSection the showTesterPanelOkCancelButtonSection to set
	 */
	public void setShowTesterPanelOkCancelButtonSection(boolean showTesterPanelOkCancelButtonSection)
	{
		this.showTesterPanelOkCancelButtonSection = showTesterPanelOkCancelButtonSection;
	}

	/**
	 * @param showFileSelectionSection the showFileSelectionSection to set
	 */
	public void setShowFileSelectionSection(boolean showFileSelectionSection)
	{
		this.showFileSelectionSection = showFileSelectionSection;
	}

	/**
	 * @param showStrategyDescription the showStrategyDescription to set
	 */
	public void setShowStrategyDescription(boolean showStrategyDescription)
	{
		this.showStrategyDescription = showStrategyDescription;
	}

	/**
	 * @param showTimezoneSelector the showTimezoneSelector to set
	 */
	public void setShowTimezoneSelector(boolean showTimezoneSelector)
	{
		this.showTimezoneSelector = showTimezoneSelector;
	}

	/**
	 * @param showValidateOutputSection the showValidateOutputSection to set
	 */
	public void setShowValidateOutputSection(boolean showValidateOutputSection)
	{
		this.showValidateOutputSection = showValidateOutputSection;
	}

	/**
	 * @param aStrategyDropDownItemDepth the strategyDropDownItemDepth to set
	 */
	public void setStrategyDropDownItemDepth(Integer aStrategyDropDownItemDepth)
	{
		this.strategyDropDownItemDepth = aStrategyDropDownItemDepth;
	}

	/**
	 * @return the strategyDropDownItemDepth
	 */
	public Integer getStrategyDropDownItemDepth()
	{
		return this.strategyDropDownItemDepth;
	}

}
