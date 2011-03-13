package org.atdl4j.ui.swing.app.impl;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractFixatdlFileSelectionPanel;


/**
 * Represents the FIXatdl file selection Swing-specific GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SwingFixatdlFileSelectionPanel 
	extends AbstractFixatdlFileSelectionPanel
{
	private final Logger logger = Logger.getLogger(SwingFixatdlFileSelectionPanel.class);
	JFrame parentComposite;
	JPanel composite;
	
	JTextField filepathText;
	JButton browseButton;
	
	public Object buildFixatdlFileSelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildFixatdlFileSelectionPanel( (JFrame) parentOrShell, atdl4jOptions );
	}
	
	public JPanel buildFixatdlFileSelectionPanel(JFrame aParentComposite, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		setParentComposite( aParentComposite );
		
		composite = new JPanel(new BorderLayout());
		composite.setBorder(BorderFactory.createTitledBorder("Load FIXatdl XML File"));
		
		filepathText = new JTextField(20);

		// -- Handle Enter key within Text field --
		filepathText.addKeyListener(new KeyAdapter (){
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!"".equals(filepathText.getText().trim())) {
						fireFixatdlFileSelectedEvent( filepathText.getText() );
					}
				}
			}
		});
		
		browseButton = new JButton("...");
		browseButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "*.xml";
					}
			    public String getExtension(File f) {
		        String ext = null;
		        String s = f.getName();
		        int i = s.lastIndexOf('.');

		        if (i > 0 &&  i < s.length() - 1) {
		            ext = s.substring(i+1).toLowerCase();
		        }
		        return ext;
			    }
					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						String extension = getExtension(f);
						if (extension != null) {
							if (extension.equals("xml")) {
							        return true;
							} 
						}
						return false;
					}
				});
				
				int returnVal = fc.showOpenDialog(parentComposite);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File f = fc.getSelectedFile();
					filepathText.setText(f.getPath());
					fireFixatdlFileSelectedEvent( f.getPath() );
				}
			}
		});

		composite.add(filepathText, BorderLayout.CENTER);
		composite.add(browseButton, BorderLayout.EAST);
	
		return composite;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanel#selectFilename(java.lang.String)
	 */
	@Override
	public void selectFilename(String aFilename)
	{
		if ( filepathText != null )
		{
			filepathText.setText( aFilename );
			fireFixatdlFileSelectedEvent( aFilename );
		}
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

	/**
	 * @param aParentComposite the parentComposite to set
	 */
	private void setParentComposite(JFrame aParentComposite)
	{
		this.parentComposite = aParentComposite;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanel#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aVisible)
	{
		if (composite != null)
		{
			composite.setVisible( aVisible );
		}
	}	
}
