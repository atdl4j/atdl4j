package org.atdl4j.ui.swt.widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.EnumPairT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.SliderT;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Widget;

public class SliderWidget extends AbstractSWTWidget<BigDecimal> {

	private Scale slider;
	private Label label;

	public SliderWidget(SliderT control, ParameterT parameter)
			throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(control.getLabel());
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		int numColumns = (parameter != null && parameter.getEnumPair() != null && parameter
				.getEnumPair().size() > 0) ? parameter.getEnumPair().size() : 1;
		c.setLayout(new GridLayout(numColumns, true));

		// slider
		Scale slider = new Scale(c, style | SWT.HORIZONTAL);
		this.slider = slider;
		slider.setIncrement(1);
		slider.setPageIncrement(1);
		GridData sliderData = new GridData(SWT.FILL, SWT.FILL, true, false);
		sliderData.horizontalSpan = numColumns;
		slider.setLayoutData(sliderData);
		slider.setMaximum(numColumns > 1 ? numColumns - 1 : 1);

		// labels based on parameter enumPair
		// TODO: this should be changed in the FIXatdl spec so that sliders can
		// have
		// list items, with labels based on the UiRep.
		if (parameter != null) {
			for (EnumPairT enumPair : parameter.getEnumPair()) {
				Label label = new Label(c, SWT.NONE);
				label.setText(enumPair.getEnumID());
				label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
						false));
			}
		}

		// tooltip
		String tooltip = control.getTooltip();
		slider.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public BigDecimal getControlValue() {
		try {
			return new BigDecimal(slider.getSelection());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public BigDecimal getParameterValue() {
		return getControlValue();
	}

	public void setValue(BigDecimal value) {
		slider.setSelection(value.intValue());
	}

	public void generateStateRuleListener(Listener listener) {
		slider.addListener(SWT.Selection, listener);
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(slider);
		return widgets;
	}

	public void addListener(Listener listener) {
		slider.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	public void removeListener(Listener listener) {
		slider.removeListener(SWT.Selection, listener);
	}

}
