package org.atdl4j.ui.swt.impl;

import javax.xml.bind.JAXBException;

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

import org.fixprotocol.atdl_1_1.core.IntT;
import org.fixprotocol.atdl_1_1.core.LengthT;
import org.fixprotocol.atdl_1_1.core.LocalMktTimeT;
import org.fixprotocol.atdl_1_1.core.MonthYearT;
import org.fixprotocol.atdl_1_1.core.NumInGroupT;
import org.fixprotocol.atdl_1_1.core.NumericT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.core.SeqNumT;
import org.fixprotocol.atdl_1_1.core.TagNumT;
import org.fixprotocol.atdl_1_1.core.UTCDateT;
import org.fixprotocol.atdl_1_1.core.UTCTimeOnlyT;
import org.fixprotocol.atdl_1_1.core.UTCTimeStampT;
import org.fixprotocol.atdl_1_1.core.MultipleCharValueT;
import org.fixprotocol.atdl_1_1.core.MultipleStringValueT;
import org.fixprotocol.atdl_1_1.layout.ControlT;
import org.fixprotocol.atdl_1_1.layout.CheckBoxT;
import org.fixprotocol.atdl_1_1.layout.DropDownListT;
import org.fixprotocol.atdl_1_1.layout.EditableDropDownListT;
import org.fixprotocol.atdl_1_1.layout.LabelT;
import org.fixprotocol.atdl_1_1.layout.RadioButtonGroupT;
import org.fixprotocol.atdl_1_1.layout.RadioButtonT;
import org.fixprotocol.atdl_1_1.layout.TextFieldT;
import org.fixprotocol.atdl_1_1.layout.SliderT;
import org.fixprotocol.atdl_1_1.layout.CheckBoxListT;
import org.fixprotocol.atdl_1_1.layout.ClockT;
import org.fixprotocol.atdl_1_1.layout.SingleSpinnerT;
import org.fixprotocol.atdl_1_1.layout.DoubleSpinnerT;
import org.fixprotocol.atdl_1_1.layout.SingleSelectListT;
import org.fixprotocol.atdl_1_1.layout.MultiSelectListT;
import org.fixprotocol.atdl_1_1.layout.HiddenFieldT;

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
