/*
 * Created on Feb 9, 2010
 *
 */
package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;

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
public interface ControlUIFactory
{
// 2/9/2010 Scott Atwell added	
	// -- Call this after constructor --
	public void init(Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();

 	/* 
 	 * @param control
 	 * @param parameter
 	 * @return (for SWT returns SWTWidget<?>)
 	 */
 	public ControlUI<?> create(ControlT control, ParameterT parameter);

}
