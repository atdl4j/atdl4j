package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.TZTimeOnlyT;
import org.atdl4j.fixatdl.core.TZTimestampT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.core.UseT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.ui.impl.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Clock widget which will display differently depending on the parameter type
 * used.
 * 
 * UTCTimestampT and UTCTimeOnlyT display in the user's local time, but will be
 * converted to/from UTC when outputting/reading FIX.
 * 
 * LocalMktTimeT, TZTimestampT, and TZTimeOnlyT display in the user's local
 * time, effectively 'what-you-see-is-what-you-get' in terms of the FIX output.
 * 
 * MonthYearT are UTCDateOnlyT are displayed in UTC (GMT) time, effectively
 * 'what-you-see-is-what-you-get' in terms of the FIX output.
 * 
 * Note that users should not use a UTCDateOnlyT clock and a UTCDTimeOnlyT clock
 * side-by-side, as this cannot account for time conversion across midnight.
 * 
 * @author john.shields
 */
public class SWTClockWidget
	extends AbstractSWTWidget<Comparable<DateTime>>
{

	private static final Logger logger = Logger.getLogger( SWTClockWidget.class );

	private org.eclipse.swt.widgets.DateTime dateClock;
	private org.eclipse.swt.widgets.DateTime timeClock;

	// -- enabledButton (for optional parameters) and label are mutually exclusive (use the one that is not null) --
	private Button enabledButton;
	private Label label;

	private boolean showMonthYear;
	private boolean showDay;
	private boolean showTime;
	private boolean useNowAsDate = false;

	public Widget createWidget(Composite parent, int style)
	{
		if ( parameter instanceof UTCTimestampT || parameter instanceof TZTimestampT)
		{
		    	if (getAtdl4jOptions()==null||getAtdl4jOptions().isShowDateInputOnTimestampClockControl())
		    	{
        			showMonthYear = true;
        			showDay = true;
		    	} else {
        			showMonthYear = false;
        			showDay = false;
        			useNowAsDate = true;
		    	}
			showTime = true;
		}
		else if ( parameter instanceof UTCDateOnlyT || parameter instanceof LocalMktDateT )
		{
			showMonthYear = true;
			showDay = true;
			showTime = false;
		}
		else if ( parameter instanceof MonthYearT )
		{
			showMonthYear = true;
			showDay = false;
			showTime = false;
		}
		else if ( parameter == null || parameter instanceof UTCTimeOnlyT || parameter instanceof TZTimeOnlyT )
		{
			showMonthYear = false;
			showDay = false;
			showTime = true;
		}
		
		boolean hasLabelOrCheckbox = false;
		
		if ( ( getAtdl4jOptions() != null ) &&
			  ( getAtdl4jOptions().isShowEnabledCheckboxOnOptionalClockControl() ) && 
			  ( parameter != null ) && 
			  ( UseT.OPTIONAL.equals( parameter.getUse() ) ) )
		{
			hasLabelOrCheckbox = true;
			enabledButton = new Button( parent, SWT.CHECK );
			if ( control.getLabel() != null )
			{
				enabledButton.setText( control.getLabel() );
			}
			enabledButton.setToolTipText( "Check to enable and specify or uncheck to disable this optional parameter value" );
			enabledButton.setSelection( false ); // TODO disabled by default ????
			enabledButton.addSelectionListener( new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					applyEnabledSetting();
				}
			} );
		}
		else
		// "required" -- use standard label without preceding checkbox
		{
			// label
			hasLabelOrCheckbox = true;
			label = new Label( parent, SWT.NONE );
			if ( control.getLabel() != null )
				label.setText( control.getLabel() );
		}

		Composite c = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( 2, true );
		gridLayout.horizontalSpacing = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginLeft = gridLayout.marginRight = 0;
		gridLayout.marginTop = gridLayout.marginBottom = 0;
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		c.setLayout( gridLayout );
		GridData controlGD = new GridData( SWT.FILL, SWT.FILL, false, false );
		controlGD.horizontalSpan = hasLabelOrCheckbox ? 1 : 2;
		c.setLayoutData(controlGD);
		
		GridData clockGD = new GridData( SWT.FILL, SWT.FILL, false, false );
		clockGD.horizontalSpan = (showMonthYear && showTime) ? 1 : 2;						
		
		// date clock
		if ( showMonthYear )
		{
			dateClock = new org.eclipse.swt.widgets.DateTime( c, style | SWT.BORDER | SWT.DATE | ( showDay ? SWT.MEDIUM : SWT.SHORT ) );
			dateClock.setLayoutData(clockGD);

		}
		// time clock
		if ( showTime )
		{
			timeClock = new org.eclipse.swt.widgets.DateTime( c, style | SWT.BORDER | SWT.TIME | SWT.MEDIUM );
			timeClock.setLayoutData(clockGD);
		}

		// tooltip
		String tooltip = getTooltip();
		if ( tooltip != null )
		{
			if ( showMonthYear )
				dateClock.setToolTipText( tooltip );
			if ( showTime )
				timeClock.setToolTipText( tooltip );
			if ( label != null )
				label.setToolTipText( tooltip );
			if ( enabledButton != null && tooltip != null )
				enabledButton.setToolTipText( tooltip );
		}

		// init value, if applicable
		setAndRenderInitValue( (XMLGregorianCalendar ) ControlHelper.getInitValue( control, getAtdl4jOptions() ), ((ClockT) control).getInitValueMode() );

		
		if ( enabledButton != null )
		{
			applyEnabledSetting();
		}

		return parent;
	}

	public DateTimeZone getLocalMktTz() 
		throws IllegalArgumentException
	{
		// This will throw IllegalArgumentException if ID cannot be resolved
		return DateTimeConverter.convertTimezoneToDateTimeZone( ((ClockT) control).getLocalMktTz() );
	}

	public DateTime getControlValueRaw()
	{
		if ( ( dateClock == null ) && ( timeClock == null ) )
		{
			return null; // disabled, no value to use
		}

		DateTime now = null; 
		if (useNowAsDate) now = new DateTime( DateTimeZone.getDefault() );		
		DateTime result = new DateTime( useNowAsDate ? now.getYear() : showMonthYear ? dateClock.getYear() : 1970,
						useNowAsDate ? now.getMonthOfYear() : showMonthYear ? dateClock.getMonth() + 1 : 1,
						useNowAsDate ? now.getDayOfMonth() : showDay ? dateClock.getDay() : 1,
                				showTime ? timeClock.getHours() : 0,
                				showTime ? timeClock.getMinutes() : 0,
                				showTime ? timeClock.getSeconds() : 0, 
                				0, 
                				DateTimeZone.getDefault() );
		

		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce an unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
		if ( parameter == null || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT )
		{
			result = result.withZone( DateTimeZone.UTC );
			logger.debug( "getControlValue() parameter: " + parameter + " result: " + result );
		}
		return result;
	}

	public void setValue(Comparable<DateTime> value)
	{
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce an unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
		if ( parameter == null || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT )
		{
			logger.debug( "setValue() parameter: " + parameter + " value: " + value );
			// -- no need to adjust DateTime --
		}
		
		// -- Force control to display time portion in local
		DateTime tempLocalTzDateTime = ((DateTime)value).withZone( DateTimeZone.getDefault() );
		
		if ( showMonthYear )
		{
			dateClock.setMonth( tempLocalTzDateTime.getMonthOfYear() - 1 );
			dateClock.setYear( tempLocalTzDateTime.getYear() );
		}
		if ( showDay )
		{
			dateClock.setDay( tempLocalTzDateTime.getDayOfMonth() );
		}
		if ( showTime )
		{
			timeClock.setHours( tempLocalTzDateTime.getHourOfDay() );
			timeClock.setMinutes( tempLocalTzDateTime.getMinuteOfHour() );
			timeClock.setSeconds( tempLocalTzDateTime.getSecondOfMinute() );
		}
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if ( enabledButton != null )
		{
			widgets.add( enabledButton );
		}

		if ( label != null )
		{
			widgets.add( label );
		}

		if ( showMonthYear )
		{
			widgets.add( dateClock );
		}
		if ( showTime )
		{
			widgets.add( timeClock );
		}
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();

		if ( showMonthYear )
		{
			widgets.add( dateClock );
		}
		if ( showTime )
		{
			widgets.add( timeClock );
		}
		return widgets;
	}


	private void applyEnabledSetting()
	{
		if ( enabledButton != null )
		{
			if ( ( dateClock != null ) && ( dateClock.isVisible() ) )
			{
				dateClock.setEnabled( enabledButton.getSelection() );
			}

			if ( ( timeClock != null ) && ( timeClock.isVisible() ) )
			{
				timeClock.setEnabled( enabledButton.getSelection() );
			}
		}
	}

	public void addListener(Listener listener)
	{
		if ( showMonthYear )
		{
			dateClock.addListener( SWT.Selection, listener );
		}
		if ( showTime )
		{
			timeClock.addListener( SWT.Selection, listener );
		}
	}

	public void removeListener(Listener listener)
	{
		if ( showMonthYear )
		{
			dateClock.removeListener( SWT.Selection, listener );
		}
		if ( showTime )
		{
			timeClock.removeListener( SWT.Selection, listener );
		}
	}

	/**
	 * Used when applying Clock@initValue (xs:time)
	 * @param aValue
	 * @param @InitValueMode
	 */
	protected void setAndRenderInitValue( XMLGregorianCalendar aValue, int aInitValueMode )
	{
		if ( aValue != null )
		{
			// -- Note that this will throw IllegalArgumentException if timezone ID
			// specified cannot be resolved --
			DateTimeZone tempLocalMktTz = getLocalMktTz();
			logger.debug( "control.getID(): " + control.getID() + " aValue: " + aValue + " getLocalMktTz(): " + tempLocalMktTz );

			// -- localMktTz is required when using/interpreting aValue --
			if ( tempLocalMktTz == null )
			{
				throw new IllegalArgumentException( "localMktTz is required when aValue (" + aValue + ") is specified. (Control.ID: "
						+ control.getID() + ")" );
			}

			DateTime tempNow = new DateTime( tempLocalMktTz );

			DateTime tempInit = new DateTime( 
					( showMonthYear && aValue.getYear() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getYear() : tempNow.getYear(), 
					( showMonthYear && aValue.getMonth() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getMonth() : tempNow.getMonthOfYear(), 
					( showMonthYear && aValue.getDay() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getDay() : tempNow.getDayOfMonth(), 
					( showMonthYear && aValue.getHour() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getHour() : 0,
					( showMonthYear && aValue.getMinute() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getMinute() : 0,
					( showMonthYear && aValue.getSecond() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getSecond(): 0, 
					0,
					tempLocalMktTz );
			
			if ( ( aInitValueMode == Atdl4jConstants.CLOCK_INIT_VALUE_MODE_USE_CURRENT_TIME_IF_LATER ) &&
				  ( tempNow.isAfter( tempInit ) ) )
			{
				// -- Use current time --
				tempInit = tempNow;
			}
			
			// -- Make sure that the value is rendered on the display in local timezone --
			setValue( tempInit.withZone( DateTimeZone.getDefault() ) );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.Atdl4jWidget#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( aControlInitValue != null )
		{
			// -- apply initValue if one has been specified --
			setAndRenderInitValue( (XMLGregorianCalendar ) aControlInitValue, ((ClockT) control).getInitValueMode() );
		}
		else
		{
			// -- reinit the time to present time --
			setValue( new DateTime() );
		}
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.impl.AbstractAtdl4jWidget#setFIXValue(java.lang.String)
	 */
	@Override
	public void setFIXValue(String aFIXValue)
	{
		super.setFIXValue( aFIXValue );
		
		DateTime tempFIXValueTime = getControlValueRaw();
		DateTime tempCurrentTime = new DateTime();
		
		// -- Check to see if the time set is < current time --
		if ( tempCurrentTime.isAfter( tempFIXValueTime ) )
		{
			logger.debug( "setFIXValue(" + aFIXValue + ") resulted in time < present (" + tempFIXValueTime + " < " + tempCurrentTime + ")" );
			
			Integer tempClockPastTimeSetFIXValueRule = getAtdl4jOptions().getClockPastTimeSetFIXValueRule( getControl() );
			logger.debug( "Control: " + getControl().getID() + " tempClockPastTimeSetFIXValueRule: " + tempClockPastTimeSetFIXValueRule );
			
			if ( Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				// -- keep as-is --
				logger.debug("Per Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS rule -- Retaining: " + tempFIXValueTime );
			}
			else if ( Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				logger.debug("Per Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT rule -- Setting: " + tempCurrentTime + " ( vs. " + tempFIXValueTime + ")" );
				setValue( tempCurrentTime );
			} 
			else if ( Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				logger.debug("Per Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL rule -- Setting control to 'null value' ( vs. " + tempFIXValueTime + ")" );
				setValueAsString( Atdl4jConstants.VALUE_NULL_INDICATOR );
			} 
		}
	}
}
