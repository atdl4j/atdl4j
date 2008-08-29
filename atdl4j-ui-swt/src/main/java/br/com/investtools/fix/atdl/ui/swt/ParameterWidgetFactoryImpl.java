package br.com.investtools.fix.atdl.ui.swt;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.layout.xmlbeans.ComponentT;
import br.com.investtools.fix.atdl.layout.xmlbeans.ComponentT.Enum;
import br.com.investtools.fix.atdl.ui.swt.widget.CheckBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.ClockParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.ComboBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.DualSpinnerParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.LabelParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.MultiCheckBoxParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.NumberTextFieldParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.RadioButtonParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.SingleSpinnerParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.SliderParameterWidget;
import br.com.investtools.fix.atdl.ui.swt.widget.StringTextFieldParameterWidget;

public class ParameterWidgetFactoryImpl implements ParameterWidgetFactory {

	@Override
	public ParameterWidget<?> create(ParameterT parameter) {
		Enum type = parameter.getControlType();
		if (type == ComponentT.CHECK_BOX) {
			return new CheckBoxParameterWidget();
		} else if (type == ComponentT.COMBO_BOX) {
			return new ComboBoxParameterWidget();
		} else if (type == ComponentT.RADIO_BUTTON) {
			return new RadioButtonParameterWidget();
		} else if (type == ComponentT.LABEL) {
			return new LabelParameterWidget();
		} else if (type == ComponentT.TEXT_FIELD) {
			switch (parameter.getType()) {
			case 1:
				return new NumberTextFieldParameterWidget();
			case 14:
				return new StringTextFieldParameterWidget();
			}
		} else if (type == ComponentT.SLIDER) {
			return new SliderParameterWidget();
		} else if (type == ComponentT.MULTI_CHECK_BOX) {
			return new MultiCheckBoxParameterWidget();
		} else if (type == ComponentT.CLOCK) {
			return new ClockParameterWidget();
		} else if (type == ComponentT.SINGLE_SPINNER) {
			return new SingleSpinnerParameterWidget();
		} else if (type == ComponentT.DUAL_SPINNER) {
			return new DualSpinnerParameterWidget();
		} 
 

		// TODO: todos os outros componentes
		return null;
	}

}
