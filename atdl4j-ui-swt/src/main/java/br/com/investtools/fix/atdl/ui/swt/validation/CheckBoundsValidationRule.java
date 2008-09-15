package br.com.investtools.fix.atdl.ui.swt.validation;

import java.math.BigDecimal;
import java.util.Map;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;

public class CheckBoundsValidationRule implements EditUI {

	private ParameterWidget<BigDecimal> widget;

	private Number min;

	private Number max;

	public CheckBoundsValidationRule(ParameterWidget<BigDecimal> widget,
			Number min, Number max) {
		this.widget = widget;
		this.min = min;
		this.max = max;
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException {
		BigDecimal n = widget.getValue();

		if (n.doubleValue() < min.doubleValue()) {
			throw new ValidationException(widget);
		}
		if (n.doubleValue() > max.doubleValue()) {
			throw new ValidationException(widget);
		}

	}
}
