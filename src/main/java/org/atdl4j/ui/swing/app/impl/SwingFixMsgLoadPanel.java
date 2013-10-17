package org.atdl4j.ui.swing.app.impl;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractFixMsgLoadPanel;


/**
 * Represents the Swing-specific tester's "Load Message" button and text field GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, October 5, 2010
 */
public class SwingFixMsgLoadPanel extends AbstractFixMsgLoadPanel
{
	Container parentContainer;
// JPanel as we need TitledBorder	private Container container;
	
	JTextField fixMsgText;
	JButton loadFixMsgButton;
	
	public Object buildFixMsgLoadPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildFixMsgLoadPanel( (JPanel) parentOrShell, atdl4jOptions );
	}
	
	public JPanel buildFixMsgLoadPanel(Container aParentContainer, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		setParentContainer( aParentContainer );
		
		JPanel tempContainer = new JPanel(new BorderLayout());
		tempContainer.setBorder( new TitledBorder( "Pre-populate with FIX Message Fragment (tag=value syntax)" ) );
		
		loadFixMsgButton = new JButton("Load Message");
		tempContainer.add( loadFixMsgButton, BorderLayout.WEST );
		loadFixMsgButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent aE)
			{
			    loadFixMessage();
			}
		} );

		fixMsgText = new JTextField(10);
		tempContainer.add( fixMsgText, BorderLayout.CENTER );
		// -- Handle Enter key within Text field --
		fixMsgText.addKeyListener( new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent aE) {
				if ( aE.getKeyCode() == KeyEvent.VK_ENTER ){
				    loadFixMessage();
				}
			}
		});
		
		return tempContainer;
	}
	
	private void loadFixMessage()
	{
		fireFixMsgLoadSelectedEvent( fixMsgText.getText() );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixMsgLoadPanel#selectFilename(java.lang.String)
	 */
	@Override
	public void setFixMsg(String aFixMsg)
	{
		if ( fixMsgText != null )
		{
			fixMsgText.setText( aFixMsg );
			fireFixMsgLoadSelectedEvent( aFixMsg );
		}
	}

	/**
	 * @return the parentContainer
	 */
	private Container getParentContainer()
	{
		return this.parentContainer;
	}

	/**
	 * @param aParentContainer the parentContainer to set
	 */
	private void setParentContainer(Container aParentContainer)
	{
		this.parentContainer = aParentContainer;
	}

}
