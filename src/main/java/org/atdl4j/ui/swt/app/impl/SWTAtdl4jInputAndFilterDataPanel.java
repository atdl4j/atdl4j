package org.atdl4j.ui.swt.app.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.StrategyFilterInputData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.ui.app.impl.AbstractAtdl4jInputAndFilterDataPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Represents the SWT-specific Atdl4jOptions and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SWTAtdl4jInputAndFilterDataPanel
		extends AbstractAtdl4jInputAndFilterDataPanel
{
	public final Logger logger = Logger.getLogger(SWTAtdl4jInputAndFilterDataPanel.class);
	private Composite parentComposite;
	
	public static int DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT = 20;
	
	private Combo dropDownListStrategyFilterFixMsgType; 
	Button checkboxInputCxlReplaceMode;
	private Combo dropDownListStrategyFilterRegion;
	private Combo dropDownListStrategyFilterCountry;
	public static String[] DEFAULT_STRATEGY_FILTER_COUNTRY_SUBSET_LIST = new String[] { "", "US", "CA", "BR", "UK", "FR", "DE", "JP", "HK", "AU" };  // just to seed it with some  
	private Combo dropDownListStrategyFilterMICCode;
	public static String[] DEFAULT_STRATEGY_FILTER_MIC_CODE_SUBSET_LIST = new String[] { "", "XNYS", "XNAS", "XBMF", "XLSE", "XPAR", "XFRA", "XETR", "XTKS", "XHKG", "XASX" };  // just to seed it with some  
	private Combo dropDownListStrategyFilterSecurityType;
	
	private Text textSelectStrategyName;
	private Text textAreaStrategyNameFilterList;
	private Button checkboxInputStrategyListAsFilter;
	
	private Combo dropDownListFixFieldOrdType;
	public static String[] DEFAULT_FIX_FIELD_ORD_TYPE_SUBSET_LIST = new String[] { "", "1", "2", "3", "4", "6", "7", "8", "9", "D", "E", "G", "I", "J", "K", "P", "Q" };  // just to seed it with some 
	public static String FIX_FIELD_NAME_ORD_TYPE = "FIX_OrdType";  // tag 40
	
	private Combo dropDownListFixFieldSide;
	public static String[] DEFAULT_FIX_FIELD_SIDE_SUBSET_LIST = new String[] { "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C" };  // just to seed it with some 
	public static String FIX_FIELD_NAME_SIDE = "FIX_Side";  // tag 54
	
	private Combo dropDownListFixFieldOrderQty;
	public static String[] DEFAULT_FIX_FIELD_ORDER_QTY_SUBSET_LIST = new String[] { "", "10", "100", "1000", "10000", "100000" };  // just to seed it with some  
	public static String FIX_FIELD_NAME_ORDER_QTY = "FIX_OrderQty";  // tag 38 
	
	private Combo dropDownListFixFieldPrice;
	public static String[] DEFAULT_FIX_FIELD_PRICE_SUBSET_LIST = new String[] { "", "1.00", "5.00", "10", "10.00", "10.75", "25.00", "50.00" };  // just to seed it with some  
	public static String FIX_FIELD_NAME_PRICE = "FIX_Price";  // tag 44
	
	private Combo dropDownListFixFieldHandlInst;
	public static String[] DEFAULT_FIX_FIELD_HANDL_INST_SUBSET_LIST = new String[] { "", "1", "2", "3" };  // just to seed it with some  
	public static String FIX_FIELD_NAME_HANDL_INST = "FIX_HandlInst";  // tag 21
	
	private Combo dropDownListFixFieldExecInst;
	public static String[] DEFAULT_FIX_FIELD_EXEC_INST_SUBSET_LIST = new String[] { ""  };  // just to seed it with some  
	public static String FIX_FIELD_NAME_EXEC_INST = "FIX_ExecInst";  // tag 21 (note MultipleCharValue)
	
	private Combo dropDownListFixFieldTimeInForce;
	public static String[] DEFAULT_FIX_FIELD_TIME_IN_FORCE_SUBSET_LIST = new String[] { "", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };  // just to seed it with some  
	public static String FIX_FIELD_NAME_TIME_IN_FORCE = "FIX_TimeInForce";  // tag 59
	
	private Combo dropDownListFixFieldClOrdLinkID;
	public static String[] DEFAULT_FIX_FIELD_CL_ORD_LINK_ID_SUBSET_LIST = new String[] { "", "COMMON_ID_1", "COMMON_ID_2" };  // just to seed it with some  
	public static String FIX_FIELD_NAME_CL_ORD_LINK_ID = "FIX_ClOrdLinkID";  // tag 583

	private Button checkboxAtd4ljShowStrategyDescription;
	private Button checkboxAtd4ljShowFileSelectionSection;
	private Button checkboxAtd4ljShowValidateOutputSection;
	private Button checkboxAtd4ljShowTesterPanelOkCancelButtonSection;
	private Button checkboxAtd4ljAccommodateMixOfStrategyPanelsAndControls;

	private Text textIncrementPolicyLotSize;
	private Text textIncrementPolicyTick;

	public Object buildAtdl4jInputAndFilterDataPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		return buildAtdl4jInputAndFilterDataPanel( (Composite) aParentOrShell, aAtdl4jOptions );
	}
	
	public Composite buildAtdl4jInputAndFilterDataPanel(Composite aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		parentComposite = (Composite) aParentOrShell;

		// -- Delegate back to AbstractAtdl4jInputAndFilterDataPanel -- 
		init( aParentOrShell, aAtdl4jOptions );
		
		// -- Build the SWT.Composite from Atdl4jCompositePanel --
		buildCoreAtdl4jSettingsPanel( aParentOrShell );

		return parentComposite;
	}


	protected Composite buildCoreAtdl4jSettingsPanel( Composite aParentOrShell )
	{
		Composite tempCoreAtdl4jSettingsPanel = new Composite( aParentOrShell, SWT.NONE );
		GridLayout tempLayout = new GridLayout(1, false);
		tempCoreAtdl4jSettingsPanel.setLayout(tempLayout);
		tempCoreAtdl4jSettingsPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		buildStrategyFilterPanel( tempCoreAtdl4jSettingsPanel );
		
		Composite tempThreeColPanel = new Composite( tempCoreAtdl4jSettingsPanel, SWT.NONE );
		GridLayout tempTwoColLayout = new GridLayout(3, false);
		tempThreeColPanel.setLayout(tempTwoColLayout);
		tempThreeColPanel.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, false));
		
		buildStandardFixFieldsPanel( tempThreeColPanel );
		buildSelectStrategyPanel( tempThreeColPanel );

		Composite tempThirdColumn = new Composite( tempThreeColPanel, SWT.NONE );
		GridLayout tempThirdColumnLayout = new GridLayout(1, false);
		tempThirdColumn.setLayout(tempThirdColumnLayout);
		tempThirdColumn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buildAtdl4jOptionsSettingsPanel( tempThirdColumn );
		buildIncrementPolicyPanel( tempThirdColumn );
		
		return tempCoreAtdl4jSettingsPanel;
	}

	protected Composite buildStrategyFilterPanel( Composite aParent )
	{
		Group tempStrategyFilterGroup = new Group( aParent, SWT.NONE );
		tempStrategyFilterGroup.setText( "FIXatdl Strategy Filter" );
		RowLayout tempStrategyFilterGroupLayout = new RowLayout( SWT.VERTICAL );
		tempStrategyFilterGroup.setLayout(tempStrategyFilterGroupLayout);
		
		Composite tempRowOne = new Composite( tempStrategyFilterGroup, SWT.NONE );
		tempRowOne.setLayout( new RowLayout( SWT.HORIZONTAL ) );
		
		Label tempLabelStrategyFilterFixMsgType = new Label( tempRowOne, SWT.NONE );
		tempLabelStrategyFilterFixMsgType.setText( "FixMsgType:" );
		dropDownListStrategyFilterFixMsgType = new Combo( tempRowOne, SWT.READ_ONLY );
		List<String> tempFixMsgTypeList = new ArrayList<String>();
		tempFixMsgTypeList.add( "" ); // add empty string at top
		tempFixMsgTypeList.addAll( Arrays.asList( Atdl4jConstants.STRATEGY_FILTER_FIX_MSG_TYPES ) );
		dropDownListStrategyFilterFixMsgType.setItems( tempFixMsgTypeList.toArray( new String[0] ) );
		dropDownListStrategyFilterFixMsgType.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );

		checkboxInputCxlReplaceMode = new Button( tempRowOne, SWT.CHECK );
		checkboxInputCxlReplaceMode.setText( "Cxl Replace Mode" );
		

		Composite tempRowTwo = new Composite( tempStrategyFilterGroup, SWT.NONE );
		tempRowTwo.setLayout( new RowLayout( SWT.HORIZONTAL ) );
		
		Label tempLabelStrategyFilterRegion = new Label( tempRowTwo, SWT.NONE );
		tempLabelStrategyFilterRegion.setText( "Region:" );
		dropDownListStrategyFilterRegion = new Combo( tempRowTwo, SWT.READ_ONLY );
		List<String> tempRegionList = new ArrayList<String>();
		tempRegionList.add( "" ); // add empty string at top
		tempRegionList.addAll( Arrays.asList( Atdl4jConstants.STRATEGY_FILTER_REGIONS ) );
		dropDownListStrategyFilterRegion.setItems( tempRegionList.toArray( new String[0] ) );
		dropDownListStrategyFilterRegion.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );

		Label tempLabelStrategyFilterCountry = new Label( tempRowTwo, SWT.NONE );
		tempLabelStrategyFilterCountry.setText( "Country:" );
		dropDownListStrategyFilterCountry = new Combo( tempRowTwo, SWT.NONE );
		dropDownListStrategyFilterCountry.setItems( DEFAULT_STRATEGY_FILTER_COUNTRY_SUBSET_LIST );
		dropDownListStrategyFilterCountry.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListStrategyFilterCountry.setToolTipText( "Specify ISO 3166 (2 character) Country Code\nExample list provided." );
		
		Label tempLabelStrategyFilterMICCode = new Label( tempRowTwo, SWT.NONE );
		tempLabelStrategyFilterMICCode.setText( "MIC Code:" );
		dropDownListStrategyFilterMICCode = new Combo( tempRowTwo, SWT.NONE );
		dropDownListStrategyFilterMICCode.setItems( DEFAULT_STRATEGY_FILTER_MIC_CODE_SUBSET_LIST );
		dropDownListStrategyFilterMICCode.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListStrategyFilterMICCode.setToolTipText( "Specify ISO 10383 (4 character) MICCode Code\nExample list provided." );
		
		Label tempLabelStrategyFilterSecurityType = new Label( tempRowTwo, SWT.NONE );
		tempLabelStrategyFilterSecurityType.setText( "Security Type:" );
		dropDownListStrategyFilterSecurityType = new Combo( tempRowTwo, SWT.NONE );
		List<String> tempSecurityTypeList = new ArrayList<String>();
		tempSecurityTypeList.add( "" ); // add empty string at top
		tempSecurityTypeList.addAll( Arrays.asList( Atdl4jConstants.STRATEGY_FILTER_SECURITY_TYPES ) );
		dropDownListStrategyFilterSecurityType.setItems( tempSecurityTypeList.toArray( new String[0] ) );

		dropDownListStrategyFilterSecurityType.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListStrategyFilterSecurityType.setToolTipText( "Specify FIX (tag 167) Security Type value\nExample list provided." );
		
		
		return tempStrategyFilterGroup;
	}

	
	protected Composite buildSelectStrategyPanel( Composite aParent )
	{
		Group tempSelectStrategyGroup = new Group( aParent, SWT.NONE );
		tempSelectStrategyGroup.setText( "Select Strategy" );
		GridLayout tempSelectStrategyGroupLayout = new GridLayout( 1, true );
		tempSelectStrategyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		tempSelectStrategyGroup.setLayout(tempSelectStrategyGroupLayout);

		Composite tempSelectStrategyNameComposite = new Composite( tempSelectStrategyGroup, SWT.NONE );
		tempSelectStrategyNameComposite.setLayout( new RowLayout( SWT.HORIZONTAL ) );
		Label tempLabelSelectStrategyName = new Label( tempSelectStrategyNameComposite, SWT.NONE );
		tempLabelSelectStrategyName.setText( "Pre-select Strategy:" );
		textSelectStrategyName = new Text( tempSelectStrategyNameComposite, SWT.BORDER );
		
		Group tempStrategyNameFilterGroup = new Group( tempSelectStrategyGroup, SWT.NONE );
		tempStrategyNameFilterGroup.setText( "Strategy Name Sequence (Filter)" );
		tempStrategyNameFilterGroup.setToolTipText( "When specified, controls the order of Strategy Name list presented to the user." );
		GridLayout tempStrategyNameFilterGroupLayout = new GridLayout( 1, true );
		tempStrategyNameFilterGroup.setLayout( tempStrategyNameFilterGroupLayout );
		tempStrategyNameFilterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		textAreaStrategyNameFilterList = new Text( tempStrategyNameFilterGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		textAreaStrategyNameFilterList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textAreaStrategyNameFilterList.setToolTipText( "When specified, controls the order of Strategy Name list presented to the user." );
		checkboxInputStrategyListAsFilter = new Button( tempStrategyNameFilterGroup, SWT.CHECK );
		checkboxInputStrategyListAsFilter.setText( "Apply List as Filter" );
		checkboxInputStrategyListAsFilter.setToolTipText( "When checked only the strategy names specified will be shown (any others will be excluded.)" );

		return tempSelectStrategyGroup;
	}

	protected Composite buildStandardFixFieldsPanel( Composite aParent )
	{
		Group tempStandardFixFieldsGroup = new Group( aParent, SWT.NONE );
		tempStandardFixFieldsGroup.setText( "FIX Fields from Order" );
		GridLayout tempStandardFixFieldsGroupLayout = new GridLayout( 2, true );
		tempStandardFixFieldsGroup.setLayout(tempStandardFixFieldsGroupLayout);
		
		Label tempLabelStrategyFilterOrdType = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterOrdType.setText( "OrdType:" );
		dropDownListFixFieldOrdType = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldOrdType.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldOrdType.setItems( DEFAULT_FIX_FIELD_ORD_TYPE_SUBSET_LIST );
		dropDownListFixFieldOrdType.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldOrdType.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterSide = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterSide.setText( "Side:" );
		dropDownListFixFieldSide = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldSide.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldSide.setItems( DEFAULT_FIX_FIELD_SIDE_SUBSET_LIST );
		dropDownListFixFieldSide.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldSide.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterOrderQty = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterOrderQty.setText( "OrderQty:" );
		dropDownListFixFieldOrderQty = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldOrderQty.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldOrderQty.setItems( DEFAULT_FIX_FIELD_ORDER_QTY_SUBSET_LIST );
		dropDownListFixFieldOrderQty.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldOrderQty.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterPrice = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterPrice.setText( "Price:" );
		dropDownListFixFieldPrice = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldPrice.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldPrice.setItems( DEFAULT_FIX_FIELD_PRICE_SUBSET_LIST );
		dropDownListFixFieldPrice.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldPrice.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterHandlInst = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterHandlInst.setText( "HandlInst:" );
		dropDownListFixFieldHandlInst = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldHandlInst.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldHandlInst.setItems( DEFAULT_FIX_FIELD_HANDL_INST_SUBSET_LIST );
		dropDownListFixFieldHandlInst.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldHandlInst.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterExecInst = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterExecInst.setText( "ExecInst:" );
		dropDownListFixFieldExecInst = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldExecInst.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldExecInst.setItems( DEFAULT_FIX_FIELD_EXEC_INST_SUBSET_LIST );
		dropDownListFixFieldExecInst.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldExecInst.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterTimeInForce = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterTimeInForce.setText( "TimeInForce:" );
		dropDownListFixFieldTimeInForce = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldTimeInForce.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldTimeInForce.setItems( DEFAULT_FIX_FIELD_TIME_IN_FORCE_SUBSET_LIST );
		dropDownListFixFieldTimeInForce.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldTimeInForce.setToolTipText( "Specify FIX Field value\nExample list provided." );
		
		Label tempLabelStrategyFilterClOrdLinkID = new Label( tempStandardFixFieldsGroup, SWT.NONE );
		tempLabelStrategyFilterClOrdLinkID.setText( "ClOrdLinkID:" );
		dropDownListFixFieldClOrdLinkID = new Combo( tempStandardFixFieldsGroup, SWT.NONE );
		dropDownListFixFieldClOrdLinkID.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
		dropDownListFixFieldClOrdLinkID.setItems( DEFAULT_FIX_FIELD_CL_ORD_LINK_ID_SUBSET_LIST );
		dropDownListFixFieldClOrdLinkID.setVisibleItemCount( DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT );
		dropDownListFixFieldClOrdLinkID.setToolTipText( "Specify FIX Field value (eg for PAIRS strategy)\nExample list provided." );

		return tempStandardFixFieldsGroup;
	}
	
	protected Composite buildAtdl4jOptionsSettingsPanel( Composite aParent )
	{
		Group tempAtdl4jOptionsSettingsGroup = new Group( aParent, SWT.NONE );
		tempAtdl4jOptionsSettingsGroup.setText( "Atdl4j Settings" );
		GridLayout tempAtdl4jOptionsSettingsGroupLayout = new GridLayout( 1, true );
		tempAtdl4jOptionsSettingsGroup.setLayout(tempAtdl4jOptionsSettingsGroupLayout);
		tempAtdl4jOptionsSettingsGroup.setLayoutData(new GridData(SWT.TOP, SWT.FILL, false, false));
		
		checkboxAtd4ljShowStrategyDescription = new Button( tempAtdl4jOptionsSettingsGroup, SWT.CHECK );
		checkboxAtd4ljShowStrategyDescription.setText( "Show Strategy Description" );
		checkboxAtd4ljShowStrategyDescription.setToolTipText( "When checked, Strategy Description panel will be shown when Strategy's Description has been specified." );
		checkboxAtd4ljShowStrategyDescription.setSelection( Atdl4jConfig.getConfig().isShowStrategyDescription() );
		
		checkboxAtd4ljShowFileSelectionSection = new Button( tempAtdl4jOptionsSettingsGroup, SWT.CHECK );
		checkboxAtd4ljShowFileSelectionSection.setText( "Show File Selection" );
		checkboxAtd4ljShowFileSelectionSection.setToolTipText( "When checked, File Selection panel will be shown." );
		checkboxAtd4ljShowFileSelectionSection.setSelection( Atdl4jConfig.getConfig().isShowFileSelectionSection() );
		
		checkboxAtd4ljShowValidateOutputSection = new Button( tempAtdl4jOptionsSettingsGroup, SWT.CHECK );
		checkboxAtd4ljShowValidateOutputSection.setText( "Show Validation Section" );
		checkboxAtd4ljShowValidateOutputSection.setToolTipText( "When checked, Validation panel will be shown providing \"Validate\" button and output text field." );
		checkboxAtd4ljShowValidateOutputSection.setSelection( Atdl4jConfig.getConfig().isShowValidateOutputSection() );
		
		checkboxAtd4ljShowTesterPanelOkCancelButtonSection = new Button( tempAtdl4jOptionsSettingsGroup, SWT.CHECK );
		checkboxAtd4ljShowTesterPanelOkCancelButtonSection.setText( "Show OK/Close" );
		checkboxAtd4ljShowTesterPanelOkCancelButtonSection.setToolTipText( "When checked, \"OK\" and \"Close\" buttons will be displayed and available." );
		checkboxAtd4ljShowTesterPanelOkCancelButtonSection.setSelection( Atdl4jConfig.getConfig().isShowTesterPanelOkCancelButtonSection() );
		
		checkboxAtd4ljAccommodateMixOfStrategyPanelsAndControls = new Button( tempAtdl4jOptionsSettingsGroup, SWT.CHECK );
		checkboxAtd4ljAccommodateMixOfStrategyPanelsAndControls.setText( "Accommodate SP/Controls Mix" );
		checkboxAtd4ljAccommodateMixOfStrategyPanelsAndControls.setToolTipText( "Accommodates StrategyPanel containing a mix of StrategyPanel and Controls.\nFIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file." );
		checkboxAtd4ljAccommodateMixOfStrategyPanelsAndControls.setSelection( getAtdl4jOptions().isAccommodateMixOfStrategyPanelsAndControls() );
		
		return tempAtdl4jOptionsSettingsGroup;
	}
	
	protected Composite buildIncrementPolicyPanel( Composite aParent )
	{
		Group tempIncrementPolicyGroup = new Group( aParent, SWT.NONE );
		tempIncrementPolicyGroup.setText( "Increment Policy" );
		GridLayout tempIncrementPolicyGroupLayout = new GridLayout( 2, false );
		tempIncrementPolicyGroup.setLayout(tempIncrementPolicyGroupLayout);
		tempIncrementPolicyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false ));
		
		Label tempLabelIncrementPolicyLotSize = new Label( tempIncrementPolicyGroup, SWT.NONE );
		tempLabelIncrementPolicyLotSize.setText( "Lot Size:" );
		textIncrementPolicyLotSize = new Text( tempIncrementPolicyGroup, SWT.NONE );
		textIncrementPolicyLotSize.setToolTipText( "May be used in conjunction with Control/@incrementPolicy on spinner controls" );
		textIncrementPolicyLotSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false ));
		setTextValue( textIncrementPolicyLotSize, getAtdl4jOptions().getInputAndFilterData().getInputIncrementPolicy_LotSize() );
			  
		Label tempLabelIncrementPolicyTick = new Label( tempIncrementPolicyGroup, SWT.NONE );
		tempLabelIncrementPolicyTick.setText( "Tick Size:" );
		textIncrementPolicyTick = new Text( tempIncrementPolicyGroup, SWT.NONE );
		textIncrementPolicyTick.setToolTipText( "May be used in conjunction with Control/@incrementPolicy on spinner controls" );
		textIncrementPolicyTick.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false ));
		setTextValue( textIncrementPolicyTick, getAtdl4jOptions().getInputAndFilterData().getInputIncrementPolicy_Tick() );
			  
		return tempIncrementPolicyGroup;
	}

	/*
	 */
	public boolean extractAtdl4jOptionsFromScreen()
	{
		StrategyFilterInputData tempStrategyFilterInputData = new StrategyFilterInputData();
		tempStrategyFilterInputData.setFixMsgType( getDropDownItemSelected( dropDownListStrategyFilterFixMsgType ) );
		tempStrategyFilterInputData.setRegion_name( getDropDownItemSelected( dropDownListStrategyFilterRegion ) );
		tempStrategyFilterInputData.setCountry_CountryCode( getDropDownItemSelected( dropDownListStrategyFilterCountry ) );
		if ( ( tempStrategyFilterInputData.getCountry_CountryCode() != null ) &&
			  ( tempStrategyFilterInputData.getRegion_name() == null ) )
		{
			throw new IllegalArgumentException("Region is required when Country is specified.");
		}
		
		tempStrategyFilterInputData.setMarket_MICCode( getDropDownItemSelected( dropDownListStrategyFilterMICCode ) );
		tempStrategyFilterInputData.setSecurityType_name( getDropDownItemSelected( dropDownListStrategyFilterSecurityType ) );
		
		// -- Set the StrategyFilterInputData we just built --
		getAtdl4jOptions().getInputAndFilterData().setStrategyFilterInputData( tempStrategyFilterInputData );

		getAtdl4jOptions().getInputAndFilterData().setInputCxlReplaceMode( getCheckboxValue( checkboxInputCxlReplaceMode, null ).booleanValue() );
		
		getAtdl4jOptions().getInputAndFilterData().setInputSelectStrategyName( getTextValue( textSelectStrategyName ) );
		getAtdl4jOptions().getInputAndFilterData().setApplyInputStrategyNameListAsFilter( getCheckboxValue( checkboxInputStrategyListAsFilter, Boolean.FALSE ) );
		getAtdl4jOptions().getInputAndFilterData().setInputStrategyNameList( getTextList( textAreaStrategyNameFilterList ) );
		
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_ORD_TYPE, dropDownListFixFieldOrdType );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_SIDE, dropDownListFixFieldSide );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_ORDER_QTY, dropDownListFixFieldOrderQty );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_PRICE, dropDownListFixFieldPrice );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_HANDL_INST, dropDownListFixFieldHandlInst );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_EXEC_INST, dropDownListFixFieldExecInst );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_TIME_IN_FORCE, dropDownListFixFieldTimeInForce );
		addFixFieldToInputAndFilterData( FIX_FIELD_NAME_CL_ORD_LINK_ID, dropDownListFixFieldClOrdLinkID );
	
		Atdl4jConfig.getConfig().setShowStrategyDescription( getCheckboxValue( checkboxAtd4ljShowStrategyDescription, null ).booleanValue() );
		Atdl4jConfig.getConfig().setShowFileSelectionSection( getCheckboxValue( checkboxAtd4ljShowFileSelectionSection, null ).booleanValue() );
		Atdl4jConfig.getConfig().setShowValidateOutputSection( getCheckboxValue( checkboxAtd4ljShowValidateOutputSection, null ).booleanValue() );
		Atdl4jConfig.getConfig().setShowTesterPanelOkCancelButtonSection( getCheckboxValue( checkboxAtd4ljShowTesterPanelOkCancelButtonSection, null ).booleanValue() );
		getAtdl4jOptions().setAccommodateMixOfStrategyPanelsAndControls( getCheckboxValue( checkboxAtd4ljAccommodateMixOfStrategyPanelsAndControls, null ).booleanValue() );

		getAtdl4jOptions().getInputAndFilterData().setInputIncrementPolicy_LotSize( getTextValueAsBigDecimal( textIncrementPolicyLotSize ) );
		getAtdl4jOptions().getInputAndFilterData().setInputIncrementPolicy_Tick( getTextValueAsBigDecimal( textIncrementPolicyTick ) );
		
		return true;
	}

	/*
	 */
	public boolean loadScreenWithAtdl4jOptions()
	{
		if ( getAtdl4jOptions().getInputAndFilterData() != null )
		{
			StrategyFilterInputData tempStrategyFilterInputData = null;
			if ( ( getAtdl4jOptions().getInputAndFilterData().getStrategyFilterInputDataList() != null ) &&
				  ( getAtdl4jOptions().getInputAndFilterData().getStrategyFilterInputDataList().size() > 0 ) )
			{
				tempStrategyFilterInputData = getAtdl4jOptions().getInputAndFilterData().getStrategyFilterInputDataList().get( 0 );
			}

			String tempFixMsgType = null;
			String tempRegion_name = null;
			String tempCountry_CountryCode = null;
			String tempMarket_MICCode = null;
			String tempSecurityType_name = null;
			
			if ( tempStrategyFilterInputData != null )
			{
				tempFixMsgType = tempStrategyFilterInputData.getFixMsgType();
				tempRegion_name = tempStrategyFilterInputData.getRegion_name();
				tempCountry_CountryCode = tempStrategyFilterInputData.getCountry_CountryCode();
				tempMarket_MICCode = tempStrategyFilterInputData.getMarket_MICCode();
				tempSecurityType_name = tempStrategyFilterInputData.getSecurityType_name();
			}

			selectDropDownItem( dropDownListStrategyFilterFixMsgType, tempFixMsgType );
			selectDropDownItem( dropDownListStrategyFilterRegion, tempRegion_name );
			selectDropDownItem( dropDownListStrategyFilterCountry, tempCountry_CountryCode );
			selectDropDownItem( dropDownListStrategyFilterMICCode, tempMarket_MICCode );
			selectDropDownItem( dropDownListStrategyFilterSecurityType, tempSecurityType_name );


			setCheckboxValue( checkboxInputCxlReplaceMode, getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode(), Boolean.FALSE );

			setTextValue( textSelectStrategyName, getAtdl4jOptions().getInputAndFilterData().getInputSelectStrategyName() );
			setCheckboxValue( checkboxInputStrategyListAsFilter, getAtdl4jOptions().getInputAndFilterData().getApplyInputStrategyNameListAsFilter(), Boolean.FALSE );
			setTextList( textAreaStrategyNameFilterList, getAtdl4jOptions().getInputAndFilterData().getInputStrategyNameList() );

			selectDropDownItem( dropDownListFixFieldOrdType, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_ORD_TYPE ) );
			selectDropDownItem( dropDownListFixFieldSide, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_SIDE ) );
			selectDropDownItem( dropDownListFixFieldOrderQty, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_ORDER_QTY ) );
			selectDropDownItem( dropDownListFixFieldPrice, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_PRICE ) );
			selectDropDownItem( dropDownListFixFieldHandlInst, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_HANDL_INST ) );
			selectDropDownItem( dropDownListFixFieldExecInst, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_EXEC_INST ) );
			selectDropDownItem( dropDownListFixFieldTimeInForce, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_TIME_IN_FORCE ) );
			selectDropDownItem( dropDownListFixFieldClOrdLinkID, getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue( FIX_FIELD_NAME_CL_ORD_LINK_ID ) );
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static List<String> getTextList( Text aText )
	{
		String tempText = aText.getText();
		if ( "".equals( tempText ) )
		{
			return null;
		}
		else
		{
			// -- Split each line based upon '\n' --
			return Arrays.asList( tempText.split( "\\r\\n" ) );
		}
	}
	
	public static void setTextList( Text aText, List<String> aList )
	{
		if ( aList == null )
		{
			aText.setText( "" );
		}
		else
		{
			StringBuffer tempBuffer = new StringBuffer();
			for (String tempString : aList )
			{
				if ( tempBuffer.length() > 0 )
				{
					tempBuffer.append( '\n' );
				}
				
				tempBuffer.append( tempString );
			}
			
			aText.setText( tempBuffer.toString() );
		}
	}
	
	public static String getTextValue( Text aText )
	{
		String tempText = aText.getText();
		if ( "".equals( tempText ) )
		{
			return null;
		}
		else
		{
			return tempText;
		}
	}
	
	public static void setTextValue( Text aText, String aValue )
	{
		if ( aValue == null )
		{
			aText.setText( "" );
		}
		else
		{
			aText.setText( aValue );
		}
	}
	
	public static BigInteger getTextValueAsBigInteger( Text aText )
	{
		String tempText = aText.getText();
		if ( "".equals( tempText ) )
		{
			return null;
		}
		else
		{
			return new BigInteger( tempText );
		}
	}
	
	public static void setTextValue( Text aText, BigInteger aValue )
	{
		if ( aValue == null )
		{
			aText.setText( "" );
		}
		else
		{
			aText.setText( aValue.toString() );
		}
	}
	
	public static BigDecimal getTextValueAsBigDecimal( Text aText )
	{
		String tempText = aText.getText();
		if ( "".equals( tempText ) )
		{
			return null;
		}
		else
		{
			return new BigDecimal( tempText );
		}
	}
	
	public static void setTextValue( Text aText, BigDecimal aValue )
	{
		if ( aValue == null )
		{
			aText.setText( "" );
		}
		else
		{
			aText.setText( aValue.toString() );
		}
	}
	
	public static Boolean getCheckboxValue( Button aCheckbox, Boolean aNullIfState )
	{
		boolean tempBool = aCheckbox.getSelection();
		
		if ( ( aNullIfState != null ) && ( aNullIfState.booleanValue() == tempBool ) )
		{
			return null;
		}
		else
		{
			return new Boolean( tempBool );
		}
	}
	
	public static void setCheckboxValue( Button aCheckbox, Boolean aValue, boolean aStateIfNull )
	{
		if ( aValue == null )
		{
			aCheckbox.setSelection( aStateIfNull );
		}
		else
		{
			aCheckbox.setSelection( aValue.booleanValue() );
		}
	}
	
	public static String getDropDownItemSelected( Combo aDropDown )
	{
		String tempText = aDropDown.getText();
		if ( "".equals( tempText ) )
		{
			return null;
		}
		else
		{
			return tempText;
		}
	}
	
	/**
	 * @param aDropDown
	 * @param aItem
	 */
	public static void selectDropDownItem( Combo aDropDown, String aItem )
	{
		if ( aItem != null )
		{
			aDropDown.setText( aItem );
		}
		else
		{
			aDropDown.deselectAll();
		}
	}
	

	protected void addFixFieldToInputAndFilterData( String aFieldName, Combo aDropDown )
	{
		String tempFieldValue = getDropDownItemSelected( aDropDown );
		if ( tempFieldValue != null )
		{
			// -- Add/update it --
			getAtdl4jOptions().getInputAndFilterData().setInputHiddenFieldNameValuePair( aFieldName, tempFieldValue );
		}
		else
		{
			if ( ( getAtdl4jOptions() != null ) && 
				  ( getAtdl4jOptions().getInputAndFilterData() != null ) &&
				  ( getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldNameValueMap() != null ) )
			{
				// -- Attempt to remove it if it exists --
				getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldNameValueMap().remove( aFieldName );
			}
		}
	}
	
}
