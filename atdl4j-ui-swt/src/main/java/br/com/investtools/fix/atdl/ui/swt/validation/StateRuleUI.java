package br.com.investtools.fix.atdl.ui.swt.validation;

import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import br.com.investtools.fix.atdl.flow.xmlbeans.BehaviorT;
import br.com.investtools.fix.atdl.flow.xmlbeans.StateRuleDocument.StateRule;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.RuleFactory;
import br.com.investtools.fix.atdl.ui.swt.ValidationException;

public class StateRuleUI implements Listener {

	private ParameterWidget<?> affectedWidget;
	private StateRule stateRule;
	private Map<String, ParameterWidget<?>> widgets;
	private Map<String, EditUI> rules;
	private EditUI rule;

	public StateRuleUI(ParameterWidget<?> affectedWidget, StateRule stateRule,
			Map<String, ParameterWidget<?>> parameters,
			Map<String, EditUI> rules) throws XmlException {
		this.affectedWidget = affectedWidget;
		this.stateRule = stateRule;
		this.widgets = parameters;
		this.rules = rules;
		this.rule = RuleFactory.createRule(stateRule.getEdit(), rules);
	}

	@Override
	public void handleEvent(Event event) {

		try {
			rule.validate(rules, widgets);
		} catch (ValidationException e) {
			setBehaviorAsStateRule(true);
			return;
		} catch (XmlException e) {
			throw new RuntimeException(e);
		}

		setBehaviorAsStateRule(false);

	}

	private void setBehaviorAsStateRule(Boolean state) {

		Boolean enabled;
		Boolean visible;

		if (stateRule.isSetEnabled()) {
			enabled = stateRule.getEnabled();
			for (Control control : affectedWidget.getControls()) {
				control.setEnabled(!(enabled ^ state));
			}
		}

		if (stateRule.isSetVisible()) {
			visible = stateRule.getVisible();
			for (Control control : affectedWidget.getControls()) {
				control.setVisible(!(visible ^ state));
			}
		}

		if (stateRule.isSetBehavior()) {
			for (Control control : affectedWidget.getControls()) {
				BehaviorT.Enum behaviorT = stateRule.getBehavior();

				if (behaviorT.equals(BehaviorT.DISABLE)) {
					control.setEnabled(!(false ^ state));
				} else if (behaviorT.equals(BehaviorT.ENABLE)) {
					control.setEnabled(!(true ^ state));
				} else if (behaviorT.equals(BehaviorT.HIDE)) {
					control.setVisible(!(false ^ state));
				} else if (behaviorT.equals(BehaviorT.UNHIDE)) {
					control.setVisible(!(true ^ state));
				}
			}

		}
	}

}
