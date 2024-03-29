package org.atdl4j.ui.swing.impl;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.swing.SwingWidget;

/**
 * 
 * This class contains the data associated with the <code>SwingWidgetFactory</code>.
 * 
 * Creation date: (Oct 4, 2010 9:05:33 PM)
 * @author Scott Atwell
 */
public class SwingWidgetFactory
{
	protected static final Logger logger = LoggerFactory.getLogger( SwingWidgetFactory.class );

	// Used to create a single parameter widget
	public SwingWidget<?> createWidget(JPanel parent, ControlT control, ParameterT parameter, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory)
	{
		SwingWidget<?> parameterWidget = null;
	
		logger.debug( "createWidget() invoked with parms parent: {} control: {} parameter: {} style: {}", parent, control, parameter, style );
	
		parameterWidget = (SwingWidget<?>) aAtdl4jWidgetFactory.create( control, parameter );
	
		logger.debug( "createWidget() returned parameterWidget: {}", parameterWidget );
	
		parameterWidget.createWidget(parent);
		logger.debug( "createWidget() completed.  parameterWidget: {}", parameterWidget );
	
		parameterWidget.applyConstOrInitValues();
	
		return parameterWidget;
	}
	
}