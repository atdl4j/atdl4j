package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.atdl4j.config.Atdl4jOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//3/18/2010 Scott Atwell avoid compile error "type parameter org.joda.time.DateTime is not within its bound"		extends AbstractSwingWidget<DateTime>
	extends AbstractSwingWidget<Comparable<Instant>>
{

	private static final Logger logger = LoggerFactory.getLogger( SwingJideClockWidget.class );

	public static boolean showEnabledButton = false;
	public static boolean show24HourClock = true;
	
	// model attributes
	/**
	 * true is a value was set either by user input or programmatically, other than a processReinit
	 */
	private boolean valueFilledIn;
	/**
	 * Default is true, but can be disabled after a call to setEnabled 
	 */
	private boolean enabled = true;
	
	
	public boolean hasLabelOrCheckbox = false;
	private JCheckBox enabledButton;
	
	private JLabel label;
	
	private DateSpinner dateClock;
	private DateSpinner timeClock;
	
	private boolean showMonthYear;
	private boolean showDay;
	private boolean showTime;
	private boolean useNowAsDate = false;


	public ZoneId getLocalMktTz() 
		throws IllegalArgumentException
	{
		// This will throw IllegalArgumentException if ID cannot be resolved
		return DateTimeConverter.convertTimezoneToZoneId( ((ClockT) control).getLocalMktTz() );
	}
	
	public Instant getControlValueRaw()
	{
		if ( ( (dateClock == null)
			&& ( (showMonthYear) || (showDay) ) )
		|| ( (timeClock == null)
			&& (showTime) ) )
		{
			return null; // disabled, no value to use
		}
		if (!valueFilledIn)
		{
		  return null;
		}

		ZonedDateTime now = null;
		ZonedDateTime date = null;
		ZonedDateTime time = null;
		if (useNowAsDate)
		{
			now = ZonedDateTime.now( ZoneId.systemDefault() );
		}
		else if ( (showMonthYear) || (showDay) )
		{
			date = ZonedDateTime.ofInstant( ((Date) dateClock.getValue()).toInstant(), ZoneId.systemDefault());
		}
		if (showTime)
		{
			time = ZonedDateTime.ofInstant( ((Date) timeClock.getValue()).toInstant(), ZoneId.systemDefault());
		}
		ZonedDateTime result = ZonedDateTime.of( useNowAsDate ? now.getYear() : showMonthYear ? date.getYear() : 1970
		                                       , useNowAsDate ? now.getMonthValue() : showMonthYear ? date.getMonthValue() : 1
		                                       , useNowAsDate ? now.getDayOfMonth() : showDay ? date.getDayOfMonth() : 1
		                                       , showTime ? time.getHour() : 0
		                                       , showTime ? time.getMinute() : 0
		                                       , showTime ? time.getSecond() : 0
		                                       , 0
		                                       , ZoneId.systemDefault()
		                                       );

		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce an unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
		if ( parameter == null || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT )
		{
			result = result.withZoneSameInstant( ZoneId.of("UTC") );
			logger.debug( "getControlValue() parameter: {} result: {}", parameter, result );
		}
		return result.toInstant();
	}
	
	public void setValue(Comparable<Instant> value)
	{
		// Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
		// Performing UTCDateT and MonthYearT coversion could produce an unexpected result.
		// No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
		if ( parameter == null || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT )
		{
			logger.debug( "setValue() parameter: {} value: {}", parameter, value );
			// -- no need to adjust DateTime --
		}
		
		// -- Force control to display time portion in local
		ZonedDateTime tempLocalTzDateTime = ZonedDateTime.ofInstant((Instant)value, ZoneId.systemDefault());
		
		if (showMonthYear) dateClock.setValue( Date.from( tempLocalTzDateTime.toInstant() ) );
		if (showTime) timeClock.setValue( Date.from( tempLocalTzDateTime.toInstant() ) );
		valueFilledIn = true;
		updateFromModel();
	}

	public List<Component> getComponents() {
		List<Component> widgets = new ArrayList<>();
		if (enabledButton != null) widgets.add(enabledButton);
		if (label != null) widgets.add(label);
		if (showMonthYear) widgets.add(dateClock);
		if (showTime) widgets.add(timeClock);
		return widgets;
	}
	
	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<>();
		if (showMonthYear) widgets.add(dateClock);
		if (showTime) widgets.add(timeClock);
		return widgets;
	}
	
	public void addListener(final SwingListener listener) {
		if (showMonthYear) dateClock.addChangeListener(listener);
		if (showTime) timeClock.addChangeListener(listener);
		if (enabledButton != null) enabledButton.addActionListener(listener);
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
			ZoneId tempLocalMktTz = getLocalMktTz();
			logger.debug( "control.getID(): {} aValue: {} getLocalMktTz(): {}", control.getID(), aValue, tempLocalMktTz );

			// -- localMktTz is required when using/interpreting aValue --
			if ( tempLocalMktTz == null )
			{
				throw new IllegalArgumentException( "localMktTz is required when aValue (" + aValue + ") is specified. (Control.ID: "
						+ control.getID() + ")" );
			}

			ZonedDateTime tempNow = ZonedDateTime.now( tempLocalMktTz );

			ZonedDateTime tempInit = ZonedDateTime.of( 
					( showMonthYear && aValue.getYear() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getYear() : tempNow.getYear(), 
					( showMonthYear && aValue.getMonth() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getMonth() : tempNow.getMonthValue(), 
					( showMonthYear && aValue.getDay() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getDay() : tempNow.getDayOfMonth(), 
					( showTime && aValue.getHour() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getHour() : 0,
					( showTime && aValue.getMinute() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getMinute() : 0,
					( showTime && aValue.getSecond() != DatatypeConstants.FIELD_UNDEFINED ) ? aValue.getSecond(): 0, 
					0,
					tempLocalMktTz );
			
			if ( ( aInitValueMode == Atdl4jConstants.CLOCK_INIT_VALUE_MODE_USE_CURRENT_TIME_IF_LATER ) &&
				  ( tempNow.isAfter( tempInit ) ) )
			{
				// -- Use current time --
				tempInit = tempNow;
			}
			
			// -- Make sure that the value is rendered on the display in local timezone --
			setValue( tempInit.withZoneSameInstant( ZoneId.systemDefault() ).toInstant() );
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
			setValue( Instant.now() );
			valueFilledIn = ( enabledButton == null ); // the editor requires a value but until the
			// enabledButton is checked, considere that no value is filled 
			updateFromModel();
		}
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// ?? adjust the visual appearance of the control ??
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.impl.AbstractControlUI#setFIXValue(java.lang.String)
	 */
	@Override
	public void setFIXValue(String aFIXValue)
	{
		super.setFIXValue( aFIXValue );
		
		Instant tempFIXValueTime = getControlValueRaw();		
		Instant tempCurrentTime = Instant.now();
		
		// -- Check to see if the time set is < current time --
		if ( tempCurrentTime.isAfter( tempFIXValueTime ) )
		{
			logger.debug( "setFIXValue({}) resulted in time < present ({} < {})", aFIXValue, tempFIXValueTime, tempCurrentTime );
		
			Integer tempClockPastTimeSetFIXValueRule = getAtdl4jOptions().getClockPastTimeSetFIXValueRule( getControl() );
			logger.debug( "Control: {} tempClockPastTimeSetFIXValueRule: {}", getControl().getID(), tempClockPastTimeSetFIXValueRule );
			
			if ( Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				// -- keep as-is --
				logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS rule -- Retaining: {}", tempFIXValueTime );
			}
			else if ( Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT rule -- Setting: {} ( vs. {})", tempCurrentTime, tempFIXValueTime );
				setValue( tempCurrentTime );
			} 
			else if ( Atdl4jOptions.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL.equals( tempClockPastTimeSetFIXValueRule ) )
			{
				logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL rule -- Setting control to 'null value' ( vs. {})", tempFIXValueTime );
				setValueAsString( Atdl4jConstants.VALUE_NULL_INDICATOR );
			} 
		}
	}
	
	
	@Override
	protected List< ? extends Component> createBrickComponents() {
	  
	  List<Component> components = new ArrayList<>();
	  
	  // tooltip
      String tooltip = control.getTooltip();      
      
      if ( parameter instanceof UTCTimestampT || parameter instanceof TZTimestampT )
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
      
      if ( getAtdl4jOptions() != null && 
          getAtdl4jOptions().isShowEnabledCheckboxOnOptionalClockControl() && 
          parameter != null && 
          UseT.OPTIONAL.equals( parameter.getUse() ) )
      {
          hasLabelOrCheckbox = true;
          enabledButton = new JCheckBox();
          enabledButton.setName(getName()+"/enablebutton");
          if (control.getLabel() != null) {
              enabledButton.setText(control.getLabel());
          }
          enabledButton.setToolTipText("Click to enable optional parameter");
          enabledButton.setSelected(false);
          enabledButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                updateFromView();                  
              }
          });
          components.add(enabledButton);
      }       
      else if (control.getLabel() != null)
      {
          // add label
          hasLabelOrCheckbox = true;
          label = new JLabel();
          label.setName(getName()+"/label");
          label.setText(control.getLabel());
          if (tooltip != null) label.setToolTipText(tooltip);
          components.add(label);
      }
      
      // date clock
      if (showMonthYear) {
          dateClock = new DateSpinner(showDay ? "dd.MM.yyyy" : "MM.yyyy");
          dateClock.setName(getName()+"/dateclock");
          if (tooltip != null) dateClock.setToolTipText(tooltip);
          components.add(dateClock);
      }
      // time clock
      if (showTime) {
          timeClock = new DateSpinner(show24HourClock ? "HH:mm:ss" : "hh:mm:ss");
          timeClock.setName(getName()+"/timeclock");
          if (tooltip != null) timeClock.setToolTipText(tooltip);
          components.add(timeClock);
      }

      // init value, if applicable
      setAndRenderInitValue( (XMLGregorianCalendar ) ControlHelper.getInitValue( control, getAtdl4jOptions() ), ((ClockT) control).getInitValueMode() );
      
      updateFromModel();
      return components;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
	  this.enabled = enabled;
      updateFromModel();
	}
	
	@Override
	public boolean isNullValue() {
	  if (!valueFilledIn)
	  {
	    return true;
	  } else {
	    return super.isNullValue();
	  }
	}
	
	private void updateFromView()
	{
	  if (enabledButton!=null)
	  {
	    valueFilledIn = enabledButton.isSelected();
	  } else {
	    valueFilledIn = true;
	  }
	  if ((timeClock != null) && (timeClock.isVisible())) {
	    timeClock.setEnabled(valueFilledIn && enabled);
	  }
	  if ((dateClock != null) && (dateClock.isVisible())) {
	    dateClock.setEnabled(valueFilledIn && enabled);
	  }
	}
	
	private void updateFromModel()
	{
	  if (enabledButton != null) {
        enabledButton.setSelected(valueFilledIn);
        enabledButton.setEnabled(enabled);
      }	  
	  if ((timeClock != null) && (timeClock.isVisible())) {
	    timeClock.setEnabled(valueFilledIn && enabled);
	  }
	  if ((dateClock != null) && (dateClock.isVisible())) {
	    dateClock.setEnabled(valueFilledIn && enabled);
	  }
	}
}
