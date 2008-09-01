package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument.Strategies;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;
import br.com.investtools.fix.atdl.ui.StrategiesUI;
import br.com.investtools.fix.atdl.ui.swt.test.candidates.Field2OperatorValidationRule;
import br.com.investtools.fix.atdl.ui.swt.test.candidates.LogicalOperatorValidationRule;
import br.com.investtools.fix.atdl.ui.swt.test.candidates.RootValidationRule;
import br.com.investtools.fix.atdl.ui.swt.test.candidates.ValidationRule;
import br.com.investtools.fix.atdl.ui.swt.test.candidates.ValueOperatorValidationRule;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditRefT;
import br.com.investtools.fix.atdl.valid.xmlbeans.LogicOperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.OperatorT;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class SWTStrategiesUI implements StrategiesUI {

	private static final Logger logger = LoggerFactory
			.getLogger(SWTStrategiesUI.class);

	private Map<StrategyT, RootValidationRule> strategyRules;

	private Map<String, ValidationRule> strategiesRules;

	private Map<StrategyT, Map<String, ParameterWidget<?>>> strategiesParameters;

	public SWTStrategiesUI(StrategiesDocument strategiesDocument,
			TabFolder tabFolder) {
		try {
			SWTFactory factory = new SWTFactory();
			Strategies strategies = strategiesDocument.getStrategies();

			// create repository for global rules
			strategiesRules = new HashMap<String, ValidationRule>();

			for (Edit edit : strategies.getEditArray()) {
				String id = edit.getId();
				if (id != null) {
					ValidationRule rule = createRule(edit);
					strategiesRules.put(id, rule);
				} else {
					logger.warn("Strategies-scoped edit without id");
				}
			}

			// create repository for rules of each strategy
			strategyRules = new HashMap<StrategyT, RootValidationRule>();

			// create repository for all parameters
			strategiesParameters = new HashMap<StrategyT, Map<String, ParameterWidget<?>>>();

			for (StrategyT strategy : strategies.getStrategyArray()) {

				// create tab item for strategy
				TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
				tabItem.setText(getText(strategy));
				Composite strategyComposite = new Composite(tabFolder, SWT.NONE);
				strategyComposite.setLayout(new FillLayout());

				Map<String, ParameterWidget<?>> strategyParameters = new HashMap<String, ParameterWidget<?>>();
				try {
					// TODO: use iterator to traverse in correct order

					// build panels
					for (StrategyPanel panel : strategy.getStrategyLayout()
							.getStrategyPanelArray()) {
						strategyParameters.putAll(factory.create(
								strategyComposite, panel, SWT.NONE));
					}

					// build parameters
					for (ParameterT parameter : strategy.getParameterArray()) {
						strategyParameters
								.put(parameter.getName(), factory.create(
										strategyComposite, parameter, SWT.NONE));
					}

					tabItem.setControl(strategyComposite);

					RootValidationRule strategyRule = new RootValidationRule();

					// generate validation rules for Edit elements
					for (Edit edit : strategy.getEditArray()) {
						// TODO
					}

					// generate validation rules for StrategyEdit elements
					for (StrategyEdit se : strategy.getStrategyEditArray()) {
						String errorMessage = se.getErrorMessage();
						if (se.isSetEdit()) {
							strategyRule.putRule(se, createRule(se.getEdit()));
						}

						// reference for a previously defined rule
						if (se.isSetEditRef()) {
							EditRefT editRef = se.getEditRef();
							String id = editRef.getId();
							strategyRule.putRule(se,
									new ReferencedValidationRule(id));
						}
					}

					strategyRules.put(strategy, strategyRule);

					// handle flows
					// TODO
					// generate FIX message
					// System.out.println(pwMap.toString());
					// for (ParameterWidget<?> pw : pwMap.values()) {
					// System.out.println(pw.getFIXValue());
					// }
				} catch (UnsupportedOperationException e) {
					// yahoo!
				}

				strategiesParameters.put(strategy, strategyParameters);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ValidationRule createRule(EditRefT editRef) throws XmlException {
		if (editRef.getId() != null) {
			String id = editRef.getId();
			return new ReferencedValidationRule(id);
		} else {
			throw new XmlException("EditRef without an id");
		}
	}

	private ValidationRule createRule(Edit edit) throws XmlException {
		if (edit.isSetLogicOperator()) {
			// edit represents a logical operator [AND|OR|NOT|XOR]
			LogicOperatorT.Enum operator = edit.getLogicOperator();
			LogicalOperatorValidationRule rule = new LogicalOperatorValidationRule(
					operator);
			if (edit.getEditArray() != null) {
				for (Edit innerEdit : edit.getEditArray()) {
					rule.addRule(createRule(innerEdit));
				}
			} else if (edit.getEditRefArray() != null) {
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
					return new ValueOperatorValidationRule<String>(field,
							operator, value);

				} else if (edit.isSetField2()) {
					// validates against another field value
					String field2 = edit.getField2();
					return new Field2OperatorValidationRule<String>(field,
							operator, field2);

				} else {
					// must be EX or NX
					if (operator.intValue() == OperatorT.INT_EX
							|| operator.intValue() == OperatorT.INT_NX) {
						return new ValueOperatorValidationRule<String>(field,
								operator, null);
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

	private static String getText(StrategyT strategy) {
		if (strategy.getUiRep() != null) {
			return strategy.getUiRep();
		}
		return strategy.getName();
	}

	@Override
	public void validate(StrategyT strategy) {
		// get validation rule for given strategy
		RootValidationRule r = strategyRules.get(strategy);
		if (r != null) {
			// delegate validation, passing all rules as context information,
			// and all parameters of the given strategy
			r.validate(strategiesRules, strategiesParameters.get(strategy));
		} else {
			logger.info("No validation rule defined for strategy \"{}\"",
					strategy.getName());
		}
	}

	/*
	 * private static RootValidationRule generateValidator(StrategyEdit se, Map<String,
	 * ParameterWidget<?>> pwMap, Edit edit) {
	 * 
	 * RootValidationRule rule = new RootValidationRule();
	 * 
	 * String id = null;
	 * 
	 * if (edit.isSetLogicOperator()) {
	 * 
	 * if (edit.getEditArray() != null) {
	 * 
	 * for (Edit innerEdit : edit.getEditArray()) {
	 * 
	 * if (edit.isSetId()) { id = edit.getId(); //
	 * mainRule.getRefRules().put(id, // generateValidator(mainRule, se, pwMap,
	 * innerEdit)); } // rule.getRules().add(generateValidator(mainRule, se, //
	 * pwMap, innerEdit)); } }
	 * 
	 * if (edit.getEditRefArray() != null) {
	 * 
	 * for (EditRefT innerRefEdit : edit.getEditRefArray()) { } } } else if
	 * (edit.isSetField() && edit.isSetOperator() && (edit.isSetField2() ^
	 * edit.isSetValue())) {
	 * 
	 * String field = edit.getField(); ParameterWidget<?> pw =
	 * pwMap.get(field); Enum operator = edit.getOperator();
	 * 
	 * if (pw != null) { // TODO if (edit.isSetValue()) { // compare with
	 * constant value String value = edit.getValue();
	 * 
	 * switch (pw.getParameter().getType()) { case 1: ParameterWidget<BigDecimal>
	 * pw2 = (ParameterWidget<BigDecimal>) pw; BigDecimal o = (BigDecimal)
	 * pw.convertValue(value); ValueOperatorValidationRuleOld<BigDecimal> r =
	 * new ValueOperatorValidationRuleOld<BigDecimal>( se, pw2, operator, o); //
	 * rule.getRules().add(r);
	 * 
	 * default: break; } } else if (edit.isSetField2()) { // compare with other
	 * field ParameterWidget<?> pw2 = pwMap.get(edit.getField2()); if (pw2 !=
	 * null) { Field2OperatorValidationRuleOld r = new
	 * Field2OperatorValidationRuleOld( se, pw, operator, pw2); //
	 * rule.getRules().add(r); } else { logger.error("{} field2 not found.",
	 * edit.getField2()); } } } else { logger.error("{} field1 not found.",
	 * edit.getField()); } } else { logger.error("No logic operator or operator
	 * set."); } return rule; }
	 */
}
