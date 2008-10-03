package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import br.com.investtools.fix.atdl.core.xmlbeans.UTCDateT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.util.ParameterListenerWrapper;
import br.com.investtools.fix.atdl.ui.swt.util.WidgetHelper;

public class UTCDateClockParameterWidget implements ParameterUI<Date> {

	private ParameterT parameter;

	private DateTime clock;

	private Label label;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		// clock
		DateTime clock = new DateTime(parent, style | SWT.BORDER | SWT.DATE
				| SWT.MEDIUM);
		this.clock = clock;
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

		return parent;
	}

	@Override
	public Date getValue() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, clock.getYear());
		c.set(Calendar.MONTH, clock.getMonth());
		c.set(Calendar.DAY_OF_MONTH, clock.getDay());
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	@Override
	public void setValue(Date value) {
		Calendar c = Calendar.getInstance();
		c.setTime(value);
		clock.setDay(c.get(Calendar.DAY_OF_MONTH));
		clock.setMonth(c.get(Calendar.MONTH));
		clock.setYear(c.get(Calendar.YEAR));
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
			throw new XmlException("Unable to parse \"" + value
					+ "\" with format \"yyyyMMdd\"");
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
