
package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;

/**
 * An interface for an UI widget which will be 
 * stores its underlying value in a parameter field.
 */
public interface ControlUI<E extends Comparable<?>> 
{
	public void init(ControlT control, ParameterT parameter, Atdl4jOptions aAtdl4jOptions);

	public void reinit();

	public boolean isNullValue();
	public void setNullValue(Boolean aNullValue);
	public Boolean getNullValue();

	public ParameterT getParameter();
	
	public ControlT getControl();
		
	// Control value accessor methods
	
	/**
	 * Will return null if isNullValue() is true, otherwise returns getControlValueRaw()
	 * @return
	 */
	public E getControlValue();
	
	/**
	 * Will return whatever value the Control has regardless of isNullValue()
	 * @return
	 */
	public E getControlValueRaw();
	
	public Comparable<?> getControlValueAsComparable();
	
	// Parameter value accessor methods
	
	public Object getParameterValue();
	
	public String getParameterFixWireValue();
	
	public Comparable<?> getParameterValueAsComparable();
	
	// Value mutator methods
	
	public void setValue(E value);
		
	/* 
	 * This method handles string matching Atdl4jConstants.VALUE_NULL_INDICATOR and invoking setNullValue().
	 */
	public void setValueAsString(String value);

	// Helper methods
	
	public Comparable<?> convertStringToControlComparable(String string);
	
	public Comparable<?> convertParameterStringToParameterComparable(String aParameterString );

	// FIX methods
	
	public String getFIXValue();
	
	public void getFIXValue(FIXMessageBuilder builder);

	// UI methods
	
	public void setVisible(boolean visible);
	public boolean isVisible();
	
	public void setEnabled(boolean enabled);	
	public boolean isEnabled();
	
	public int getFIXType();
	
	public E getLastNonNullStateControlValueRaw();
	
	/**
	 * Used when pre-populating a control with its FIX message wire value 
	 * For example: PercentageT with isMultiplyBy100() == true would have ".1234" on the wire for "12.34" displayed/stored by the control (for 12.34%). 
	 * @param aFIXValue
	 */
	public void setFIXValue( String aFIXValue );
	
	public void applyConstOrInitValues();
	
	public void processConstValueHasBeenSet();
	
	public void setControlExcludingLabelEnabled(boolean enabled);	
	public boolean isControlExcludingLabelEnabled();
	
	public boolean isHiddenFieldForInputAndFilterData();
	public void setHiddenFieldForInputAndFilterData(boolean aBoolean);
	
	public StrategyPanelT getParentStrategyPanel();
	public void setParentStrategyPanel( StrategyPanelT aStrategyPanel );
	
	public Object getParent();
	public void setParent( Object aParent );

}