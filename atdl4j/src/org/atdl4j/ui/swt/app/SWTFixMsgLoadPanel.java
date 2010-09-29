package org.atdl4j.ui.swt.app;


import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.AbstractFixMsgLoadPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * Represents the SWT-specific tester's "Load Message" button and text field GUI component. 
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SWTFixMsgLoadPanel 
	extends AbstractFixMsgLoadPanel
{
	private final Logger logger = Logger.getLogger(SWTFixMsgLoadPanel.class);
	Composite parentComposite;
	Composite composite;
	
	Text fixMsgText;
	Button loadFixMsgButton;
	
	public Object buildFixMsgLoadPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions)
	{
		return buildFixMsgLoadPanel( (Composite) parentOrShell, atdl4jOptions );
	}
	
	public Composite buildFixMsgLoadPanel(Composite aParentComposite, Atdl4jOptions atdl4jOptions)
	{
		setAtdl4jOptions( atdl4jOptions );
		setParentComposite( aParentComposite );
		
		composite = new Group(aParentComposite, SWT.NONE);
		((Group) composite).setText("Pre-populate with FIX Message Fragment (tag=value syntax)");
		
		loadFixMsgButton = new Button(composite, SWT.NONE);
		loadFixMsgButton.setText("Load Message");
		loadFixMsgButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e)
			{
				fireFixMsgLoadSelectedEvent( fixMsgText.getText() );
			}
		});


		GridLayout tempLayout = new GridLayout(3, false);
		composite.setLayout(tempLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		fixMsgText = new Text(composite, SWT.BORDER);
		GridData fixMsgTextData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		fixMsgTextData.horizontalSpan = 2;
		fixMsgText.setLayoutData(fixMsgTextData);
		

		// -- Handle Enter key within Text field --
		fixMsgText.addListener( SWT.DefaultSelection, new Listener()
		{
			public void handleEvent(Event e) 
			{
				fireFixMsgLoadSelectedEvent( fixMsgText.getText() );
			}
		});
		
		
	
		return composite;
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

}
