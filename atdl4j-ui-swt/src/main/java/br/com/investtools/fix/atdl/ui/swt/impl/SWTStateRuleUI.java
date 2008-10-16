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

				if (affectedWidget instanceof CheckBoxListParameterWidget) {
					CheckBoxListParameterWidget checkBoxListParameterWidget = (CheckBoxListParameterWidget) affectedWidget;
					checkBoxListParameterWidget.setValue(value);
				} else if (affectedWidget instanceof CheckBoxParameterWidget) {
					CheckBoxParameterWidget checkBoxParameterWidget = (CheckBoxParameterWidget) affectedWidget;
					checkBoxParameterWidget.setValue(checkBoxParameterWidget
							.convertValue(value));
				} else if (affectedWidget instanceof DropDownListParameterWidget) {
					DropDownListParameterWidget dropDownListParameterWidget = (DropDownListParameterWidget) affectedWidget;
					dropDownListParameterWidget.setValue(value);
				} else if (affectedWidget instanceof DualSpinnerParameterWidget) {
					DualSpinnerParameterWidget dualSpinnerParameterWidget = (DualSpinnerParameterWidget) affectedWidget;
					dualSpinnerParameterWidget
							.setValue(dualSpinnerParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof HiddenParameterWidget) {
					HiddenParameterWidget hiddenParameterWidget = (HiddenParameterWidget) affectedWidget;
					hiddenParameterWidget.setValue(value);
				} else if (affectedWidget instanceof LocalMktTimeClockParameterWidget) {
					LocalMktTimeClockParameterWidget localMktTimeClockParameterWidget = (LocalMktTimeClockParameterWidget) affectedWidget;
					localMktTimeClockParameterWidget
							.setValue(localMktTimeClockParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof MonthYearClockParameterWidget) {
					MonthYearClockParameterWidget monthYearClockParameterWidget = (MonthYearClockParameterWidget) affectedWidget;
					monthYearClockParameterWidget
							.setValue(monthYearClockParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof NumberTextFieldParameterWidget) {
					NumberTextFieldParameterWidget numberTextFieldParameterWidget = (NumberTextFieldParameterWidget) affectedWidget;
					numberTextFieldParameterWidget
							.setValue(numberTextFieldParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof RadioButtonListParameterWidget) {
					RadioButtonListParameterWidget radioButtonListParameterWidget = (RadioButtonListParameterWidget) affectedWidget;
					radioButtonListParameterWidget.setValue(value);
				} else if (affectedWidget instanceof SingleSpinnerParameterWidget) {
					SingleSpinnerParameterWidget singleSpinnerParameterWidget = (SingleSpinnerParameterWidget) affectedWidget;
					singleSpinnerParameterWidget
							.setValue(singleSpinnerParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof SliderParameterWidget) {
					SliderParameterWidget sliderParameterWidget = (SliderParameterWidget) affectedWidget;
					sliderParameterWidget.setValue(sliderParameterWidget
							.convertValue(value));
				} else if (affectedWidget instanceof StringTextFieldParameterWidget) {
					StringTextFieldParameterWidget stringTextFieldParameterWidget = (StringTextFieldParameterWidget) affectedWidget;
					stringTextFieldParameterWidget.setValue(value);
				} else if (affectedWidget instanceof UTCDateClockParameterWidget) {
					UTCDateClockParameterWidget utcDateClockParameterWidget = (UTCDateClockParameterWidget) affectedWidget;
					utcDateClockParameterWidget
							.setValue(utcDateClockParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof UTCTimeOnlyClockParameterWidget) {
					UTCTimeOnlyClockParameterWidget utcTimeOnlyClockParameterWidget = (UTCTimeOnlyClockParameterWidget) affectedWidget;
					utcTimeOnlyClockParameterWidget
							.setValue(utcTimeOnlyClockParameterWidget
									.convertValue(value));
				} else if (affectedWidget instanceof UTCTimeStampClockParameterWidget) {
					UTCTimeStampClockParameterWidget utcTimeStampClockParameterWidget = (UTCTimeStampClockParameterWidget) affectedWidget;
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
