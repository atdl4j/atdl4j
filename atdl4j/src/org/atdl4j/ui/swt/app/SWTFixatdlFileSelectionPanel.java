package org.atdl4j.ui.swt.app;


import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.AbstractFixatdlFileSelectionPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * Represents the FIXatdl file selection SWT-specific GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SWTFixatdlFileSelectionPanel 
	extends AbstractFixatdlFileSelectionPanel
{
	private final Logger logger = Logger.getLogger(SWTFixatdlFileSelectionPanel.class);
	Composite parentComposite;
	Composite composite;
	
	Text filepathText;
	Button browseButton;
	
	public Object buildFixatdlFileSelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildFixatdlFileSelectionPanel( (Composite) parentOrShell, atdl4jOptions );
	}
	
	public Composite buildFixatdlFileSelectionPanel(Composite aParentComposite, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		setParentComposite( aParentComposite );
		
		// -- SWTVisibleGroup avoids consuming vertical space when hidden via setVisible(false) --
		composite = new SWTVisibleGroup(aParentComposite, SWT.NONE);
		((Group) composite).setText("Load FIXatdl XML File");
		
		GridLayout tempLayout = new GridLayout(3, false);
		composite.setLayout(tempLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		filepathText = new Text(composite, SWT.BORDER);

		// -- Handle Enter key within Text field --
		filepathText.addListener( SWT.DefaultSelection, new Listener()
		{
			public void handleEvent(Event e) 
			{
				fireFixatdlFileSelectedEvent( filepathText.getText() );
			}
		});
		
		
		GridData filepathTextData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		filepathTextData.horizontalSpan = 2;
		filepathText.setLayoutData(filepathTextData);
		browseButton = new Button(composite, SWT.NONE);
		browseButton.setText("...");
		browseButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setFilterExtensions( new String[] { "*.xml", "*.*" } );
				dialog.setFilterNames( new String[] { "XML Files (*.xml)", "All Files (*.*)" } );
				String filepath = dialog.open();
				if (filepath != null) 
				{
					filepathText.setText(filepath);
					fireFixatdlFileSelectedEvent( filepath );
				}
			}
		});

	
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
	private Shell getShell()
	{
		if ( getParentComposite() != null )
		{
			return getParentComposite().getShell();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return the parentComposite
	 */
	private Composite getParentComposite()
	{
		return this.parentComposite;
	}

	/**
	 * @param aParentComposite the parentComposite to set
	 */
	private void setParentComposite(Composite aParentComposite)
	{
		this.parentComposite = aParentComposite;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanel#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aVisible)
	{
		if ( ( composite != null ) && ( ! composite.isDisposed() ) )
		{
			composite.setVisible( aVisible );
		}
	}	
}
