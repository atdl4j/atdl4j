/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.swt.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractAtdl4jTesterApp;
import org.atdl4j.ui.swt.config.SWTAtdl4jConfiguration;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Represents the SWT-specific "TesterApp" with a main() line.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for
 *      AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel
 *      ->AbstractAtdl4jCompositePanel layering structure. *
 * 
 *      Creation date: (Feb 28, 2010 6:26:02 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SWTAtdl4jTesterApp
		extends AbstractAtdl4jTesterApp
{
	public final Logger logger = Logger.getLogger( SWTAtdl4jTesterApp.class );
	private Shell shell;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
	    	Atdl4jConfiguration config = new SWTAtdl4jConfiguration();
		SWTAtdl4jTesterApp tempSWTAtdl4jTesterApp = new SWTAtdl4jTesterApp();
		try
		{
			tempSWTAtdl4jTesterApp.mainLine( args, config );
		}
		catch (Exception e)
		{
			if ( Atdl4jConfig.getConfig().isCatchAllMainlineExceptions() )
			{
				tempSWTAtdl4jTesterApp.logger.warn( "Fatal Exception in mainLine", e );
			}
			else
			{
				throw e;
			}
		}
	}

	public void mainLine(String[] args, Atdl4jConfiguration config) throws Exception
	{
		// -- Setup SWT-specific Display and Shell --
		Display display = new Display();
		shell = new Shell( display );
		GridLayout shellLayout = new GridLayout( 1, true );
		shell.setLayout( shellLayout );
		// shell.setText(
		// "atdl4j(sm)- The Open-Source Java Solution for FIXatdl(sm)" );
		// \u2120 should work but just get square box shell.setText(
		// "atdl4j\u2120 - The Open-Source Java Solution for FIXatdl\u2120" );
		shell.setText( "atdl4j - The Open-Source Java Solution for FIXatdl" );

		// -- Delegate setup to AbstractAtdl4jTesterApp, construct a new
		// SWT-specific Atdl4jOptions --
		init( args, config, new Atdl4jOptions(), shell );

		// -- Build the SWT.Composite from Atdl4jTesterPanel (** core GUI component **) --
		getAtdl4jTesterPanel().buildAtdl4jTesterPanel( shell, getAtdl4jOptions() );

		// -- SWT-specific stuff to improve layout --
		// screen sizing works better with pack() after open() shell.pack();
		shell.open();
		shell.pack();

		// -- avoid postage stamp size at start --
		shell.setSize( shell.computeSize( 475, 500 ) );

		// -- SWT-specific stuff to keep Display and Shell active --
		while ( !shell.isDisposed() )
		{
			try
			{
				if ( !display.readAndDispatch() )
				{
					display.sleep();
				}
			}
			catch (Exception e)
			{
				if ( Atdl4jConfig.getConfig().isCatchAllRuntimeExceptions() )
				{
					logger.warn( "Fatal Exception encountered", e );
					if ( ( getAtdl4jTesterPanel() != null ) && 
						  ( getAtdl4jTesterPanel().getAtdl4jCompositePanel() != null ) && 
						  ( getAtdl4jTesterPanel().getAtdl4jCompositePanel().getAtdl4jUserMessageHandler() != null ) )
					{
						getAtdl4jTesterPanel().getAtdl4jCompositePanel().getAtdl4jUserMessageHandler().displayException( "Fatal Exception encountered", "", e );

					}
				}
				else
				{
					throw e;
				}
			}
		}

		display.dispose();
	}

}
