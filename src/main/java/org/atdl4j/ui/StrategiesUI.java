package org.atdl4j.ui;

import java.util.List;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.StrategiesUIListener;

public interface StrategiesUI {

	// -- Call this after constructor --
	public void init(Atdl4jOptions aAtdl4jOptions);

	public Atdl4jOptions getAtdl4jOptions();
	
	public Object buildStrategiesPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler);

	public void createStrategyPanels(StrategiesT strategies, List<StrategyT> aFilteredStrategyList) throws FIXatdlFormatException; // throws Exception;
	 
	public void removeAllStrategyPanels();
	
	public void adjustLayoutForSelectedStrategy( StrategyT aStrategy );
	
	public boolean isPreCached();
	
	public void setPreCached( boolean aPreCached );
	
	public void addListener(StrategiesUIListener strategiesUIListener);
	
	public void removeListener(StrategiesUIListener strategiesUIListener);
	
	public void addWidgetListener(AtdlWidgetListener listener);
	
	public void removeWidgetListener(AtdlWidgetListener listener);

	public void setVisible( boolean aVisible );
	
// 12/15/2010 Scott Atwell	public StrategyUI getStrategyUI( StrategyT aStrategy );
	public StrategyUI getStrategyUI( StrategyT aStrategy, boolean aReinitPanelFlag );
	  
	public StrategyUI getCurrentlyDisplayedStrategyUI();
	public StrategyT getCurrentlyDisplayedStrategy();
}
