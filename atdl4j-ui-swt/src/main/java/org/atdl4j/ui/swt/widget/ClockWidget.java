package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.atdl4j.atdl.core.LocalMktTimeT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.UTCDateT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimeStampT;
import org.atdl4j.atdl.core.UseT;
import org.atdl4j.atdl.layout.ClockT;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.swt.impl.SWTStrategyUI;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
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


/**
 * Clock widget which will display differently depending on the parameter type used.
 * 
 * UTCTimestampT and UTCTimeOnlyT display in the user's local time,
 * but will be converted to/from UTC when outputting/reading FIX.
 * 
 * LocalMktTimeT, TZTimestampT, and TZTimeOnlyT display in the user's local time,
 * effectively 'what-you-see-is-what-you-get' in terms of the FIX output.
 * 
 * MonthYearT are UTCDateOnlyT are displayed in UTC (GMT) time, effectively
 * 'what-you-see-is-what-you-get' in terms of the FIX output.
 *  
 * Note that users should not use a UTCDateOnlyT clock and a UTCDTimeOnlyT clock side-by-side, 
 * as this cannot account for time conversion across midnight.
 * 
 * @author john.shields
 */
public class ClockWidget extends AbstractSWTWidget<DateTime> {

	private org.eclipse.swt.widgets.DateTime dateClock;
	private org.eclipse.swt.widgets.DateTime timeClock;

// 1/20/2010 Scott Atwell added
	public static boolean disableCheckBoxForOptionalParameterHandling = false;

//TODO 1/20/2010 Scott Atwell added
	// -- enabledButton (for optional parameters) and label are mutually exclusive (use the one that is not null) --
	private Button enabledButton;
	private Label label;
	
	private boolean showMonthYear;
	private boolean showDay;
	private boolean showTime;

	
	public ClockWidget(ClockT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {
		
		if (parameter instanceof UTCTimeStampT)
		{
			showMonthYear = true;
			showDay = true;
			showTime = true;
		}
		else if (parameter instanceof UTCDateT ||
				 parameter instanceof LocalMktTimeT)
		{
			showMonthYear = true;
			showDay = true;
			showTime = false;
		}
		else if (parameter instanceof MonthYearT)
		{
			showMonthYear = true;
			showDay = false;
			showTime = false;
		}
		else if (parameter == null || 
				 parameter instanceof UTCTimeOnlyT)
		{
			showMonthYear = false;
			showDay = false;
			showTime = true;
		}
				
// 1/20/2010 Scott Atwell added to enable/disable "optional" Clock display BELOW
		if ( ( disableCheckBoxForOptionalParameterHandling == false ) &&
			  ( ( parameter != null) && ( UseT.OPTIONAL.equals( parameter.getUse() ) ) ) )
		{
			enabledButton = new Button(parent, SWT.CHECK);		
//			enabledButton.setText("");
			if ( control.getLabel() != null )
			{
				enabledButton.setText(control.getLabel());
			}
			
			enabledButton.setToolTipText( "Check to enable and specify or uncheck to disable this optional parameter value" );

			enabledButton.setSelection( false );  //TODO disabled by default ????
			
			enabledButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					applyEnabledSetting();
				}
			});
		}
		else  // "required" -- use standard label without preceding checkbox
		{
			// label
			label = new Label(parent, SWT.NONE);
	// 1/20/2010 Scott Atwell avoid NPE as label is not required on Control		label.setText(control.getLabel());
			if ( control.getLabel() != null )
			{
				label.setText(control.getLabel());
			}
		}
// 1/20/2010 Scott Atwell added to enable/disable "optional" Clock display BELOW
/****		
		// label
		label = new Label(parent, SWT.NONE);
// 1/20/2010 Scott Atwell avoid NPE as label is not required on Control		label.setText(control.getLabel());
		if ( control.getLabel() != null )
		{
			label.setText(control.getLabel());
		}
***/
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
		if (showMonthYear)
		{
			dateClock = new org.eclipse.swt.widgets.DateTime(c, style | SWT.BORDER | SWT.DATE | (showDay ? SWT.MEDIUM : SWT.SHORT));
			dateClock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		}
		// time clock
		if (showTime)
		{
			timeClock = new org.eclipse.swt.widgets.DateTime(c, style | SWT.BORDER | SWT.TIME | SWT.MEDIUM);
			timeClock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		}

		// tooltip
		String tooltip = control.getTooltip();
		if (showMonthYear) dateClock.setToolTipText(tooltip);
		if (showTime) timeClock.setToolTipText(tooltip);
// 1/20/2010 Scott Atwell		label.setToolTipText(tooltip);
		if ( label != null )		label.setToolTipText(tooltip);
		if ( ( enabledButton != null ) && ( tooltip != null ) )	enabledButton.setToolTipText( tooltip );
		
		
		// init value
		if (((ClockT)control).getInitValue() != null) {
			DateTime now = new DateTime(DateTimeZone.UTC);
			
			XMLGregorianCalendar initValue = ((ClockT)control).getInitValue();
			DateTime init = new DateTime(
					(showMonthYear && initValue.getYear() != DatatypeConstants.FIELD_UNDEFINED) ? initValue.getYear() : now.getYear(),
					(showMonthYear && initValue.getMonth() != DatatypeConstants.FIELD_UNDEFINED) ? initValue.getMonth() : now.getMonthOfYear(),
					(showMonthYear && initValue.getDay() != DatatypeConstants.FIELD_UNDEFINED) ? initValue.getDay() : now.getDayOfMonth(),
					(showMonthYear && initValue.getHour() != DatatypeConstants.FIELD_UNDEFINED) ? initValue.getHour() : 0,
					(showMonthYear && initValue.getMinute() != DatatypeConstants.FIELD_UNDEFINED) ? initValue.getMinute() : 0,				
					(showMonthYear && initValue.getSecond() != DatatypeConstants.FIELD_UNDEFINED) ? initValue.getSecond() : 0,
					0,
					DateTimeZone.UTC);
			setValue(init);
		}
		
//TODO 1/20/2010 Scott Atwell
		if ( enabledButton != null )
		{
			applyEnabledSetting();
		}
		
		return parent;
	}

	// TODO: Move timezone attribute to Control and modify this method
	public DateTimeZone getTimeZone()
	{
		if (parameter != null)
		{			
			//List<String> timezones = Arrays.asList(TimeZone.getAvailableIDs());
			if (parameter instanceof UTCTimeStampT &&
				((UTCTimeStampT)parameter).getLocalMktTz() != null)
			{
				try {
					return DateTimeZone.forID(((UTCTimeStampT)parameter).getLocalMktTz());
				} catch (IllegalArgumentException e) { }
			}
			if (parameter instanceof LocalMktTimeT && 
				((LocalMktTimeT)parameter).getLocalMktTz() != null)
			{
				try {
					return DateTimeZone.forID(((LocalMktTimeT)parameter).getLocalMktTz());
				} catch (IllegalArgumentException e) { }
			}
			if (parameter instanceof MonthYearT ||
				parameter instanceof UTCDateT)
			{
				return DateTimeZone.UTC;
			}
		}
		return DateTimeZone.getDefault();
	}
	
	public DateTime getControlValue() {
//TODO 1/20/2010 Scott Atwell added BELOW
		// -- Return null if control is either not visible or disabled --
		if ( ( ( dateClock == null ) || ( dateClock.isVisible() == false ) || ( dateClock.isEnabled() == false ) ) &&
			  ( ( timeClock == null ) || ( timeClock.isVisible() == false ) || ( timeClock.isEnabled() == false ) ) )
//		if ( ( enabledButton != null ) && ( enabledButton.getSelection() == false ) )
		{
			return null;  // disabled, no value to use
		}
//TODO 1/20/2010 Scott Atwell added ABOVE		
		
		DateTime result = new DateTime(
				showMonthYear? dateClock.getYear() : 1970,
				showMonthYear? dateClock.getMonth()+1 : 1,
				showDay? dateClock.getDay() : 1,
				showTime? timeClock.getHours() : 0,
				showTime? timeClock.getMinutes() : 0,				
				showTime? timeClock.getSeconds() : 0,
				0,
				getTimeZone());
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce and unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
		if (parameter == null ||
			parameter instanceof UTCTimeStampT ||
			parameter instanceof UTCTimeOnlyT)
		{
			result = result.toDateTime(DateTimeZone.UTC);
		}
		return result;		
	}

	public DateTime getParameterValue() {
		return getControlValue();
	}
		
	public void setValue(DateTime value) {
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce and unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
		if (parameter == null ||
			parameter instanceof UTCTimeStampT ||
			parameter instanceof UTCTimeOnlyT)
		{
			value = value.toDateTime(getTimeZone());
		}
		if (showMonthYear) {
			dateClock.setMonth(value.getMonthOfYear()-1);
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
//TODO 1/20/2010 Scott Atwell added
		if ( enabledButton != null )
		{
			widgets.add(enabledButton);
		}
		
// TODO 1/20/2010 Scott Atwell		widgets.add(label);
		if ( label != null )
		{
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
	
//TODO 1/20/2010 Scott Atwell added	
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
}
