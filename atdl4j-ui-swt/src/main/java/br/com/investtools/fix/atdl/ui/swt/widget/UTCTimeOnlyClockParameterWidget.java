package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCTimeOnlyT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class UTCTimeOnlyClockParameterWidget implements ParameterWidget<Date> {

	private ParameterT parameter;

	private DateTime clock;

	private Label label;

	private String localMktTz;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		// clock
		DateTime clock = new DateTime(parent, style | SWT.BORDER | SWT.TIME
				| SWT.MEDIUM);
		this.clock = clock;
		clock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// tooltip
		String tooltip = parameter.getTooltip();
		clock.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		UTCTimeOnlyT utcTimeOnly = (UTCTimeOnlyT) parameter;

		if (utcTimeOnly.isSetLocalMktTz())
			localMktTz = utcTimeOnly.getLocalMktTz();

		// init value
		if (utcTimeOnly.isSetInitValue()) {
			Calendar initValue = utcTimeOnly.getInitValue();
			if (localMktTz != null)
				initValue.setTimeZone(getTimeZone(localMktTz));
			clock.setHours(initValue.get(Calendar.HOUR_OF_DAY));
			clock.setMinutes(initValue.get(Calendar.MINUTE));
			clock.setSeconds(initValue.get(Calendar.SECOND));
		}

		return parent;
	}

	@Override
	public Date getValue() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, clock.getHours());
		c.set(Calendar.MINUTE, clock.getMinutes());
		c.set(Calendar.SECOND, clock.getSeconds());
		if (localMktTz != null)
			c.setTimeZone(getTimeZone(localMktTz));
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	@Override
	public void setValue(Date value) {
		Calendar c = Calendar.getInstance();
		c.setTime(value);
		clock.setHours(c.get(Calendar.HOUR_OF_DAY));
		clock.setMinutes(c.get(Calendar.MINUTE));
		clock.setSeconds(c.get(Calendar.SECOND));
	}

	private static TimeZone getTimeZone(String localMktTz) {
		return TimeZone.getTimeZone(localMktTz);
	}

	@Override
	public String getFIXValue() {

		Date date = this.getValue();
		DateFormat fixUTCTimeOnlyFormat = new SimpleDateFormat("HH:mm:ss");
		String value = fixUTCTimeOnlyFormat.format(date);

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
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			throw new XmlException("Unable to parse \"" + value
					+ "\" with format \"HH:mm:ss\"");
		}

	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
		clock.addListener(SWT.Selection, listener);

	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(clock);
		return widgets;
	}

	@Override
	public void addListener(Listener listener) {
		clock.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	@Override
	public void removeListener(Listener listener) {
		clock.removeListener(SWT.Selection, listener);
	}

}
