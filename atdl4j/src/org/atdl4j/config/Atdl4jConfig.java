/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.util.List;
import java.util.Map;

import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.LabelT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.fixatdl.layout.TextFieldT;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.ControlUIFactory;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataSelectionPanel;
import org.atdl4j.ui.app.Atdl4jTesterPanel;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.FixMsgLoadPanel;
import org.atdl4j.ui.app.FixatdlFileSelectionPanel;
import org.atdl4j.ui.app.StrategiesPanel;
import org.atdl4j.ui.app.StrategyDescriptionPanel;
import org.atdl4j.ui.app.StrategySelectionPanel;

/**
 * 
 * Creation date: (Feb 7, 2010 7:01:05 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface Atdl4jConfig
{
	// -- UI Infrastructure --
	/**
	 * @return the strategiesUIFactory
	 */
	public StrategiesUIFactory getStrategiesUIFactory();

	/**
	 * @param strategies
	 * @return
	 */
	public StrategiesUI getStrategiesUI(StrategiesT strategies);


	/**
	 * @param strategy
	 * @param strategiesRules
	 * @param parentContainer (for SWT: should be swt.Composite)
	 * @return
	 */
	public StrategyUI getStrategyUI(StrategyT strategy, Map<String, ValidationRule> strategiesRules, Object parentContainer);
	
	/**
	 * @return
	 */
	public ControlUIFactory getControlUIFactory();
	
	/**
	 * @return
	 */
	public TypeConverterFactory getTypeConverterFactory();
	
	
	// -- Controls/Widgets (first arg is of type ControlT -- 
	public ControlUI getControlUIForCheckBoxT(CheckBoxT control, ParameterT parameter);
	public ControlUI getControlUIForDropDownListT(DropDownListT control, ParameterT parameter);
	public ControlUI getControlUIForEditableDropDownListT(EditableDropDownListT control, ParameterT parameter);
	public ControlUI getControlUIForRadioButtonListT(RadioButtonListT control, ParameterT parameter);
	public ControlUI getControlUIForTextFieldT(TextFieldT control, ParameterT parameter);
	public ControlUI getControlUIForSliderT(SliderT control, ParameterT parameter);
	public ControlUI getControlUIForCheckBoxListT(CheckBoxListT control, ParameterT parameter);
	public ControlUI getControlUIForClockT(ClockT control, ParameterT parameter);
	public ControlUI getControlUIForSingleSpinnerT(SingleSpinnerT control, ParameterT parameter);
	public ControlUI getControlUIForDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter);
	public ControlUI getControlUIForSingleSelectListT(SingleSelectListT control, ParameterT parameter);
	public ControlUI getControlUIForMultiSelectListT(MultiSelectListT control, ParameterT parameter);
	public ControlUI getControlUIForHiddenFieldT(HiddenFieldT control, ParameterT parameter);
	public ControlUI getControlUIForLabelT(LabelT control, ParameterT parameter);
	public ControlUI getControlUIForRadioButtonT(RadioButtonT control, ParameterT parameter);

	// -- App Components --
	public Atdl4jTesterPanel getAtdl4jTesterPanel();
	public Atdl4jInputAndFilterDataSelectionPanel getAtdl4jInputAndFilterDataSelectionPanel();
	public Atdl4jInputAndFilterDataPanel getAtdl4jInputAndFilterDataPanel();
	public Atdl4jCompositePanel getAtdl4jCompositePanel();
	public StrategiesPanel getStrategiesPanel();
	public void initAtdl4jUserMessageHandler( Object parentOrShell );
	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler();
	public FixatdlFileSelectionPanel getFixatdlFileSelectionPanel();
	public FixMsgLoadPanel getFixMsgLoadPanel();
	public StrategySelectionPanel getStrategySelectionPanel();
	public StrategyDescriptionPanel getStrategyDescriptionPanel();
	
	public InputAndFilterData getInputAndFilterData();
	public void setInputAndFilterData(InputAndFilterData inputAndFilterData);
	
	public StrategiesT getStrategies();
	public void setStrategies(StrategiesT strategies);

	public Map<StrategyT, StrategyUI> getStrategyUIMap();
	public void setStrategyUIMap(Map<StrategyT, StrategyUI> strategyUIMap);

	public StrategyT getSelectedStrategy();
	public void setSelectedStrategy(StrategyT selectedStrategy);

	public void setShowStrategyDescription(boolean showStrategyDescription);
	public boolean isShowStrategyDescription();

	public void setShowTimezoneSelector(boolean showTimezoneSelector);
	public boolean isShowTimezoneSelector();
	
	public void setShowValidateOutputSection(boolean showValidateOutputSection);
	public boolean isShowValidateOutputSection();

	public void setShowCompositePanelOkCancelButtonSection(boolean showCompositePanelOkCancelButtonSection);
	public boolean isShowCompositePanelOkCancelButtonSection();

	public List<StrategyT> getStrategiesFilteredStrategyList();

	public boolean isUsePreCachedStrategyPanels();
	public void setUsePreCachedStrategyPanels(boolean aUsePreCachedStrategyPanels);
	
	public boolean isTreatControlVisibleFalseAsNull();
	public boolean isTreatControlEnabledFalseAsNull();
	public boolean isRestoreLastNonNullStateControlValueBehavior();
	
	public boolean isShowEnabledCheckboxOnOptionalClockControl();

// 3/20/2010 Scott Atwell	public Integer getDefaultDigitsForSpinnerControl();
	public int getDefaultDigitsForSpinnerControl( ParameterT aParameter );
	
	public Integer getStrategyDropDownItemDepth();
	
	public boolean isSelectedStrategyValidated();
	public void setSelectedStrategyValidated(boolean aSelectedStrategyValidated);
	
	public boolean isCatchAllStrategyLoadExceptions();
	public boolean isCatchAllValidationExceptions();
	public boolean isCatchAllRuntimeExceptions();
	public boolean isCatchAllMainlineExceptions();
	public void setCatchAllStrategyLoadExceptions(boolean aCatchAllStrategyLoadExceptions);
	public void setCatchAllValidationExceptions(boolean aCatchAllStrategyLoadExceptions);
	public void setCatchAllRuntimeExceptions(boolean aCatchAllRuntimeExceptions);
	public void setCatchAllMainlineExceptions(boolean aCatchAllMainlineExceptions);

	public void setDebugLoggingLevel( boolean aDebugLevelFlag );
	public boolean isDebugLoggingLevel();
}
