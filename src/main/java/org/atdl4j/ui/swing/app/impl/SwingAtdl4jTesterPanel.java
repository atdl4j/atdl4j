package org.atdl4j.ui.swing.app.impl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.app.impl.AbstractAtdl4jTesterPanel;

/**
 * Represents the Swing-specific "TesterApp" core GUI component (without a main() line).
 * Contains a Atdl4jCompositePanel.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 */
public class SwingAtdl4jTesterPanel
		extends AbstractAtdl4jTesterPanel
{
	public final Logger logger = Logger.getLogger(SwingAtdl4jTesterPanel.class);
	private JFrame parentComposite;
	
	private JPanel inputAndFilterDataAndLoadMessageComposite;
	private JPanel validateOutputSection;
	
	private JCheckBoxMenuItem showFileSelectionMenuItem;
	private JCheckBoxMenuItem showValidateOutputMenuItem;
	
	private JPanel okCancelButtonSection;
	private JTextField outputFixMessageText;

	
	public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		return buildAtdl4jTesterPanel( (JFrame) aParentOrShell, aAtdl4jOptions );
	}
	
	public JFrame buildAtdl4jTesterPanel(JFrame frame, Atdl4jOptions aAtdl4jOptions)
	{
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		frame.getContentPane().add(mainPanel);

		parentComposite = frame;

		// -- Delegate back to AbstractAtdl4jTesterPanel -- 
		init( parentComposite, aAtdl4jOptions );
		inputAndFilterDataAndLoadMessageComposite = new JPanel(new BorderLayout());
		inputAndFilterDataAndLoadMessageComposite.setBorder(BorderFactory.createTitledBorder("Testing Input"));
		// -- Build the Swing.JPanel from Atdl4jInputAndFilterDataSelectionPanel ("Input Data/Filter Criteria" button) --
		JPanel inpPanel = (JPanel)getAtdl4jInputAndFilterDataSelectionPanel().buildAtdl4jInputAndFilterDataSelectionPanel( inputAndFilterDataAndLoadMessageComposite, getAtdl4jOptions(), getAtdl4jCompositePanel().getAtdl4jUserMessageHandler() );
		if (inpPanel != null) {
			inputAndFilterDataAndLoadMessageComposite.add(inpPanel, BorderLayout.WEST);
		}
		// -- Build the Swing.JPanel from FixMsgLoadPanel ("Load FIX Message" button) --
		JPanel loadPanel = (JPanel)getFixMsgLoadPanel().buildFixMsgLoadPanel( inputAndFilterDataAndLoadMessageComposite, getAtdl4jOptions() );
		inputAndFilterDataAndLoadMessageComposite.add(loadPanel, BorderLayout.CENTER);		
		parentComposite.add(inputAndFilterDataAndLoadMessageComposite, BorderLayout.NORTH);
		
		JPanel internalPanel = new JPanel(new BorderLayout());
		
		// -- Build the Swing.JPanel from FixatdlFileSelectionPanel (filename / file dialog) --
		JPanel loadFilePanel = (JPanel)getFixatdlFileSelectionPanel().buildFixatdlFileSelectionPanel( parentComposite, getAtdl4jOptions() );
		internalPanel.add(loadFilePanel, BorderLayout.NORTH);		

		// -- Build the Swing.JPanel from Atdl4jCompositePanel --
		JPanel strategyParamsPanel = (JPanel)getAtdl4jCompositePanel().buildAtdl4jCompositePanel( parentComposite, aAtdl4jOptions );
		internalPanel.add(strategyParamsPanel, BorderLayout.CENTER);	
		
		// -- Build the Swing.JPanel containing "Validate Output" button and outputFixMessageText --
		JPanel validatePanel = createValidateOutputSection();
		internalPanel.add(validatePanel, BorderLayout.SOUTH);	

		parentComposite.add(internalPanel, BorderLayout.CENTER);
		
		// -- Build the Swing.JPanel containing "OK" and "Cancel" buttons --
		JPanel buttonsPanel = createOkCancelButtonSection();
		parentComposite.add(buttonsPanel, BorderLayout.SOUTH);

		// -- Build the Swing JMenuItems --
		
		createPopupMenuForPanel(internalPanel);
		
		return parentComposite;
	}
	
	public void closePanel()
	{
		if ( parentComposite != null )
		{
			parentComposite.dispose();
		}
	}
	
	public void setVisibleFileSelectionSection( boolean aVisible )
	{
		 if ( getFixatdlFileSelectionPanel() != null ) 
		 {
			 getFixatdlFileSelectionPanel().setVisible( aVisible );

			 if ( showFileSelectionMenuItem != null )
			 {
				 showFileSelectionMenuItem.setSelected( aVisible );
			 }
		 }
	}
	
	protected void createPopupMenuForPanel(JPanel _panel)
	{
		final JPopupMenu menu = new JPopupMenu();
		
		// -- "Show File Selection" --
		setVisibleFileSelectionSection( Atdl4jConfig.getConfig().isShowFileSelectionSection() );
		showFileSelectionMenuItem = new JCheckBoxMenuItem("Show File Selection" );
		showFileSelectionMenuItem.setSelected( Atdl4jConfig.getConfig().isShowFileSelectionSection() );
		showFileSelectionMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisibleFileSelectionSection( showFileSelectionMenuItem.isSelected() );
			}
		});
		
		
		// -- "Show Validate Output" --
		setVisibleValidateOutputSection( Atdl4jConfig.getConfig().isShowValidateOutputSection() );
		showValidateOutputMenuItem = new JCheckBoxMenuItem("Show Validate Output" );
		showValidateOutputMenuItem.setSelected( Atdl4jConfig.getConfig().isShowValidateOutputSection() );
		showValidateOutputMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisibleValidateOutputSection( showValidateOutputMenuItem.isSelected() );
			}
		});
		
		menu.add(showFileSelectionMenuItem);
		menu.add(showValidateOutputMenuItem);
		
		
		_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		
	}
	
	protected JPanel createValidateOutputSection()
	{
		validateOutputSection = new JPanel(new BorderLayout());
		validateOutputSection.setBorder(BorderFactory.createTitledBorder("Validation"));
		// validate button
		JButton validateButton = new JButton("Validate Output");
		validateButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
                		try {
                		    validateButtonSelected();
                		} catch (ValidationException ex) {
                		    logger.info("Validation Exception:", ex);
               			    getAtdl4jUserMessageHandler().displayException(
                				"Validation Exception", "", ex);
                		}
			}
		});
		validateOutputSection.add(validateButton, BorderLayout.WEST);
		
		outputFixMessageText = new JTextField(20);
		validateOutputSection.add(outputFixMessageText, BorderLayout.CENTER);
		return validateOutputSection;
	}
	
	public void setVisibleValidateOutputSection( boolean aVisible )
	{
		 if ( validateOutputSection != null )
		 {
			 validateOutputSection.setVisible( aVisible );
			 
			 if ( showValidateOutputMenuItem != null )
			 {
				 showValidateOutputMenuItem.setSelected( aVisible );
			 }
		 }
	}
	
	public void setVisibleTestingInputSection( boolean aVisible )
	{
		/*
		 if ( ( inputAndFilterDataAndLoadMessageComposite != null ) && ( ! inputAndFilterDataAndLoadMessageComposite.isDisposed() ) )
		 {
			 inputAndFilterDataAndLoadMessageComposite.setVisible( aVisible );
			 if ( inputAndFilterDataAndLoadMessageComposite.getLayoutData() instanceof GridData )
			 {
				((GridData) inputAndFilterDataAndLoadMessageComposite.getLayoutData()).exclude = !aVisible;
			 }

			 packLayout();
		 }
		 */
	}

	protected JPanel createOkCancelButtonSection()
	{
		okCancelButtonSection = new JPanel();

		// OK button
		JButton okButton = new JButton("OK");
		okButton.setToolTipText( "Validate and accept the specified strategy and parameters" );
		okButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				okButtonSelected();				
			}
		});
		
		// Cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText( "Cancel ignoring any specified changes" );
		cancelButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButtonSelected();				
			}
		});
		
		okCancelButtonSection.add(okButton);
		okCancelButtonSection.add(cancelButton);
		
		setVisibleOkCancelButtonSection( Atdl4jConfig.getConfig().isShowTesterPanelOkCancelButtonSection() );
		
		return okCancelButtonSection;
	}
	
	public void setVisibleOkCancelButtonSection( boolean aVisible )
	{
		 if ( okCancelButtonSection != null ) 
		 {
			 okCancelButtonSection.setVisible( aVisible );
		 }
	}

	protected void packLayout()
	{
		getShell().pack();
	}
	
	
	/**
	 * Returns getParentComposite().getShell().
	 * @return the shell
	 */
	private JFrame getShell()
	{
		if ( getParentComposite() != null )
		{
			return (JFrame)getParentComposite().getParent();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return the parentComposite
	 */
	private JFrame getParentComposite()
	{
		return this.parentComposite;
	}

	protected void setValidateOutputText(String aText)
	{
		if ( ( Atdl4jConfig.getConfig().isShowValidateOutputSection() ) )
		{
			if ( aText != null )
			{
				outputFixMessageText.setText( aText.replace( '\n', ' ' ) );
			}
			else
			{
				outputFixMessageText.setText( "" );
			}
		}
		else
		{
			outputFixMessageText.setText( aText.replace( '\n', ' ' ) );
		}	
	}
	
}
