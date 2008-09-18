package br.com.investtools.fix.atdl.ui.swt.widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class SliderParameterWidget implements ParameterWidget<BigDecimal> {

	private ParameterT parameter;

	private Scale slider;
	
	private Label label;

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		this.parameter = parameter;

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		int numColumns = parameter.getEnumPairArray() != null ? parameter
				.getEnumPairArray().length : 1;
		c.setLayout(new GridLayout(numColumns, true));

		// slider
		Scale slider = new Scale(c, style | SWT.HORIZONTAL);
		this.slider = slider;
		slider.setIncrement(1);
		slider.setPageIncrement(1);
		GridData sliderData = new GridData(SWT.FILL, SWT.FILL, true, false);
		sliderData.horizontalSpan = numColumns;
		slider.setLayoutData(sliderData);
		slider.setMaximum(numColumns - 1);

		for (int i = 0; i < parameter.getEnumPairArray().length; i++) {
			Label label = new Label(c, SWT.NONE);
			label.setText(parameter.getEnumPairArray(i).getUiRep());
			label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
					false));
		}

		// tooltip
		String tooltip = parameter.getTooltip();
		slider.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public BigDecimal getValue() {
		return new BigDecimal(this.slider.getSelection());
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue().toString();
		} else {
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			String value = getValue().toString();
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public BigDecimal convertValue(String value) {
		return new BigDecimal(value);
	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
		slider.addListener(SWT.Selection, listener);
	}

	@Override
	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(slider);
		return widgets;
	}
	
}
