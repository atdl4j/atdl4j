package br.com.investtools.fix.atdl.ui.swt.impl;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.xmlbeans.BooleanT;
import br.com.investtools.fix.atdl.core.xmlbeans.CharT;
import br.com.investtools.fix.atdl.core.xmlbeans.CurrencyT;
import br.com.investtools.fix.atdl.core.xmlbeans.DataT;
import br.com.investtools.fix.atdl.core.xmlbeans.ExchangeT;
import br.com.investtools.fix.atdl.core.xmlbeans.IntT;
import br.com.investtools.fix.atdl.core.xmlbeans.LengthT;
import br.com.investtools.fix.atdl.core.xmlbeans.LocalMktTimeT;
import br.com.investtools.fix.atdl.core.xmlbeans.MonthYearT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleCharValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleStringValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.NumInGroupT;
import br.com.investtools.fix.atdl.core.xmlbeans.NumericT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.SeqNumT;
import br.com.investtools.fix.atdl.core.xmlbeans.StringT;
import br.com.investtools.fix.atdl.core.xmlbeans.TagNumT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCDateT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCTimeOnlyT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCTimeStampT;
import br.com.investtools.fix.atdl.layout.xmlbeans.ComponentT;
import br.com.investtools.fix.atdl.layout.xmlbeans.ComponentT.Enum;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;
import br.com.investtools.fix.atdl.ui.swt.ParameterUIFactory;
import br.com.investtools.fix.atdl.ui.swt.widget.CheckBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.ComboBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.DualSpinnerParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.HiddenParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.LabelParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.LocalMktTimeClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.MonthYearClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.MultiCheckBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.NumberTextFieldParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.RadioButtonParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.SingleSpinnerParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.SliderParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.StringTextFieldParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.UTCDateClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.UTCTimeOnlyClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.UTCTimeStampClockParameterWidget;

/**
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 */
public class ParameterUIFactoryImpl implements ParameterUIFactory {

	@Override
	public ParameterUI<?> create(ParameterT parameter) throws XmlException {
		Enum type = parameter.getControlType();
		if (type != null) {
			// user defined a specific control type
			if (type == ComponentT.CHECK_BOX) {
				return new CheckBoxParameterWidget();
			} else if (type == ComponentT.COMBO_BOX) {
				return new ComboBoxParameterWidget();
			} else if (type == ComponentT.EDITABLE_COMBO_BOX) {
				return new ComboBoxParameterWidget(true);
			} else if (type == ComponentT.RADIO_BUTTON) {
				return new RadioButtonParameterWidget();
			} else if (type == ComponentT.LABEL) {
				return new LabelParameterWidget();
			} else if (type == ComponentT.TEXT_FIELD) {

				if (parameter instanceof CharT) {
					// limited textfield
					return new StringTextFieldParameterWidget();
				} else if (parameter instanceof CurrencyT) {
					return new NumberTextFieldParameterWidget();
				} else if (parameter instanceof DataT) {
					return new StringTextFieldParameterWidget();
				} else if (parameter instanceof ExchangeT) {
					return new NumberTextFieldParameterWidget();
				} else if (parameter instanceof IntT) {
					return new NumberTextFieldParameterWidget();
				} else if (parameter instanceof LengthT) {
					return new StringTextFieldParameterWidget();
				} else if (parameter instanceof MultipleCharValueT) {
					return new StringTextFieldParameterWidget();
				} else if (parameter instanceof MultipleStringValueT) {
					return new StringTextFieldParameterWidget();
				} else if (parameter instanceof NumericT) {
					return new NumberTextFieldParameterWidget();
				} else if (parameter instanceof NumInGroupT) {
					return new NumberTextFieldParameterWidget();
				} else if (parameter instanceof SeqNumT) {
					return new NumberTextFieldParameterWidget();
				} else if (parameter instanceof StringT) {
					return new StringTextFieldParameterWidget();
				} else if (parameter instanceof TagNumT) {
					return new NumberTextFieldParameterWidget();
				}

			} else if (type == ComponentT.SLIDER) {
				return new SliderParameterWidget();
			} else if (type == ComponentT.MULTI_CHECK_BOX) {
				return new MultiCheckBoxParameterWidget();
			} else if (type == ComponentT.CLOCK) {

				if (parameter instanceof LocalMktTimeT) {
					return new LocalMktTimeClockParameterWidget();
				} else if (parameter instanceof MonthYearT) {
					return new MonthYearClockParameterWidget();
				} else if (parameter instanceof UTCDateT) {
					return new UTCDateClockParameterWidget();
				} else if (parameter instanceof UTCTimeOnlyT) {
					return new UTCTimeOnlyClockParameterWidget();
				} else if (parameter instanceof UTCTimeStampT) {
					return new UTCTimeStampClockParameterWidget();
				}

			} else if (type == ComponentT.SINGLE_SPINNER) {
				return new SingleSpinnerParameterWidget();
			} else if (type == ComponentT.DUAL_SPINNER) {
				return new DualSpinnerParameterWidget();
			}
		} else {
			// infer widget type from parameter type

			if (parameter instanceof BooleanT) {
				return new CheckBoxParameterWidget();
			} else if (parameter instanceof CharT) {
				// limited textfield
				return new StringTextFieldParameterWidget();
			} else if (parameter instanceof CurrencyT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof DataT) {
				return new StringTextFieldParameterWidget();
			} else if (parameter instanceof ExchangeT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof IntT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof LengthT) {
				return new StringTextFieldParameterWidget();
			} else if (parameter instanceof LocalMktTimeT) {
				return new LocalMktTimeClockParameterWidget();
			} else if (parameter instanceof MonthYearT) {
				return new MonthYearClockParameterWidget();
			} else if (parameter instanceof MultipleCharValueT) {
				return new StringTextFieldParameterWidget();
			} else if (parameter instanceof MultipleStringValueT) {
				return new StringTextFieldParameterWidget();
			} else if (parameter instanceof NumericT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof NumInGroupT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof SeqNumT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof StringT) {
				return new StringTextFieldParameterWidget();
			} else if (parameter instanceof TagNumT) {
				return new SingleSpinnerParameterWidget();
			} else if (parameter instanceof UTCDateT) {
				return new UTCDateClockParameterWidget();
			} else if (parameter instanceof UTCTimeOnlyT) {
				return new UTCTimeOnlyClockParameterWidget();
			} else if (parameter instanceof UTCTimeStampT) {
				return new UTCTimeStampClockParameterWidget();
			} else if (parameter.getConst()) {
				return new HiddenParameterWidget(parameter);
			}

		}

		throw new XmlException("Unsupported control type: " + type
				+ " for ParameterT: " + parameter.getName());

	}

}
