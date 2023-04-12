package org.atdl4j.ui.swt.impl;

import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	protected static final Logger logger = LoggerFactory.getLogger( SWTWidgetFactory.class );

	// Used to create a single parameter widget
	public static SWTWidget<?> createWidget(Composite parent, ControlT control, ParameterT parameter, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory)
	{
		SWTWidget<?> parameterWidget = null;
	
		logger.debug( "createWidget() invoked " + "with parms parent: {} control: {} parameter: {} style: {}", parent, control, parameter, style );
	
		parameterWidget = (SWTWidget<?>) aAtdl4jWidgetFactory.create( control, parameter );
	
		logger.debug( "createWidget() returned parameterWidget: {}", parameterWidget );
	
		parameterWidget.createWidget( parent, style );
		logger.debug( "createWidget() completed.  parameterWidget: {}", parameterWidget );
	
		parameterWidget.applyConstOrInitValues();
	
		return parameterWidget;
	}
}
