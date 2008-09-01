package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class ClockParameterWidget implements ParameterWidget<Date> {

	private ParameterT parameter;

	private DateTime clock;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));

		// clock
		DateTime clock = new DateTime(parent, style | SWT.BORDER | SWT.TIME);
		clock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.clock = clock;

		// tooltip
		String tooltip = parameter.getTooltip();
		clock.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

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
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue();
		} else {
			// TODO: value must be UTC
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			DateFormat fixDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date now = new Date();
			String value = fixDateFormat.format(now) + "-"
					+ Integer.toString(clock.getHours()) + ":"
					+ Integer.toString(clock.getMinutes()) + ":"
					+ Integer.toString(clock.getSeconds());
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public Date convertValue(String value) {
		// TODO: value must be UTC
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			// TODO malformed value
		}
		return new Date();

	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

}
