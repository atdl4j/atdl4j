/*
 * Created on Feb 7, 2010
 *
 */
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
	public boolean isThrowEventRuntimeExceptions();
	
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
	
	public Atdl4jWidget<?> createAtdl4jWidgetForCheckBoxListT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForCheckBoxT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForClockT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForDoubleSpinnerT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForDropDownListT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForEditableDropDownListT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForHiddenFieldT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForLabelT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForMultiSelectListT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForRadioButtonListT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForRadioButtonT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForSingleSelectListT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForSingleSpinnerT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForSliderT() throws Atdl4jClassLoadException;
	public Atdl4jWidget<?> createAtdl4jWidgetForTextFieldT() throws Atdl4jClassLoadException;
	public Atdl4jCompositePanel createAtdl4jCompositePanel() throws Atdl4jClassLoadException;
	public Atdl4jInputAndFilterDataPanel createAtdl4jInputAndFilterDataPanel() throws Atdl4jClassLoadException;
	public Atdl4jInputAndFilterDataSelectionPanel createAtdl4jInputAndFilterDataSelectionPanel() throws Atdl4jClassLoadException;
	public Atdl4jTesterPanel createAtdl4jTesterPanel() throws Atdl4jClassLoadException;
	public Atdl4jUserMessageHandler createAtdl4jUserMessageHandler() throws Atdl4jClassLoadException;
	public Atdl4jWidgetFactory createAtdl4jWidgetFactory() throws Atdl4jClassLoadException;
	public FixatdlFileSelectionPanel createFixatdlFileSelectionPanel() throws Atdl4jClassLoadException;
	public FixMsgLoadPanel createFixMsgLoadPanel() throws Atdl4jClassLoadException;
	public StrategiesUI createStrategiesUI() throws Atdl4jClassLoadException;
	public StrategyDescriptionPanel createStrategyDescriptionPanel() throws Atdl4jClassLoadException;
	public StrategyPanelHelper createStrategyPanelHelper() throws Atdl4jClassLoadException;
	public StrategySelectionPanel createStrategySelectionPanel() throws Atdl4jClassLoadException;
	public StrategyUI createStrategyUI() throws Atdl4jClassLoadException;
	public TypeConverterFactory createTypeConverterFactory() throws Atdl4jClassLoadException;
}
