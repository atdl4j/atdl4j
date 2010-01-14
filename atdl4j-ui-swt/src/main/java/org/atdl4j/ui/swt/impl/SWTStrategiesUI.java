package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.validation.EditT;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.swt.util.RuleFactory;
import org.eclipse.swt.widgets.Composite;

public class SWTStrategiesUI implements StrategiesUI<Composite> {

	private Map<String, ValidationRule> strategiesRules;

	public SWTStrategiesUI(StrategiesT strategies) throws JAXBException {

		strategiesRules = new HashMap<String, ValidationRule>();

		for (EditT edit : strategies.getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = RuleFactory.createRule(edit,
						strategiesRules, strategies);
				strategiesRules.put(id, rule);
			} else {
				throw new JAXBException("Strategies-scoped edit without id");
			}
		}
	}

	public SWTStrategyUI createUI(StrategyT strategy, Composite parent)
			throws JAXBException {
		// try {
		return new SWTStrategyUI(strategy, strategiesRules, parent);
		// } catch (JAXBException e) {
		// throw new JAXBException("Error in Strategy \"" + strategy.getName() +
		// "\": " + e.getMessage());
		// }
	}

}
