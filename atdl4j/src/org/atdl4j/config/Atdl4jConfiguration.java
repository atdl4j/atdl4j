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
	// -- UI Infrastructure --
	/**
	 * @return the strategiesUIFactory
	 */
//TODO 9/26/2010 Scott Atwell	public StrategiesUIFactory getStrategiesUIFactory();

	public void setDebugLoggingLevel( boolean aDebugLevelFlag );
	public boolean isDebugLoggingLevel();
	
	public String getClassNameControlUIForCheckBoxListT();
	public String getClassNameControlUIForCheckBoxT();
	public String getClassNameControlUIForClockT();
	public String getClassNameControlUIForDoubleSpinnerT();
	public String getClassNameControlUIForDropDownListT();
	public String getClassNameControlUIForEditableDropDownListT();
	public String getClassNameControlUIForHiddenFieldT();
	public String getClassNameControlUIForLabelT();
	public String getClassNameControlUIForMultiSelectListT();
	public String getClassNameControlUIForRadioButtonListT();
	public String getClassNameControlUIForRadioButtonT();
	public String getClassNameControlUIForSingleSelectListT();
	public String getClassNameControlUIForSingleSpinnerT();
	public String getClassNameControlUIForSliderT();
	public String getClassNameControlUIForTextFieldT();


	public String getClassNameAtdl4jCompositePanel();
	public String getClassNameAtdl4jInputAndFilterDataPanel();
	public String getClassNameAtdl4jInputAndFilterDataSelectionPanel();
	public String getClassNameAtdl4jTesterPanel();
	public String getClassNameAtdl4jUserMessageHandler();
	public String getClassNameControlUIFactory();
	public String getClassNameFixatdlFileSelectionPanel();
	public String getClassNameFixMsgLoadPanel();
	public String getClassNameStrategiesUI();
	public String getClassNameStrategyDescriptionPanel();
	public String getClassNameStrategyPanelHelper();
	public String getClassNameStrategySelectionPanel();
	public String getClassNameStrategyUI();
	public String getClassNameTypeConverterFactory();
}
