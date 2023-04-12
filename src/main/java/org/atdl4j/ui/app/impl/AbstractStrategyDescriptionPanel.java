package org.atdl4j.ui.app.impl;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.StrategyDescriptionPanel;

/**
 * Represents the base, non-GUI system-specific Strategy Description GUI component.
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public abstract class AbstractStrategyDescriptionPanel
		implements StrategyDescriptionPanel
{
	private Atdl4jOptions atdl4jOptions = null;

	protected abstract void setStrategyDescriptionText( String aText );

	/* 
	 * Loads aStrategy.getDescription() if populated and if Atdl4jOptions.isShowStrategyDescription(), 
	 * otherwise invokes setVisible( false ).
	 * 
	 * @param aStrategy
	 */
	public void loadStrategyDescriptionVisible( StrategyT aStrategy )
	{
		setStrategyDescriptionText( "" );
		if ( ( Atdl4jConfig.getConfig().isShowStrategyDescription() )
			&& ( aStrategy != null ) && ( aStrategy.getDescription() != null )  )
		{
			setVisible( true );
		}
		else
		{
			setVisible( false );
		}
	}	
	
	public void loadStrategyDescriptionText( StrategyT aStrategy )
	{
		if ( ( Atdl4jConfig.getConfig().isShowStrategyDescription() ) )
		{
			if ( ( aStrategy != null ) && ( aStrategy.getDescription() != null ) )
			{
				setStrategyDescriptionText( aStrategy.getDescription() );
			}
			else
			{
				setStrategyDescriptionText( "" );
			}
		}
	}

	/**
	 * @param atdl4jOptions the atdl4jOptions to set
	 */
	protected void setAtdl4jOptions(Atdl4jOptions atdl4jOptions)
	{
		this.atdl4jOptions = atdl4jOptions;
	}


	/**
	 * @return the atdl4jOptions
	 */
	public Atdl4jOptions getAtdl4jOptions()
	{
		return atdl4jOptions;
	}
}