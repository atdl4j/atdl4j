package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.atdl4j.atdl.core.LocalMktTimeT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.UTCDateT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimeStampT;
import org.atdl4j.atdl.layout.ClockT;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

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
public class ClockWidget extends AbstractSWTWidget<org.joda.time.DateTime> {

	private org.eclipse.swt.widgets.DateTime dateClock;
	private org.eclipse.swt.widgets.DateTime timeClock;
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

		if (parameter instanceof UTCTimeStampT) {
			showMonthYear = true;
			showDay = true;
			showTime = true;
		} else if (parameter instanceof UTCDateT
				|| parameter instanceof LocalMktTimeT) {
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

		// label
		label = new Label(parent, SWT.NONE);
		label.setText(control.getLabel());

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
		String tooltip = control.getTooltip();
		if (showMonthYear)
			dateClock.setToolTipText(tooltip);
		if (showTime)
			timeClock.setToolTipText(tooltip);
		label.setToolTipText(tooltip);

		// init value
		if (((ClockT) control).getInitValue() != null) {
			org.joda.time.DateTime now = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC);

			XMLGregorianCalendar initValue = ((ClockT) control).getInitValue();
			org.joda.time.DateTime init = new org.joda.time.DateTime(
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
							: 0, 0, org.joda.time.DateTimeZone.UTC);
			setValue(init);
		}
		return parent;
	}

	// TODO: Move timezone attribute to Control and modify this method
	public org.joda.time.DateTimeZone getTimeZone() {
		if (parameter != null) {
			// List<String> timezones =
			// Arrays.asList(TimeZone.getAvailableIDs());
			if (parameter instanceof UTCTimeStampT
					&& ((UTCTimeStampT) parameter).getLocalMktTz() != null) {
				try {
					return org.joda.time.DateTimeZone.forID(((UTCTimeStampT) parameter)
							.getLocalMktTz().value());
				} catch (IllegalArgumentException e) {
				}
			}
			if (parameter instanceof LocalMktTimeT
					&& ((LocalMktTimeT) parameter).getLocalMktTz() != null) {
				try {
					return org.joda.time.DateTimeZone.forID(((LocalMktTimeT) parameter)
							.getLocalMktTz().value());
				} catch (IllegalArgumentException e) {
				}
			}
			if (parameter instanceof MonthYearT
					|| parameter instanceof UTCDateT) {
				return org.joda.time.DateTimeZone.UTC;
			}
		}
		return org.joda.time.DateTimeZone.getDefault();
	}

	public org.joda.time.DateTime getControlValue() {
		org.joda.time.DateTime result = new org.joda.time.DateTime(showMonthYear ? dateClock.getYear()
				: 1970, showMonthYear ? dateClock.getMonth() + 1 : 1,
				showDay ? dateClock.getDay() : 1, showTime ? timeClock
						.getHours() : 0, showTime ? timeClock.getMinutes() : 0,
				showTime ? timeClock.getSeconds() : 0, 0, getTimeZone());
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce and
		// unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and
		// TZTimeOnlyT.
		if (parameter == null || parameter instanceof UTCTimeStampT
				|| parameter instanceof UTCTimeOnlyT) {
			result = result.toDateTime(org.joda.time.DateTimeZone.UTC);
		}
		return result;
	}

	public org.joda.time.DateTime getParameterValue() {
		return getControlValue();
	}

	public void setValue(org.joda.time.DateTime value) {
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce and
		// unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and
		// TZTimeOnlyT.
		if (parameter == null || parameter instanceof UTCTimeStampT
				|| parameter instanceof UTCTimeOnlyT) {
			value = value.toDateTime(getTimeZone());
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

	public void generateStateRuleListener(Listener listener) {
		if (showMonthYear) {
			dateClock.addListener(SWT.Selection, listener);
		}
		if (showTime) {
			timeClock.addListener(SWT.Selection, listener);
		}
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		if (showMonthYear) {
			widgets.add(dateClock);
		}
		if (showTime) {
			widgets.add(timeClock);
		}
		return widgets;
	}

	public void addListener(Listener listener) {
		ParameterListenerWrapper wrapper = new ParameterListenerWrapper(this,
				listener);
		if (showMonthYear) {
			dateClock.addListener(SWT.Selection, wrapper);
		}
		if (showTime) {
			timeClock.addListener(SWT.Selection, wrapper);
		}
	}

	public void removeListener(Listener listener) {
		if (showMonthYear) {
			dateClock.removeListener(SWT.Selection, listener);
		}
		if (showTime) {
			timeClock.removeListener(SWT.Selection, listener);
		}
	}
}
