package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;
import br.com.investtools.fix.atdl.valid.xmlbeans.LogicOperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.LogicOperatorT.Enum;

/**
 * ValidationRule that behaves as a composite, using on of the following
 * operators: AND, OR, XOR, NOT.
 * 
 * @author renato.gallart
 * 
 */
public class LogicalOperatorValidationRule implements EditUI {

	private Enum operator;

	private List<EditUI> rules;

	public LogicalOperatorValidationRule(Enum operator) {
		this.operator = operator;
		this.rules = new ArrayList<EditUI>();
	}

	public void addRule(EditUI rule) {
		this.rules.add(rule);
	}

	@Override
	public void validate(Map<String, EditUI> rules,
			Map<String, ParameterUI<?>> widgets) throws ValidationException,
			XmlException {

		ValidationException ex = null;

		switch (operator.intValue()) {
		case LogicOperatorT.INT_AND:

			// AND - with short circuit

			/*
			 * for (EditUI rule : this.rules) { try { rule.validate(rules,
			 * widgets); } catch (ValidationException e) { throw e; } }
			 */

			// AND - no short circuit, throwing last error
			/*
			 * for (ValidationRule rule : this.rules) { try {
			 * rule.validate(strategyEdit, rules, widgets); } catch
			 * (ValidationException e) { ex = e; } } if (ex != null) { throw ex;
			 * }
			 */

			// AND - new interpretation
			boolean valid = true;

			for (EditUI rule : this.rules) {
				try {
					rule.validate(rules, widgets);
					valid = false;
					break;
				} catch (ValidationException e) {
					ex = e;
				}
			}
			if (valid) {
				throw ex;
			}

			break;

		case LogicOperatorT.INT_OR:

			// boolean valid = false;

			// OR - no short circuit
			/*
			 * for (ValidationRule rule : rules) { try { rule.validate(); valid
			 * = true; } catch (ValidationException e) { ex = e; } } if (!valid)
			 * { throw ex; }
			 */

			// OR - short circuit
			/*
			 * for (EditUI rule : this.rules) { try { rule.validate(rules,
			 * widgets); valid = true; break; } catch (ValidationException e) {
			 * ex = e; } } if (!valid) { throw ex; }
			 */

			// OR - new interpretation, behaves as the old AND with short
			// circuit
			for (EditUI rule : this.rules) {
				try {
					rule.validate(rules, widgets);
				} catch (ValidationException e) {
					throw e;
				}
			}

			break;

		case LogicOperatorT.INT_XOR:

			boolean state = false;

			for (int i = 0; i < this.rules.size(); i++) {

				// XOR

				/*
				 * try { this.rules.get(i).validate(rules, widgets); if (i != 0)
				 * { if (!state) { break; } } state = true;
				 * 
				 * } catch (ValidationException e) { if (i != 0) { if (state) {
				 * break; } } state = false; }
				 * 
				 * if (i == this.rules.size() - 1) {
				 * 
				 * ParameterUI<?> parameter = getParameterFromRule(i); throw new
				 * ValidationException(parameter);
				 * 
				 * }
				 */

				// XOR - new interpretation
				try {
					this.rules.get(i).validate(rules, widgets);
					if (i == 0) {
						state = false;
					} else {
						if (state) {
							ParameterUI<?> parameter = getParameterFromRule(i);
							throw new ValidationException(parameter);
						}

					}
				} catch (ValidationException e) {
					if (i == 0) {
						state = true;
					} else {
						if (!state) {
							ParameterUI<?> parameter = getParameterFromRule(i);
							throw new ValidationException(parameter);
						}
					}
				}

				break;

			}

			break;

		case LogicOperatorT.INT_NOT:

			/*
			 * NOT for (EditUI rule : this.rules) { try { rule.validate(rules,
			 * widgets); } catch (ValidationException e) { ex = e; } }
			 * 
			 * if (ex == null) { ParameterUI<?> parameter =
			 * getParameterFromRule(0); throw new
			 * ValidationException(parameter); }
			 * 
			 * break;
			 */

			// NOT - new interpretation
			for (EditUI rule : this.rules) {
				try {
					rule.validate(rules, widgets);
				} catch (ValidationException e) {
					ex = e;
				}
			}

			if (ex == null) {
				ParameterUI<?> parameter = getParameterFromRule(0);
				throw new ValidationException(parameter);
			}

			break;

		}

	}

	private ParameterUI<?> getParameterFromRule(int i) {
		ParameterUI<?> parameter = null;
		EditUI rule = this.rules.get(i);
		if (rule instanceof ParameterValidationRule) {
			ParameterValidationRule r = (ParameterValidationRule) rule;
			parameter = r.getParameter();
		}
		return parameter;
	}
}
