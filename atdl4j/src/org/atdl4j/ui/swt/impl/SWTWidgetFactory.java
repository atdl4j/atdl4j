package org.atdl4j.ui.swt.impl;

import org.eclipse.swt.widgets.Composite;
import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.swt.SWTWidget;

/**
 * 
 * This class contains the data associated with the <code>SWTWidgetFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 9:05:33 PM)
 * @author Scott Atwell
 */
public class SWTWidgetFactory
{
	protected static final Logger logger = Logger.getLogger( SWTWidgetFactory.class );

	// Used to create a single parameter widget
	public static SWTWidget<?> createWidget(Composite parent, ControlT control, ParameterT parameter, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory)
	{
		SWTWidget<?> parameterWidget = null;
	
		logger.debug( "createWidget() invoked " + "with parms parent: " + parent
				+ " control: " + control + " parameter: " + parameter + " style: " + style );
	
		parameterWidget = (SWTWidget<?>) aAtdl4jWidgetFactory.create( control, parameter );
	
		logger.debug( "createWidget() returned parameterWidget: " + parameterWidget );
	
		parameterWidget.createWidget( parent, style );
		logger.debug( "createWidget() completed.  parameterWidget: " + parameterWidget );
	
		parameterWidget.applyConstOrInitValues();
	
		return parameterWidget;
	}
}
