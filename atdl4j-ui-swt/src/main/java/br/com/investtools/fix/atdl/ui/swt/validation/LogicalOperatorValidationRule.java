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

			// AND com curto circuito

			for (EditUI rule : this.rules) {
				try {
					rule.validate(rules, widgets);
				} catch (ValidationException e) {
					throw e;
				}
			}

			// AND sem curto circuito, relancando o ultimo erro
			/*
			 * for (ValidationRule rule : this.rules) { try {
			 * rule.validate(strategyEdit, rules, widgets); } catch
			 * (ValidationException e) { ex = e; } } if (ex != null) { throw ex;
			 * }
			 */

			break;

		case LogicOperatorT.INT_OR:

			boolean valid = false;

			// OR sem curto circuito
			/*
			 * for (ValidationRule rule : rules) { try { rule.validate(); valid
			 * = true; } catch (ValidationException e) { ex = e; } } if (!valid)
			 * { throw ex; }
			 */

			// OR com curto circuito
			for (EditUI rule : this.rules) {
				try {
					rule.validate(rules, widgets);
					valid = true;
					break;
				} catch (ValidationException e) {
					ex = e;
				}
			}
			if (!valid) {
				throw ex;
			}

			break;

		case LogicOperatorT.INT_XOR:

			boolean state = true;

			for (int i = 0; i < this.rules.size(); i++) {
				try {
					this.rules.get(i).validate(rules, widgets);
					if (i != 0) {
						if (!state) {
							break;
						}
					}
					state = true;

				} catch (ValidationException e) {
					if (i != 0) {
						if (state) {
							break;
						}
					}
					state = false;
				}

				if (i == this.rules.size() - 1) {

					ParameterUI<?> parameter = null;
					for (int j = 0; j < this.rules.size(); j++) {
						EditUI rule = this.rules.get(i);
						if (rule instanceof ParameterValidationRule) {
							ParameterValidationRule r = (ParameterValidationRule) rule;
							parameter = r.getParameter();
						}
					}
					throw new ValidationException(parameter);

				}

			}

			break;

		case LogicOperatorT.INT_NOT:

			for (EditUI rule : this.rules) {
				try {
					rule.validate(rules, widgets);
				} catch (ValidationException e) {
				}
			}

			if (ex == null) {

				EditUI rule = this.rules.get(0);
				ParameterUI<?> parameter = null;
				if (rule instanceof ParameterValidationRule) {
					ParameterValidationRule r = (ParameterValidationRule) rule;
					parameter = r.getParameter();
				}
				throw new ValidationException(parameter);

			}

			break;

		}

	}
}
