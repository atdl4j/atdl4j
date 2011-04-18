package org.atdl4j.ui.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.ParameterHelper;
import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.data.StrategyRuleset;
import org.atdl4j.data.TypeConverterFactoryConfig;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.fix.FIXMessageBuilder;
import org.atdl4j.data.fix.StringFIXMessageBuilder;
import org.atdl4j.data.validation.LengthValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.PatternValidationRule;
import org.atdl4j.data.validation.ReferencedValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.ObjectFactory;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.core.UseT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.fixatdl.validation.EditRefT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.fixatdl.validation.LogicOperatorT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.fixatdl.validation.StrategyEditT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.StrategyPanelHelper;
import org.atdl4j.ui.StrategyUI;

/**
 * Base class for ValidationRule.
 * 
 * @param <E>
 */
public abstract class AbstractStrategyUI 
	implements StrategyUI 
{
	protected static final Logger logger = Logger.getLogger( AbstractStrategyUI.class );
	
	protected Map<String, ParameterT> parameterMap;
	
	private Atdl4jOptions atdl4jOptions;
	
	// of StateListeners to attach to Atdl4jWidgets
	private StrategyRuleset strategyRuleset;
	
	private Map<String, ValidationRule> completeValidationRuleMap;

	protected StrategyT strategy;
	
	private StrategiesT strategies;
	
	Atdl4jWidgetFactory atdl4jWidgetFactory;
	StrategyPanelHelper strategyPanelHelper;
	
	abstract protected void buildAtdl4jWidgetMap( List<StrategyPanelT> aStrategyPanelList ) throws FIXatdlFormatException;
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of Atdl4jWidget<?> --
	abstract public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetMap();
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of Atdl4jWidget<?> --
	abstract public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetWithParameterMap();

	// -- Used by init() --
	abstract protected void initBegin(Object parentContainer);
	abstract protected void buildAtdl4jWidgetMap() throws FIXatdlFormatException;
	abstract protected void createRadioGroups();
	abstract protected void buildAtdl4jWidgetWithParameterMap();
	abstract protected void attachGlobalStateRulesToControls() throws FIXatdlFormatException;
	abstract protected void attachStateListenersToAllAtdl4jWidgets();
	abstract protected void initEnd();

	abstract protected void addToAtdl4jWidgetMap( String aName, Atdl4jWidget<?> aAtdl4jWidget );
	abstract protected void addToAtdl4jWidgetWithParameterMap( String aName, Atdl4jWidget<?> aAtdl4jWidget );
	abstract protected void removeFromAtdl4jWidgetMap( String aName );
	abstract protected void removeFromAtdl4jWidgetWithParameterMap( String aName );
	abstract public void setCxlReplaceMode(boolean cxlReplaceMode);
	abstract protected void fireStateListeners();
	abstract protected void fireStateListenersForAtdl4jWidget( Atdl4jWidget<?> aAtdl4jWidget );
	abstract protected void fireLoadFixMessageStateListenersForAtdl4jWidget( Atdl4jWidget<?> aAtdl4jWidget );


	abstract protected void applyRadioGroupRules();
	
	/**
	 * Standalone intializer
	 * 
	 * @param strategy
	 * @param aStrategies
	 * @param aAtdl4jOptions (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 * @throws FIXatdlFormatException 
	 * @throws Atdl4jClassLoadException 
	 */
	public void init(StrategyT strategy, Atdl4jOptions options, Object parentContainer) throws FIXatdlFormatException
	{
	    init(strategy,
		 new StrategiesT(),
		 options,
		 new HashMap<String, ValidationRule>(),
		 parentContainer);
	}	
	
	/**
	 * @param strategy
	 * @param aStrategies
	 * @param aAtdl4jOptions (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 * @throws Atdl4jClassLoadException 
	 */
	public void init(StrategyT strategy, StrategiesT aStrategies, Atdl4jOptions aAtdl4jOptions, Map<String, ValidationRule> strategiesRules, Object parentContainer) throws FIXatdlFormatException
	{
		setStrategy( strategy );
		setStrategies( aStrategies );
		setAtdl4jOptions( aAtdl4jOptions );

		initBegin( parentContainer );

		// initialize rules collection with global rules
		setStrategyRuleset( new StrategyRuleset() );

		setParameterMap( buildParameters( getStrategy() ) );
		
		setCompleteValidationRuleMap( buildGlobalAndLocalRuleMap( getStrategy(), strategiesRules ) );
		
		buildAtdl4jWidgetMap();
		
		checkForDuplicateControlIDs();
		createRadioGroups();

		addHiddenFieldsForInputAndFilterData( getAtdl4jOptions().getInputAndFilterData() );
		
		buildAtdl4jWidgetWithParameterMap();
		attachGlobalStateRulesToControls();
		
		addHiddenFieldsForParameterWithoutControl( getParameterMap() );

		attachStateListenersToAllAtdl4jWidgets();
		
		
		
		// -- Last statement --
		initEnd();
	}


	
	/**
	 * @param atdl4jOptions
	 *           the atdl4jOptions to set
	 */
	protected void setAtdl4jOptions(Atdl4jOptions atdl4jOptions)
	{
		this.atdl4jOptions = atdl4jOptions;
	}

	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return atdl4jOptions;
	}


	/**
	 * @return the parameterMap
	 */
	public Map<String, ParameterT> getParameterMap()
	{
		return this.parameterMap;
	}

	/**
	 * @param aParameterMap the parameterMap to set
	 */
	protected void setParameterMap(Map<String, ParameterT> aParameterMap)
	{
		this.parameterMap = aParameterMap;
	}

	/**
	 * @return the strategyRuleset
	 */
	public StrategyRuleset getStrategyRuleset()
	{
		return this.strategyRuleset;
	}

	/**
	 * @param aStrategyRuleset the strategyRuleset to set
	 */
	protected void setStrategyRuleset(StrategyRuleset aStrategyRuleset)
	{
		this.strategyRuleset = aStrategyRuleset;
	}

	/**
	 * @param completeValidationRuleMap the completeValidationRuleMap to set
	 */
	protected void setCompleteValidationRuleMap(Map<String, ValidationRule> completeValidationRuleMap)
	{
		this.completeValidationRuleMap = completeValidationRuleMap;
	}

	/**
	 * @return the completeValidationRuleMap
	 */
	public Map<String, ValidationRule> getCompleteValidationRuleMap()
	{
		return completeValidationRuleMap;
	}

	/**
	 * @return the strategy
	 */
	public StrategyT getStrategy()
	{
		return this.strategy;
	}

	/**
	 * @param aStrategy the strategy to set
	 */
	protected void setStrategy(StrategyT aStrategy)
	{
		this.strategy = aStrategy;
	}
	
	/**
	 * @param strategy
	 * @return
	 * @throws FIXatdlFormatException 
	 * @throws Atdl4jClassLoadException 
	 */
	protected Map<String, ParameterT> buildParameters(StrategyT strategy) throws FIXatdlFormatException
	{
		Map<String, ParameterT> tempParameters = new HashMap<String, ParameterT>();
		
		// build parameters
		for ( ParameterT parameter : strategy.getParameter() )
		{
			// compile list of parameters (TODO: is this needed?)
			tempParameters.put( parameter.getName(), parameter );

			boolean tempIsRequired = false;
			
			// required fields should be validated as well
			if ( parameter.getUse() != null )
			{
				if ( parameter.getUse().equals( UseT.REQUIRED ) )
				{
					tempIsRequired = true;
					ValidationRule requiredFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.EX, null, strategy );
					getStrategyRuleset().addRequiredFieldRule( requiredFieldRule );
				}
			}
			
			ParameterTypeConverter<?> tempTypeConverter = TypeConverterFactoryConfig.getTypeConverterFactory().createParameterTypeConverter( parameter );
			
			if ( ParameterHelper.getConstValue( parameter ) != null )
			{
				String tempStringValue = tempTypeConverter.convertParameterValueToComparisonString( ParameterHelper.getConstValue( parameter ) ); 
				ValidationRule tempFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.EQ, tempStringValue, strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addConstFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addConstFieldRule( tempOptionalWrapperEdit );
				}
			}
			
			if ( ParameterHelper.getMinValue( parameter ) != null )
			{
				String tempStringValue = tempTypeConverter.convertParameterValueToComparisonString( ParameterHelper.getMinValue( parameter ) ); 
				ValidationRule tempFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.GE, tempStringValue, strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addRangeFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addRangeFieldRule( tempOptionalWrapperEdit );
				}
			}
			
			if ( ParameterHelper.getMaxValue( parameter ) != null )
			{
				String tempStringValue = tempTypeConverter.convertParameterValueToComparisonString( ParameterHelper.getMaxValue( parameter ) ); 
				ValidationRule tempFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.LE, tempStringValue, strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addRangeFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addRangeFieldRule( tempOptionalWrapperEdit );
				}
			}
			
			if ( ParameterHelper.getMinLength( parameter ) != null )
			{
				ValidationRule tempFieldRule = new LengthValidationRule( parameter.getName(), OperatorT.GE, ParameterHelper.getMinLength( parameter ), strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addLengthFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addLengthFieldRule( tempOptionalWrapperEdit );
				}
			}
			
			if ( ParameterHelper.getMaxLength( parameter ) != null )
			{
				ValidationRule tempFieldRule = new LengthValidationRule( parameter.getName(), OperatorT.LE, ParameterHelper.getMaxLength( parameter ), strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addLengthFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addLengthFieldRule( tempOptionalWrapperEdit );
				}
			}

			
			// validate types based on patterns
			if ( parameter instanceof MultipleCharValueT )
			{
				MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule( multipleCharValueT.getName(), "\\S?(\\s\\S?)*" );
				getStrategyRuleset().addPatternRule( patternBasedRule );

			}
			else if ( parameter instanceof MultipleStringValueT )
			{
				MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule( multipleStringValueT.getName(), "\\S+(\\s\\S+)*" );
				getStrategyRuleset().addPatternRule( patternBasedRule );
			}

			// 2/1/2010 John Shields added
			// TODO Deprecate trueWireValue and falseWireValue attribute;
			if ( parameter instanceof BooleanT )
			{
				if ( ( (BooleanT) parameter ).getTrueWireValue() != null )
					throw new FIXatdlFormatException( "Attribute \"trueWireValue\" on Boolean_t is deprecated."
							+ " Please use \"checkedEnumRef\" on CheckBox_t or RadioButton_t instead." );

				if ( ( (BooleanT) parameter ).getFalseWireValue() != null )
					throw new FIXatdlFormatException( "Attribute \"falseWireValue\" on Boolean_t is deprecated."
							+ " Please use \"uncheckedEnumRef\" on CheckBox_t or RadioButton_t instead." );
			}
		}
		
		return tempParameters;
	}


	/**
	 * @param strategy
	 * @param strategiesRules
	 * @return
	 * @throws FIXatdlFormatException 
	 */
	protected Map<String, ValidationRule> buildGlobalAndLocalRuleMap(StrategyT strategy, Map<String, ValidationRule> strategiesRules) throws FIXatdlFormatException
	{
		Map<String, ValidationRule> tempRuleMap = new HashMap<String, ValidationRule>( strategiesRules );

		// and add local rules
		for ( EditT edit : strategy.getEdit() )
		{
			ValidationRule rule = ValidationRuleFactory.createRule( edit, tempRuleMap, strategy );
			String id = edit.getId();
			if ( id != null )
			{
				tempRuleMap.put( id, rule );
			}
		}

		// generate validation rules for StrategyEdit elements
		for ( StrategyEditT se : strategy.getStrategyEdit() )
		{
			if ( se.getEdit() != null )
			{
				EditT edit = se.getEdit();
				ValidationRule rule = ValidationRuleFactory.createRule( edit, tempRuleMap, se );
				String id = edit.getId();
				if ( id != null )
				{
					tempRuleMap.put( id, rule ); // TODO: this line should be moved
				}
				// to RuleFactory?
				getStrategyRuleset().putRefRule( se, rule ); // TODO: this line should be moved 
				// to RuleFactory?
			}

			// reference for a previously defined rule
			if ( se.getEditRef() != null )
			{
				EditRefT editRef = se.getEditRef();
				String id = editRef.getId();
				getStrategyRuleset().putRefRule( se, new ReferencedValidationRule( id ) ); // TODO:
				// this
				// line
				// should
				// be
				// moved
				// to
				// RuleFactory?
			}
		}

		return tempRuleMap;
	}
	
	protected void checkForDuplicateControlIDs() throws FIXatdlFormatException
	{
		// -- Note getAtdl4jWidgetMap() constructs a new Map --
		Collection<Atdl4jWidget<?>> tempAtdl4jWidgetMapValues = (Collection<Atdl4jWidget<?>>) getAtdl4jWidgetMap().values();
		
		for ( Atdl4jWidget<?> widget : tempAtdl4jWidgetMapValues )
		{
			for ( Atdl4jWidget<?> widget2 : tempAtdl4jWidgetMapValues )
			{
				if ( widget != widget2 && widget.getControl().getID().equals( widget2.getControl().getID() ) )
					throw new FIXatdlFormatException( "Duplicate Control ID: \"" + widget.getControl().getID() + "\"" );
			}
		}
	}

	public Atdl4jWidget<?> getAtdl4jWidgetForParameter( ParameterT aParameterRef )
	{
		if ( ( aParameterRef != null ) && ( getAtdl4jWidgetWithParameterMap() != null ) )
		{
			Collection<Atdl4jWidget<?>> tempAtdl4jWidgetWithParameterMapValues = (Collection<Atdl4jWidget<?>>) getAtdl4jWidgetWithParameterMap().values();
			
			for ( Atdl4jWidget<?> widget : tempAtdl4jWidgetWithParameterMapValues )
			{
				if ( aParameterRef.equals( widget.getParameter() ) )
				{
					return widget;
				}
			}
		}
		
		return null;
	}


	protected void addHiddenFieldsForInputAndFilterData( InputAndFilterData aInputAndFilterData )
	{
		if ( ( aInputAndFilterData != null )
				&& ( aInputAndFilterData.getInputHiddenFieldNameValueMap() != null ) )
		{
			ObjectFactory tempObjectFactory = new ObjectFactory();
	
			for ( Map.Entry<String, String> tempMapEntry : aInputAndFilterData.getInputHiddenFieldNameValueMap().entrySet() )
			{
				String tempName = tempMapEntry.getKey();
				Object tempValue = tempMapEntry.getValue();
				ParameterT parameter = tempObjectFactory.createStringT();
				parameter.setName( tempName );
				parameter.setUse( UseT.OPTIONAL );
	
				// compile list of parameters (TODO: is this needed?)
				getParameterMap().put( parameter.getName(), parameter );
	
				HiddenFieldT hiddenField = new HiddenFieldT();
				hiddenField.setInitValue( tempValue.toString() );
				hiddenField.setParameterRef( tempName );
	
				Atdl4jWidget<?> hiddenFieldWidget = getAtdl4jWidgetFactory().createHiddenFieldT( hiddenField, parameter );
				hiddenFieldWidget.setHiddenFieldForInputAndFilterData( true );
				
				addToAtdl4jWidgetMap( tempName, hiddenFieldWidget );
				addToAtdl4jWidgetWithParameterMap( tempName, hiddenFieldWidget );
			}
		}
		
	}

	protected void clearHiddenFieldsForInputAndFilterData()
	{
		for ( Map.Entry<String,Atdl4jWidget<?>> tempEntry : getAtdl4jWidgetMap().entrySet() )
		{
			if ( tempEntry.getValue().isHiddenFieldForInputAndFilterData() )
			{
				removeFromAtdl4jWidgetMap( tempEntry.getKey() );
				removeFromAtdl4jWidgetWithParameterMap( tempEntry.getKey() );
			}
		}
	}

	protected void reloadHiddenFieldsForInputAndFilterData( InputAndFilterData aInputAndFilterData )
	{
		clearHiddenFieldsForInputAndFilterData();
		addHiddenFieldsForInputAndFilterData( aInputAndFilterData );
	}
	
	
	protected void addHiddenFieldsForParameterWithoutControl( Map<String, ParameterT> aParameterMap )
	{
		if ( aParameterMap != null )
		{
			for ( Map.Entry<String, ParameterT> tempMapEntry : aParameterMap.entrySet() )
			{
				String tempName = tempMapEntry.getKey();
				ParameterT tempParameter = tempMapEntry.getValue();

				// -- If Parameter does not have a Control --
				if ( getAtdl4jWidgetForParameter( tempParameter ) == null )
				{
					// -- Add a HiddenField control for this parameter (to add to ControlWithParameters map used by StrategyEdit and FIX Message building) -- 
					HiddenFieldT tempHiddenField = new HiddenFieldT();
					tempHiddenField.setParameterRef( tempName );
		
					Atdl4jWidget<?> hiddenFieldWidget = getAtdl4jWidgetFactory().createHiddenFieldT( tempHiddenField, tempParameter );
					addToAtdl4jWidgetMap( tempName, hiddenFieldWidget );
					addToAtdl4jWidgetWithParameterMap( tempName, hiddenFieldWidget );
				}
			}
		}
	}

	
	
	public void validate() throws ValidationException
	{
		if ( getStrategyRuleset() != null )
		{
			// delegate validation, passing all global and local rules as
			// context information, and all my parameters
			// -- Note that getAtdl4jWidgetWithParameterMap() constructs a new Map --
			getStrategyRuleset().validate( getCompleteValidationRuleMap(), getAtdl4jWidgetWithParameterMap() );
		}
		else
		{
			logger.info( "No validation rule defined for strategy " + getStrategy().getName() );
		}
	}

	public String getFIXMessage()
	{
		StringFIXMessageBuilder builder = new StringFIXMessageBuilder();
		getFIXMessage( builder );
		return builder.getMessage();
	}

	public void getFIXMessage(FIXMessageBuilder builder)
	{
		builder.onStart();

		// Scott Atwell 1/16/2010 added
		if ( ( getStrategy() != null ) && ( getStrategies() != null ) )
		{
			// Set TargetStrategy
			String strategyIdentifier = getStrategy().getWireValue();
			if ( strategyIdentifier != null )
			{
				if ( getStrategies().getStrategyIdentifierTag() != null )
				{
					builder.onField( getStrategies().getStrategyIdentifierTag().intValue(), strategyIdentifier.toString() );
				}
				else
				{
					builder.onField( 847, strategyIdentifier );
				}
			}

			// Scott Atwell 1/16/2010 added
			// Set StrategyVersion
			String strategyVersion = getStrategy().getVersion();
			if ( strategyVersion != null )
			{
				if ( getStrategies().getVersionIdentifierTag() != null )
				{
					builder.onField( getStrategies().getVersionIdentifierTag().intValue(), strategyVersion.toString() );
				}
			}
		}

		/*
		 * TODO 2/1/2010 John Shields added Beginning of Repeating Group
		 * implementation. Currently there is an error in ATDL I believe where
		 * StrategyT can only have one RepeatingGroupT HashMap<String,
		 * RepeatingGroupT> rgroups = new HashMap<String, RepeatingGroupT>(); for
		 * (RepeatingGroupT rg : strategy.getRepeatingGroup()) { for (ParameterT
		 * rg : strategy.getRepeatingGroup()) {
		 * 
		 * } }
		 */

		// -- Note that getAtdl4jWidgetMap() constructs a new Map --
		for ( Atdl4jWidget<?> control : getAtdl4jWidgetMap().values() )
		{
			if ( control.getParameter() != null )
				control.getFIXValue( builder );
		}
		builder.onEnd();
	}


	// TODO: this doesn't know how to load custom repeating groups
	// or standard repeating groups aside from Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS StrategyParameters
	// TODO: would like to integrate with QuickFIX engine
	public void setFIXMessage(String fixMessage)
	{
		// TODO: need to reverse engineer state groups

		String[] fixParams = fixMessage.split( "\\001" );

		for ( int i = 0; i < fixParams.length; i++ )
		{
			String[] pair = fixParams[ i ].split( "=" );
			int tag = Integer.parseInt( pair[ 0 ] );
			String value = pair[ 1 ];

			logger.debug("setFIXMessage() i: " + i + " extracted tag: " + tag + " value: " + value );

			// not repeating group
			if ( tag < Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS || tag > Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE )
			{
				// -- Note that getAtdl4jWidgetWithParameterMap() constructs a new Map --
				for ( Atdl4jWidget<?> widget : getAtdl4jWidgetWithParameterMap().values() )
				{
					if ( widget.getParameter().getFixTag() != null && widget.getParameter().getFixTag().equals( BigInteger.valueOf( tag ) ) )
					{
						loadAtdl4jWidgetWithFIXValue( widget, value );
					}
				}
			}
			// StrategyParams repeating group
			else if ( tag == Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS )
			{
				i++;
				for ( int j = 0; j < Integer.parseInt( value ); j++ )
				{
					String name = fixParams[ i ].split( "=" )[ 1 ];
					String value2 = fixParams[ i + 2 ].split( "=" )[ 1 ];

					// -- Note that getAtdl4jWidgetWithParameterMap() constructs a new Map --
					for ( Atdl4jWidget<?> widget : getAtdl4jWidgetWithParameterMap().values() )
					{
						if ( widget.getParameter().getName() != null && widget.getParameter().getName().equals( name ) )
						{
							loadAtdl4jWidgetWithFIXValue( widget, value2 );
						}
					}
					i = i + 3;
				}
			}
		}

		fireStateListeners();
		logger.debug("setFIXMessage() complete.");
	}

	/**
	 * @param aWidget
	 * @param aValue
	 * @return boolean indicating whether any Collapsible panels were adjusted;
	 * @throws Atdl4jClassLoadException 
	 */
	protected boolean loadAtdl4jWidgetWithFIXValue( Atdl4jWidget<?> aWidget, String aValue )
	{
		aWidget.setFIXValue( aValue );
		
		// -- Handles toggling associated controls (eg checkbox or radio button) when control is set to a non Atdl4jConstants.VALUE_NULL_INDICATOR value --
		fireLoadFixMessageStateListenersForAtdl4jWidget( aWidget );

		fireStateListenersForAtdl4jWidget( aWidget );
		
		// -- If the specified aWidget is part of a Collapsible StrategyPanel which is currently Collapsed, then expand it -- 
		// -- (aCollapsed=false) --
		return getStrategyPanelHelper().expandAtdl4jWidgetParentStrategyPanel( aWidget );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.StrategyUI#reinitStrategyPanel()
	 */
	@Override
	public void reinitStrategyPanel()
	{
		reloadHiddenFieldsForInputAndFilterData( getAtdl4jOptions().getInputAndFilterData() );
		
		for ( Atdl4jWidget<?> tempAtdl4jWidget : getAtdl4jWidgetMap().values() )
		{
			logger.debug( "Invoking Atdl4jWidget.reinit() for: " + tempAtdl4jWidget.getControl().getID() );

			tempAtdl4jWidget.reinit();
		}

		// -- Set Strategy's CxlReplaceMode --
		setCxlReplaceMode( getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode() );;
		
		// -- Execute StateRules --
		fireStateListeners();
		
		// -- If no RadioButtons within a radioGroup are selected, then first one in list will be selected --
		applyRadioGroupRules();
	}

	/**
	 * @return the strategies
	 */
	protected StrategiesT getStrategies()
	{
		return this.strategies;
	}

	/**
	 * @param aStrategies the strategies to set
	 */
	protected void setStrategies(StrategiesT aStrategies)
	{
		this.strategies = aStrategies;
	}
	
	public Atdl4jWidgetFactory getAtdl4jWidgetFactory() 
	{
		if ( atdl4jWidgetFactory == null ) 
		{
		    atdl4jWidgetFactory = Atdl4jConfig.getConfig().createAtdl4jWidgetFactory();			
		    atdl4jWidgetFactory.init( getAtdl4jOptions() );
		}
		return atdl4jWidgetFactory;
	}
	
	/**
	 * @return
	 * @throws Atdl4jClassLoadException 
	 */
	public StrategyPanelHelper getStrategyPanelHelper()
	{
		if ( strategyPanelHelper == null ) 
		{
		    strategyPanelHelper = Atdl4jConfig.getConfig().createStrategyPanelHelper();
		}
		return strategyPanelHelper;
	}	
}