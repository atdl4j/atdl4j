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
	private static String PACKAGE_PATH_ORG_ATDL4J_UI_SWT = "org.atdl4j.ui.swt.";
	
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
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTFixatdlFileSelectionPanel";
	}

	protected String getDefaultClassNameFixMsgLoadPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTFixMsgLoadPanel";
	}

	protected String getDefaultClassNameStrategySelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTStrategySelectionPanel";
	}

	protected String getDefaultClassNameStrategyDescriptionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTStrategyDescriptionPanel";
	}

	protected String getDefaultClassNameAtdl4jTesterPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTAtdl4jTesterPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTAtdl4jInputAndFilterDataSelectionPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTAtdl4jInputAndFilterDataPanel";
	}

	protected String getDefaultClassNameAtdl4jCompositePanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTAtdl4jCompositePanel";
	}

	protected String getDefaultClassNameAtdl4jUserMessageHandler()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTAtdl4jUserMessageHandler";
	}

	protected String getDefaultClassNameControlUIForCheckBoxListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.CheckBoxListWidget";
	}

	protected String getDefaultClassNameControlUIForCheckBoxT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.ButtonWidget";
	}

	protected String getDefaultClassNameControlUIForClockT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.ClockWidget";
	}

	protected String getDefaultClassNameControlUIForDoubleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SpinnerWidget";
	}

	protected String getDefaultClassNameControlUIForDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.DropDownListWidget";
	}

	protected String getDefaultClassNameControlUIForEditableDropDownListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.DropDownListWidget";
	}

	protected String getDefaultClassNameControlUIForHiddenFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.HiddenFieldWidget";
	}

	protected String getDefaultClassNameControlUIForLabelT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.LabelWidget";
	}

	protected String getDefaultClassNameControlUIForMultiSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.ListBoxWidget";
	}

	protected String getDefaultClassNameControlUIForRadioButtonListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.RadioButtonListWidget";
	}

	protected String getDefaultClassNameControlUIForRadioButtonT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.ButtonWidget";
	}

	protected String getDefaultClassNameControlUIForSingleSelectListT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.ListBoxWidget";
	}

	protected String getDefaultClassNameControlUIForSingleSpinnerT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SpinnerWidget";
	}

	protected String getDefaultClassNameControlUIForSliderT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.SliderWidget";
	}

	protected String getDefaultClassNameControlUIForTextFieldT()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "widget.TextFieldWidget";
	}

	protected String getDefaultClassNameStrategyPanelHelper()
	{
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTStrategyPanelHelper";
	}
}
