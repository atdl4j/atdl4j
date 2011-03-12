package org.atdl4j.ui.swing.app.impl;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractAtdl4jUserMessageHandler;


/**
 * Represents the Swing-specific GUI pop-up message screen support.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SwingAtdl4jUserMessageHandler 
	extends AbstractAtdl4jUserMessageHandler
{
	private final Logger logger = Logger.getLogger(SwingAtdl4jUserMessageHandler.class);
	
	private JFrame parentComposite;
	
	public void init(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		init( (JFrame) parentOrShell, atdl4jOptions );
	}
	
	public void init(JFrame aParentComposite, Atdl4jOptions atdl4jOptions)
	{
		parentComposite = aParentComposite;
		setAtdl4jOptions( atdl4jOptions );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jUserMessageHandler#displayException(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void displayException(String aTitle, String aMsgText, Throwable e)
	{
		String txt = "";
		//TODO: remove
		e.printStackTrace();
		
		if ( aTitle != null )
		{
			txt += aTitle;
			logger.error( aTitle, e );
		}
		else
		{
			txt += "Exception";
			logger.error( "Exception: ", e );
		}
		
		String msg = extractExceptionMessage( e );
		
		if ( ( aMsgText != null ) && ( aMsgText.length() > 0 ) )
		{
			txt += aMsgText + "\n\n" + msg;
		}
		else
		{
			txt += msg ;
		}
		
		JOptionPane.showMessageDialog(parentComposite, txt, aTitle, JOptionPane.ERROR_MESSAGE);
		
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jUserMessageHandler#displayException(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void displayMessage(String aTitle, String aMsgText)
	{
		JOptionPane.showMessageDialog(parentComposite, aMsgText, aTitle, JOptionPane.INFORMATION_MESSAGE);
	}

}
