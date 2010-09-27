/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.PercentageT;
import org.atdl4j.fixatdl.core.QtyT;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.LabelT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.fixatdl.layout.TextFieldT;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.ControlUIFactory;
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

/**
 * Typical setup (for class named XXX):
 * 	private String classNameXXX;
 * 	public void setClassNameXXX(String);
 * 	public String getClassNameXXX();
 *    abstract protected String getDefaultClassNameXXX();
 *    public XXX getXXX() throws ;
 * 	add to constructor:  setClassNameXXX( getDefaultClassNameXXX() );
 * 	NOTE:  add public XXX getXXX() to Atdl4jConfig
 * 	NOTE:  implement protected String getDefaultClassNameXXX(); within derived classes
 * 
 * Note that Class.forName()'s InstantiationException, IllegalAccessException, ClassNotFoundException will be caught and 
 * handled as a run-time error (IllegalStateException)
 * 
 * This class contains the data associated with the <code>AbstractAtdl4jConfig</code>.
 * 
 * Creation date: (Feb 7, 2010 6:12:35 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public abstract class AbstractAtdl4jConfig
	implements Atdl4jConfig
{
	private final Logger logger = Logger.getLogger(AbstractAtdl4jConfig.class);

	public static String ATDL4J_PACKAGE_NAME_PATH_FOR_DEBUG_LOGGING = "org.atdl4j";
//TODO 9/26/2010 Scott Atwell	public static String DEFAULT_CLASS_NAME_STRATEGIES_UI_FACTORY = "org.atdl4j.ui.impl.BaseStrategiesUIFactory";
	public static String DEFAULT_CLASS_NAME_CONTROL_UI_FACTORY = "org.atdl4j.ui.impl.BaseControlUIFactory";
	public static String DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY = "org.atdl4j.data.TypeConverterFactory";
	
	
	// -- UI Infrastructure --
//TODO 9/26/2010 Scott Atwell	private String classNameStrategiesUIFactory;
//TODO 9/26/2010 Scott Atwell	private StrategiesUIFactory strategiesUIFactory;
	private TypeConverterFactory typeConverterFactory;
	private StrategyPanelHelper strategyPanelHelper;
	
	private String classNameStrategiesUI;
	private StrategiesUI strategiesUI;

	private String classNameStrategyUI;

	private String classNameControlUIFactory;
	private ControlUIFactory controlUIFactory;

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
	private Atdl4jTesterPanel atdl4jTesterPanel;
	private String classNameAtdl4jInputAndFilterDataSelectionPanel;
	private Atdl4jInputAndFilterDataSelectionPanel atdl4jInputAndFilterDataSelectionPanel;
	private String classNameAtdl4jInputAndFilterDataPanel;
	private Atdl4jInputAndFilterDataPanel atdl4jInputAndFilterDataPanel;
	private String classNameAtdl4jCompositePanel;
	private Atdl4jCompositePanel atdl4jCompositePanel;
	private String classNameAtdl4jUserMessageHandler;
	private Atdl4jUserMessageHandler atdl4jUserMessageHandler;
	private String classNameFixatdlFileSelectionPanel;
	private FixatdlFileSelectionPanel fixatdlFileSelectionPanel;
	private String classNameFixMsgLoadPanel;
	private FixMsgLoadPanel fixMsgLoadPanel;
	private String classNameStrategySelectionPanel;
	private StrategySelectionPanel strategySelectionPanel;
	private String classNameStrategyDescriptionPanel;
	private StrategyDescriptionPanel strategyDescriptionPanel;
	
	
	private InputAndFilterData inputAndFilterData;
	
	private boolean showStrategyDescription = true;
	private boolean showTimezoneSelector = false;
	private boolean showFileSelectionSection = true;
	private boolean showValidateOutputSection = true;
	private boolean showCompositePanelOkCancelButtonSection = true;
	private Integer strategyDropDownItemDepth = new Integer( 15 );  // ComboBox drop down 'depth' (aka VisibleItemCount)
	private boolean selectedStrategyValidated = false;
	
// 6/23/2010 Scott Atwell	private boolean usePreCachedStrategyPanels = true;

	private boolean treatControlVisibleFalseAsNull = false;
	private boolean treatControlEnabledFalseAsNull = false;	
	private boolean restoreLastNonNullStateControlValueBehavior = true;	
// 8/15/2010 Scott Atwell added
	private boolean accommodateMixOfStrategyPanelsAndControls = false;  // FIXatdl 1.1 spec recommends against vs. prohibits
	
	private boolean showEnabledCheckboxOnOptionalClockControl = false;
	
	private int defaultDigitsForSpinnerControlForPercentage = 0;
	private int defaultDigitsForSpinnerControlForQty = 0;
	private int defaultDigitsForSpinnerControl = 2;

//TODO 9/27/2010 Scott Atwell removed	private StrategiesT strategies;
// 6/23/2010 Scott Atwell	private Map<StrategyT, StrategyUI> strategyUIMap;
	private StrategyT selectedStrategy;
	
	private boolean catchAllStrategyLoadExceptions  = false;
	private boolean catchAllValidationExceptions  = false;
	private boolean catchAllRuntimeExceptions  = false;
	private boolean catchAllMainlineExceptions  = false;

//TODO 9/26/2010 Scott Atwell	protected String getDefaultClassNameStrategiesUIFactory()
//	{
//		return DEFAULT_CLASS_NAME_STRATEGIES_UI_FACTORY;
//	}
	
	protected String getDefaultClassNameControlUIFactory()
	{ 
		return DEFAULT_CLASS_NAME_CONTROL_UI_FACTORY;
	}
	
	protected String getDefaultClassNameTypeConverterFactory()
	{ 
		return DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY;
	}
	
// 7/7/2010 Scott Atwell changed to null	private BigDecimal defaultIncrementValue = new BigDecimal( "1.0" );
	private BigDecimal defaultIncrementValue = null;
	private BigDecimal defaultLotSizeIncrementValue = new BigDecimal( "1.0" );
// 7/6/2010 Scott Atwell	private BigDecimal defaultTickIncrementValue = new BigDecimal( "1.0" );
	private BigDecimal defaultTickIncrementValue = new BigDecimal( "0.0001" );

	// -- Controls Clock control's behavior when FIX message timestamp (eg "StartTime" or "EffectiveTime") is older than current time --
	private Integer clockStartTimeSetFIXValueWithPastTimeRule = CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT;
	private Integer clockEndTimeSetFIXValueWithPastTimeRule = CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL;
	private Integer clockUnknownSetFIXValueWithPastTimeRule = CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS;
	// -- Used by isClockControlStartTime() and isClockControlEndTime() to check aControl.getID() to see if it contains any of the String "IDValueFragments" within these lists --
	private String[] clockControlStartTimeIDValueFragmentList = new String[]{ "Start", "Effective", "Begin" };
	private String[] clockControlEndTimeIDValueFragmentList = new String[]{ "End", "Expire", "Stop" };
	
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
	public AbstractAtdl4jConfig()
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
	 * @param aStrategy
	 * @return
	 */
	public StrategyUI getStrategyUI(StrategyT aStrategy)
	{
// 9/26/2010 Scott Atwell		return getStrategiesPanel().getStrategyUI(aStrategy);
// 9/27/2010 Scott Atwell		return getStrategiesUI( getStrategies() ).getStrategyUI(aStrategy);
		return getStrategiesUI().getStrategyUI(aStrategy);
	}

	/**
	 * Constructs a new instance every call.
	 * 
	 * @param strategies
	 * @return
	 */
/** TODO 9/27/2010 Scott Atwell	
	public StrategiesUI getStrategiesUI(StrategiesT strategies)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameStrategiesUI();
		logger.debug( "getStrategiesUI() loading class named: " + tempClassName );
		StrategiesUI strategiesUI;
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
			strategiesUI.init( strategies, this );
		}
		
		return strategiesUI;
	}
**/	
	/**
	 * @return
	 */
	public StrategiesUI getStrategiesUI()
	{
		if ( ( strategiesUI == null ) && ( getClassNameStrategiesUI() != null ) )
		{
			// -- Constructs a new instance every call --
			String tempClassName = getClassNameStrategiesUI();
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
				strategiesUI.init( this );
			}
		}
		
		return strategiesUI;
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
	 * Constructs a new instance every call.
	 * 
	 * @param strategy
	 * @param aStrategies
	 * @param strategiesRules
	 * @param parentContainer (for SWT: should be swt.Composite)
	 * @return
	 */
// 9/27/2010 Scott Atwell added StrategiesT	public StrategyUI getStrategyUI(StrategyT strategy, Map<String, ValidationRule> strategiesRules, Object parentContainer)
	public StrategyUI getStrategyUI(StrategyT strategy, StrategiesT aStrategies, Map<String, ValidationRule> strategiesRules, Object parentContainer)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameStrategyUI();
		logger.debug( "getStrategyUI() loading class named: " + tempClassName );
		StrategyUI strategyUI;
		try
		{
			strategyUI = ((Class<StrategyUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( strategyUI != null )
		{
// 9/27/2010 Scott Atwell			strategyUI.init( strategy, this, strategiesRules, parentContainer );
			strategyUI.init( strategy, aStrategies, this, strategiesRules, parentContainer );
		}
		
		return strategyUI;
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
	 * Constructs a new instance every call.
	 * 
	 * @return
	 */
/** TODO 9/27/2010 Scott Atwell rewrote using local instance variable	
	public ControlUIFactory getControlUIFactory() 
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIFactory();
		logger.debug( "getControlUIFactory() loading class named: " + tempClassName );
		ControlUIFactory controlUIFactory;
		try
		{
			controlUIFactory = ((Class<ControlUIFactory>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIFactory != null )
		{
			controlUIFactory.init( this );
		}
		
		return controlUIFactory;
	}
**/
	public ControlUIFactory getControlUIFactory() 
	{
		if ( ( controlUIFactory == null ) && ( getClassNameControlUIFactory() != null ) ) 
		{
			// -- Constructs a new instance every call --
			String tempClassName = getClassNameControlUIFactory();
			logger.debug( "getControlUIFactory() loading class named: " + tempClassName );
			try
			{
				controlUIFactory = ((Class<ControlUIFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIFactory != null )
			{
				controlUIFactory.init( this );
			}
		}
		
		return controlUIFactory;
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
	 * @return
	 */
	public TypeConverterFactory getTypeConverterFactory() 
	{
		if ( ( typeConverterFactory == null ) && ( getClassNameTypeConverterFactory() != null ) )
		{
			String tempClassName = getClassNameTypeConverterFactory();
			logger.debug( "getTypeConverterFactory() loading class named: " + tempClassName );
			TypeConverterFactory typeConverterFactory;
			try
			{
				typeConverterFactory = ((Class<TypeConverterFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return typeConverterFactory;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForCheckBoxT(CheckBoxT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForCheckBoxT();
		logger.debug( "getControlUIForCheckBoxT() loading class named: " + tempClassName );
		ControlUI controlUIForCheckBoxT;
		try
		{
			controlUIForCheckBoxT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForCheckBoxT != null )
		{
			controlUIForCheckBoxT.init( control, parameter, this );
		}
		
		return controlUIForCheckBoxT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForDropDownListT(DropDownListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForDropDownListT();
		logger.debug( "getControlUIForDropDownListT() loading class named: " + tempClassName );
		ControlUI controlUIForDropDownListT;
		try
		{
			controlUIForDropDownListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForDropDownListT != null )
		{
			controlUIForDropDownListT.init( control, parameter, this );
		}
		
		return controlUIForDropDownListT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForEditableDropDownListT(EditableDropDownListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForEditableDropDownListT();
		logger.debug( "getControlUIForEditableDropDownListT() loading class named: " + tempClassName );
		ControlUI controlUIForEditableDropDownListT;
		try
		{
			controlUIForEditableDropDownListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForEditableDropDownListT != null )
		{
			controlUIForEditableDropDownListT.init( control, parameter, this );
		}
		
		return controlUIForEditableDropDownListT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForRadioButtonListT(RadioButtonListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForRadioButtonListT();
		logger.debug( "getControlUIForRadioButtonListT() loading class named: " + tempClassName );
		ControlUI controlUIForRadioButtonListT;
		try
		{
			controlUIForRadioButtonListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForRadioButtonListT != null )
		{
			controlUIForRadioButtonListT.init( control, parameter, this );
		}
		
		return controlUIForRadioButtonListT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForTextFieldT(TextFieldT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForTextFieldT();
		logger.debug( "getControlUIForTextFieldT() loading class named: " + tempClassName );
		ControlUI controlUIForTextFieldT;
		try
		{
			controlUIForTextFieldT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForTextFieldT != null )
		{
			controlUIForTextFieldT.init( control, parameter, this );
		}
		
		return controlUIForTextFieldT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForSliderT(SliderT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForSliderT();
		logger.debug( "getControlUIForSliderT() loading class named: " + tempClassName );
		ControlUI controlUIForSliderT;
		try
		{
			controlUIForSliderT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForSliderT != null )
		{
			controlUIForSliderT.init( control, parameter, this );
		}
		
		return controlUIForSliderT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForCheckBoxListT(CheckBoxListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForCheckBoxListT();
		logger.debug( "getControlUIForCheckBoxListT() loading class named: " + tempClassName );
		ControlUI controlUIForCheckBoxListT;
		try
		{
			controlUIForCheckBoxListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForCheckBoxListT != null )
		{
			controlUIForCheckBoxListT.init( control, parameter, this );
		}
		
		return controlUIForCheckBoxListT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForClockT(ClockT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForClockT();
		logger.debug( "getControlUIForClockT() loading class named: " + tempClassName );
		ControlUI controlUIForClockT;
		try
		{
			controlUIForClockT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForClockT != null )
		{
			controlUIForClockT.init( control, parameter, this );
		}
		
		return controlUIForClockT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForSingleSpinnerT(SingleSpinnerT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForSingleSpinnerT();
		logger.debug( "getControlUIForSingleSpinnerT() loading class named: " + tempClassName );
		ControlUI controlUIForSingleSpinnerT;
		try
		{
			controlUIForSingleSpinnerT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForSingleSpinnerT != null )
		{
			controlUIForSingleSpinnerT.init( control, parameter, this );
		}
		
		return controlUIForSingleSpinnerT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForDoubleSpinnerT();
		logger.debug( "getControlUIForDoubleSpinnerT() loading class named: " + tempClassName );
		ControlUI controlUIForDoubleSpinnerT;
		try
		{
			controlUIForDoubleSpinnerT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForDoubleSpinnerT != null )
		{
			controlUIForDoubleSpinnerT.init( control, parameter, this );
		}
		
		return controlUIForDoubleSpinnerT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForSingleSelectListT(SingleSelectListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForSingleSelectListT();
		logger.debug( "getControlUIForSingleSelectListT() loading class named: " + tempClassName );
		ControlUI controlUIForSingleSelectListT;
		try
		{
			controlUIForSingleSelectListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForSingleSelectListT != null )
		{
			controlUIForSingleSelectListT.init( control, parameter, this );
		}
		
		return controlUIForSingleSelectListT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForMultiSelectListT(MultiSelectListT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForMultiSelectListT();
		logger.debug( "getControlUIForMultiSelectListT() loading class named: " + tempClassName );
		ControlUI controlUIForMultiSelectListT;
		try
		{
			controlUIForMultiSelectListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForMultiSelectListT != null )
		{
			controlUIForMultiSelectListT.init( control, parameter, this );
		}
		
		return controlUIForMultiSelectListT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForHiddenFieldT(HiddenFieldT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForHiddenFieldT();
		logger.debug( "getControlUIForHiddenFieldT() loading class named: " + tempClassName );
		ControlUI controlUIForHiddenFieldT;
		try
		{
			controlUIForHiddenFieldT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForHiddenFieldT != null )
		{
			controlUIForHiddenFieldT.init( control, parameter, this );
		}
		
		return controlUIForHiddenFieldT;
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
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForLabelT(LabelT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForLabelT();
		logger.debug( "getControlUIForLabelT() loading class named: " + tempClassName );
		ControlUI controlUIForLabelT;
		try
		{
			controlUIForLabelT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForLabelT != null )
		{
			controlUIForLabelT.init( control, parameter, this );
		}
		
		return controlUIForLabelT;
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
	
	/**
	 * Constructs a new instance every call.
	 * 
	 * @param control
	 * @param parameter
	 * @return
	 */
	public ControlUI getControlUIForRadioButtonT(RadioButtonT control, ParameterT parameter)
	{
		// -- Constructs a new instance every call --
		String tempClassName = getClassNameControlUIForRadioButtonT();
		logger.debug( "getControlUIForRadioButtonT() loading class named: " + tempClassName );
		ControlUI controlUIForRadioButtonT;
		try
		{
			controlUIForRadioButtonT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
		}
		catch ( Exception e )
		{
			logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
			throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
		}
		
		if ( controlUIForRadioButtonT != null )
		{
			controlUIForRadioButtonT.init( control, parameter, this );
		}
		
		return controlUIForRadioButtonT;
	}

	
	/**
	 * @param inputAndFilterData the inputAndFilterData to set
	 */
	public void setInputAndFilterData(InputAndFilterData inputAndFilterData)
	{
		this.inputAndFilterData = inputAndFilterData;
	}

	/**
	 * @return the inputAndFilterData
	 */
	public InputAndFilterData getInputAndFilterData()
	{
		return inputAndFilterData;
	}

	/**
	 * @param strategyUIMap the strategyUIMap to set
	 */
// 6/23/2010 Scott Atwell	public void setStrategyUIMap(Map<StrategyT, StrategyUI> strategyUIMap)
//	{
//		this.strategyUIMap = strategyUIMap;
//	}

	/**
	 * @return the strategyUIMap
	 */
// 6/23/2010 Scott Atwell	public Map<StrategyT, StrategyUI> getStrategyUIMap()
//	{
//		return strategyUIMap;
//	}

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
	 * @param showStrategyDescription the showStrategyDescription to set
	 */
	public void setShowStrategyDescription(boolean showStrategyDescription)
	{
		this.showStrategyDescription = showStrategyDescription;
	}

	/**
	 * @return the showStrategyDescription
	 */
	public boolean isShowStrategyDescription()
	{
		return showStrategyDescription;
	}

	/**
	 * @param showTimezoneSelector the showTimezoneSelector to set
	 */
	public void setShowTimezoneSelector(boolean showTimezoneSelector)
	{
		this.showTimezoneSelector = showTimezoneSelector;
	}

	/**
	 * @return the showTimezoneSelector
	 */
	public boolean isShowTimezoneSelector()
	{
		return showTimezoneSelector;
	}

	/**
	 * @param showFileSelectionSection the showFileSelectionSection to set
	 */
	public void setShowFileSelectionSection(boolean showFileSelectionSection)
	{
		this.showFileSelectionSection = showFileSelectionSection;
	}

	/**
	 * @return the showFileSelectionSection
	 */
	public boolean isShowFileSelectionSection()
	{
		return showFileSelectionSection;
	}

	/**
	 * @param showValidateOutputSection the showValidateOutputSection to set
	 */
	public void setShowValidateOutputSection(boolean showValidateOutputSection)
	{
		this.showValidateOutputSection = showValidateOutputSection;
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
	 * @return the showCompositePanelOkCancelButtonSection
	 */
	public boolean isShowCompositePanelOkCancelButtonSection()
	{
		return showCompositePanelOkCancelButtonSection;
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
	 * @param strategySelectionPanel the strategySelectionPanel to set
	 */
	public void setStrategySelectionPanel(StrategySelectionPanel strategySelectionPanel)
	{
		this.strategySelectionPanel = strategySelectionPanel;
	}

	/**
	 * @return the StrategySelectionPanel
	 */
	public StrategySelectionPanel getStrategySelectionPanel() 
	{
		if ( ( strategySelectionPanel == null ) && ( getClassNameStrategySelectionPanel() != null ) )
		{
			String tempClassName = getClassNameStrategySelectionPanel();
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
	 * @param strategyDescriptionPanel the strategyDescriptionPanel to set
	 */
	public void setStrategyDescriptionPanel(StrategyDescriptionPanel strategyDescriptionPanel)
	{
		this.strategyDescriptionPanel = strategyDescriptionPanel;
	}

	/**
	 * @return the StrategyDescriptionPanel
	 */
	public StrategyDescriptionPanel getStrategyDescriptionPanel() 
	{
		if ( ( strategyDescriptionPanel == null ) && ( getClassNameStrategyDescriptionPanel() != null ) )
		{
			String tempClassName = getClassNameStrategyDescriptionPanel();
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
	 * @param aFixatdlFileSelectionPanel the aFixatdlFileSelectionPanel to set
	 */
	public void setFixatdlFileSelectionPanel(FixatdlFileSelectionPanel aFixatdlFileSelectionPanel)
	{
		this.fixatdlFileSelectionPanel = aFixatdlFileSelectionPanel;
	}

	/**
	 * @return the FixatdlFileSelectionPanel
	 */
	public FixatdlFileSelectionPanel getFixatdlFileSelectionPanel() 
	{
		if ( ( fixatdlFileSelectionPanel == null ) && ( getClassNameFixatdlFileSelectionPanel() != null ) )
		{
			String tempClassName = getClassNameFixatdlFileSelectionPanel();
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
	 * @param aFixMsgLoadPanel the aFixMsgLoadPanel to set
	 */
	public void setFixMsgLoadPanel(FixMsgLoadPanel aFixMsgLoadPanel)
	{
		this.fixMsgLoadPanel = aFixMsgLoadPanel;
	}

	/**
	 * @return the FixMsgLoadPanel
	 */
	public FixMsgLoadPanel getFixMsgLoadPanel() 
	{
		if ( ( fixMsgLoadPanel == null ) && ( getClassNameFixMsgLoadPanel() != null ) )
		{
			String tempClassName = getClassNameFixMsgLoadPanel();
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
		getAtdl4jUserMessageHandler().init(  parentOrShell, this );
	}

	/**
	 * @return the Atdl4jUserMessageHandler
	 */
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler() 
	{
		if ( ( atdl4jUserMessageHandler == null ) && ( getClassNameAtdl4jUserMessageHandler() != null ) )
		{
			String tempClassName = getClassNameAtdl4jUserMessageHandler();
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
	 * @param atdl4jTesterPanel the atdl4jTesterPanel to set
	 */
	public void setAtdl4jTesterPanel(Atdl4jTesterPanel atdl4jTesterPanel)
	{
		this.atdl4jTesterPanel = atdl4jTesterPanel;
	}

	/**
	 * @return the Atdl4jTesterPanel
	 */
	public Atdl4jTesterPanel getAtdl4jTesterPanel() 
	{
		if ( ( atdl4jTesterPanel == null ) && ( getClassNameAtdl4jTesterPanel() != null ) )
		{
			String tempClassName = getClassNameAtdl4jTesterPanel();
			logger.debug( "getAtdl4jTesterPanel() loading class named: " + tempClassName );
			try
			{
				atdl4jTesterPanel = ((Class<Atdl4jTesterPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jTesterPanel;
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
	 * @param atdl4jInputAndFilterDataPanel the atdl4jInputAndFilterDataPanel to set
	 */
	public void setAtdl4jInputAndFilterDataPanel(Atdl4jInputAndFilterDataPanel atdl4jInputAndFilterDataPanel)
	{
		this.atdl4jInputAndFilterDataPanel = atdl4jInputAndFilterDataPanel;
	}

	/**
	 * @return the Atdl4jInputAndFilterDataPanel
	 */
	public Atdl4jInputAndFilterDataPanel getAtdl4jInputAndFilterDataPanel() 
	{
		if ( ( atdl4jInputAndFilterDataPanel == null ) && ( getClassNameAtdl4jInputAndFilterDataPanel() != null ) )
		{
			String tempClassName = getClassNameAtdl4jInputAndFilterDataPanel();
			logger.debug( "getAtdl4jInputAndFilterDataPanel() loading class named: " + tempClassName );
			try
			{
				atdl4jInputAndFilterDataPanel = ((Class<Atdl4jInputAndFilterDataPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jInputAndFilterDataPanel;
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
	 * @param atdl4jInputAndFilterDataSelectionPanel the atdl4jInputAndFilterDataSelectionPanel to set
	 */
	public void setAtdl4jInputAndFilterDataSelectionPanel(Atdl4jInputAndFilterDataSelectionPanel atdl4jInputAndFilterDataSelectionPanel)
	{
		this.atdl4jInputAndFilterDataSelectionPanel = atdl4jInputAndFilterDataSelectionPanel;
	}

	/**
	 * @return the Atdl4jInputAndFilterDataSelectionPanel
	 */
	public Atdl4jInputAndFilterDataSelectionPanel getAtdl4jInputAndFilterDataSelectionPanel() 
	{
		if ( ( atdl4jInputAndFilterDataSelectionPanel == null ) && ( getClassNameAtdl4jInputAndFilterDataSelectionPanel() != null ) )
		{
			String tempClassName = getClassNameAtdl4jInputAndFilterDataSelectionPanel();
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
	 * @param atdl4jCompositePanel the atdl4jCompositePanel to set
	 */
	public void setAtdl4jCompositePanel(Atdl4jCompositePanel atdl4jCompositePanel)
	{
		this.atdl4jCompositePanel = atdl4jCompositePanel;
	}

	/**
	 * @return the Atdl4jCompositePanel
	 */
	public Atdl4jCompositePanel getAtdl4jCompositePanel() 
	{
		if ( ( atdl4jCompositePanel == null ) && ( getClassNameAtdl4jCompositePanel() != null ) )
		{
			String tempClassName = getClassNameAtdl4jCompositePanel();
			logger.debug( "getAtdl4jCompositePanel() loading class named: " + tempClassName );
			try
			{
				atdl4jCompositePanel = ((Class<Atdl4jCompositePanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jCompositePanel;
	}

	/**
	 * @return the treatControlVisibleFalseAsNull
	 */
	public boolean isTreatControlVisibleFalseAsNull()
	{
		return this.treatControlVisibleFalseAsNull;
	}

	/**
	 * @param aTreatControlVisibleFalseAsNull the treatControlVisibleFalseAsNull to set
	 */
	public void setTreatControlVisibleFalseAsNull(boolean aTreatControlVisibleFalseAsNull)
	{
		this.treatControlVisibleFalseAsNull = aTreatControlVisibleFalseAsNull;
	}

	/**
	 * @return the treatControlEnabledFalseAsNull
	 */
	public boolean isTreatControlEnabledFalseAsNull()
	{
		return this.treatControlEnabledFalseAsNull;
	}

	/**
	 * @param aTreatControlEnabledFalseAsNull the treatControlEnabledFalseAsNull to set
	 */
	public void setTreatControlEnabledFalseAsNull(boolean aTreatControlEnabledFalseAsNull)
	{
		this.treatControlEnabledFalseAsNull = aTreatControlEnabledFalseAsNull;
	}

	/**
	 * @return the showEnabledCheckboxOnOptionalClockControl
	 */
	public boolean isShowEnabledCheckboxOnOptionalClockControl()
	{
		return this.showEnabledCheckboxOnOptionalClockControl;
	}

	/**
	 * @param aShowEnabledCheckboxOnOptionalClockControl the showEnabledCheckboxOnOptionalClockControl to set
	 */
	public void setShowEnabledCheckboxOnOptionalClockControl(boolean aShowEnabledCheckboxOnOptionalClockControl)
	{
		this.showEnabledCheckboxOnOptionalClockControl = aShowEnabledCheckboxOnOptionalClockControl;
	}

	/**
	 * @return the restoreLastNonNullStateControlValueBehavior
	 */
	public boolean isRestoreLastNonNullStateControlValueBehavior()
	{
		return this.restoreLastNonNullStateControlValueBehavior;
	}

	/**
	 * @param aRestoreLastNonNullStateControlValueBehavior the restoreLastNonNullStateControlValueBehavior to set
	 */
	public void setRestoreLastNonNullStateControlValueBehavior(boolean aRestoreLastNonNullStateControlValueBehavior)
	{
		this.restoreLastNonNullStateControlValueBehavior = aRestoreLastNonNullStateControlValueBehavior;
	}

	/**
	 * @return the strategyDropDownItemDepth
	 */
	public Integer getStrategyDropDownItemDepth()
	{
		return this.strategyDropDownItemDepth;
	}

	/**
	 * @param aStrategyDropDownItemDepth the strategyDropDownItemDepth to set
	 */
	public void setStrategyDropDownItemDepth(Integer aStrategyDropDownItemDepth)
	{
		this.strategyDropDownItemDepth = aStrategyDropDownItemDepth;
	}

	/**
	 * @return the selectedStrategyValidated
	 */
	public boolean isSelectedStrategyValidated()
	{
		return this.selectedStrategyValidated;
	}

	/**
	 * @param aSelectedStrategyValidated the selectedStrategyValidated to set
	 */
	public void setSelectedStrategyValidated(boolean aSelectedStrategyValidated)
	{
		this.selectedStrategyValidated = aSelectedStrategyValidated;
	}

	/**
	 * @return the usePreCachedStrategyPanels
	 */
// 6/23/2010 Scott Atwell	public boolean isUsePreCachedStrategyPanels()
//	{
//		return this.usePreCachedStrategyPanels;
//	}

	/**
	 * @param aUsePreCachedStrategyPanels the usePreCachedStrategyPanels to set
	 */
// 6/23/2010 Scott Atwell	public void setUsePreCachedStrategyPanels(boolean aUsePreCachedStrategyPanels)
//	{
//		this.usePreCachedStrategyPanels = aUsePreCachedStrategyPanels;
//	}
	
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
	 * @return the catchAllRuntimeExceptions
	 */
	public boolean isCatchAllRuntimeExceptions()
	{
		return this.catchAllRuntimeExceptions;
	}
	
	/**
	 * @return the catchAllMainlineExceptions
	 */
	public boolean isCatchAllMainlineExceptions()
	{
		return this.catchAllMainlineExceptions;
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
	 * @param aCatchAllRuntimeExceptions the catchAllRuntimeExceptions to set
	 */
	public void setCatchAllRuntimeExceptions(boolean aCatchAllRuntimeExceptions)
	{
		this.catchAllRuntimeExceptions = aCatchAllRuntimeExceptions;
	}
	
	/**
	 * @param aCatchAllMainlineExceptions the catchAllMainlineExceptions to set
	 */
	public void setCatchAllMainlineExceptions(boolean aCatchAllMainlineExceptions)
	{
		this.catchAllMainlineExceptions = aCatchAllMainlineExceptions;
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
	
	/* 
	 * Returns the Spinner control's "digits" value for the specified aParameter (eg 0 vs. 2 for Percentage)
	 */
	public int getDefaultDigitsForSpinnerControl( ParameterT aParameter )
	{
		if ( aParameter != null )
		{
			if ( aParameter instanceof PercentageT )
			{
				return getDefaultDigitsForSpinnerControlForPercentage();
			}
			else if ( aParameter instanceof QtyT )
			{
				return getDefaultDigitsForSpinnerControlForQty();
			}			
// use Atdl4jConfig.getDefaultDigitsForSpinnerControl() for these			
//			else if ( aParameter instanceof FloatT )
//			{
//			}
//			else if ( aParameter instanceof AmtT )
//			{
//			}
//			else if ( aParameter instanceof PriceOffsetT )
//			{
//			}
//			else if ( aParameter instanceof PriceT )
//			{
//			}
		}
		
		// -- not specified via rule above, use default if we have one within Atdl4jConfig --
		return getDefaultDigitsForSpinnerControl();
	}

	/**
	 * @return the defaultDigitsForSpinnerControlForPercentage
	 */
	protected int getDefaultDigitsForSpinnerControlForPercentage()
	{
		return this.defaultDigitsForSpinnerControlForPercentage;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControlForPercentage the defaultDigitsForSpinnerControlForPercentage to set
	 */
	protected void setDefaultDigitsForSpinnerControlForPercentage(int aDefaultDigitsForSpinnerControlForPercentage)
	{
		this.defaultDigitsForSpinnerControlForPercentage = aDefaultDigitsForSpinnerControlForPercentage;
	}

	/**
	 * @return the defaultDigitsForSpinnerControlForQty
	 */
	protected int getDefaultDigitsForSpinnerControlForQty()
	{
		return this.defaultDigitsForSpinnerControlForQty;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControlForQty the defaultDigitsForSpinnerControlForQty to set
	 */
	protected void setDefaultDigitsForSpinnerControlForQty(int aDefaultDigitsForSpinnerControlForQty)
	{
		this.defaultDigitsForSpinnerControlForQty = aDefaultDigitsForSpinnerControlForQty;
	}

	/**
	 * @return the defaultDigitsForSpinnerControl
	 */
	protected int getDefaultDigitsForSpinnerControl()
	{
		return this.defaultDigitsForSpinnerControl;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControl the defaultDigitsForSpinnerControl to set
	 */
	protected void setDefaultDigitsForSpinnerControl(int aDefaultDigitsForSpinnerControl)
	{
		this.defaultDigitsForSpinnerControl = aDefaultDigitsForSpinnerControl;
	}
	
	/**
	 * Uses, if specified, InputAndFilterData.getInputStrategyNameList() 
	 * and InputAndFilterData.getApplyInputStrategyNameListAsFilter() 
	 * to control the order presented to the user and, if so desired, exclude strategies against the available aStrategyList
	 * 
	 * @param aStrategyList
	 * @return
	 */
	public List<StrategyT> getStrategyListUsingInputStrategyNameListFilter( List<StrategyT> aStrategyList )
	{
		if ( aStrategyList == null )
		{
			return null;
		}

		if ( ( getInputAndFilterData() != null ) &&
			  ( getInputAndFilterData().getInputStrategyNameList() != null ) && 
			  ( getInputAndFilterData().getInputStrategyNameList().size() > 0  ) )
		{
			List<StrategyT> tempAvailableStrategyList = new ArrayList<StrategyT>();

			// -- Add the strategies according to their order in the specified InputStrategyNameList --
			for ( String tempStrategyName : getInputAndFilterData().getInputStrategyNameList() )
			{
				for ( StrategyT tempStrategy : aStrategyList )
				{
					if ( tempStrategyName.equals(  tempStrategy.getName() ) )
					{
						// -- Strategy is in the InputStrategyNameList --
						tempAvailableStrategyList.add( tempStrategy );
					}
				}				
			}
			
			// -- Add any other strategyNames (in their order within the FIXatdl file) to the end unless setting specifies the input list is a filter --
			if ( ! Boolean.TRUE.equals( getInputAndFilterData().getApplyInputStrategyNameListAsFilter() ) )
			{
				for ( StrategyT tempStrategy : aStrategyList )
				{
					if ( ! tempAvailableStrategyList.contains( tempStrategy ) )
					{
						tempAvailableStrategyList.add( tempStrategy );
					}
				}
			}
			
			logger.debug("getStrategyListUsingInputStrategyNameListFilter() returning: " + tempAvailableStrategyList);
			return tempAvailableStrategyList;
		}
		else
		{
			// -- Return the original list unfiltered --
			return aStrategyList;
		}
	}

	/**
	 * @return the defaultIncrementValue
	 */
	public BigDecimal getDefaultIncrementValue()
	{
		return this.defaultIncrementValue;
	}

	/**
	 * @param aDefaultIncrementValue the defaultIncrementValue to set
	 */
	public void setDefaultIncrementValue(BigDecimal aDefaultIncrementValue)
	{
		this.defaultIncrementValue = aDefaultIncrementValue;
	}

	/**
	 * @return the defaultLotSizeIncrementValue
	 */
	public BigDecimal getDefaultLotSizeIncrementValue()
	{
		return this.defaultLotSizeIncrementValue;
	}

	/**
	 * @param aDefaultLotSizeIncrementValue the defaultLotSizeIncrementValue to set
	 */
	public void setDefaultLotSizeIncrementValue(BigDecimal aDefaultLotSizeIncrementValue)
	{
		this.defaultLotSizeIncrementValue = aDefaultLotSizeIncrementValue;
	}

	/**
	 * @return the defaultTickIncrementValue
	 */
	public BigDecimal getDefaultTickIncrementValue()
	{
		return this.defaultTickIncrementValue;
	}

	/**
	 * @param aDefaultTickIncrementValue the defaultTickIncrementValue to set
	 */
	public void setDefaultTickIncrementValue(BigDecimal aDefaultTickIncrementValue)
	{
		this.defaultTickIncrementValue = aDefaultTickIncrementValue;
	}

	/**
	 * @param clockStartTimeSetFIXValueWithPastTimeRule the clockStartTimeSetFIXValueWithPastTimeRule to set
	 */
	public void setClockStartTimeSetFIXValueWithPastTimeRule(Integer aClockPastTimeSetFIXValueRule)
	{
		this.clockStartTimeSetFIXValueWithPastTimeRule = aClockPastTimeSetFIXValueRule;
	}

	/**
	 * @return the clockStartTimeSetFIXValueWithPastTimeRule
	 */
	public Integer getClockStartTimeSetFIXValueWithPastTimeRule()
	{
		return clockStartTimeSetFIXValueWithPastTimeRule;
	}

	/**
	 * @param clockEndTimeSetFIXValueWithPastTimeRule the clockEndTimeSetFIXValueWithPastTimeRule to set
	 */
	public void setClockEndTimeSetFIXValueWithPastTimeRule(Integer aClockPastTimeSetFIXValueRule)
	{
		this.clockEndTimeSetFIXValueWithPastTimeRule = aClockPastTimeSetFIXValueRule;
	}

	/**
	 * @return the clockEndTimeSetFIXValueWithPastTimeRule
	 */
	public Integer getClockEndTimeSetFIXValueWithPastTimeRule()
	{
		return clockEndTimeSetFIXValueWithPastTimeRule;
	}

	/**
	 * @param clockUnknownSetFIXValueWithPastTimeRule the clockUnknownSetFIXValueWithPastTimeRule to set
	 */
	public void setClockUnknownSetFIXValueWithPastTimeRule(Integer aClockPastTimeSetFIXValueRule)
	{
		this.clockUnknownSetFIXValueWithPastTimeRule = aClockPastTimeSetFIXValueRule;
	}

	/**
	 * @return the clockUnknownSetFIXValueWithPastTimeRule
	 */
	public Integer getClockUnknownSetFIXValueWithPastTimeRule()
	{
		return clockUnknownSetFIXValueWithPastTimeRule;
	}

	/**
	 * 'Identifies' Clock controls (by string within Control/@ID) as a 'Start', 'End', or 'Unknown' control 
	 * and returns the appropriate getClock___SetFIXValueWithPastTimeRule() value
	 * @param aControl
	 * @return
	 */
	public Integer getClockPastTimeSetFIXValueRule( ControlT aControl )
	{
		if ( aControl == null )
		{
			throw new IllegalStateException( "aControl provided was null.");
		}
		
		if ( ( aControl instanceof ClockT ) == false )
		{
			throw new IllegalStateException( "aControl: " + aControl + " ID: " + aControl.getID() + " was not a ClockT" );
		}
		
		if ( isClockControlStartTime( aControl ) )
		{
			logger.debug( "aControl: " + aControl.getID() + " identified as 'StartTime'.  Returning: " + getClockStartTimeSetFIXValueWithPastTimeRule() );
			return getClockStartTimeSetFIXValueWithPastTimeRule();
		}
		else if ( isClockControlEndTime( aControl ) )
		{
			logger.debug( "aControl: " + aControl.getID() + " identified as 'EndTime'.  Returning: " + getClockEndTimeSetFIXValueWithPastTimeRule() );
			return getClockEndTimeSetFIXValueWithPastTimeRule();
		}
		else
		{
			logger.debug( "aControl: " + aControl.getID() + " WAS NOT identified as either 'StartTime' or 'EndTime'.  Returning: " + getClockUnknownSetFIXValueWithPastTimeRule() );
			return getClockUnknownSetFIXValueWithPastTimeRule();
		}
	}
	
	/**
	 * Checks aControl.getID() to see if it contains any of the String "IDValueFragments" within getClockControlStartTimeIDValueFragmentList()
	 * @param aControl
	 * @return
	 */
	protected boolean isClockControlStartTime( ControlT aControl )
	{
		if ( ( aControl != null ) && ( aControl instanceof ClockT ) )
		{
			String tempControlID = aControl.getID();
			
			if ( getClockControlStartTimeIDValueFragmentList() != null )
			{
				for ( String tempIDValueFragment : getClockControlStartTimeIDValueFragmentList() )
				{
					if ( tempControlID.contains( tempIDValueFragment ) )
					{
						logger.debug( "aControl: " + aControl.getID() + " identified as 'StartTime' via IDValueFragment: " + tempIDValueFragment );
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks aControl.getID() to see if it contains any of the String "IDValueFragments" within getClockControlEndTimeIDValueFragmentList()
	 * @param aControl
	 * @return
	 */
	protected boolean isClockControlEndTime( ControlT aControl )
	{
		if ( ( aControl != null ) && ( aControl instanceof ClockT ) )
		{
			String tempControlID = aControl.getID();
			
			if ( getClockControlEndTimeIDValueFragmentList() != null )
			{
				for ( String tempIDValueFragment : getClockControlEndTimeIDValueFragmentList() )
				{
					if ( tempControlID.contains( tempIDValueFragment ) )
					{
						logger.debug( "aControl: " + aControl.getID() + " identified as 'EndTime' via IDValueFragment: " + tempIDValueFragment );
						return true;
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * @return the clockControlStartTimeIDValueFragmentList
	 */
	public String[] getClockControlStartTimeIDValueFragmentList()
	{
		return this.clockControlStartTimeIDValueFragmentList;
	}

	/**
	 * @param aClockControlStartTimeIDValueFragmentList the clockControlStartTimeIDValueFragmentList to set
	 */
	public void setClockControlStartTimeIDValueFragmentList(String[] aClockControlStartTimeIDValueFragmentList)
	{
		this.clockControlStartTimeIDValueFragmentList = aClockControlStartTimeIDValueFragmentList;
	}

	/**
	 * @return the clockControlEndTimeIDValueFragmentList
	 */
	public String[] getClockControlEndTimeIDValueFragmentList()
	{
		return this.clockControlEndTimeIDValueFragmentList;
	}

	/**
	 * @param aClockControlEndTimeIDValueFragmentList the clockControlEndTimeIDValueFragmentList to set
	 */
	public void setClockControlEndTimeIDValueFragmentList(String[] aClockControlEndTimeIDValueFragmentList)
	{
		this.clockControlEndTimeIDValueFragmentList = aClockControlEndTimeIDValueFragmentList;
	}

	/**
	 * FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file.
	 *  
	 * @param accommodateMixOfStrategyPanelsAndControls the accommodateMixOfStrategyPanelsAndControls to set
	 */
	public void setAccommodateMixOfStrategyPanelsAndControls(boolean accommodateMixOfStrategyPanelsAndControls)
	{
		this.accommodateMixOfStrategyPanelsAndControls = accommodateMixOfStrategyPanelsAndControls;
	}

	/**
	 * FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file.
	 * 
	 * @return the accommodateMixOfStrategyPanelsAndControls
	 */
	public boolean isAccommodateMixOfStrategyPanelsAndControls()
	{
		return accommodateMixOfStrategyPanelsAndControls;
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
	 * @return
	 */
	public StrategyPanelHelper getStrategyPanelHelper()
	{
		if ( ( strategyPanelHelper == null ) && ( getClassNameStrategyPanelHelper() != null ) )
		{
			String tempClassName = getClassNameStrategyPanelHelper();
			logger.debug( "getStrategyPanelHelper() loading class named: " + tempClassName );
			try
			{
				strategyPanelHelper = ((Class<StrategyPanelHelper>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return strategyPanelHelper;
	}
}
