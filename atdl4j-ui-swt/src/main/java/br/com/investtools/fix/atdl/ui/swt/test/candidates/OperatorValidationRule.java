package br.com.investtools.fix.atdl.ui.swt.test.candidates;

import java.util.Map;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT.Enum;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

/**
 * Validator that validates input against a constant value.
 * 
 * @author renato.gallart
 * 
 * @param <E>
 */
public class OperatorValidationRule<E extends Comparable<E>> extends OperatorValidationRuleAbstract<E> {

	private String field;

	private Enum operator;

	private E value;

	public OperatorValidationRule(String field, Enum operator, String field2, E value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(StrategyEdit strategyEdit, Map<String, ValidationRule> rules,
			Map<String, ParameterWidget<?>> widgets) throws ValidationException {

		// get the widget from context using field name
		ParameterWidget<?> widget = widgets.get(field);
		if (widget == null) {
			throw new ValidationException(null,
					"No widget defined for field \"" + field
							+ "\" in this context");
		}

		Object fieldValue = widget.getValue();

		validateValues(strategyEdit, widget, fieldValue, operator, value);
	}


}
