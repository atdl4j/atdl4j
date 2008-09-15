package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCDateT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class UTCDateClockParameterWidget implements ParameterWidget<Date> {

	private ParameterT parameter;

	private DateTime clock;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));

		// clock
		DateTime clock = new DateTime(parent, style | SWT.BORDER | SWT.DATE
				| SWT.MEDIUM);
		clock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// tooltip
		String tooltip = parameter.getTooltip();
		clock.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		UTCDateT utcDate = (UTCDateT) parameter;

		// init value
		if (utcDate.isSetInitValue()) {
			Calendar c = utcDate.getInitValue();
			clock.setDay(c.get(Calendar.DAY_OF_MONTH));
			clock.setMonth(c.get(Calendar.MONTH));
			clock.setYear(c.get(Calendar.YEAR));
		}
		
		this.clock = clock;

		return parent;
	}

	public Date getValue() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, clock.getYear());
		c.set(Calendar.MONTH, clock.getMonth());
		c.set(Calendar.DAY_OF_MONTH, clock.getDay());
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	@Override
	public String getFIXValue() {

		Date date = this.getValue();
		DateFormat fixUTCDateClockFormat = new SimpleDateFormat("yyyyMM");
		String value = fixUTCDateClockFormat.format(date);

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
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			throw new XmlException( "Unable to parse \"" + value + "\" with format \"yyyyMMdd\"");
		}

	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

}
