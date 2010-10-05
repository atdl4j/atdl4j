package org.atdl4j.ui.swt.app.impl;


import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractAtdl4jUserMessageHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;


/**
 * Represents the SWT-specific GUI pop-up message screen support.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SWTAtdl4jUserMessageHandler 
	extends AbstractAtdl4jUserMessageHandler
{
	private final Logger logger = Logger.getLogger(SWTAtdl4jUserMessageHandler.class);
	
	private Composite parentComposite;
	
	public void init(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		init( ((Composite) parentOrShell).getShell(), atdl4jOptions );
	}
	
	public void init(Composite aParentComposite, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		setParentComposite( aParentComposite );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jUserMessageHandler#displayException(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void displayException(String aTitle, String aMsgText, Throwable e)
	{
		MessageBox messageBox = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
		
		if ( aTitle != null )
		{
			messageBox.setText( aTitle );
			logger.warn( aTitle, e );
		}
		else
		{
			messageBox.setText( "Exception" );
			logger.warn( "Exception: ", e );
		}
		
		String msg = extractExceptionMessage( e );
		
		if ( ( aMsgText != null ) && ( aMsgText.length() > 0 ) )
		{
			messageBox.setMessage(aMsgText + "\n\n" + msg);
		}
		else
		{
			messageBox.setMessage( "" + msg );
		}
		
		messageBox.open();
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jUserMessageHandler#displayException(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void displayMessage(String aTitle, String aMsgText)
	{
		MessageBox messageBox = new MessageBox(getShell(), SWT.OK | SWT.ICON_INFORMATION );
		
		if ( aTitle != null )
		{
			messageBox.setText( aTitle );
		}
		
		messageBox.setMessage( aMsgText );
		messageBox.open();
	}

	/**
	 * @return the parentComposite
	 */
	private Composite getParentComposite()
	{
		return this.parentComposite;
	}

	/**
	 * @param aParentComposite the parentComposite to set
	 */
	private void setParentComposite(Composite aParentComposite)
	{
		this.parentComposite = aParentComposite;
	}

	/**
	 * Returns getParentComposite().getShell().
	 * @return the shell
	 */
	private Shell getShell()
	{
		if ( getParentComposite() != null )
		{
			return getParentComposite().getShell();
		}
		else
		{
			return null;
		}
	}

	
}
