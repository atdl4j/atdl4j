package org.atdl4j.ui.swt.app.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.impl.AbstractStrategySelectionPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * Represents the SWT-specific available strategy choices GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SWTStrategySelectionPanel 
	extends AbstractStrategySelectionPanel
{
	private final Logger logger = Logger.getLogger(SWTStrategySelectionPanel.class);
	
	private Composite dropdownComposite;
	private Combo strategiesDropDown;
	public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildStrategySelectionPanel( (Composite) parentOrShell, atdl4jOptions );
	}
	
	public Composite buildStrategySelectionPanel(Composite aParentComposite, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		
		// Strategy selector dropdown
		dropdownComposite = new Composite(aParentComposite, SWT.NONE);
		GridLayout dropdownLayout = new GridLayout(2, false);
		dropdownComposite.setLayout(dropdownLayout);
		dropdownComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// label
		Label strategiesDropDownLabel = new Label(dropdownComposite, SWT.NONE);
		strategiesDropDownLabel.setText("Strategy");
		// dropDownList
		strategiesDropDown = new Combo(dropdownComposite, SWT.READ_ONLY | SWT.BORDER);
		strategiesDropDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		// -- Increase font size for Drop Down --
		FontData[] fontData = strategiesDropDown.getFont().getFontData(); 
		for(int i = 0; i < fontData.length; ++i)
		{
		    fontData[i].setHeight( fontData[i].getHeight() + 3 ); 
		    fontData[i].setStyle( SWT.BOLD );
		}
		
		final Font newFont = new Font(strategiesDropDown.getDisplay(), fontData); 
		strategiesDropDown.setFont(newFont); 
		 
		// Since you created the font, you must dispose it 
		strategiesDropDown.addDisposeListener(new DisposeListener()
		{
		    public void widgetDisposed(DisposeEvent e) 
		    { 
		        newFont.dispose(); 
		    } 
		}); 
		
// TODO wish to avoid issue with changing the font causes the initial combo box display to be very narrow 
	
		if ( Atdl4jConfig.getConfig().getStrategyDropDownItemDepth() != null )
		{
			strategiesDropDown.setVisibleItemCount( Atdl4jConfig.getConfig().getStrategyDropDownItemDepth().intValue() );
		}
		// tooltip
		strategiesDropDown.setToolTipText("Select a Strategy");
		// action listener
		strategiesDropDown.addSelectionListener(new SelectionAdapter() 
			{
				@Override
				public void widgetSelected(SelectionEvent event) 
				{
					int index = strategiesDropDown.getSelectionIndex();
					logger.debug( "strategiesDropDown.widgetSelected.  strategiesDropDown.getSelectionIndex(): " + index );					
					selectDropDownStrategy( index );
				}
			} 
		);
	
		return dropdownComposite;
	}

	
	public void loadStrategyList( List<StrategyT> aStrategyList )
	{
		// remove all dropdown items
		strategiesDropDown.removeAll();
		setStrategiesList( aStrategyList );

		if ( getStrategiesList() == null )
		{
			return;
		}

		for (StrategyT tempStrategy : getStrategiesList()) 
		{
			logger.debug( "loadStrategyList() [" + strategiesDropDown.getItemCount() + "] strategiesDropDown.add: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
			
			// create dropdown item for strategy
			strategiesDropDown.add( Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
		}
		
		dropdownComposite.layout(); 
	}


	public void selectDropDownStrategy(int index) 
	{
		logger.debug( "selectDropDownStrategy() index: " + index );
		
		if ( getStrategiesList().size() != strategiesDropDown.getItemCount() )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: getStrategiesList().size(): " + getStrategiesList().size() + " does NOT MATCH strategiesDropDown.getItemCount(): " + strategiesDropDown.getItemCount() );
		}
		
		strategiesDropDown.select( index );
		
		StrategyT tempStrategy = getStrategiesList().get( index );
		
		if ( ! strategiesDropDown.getItem( index ).equals( Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) ) )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: strategiesDropDown.getItem(" + index + "): " + strategiesDropDown.getItem( index ) + " DID NOT MATCH tempStrategy: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
		}
		
		fireStrategySelectedEvent( tempStrategy );
	}

	
	public void selectDropDownStrategyByStrategyName(String aStrategyName) throws Atdl4jClassLoadException 
	{
		logger.debug( "selectDropDownStrategyByStrategyName() aStrategyName: " + aStrategyName );
		
		if ( getStrategiesList().size() != strategiesDropDown.getItemCount() )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: getStrategiesList().size(): " + getStrategiesList().size() + " does NOT MATCH strategiesDropDown.getItemCount(): " + strategiesDropDown.getItemCount() );
		}
			
		for (int i = 0; i < getStrategiesList().size(); i++) 
		{
			StrategyT tempStrategy = getStrategiesList().get( i );
			
			if ( aStrategyName.equals( tempStrategy.getName() ) )
			{
				logger.debug( "selectDropDownStrategyByStrategyName() invoking selectDropDownStrategy( " + i + " )" );
				selectDropDownStrategy( i );
			}
		}
	}

	public void selectDropDownStrategyByStrategyWireValue( String aStrategyWireValue ) throws Atdl4jClassLoadException 
	{
		logger.debug( "selectDropDownStrategyByStrategyWireValue() aStrategyWireValue: " + aStrategyWireValue );
		
		if ( getStrategiesList().size() != strategiesDropDown.getItemCount() )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: getStrategiesList().size(): " + getStrategiesList().size() + " does NOT MATCH strategiesDropDown.getItemCount(): " + strategiesDropDown.getItemCount() );
		}
			
		for (int i = 0; i < getStrategiesList().size(); i++) 
		{
			StrategyT tempStrategy = getStrategiesList().get( i );
			
			if ( aStrategyWireValue.equals( tempStrategy.getWireValue() ) )
			{
				logger.debug( "selectDropDownStrategyByStrategyWireValue() invoking selectDropDownStrategy( " + i + " )" );
				selectDropDownStrategy( i );
			}
		}
	}
	
	public void selectFirstDropDownStrategy() throws Atdl4jClassLoadException
	{
		if ( ( strategiesDropDown != null ) && 
			  ( strategiesDropDown.getItemCount() > 0 ) )
		{
			strategiesDropDown.deselectAll();
			logger.debug( "selectFirstDropDownStrategy() invoking selectDropDownStrategy( 0 )" );
			selectDropDownStrategy( 0 );
		}
	}
}
