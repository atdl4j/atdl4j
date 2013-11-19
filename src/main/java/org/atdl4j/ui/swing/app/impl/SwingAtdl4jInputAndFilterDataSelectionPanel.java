package org.atdl4j.ui.swing.app.impl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.impl.AbstractAtdl4jInputAndFilterDataSelectionPanel;

/**
 * Represents the Swing-specific GUI component used to invoke Atdl4jInputAndFilterData pop-up.
 * 
 */
public class SwingAtdl4jInputAndFilterDataSelectionPanel
		extends AbstractAtdl4jInputAndFilterDataSelectionPanel
{
	private JPanel parentPanel;
	private JButton atdl4jInputAndFilterDataPanelButton;
	private JDialog atdl4jInputAndFilterDataPanelDialog;
	
	private JCheckBox debugModeButton;
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataSelectionPanel#buildAtdl4jInputAndFilterDataSelectionPanel(java.lang.Object, org.atdl4j.config.Atdl4jOptions, org.atdl4j.ui.app.Atdl4jUserMessageHandler)
	 */
	@Override
	public Object buildAtdl4jInputAndFilterDataSelectionPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions,
			Atdl4jUserMessageHandler aAtdl4jUserMessageHandler) 
	{
		return buildAtdl4jInputAndFilterDataSelectionPanel( (JPanel) aParentOrShell, aAtdl4jOptions, aAtdl4jUserMessageHandler);
	}
	
	public JPanel buildAtdl4jInputAndFilterDataSelectionPanel(JPanel aParentOrShell, Atdl4jOptions aAtdl4jOptions,
			Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
	{
		parentPanel = (JPanel) aParentOrShell;
		
		// -- Delegate back to AbstractAtdl4jInputAndFilterDataSelectionPanel --
		init (aParentOrShell, aAtdl4jOptions, aAtdl4jUserMessageHandler);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		atdl4jInputAndFilterDataPanelButton = new JButton("Input And Filter Data");
		atdl4jInputAndFilterDataPanelButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent aE)
			{
			    buttonInputAndFilterDataSelected();
			}
		} );
		
		panel.add(atdl4jInputAndFilterDataPanelButton, BorderLayout.CENTER);
		
		return panel;
	}
	
	private void closeAtdl4jInputAndFilterDataPanelDialog()
	{
		if (atdl4jInputAndFilterDataPanelDialog != null)
		{
			atdl4jInputAndFilterDataPanelDialog.setVisible(false);
		}
	}
	
	private void buttonInputAndFilterDataSelected()
	{
		if (atdl4jInputAndFilterDataPanelDialog == null)
		{
			atdl4jInputAndFilterDataPanelDialog = createAtdl4jInputAndFilterDataPanelDialog();
		}
		
		getAtdl4jInputAndFilterDataPanel().loadScreenWithAtdl4jOptions();
		getDebugModeButton().setSelected( Atdl4jConfig.getConfig().isDebugLoggingLevel() );
		
		atdl4jInputAndFilterDataPanelDialog.setVisible(true);
	}
	
	private void buttonOkSelected()
	{
		// -- Atdl4jInputAndFilterDataPanel.extractAtdl4jOptionsFromScreen() populates/overlays data members within our Atdl4jOptions -- 
		if ( ! getAtdl4jInputAndFilterDataPanel().extractAtdl4jOptionsFromScreen() )
		{
			getAtdl4jUserMessageHandler().displayMessage( "Error", "Error extracting Atdl4jOptions extracted from screen" );
			return;
		}
		fireInputAndFilterDataSpecifiedEvent( getAtdl4jOptions().getInputAndFilterData() );
		closeAtdl4jInputAndFilterDataPanelDialog();
	}
	
	private void buttonCancelSelected()
	{
		closeAtdl4jInputAndFilterDataPanelDialog();
	}
	
	private JDialog createAtdl4jInputAndFilterDataPanelDialog()
	{
		JDialog tempDialog = new JDialog();
		tempDialog.setTitle( "atdl4j Input and Filter Data / Configuration Settings" );
		tempDialog.setModal(true);
		
		getAtdl4jInputAndFilterDataPanel().buildAtdl4jInputAndFilterDataPanel(tempDialog, getAtdl4jOptions());
		
		JPanel footerPanel = new JPanel();
		tempDialog.getContentPane().add(footerPanel, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				buttonOkSelected();
			}
		} );
		footerPanel.add(okButton);
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				buttonCancelSelected();
			}
		} );
		footerPanel.add(cancelButton);
		
		setDebugModeButton(new JCheckBox("Debug Mode"));
		getDebugModeButton().addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Atdl4jConfig.getConfig().setDebugLoggingLevel( getDebugModeButton().isSelected() );
			}
		} );
		footerPanel.add(getDebugModeButton());
		
		tempDialog.pack();
		return tempDialog;
	}

	public JCheckBox getDebugModeButton()
	{
		return debugModeButton;
	}

	public void setDebugModeButton(JCheckBox debugModeButton)
	{
		this.debugModeButton = debugModeButton;
	}
}
