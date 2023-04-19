package org.atdl4j.config;

import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyPanelHelper;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataSelectionPanel;
import org.atdl4j.ui.app.Atdl4jTesterPanel;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.FixMsgLoadPanel;
import org.atdl4j.ui.app.FixatdlFileSelectionPanel;
import org.atdl4j.ui.app.StrategyDescriptionPanel;
import org.atdl4j.ui.app.StrategySelectionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Typical setup (for class named XXX):
 * 	private String classNameXXX;
 * 	public void setClassNameXXX(String);
 * 	public String getClassNameXXX();
 *    protected abstract String getDefaultClassNameXXX();
 *    public XXX getXXX() throws ;
 * 	add to constructor:  setClassNameXXX( getDefaultClassNameXXX() );
 * 	NOTE:  add public XXX getXXX() to Atdl4jOptions
 * 	NOTE:  implement protected String getDefaultClassNameXXX(); within derived classes
 * 
 * Note that Class.forName()'s InstantiationException, IllegalAccessException, ClassNotFoundException are
 * rethrown as an Atdl4jClassLoadException
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
	private final Logger logger = LoggerFactory.getLogger(AbstractAtdl4jConfiguration.class);

	public static final String ATDL4J_PACKAGE_NAME_PATH_FOR_DEBUG_LOGGING = "org.atdl4j";
	public static final String DEFAULT_CLASS_NAME_ATDL4J_WIDGET_FACTORY = "org.atdl4j.ui.impl.BaseAtdl4jWidgetFactory";
	public static final String DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY = "org.atdl4j.data.TypeConverterFactory";
	
	
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
	private boolean throwEventRuntimeExceptions = true;
	private boolean prevalidateClassLoaders = true;
	
	private boolean showStrategyDescription = true;
	private boolean showTimezoneSelector = false;
	private boolean showFileSelectionSection = true;
	private boolean showValidateOutputSection = true;
	private boolean showCompositePanelOkCancelButtonSection = true;
	private boolean showTesterPanelOkCancelButtonSection = true;
	private Integer strategyDropDownItemDepth = Integer.valueOf( 15 );  // ComboBox drop down 'depth' (aka VisibleItemCount)

	
	protected String getDefaultClassNameAtdl4jWidgetFactory()
	{ 
		return DEFAULT_CLASS_NAME_ATDL4J_WIDGET_FACTORY;
	}
	
	protected String getDefaultClassNameTypeConverterFactory()
	{ 
		return DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY;
	}
	
	// -- UI Infrastructure --
	protected abstract String getDefaultClassNameStrategiesUI();
	protected abstract String getDefaultClassNameStrategyUI();
	protected abstract String getDefaultClassNameStrategyPanelHelper();
	
	// -- Controls/Widgets -- 
	protected abstract String getDefaultClassNameAtdl4jWidgetForCheckBoxT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForDropDownListT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForEditableDropDownListT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForRadioButtonListT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForTextFieldT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForSliderT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForCheckBoxListT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForClockT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForSingleSpinnerT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForDoubleSpinnerT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForSingleSelectListT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForMultiSelectListT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForHiddenFieldT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForLabelT();
	protected abstract String getDefaultClassNameAtdl4jWidgetForRadioButtonT();

	// -- App Components --
	protected abstract String getDefaultClassNameAtdl4jTesterPanel();
	protected abstract String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel();
	protected abstract String getDefaultClassNameAtdl4jInputAndFilterDataPanel();
	protected abstract String getDefaultClassNameAtdl4jCompositePanel();
	protected abstract String getDefaultClassNameAtdl4jUserMessageHandler();
	protected abstract String getDefaultClassNameFixatdlFileSelectionPanel();
	protected abstract String getDefaultClassNameFixMsgLoadPanel();
	protected abstract String getDefaultClassNameStrategySelectionPanel();
	protected abstract String getDefaultClassNameStrategyDescriptionPanel();
	
	/** 
	 * 
	 */
	protected AbstractAtdl4jConfiguration()
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
		
		// test for runtime exceptions
		if (prevalidateClassLoaders) testClassLoaders();
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
	 * @return the throwEventRuntimeExceptions
	 */
	public boolean isThrowEventRuntimeExceptions() {
	    return throwEventRuntimeExceptions;
	}
	
	/**
	 * @return the prevalidateClassLoaders
	 */
	public boolean isPrevalidateClassLoaders() {
	    return prevalidateClassLoaders;
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
	 * @param aCatchAllValidationExceptions the catchAllStrategyLoadExceptions to set
	 */
	public void setCatchAllValidationExceptions(boolean aCatchAllValidationExceptions)
	{
		this.catchAllValidationExceptions = aCatchAllValidationExceptions;
	}

	/**
	 * @param throwEventRuntimeExceptions the throwEventRuntimeExceptions to set
	 */
	public void setThrowEventRuntimeExceptions(
		boolean throwEventRuntimeExceptions) {
	    this.throwEventRuntimeExceptions = throwEventRuntimeExceptions;
	}
	
	/**
	 * @param prevalidateClassLoaders the prevalidateClassLoaders to set
	 */
	public void setPrevalidateClassLoaders(boolean prevalidateClassLoaders) {
	    this.prevalidateClassLoaders = prevalidateClassLoaders;
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
	
	/**
	 * Class factory methods
	 */
	private <E> E createClass(String aClassName)
	{
	    if (aClassName==null) throw new Atdl4jClassLoadException(aClassName);
	    logger.debug( "Loading class: {}", aClassName );
	    try
		{
		    return ((Class<E>) Class.forName( aClassName ) ).newInstance();
	    }
	    catch (ClassNotFoundException|IllegalAccessException|InstantiationException e)
		{
		 throw new Atdl4jClassLoadException(aClassName, e);
	    }
	}
	
	public Atdl4jWidget<?> createAtdl4jWidgetForCheckBoxListT()
	{
	    return createClass(getClassNameAtdl4jWidgetForCheckBoxListT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForCheckBoxT()
	{
	    return createClass(getClassNameAtdl4jWidgetForCheckBoxT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForClockT()
	{
	    return createClass(getClassNameAtdl4jWidgetForClockT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForDoubleSpinnerT()
	{
	    return createClass(getClassNameAtdl4jWidgetForDoubleSpinnerT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForDropDownListT()
	{
	    return createClass(getClassNameAtdl4jWidgetForDropDownListT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForEditableDropDownListT()
	{
	    return createClass(getClassNameAtdl4jWidgetForEditableDropDownListT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForHiddenFieldT()
	{
	    return createClass(getClassNameAtdl4jWidgetForHiddenFieldT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForLabelT()
	{
	    return createClass(getClassNameAtdl4jWidgetForLabelT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForMultiSelectListT()
	{
	    return createClass(getClassNameAtdl4jWidgetForMultiSelectListT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForRadioButtonListT()
	{
	    return createClass(getClassNameAtdl4jWidgetForRadioButtonListT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForRadioButtonT()
	{
	    return createClass(getClassNameAtdl4jWidgetForRadioButtonT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForSingleSelectListT()
	{
	    return createClass(getClassNameAtdl4jWidgetForSingleSelectListT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForSingleSpinnerT()
	{
	    return createClass(getClassNameAtdl4jWidgetForSingleSpinnerT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForSliderT()
	{
	    return createClass(getClassNameAtdl4jWidgetForSliderT());
	}
	public Atdl4jWidget<?> createAtdl4jWidgetForTextFieldT()
	{
	    return createClass(getClassNameAtdl4jWidgetForTextFieldT());
	}
	public Atdl4jCompositePanel createAtdl4jCompositePanel()
	{
	    return createClass(getClassNameAtdl4jCompositePanel());
	}
	public Atdl4jInputAndFilterDataPanel createAtdl4jInputAndFilterDataPanel()
	{
	    return createClass(getClassNameAtdl4jInputAndFilterDataPanel());
	}
	public Atdl4jInputAndFilterDataSelectionPanel createAtdl4jInputAndFilterDataSelectionPanel()
	{
	    return createClass(getClassNameAtdl4jInputAndFilterDataSelectionPanel());
	}
	public Atdl4jTesterPanel createAtdl4jTesterPanel()
	{
	    return createClass(getClassNameAtdl4jTesterPanel());
	}
	public Atdl4jUserMessageHandler createAtdl4jUserMessageHandler()
	{
	    return createClass(getClassNameAtdl4jUserMessageHandler());
	}
	public Atdl4jWidgetFactory createAtdl4jWidgetFactory()
	{
	    return createClass(getClassNameAtdl4jWidgetFactory());
	}
	public FixatdlFileSelectionPanel createFixatdlFileSelectionPanel()
	{
	    return createClass(getClassNameFixatdlFileSelectionPanel());
	}
	public FixMsgLoadPanel createFixMsgLoadPanel()
	{
	    return createClass(getClassNameFixMsgLoadPanel());
	}
	public StrategiesUI createStrategiesUI()
	{
	    return createClass(getClassNameStrategiesUI());
	}
	public StrategyDescriptionPanel createStrategyDescriptionPanel()
	{
	    return createClass(getClassNameStrategyDescriptionPanel());
	}
	public StrategyPanelHelper createStrategyPanelHelper()
	{
	    return createClass(getClassNameStrategyPanelHelper());
	}
	public StrategySelectionPanel createStrategySelectionPanel()
	{
	    return createClass(getClassNameStrategySelectionPanel());
	}
	public StrategyUI createStrategyUI()
	{
	    return createClass(getClassNameStrategyUI());
	}
	public TypeConverterFactory createTypeConverterFactory()
	{
	    return createClass(getClassNameTypeConverterFactory());
	}
	
	public void testClassLoaders()
	{
        	createAtdl4jWidgetForCheckBoxListT();
        	createAtdl4jWidgetForCheckBoxT();
        	createAtdl4jWidgetForClockT();
        	createAtdl4jWidgetForDoubleSpinnerT();
        	createAtdl4jWidgetForDropDownListT();
        	createAtdl4jWidgetForEditableDropDownListT();
        	createAtdl4jWidgetForHiddenFieldT();
        	createAtdl4jWidgetForLabelT();
        	createAtdl4jWidgetForMultiSelectListT();
        	createAtdl4jWidgetForRadioButtonListT();
        	createAtdl4jWidgetForRadioButtonT();
        	createAtdl4jWidgetForSingleSelectListT();
        	createAtdl4jWidgetForSingleSpinnerT();
        	createAtdl4jWidgetForSliderT();
        	createAtdl4jWidgetForTextFieldT();
        	createAtdl4jCompositePanel();
        	createAtdl4jInputAndFilterDataPanel();
        	createAtdl4jInputAndFilterDataSelectionPanel();
        	createAtdl4jTesterPanel();
        	createAtdl4jUserMessageHandler();
        	createAtdl4jWidgetFactory();
        	createFixatdlFileSelectionPanel();
        	createFixMsgLoadPanel();
        	createStrategiesUI();
        	createStrategyDescriptionPanel();
        	createStrategyPanelHelper();
        	createStrategySelectionPanel();
        	createStrategyUI();
        	createTypeConverterFactory();
	}
}
