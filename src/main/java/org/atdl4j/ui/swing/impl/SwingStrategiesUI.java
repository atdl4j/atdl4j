package org.atdl4j.ui.swing.impl;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.impl.AbstractStrategiesUI;
import org.atdl4j.ui.swing.SwingListener;
import org.atdl4j.ui.swing.SwingWidget;

public class SwingStrategiesUI
	extends AbstractStrategiesUI
{
	private Window parentFrame;
	
	private JPanel strategiesPanel;
	
	private Map<String, SwingWidgetListener> swingWidgetListeners;
  
	private int pendingNotifications;
	
	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SwingStrategiesUI()
	{
	  this.swingWidgetListeners = new HashMap<String, SwingWidgetListener>(); 
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
		return buildStrategiesPanel( (Window) parentOrShell, atdl4jOptions, aAtdl4jUserMessageHandler );
	}
	
	public JPanel buildStrategiesPanel(Window aParentComposite, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler)
	{
		parentFrame = aParentComposite;
		
		setAtdl4jOptions( atdl4jOptions );
	
		setAtdl4jUserMessageHandler( aAtdl4jUserMessageHandler );
	
		// Main strategies panel
		strategiesPanel = new JPanel();
	
		return strategiesPanel;
	}
	
	public void removeAllStrategyPanels()
	{
		strategiesPanel.removeAll();
	}
	
	public void createStrategyPanels(StrategiesT aStrategies, List<StrategyT> aFilteredStrategyList) throws FIXatdlFormatException
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
        if (getAtdl4jOptions().isPreloadPanels()) {
          for (StrategyT strategy : aFilteredStrategyList) {
            removeAllStrategyPanels();
            StrategyUI ui = SwingStrategyUIFactory
                .createStrategyUIAndContainer(this, strategy);
            setCurrentlyDisplayedStrategyUI(ui);
          }
          setPreCached(true);
        }
	}  
	
	
	public void adjustLayoutForSelectedStrategy( StrategyT aStrategy )
	{
		muteWidgetNotification();
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
		allowWidgetNotification();
	}
	
	
  private void muteWidgetNotification() {
    pendingNotifications++;
  }
  
  private void allowWidgetNotification() {
    pendingNotifications--;
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
				setWidgetListeners(getCurrentlyDisplayedStrategyUI());
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
			setWidgetListeners(getCurrentlyDisplayedStrategyUI());
			
			return tempStrategyUI;
		}
	}

  private void setWidgetListeners(StrategyUI strategyUI) {
    for (SwingWidgetListener l :  swingWidgetListeners.values()) {
      l.dispose();
    }
    swingWidgetListeners.clear();
    for (Atdl4jWidget< ? > widget : strategyUI.getAtdl4jWidgetMap().values()) {
      // some widgets don't have a parameter reference, they are for control only (eg. radio buttons)
      if (widget.getParameter() != null)
      {
        if (logger.isDebugEnabled()) {
          logger.debug("Adding listener on "
              + strategyUI.getStrategy().getName() + " "
              + widget.getParameter().getName() + " widget " + widget);
        }
        swingWidgetListeners.put(widget.getParameter().getName(), new SwingWidgetListener((SwingWidget) widget));
      }
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
	
	
	private boolean isWidgetNotificationsAllowed() {
	  return pendingNotifications == 0;
	}
	
  public class SwingWidgetListener
      implements SwingListener
  {

    private String paramName;

    private final SwingWidget swingWidget;

    public SwingWidgetListener(SwingWidget swingWidget) {
      super();
      this.swingWidget = swingWidget;
      paramName = swingWidget.getParameter().getName();
      swingWidget.addListener(this);
    }

    public void dispose() {
      swingWidget.removeListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      fireWidgetChangedEvent(paramName);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
      fireWidgetChangedEvent(paramName);
    }

    private void fireWidgetChangedEvent(String aParamName) {
      if (logger.isDebugEnabled()) {
        logger.debug("Widget changed :" + aParamName);
      }
      if (isWidgetNotificationsAllowed()) {
        fireWidgetChanged(swingWidget);
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
      fireWidgetChangedEvent(paramName);
    }

    @Override
    public void handleEvent() {
      fireWidgetChangedEvent(paramName);
    }

    @Override
    public SwingWidget< ? > getAffectedWidget() {
      return swingWidget;
    }

    @Override
    public ValidationRule getRule() {
      return null;
    }

    @Override
    public void setCxlReplaceMode(boolean flag) {
      // 
    }

    @Override
    public void handleLoadFixMessageEvent() {
      fireWidgetChangedEvent(paramName);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      fireWidgetChangedEvent(paramName);      
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      fireWidgetChangedEvent(paramName);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      fireWidgetChangedEvent(paramName);      
    }

  }

}
