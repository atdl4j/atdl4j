package org.atdl4j.ui.swing.app.impl;


import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	
	private JComboBox strategiesDropDown;
	public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildStrategySelectionPanel( (JFrame) parentOrShell, atdl4jOptions );
	}
	
	public JPanel buildStrategySelectionPanel(JFrame aParentContainer, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		
		JPanel panel = new JPanel(new BorderLayout());
		// label
		JLabel strategiesDropDownLabel = new JLabel("Strategy");
		panel.add( strategiesDropDownLabel, BorderLayout.WEST );

		// dropDownList
		strategiesDropDown = new JComboBox();
		strategiesDropDown.setEditable( false );
		
		panel.add(strategiesDropDown, BorderLayout.CENTER);

		if ( Atdl4jConfig.getConfig().getStrategyDropDownItemDepth() != null )
		{
			strategiesDropDown.setMaximumRowCount( Atdl4jConfig.getConfig().getStrategyDropDownItemDepth().intValue() );
		}
		// tooltip
		strategiesDropDown.setToolTipText("Select a Strategy");
		// action listener
		strategiesDropDown.addItemListener( new ItemListener()	{

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int index = strategiesDropDown.getSelectedIndex();
					logger.debug( "strategiesDropDown.widgetSelected.  strategiesDropDown.getSelectionIndex(): " + index );
					selectDropDownStrategy( index );
				}
			}
		} );
	
		return panel;
	}


	public void loadStrategyList( List<StrategyT> aStrategyList )
	{
		strategiesDropDown.removeAllItems();

		setStrategiesList( aStrategyList );

		if ( getStrategiesList() == null )
		{
			return;
		}

		for (StrategyT tempStrategy : getStrategiesList()) 
		{
			logger.debug( "loadStrategyList() [" + strategiesDropDown.getItemCount() + "] strategiesDropDown.add: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
			strategiesDropDown.addItem( Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
		}
	}


	public void selectDropDownStrategy(int index)
	{
		logger.debug( "selectDropDownStrategy() index: " + index );
		
		if ( getStrategiesList().size() != strategiesDropDown.getItemCount() )
		{
			return;
		}
		
		strategiesDropDown.setSelectedIndex( index );
		
		StrategyT tempStrategy = getStrategiesList().get( index );
		
		if ( ! strategiesDropDown.getItemAt( index ).equals( Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) ) )
		{
			throw new IllegalStateException( "UNEXPECTED ERROR: strategiesDropDown.getItem(" + index + "): " + strategiesDropDown.getItemAt( index ) + " DID NOT MATCH tempStrategy: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategy ) );
		}
		
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
