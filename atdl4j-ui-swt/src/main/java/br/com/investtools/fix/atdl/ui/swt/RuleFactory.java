package br.com.investtools.fix.atdl.ui.swt;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.validation.Field2OperatorValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.LogicalOperatorValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ValueOperatorValidationRule;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditRefT;
import br.com.investtools.fix.atdl.valid.xmlbeans.LogicOperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;

public abstract class RuleFactory {

	public static ValidationRule createRule(Edit edit) throws XmlException {
		if (edit.isSetLogicOperator()) {
			// edit represents a logical operator [AND|OR|NOT|XOR]
			LogicOperatorT.Enum operator = edit.getLogicOperator();
			LogicalOperatorValidationRule rule = new LogicalOperatorValidationRule(
					operator);
			if (edit.getEditArray() != null) {
				for (Edit innerEdit : edit.getEditArray()) {
					rule.addRule(createRule(innerEdit));
				}
			}
			if (edit.getEditRefArray() != null) {
				for (EditRefT innerRefEdit : edit.getEditRefArray()) {
					rule.addRule(createRule(innerRefEdit));
				}
			}
			return rule;

		} else if (edit.isSetOperator()) {
			// edit represents a simple operator [EX|NX|EQ|LT|GT|NE|LE|GE]
			OperatorT.Enum operator = edit.getOperator();
			if (edit.isSetField()) {
				String field = edit.getField();
				if (edit.isSetValue()) {
					// validates against a constant value
					String value = edit.getValue();
					return new ValueOperatorValidationRule(field, operator,
							value);

				} else if (edit.isSetField2()) {
					// validates against another field value
					String field2 = edit.getField2();
					return new Field2OperatorValidationRule(field, operator,
							field2);

				} else {
					// must be EX or NX
					if (operator.intValue() == OperatorT.INT_EX
							|| operator.intValue() == OperatorT.INT_NX) {
						return new ValueOperatorValidationRule(field, operator,
								null);
					} else {
						throw new XmlException(
								"Operator must be EX or NX when there is no \"value\" of \"field2\" attribute");
					}
				}
			} else {
				throw new XmlException("No field set for edit  \""
						+ edit.getId() + "\"");
			}
		} else {
			throw new XmlException(
					"No logic operator or operator set for edit \""
							+ edit.getId() + "\"");
		}
	}

	public static ValidationRule createRule(EditRefT editRef)
			throws XmlException {
		if (editRef.getId() != null) {
			String id = editRef.getId();
			return new ReferencedValidationRule(id);
		} else {
			throw new XmlException("EditRef without an id");
		}
	}

}
