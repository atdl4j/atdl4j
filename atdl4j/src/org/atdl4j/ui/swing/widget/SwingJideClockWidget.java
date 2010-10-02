package org.atdl4j.ui.swing.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JLabel;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.core.UseT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.ui.ControlHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.atdl4j.ui.swing.SwingListener;

import com.jidesoft.spinner.DateSpinner;

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
public class SwingJideClockWidget
//3/18/2010 Scott Atwell avoid compile error "type parameter org.joda.time.DateTime is not within its bound"		extends AbstractSWTWidget<DateTime>
	extends AbstractSwingWidget<Comparable<DateTime>>
{

	private static final Logger logger = Logger.getLogger( SwingJideClockWidget.class );

	public static boolean showEnabledButton = false;
	public static boolean show24HourClock = true;
	
	public boolean hasLabelOrCheckbox = false;
	private JCheckBox enabledButton;
	
	private JLabel label;
	
	private DateSpinner dateClock;
	private DateSpinner timeClock;
	
	private boolean showMonthYear;
	private boolean showDay;
	private boolean showTime;

	public void createWidget(Container parent)
	{
		// tooltip
		String tooltip = control.getTooltip();		
		
		if ( parameter instanceof UTCTimestampT )
		{
			showMonthYear = true;
			showDay = true;
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
		else if ( parameter == null || parameter instanceof UTCTimeOnlyT )
		{
			showMonthYear = false;
			showDay = false;
			showTime = true;
		}
		
		if ( getAtdl4jOptions() != null && 
			getAtdl4jOptions().isShowEnabledCheckboxOnOptionalClockControl() && 
//		if ( ( Atdl4jConfig.get() != null ) &&
//			  ( Atdl4jConfig.get().isShowEnabledCheckboxOnOptionalClockControl() ) && 
			parameter != null && 
			UseT.OPTIONAL.equals( parameter.getUse() ) )
		{
			hasLabelOrCheckbox = true;
			enabledButton = new JCheckBox();
			if (control.getLabel() != null) {
				enabledButton.setText(control.getLabel());
			}
			enabledButton.setToolTipText("Click to enable optional parameter");
			enabledButton.setSelected(false);
			enabledButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					applyEnabledSetting();					
				}
			});
			parent.add(enabledButton);
		}		
		else if (control.getLabel() != null)
		{
			// add label
			hasLabelOrCheckbox = true;
			label = new JLabel();
			label.setText(control.getLabel());
			if (tooltip != null) label.setToolTipText(tooltip);
			parent.add(label);
		}
		
		// date clock
		if (showMonthYear) {
			dateClock = new DateSpinner(showDay ? "DD/MM/YYYY" : "MM/YYYY");
			if (tooltip != null) dateClock.setToolTipText(tooltip);
			parent.add(dateClock);
		}
		// time clock
		if (showTime) {
			timeClock = new DateSpinner(show24HourClock ? "HH:mm:ss" : "hh:mm:ss");
			if (tooltip != null) timeClock.setToolTipText(tooltip);
			parent.add(timeClock);
		}

		// init value, if applicable
		setAndRenderInitValue( (XMLGregorianCalendar ) ControlHelper.getInitValue( control, getAtdl4jOptions() ), ((ClockT) control).getInitValueMode() );
		
		if ( enabledButton != null )
		{
			applyEnabledSetting();
		}
	}

	public DateTimeZone getLocalMktTz() 
		throws IllegalArgumentException
	{
		// This will throw IllegalArgumentException if ID cannot be resolved
		return DateTimeConverter.convertTimezoneToDateTimeZone( ((ClockT) control).getLocalMktTz() );
	}
	
	public DateTime getControlValueRaw()
	{
		if ((dateClock == null) && (timeClock == null))
		{
			return null; // disabled, no value to use
		}

		DateTime date = null;
		DateTime time = null;
		if (showMonthYear) date = new DateTime(dateClock.getValue());
		if (showTime) time = new DateTime(timeClock.getValue());
		DateTime result = new DateTime(showMonthYear ? date.getYear() : 1970,
									showMonthYear ? date.getMonthOfYear() : 1,
									showDay ? date.getDayOfMonth() : 1, 
									showTime ? time.getHourOfDay() : 0,
									showTime ? time.getMinuteOfHour() : 0,
									showTime ? time.getSecondOfMinute() : 0,
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
		
		if (showMonthYear) dateClock.setValue(tempLocalTzDateTime.toDate());
		if (showTime) timeClock.setValue(tempLocalTzDateTime.toLocalTime());
	}

	private void applyEnabledSetting()
	{
		if ( enabledButton != null )
		{
			if ( ( dateClock != null ) && ( dateClock.isVisible() ) )
			{
				dateClock.setEnabled( enabledButton.isSelected() );
			}

			if ( ( timeClock != null ) && ( timeClock.isVisible() ) )
			{
				timeClock.setEnabled( enabledButton.isSelected() );
			}
		}
	}
	
	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<Component>();
		if (enabledButton != null) widgets.add(enabledButton);
		if (label != null) widgets.add(label);
		if (showMonthYear) widgets.add(dateClock);
		if (showTime) widgets.add(timeClock);
		return widgets;
	}
	
	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<Component>();
		if (showMonthYear) widgets.add(dateClock);
		if (showTime) widgets.add(timeClock);
		return widgets;
	}
	
	public void addListener(SwingListener listener) {
		if (showMonthYear) dateClock.addChangeListener(listener);
		if (showTime) timeClock.addChangeListener(listener);
	}

	public void removeListener(SwingListener listener) {
		if (showMonthYear) dateClock.removeChangeListener(listener);
		if (showTime) timeClock.removeChangeListener(listener);
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
	 * @see org.atdl4j.ui.ControlUI#reinit()
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
	 * @see org.atdl4j.ui.impl.AbstractControlUI#setFIXValue(java.lang.String)
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
		//	Integer tempClockPastTimeSetFIXValueRule = Atdl4jConfig().getClockPastTimeSetFIXValueRule( getControl() );
			logger.debug( "Control: " + getControl().getID() + " tempClockPastTimeSetFIXValueRule: " + tempClockPastTimeSetFIXValueRule );
			
			if ( getAtdl4jOptions().CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				// -- keep as-is --
				logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS rule -- Retaining: " + tempFIXValueTime );
			}
			else if ( getAtdl4jOptions().CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT rule -- Setting: " + tempCurrentTime + " ( vs. " + tempFIXValueTime + ")" );
				setValue( tempCurrentTime );
			} 
			else if ( getAtdl4jOptions().CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL rule -- Setting control to 'null value' ( vs. " + tempFIXValueTime + ")" );
				setValueAsString( Atdl4jConstants.VALUE_NULL_INDICATOR );
			} 
		}
	}
}