/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import org.atdl4j.data.TypeConverterFactory;
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
	public boolean isPrevalidateClassLoaders();
	
	public void setCatchAllMainlineExceptions(boolean aCatchAllMainlineExceptions);
	public void setCatchAllRuntimeExceptions(boolean aCatchAllRuntimeExceptions);
	public void setCatchAllStrategyLoadExceptions(boolean aCatchAllStrategyLoadExceptions);
	public void setCatchAllValidationExceptions(boolean aCatchAllValidationExceptions);
	public void setThrowEventRuntimeExceptions(boolean aThrowEventRuntimeExceptions);
	public void setPrevalidateClassLoaders(boolean aPrevalidateClassLoaders);

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
	
	public Atdl4jWidget<?> createAtdl4jWidgetForCheckBoxListT();
	public Atdl4jWidget<?> createAtdl4jWidgetForCheckBoxT();
	public Atdl4jWidget<?> createAtdl4jWidgetForClockT();
	public Atdl4jWidget<?> createAtdl4jWidgetForDoubleSpinnerT();
	public Atdl4jWidget<?> createAtdl4jWidgetForDropDownListT();
	public Atdl4jWidget<?> createAtdl4jWidgetForEditableDropDownListT();
	public Atdl4jWidget<?> createAtdl4jWidgetForHiddenFieldT();
	public Atdl4jWidget<?> createAtdl4jWidgetForLabelT();
	public Atdl4jWidget<?> createAtdl4jWidgetForMultiSelectListT();
	public Atdl4jWidget<?> createAtdl4jWidgetForRadioButtonListT();
	public Atdl4jWidget<?> createAtdl4jWidgetForRadioButtonT();
	public Atdl4jWidget<?> createAtdl4jWidgetForSingleSelectListT();
	public Atdl4jWidget<?> createAtdl4jWidgetForSingleSpinnerT();
	public Atdl4jWidget<?> createAtdl4jWidgetForSliderT();
	public Atdl4jWidget<?> createAtdl4jWidgetForTextFieldT();
	public Atdl4jCompositePanel createAtdl4jCompositePanel();
	public Atdl4jInputAndFilterDataPanel createAtdl4jInputAndFilterDataPanel();
	public Atdl4jInputAndFilterDataSelectionPanel createAtdl4jInputAndFilterDataSelectionPanel();
	public Atdl4jTesterPanel createAtdl4jTesterPanel();
	public Atdl4jUserMessageHandler createAtdl4jUserMessageHandler();
	public Atdl4jWidgetFactory createAtdl4jWidgetFactory();
	public FixatdlFileSelectionPanel createFixatdlFileSelectionPanel();
	public FixMsgLoadPanel createFixMsgLoadPanel();
	public StrategiesUI createStrategiesUI();
	public StrategyDescriptionPanel createStrategyDescriptionPanel();
	public StrategyPanelHelper createStrategyPanelHelper();
	public StrategySelectionPanel createStrategySelectionPanel();
	public StrategyUI createStrategyUI();
	public TypeConverterFactory createTypeConverterFactory();
}
