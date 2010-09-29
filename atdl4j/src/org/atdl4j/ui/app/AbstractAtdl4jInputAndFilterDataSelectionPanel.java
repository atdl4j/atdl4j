/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.InputAndFilterData;

/**
 * Represents the base, non-GUI specific component used to invoke Atdl4jInputAndFilterDataPanel pop-up.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public abstract class AbstractAtdl4jInputAndFilterDataSelectionPanel
	implements Atdl4jInputAndFilterDataSelectionPanel,
		Atdl4jInputAndFilterDataPanelListener
{
	private final Logger logger = Logger.getLogger(AbstractAtdl4jInputAndFilterDataSelectionPanel.class);

	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private Atdl4jInputAndFilterDataPanel atdl4jInputAndFilterDataPanel;

	private List<Atdl4jInputAndFilterDataPanelListener> listenerList = new Vector<Atdl4jInputAndFilterDataPanelListener>();

// 9/29/2010 Scott Atwell Added
	private Atdl4jUserMessageHandler atdl4jUserMessageHandler;
	
// 9/29/2010 	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions )
	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler )
	{
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		setAtdl4jUserMessageHandler( aAtdl4jUserMessageHandler );
		
		// -- FixMsgLoadPanel (Load Message button/text field) - build() method called via concrete class --
//		setAtdl4jInputAndFilterDataPanel( getAtdl4jOptions().getAtdl4jInputAndFilterDataPanel() );
		setAtdl4jInputAndFilterDataPanel( getAtdl4jInputAndFilterDataPanel() );
		getAtdl4jInputAndFilterDataPanel().addListener( this );
	}

	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return this.atdl4jOptions;
	}

	/**
	 * @param aAtdl4jOptions the atdl4jOptions to set
	 */
	private void setAtdl4jOptions(Atdl4jOptions aAtdl4jOptions)
	{
		this.atdl4jOptions = aAtdl4jOptions;
	}

	/**
	 * @return the parentOrShell
	 */
	public Object getParentOrShell()
	{
		return this.parentOrShell;
	}

	/**
	 * @param aParentOrShell the parentOrShell to set
	 */
	private void setParentOrShell(Object aParentOrShell)
	{
		this.parentOrShell = aParentOrShell;
	}
	

	public void addListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener )
	{
		listenerList.add( aAtdl4jInputAndFilterCriteriaPanelListener );
	}

	public void removeListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener )
	{
		listenerList.remove( aAtdl4jInputAndFilterCriteriaPanelListener );
	}	
	
	protected void fireInputAndFilterDataSpecifiedEvent( InputAndFilterData aInputAndFilterData )
	{
		for ( Atdl4jInputAndFilterDataPanelListener tempListener : listenerList )
		{
			tempListener.inputAndFilterDataSpecified( aInputAndFilterData );
		}
	}
	
	/**
	 * @param atdl4jInputAndFilterDataPanel the atdl4jInputAndFilterDataPanel to set
	 */
	public void setAtdl4jInputAndFilterDataPanel(Atdl4jInputAndFilterDataPanel atdl4jInputAndFilterDataPanel)
	{
		this.atdl4jInputAndFilterDataPanel = atdl4jInputAndFilterDataPanel;
	}

	/**
	 * @return the atdl4jInputAndFilterDataPanel
	 */
//	public Atdl4jInputAndFilterDataPanel getAtdl4jInputAndFilterDataPanel()
//	{
//		return atdl4jInputAndFilterDataPanel;
//	}

	/* 
	 * Re-fire to listeners who have registered with us.
	 */
	public void inputAndFilterDataSpecified(InputAndFilterData aInputAndFilterData)
	{
		fireInputAndFilterDataSpecifiedEvent( aInputAndFilterData );
	}
	
	
	/**
	 * @return the Atdl4jInputAndFilterDataPanel
	 */
	public Atdl4jInputAndFilterDataPanel getAtdl4jInputAndFilterDataPanel() 
	{
		if ( ( atdl4jInputAndFilterDataPanel == null ) && ( Atdl4jConfig.getConfig().getClassNameAtdl4jInputAndFilterDataPanel() != null ) )
		{
			String tempClassName = Atdl4jConfig.getConfig().getClassNameAtdl4jInputAndFilterDataPanel();
			logger.debug( "getAtdl4jInputAndFilterDataPanel() loading class named: " + tempClassName );
			try
			{
				atdl4jInputAndFilterDataPanel = ((Class<Atdl4jInputAndFilterDataPanel>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return atdl4jInputAndFilterDataPanel;
	}

	/**
	 * @return the atdl4jUserMessageHandler
	 */
	protected Atdl4jUserMessageHandler getAtdl4jUserMessageHandler()
	{
		return this.atdl4jUserMessageHandler;
	}

	/**
	 * @param aAtdl4jUserMessageHandler the atdl4jUserMessageHandler to set
	 */
	protected void setAtdl4jUserMessageHandler(Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
	{
		this.atdl4jUserMessageHandler = aAtdl4jUserMessageHandler;
	}
}
