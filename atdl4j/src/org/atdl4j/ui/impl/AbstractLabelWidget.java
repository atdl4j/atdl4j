package org.atdl4j.ui.impl;


/* 
 * This class intentionally does not support parameterRef or initValue
 * which LabelT inherits from ControlT.
 * 
 * Use HiddenField if you'd like to have control associated with a parameter. 
 */

public abstract class AbstractLabelWidget extends AbstractAtdl4jWidget<String> 
{
	// -- Overriden --
	protected void initPreCheck()
	{
		this.parameter = null;
	}
	
	public void setValue(String value) {
		// do nothing
	}
	

	public String getControlValueRaw() 
	{
		return null; // Labels cannot store values
	}

	public Object getParameterValue() {
		return null; // Labels cannot store values
	}
	
	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// Do nothing (do not need to adjust appearance of the control)
	}

	/**
	 * 
	 */
	public void processConstValueHasBeenSet()
	{
		// -- no operation -- 
	}

	public boolean isControlExcludingLabelEnabled()
	{
		return isEnabled();
	}

	public void setControlExcludingLabelEnabled(boolean aEnabled)
	{
		setEnabled( aEnabled );
	}
}
