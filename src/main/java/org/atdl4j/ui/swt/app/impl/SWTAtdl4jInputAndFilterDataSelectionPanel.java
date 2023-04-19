package org.atdl4j.ui.swt.app.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.impl.AbstractAtdl4jInputAndFilterDataSelectionPanel;
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
	public final Logger logger = LoggerFactory.getLogger(SWTAtdl4jInputAndFilterDataSelectionPanel.class);
	private Composite parentComposite;
	
	private Button atdl4jInputAndFilterDataPanelButton;
	private Shell atdl4jInputAndFilterDataPanelShell;

	public Object buildAtdl4jInputAndFilterDataSelectionPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
	{
		return buildAtdl4jInputAndFilterDataSelectionPanel( (Composite) aParentOrShell, aAtdl4jOptions, aAtdl4jUserMessageHandler );
	}
	
	public Composite buildAtdl4jInputAndFilterDataSelectionPanel(Composite aParentOrShell, Atdl4jOptions aAtdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
	{
		parentComposite = aParentOrShell;

		// -- Delegate back to AbstractAtdl4jInputAndFilterDataSelectionPanel -- 
		init( aParentOrShell, aAtdl4jOptions, aAtdl4jUserMessageHandler );
		
		
		atdl4jInputAndFilterDataPanelButton = new Button( aParentOrShell, SWT.PUSH );
		atdl4jInputAndFilterDataPanelButton.setText( "Input And Filter Data" );
		atdl4jInputAndFilterDataPanelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
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
		
		getAtdl4jInputAndFilterDataPanel().loadScreenWithAtdl4jOptions();

		// -- Open/Pop-up the dialog window --
		atdl4jInputAndFilterDataPanelShell.open();	
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
		
		getAtdl4jInputAndFilterDataPanel().buildAtdl4jInputAndFilterDataPanel( tempAtdl4jInputAndFilterDataPanelShell, getAtdl4jOptions() );

		Composite tempFooter = new Composite( tempAtdl4jInputAndFilterDataPanelShell, SWT.NONE );
		tempFooter.setLayout( new FillLayout( SWT.HORIZONTAL ) );
		
		Button tempOkButton = new Button ( tempFooter, SWT.PUSH );
		tempOkButton.setText( "OK" );
		tempOkButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				buttonOkSelected();
			}
		} );
		
		Button tempCancelButton = new Button ( tempFooter, SWT.PUSH );
		tempCancelButton.setText( "Cancel" );
		tempCancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				buttonCancelSelected();
			}
		} );
		
		//blank button used as spacer
		Button tempBlankButton = new Button(tempFooter, SWT.NONE);
		tempBlankButton.setVisible(false);

		tempAtdl4jInputAndFilterDataPanelShell.layout( true );
		tempAtdl4jInputAndFilterDataPanelShell.pack();
		
		return tempAtdl4jInputAndFilterDataPanelShell;
	}

}
