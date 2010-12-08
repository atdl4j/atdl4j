/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;


/**
 * 
 * Creation date: (Feb 7, 2010 7:01:05 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface Atdl4jConfiguration
{
	public void setDebugLoggingLevel( boolean aDebugLevelFlag );
	public boolean isDebugLoggingLevel();
	
	public String getClassNameAtdl4jWidgetForCheckBoxListT();
	public String getClassNameAtdl4jWidgetForCheckBoxT();
	public String getClassNameAtdl4jWidgetForClockT();
	public String getClassNameAtdl4jWidgetForDoubleSpinnerT();
	public String getClassNameAtdl4jWidgetForDropDownListT();
	public String getClassNameAtdl4jWidgetForEditableDropDownListT();
	public String getClassNameAtdl4jWidgetForHiddenFieldT();
	public String getClassNameAtdl4jWidgetForLabelT();
	public String getClassNameAtdl4jWidgetForMultiSelectListT();
	public String getClassNameAtdl4jWidgetForRadioButtonListT();
	public String getClassNameAtdl4jWidgetForRadioButtonT();
	public String getClassNameAtdl4jWidgetForSingleSelectListT();
	public String getClassNameAtdl4jWidgetForSingleSpinnerT();
	public String getClassNameAtdl4jWidgetForSliderT();
	public String getClassNameAtdl4jWidgetForTextFieldT();


	public String getClassNameAtdl4jCompositePanel();
	public String getClassNameAtdl4jInputAndFilterDataPanel();
	public String getClassNameAtdl4jInputAndFilterDataSelectionPanel();
	public String getClassNameAtdl4jTesterPanel();
	public String getClassNameAtdl4jUserMessageHandler();
	public String getClassNameAtdl4jWidgetFactory();
	public String getClassNameFixatdlFileSelectionPanel();
	public String getClassNameFixMsgLoadPanel();
	public String getClassNameStrategiesUI();
	public String getClassNameStrategyDescriptionPanel();
	public String getClassNameStrategyPanelHelper();
	public String getClassNameStrategySelectionPanel();
	public String getClassNameStrategyUI();
	public String getClassNameTypeConverterFactory();
	
	public boolean isCatchAllMainlineExceptions();
	public boolean isCatchAllRuntimeExceptions();;
	public boolean isCatchAllStrategyLoadExceptions();
	public boolean isCatchAllValidationExceptions();
	public void setCatchAllMainlineExceptions(boolean aCatchAllMainlineExceptions);
	public void setCatchAllRuntimeExceptions(boolean aCatchAllRuntimeExceptions);
	public void setCatchAllStrategyLoadExceptions(boolean aCatchAllStrategyLoadExceptions);
	public void setCatchAllValidationExceptions(boolean aCatchAllValidationExceptions);

	public boolean isShowCompositePanelOkCancelButtonSection();
	public boolean isShowTesterPanelOkCancelButtonSection();
	public boolean isShowFileSelectionSection();
	public boolean isShowStrategyDescription();
	public boolean isShowTimezoneSelector();
	public boolean isShowValidateOutputSection();
	public Integer getStrategyDropDownItemDepth();
	public void setShowCompositePanelOkCancelButtonSection(boolean showCompositePanelOkCancelButtonSection);
	public void setShowTesterPanelOkCancelButtonSection(boolean showTesterPanelOkCancelButtonSection);
	public void setShowFileSelectionSection(boolean showFileSelectionSection);
	public void setShowStrategyDescription(boolean showStrategyDescription);
	public void setShowTimezoneSelector(boolean showTimezoneSelector);
	public void setShowValidateOutputSection(boolean showValidateOutputSection);
	public void setStrategyDropDownItemDepth(Integer aStrategyDropDownItemDepth);

}
