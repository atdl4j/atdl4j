package br.com.investtools.fix.atdl.ui.swt.test.candidates;

import java.math.BigDecimal;
import java.util.Map;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class CheckBoundsValidationRule implements ValidationRule {

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
	public void validate(StrategyEdit strategyEdit,
			Map<String, ValidationRule> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException {
		BigDecimal n = widget.getValue();

		if (n.doubleValue() < min.doubleValue()) {
			throw new ValidationException(widget, strategyEdit
					.getErrorMessage());
		}
		if (n.doubleValue() > max.doubleValue()) {
			throw new ValidationException(widget, strategyEdit
					.getErrorMessage());
		}

	}
}
