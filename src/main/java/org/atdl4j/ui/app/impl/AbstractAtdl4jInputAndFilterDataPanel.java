/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.app.impl;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel;
import org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanelListener;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;

/**
 * Represents the base, non-GUI system-specific Atdl4jOptions and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public abstract class AbstractAtdl4jInputAndFilterDataPanel
	implements Atdl4jInputAndFilterDataPanel
{
	
	public static final String STRATEGY_FILTER_PANEL_NAME = "FIXatdl Strategy Filter";
	public static final String STANDARD_FIX_FIELDS_PANEL_NAME = "FIX Fields from Order";
	
	public static final int DEFAULT_DROP_DOWN_VISIBLE_ITEM_COUNT = 20;
	
	public static final String[] DEFAULT_STRATEGY_FILTER_COUNTRY_SUBSET_LIST = new String[] { "", "US", "CA", "BR", "UK", "FR", "DE", "JP", "HK", "AU" };  // just to seed it with some  
	public static final String[] DEFAULT_STRATEGY_FILTER_MIC_CODE_SUBSET_LIST = new String[] { "", "XNYS", "XNAS", "XBMF", "XLSE", "XPAR", "XFRA", "XETR", "XTKS", "XHKG", "XASX" };  // just to seed it with some  
	
	public static final String[] DEFAULT_FIX_FIELD_ORD_TYPE_SUBSET_LIST = new String[] { "", "1", "2", "3", "4", "6", "7", "8", "9", "D", "E", "G", "I", "J", "K", "P", "Q" };  // just to seed it with some 
	public static final String FIX_FIELD_NAME_ORD_TYPE = "FIX_OrdType";  // tag 40
	
	public static final String[] DEFAULT_FIX_FIELD_SIDE_SUBSET_LIST = new String[] { "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C" };  // just to seed it with some 
	public static final String FIX_FIELD_NAME_SIDE = "FIX_Side";  // tag 54
	
	public static final String[] DEFAULT_FIX_FIELD_ORDER_QTY_SUBSET_LIST = new String[] { "", "10", "100", "1000", "10000", "100000" };  // just to seed it with some  
	public static final String FIX_FIELD_NAME_ORDER_QTY = "FIX_OrderQty";  // tag 38 
	
	public static final String[] DEFAULT_FIX_FIELD_PRICE_SUBSET_LIST = new String[] { "", "1.00", "5.00", "10", "10.00", "10.75", "25.00", "50.00" };  // just to seed it with some  
	public static final String FIX_FIELD_NAME_PRICE = "FIX_Price";  // tag 44
	
	public static final String[] DEFAULT_FIX_FIELD_HANDL_INST_SUBSET_LIST = new String[] { "", "1", "2", "3" };  // just to seed it with some  
	public static final String FIX_FIELD_NAME_HANDL_INST = "FIX_HandlInst";  // tag 21
	
	public static final String[] DEFAULT_FIX_FIELD_EXEC_INST_SUBSET_LIST = new String[] { ""  };  // just to seed it with some  
	public static final String FIX_FIELD_NAME_EXEC_INST = "FIX_ExecInst";  // tag 21 (note MultipleCharValue)
	
	public static final String[] DEFAULT_FIX_FIELD_TIME_IN_FORCE_SUBSET_LIST = new String[] { "", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };  // just to seed it with some  
	public static final String FIX_FIELD_NAME_TIME_IN_FORCE = "FIX_TimeInForce";  // tag 59
	
	public static final String[] DEFAULT_FIX_FIELD_CL_ORD_LINK_ID_SUBSET_LIST = new String[] { "", "COMMON_ID_1", "COMMON_ID_2" };  // just to seed it with some  
	public static final String FIX_FIELD_NAME_CL_ORD_LINK_ID = "FIX_ClOrdLinkID";  // tag 583
	
	
	Atdl4jOptions atdl4jOptions;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, JDialog, etc
	private Atdl4jUserMessageHandler atdl4jUserMessageHandler;
	
	private List<Atdl4jInputAndFilterDataPanelListener> listenerList = new ArrayList<>();

	protected void init( Object aParentOrShell, Atdl4jOptions aAtdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler )
	{
		setAtdl4jOptions( aAtdl4jOptions );
		setParentOrShell( aParentOrShell );
		
		setAtdl4jUserMessageHandler( aAtdl4jUserMessageHandler );
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

	public Atdl4jUserMessageHandler getAtdl4jUserMessageHandler() {
		return atdl4jUserMessageHandler;
	}

	public void setAtdl4jUserMessageHandler(Atdl4jUserMessageHandler atdl4jUserMessageHandler) {
		this.atdl4jUserMessageHandler = atdl4jUserMessageHandler;
	}


}
