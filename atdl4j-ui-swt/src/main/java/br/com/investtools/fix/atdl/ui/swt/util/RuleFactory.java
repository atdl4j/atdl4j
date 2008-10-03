package br.com.investtools.fix.atdl.ui.swt.util;

import java.util.Map;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.validation.Field2OperatorValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.LogicalOperatorValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ReferencedValidationRule;
import br.com.investtools.fix.atdl.ui.swt.validation.ValueOperatorValidationRule;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditRefT;
import br.com.investtools.fix.atdl.valid.xmlbeans.LogicOperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;

public abstract class RuleFactory {

	public static EditUI createRule(Edit edit,
			Map<String, EditUI> strategiesRules) throws XmlException {
		if (edit.isSetLogicOperator()) {
			// edit represents a logical operator [AND|OR|NOT|XOR]
			LogicOperatorT.Enum operator = edit.getLogicOperator();
			LogicalOperatorValidationRule rule = new LogicalOperatorValidationRule(
					operator);
			if (edit.getEditArray() != null) {
				for (Edit innerEdit : edit.getEditArray()) {

					EditUI innerRule = createRule(innerEdit, strategiesRules);
					if (innerEdit.isSetId())
						strategiesRules.put(innerEdit.getId(), innerRule);
					rule.addRule(innerRule);
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
					EditUI rule = new ValueOperatorValidationRule(field,
							operator, value);
					if (edit.isSetId())
						strategiesRules.put(edit.getId(), rule);

					return rule;

				} else if (edit.isSetField2()) {
					// validates against another field value
					String field2 = edit.getField2();
					EditUI rule = new Field2OperatorValidationRule(field,
							operator, field2);
					if (edit.isSetId())
						strategiesRules.put(edit.getId(), rule);

					return rule;

				} else {
					// must be EX or NX
					if (operator.intValue() == OperatorT.INT_EX
							|| operator.intValue() == OperatorT.INT_NX) {

						EditUI rule = new ValueOperatorValidationRule(field,
								operator, null);
						if (edit.isSetId())
							strategiesRules.put(edit.getId(), rule);

						return rule;
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

	public static EditUI createRule(EditRefT editRef) throws XmlException {
		if (editRef.getId() != null) {
			String id = editRef.getId();
			return new ReferencedValidationRule(id);
		} else {
			throw new XmlException("EditRef without an id");
		}
	}

}
