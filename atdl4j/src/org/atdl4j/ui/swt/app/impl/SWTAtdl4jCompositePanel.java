package org.atdl4j.ui.swt.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.Atdl4jClassLoadException;
import org.atdl4j.ui.app.impl.AbstractAtdl4jCompositePanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

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
	
	private Composite okCancelButtonSection;
	
	public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) throws Atdl4jClassLoadException
	{
		return buildAtdl4jCompositePanel( (Composite) aParentOrShell, aAtdl4jOptions );
	}
	
	public Composite buildAtdl4jCompositePanel(Composite aParentComposite, Atdl4jOptions aAtdl4jOptions) throws Atdl4jClassLoadException
	{
		setParentComposite( aParentComposite );
		
		// -- Delegate back to AbstractAtdl4jCompositePanel -- 
		init( aParentComposite, aAtdl4jOptions );

		// -- Build the SWT.Composite from StrategySelectionPanel (drop down with list of strategies to choose from) --
		getStrategySelectionPanel().buildStrategySelectionPanel( getParentOrShell(), getAtdl4jOptions() );

		// -- Build the SWT.Composite from StrategyDescriptionPanel (text box with description for selected strategy) --
		getStrategyDescriptionPanel().buildStrategyDescriptionPanel( getParentOrShell(), getAtdl4jOptions() );
		getStrategyDescriptionPanel().setVisible( false );  // hide until there is data to populate it with
		
		// -- Build the SWT.Composite from StrategiesPanel (GUI display of each strategy's parameters) --
		getStrategiesUI().buildStrategiesPanel( getParentOrShell(), getAtdl4jOptions(), getAtdl4jUserMessageHandler() );

		// -- Build the SWT.Composite containing "OK" and "Cancel" buttons --
		createOkCancelButtonSection();
		
		return aParentComposite;
	}

	
	protected Composite createOkCancelButtonSection()
	{
		okCancelButtonSection = new Composite(getShell(), SWT.NONE);
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
		
		setVisibleOkCancelButtonSection( Atdl4jConfig.getConfig().isShowCompositePanelOkCancelButtonSection() );
		
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
}
