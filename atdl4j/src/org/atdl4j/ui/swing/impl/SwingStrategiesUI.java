package org.atdl4j.ui.swing.impl;

import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.impl.AbstractStrategiesUI;

public class SwingStrategiesUI
	extends AbstractStrategiesUI
{
	private final Logger logger = Logger.getLogger( SwingStrategiesUI.class );
	private JFrame parentFrame;
	
	private JPanel strategiesPanel;
	
	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SwingStrategiesUI()
	{
	}

	public SwingStrategiesUI(Atdl4jOptions aAtdl4jOptions)
	{
		init(aAtdl4jOptions);
	}

public void init(Atdl4jOptions aAtdl4jOptions)
{
	setAtdl4jOptions( aAtdl4jOptions );
}
	
public Object buildStrategiesPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
{
	return buildStrategiesPanel( (JFrame) parentOrShell, atdl4jOptions, aAtdl4jUserMessageHandler );
}

public JPanel buildStrategiesPanel(JFrame aParentComposite, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
{
	parentFrame = aParentComposite;
	
	setAtdl4jOptions( atdl4jOptions );

	setAtdl4jUserMessageHandler( aAtdl4jUserMessageHandler );

	// Main strategies panel
	strategiesPanel = new JPanel();
//	strategiesPanel.setPreferredSize(new Dimension(500, 500));

	return strategiesPanel;
}

public void removeAllStrategyPanels(){
	strategiesPanel.removeAll();
}

public void createStrategyPanels(StrategiesT aStrategies, List<StrategyT> aFilteredStrategyList)
{
	// -- Check to see if StrategiesT has changed (eg new file loaded) --
	if ( ( getStrategies() == null ) || ( ! getStrategies().equals( aStrategies ) ) )
	{
		setStrategies( aStrategies );
		
		setStrategiesRules( new HashMap<String, ValidationRule>() );
		for (EditT edit : getStrategies().getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit,
						getStrategiesRules(), getStrategies());
				getStrategiesRules().put(id, rule);
			} else {
				throw new IllegalArgumentException("Strategies-scoped edit without id");
			}
		}
	}
			
	setPreCached( false );
	setCurrentlyDisplayedStrategyUI( null );

	for ( StrategyT strategy : aFilteredStrategyList )
	{
		removeAllStrategyPanels();
		StrategyUI ui = SwingStrategyUIFactory.createStrategyUIAndContainer( this, strategy );
		setCurrentlyDisplayedStrategyUI( ui );

		if ( ui == null )
		{
			// skip to next strategy
			continue;
		}
	}
	setPreCached( true );
}  


public void adjustLayoutForSelectedStrategy( StrategyT aStrategy )
{
	
	if ( strategiesPanel != null )
	{
		// -- (aReinitPanelFlag=true) --
		StrategyUI tempStrategyUI = getStrategyUI( aStrategy, true );
		
		if ( tempStrategyUI == null  )
		{
			logger.info("ERROR:  Strategy name: " + aStrategy.getName() + " was not found.  (aStrategy: " + aStrategy + ")" );
			return;
		}

		logger.debug( "Invoking  tempStrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );								
		tempStrategyUI.reinitStrategyPanel();
	}
}


/* 
 * (non-Javadoc)
 * @see org.atdl4j.ui.app.StrategiesPanel#setVisible(boolean)
 */
@Override
public void setVisible(boolean aVisible)
{
	if ( strategiesPanel != null )
	{
		strategiesPanel.setVisible( aVisible );
	}
}


// 12/15/2010 Scott Atwell public StrategyUI getStrategyUI( StrategyT aStrategy )
public StrategyUI getStrategyUI( StrategyT aStrategy, boolean aReinitPanelFlag )
{
	if ( aStrategy.equals( getCurrentlyDisplayedStrategy() ) )
	{
		logger.debug("Strategy name: " + aStrategy.getName() + " is currently being displayed.  Returning getCurrentlyDisplayedStrategyUI()" );
// 12/15/2010 Scott Atwell return getCurrentlyDisplayedStrategyUI();
		if ( aReinitPanelFlag )
		{
			getCurrentlyDisplayedStrategyUI().reinitStrategyPanel();
		}
		
		return getCurrentlyDisplayedStrategyUI();
	}
	else
	{
		logger.debug("Strategy name: " + aStrategy.getName() + " is not currently displayed.  Invoking removeAllStrategyPanels() and returning createStrategyPanel()" );
		removeAllStrategyPanels();

		StrategyUI tempStrategyUI = SwingStrategyUIFactory.createStrategyUIAndContainer( this, aStrategy );
		setCurrentlyDisplayedStrategyUI( tempStrategyUI );
		
		logger.debug("Invoking relayoutCollapsibleStrategyPanels() for: " + aStrategy.getName() );
		tempStrategyUI.relayoutCollapsibleStrategyPanels();
		
		return tempStrategyUI;
	}
}

/**
 * @return the strategiesPanel
 */
protected JPanel getStrategiesPanel()
{
	return this.strategiesPanel;
}

/**
 * @param aStrategiesPanel the strategiesPanel to set
 */
protected void setStrategiesPanel(JPanel aStrategiesPanel)
{
	this.strategiesPanel = aStrategiesPanel;
}

}
