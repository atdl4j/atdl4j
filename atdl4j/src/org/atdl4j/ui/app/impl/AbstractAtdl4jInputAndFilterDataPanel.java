/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.app.impl;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanelListener;

/**
 * Represents the base, non-GUI system-specific Atdl4jOptions and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public abstract class AbstractAtdl4jInputAndFilterDataPanel
	implements Atdl4jInputAndFilterDataPanel
{

	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private List<Atdl4jInputAndFilterDataPanelListener> listenerList = new Vector<Atdl4jInputAndFilterDataPanelListener>();

	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions )
	{
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
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


}
