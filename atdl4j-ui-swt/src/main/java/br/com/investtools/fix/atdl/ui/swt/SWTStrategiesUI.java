package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.widgets.Composite;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument.Strategies;
import br.com.investtools.fix.atdl.ui.StrategiesUI;
import br.com.investtools.fix.atdl.ui.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.validation.EditUI;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;

public class SWTStrategiesUI implements StrategiesUI<Composite> {

	private Map<String, EditUI> strategiesRules;

	public SWTStrategiesUI(StrategiesDocument strategiesDocument)
			throws XmlException {
		Strategies strategies = strategiesDocument.getStrategies();

		strategiesRules = new HashMap<String, EditUI>();

		for (Edit edit : strategies.getEditArray()) {
			String id = edit.getId();
			if (id != null) {
				EditUI rule = RuleFactory.createRule(edit, strategiesRules);
				strategiesRules.put(id, rule);
			} else {
				throw new XmlException("Strategies-scoped edit without id");
			}
		}
	}

	@Override
	public StrategyUI createUI(StrategyT strategy, Composite parent)
			throws XmlException {
		// create strategy UI representation
		return new SWTStrategyUI(strategy, strategiesRules, parent);
	}

}
