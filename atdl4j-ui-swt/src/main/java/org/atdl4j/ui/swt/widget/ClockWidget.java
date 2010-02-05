package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.LocalMktDateT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.UTCDateOnlyT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimestampT;
import org.atdl4j.atdl.layout.ClockT;
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
import org.atdl4j.atdl.core.UseT;

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
public class ClockWidget extends AbstractSWTWidget<DateTime> {

	private static final Logger logger = Logger.getLogger(ClockWidget.class);

	private org.eclipse.swt.widgets.DateTime dateClock;
	private org.eclipse.swt.widgets.DateTime timeClock;

	// 1/20/2010 Scott Atwell added
	// TODO: move this to app config
	public static boolean showEnabledButton = false;

	// TODO 1/20/2010 Scott Atwell added
	// -- enabledButton (for optional parameters) and label are mutually
	// exclusive (use the one that is not null) --
	private Button enabledButton;
	private Label label;

	private boolean showMonthYear;
	private boolean showDay;
	private boolean showTime;

	public ClockWidget(ClockT control, ParameterT parameter)
			throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {

		if (parameter instanceof UTCTimestampT) {
			showMonthYear = true;
			showDay = true;
			showTime = true;
		} else if (parameter instanceof UTCDateOnlyT
				|| parameter instanceof LocalMktDateT) {
			showMonthYear = true;
			showDay = true;
			showTime = false;
		} else if (parameter instanceof MonthYearT) {
			showMonthYear = true;
			showDay = false;
			showTime = false;
		} else if (parameter == null || parameter instanceof UTCTimeOnlyT) {
			showMonthYear = false;
			showDay = false;
			showTime = true;
		}

		// 1/20/2010 Scott Atwell added to enable/disable "optional" Clock
		// display BELOW
		if (showEnabledButton && parameter != null
				&& UseT.OPTIONAL.equals(parameter.getUse())) {
			enabledButton = new Button(parent, SWT.CHECK);
			if (control.getLabel() != null) {
				enabledButton.setText(control.getLabel());
			}
			enabledButton
					.setToolTipText("Check to enable and specify or uncheck to disable this optional parameter value");
			enabledButton.setSelection(false); // TODO disabled by default ????
			enabledButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					applyEnabledSetting();
				}
			});
		} else // "required" -- use standard label without preceding checkbox
		{
			// label
			label = new Label(parent, SWT.NONE);
			if (control.getLabel() != null)
				label.setText(control.getLabel());
		}

		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.horizontalSpacing = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginLeft = gridLayout.marginRight = 0;
		gridLayout.marginTop = gridLayout.marginBottom = 0;
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		c.setLayout(gridLayout);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		// date clock
		if (showMonthYear) {
			dateClock = new org.eclipse.swt.widgets.DateTime(c, style
					| SWT.BORDER | SWT.DATE
					| (showDay ? SWT.MEDIUM : SWT.SHORT));
			dateClock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false));

		}
		// time clock
		if (showTime) {
			timeClock = new org.eclipse.swt.widgets.DateTime(c, style
					| SWT.BORDER | SWT.TIME | SWT.MEDIUM);
			timeClock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false));
		}

		// tooltip
		String tooltip = getTooltip();
		if (tooltip != null) {
			if (showMonthYear) dateClock.setToolTipText(tooltip);
			if (showTime) timeClock.setToolTipText(tooltip);
			if (label != null) label.setToolTipText(tooltip);
			if (enabledButton != null && tooltip != null) enabledButton.setToolTipText(tooltip);
		}

		// init value
		XMLGregorianCalendar initValue = ((ClockT) control).getInitValue();
		if (initValue != null) {
// 2/1/2010 Scott Atwell			DateTime now = new DateTime(DateTimeZone.UTC);
			// -- Note that this will throw IllegalArgumentException if timezone ID specified cannot be resolved --
			DateTimeZone tempLocalMktTz = getLocalMktTz();
			logger.debug( "control.getID(): " + control.getID() + " initValue: " + initValue + " getLocalMktTz(): " + tempLocalMktTz );
			
			// -- localMktTz is required when using/interpreting initValue --
			if ( tempLocalMktTz == null )
			{
				throw new IllegalArgumentException( "localMktTz is required when initValue (" + initValue + ") is specified. (Control.ID: " + control.getID() + ")"); 
			}
			
			DateTime now = new DateTime( tempLocalMktTz );

			DateTime init = new DateTime(
					(showMonthYear && initValue.getYear() != DatatypeConstants.FIELD_UNDEFINED) ? initValue
							.getYear()
							: now.getYear(),
					(showMonthYear && initValue.getMonth() != DatatypeConstants.FIELD_UNDEFINED) ? initValue
							.getMonth()
							: now.getMonthOfYear(),
					(showMonthYear && initValue.getDay() != DatatypeConstants.FIELD_UNDEFINED) ? initValue
							.getDay()
							: now.getDayOfMonth(),
					(showMonthYear && initValue.getHour() != DatatypeConstants.FIELD_UNDEFINED) ? initValue
							.getHour()
							: 0,
					(showMonthYear && initValue.getMinute() != DatatypeConstants.FIELD_UNDEFINED) ? initValue
							.getMinute()
							: 0,
					(showMonthYear && initValue.getSecond() != DatatypeConstants.FIELD_UNDEFINED) ? initValue
							.getSecond()
// 2/1/2010 Scott Atwell							: 0, 0, DateTimeZone.UTC);
							: 0, 0, tempLocalMktTz );
// 2/3/2010 Scott Atwell -- we need to make sure that the value is rendered on the display in local timezone  			setValue(init);
			setValue(init.withZone( DateTimeZone.getDefault() ));
		}

		// TODO 1/20/2010 Scott Atwell
		if (enabledButton != null) {
			applyEnabledSetting();
		}

		return parent;
	}

/*** 2/3/2010 Scott Atwell replaced getTimeZone() with getLocalMktTz()	
	// TODO: Move timezone attribute to Control and modify this method
	public org.joda.time.DateTimeZone getTimeZone() {
		if (parameter != null) {
			// List<String> timezones =
			// Arrays.asList(TimeZone.getAvailableIDs());
			if (parameter instanceof UTCTimestampT
					&& ((UTCTimestampT) parameter).getLocalMktTz() != null) {
				try {
					return org.joda.time.DateTimeZone
							.forID(((UTCTimestampT) parameter).getLocalMktTz()
									.value());
				} catch (IllegalArgumentException e) {
				}
			}
			if (parameter instanceof MonthYearT
					|| parameter instanceof UTCDateOnlyT
					|| parameter instanceof LocalMktDateT) {
				return org.joda.time.DateTimeZone.UTC;
			}
		}
		return DateTimeZone.getDefault();
	}
***/
	// TODO: Move timezone attribute to Control and modify this method
	public org.joda.time.DateTimeZone getLocalMktTz()
		throws IllegalArgumentException
	{
		if (parameter != null) 
		{
			// List<String> timezones =
			// Arrays.asList(TimeZone.getAvailableIDs());
			if (parameter instanceof UTCTimestampT
// 2/3/2010 the Control has its own localMktTz for initValue					&& ((UTCTimestampT) parameter).getLocalMktTz() != null) 
					&& control != null && control instanceof ClockT
					&& ((ClockT) control).getLocalMktTz() != null) 
			{
//				try {
				// This will throw IllegalArgumentException if ID cannot be resolved
				return org.joda.time.DateTimeZone
// 2/3/2010 use Control's own localMktTz						.forID(((UTCTimestampT) parameter).getLocalMktTz()
						.forID(((ClockT) control).getLocalMktTz()
								.value());
//				} catch (IllegalArgumentException e) {
//				}
			}
		}
		
		return null;
	}
	
	
	public DateTime getControlValue() {
		// TODO 1/20/2010 Scott Atwell added BELOW
		// -- Return null if control is either not visible or disabled --
		if (((dateClock == null) || (dateClock.isVisible() == false) || (dateClock
				.isEnabled() == false))
				&& ((timeClock == null) || (timeClock.isVisible() == false) || (timeClock
						.isEnabled() == false)))
		// if ( ( enabledButton != null ) && ( enabledButton.getSelection() ==
		// false ) )
		{
			return null; // disabled, no value to use
		}
		// TODO 1/20/2010 Scott Atwell added ABOVE

		DateTime result = new DateTime(showMonthYear ? dateClock.getYear()
				: 1970, showMonthYear ? dateClock.getMonth() + 1 : 1,
				showDay ? dateClock.getDay() : 1, showTime ? timeClock
						.getHours() : 0, showTime ? timeClock.getMinutes() : 0,
// 2/2/2010				showTime ? timeClock.getSeconds() : 0, 0, getTimeZone());
						showTime ? timeClock.getSeconds() : 0, 0, DateTimeZone.getDefault() );
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce and
		// unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and
		// TZTimeOnlyT.
		if (parameter == null || parameter instanceof UTCTimestampT
				|| parameter instanceof UTCTimeOnlyT) {
			result = result.toDateTime(DateTimeZone.UTC);
			logger.debug( "getControlValue() parameter: " + parameter + " result: " + result );
		}
		return result;
	}

	public DateTime getParameterValue() {
		return getControlValue();
	}

	public void setValue(DateTime value) {
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce and
		// unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and
		// TZTimeOnlyT.
		if (parameter == null || parameter instanceof UTCTimestampT
				|| parameter instanceof UTCTimeOnlyT) {
// 2/2/2010			logger.debug( "setValue() parameter: " + parameter + " value: " + value + " getTimeZone(): " + getTimeZone() + " value.toDateTime(getTimeZone()): " + value.toDateTime(getTimeZone()) );
// 2/2/2010			value = value.toDateTime(getTimeZone());
			logger.debug( "setValue() parameter: " + parameter + " value: " + value );
			// -- no need to adjust DateTime --
		}
		if (showMonthYear) {
			dateClock.setMonth(value.getMonthOfYear() - 1);
			dateClock.setYear(value.getYear());
		}
		if (showDay) {
			dateClock.setDay(value.getDayOfMonth());
		}
		if (showTime) {
			timeClock.setHours(value.getHourOfDay());
			timeClock.setMinutes(value.getMinuteOfHour());
			timeClock.setSeconds(value.getSecondOfMinute());
		}
	}



	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		// TODO 1/20/2010 Scott Atwell added
		if (enabledButton != null) {
			widgets.add(enabledButton);
		}

		// TODO 1/20/2010 Scott Atwell widgets.add(label);
		if (label != null) {
			widgets.add(label);
		}

		if (showMonthYear) {
			widgets.add(dateClock);
		}
		if (showTime) {
			widgets.add(timeClock);
		}
		return widgets;
	}

	// 1/20/2010 Scott Atwell added
	private void applyEnabledSetting() {
		if (enabledButton != null) {
			if ((dateClock != null) && (dateClock.isVisible())) {
				dateClock.setEnabled(enabledButton.getSelection());
			}

			if ((timeClock != null) && (timeClock.isVisible())) {
				timeClock.setEnabled(enabledButton.getSelection());
			}
		}
	}
	
    public void addListener(Listener listener)
    {
	if (showMonthYear)
	{
	    dateClock.addListener(SWT.Selection, listener);
	}
	if (showTime)
	{
	    timeClock.addListener(SWT.Selection, listener);
	}
    }

    public void removeListener(Listener listener)
    {
	if (showMonthYear)
	{
	    dateClock.removeListener(SWT.Selection, listener);
	}
	if (showTime)
	{
	    timeClock.removeListener(SWT.Selection, listener);
	}
    }
}
