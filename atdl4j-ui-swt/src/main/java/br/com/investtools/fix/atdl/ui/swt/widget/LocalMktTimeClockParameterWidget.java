package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.LocalMktTimeT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class LocalMktTimeClockParameterWidget implements ParameterWidget<Date> {

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
		clock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.clock = clock;

		// tooltip
		String tooltip = parameter.getTooltip();
		clock.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		LocalMktTimeT localMktTime = (LocalMktTimeT) parameter;

		if (localMktTime.isSetLocalMktTz())
			localMktTz = localMktTime.getLocalMktTz();

		return parent;
	}

	@Override
	public Date getValue() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, clock.getYear());
		c.set(Calendar.MONTH, clock.getMonth());
		c.set(Calendar.DAY_OF_MONTH, clock.getDay());
		c.set(Calendar.HOUR_OF_DAY, clock.getHours());
		c.set(Calendar.MINUTE, clock.getMinutes());
		c.set(Calendar.SECOND, clock.getSeconds());
		c.clear(Calendar.MILLISECOND);
		if (localMktTz != null)
			c.setTimeZone(TimeZone.getTimeZone(localMktTz));
		return c.getTime();
	}

	@Override
	public void setValue(Date value) {
		Calendar c = Calendar.getInstance();
		c.setTime(value);
		clock.setDay(c.get(Calendar.DAY_OF_MONTH));
		clock.setMonth(c.get(Calendar.MONTH));
		clock.setYear(c.get(Calendar.YEAR));
		clock.setHours(c.get(Calendar.HOUR_OF_DAY));
		clock.setMinutes(c.get(Calendar.MINUTE));
		clock.setSeconds(c.get(Calendar.SECOND));
	}

	@Override
	public String getFIXValue() {

		Date date = this.getValue();
		DateFormat fixLocalMktTimeFormat = new SimpleDateFormat("yyyyMMdd");
		String value = fixLocalMktTimeFormat.format(date);

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
	public Date convertValue(String value) {
		// TODO reset timezone here?
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			// exception
		}
		return new Date();

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

}
