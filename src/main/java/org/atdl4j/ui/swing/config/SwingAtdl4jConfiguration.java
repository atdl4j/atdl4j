package org.atdl4j.ui.swing.config;

import org.atdl4j.config.AbstractAtdl4jConfiguration;

/**
 * 
 * This class contains the data associated with the <code>SwingAtdl4jConfiguration</code>.
 * 
 * Creation date: October 5, 2010
 * @author Scott Atwell
 */
public class SwingAtdl4jConfiguration
		extends AbstractAtdl4jConfiguration
{
	private static String PACKAGE_PATH_ORG_ATDL4J_UI_SWING = "org.atdl4j.ui.swing.";
	
	public SwingAtdl4jConfiguration()
	{
		super();
	}
	
	protected String getDefaultClassNameStrategiesUI()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "impl.SwingStrategiesUI";
	}
	
	protected String getDefaultClassNameStrategyUI()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "impl.SwingStrategyUI";
	}
	
	protected String getDefaultClassNameFixatdlFileSelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingFixatdlFileSelectionPanel";
	}

	protected String getDefaultClassNameFixMsgLoadPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingFixMsgLoadPanel";
	}

	protected String getDefaultClassNameStrategySelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingStrategySelectionPanel";
	}

	protected String getDefaultClassNameStrategyDescriptionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingStrategyDescriptionPanel";
	}

	protected String getDefaultClassNameAtdl4jTesterPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingAtdl4jTesterPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingAtdl4jInputAndFilterDataSelectionPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingAtdl4jInputAndFilterDataPanel";
	}

	protected String getDefaultClassNameAtdl4jCompositePanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingAtdl4jCompositePanel";
	}

	protected String getDefaultClassNameAtdl4jUserMessageHandler()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.impl.SwingAtdl4jUserMessageHandler";
	}

	protected String getDefaultClassNameAtdl4jWidgetForCheckBoxListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingCheckBoxListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForCheckBoxT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingButtonWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForClockT()
	{
//		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingClockWidget";
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingJideClockWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForDoubleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingSpinnerWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingDropDownListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForEditableDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingDropDownListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForHiddenFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingHiddenFieldWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForLabelT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingLabelWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForMultiSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingListBoxWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForRadioButtonListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingRadioButtonListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForRadioButtonT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingButtonWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSingleSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingListBoxWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSingleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingSpinnerWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSliderT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingSliderWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForTextFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingTextFieldWidget";
	}

	protected String getDefaultClassNameStrategyPanelHelper()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "impl.SwingStrategyPanelHelper";
	}
}
