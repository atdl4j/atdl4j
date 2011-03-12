package org.atdl4j.ui.swing.app.impl;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
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
	private JFrame parentComposite;
	private JPanel strPanel;
	private JPanel strategySelectionPanel;
	
	public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		return buildAtdl4jCompositePanel( (JFrame) aParentOrShell, aAtdl4jOptions );
	}
	
	public JPanel buildAtdl4jCompositePanel(JFrame aParentComposite, Atdl4jOptions aAtdl4jOptions)
	{
		parentComposite =  aParentComposite;

		JPanel panel = new JPanel(new BorderLayout());
		
		// -- Delegate back to AbstractAtdl4jCompositePanel -- 
		init( aParentComposite, aAtdl4jOptions );

		strategySelectionPanel = new JPanel();
		strategySelectionPanel.add((JPanel)getStrategySelectionPanel().buildStrategySelectionPanel( getParentOrShell(), getAtdl4jOptions()));
		panel.add(strategySelectionPanel, BorderLayout.NORTH);
		
		JPanel descrPanel = (JPanel)getStrategyDescriptionPanel().buildStrategyDescriptionPanel( getParentOrShell(), getAtdl4jOptions() );
		panel.add(descrPanel, BorderLayout.SOUTH);
		descrPanel.setVisible( false );  // hide until there is data to populate it with
		
		strPanel = (JPanel)getStrategiesUI().buildStrategiesPanel( getParentOrShell(), getAtdl4jOptions(), getAtdl4jUserMessageHandler() );
		panel.add(strPanel, BorderLayout.CENTER);
		
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
}
