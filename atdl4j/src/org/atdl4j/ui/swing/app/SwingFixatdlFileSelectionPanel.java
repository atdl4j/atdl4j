package org.atdl4j.ui.swing.app;


import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
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
public class SwingFixatdlFileSelectionPanel 
	extends AbstractFixatdlFileSelectionPanel
{
	private final Logger logger = Logger.getLogger(SwingFixatdlFileSelectionPanel.class);
	Composite parentComposite;
	Composite composite;
	
	Text filepathText;
	Button browseButton;
	
	public Object buildFixatdlFileSelectionPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig)
	{
		return buildFixatdlFileSelectionPanel( (Composite) parentOrShell, atdl4jConfig );
	}
	
	public Composite buildFixatdlFileSelectionPanel(Composite aParentComposite, Atdl4jConfig atdl4jConfig)
	{
		setAtdl4jConfig( atdl4jConfig );
		setParentComposite( aParentComposite );
		
		// -- SWTVisibleGroup avoids consuming vertical space when hidden via setVisible(false) --
		composite = new SwingVisibleGroup(aParentComposite, SWT.NONE);
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
