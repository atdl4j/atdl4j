package org.atdl4j.ui.swt.app;


import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.app.AbstractStrategyDescriptionPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;


/**
 * Represents the SWT-specific Strategy Description GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public class SWTStrategyDescriptionPanel 
	extends AbstractStrategyDescriptionPanel
{
	private final Logger logger = Logger.getLogger(SWTStrategyDescriptionPanel.class);
	
	private Composite composite;
	private Text strategyDescription;

	private int DEFAULT_COMPOSITE_HEIGHT_HINT = 45;
	private int DEFAULT_STRATEGY_DESCRIPTION_HEIGHT_HINT = 35;

	public Object buildStrategyDescriptionPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig)
	{
		return buildStrategyDescriptionPanel( (Composite) parentOrShell, atdl4jConfig );
	}
	
	public Composite buildStrategyDescriptionPanel(Composite aParentComposite, Atdl4jConfig atdl4jConfig)
	{
		setAtdl4jConfig( atdl4jConfig );
	
		composite = new SWTVisibleGroup(aParentComposite, SWT.NONE);
		((Group) composite).setText("Strategy Description");
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

  		strategyDescription = new Text(composite, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL );
  	   strategyDescription.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
  		strategyDescription.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		
  		
  		GridData descData = new GridData(SWT.FILL, SWT.FILL, true, false);
 		descData.heightHint = DEFAULT_STRATEGY_DESCRIPTION_HEIGHT_HINT;
		strategyDescription.setLayoutData(descData);
	
		return composite;
	}


	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.AbstractStrategyDescriptionPanel#setStrategyDescriptionText(java.lang.String)
	 */
	protected void setStrategyDescriptionText(String aText)
	{
		if ( strategyDescription != null )
		{
			strategyDescription.setText( aText );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategyDescriptionPanel#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aVisible)
	{
		// -- Note that SWTVisibleGroup vs. Group as this handles setVisible() better (without consuming vertical space in GridLayout) --
		if ( ( composite != null ) && ( ! composite.isDisposed() ) )
		{
			composite.setVisible( aVisible );
		}
	}
}
