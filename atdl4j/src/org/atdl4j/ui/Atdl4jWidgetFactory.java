/*
 * Created on Feb 9, 2010
 *
 */
package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;

/**
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.
 * 
 * Creation date: (Feb 9, 2010 8:37:54 AM)
 * @author Scott Atwell
 * @version 1.0, Feb 9, 2010
 */
public interface Atdl4jWidgetFactory
{
	// -- Call this after constructor --
	public void init(Atdl4jOptions aAtdl4jOptions);

	public Atdl4jOptions getAtdl4jOptions();

 	/* 
 	 * @param control
 	 * @param parameter
 	 * @return (for SWT returns SWTWidget<?>)
 	 */
 	public Atdl4jWidget<?> create(ControlT control, ParameterT parameter);

	public Atdl4jWidget createHiddenFieldT(HiddenFieldT control, ParameterT parameter);
}
