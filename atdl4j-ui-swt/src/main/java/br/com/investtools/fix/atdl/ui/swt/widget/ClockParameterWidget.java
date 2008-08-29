package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());

		// label
		Label l = new Label(c, SWT.NONE);
		l.setText(getLabelText(parameter));

		// clock
		DateTime clock = new DateTime(c, style | SWT.BORDER | SWT.TIME);
		this.clock = clock;

		// tooltip
		String tooltip = parameter.getTooltip();
		clock.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return c;
	}

	public String getLabelText(ParameterT parameter) {
		if (parameter.getUiRep() != null) {
			return parameter.getUiRep();
		}
		return parameter.getName();
	}

	public Date getValue() {
		// TODO: verificar se conversão é essa mesma

		String value = Integer.toString(clock.getDay()) + "/" + Integer.toString(clock.getMonth()) + "/" + Integer.toString(clock.getYear())  + "-" + Integer.toString(clock.getHours()) + ":" + Integer.toString(clock.getMinutes()) + ":" + Integer.toString(clock.getSeconds());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
		
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			// XXX malformed value
		}
		return new Date();
		
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue();
		} else {
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			DateFormat fixDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date now = new Date();
			String value = fixDateFormat.format(now) + "-" + Integer.toString(clock.getHours()) + ":" + Integer.toString(clock.getMinutes()) + ":" + Integer.toString(clock.getSeconds());
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public Date convertValue(String value) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			// XXX malformed value
		}
		return new Date();

	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}


}
