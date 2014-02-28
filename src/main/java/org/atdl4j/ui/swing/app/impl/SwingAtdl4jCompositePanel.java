package org.atdl4j.ui.swing.app.impl;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.impl.AbstractAtdl4jCompositePanel;

/**
 * Represents the Swing-specific strategy selection and display GUI component.
 * 
 * Creation date: (Feb 28, 2010 6:26:02 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SwingAtdl4jCompositePanel
		extends AbstractAtdl4jCompositePanel
{
	public final Logger logger = Logger.getLogger(SwingAtdl4jCompositePanel.class);
	private Window parentComposite;
	private JPanel strPanel;
	private JPanel strategySelectionPanel;
	private JPanel panel;
	
	public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		return buildAtdl4jCompositePanel( (Window) aParentOrShell, aAtdl4jOptions );
	}
	
	public JPanel buildAtdl4jCompositePanel(Window aParentComposite, Atdl4jOptions aAtdl4jOptions)
	{
	  parentComposite =  aParentComposite;

      panel = new JPanel(new GridBagLayout());
      
      GridBagConstraints gbc = new GridBagConstraints();
      
      // -- Delegate back to AbstractAtdl4jCompositePanel -- 
      init( aParentComposite, aAtdl4jOptions );

      strategySelectionPanel = new JPanel();
      strategySelectionPanel.add((JPanel)getStrategySelectionPanel().buildStrategySelectionPanel( getParentOrShell(), getAtdl4jOptions()));
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      panel.add(strategySelectionPanel, gbc);
      
      
      gbc.weightx = 1;
      gbc.gridy = 1;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.BOTH;
      strPanel = (JPanel)getStrategiesUI().buildStrategiesPanel( getParentOrShell(), getAtdl4jOptions(), getAtdl4jUserMessageHandler() );
      panel.add(strPanel, gbc);
      
      
      
      JPanel descrPanel = (JPanel)getStrategyDescriptionPanel().buildStrategyDescriptionPanel( getParentOrShell(), getAtdl4jOptions() );
      gbc.weightx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      descrPanel.setPreferredSize(new Dimension((int) strPanel.getPreferredSize().getWidth(), 120)); 
      panel.add(descrPanel, gbc);
      descrPanel.setVisible( false );  // hide until there is data to populate it with
      
      return panel;
	}
	
	public void setVisibleStrategySectionPanel( boolean aVisible )
	{
		 if (  strategySelectionPanel != null  )
		 {
			 strategySelectionPanel.setVisible( aVisible );
		}
	}

	protected void packLayout()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (strPanel != null) {
					strPanel.revalidate();
					strPanel.repaint();
				}
				if (parentComposite != null) {
					parentComposite.pack();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.impl.AbstractAtdl4jCompositePanel#setVisibleOkCancelButtonSection(boolean)
	 */
	@Override
	public void setVisibleOkCancelButtonSection(boolean aVisible) {
		
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategySelectionPanelListener#strategySelected(org.atdl4j.fixatdl.core.StrategyT, int)
	 */
	@Override
	public void strategySelected(StrategyT aStrategy)
	{
		setSelectedStrategy( aStrategy );
		setSelectedStrategyValidated( false );
		getStrategyDescriptionPanel().loadStrategyDescriptionVisible( aStrategy );
		getStrategiesUI().adjustLayoutForSelectedStrategy( aStrategy );
		getStrategyDescriptionPanel().loadStrategyDescriptionText( aStrategy );
		// -- Notify StrategyEventListener (eg Atdl4jTesterPanel), aSelectedViaLoadFixMsg=false --
		fireStrategyEventListenerStrategySelected( aStrategy, false );
		packLayout();
	}
}
