/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.swt.config;

import org.atdl4j.config.AbstractAtdl4jConfig;

/**
 * 
 * This class contains the data associated with the <code>SWTAtdl4jConfig</code>.
 * 
 * Creation date: (Feb 7, 2010 6:39:07 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public class SWTAtdl4jConfig
		extends AbstractAtdl4jConfig
{
	private static String PACKAGE_PATH_ORG_ATDL4J_UI_SWT = "org.atdl4j.ui.swt.";
	
	public SWTAtdl4jConfig()
	{
		super();
	}
	
// @see org.atdl4j.ui.impl.BaseStrategiesUIFactory	
//	protected String getDefaultClassNameStrategiesUIFactory()
//	{ 
//		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTStrategiesUIFactory";
//	}
	
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

	protected String getDefaultClassNameStrategiesPanel()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTStrategiesPanel";
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
	
}
