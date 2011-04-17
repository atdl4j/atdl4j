package org.atdl4j.ui.swing.impl;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
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
	protected static final Logger logger = Logger.getLogger( SwingWidgetFactory.class );

	// Used to create a single parameter widget
	public static SwingWidget<?> createWidget(JPanel parent, ControlT control, ParameterT parameter, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory) throws Atdl4jClassLoadException
	{
		SwingWidget<?> parameterWidget = null;
	
		logger.debug( "createWidget() invoked " + "with parms parent: " + parent
				+ " control: " + control + " parameter: " + parameter + " style: " + style );
	
		parameterWidget = (SwingWidget<?>) aAtdl4jWidgetFactory.create( control, parameter );
	
		logger.debug( "createWidget() returned parameterWidget: " + parameterWidget );
	
		parameterWidget.createWidget(parent);
		logger.debug( "createWidget() completed.  parameterWidget: " + parameterWidget );
	
		parameterWidget.applyConstOrInitValues();
	
		return parameterWidget;
	}
}