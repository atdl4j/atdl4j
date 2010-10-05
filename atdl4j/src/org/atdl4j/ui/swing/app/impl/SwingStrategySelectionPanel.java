package org.atdl4j.ui.swing.app.impl;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.impl.AbstractStrategySelectionPanel;


/**
 * Represents the Swing-specific available strategy choices GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, October 5, 2010
 */
public class SwingStrategySelectionPanel 
	extends AbstractStrategySelectionPanel
{
	private final Logger logger = Logger.getLogger(SwingStrategySelectionPanel.class);
	
	private Container dropdownContainer;
	private JComboBox strategiesDropDown;
	public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildStrategySelectionPanel( (Container) parentOrShell, atdl4jOptions );
	}
	
	public Container buildStrategySelectionPanel(Container aParentContainer, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		
		// Strategy selector dropdown
		dropdownContainer = new Container();
		BorderLayout dropdownLayout = new BorderLayout();
		dropdownContainer.setLayout( dropdownLayout );
		
		aParentContainer.add( dropdownContainer );
		
		// label
		JLabel strategiesDropDownLabel = new JLabel("Strategy");
		dropdownLayout.addLayoutComponent( strategiesDropDownLabel, BorderLayout.WEST );
		// dropDownList
		strategiesDropDown = new JComboBox();
		strategiesDropDown.setEditable( false );
// 4/17/2010 Scott Atwell avoid taking the full width of screen for relatively short strategy names		strategiesDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		dropdownLayout.addLayoutComponent( strategiesDropDown, BorderLayout.EAST );

		// -- Increase font size for Drop Down --
		Font tempNewFont = strategiesDropDown.getFont().deriveFont( Font.BOLD, (strategiesDropDown.getFont().getSize() + 3) );
		strategiesDropDown.setFont(tempNewFont); 
		 
//TODO ???? SWT only		// Since you created the font, you must dispose it
/**		
		strategiesDropDown.addDisposeListener(new DisposeListener()
		{
		    public void widgetDisposed(DisposeEvent e) 
		    { 
		        newFont.dispose(); 
		    } 
		}); 
**/
		
// TODO wish to avoid issue with changing the font causes the initial combo box display to be very narrow 
	
//		if ( ( atdl4jOptions != null ) && ( atdl4jOptions.getStrategyDropDownItemDepth() != null ) )
		if ( Atdl4jConfig.getConfig().getStrategyDropDownItemDepth() != null )
		{
			strategiesDropDown.setMaximumRowCount( Atdl4jConfig.getConfig().getStrategyDropDownItemDepth().intValue() );
		}
		// tooltip
		strategiesDropDown.setToolTipText("Select a Strategy");
		// action listener
		strategiesDropDown.addItemListener( new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent aE)
			{
				int index = strategiesDropDown.getSelectedIndex();
				logger.debug( "strategiesDropDown.widgetSelected.  strategiesDropDown.getSelectionIndex(): " + index );					
									selectDropDownStrategy( index );
			}
		} );
	
		return dropdownContainer;
	}

	
	public void loadStrategyList( List<StrategyT> aStrategyList )
	{
		// remove all dropdown items
		strategiesDropDown.removeAll();
/*** 4/16/2010 Scott Atwell
		List<String> tempStrategyUiRepOrNameList = getStrategyUiRepOrNameList( aStrategyList );
		
		if ( tempStrategyUiRepOrNameList == null )
		{
			return;
		}
		
		for (String tempStrategy : tempStrategyUiRepOrNameList) 
		{
			// create dropdown item for strategy
			strategiesDropDown.add( tempStrategy );
		}
***/
		setStrategiesList( aStrategyList );

		if ( getStrategiesList() == null )
		{
			return;
		}

		for (StrategyT tempStrategy : getStrategiesList()) 
		{
			logger.debug( "loadStrategyList() [" + strategiesDropDown.getItemCount() + "] strategiesDropDown.add: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
			
			// create dropdown item for strategy
			strategiesDropDown.addItem( Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
		}
		
//TODO deprecated for Swing		dropdownContainer.layout(); 
//		if (strategiesDropDown.getItemCount() > 0)
//		{
// 4/16/2010 Scott Atwell - Container panel caller does this			strategiesDropDown.select( 0 );
//		}
	}


	public void selectDropDownStrategy(int index) 
	{
		logger.debug( "selectDropDownStrategy() index: " + index );
		
		if ( getStrategiesList().size() != strategiesDropDown.getItemCount() )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: getStrategiesList().size(): " + getStrategiesList().size() + " does NOT MATCH strategiesDropDown.getItemCount(): " + strategiesDropDown.getItemCount() );
		}
		
		strategiesDropDown.setSelectedIndex( index );
		
		StrategyT tempStrategy = getStrategiesList().get( index );
		
		if ( ! strategiesDropDown.getItemAt( index ).equals( Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) ) )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: strategiesDropDown.getItem(" + index + "): " + strategiesDropDown.getItemAt( index ) + " DID NOT MATCH tempStrategy: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
		}
		
// 9/27/2010 Scott Atwell moved to AbstractAtdl4jContainerPanel.strategySelected()		getAtdl4jOptions().setSelectedStrategy( tempStrategy );
		
// 4/16/2010 Scott Atwell		fireStrategySelectedEvent( tempStrategy, index );
		fireStrategySelectedEvent( tempStrategy );
	}

	
// 4/16/2010 Scott Atwell	public void selectDropDownStrategy(String strategyName) 
	public void selectDropDownStrategyByStrategyName(String aStrategyName) 
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

// 4/16/2010 Scott Atwell added	
	public void selectDropDownStrategyByStrategyWireValue( String aStrategyWireValue ) 
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
	
	public void selectFirstDropDownStrategy()
	{
		if ( ( strategiesDropDown != null ) && 
			  ( strategiesDropDown.getItemCount() > 0 ) )
		{
			strategiesDropDown.setSelectedItem( null );
			logger.debug( "selectFirstDropDownStrategy() invoking selectDropDownStrategy( 0 )" );
			selectDropDownStrategy( 0 );
		}
	}
}
