package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCTimeStampT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class UTCTimeStampClockParameterWidget implements ParameterWidget<Date> {

	private ParameterT parameter;

	private DateTime clock;

	private String localMktTz;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		//TODO two separate controls will be needed: one for date, one for time 
		
		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));

		// clock
		DateTime clock = new DateTime(parent, style | SWT.BORDER | SWT.TIME
				| SWT.LONG);
		clock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		// tooltip
		String tooltip = parameter.getTooltip();
		clock.setToolTipText(tooltip);
		l.setToolTipText(tooltip);
		
		UTCTimeStampT utcTimeStamp = (UTCTimeStampT) parameter;

		if (utcTimeStamp.isSetLocalMktTz())
			localMktTz = utcTimeStamp.getLocalMktTz();
		
		// init value
		if (utcTimeStamp.isSetInitValue()){ 
			Calendar initValue = utcTimeStamp.getInitValue();
			if (localMktTz != null)
				initValue.setTimeZone(getTimeZone(localMktTz));
			clock.setYear(initValue.get(Calendar.YEAR));
			clock.setMonth(initValue.get(Calendar.MONTH));
			clock.setDay(initValue.get(Calendar.DAY_OF_MONTH));
			clock.setHours(initValue.get(Calendar.HOUR));
			clock.setMinutes(initValue.get(Calendar.MINUTE));
			clock.setSeconds(initValue.get(Calendar.SECOND));
		}
		
		this.clock = clock;

		return parent;
	}

	public Date getValue() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, clock.getYear());
		c.set(Calendar.MONTH, clock.getMonth());
		c.set(Calendar.DAY_OF_MONTH, clock.getDay());
		c.set(Calendar.HOUR_OF_DAY, clock.getHours());
		c.set(Calendar.MINUTE, clock.getMinutes());
		c.set(Calendar.SECOND, clock.getSeconds());
		if (localMktTz != null)
			c.setTimeZone(getTimeZone(localMktTz));
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	private static TimeZone getTimeZone(String localMktTz) {
		return TimeZone.getTimeZone(localMktTz);
	}

	@Override
	public String getFIXValue() {

		Date date = this.getValue();
		DateFormat fixUTCTimeStampFormat = new SimpleDateFormat(
				"yyyyMMdd-hh:mm:ss");
		String value = fixUTCTimeStampFormat.format(date);

		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ value;
		} else {

			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public Date convertValue(String value) throws XmlException {
		// TODO reset timezone here?
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hh:mm:ss");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			throw new XmlException( "Unable to parse \"" + value + "\" with format \"yyyyMMdd-hh:mm:ss\"");
		}

	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

}
