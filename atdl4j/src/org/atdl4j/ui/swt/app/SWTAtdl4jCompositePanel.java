package org.atdl4j.ui.swt.app;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.app.AbstractAtdl4jCompositePanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Represents the SWT-specific strategy selection and display GUI component.
 * 
 * Creation date: (Feb 28, 2010 6:26:02 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SWTAtdl4jCompositePanel
		extends AbstractAtdl4jCompositePanel
{
	public final Logger logger = Logger.getLogger(SWTAtdl4jCompositePanel.class);
	private Composite parentComposite;
	
	private Composite validateOutputSection;
	private Composite okCancelButtonSection;
	private Text outputFixMessageText;
	
	
	public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig)
	{
		return buildAtdl4jCompositePanel( (Composite) aParentOrShell, aAtdl4jConfig );
	}
	
	public Composite buildAtdl4jCompositePanel(Composite aParentComposite, Atdl4jConfig aAtdl4jConfig)
	{
		setParentComposite( aParentComposite );
		
// SWT doesn't use this, if we use it, then consumes vertical height at top		
//		Composite tempComposite = new Composite(aParentComposite, SWT.NONE);
//		GridLayout tempLayout = new GridLayout(1, false);
//		tempComposite.setLayout(tempLayout);
//		tempComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// -- Delegate back to AbstractAtdl4jCompositePanel -- 
		init( aParentComposite, aAtdl4jConfig );

		// -- Build the SWT.Composite from FixatdlFileSelectionPanel (filename / file dialog) --
		getFixatdlFileSelectionPanel().buildFixatdlFileSelectionPanel( getParentOrShell(), getAtdl4jConfig() );

		// -- Build the SWT.Composite from StrategySelectionPanel (drop down with list of strategies to choose from) --
		getStrategySelectionPanel().buildStrategySelectionPanel( getParentOrShell(), getAtdl4jConfig() );

		// -- Build the SWT.Composite from StrategyDescriptionPanel (text box with description for selected strategy) --
		getStrategyDescriptionPanel().buildStrategyDescriptionPanel( getParentOrShell(), getAtdl4jConfig() );
		getStrategyDescriptionPanel().setVisible( false );  // hide until there is data to populate it with
		
		// -- Build the SWT.Composite from StrategiesPanel (GUI display of each strategy's parameters) --
		getStrategiesPanel().buildStrategiesPanel( getParentOrShell(), getAtdl4jConfig() );

		// -- Build the SWT.Composite containing "Validate Output" button and outputFixMessageText --
		createValidateOutputSection();
		
		// -- Build the SWT.Composite containing "OK" and "Cancel" buttons --
		createOkCancelButtonSection();
		
//		return tempComposite;
		return aParentComposite;
	}

	protected Composite createValidateOutputSection()
	{
// 3/8/2010	Scott Atwell 	validateOutputSection = new Group(getShell(), SWT.NONE);
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
				validateButtonSelected();
			}
		});
		
		outputFixMessageText = new Text(validateOutputSection, SWT.BORDER);
		outputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		// -- triggers getAtdl4jConfig().isShowValidateOutputSection() check and setVisibleValidateOutputSection() --
		setValidateOutputText( "" );
		
		return validateOutputSection;
	}
	
	public void setVisibleValidateOutputSection( boolean aVisible )
	{
		 if ( ( validateOutputSection != null ) && ( ! validateOutputSection.isDisposed() ) )
		 {
			 validateOutputSection.setVisible( aVisible );
		 }
	}
	
	protected void setValidateOutputText(String aText)
	{
		if ( ( getAtdl4jConfig() != null ) && ( getAtdl4jConfig().isShowValidateOutputSection() ) )
		{
			if ( aText != null )
			{
// 3/14/2010 Scott Atwell				outputFixMessageText.setText( aText );
				outputFixMessageText.setText( aText.replace( '\n', ' ' ) );
				setVisibleValidateOutputSection( true );
			}
			else
			{
				outputFixMessageText.setText( "" );
				setVisibleValidateOutputSection( false );
			}
		}
		else
		{
// 3/14/2010 Scott Atwell			outputFixMessageText.setText( aText );
			outputFixMessageText.setText( aText.replace( '\n', ' ' ) );
			setVisibleValidateOutputSection( false );
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
		
		setVisibleOkCancelButtonSection( getAtdl4jConfig().isShowCompositePanelOkCancelButtonSection() );
		
		return okCancelButtonSection;
	}
	
	public void setVisibleOkCancelButtonSection( boolean aVisible )
	{
		 if ( ( okCancelButtonSection != null ) && ( ! okCancelButtonSection.isDisposed() ) )
		 {
			 okCancelButtonSection.setVisible( aVisible );
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


}
