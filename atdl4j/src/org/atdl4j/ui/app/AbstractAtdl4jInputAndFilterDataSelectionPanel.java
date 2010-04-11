/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jConfig;
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

	Atdl4jConfig atdl4jConfig;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private Atdl4jInputAndFilterDataPanel atdl4jInputAndFilterDataPanel;

	private List<Atdl4jInputAndFilterDataPanelListener> listenerList = new Vector<Atdl4jInputAndFilterDataPanelListener>();

	protected void init( Object aParentOrShell, Atdl4jConfig aAtdl4jConfig )
	{
		setAtdl4jConfig( aAtdl4jConfig );
		setParentOrShell( aParentOrShell );
		
		
		
		// -- FixMsgLoadPanel (Load Message button/text field) - build() method called via concrete class --
		setAtdl4jInputAndFilterDataPanel( getAtdl4jConfig().getAtdl4jInputAndFilterDataPanel() );
		getAtdl4jInputAndFilterDataPanel().addListener( this );
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return this.atdl4jConfig;
	}

	/**
	 * @param aAtdl4jConfig the atdl4jConfig to set
	 */
	private void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		this.atdl4jConfig = aAtdl4jConfig;
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
	public Atdl4jInputAndFilterDataPanel getAtdl4jInputAndFilterDataPanel()
	{
		return atdl4jInputAndFilterDataPanel;
	}

	/* 
	 * Re-fire to listeners who have registered with us.
	 */
	public void inputAndFilterDataSpecified(InputAndFilterData aInputAndFilterData)
	{
		fireInputAndFilterDataSpecifiedEvent( aInputAndFilterData );
	}
}
