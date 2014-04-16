/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.swt.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.app.StrategySelectionEvent;
import org.atdl4j.ui.app.impl.AbstractAtdl4jTesterPanel;
import org.atdl4j.ui.swt.util.SWTMenuHelper;
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
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Represents the SWT-specific "TesterApp" core GUI component (without a main() line).
 * Contains a Atdl4jCompositePanel.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * Creation date: (Feb 28, 2010 6:26:02 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SWTAtdl4jTesterPanel
		extends AbstractAtdl4jTesterPanel
{
	public final Logger logger = Logger.getLogger(SWTAtdl4jTesterPanel.class);
	private Composite parentComposite;
	
	private Composite inputAndFilterDataAndLoadMessageComposite;
	private Composite validateOutputSection;
	
	private MenuItem showFileSelectionMenuItem;
	private MenuItem showValidateOutputMenuItem;
	
	private Composite okCancelButtonSection;
	private Text outputFixMessageText;

	
	public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		return buildAtdl4jTesterPanel( (Composite) aParentOrShell, aAtdl4jOptions );
	}
	
	public Composite buildAtdl4jTesterPanel(Composite aParentOrShell, Atdl4jOptions aAtdl4jOptions)
	{
		parentComposite = (Composite) aParentOrShell;

		// -- Delegate back to AbstractAtdl4jTesterPanel -- 
		init( aParentOrShell, aAtdl4jOptions );
		
//TODO any "tester-specific stuff" to add???
		
//TODO		
//		if (getAtdl4jOptions().isShowTimezoneSelector())
//		{
//
//		    Label tzLabel = new Label(headerComposite, SWT.NONE);
//		    tzLabel.setText("Timezone:");
//		    // dropDownList
//		    Combo tzDropDown = new Combo(headerComposite, SWT.READ_ONLY | SWT.BORDER);
//		    GridData tzData = new GridData(SWT.FILL, SWT.CENTER, true, true);
//		    tzData.horizontalSpan = 2;
//		    tzDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
//			    false));
//		}


		inputAndFilterDataAndLoadMessageComposite = new Group( aParentOrShell, SWT.NONE );
		((Group) inputAndFilterDataAndLoadMessageComposite).setText( "Testing Input" );
		inputAndFilterDataAndLoadMessageComposite.setLayout( new org.eclipse.swt.layout.GridLayout(2, false) );
		inputAndFilterDataAndLoadMessageComposite.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, false ) );
		
		// -- Build the SWT.Composite from Atdl4jInputAndFilterDataSelectionPanel ("Input Data/Filter Criteria" button) --
		getAtdl4jInputAndFilterDataSelectionPanel().buildAtdl4jInputAndFilterDataSelectionPanel( inputAndFilterDataAndLoadMessageComposite, getAtdl4jOptions(), getAtdl4jCompositePanel().getAtdl4jUserMessageHandler() );
		
		// -- Build the SWT.Composite from FixMsgLoadPanel ("Load FIX Message" button) --
		getFixMsgLoadPanel().buildFixMsgLoadPanel( inputAndFilterDataAndLoadMessageComposite, getAtdl4jOptions() );
		
		// -- Build the SWT.Composite from FixatdlFileSelectionPanel (filename / file dialog) --
		getFixatdlFileSelectionPanel().buildFixatdlFileSelectionPanel( getParentOrShell(), getAtdl4jOptions() );

		// -- Build the SWT.Composite from Atdl4jCompositePanel --
		getAtdl4jCompositePanel().buildAtdl4jCompositePanel( aParentOrShell, aAtdl4jOptions );

		// -- Build the SWT.Composite containing "Validate Output" button and outputFixMessageText --
		createValidateOutputSection();

		// -- Build the SWT.Composite containing "OK" and "Cancel" buttons --
		createOkCancelButtonSection();

		// -- Build the SWT MenuItems --
		createMenuItems();
	
		
		// -- Implied --
/**		
		setVisibleTestingInputSection( true );
		setVisibleFileSelectionSection( true );
		setVisibleValidateOutputSection( true );
		setVisibleOkCancelButtonSection( true );
**/
		
		return parentComposite;
	}
	
	public void closePanel()
	{
		if ( ( parentComposite != null ) && ( ! parentComposite.getShell().isDisposed() ) )
		{
			parentComposite.getShell().dispose();
		}
	}
	
	public void setVisibleFileSelectionSection( boolean aVisible )
	{
		 if ( getFixatdlFileSelectionPanel() != null ) 
		 {
			 getFixatdlFileSelectionPanel().setVisible( aVisible );

			 if ( showFileSelectionMenuItem != null )
			 {
				 showFileSelectionMenuItem.setSelection( aVisible );
			 }
			 packLayout();
		 }
	}
	
	protected void createMenuItems()
	{
		// -- "Show File Selection" --
		setVisibleFileSelectionSection( Atdl4jConfig.getConfig().isShowFileSelectionSection() );
		showFileSelectionMenuItem = SWTMenuHelper.addShellPopupCheckMenuItem( getShell(), "Show File Selection" );
		showFileSelectionMenuItem.setSelection( Atdl4jConfig.getConfig().isShowFileSelectionSection() );
		showFileSelectionMenuItem.addListener( SWT.Selection, new Listener()
		{
			@Override
			public void handleEvent(Event aEvent)
			{
				setVisibleFileSelectionSection( showFileSelectionMenuItem.getSelection() );
			}
		});
		
		
		// -- "Show Validate Output" --
		setVisibleValidateOutputSection( Atdl4jConfig.getConfig().isShowValidateOutputSection() );
		showValidateOutputMenuItem = SWTMenuHelper.addShellPopupCheckMenuItem( getShell(), "Show Validate Output" );
		showValidateOutputMenuItem.setSelection( Atdl4jConfig.getConfig().isShowValidateOutputSection() );
		showValidateOutputMenuItem.addListener( SWT.Selection, new Listener()
		{
			@Override
			public void handleEvent(Event aEvent)
			{
				setVisibleValidateOutputSection( showValidateOutputMenuItem.getSelection() );
			}
		});
		
		
	}
	
	protected Composite createValidateOutputSection()
	{
		// -- SWTVisibleGroup avoids consuming vertical space when hidden via setVisible(false) --
		validateOutputSection = new SWTVisibleGroup(getShell(), SWT.NONE);
		((Group) validateOutputSection).setText("Validation");
		validateOutputSection.setLayout(new GridLayout(2, false));
		validateOutputSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// validate button
		Button validateButton = new Button(validateOutputSection, SWT.NONE);
		validateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		validateButton.setText("Validate Output");
		validateButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
                		try {
                		    validateButtonSelected();
                		} catch (ValidationException ex) {
                		    logger.info("Validation Exception:", ex);
                		    getAtdl4jUserMessageHandler().displayException("Validation Exception", "", ex);
                		}
			}
		});
		
		outputFixMessageText = new Text(validateOutputSection, SWT.BORDER);
		outputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		setValidateOutputText( "" );
		
		return validateOutputSection;
	}
	
	public void setVisibleValidateOutputSection( boolean aVisible )
	{
		 if ( ( validateOutputSection != null ) && ( ! validateOutputSection.isDisposed() ) )
		 {
			 validateOutputSection.setVisible( aVisible );
			 
			 if ( validateOutputSection.getLayoutData() instanceof GridData )
			 {
				((GridData) validateOutputSection.getLayoutData()).exclude = !aVisible;
			 }
			 
			 if ( showValidateOutputMenuItem != null )
			 {
				 showValidateOutputMenuItem.setSelection( aVisible );
			 }
			 packLayout();
		 }
	}
	
	public void setVisibleTestingInputSection( boolean aVisible )
	{
		 if ( ( inputAndFilterDataAndLoadMessageComposite != null ) && ( ! inputAndFilterDataAndLoadMessageComposite.isDisposed() ) )
		 {
			 inputAndFilterDataAndLoadMessageComposite.setVisible( aVisible );
			 if ( inputAndFilterDataAndLoadMessageComposite.getLayoutData() instanceof GridData )
			 {
				((GridData) inputAndFilterDataAndLoadMessageComposite.getLayoutData()).exclude = !aVisible;
			 }

			 packLayout();
		 }
	}

	protected Composite createOkCancelButtonSection()
	{
		// -- SWTVisibleComposite avoids consuming vertical space when hidden via setVisible(false) --
		okCancelButtonSection = new SWTVisibleComposite(getShell(), SWT.NONE);
		okCancelButtonSection.setLayout(new GridLayout(2, true));
		okCancelButtonSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// OK button
		Button okButton = new Button(okCancelButtonSection, SWT.NONE);
		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		okButton.setText("OK");
		okButton.setToolTipText( "Validate and accept the specified strategy and parameters" );
		okButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				okButtonSelected();
			}
		});
		
		// Cancel button
		Button cancelButton = new Button(okCancelButtonSection, SWT.NONE);
		cancelButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText( "Cancel ignoring any specified changes" );
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				cancelButtonSelected();
			}
		});
		
		setVisibleOkCancelButtonSection( Atdl4jConfig.getConfig().isShowTesterPanelOkCancelButtonSection() );
		
		return okCancelButtonSection;
	}
	
	public void setVisibleOkCancelButtonSection( boolean aVisible )
	{
		 if ( ( okCancelButtonSection != null ) && ( ! okCancelButtonSection.isDisposed() ) )
		 {
			 okCancelButtonSection.setVisible( aVisible );
			 if ( okCancelButtonSection.getLayoutData() instanceof GridData )
			 {
				((GridData) okCancelButtonSection.getLayoutData()).exclude = !aVisible;
			 }
			 packLayout();
		 }
	}

	protected void packLayout()
	{
		getShell().layout();
		getShell().pack();
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

  @Override
  public void beforeStrategyIsSelected(StrategySelectionEvent event) {
  }
	
}
