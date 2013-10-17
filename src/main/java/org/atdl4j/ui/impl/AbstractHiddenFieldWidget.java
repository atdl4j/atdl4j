package org.atdl4j.ui.impl;

import org.atdl4j.data.ParameterHelper;
import org.atdl4j.fixatdl.layout.HiddenFieldT;

public abstract class AbstractHiddenFieldWidget
		extends AbstractAtdl4jWidget<String>
{

	protected String value;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractAtdl4jWidget.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jOptions aAtdl4jOptions) throws JAXBException
	 * public AbstractHiddenFieldWidget(HiddenFieldT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * this.setValue(getConstInitValue()); init(); }
	 **/
	// -- Overriden --
	protected void initPreCheck()
	{
		this.setValue( getConstInitValue() );
	}

	private String getConstInitValue()
	{
		return ( (HiddenFieldT) control ).getInitValue();
	}

	public String getControlValueRaw()
	{
		return value;
	}

	public Object getParameterValue()
	{
		if ( ParameterHelper.getConstValue( parameter ) != null )
		{
			return ParameterHelper.getConstValue( parameter );
		}
		
		// -- Better handles cases where Control may display a value differently than Parameter (eg PercentageT appearing "12.34%" in Control but 0.1234 in Parameter) --
		return controlConverter.convertControlValueToParameterValue( value );
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void setEnabled(boolean enabled)
	{
		// Do nothing
	}

	public void setVisible(boolean visible)
	{
		// Do nothing
	}
	
	public boolean isEnabled()
	{
		return false;
	}
	
	public boolean isVisible()
	{
		return false;
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
		return false;
	}

	public void setControlExcludingLabelEnabled(boolean aEnabled)
	{
		// Do nothing
	}

}
