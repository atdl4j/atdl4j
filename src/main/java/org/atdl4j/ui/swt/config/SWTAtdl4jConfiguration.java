package org.atdl4j.ui.swt.config;

import org.atdl4j.config.AbstractAtdl4jConfiguration;

/**
 * 
 * This class contains the data associated with the <code>SWTAtdl4jConfiguration</code>.
 * 
 * Creation date: (Sep 28, 2010 6:29:30 PM)
 * @author Scott Atwell
 */
public class SWTAtdl4jConfiguration
		extends AbstractAtdl4jConfiguration
{
	private static final String PACKAGE_PATH_ORG_ATDL4J_UI_SWT = "org.atdl4j.ui.swt.";
	
	public SWTAtdl4jConfiguration()
	{
		super();
	}
	
	protected String getDefaultClassNameStrategiesUI()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTStrategiesUI";
	}
	
	protected String getDefaultClassNameStrategyUI()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTStrategyUI";
	}
	
	protected String getDefaultClassNameFixatdlFileSelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTFixatdlFileSelectionPanel";
	}

	protected String getDefaultClassNameFixMsgLoadPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTFixMsgLoadPanel";
	}

	protected String getDefaultClassNameStrategySelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTStrategySelectionPanel";
	}

	protected String getDefaultClassNameStrategyDescriptionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTStrategyDescriptionPanel";
	}

	protected String getDefaultClassNameAtdl4jTesterPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTAtdl4jTesterPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTAtdl4jInputAndFilterDataSelectionPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTAtdl4jInputAndFilterDataPanel";
	}

	protected String getDefaultClassNameAtdl4jCompositePanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTAtdl4jCompositePanel";
	}

	protected String getDefaultClassNameAtdl4jUserMessageHandler()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.impl.SWTAtdl4jUserMessageHandler";
	}

	protected String getDefaultClassNameAtdl4jWidgetForCheckBoxListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTCheckBoxListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForCheckBoxT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTButtonWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForClockT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTClockWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForDoubleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTSpinnerWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTDropDownListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForEditableDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTDropDownListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForHiddenFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTHiddenFieldWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForLabelT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTLabelWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForMultiSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTListBoxWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForRadioButtonListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTRadioButtonListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForRadioButtonT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTButtonWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSingleSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTListBoxWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSingleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTSpinnerWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSliderT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTSliderWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForTextFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SWTTextFieldWidget";
	}

	protected String getDefaultClassNameStrategyPanelHelper()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTStrategyPanelHelper";
	}
}
