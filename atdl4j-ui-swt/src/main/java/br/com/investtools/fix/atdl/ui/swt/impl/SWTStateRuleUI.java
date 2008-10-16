package br.com.investtools.fix.atdl.ui.swt.impl;

import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import br.com.investtools.fix.atdl.flow.xmlbeans.StateRuleDocument.StateRule;
import br.com.investtools.fix.atdl.ui.swt.EditUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.exceptions.ValidationException;
import br.com.investtools.fix.atdl.ui.swt.util.RuleFactory;
import br.com.investtools.fix.atdl.ui.swt.widget.CheckBoxListParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.CheckBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.DropDownListParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.DualSpinnerParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.HiddenParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.LocalMktTimeClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.MonthYearClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.NumberTextFieldParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.RadioButtonListParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.SingleSpinnerParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.SliderParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.StringTextFieldParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.UTCDateClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.UTCTimeOnlyClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.UTCTimeStampClockParameterWidget;

public class SWTStateRuleUI implements Listener {

	private ParameterUI<?> affectedWidget;
	private StateRule stateRule;
	private Map<String, ParameterUI<?>> widgets;
	private Map<String, EditUI> rules;
	private EditUI rule;

	public SWTStateRuleUI(ParameterUI<?> affectedWidget, StateRule stateRule,
			Map<String, ParameterUI<?>> parameters, Map<String, EditUI> rules)
			throws XmlException {
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

		if (stateRule.isSetEnabled()) {
			Boolean enabled = stateRule.getEnabled();
			for (Control control : affectedWidget.getControls()) {
				control.setEnabled(!(enabled ^ state));
			}
		}

		if (stateRule.isSetVisible()) {
			Boolean visible = stateRule.getVisible();
			for (Control control : affectedWidget.getControls()) {
				control.setVisible(!(visible ^ state));
			}
		}

		if (stateRule.isSetValue() && state) {
			String value = stateRule.getValue();

			try {
				// XXX: working around compiler bug described at
				// http://bugs.sun.com/view_bug.do?bug_id=6548436
				if (CheckBoxListParameterWidget.class
						.isInstance(affectedWidget)) {
					CheckBoxListParameterWidget checkBoxListParameterWidget = CheckBoxListParameterWidget.class
							.cast(affectedWidget);
					checkBoxListParameterWidget.setValue(value);
				} else if (CheckBoxParameterWidget.class
						.isInstance(affectedWidget)) {
					CheckBoxParameterWidget checkBoxParameterWidget = CheckBoxParameterWidget.class
							.cast(affectedWidget);
					checkBoxParameterWidget.setValue(checkBoxParameterWidget
							.convertValue(value));
				} else if (DropDownListParameterWidget.class
						.isInstance(affectedWidget)) {
					DropDownListParameterWidget dropDownListParameterWidget = DropDownListParameterWidget.class
							.cast(affectedWidget);
					dropDownListParameterWidget.setValue(value);
				} else if (DualSpinnerParameterWidget.class
						.isInstance(affectedWidget)) {
					DualSpinnerParameterWidget dualSpinnerParameterWidget = DualSpinnerParameterWidget.class
							.cast(affectedWidget);
					dualSpinnerParameterWidget
							.setValue(dualSpinnerParameterWidget
									.convertValue(value));
				} else if (HiddenParameterWidget.class
						.isInstance(affectedWidget)) {
					HiddenParameterWidget hiddenParameterWidget = HiddenParameterWidget.class
							.cast(affectedWidget);
					hiddenParameterWidget.setValue(value);
				} else if (LocalMktTimeClockParameterWidget.class
						.isInstance(affectedWidget)) {
					LocalMktTimeClockParameterWidget localMktTimeClockParameterWidget = LocalMktTimeClockParameterWidget.class
							.cast(affectedWidget);
					localMktTimeClockParameterWidget
							.setValue(localMktTimeClockParameterWidget
									.convertValue(value));
				} else if (MonthYearClockParameterWidget.class
						.isInstance(affectedWidget)) {
					MonthYearClockParameterWidget monthYearClockParameterWidget = MonthYearClockParameterWidget.class
							.cast(affectedWidget);
					monthYearClockParameterWidget
							.setValue(monthYearClockParameterWidget
									.convertValue(value));
				} else if (NumberTextFieldParameterWidget.class
						.isInstance(affectedWidget)) {
					NumberTextFieldParameterWidget numberTextFieldParameterWidget = NumberTextFieldParameterWidget.class
							.cast(affectedWidget);
					numberTextFieldParameterWidget
							.setValue(numberTextFieldParameterWidget
									.convertValue(value));
				} else if (RadioButtonListParameterWidget.class
						.isInstance(affectedWidget)) {
					RadioButtonListParameterWidget radioButtonListParameterWidget = RadioButtonListParameterWidget.class
							.cast(affectedWidget);
					radioButtonListParameterWidget.setValue(value);
				} else if (SingleSpinnerParameterWidget.class
						.isInstance(affectedWidget)) {
					SingleSpinnerParameterWidget singleSpinnerParameterWidget = SingleSpinnerParameterWidget.class
							.cast(affectedWidget);
					singleSpinnerParameterWidget
							.setValue(singleSpinnerParameterWidget
									.convertValue(value));
				} else if (SliderParameterWidget.class
						.isInstance(affectedWidget)) {
					SliderParameterWidget sliderParameterWidget = SliderParameterWidget.class
							.cast(affectedWidget);
					sliderParameterWidget.setValue(sliderParameterWidget
							.convertValue(value));
				} else if (StringTextFieldParameterWidget.class
						.isInstance(affectedWidget)) {
					StringTextFieldParameterWidget stringTextFieldParameterWidget = StringTextFieldParameterWidget.class
							.cast(affectedWidget);
					stringTextFieldParameterWidget.setValue(value);
				} else if (UTCDateClockParameterWidget.class
						.isInstance(affectedWidget)) {
					UTCDateClockParameterWidget utcDateClockParameterWidget = UTCDateClockParameterWidget.class
							.cast(affectedWidget);
					utcDateClockParameterWidget
							.setValue(utcDateClockParameterWidget
									.convertValue(value));
				} else if (UTCTimeOnlyClockParameterWidget.class
						.isInstance(affectedWidget)) {
					UTCTimeOnlyClockParameterWidget utcTimeOnlyClockParameterWidget = UTCTimeOnlyClockParameterWidget.class
							.cast(affectedWidget);
					utcTimeOnlyClockParameterWidget
							.setValue(utcTimeOnlyClockParameterWidget
									.convertValue(value));
				} else if (UTCTimeStampClockParameterWidget.class
						.isInstance(affectedWidget)) {
					UTCTimeStampClockParameterWidget utcTimeStampClockParameterWidget = UTCTimeStampClockParameterWidget.class
							.cast(affectedWidget);
					utcTimeStampClockParameterWidget
							.setValue(utcTimeStampClockParameterWidget
									.convertValue(value));
				}
			} catch (XmlException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
