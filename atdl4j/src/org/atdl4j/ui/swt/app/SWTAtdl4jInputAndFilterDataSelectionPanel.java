package org.atdl4j.ui.swt.app;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.app.AbstractAtdl4jInputAndFilterDataSelectionPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * Represents the SWT-specific GUI component used to invoke Atdl4jInputAndFilterData pop-up.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SWTAtdl4jInputAndFilterDataSelectionPanel
		extends AbstractAtdl4jInputAndFilterDataSelectionPanel
{
	public final Logger logger = Logger.getLogger(SWTAtdl4jInputAndFilterDataSelectionPanel.class);
	private Composite parentComposite;
	
	private Button atdl4jInputAndFilterDataPanelButton;
	private Shell atdl4jInputAndFilterDataPanelShell;
	private Button debugModeButton;
	
	public Object buildAtdl4jInputAndFilterDataSelectionPanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig)
	{
		return buildAtdl4jInputAndFilterDataSelectionPanel( (Composite) aParentOrShell, aAtdl4jConfig );
	}
	
	public Composite buildAtdl4jInputAndFilterDataSelectionPanel(Composite aParentOrShell, Atdl4jConfig aAtdl4jConfig)
	{
		parentComposite = (Composite) aParentOrShell;

		// -- Delegate back to AbstractAtdl4jInputAndFilterDataSelectionPanel -- 
		init( aParentOrShell, aAtdl4jConfig );
		
		
		atdl4jInputAndFilterDataPanelButton = new Button( aParentOrShell, SWT.PUSH );
		atdl4jInputAndFilterDataPanelButton.setText( "Input And Filter Data" );
		atdl4jInputAndFilterDataPanelButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e)
			{
				buttonInputAndFilterDataSelected();
			}
		});

		return parentComposite;
	}


	private void closeAtdl4jInputAndFilterDataPanelShell()
	{
		if ( ( atdl4jInputAndFilterDataPanelShell != null ) && 
			  ( ! atdl4jInputAndFilterDataPanelShell.isDisposed() ) )
		{
			atdl4jInputAndFilterDataPanelShell.close();
		}
	}
	
	private void buttonInputAndFilterDataSelected()
	{
		if ( ( atdl4jInputAndFilterDataPanelShell == null ) || 
			  ( atdl4jInputAndFilterDataPanelShell.isDisposed() ) )
		{
			atdl4jInputAndFilterDataPanelShell = createAtdl4jInputAndFilterDataPanelShell();
		}
		
		getAtdl4jInputAndFilterDataPanel().loadScreenWithAtdl4jConfig();
		getDebugModeButton().setSelection( getAtdl4jConfig().isDebugLoggingLevel() );
		
		// -- Open/Pop-up the dialog window --
		atdl4jInputAndFilterDataPanelShell.open();	
	}
	
	private void buttonOkSelected()
	{
		// -- Atdl4jInputAndFilterDataPanel.extractAtdl4jConfigFromScreen() populates/overlays data members within our Atdl4jConfig -- 
		if ( ! getAtdl4jInputAndFilterDataPanel().extractAtdl4jConfigFromScreen() )
		{
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayMessage( "Error", "Error extracting Atdl4jConfig extracted from screen" );
			return;
		}
		fireInputAndFilterDataSpecifiedEvent( getAtdl4jConfig().getInputAndFilterData() );
		closeAtdl4jInputAndFilterDataPanelShell();
	}
	
	private void buttonCancelSelected()
	{
		closeAtdl4jInputAndFilterDataPanelShell();
	}
	
	private Shell createAtdl4jInputAndFilterDataPanelShell()
	{
		Shell tempAtdl4jInputAndFilterDataPanelShell = new Shell( ( (Composite) getParentOrShell()).getDisplay(), SWT.APPLICATION_MODAL | SWT.TITLE | SWT.RESIZE );
		tempAtdl4jInputAndFilterDataPanelShell.setText( "atdl4j Input and Filter Data / Configuration Settings" );
		tempAtdl4jInputAndFilterDataPanelShell.setLayout( new GridLayout() );
		
		getAtdl4jInputAndFilterDataPanel().buildAtdl4jInputAndFilterDataPanel( tempAtdl4jInputAndFilterDataPanelShell, getAtdl4jConfig() );

		Composite tempFooter = new Composite( tempAtdl4jInputAndFilterDataPanelShell, SWT.NONE );
		tempFooter.setLayout( new FillLayout( SWT.HORIZONTAL ) );
		
		Button tempOkButton = new Button ( tempFooter, SWT.PUSH );
		tempOkButton.setText( "OK" );
		tempOkButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e)
			{
				buttonOkSelected();
			}
		} );
		
		Button tempCancelButton = new Button ( tempFooter, SWT.PUSH );
		tempCancelButton.setText( "Cancel" );
		tempCancelButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e)
			{
				buttonCancelSelected();
			}
		} );
		
		//blank button used as spacer
		Button tempBlankButton = new Button(tempFooter, SWT.NONE);
		tempBlankButton.setVisible(false);

		setDebugModeButton( new Button(tempFooter, SWT.CHECK) );		
		getDebugModeButton().setText("Debug Mode");

		getDebugModeButton().addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				getAtdl4jConfig().setDebugLoggingLevel( getDebugModeButton().getSelection() );
			}
		});
		
		tempAtdl4jInputAndFilterDataPanelShell.layout( true );
		tempAtdl4jInputAndFilterDataPanelShell.pack();
		
		return tempAtdl4jInputAndFilterDataPanelShell;
	}

	/**
	 * @return the debugModeButton
	 */
	public Button getDebugModeButton()
	{
		return this.debugModeButton;
	}

	/**
	 * @param aDebugModeButton the debugModeButton to set
	 */
	public void setDebugModeButton(Button aDebugModeButton)
	{
		this.debugModeButton = aDebugModeButton;
	}

}
