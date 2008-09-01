package br.com.investtools.fix.atdl.ui.swt.widget;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class HiddenParameterWidget implements ParameterWidget<String> {

	private ParameterT parameter;

	private String value;

	public HiddenParameterWidget(ParameterT parameter) {
		this.parameter = parameter;
		// TODO later: get value from parameter sub-type
		this.value = "";
	}

	@Override
	public String convertValue(String value) {
		return value;
	}

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		return null;
	}

	@Override
	public String getFIXValue() {
		// TODO later define fix value for Hidden Widget
		return null;
	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public String getValue() {
		return value;
	}

}
