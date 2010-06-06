/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.swing.app;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.app.AbstractAtdl4jTesterPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

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
public class SwingAtdl4jTesterPanel
		extends AbstractAtdl4jTesterPanel
{
	public final Logger logger = Logger.getLogger(SwingAtdl4jTesterPanel.class);
	private Composite parentComposite;
	
	private Composite inputAndFilterDataAndLoadMessageComposite;
	
	public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig)
	{
		return buildAtdl4jTesterPanel( (Composite) aParentOrShell, aAtdl4jConfig );
	}
	
	public Composite buildAtdl4jTesterPanel(Composite aParentOrShell, Atdl4jConfig aAtdl4jConfig)
	{
		parentComposite = (Composite) aParentOrShell;

// SWT doesn't use this, if we use it, then consumes vertical height at top
//		Composite tempComposite = new Composite(shell, SWT.NONE);
//		GridLayout tempLayout = new GridLayout(1, false);
//		tempComposite.setLayout(tempLayout);
//		tempComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// -- Delegate back to AbstractAtdl4jTesterPanel -- 
		init( aParentOrShell, aAtdl4jConfig );
		
//TODO any "tester-specific stuff" to add???
		
//TODO		
//		if (getAtdl4jConfig().isShowTimezoneSelector())
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
		getAtdl4jInputAndFilterDataSelectionPanel().buildAtdl4jInputAndFilterDataSelectionPanel( inputAndFilterDataAndLoadMessageComposite, getAtdl4jConfig() );
		
		// -- Build the SWT.Composite from FixMsgLoadPanel ("Load FIX Message" button) --
		getFixMsgLoadPanel().buildFixMsgLoadPanel( inputAndFilterDataAndLoadMessageComposite, getAtdl4jConfig() );
		
		
		// -- Build the SWT.Composite from Atdl4jCompositePanel --
		getAtdl4jCompositePanel().buildAtdl4jCompositePanel( aParentOrShell, aAtdl4jConfig );

		return parentComposite;
	}
	
	public void closePanel()
	{
		if ( ( parentComposite != null ) && ( ! parentComposite.getShell().isDisposed() ) )
		{
			parentComposite.getShell().dispose();
		}
	}
}
