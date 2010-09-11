/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.swing.config;

import org.atdl4j.config.AbstractAtdl4jConfig;

/**
 * 
 * This class contains the data associated with the <code>SwingAtdl4jConfig</code>.
 * 
 * Creation date: (Feb 7, 2010 6:39:07 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public class SwingAtdl4jConfig
		extends AbstractAtdl4jConfig
{
	private static String PACKAGE_PATH_ORG_ATDL4J_UI_SWING = "org.atdl4j.ui.swing.";
	
	public SwingAtdl4jConfig()
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
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingFixatdlFileSelectionPanel";
	}

	protected String getDefaultClassNameFixMsgLoadPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingFixMsgLoadPanel";
	}

	protected String getDefaultClassNameStrategySelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingStrategySelectionPanel";
	}

	protected String getDefaultClassNameStrategyDescriptionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingStrategyDescriptionPanel";
	}

	protected String getDefaultClassNameStrategiesPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingStrategiesPanel";
	}

	protected String getDefaultClassNameAtdl4jTesterPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingAtdl4jTesterPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingAtdl4jInputAndFilterDataSelectionPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingAtdl4jInputAndFilterDataPanel";
	}

	protected String getDefaultClassNameAtdl4jCompositePanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingAtdl4jCompositePanel";
	}

	protected String getDefaultClassNameAtdl4jUserMessageHandler()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "app.SwingAtdl4jUserMessageHandler";
	}

	protected String getDefaultClassNameControlUIForCheckBoxListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingCheckBoxListWidget";
	}

	protected String getDefaultClassNameControlUIForCheckBoxT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingButtonWidget";
	}

	protected String getDefaultClassNameControlUIForClockT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingClockWidget";
	}

	protected String getDefaultClassNameControlUIForDoubleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingSpinnerWidget";
	}

	protected String getDefaultClassNameControlUIForDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingDropDownListWidget";
	}

	protected String getDefaultClassNameControlUIForEditableDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingDropDownListWidget";
	}

	protected String getDefaultClassNameControlUIForHiddenFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingHiddenFieldWidget";
	}

	protected String getDefaultClassNameControlUIForLabelT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingLabelWidget";
	}

	protected String getDefaultClassNameControlUIForMultiSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingListBoxWidget";
	}

	protected String getDefaultClassNameControlUIForRadioButtonListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingRadioButtonListWidget";
	}

	protected String getDefaultClassNameControlUIForRadioButtonT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingButtonWidget";
	}

	protected String getDefaultClassNameControlUIForSingleSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingListBoxWidget";
	}

	protected String getDefaultClassNameControlUIForSingleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingSpinnerWidget";
	}

	protected String getDefaultClassNameControlUIForSliderT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingSliderWidget";
	}

	protected String getDefaultClassNameControlUIForTextFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "widget.SwingTextFieldWidget";
	}

	// Johnny Shields -- I don't like this in the config, as it's implementation specific
	protected String getDefaultClassNameStrategyPanelHelper()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWING + "impl.SwingStrategyPanelHelper";
	}	
}
