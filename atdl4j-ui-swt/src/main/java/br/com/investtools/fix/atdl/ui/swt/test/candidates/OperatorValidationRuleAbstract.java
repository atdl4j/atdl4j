package br.com.investtools.fix.atdl.ui.swt.test.candidates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT.Enum;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

/**
 * 
 * 
 * @author renato.gallart
 * 
 * @param <E>
 */
public abstract class OperatorValidationRuleAbstract<E extends Comparable<E>> implements
		ValidationRule {

	private static final Logger logger = LoggerFactory
	.getLogger(OperatorValidationRuleAbstract.class);
	
	protected void validateValues(StrategyEdit strategyEdit,
			ParameterWidget<?> widget, Object value1, Enum operator, Object value2) {
		switch (operator.intValue()) {
		case OperatorT.INT_EQ:
			if (!value2.equals(value1)) {
				throw new ValidationException(widget, strategyEdit.getErrorMessage());
			}
			break;

		case OperatorT.INT_EX:
			if (value1 == null || "".equals(value1.toString())) {
				throw new ValidationException(widget, strategyEdit.getErrorMessage());
			}
			break;

		case OperatorT.INT_GE:
			if (value1 instanceof Comparable) {
				Comparable c = (Comparable) value1;
				if (c.compareTo(value2) < 0) {
					throw new ValidationException(widget, strategyEdit.getErrorMessage());
				}
			} else {
				throw new ValidationException(widget, "Value is not comparable");
			}
			break;

		case OperatorT.INT_GT:
			if (value1 instanceof Comparable) {
				Comparable c = (Comparable) value1;
				if (c.compareTo(value2) <= 0) {
					throw new ValidationException(widget, strategyEdit.getErrorMessage());
				}
			} else {
				throw new ValidationException(widget, "Value is not comparable");
			}
			break;

		case OperatorT.INT_LE:
			if (value1 instanceof Comparable) {
				Comparable c = (Comparable) value1;
				if (c.compareTo(value2) > 0) {
					throw new ValidationException(widget, strategyEdit.getErrorMessage());
				}
			} else {
				throw new ValidationException(widget, "Value is not comparable");
			}
			break;

		case OperatorT.INT_LT:
			if (value1 instanceof Comparable) {
				Comparable c = (Comparable) value1;
				if (c.compareTo(value2) >= 0) {
					throw new ValidationException(widget, strategyEdit.getErrorMessage());
				}
			} else {
				throw new ValidationException(widget, "Value is not comparable");
			}
			break;

		case OperatorT.INT_NE:
			if (value2.equals(value1)) {
				throw new ValidationException(widget, strategyEdit.getErrorMessage());
			}
			break;

		case OperatorT.INT_NX:
			if (value1 != null && !"".equals(value1.toString())) {
				throw new ValidationException(widget, strategyEdit.getErrorMessage());
			}
			break;

		default:
			// Supposed to never happen, since the schema enforces an enumerated
			// base restriction.
			logger.error("Invalid operator.");
			break;
		}
	}


}
