package org.atdl4j.ui.swing.app.impl;


import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractStrategyDescriptionPanel;


/**
 * Represents the Swing-specific Strategy Description GUI component.
 * 
 * @author Scott Atwell
 * @version October 5, 2010
 */
public class SwingStrategyDescriptionPanel 
	extends AbstractStrategyDescriptionPanel
{
	private final Logger logger = Logger.getLogger(SwingStrategyDescriptionPanel.class);
	
// JPanel as we need TitledBorder	private Container container;
	private JPanel container;
	private JTextArea strategyDescription;

//	private int DEFAULT_STRATEGY_DESCRIPTION_HEIGHT_HINT = 35;
	private int DEFAULT_STRATEGY_DESCRIPTION_ROWS = 2;

	public Object buildStrategyDescriptionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildStrategyDescriptionPanel( (Container) parentOrShell, atdl4jOptions );
	}
	
	public Container buildStrategyDescriptionPanel(Container aParentContainer, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
	
		JPanel tempContainer = new JPanel();
		tempContainer.setBorder( new TitledBorder( "Strategy Description" ) );
		BorderLayout tempLayout = new BorderLayout();
		tempContainer.setLayout( tempLayout );
		
		aParentContainer.add( tempContainer );
		
  		strategyDescription = new JTextArea( DEFAULT_STRATEGY_DESCRIPTION_ROWS, 1);
  		strategyDescription.setLineWrap( true );
  		strategyDescription.setWrapStyleWord( true );
  		strategyDescription.setEditable( false );
  		
//TODO  	   strategyDescription.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
//TODO  		strategyDescription.setForeground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		
		JScrollPane tempScrollPane = new JScrollPane( strategyDescription );
		tempLayout.addLayoutComponent( tempScrollPane, BorderLayout.CENTER );
	
		return container;
	}


	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.AbstractStrategyDescriptionPanel#setStrategyDescriptionText(java.lang.String)
	 */
	protected void setStrategyDescriptionText(String aText)
	{
		if ( strategyDescription != null )
		{
			strategyDescription.setText( aText );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategyDescriptionPanel#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aVisible)
	{
		if ( container != null )
		{
			container.setVisible( aVisible );
		}
	}
}
