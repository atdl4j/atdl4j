package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.ui.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Widget;

public class SWTSliderWidget
		extends AbstractSWTWidget<String>
{
	private Scale slider;
	private Label label;
	private List<Label> sliderLabels;


	public Widget createWidget(Composite parent, int style)
	{
		sliderLabels = new ArrayList<Label>();
		String tooltip = getTooltip();
		GridData controlGD = new GridData( SWT.FILL, SWT.FILL, false, false );
		
		// label
		if ( control.getLabel() != null ) {
			label = new Label( parent, SWT.NONE );
			label.setText( control.getLabel() );
			if ( tooltip != null ) label.setToolTipText( tooltip );
			controlGD.horizontalSpan = 1;
		} else {
			controlGD.horizontalSpan = 2;
		}
		
		Composite c = new Composite( parent, SWT.NONE );
		c.setLayoutData(controlGD);

		int numColumns = ( ( (SliderT) control ).getListItem() != null && ( (SliderT) control ).getListItem().size() > 0 ) ? ( (SliderT) control )
				.getListItem().size() : 1;
		c.setLayout( new GridLayout( numColumns, true ) );

		// slider
		slider = new Scale( c, style | SWT.HORIZONTAL );
		slider.setIncrement( 1 );
		slider.setPageIncrement( 1 );
		GridData sliderData = new GridData( SWT.FILL, SWT.FILL, true, false );
		sliderData.horizontalSpan = numColumns;
		slider.setLayoutData( sliderData );
		slider.setMaximum( numColumns > 1 ? numColumns - 1 : 1 );

		// labels based on parameter ListItemTs
		if ( ( (SliderT) control ).getListItem() != null )
		{
			for ( ListItemT li : ( (SliderT) control ).getListItem() )
			{
				Label label = new Label( c, SWT.NONE );
				if (li.getUiRep() != null && !li.getUiRep().equals(""))
				{
					label.setText( li.getUiRep() );
				} else {
					// add some whitespace for hover tooltips
					label.setText( "   " );
				}			
				label.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false ) );
				for ( EnumPairT ep : parameter.getEnumPair() )
				{
				    if (ep.getEnumID().equals(li.getEnumID()) && ep.getDescription() != null && !ep.getDescription().equals(""))
				    {
				    	label.setToolTipText(ep.getDescription());
				    }
				}
				sliderLabels.add(label);
			}
		}

		// tooltip

		if ( tooltip != null )
		{
			slider.setToolTipText( tooltip );
			label.setToolTipText( tooltip );
		}

		if ( ControlHelper.getInitValue( control, getAtdl4jOptions() ) != null )
			setValue( (String) ControlHelper.getInitValue( control, getAtdl4jOptions() ), true );

		return parent;
	}
	
	
	public String getControlValueRaw()
	{
		return ( (SliderT) control ).getListItem().get( slider.getSelection() ).getEnumID();
	}

	public String getParameterValue()
	{
		return getParameterValueAsEnumWireValue();
	}

	public void setValue(String value)
	{
		this.setValue( value, false );
	}

	public void setValue(String value, boolean setValueAsControl)
	{
		for ( int i = 0; i < getListItems().size(); i++ )
		{
			if ( setValueAsControl || parameter == null )
			{
				if ( getListItems().get( i ).getEnumID().equals( value ) )
				{
					slider.setSelection( i );
					break;
				}
			}
			else
			{
				if ( parameter.getEnumPair().get( i ).getWireValue().equals( value ) )
				{
					slider.setSelection( i );
					break;
				}
			}
		}
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if (label != null) widgets.add( label );
		widgets.add( slider );
		widgets.addAll( sliderLabels );
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.add( slider );
		widgets.addAll( sliderLabels );
		return widgets;
	}

	public void addListener(Listener listener)
	{
		slider.addListener( SWT.Selection, listener );
	}

	public void removeListener(Listener listener)
	{
		slider.removeListener( SWT.Selection, listener );
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.Atdl4jWidget#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( slider != null ) && ( ! slider.isDisposed() ) )
		{
			if ( aControlInitValue != null )
			{
				// -- apply initValue if one has been specified --
				setValue( (String) aControlInitValue, true );
			}
			else
			{
				// -- set to minimum when no initValue exists --
				slider.setSelection( slider.getMinimum() );
			}
		}
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
}
