package org.atdl4j.ui.swt.impl;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.IntT;
import org.atdl4j.atdl.core.LengthT;
import org.atdl4j.atdl.core.LocalMktTimeT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.MultipleCharValueT;
import org.atdl4j.atdl.core.MultipleStringValueT;
import org.atdl4j.atdl.core.NumInGroupT;
import org.atdl4j.atdl.core.NumericT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.SeqNumT;
import org.atdl4j.atdl.core.TagNumT;
import org.atdl4j.atdl.core.UTCDateT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimeStampT;
import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.ClockT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.DoubleSpinnerT;
import org.atdl4j.atdl.layout.DropDownListT;
import org.atdl4j.atdl.layout.EditableDropDownListT;
import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.atdl.layout.LabelT;
import org.atdl4j.atdl.layout.MultiSelectListT;
import org.atdl4j.atdl.layout.RadioButtonGroupT;
import org.atdl4j.atdl.layout.RadioButtonT;
import org.atdl4j.atdl.layout.SingleSelectListT;
import org.atdl4j.atdl.layout.SingleSpinnerT;
import org.atdl4j.atdl.layout.SliderT;
import org.atdl4j.atdl.layout.TextFieldT;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.widget.ButtonWidget;
import org.atdl4j.ui.swt.widget.CheckBoxListWidget;
import org.atdl4j.ui.swt.widget.ClockWidget;
import org.atdl4j.ui.swt.widget.DropDownListWidget;
import org.atdl4j.ui.swt.widget.HiddenFieldWidget;
import org.atdl4j.ui.swt.widget.LabelWidget;
import org.atdl4j.ui.swt.widget.ListBoxWidget;
import org.atdl4j.ui.swt.widget.SpinnerWidget;
import org.atdl4j.ui.swt.widget.TextFieldWidget;
import org.atdl4j.ui.swt.widget.RadioButtonGroupWidget;
import org.atdl4j.ui.swt.widget.SliderWidget;


/**
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.
 * 
 */
public class SWTControlUIFactory {

	public SWTWidget<?> create(ControlT control, ParameterT parameter) throws JAXBException {

		if (control instanceof CheckBoxT) {
			return new ButtonWidget((CheckBoxT)control, parameter);
		} else if (control instanceof DropDownListT) {
			return new DropDownListWidget((DropDownListT)control, parameter);
		} else if (control instanceof EditableDropDownListT) {
			return new DropDownListWidget((EditableDropDownListT)control, parameter);
		} else if (control instanceof RadioButtonGroupT) {
			return new RadioButtonGroupWidget((RadioButtonGroupT)control, parameter);
		} else if (control instanceof TextFieldT) {
			return new TextFieldWidget((TextFieldT)control, parameter);
		} else if (control instanceof SliderT) {
			return new SliderWidget((SliderT)control, parameter);
		} else if (control instanceof CheckBoxListT) {
			// CheckBoxList must use a multiple value parameter
			if (parameter == null || 
				parameter instanceof MultipleStringValueT ||
				parameter instanceof MultipleCharValueT)
				return new CheckBoxListWidget((CheckBoxListT)control, parameter);
		} else if (control instanceof ClockT) {
			if (parameter == null ||
				parameter instanceof LocalMktTimeT ||
				parameter instanceof MonthYearT ||
				parameter instanceof UTCDateT ||
				parameter instanceof UTCTimeOnlyT ||
				parameter instanceof UTCTimeStampT) { // support StringT as well...
				return new ClockWidget((ClockT)control, parameter);
			}
			/*
			if (parameter == null) {
					return new UTCTimeStampClockWidget((ClockT)control, parameter);	
			} else if (parameter instanceof LocalMktTimeT) {
				return new LocalMktTimeClockWidget((ClockT)control, (LocalMktTimeT)parameter);
			} else if (parameter instanceof MonthYearT) {
				return new MonthYearClockWidget((ClockT)control, (MonthYearT)parameter);
			} else if (parameter instanceof UTCDateT) {
				return new UTCDateClockWidget((ClockT)control, (UTCDateT)parameter);
			} else if (parameter instanceof UTCTimeOnlyT) {
				return new UTCTimeOnlyClockWidget((ClockT)control, (UTCTimeOnlyT)parameter);
			} else if (parameter instanceof UTCTimeStampT) {
				return new UTCTimeStampClockWidget((ClockT)control, (UTCTimeStampT)parameter);
			}		*/
		} else if (control instanceof SingleSpinnerT) {
			// SingleSpinner must use a number parameter
			if (parameter == null || 
					parameter instanceof IntT ||
					parameter instanceof TagNumT ||
					parameter instanceof LengthT ||	
					parameter instanceof SeqNumT ||
					parameter instanceof NumInGroupT ||
					parameter instanceof NumericT)
			return new SpinnerWidget((SingleSpinnerT)control, parameter);
		} else if (control instanceof DoubleSpinnerT) {
			// DoubleSpinner must use a number parameter
			if (parameter == null || 
					parameter instanceof IntT ||
					parameter instanceof TagNumT ||
					parameter instanceof LengthT ||	
					parameter instanceof SeqNumT ||
					parameter instanceof NumInGroupT ||
					parameter instanceof NumericT)
			return new SpinnerWidget((DoubleSpinnerT)control, parameter);
		} else if (control instanceof SingleSelectListT) {
			return new ListBoxWidget((SingleSelectListT)control, parameter);
		} else if (control instanceof MultiSelectListT) {
			// MultiSelectList must use a multiple value parameter
			if (parameter == null || 
				parameter instanceof MultipleStringValueT ||
				parameter instanceof MultipleCharValueT)
			return new ListBoxWidget((MultiSelectListT)control, parameter);
		} else if (control instanceof HiddenFieldT) {
			return new HiddenFieldWidget((HiddenFieldT)control, parameter);
		} else if (control instanceof LabelT) {
			return new LabelWidget((LabelT)control);
		} else if (control instanceof RadioButtonT) {
			return new ButtonWidget((RadioButtonT)control, parameter);
		}
		
		throw new JAXBException("Control ID: \"" + control.getID() + "\" has unsupported Control type \"" + control.getClass().getSimpleName()
				+ "\"" + (parameter == null? "" : " for Parameter type \"" + parameter.getClass().getSimpleName() + "\"")				);

	}

}
