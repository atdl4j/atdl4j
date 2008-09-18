package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class LabelParameterWidget implements ParameterWidget<String> {

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {

		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(WidgetHelper.getLabelText(parameter));

		// make it span two columns because there is no input control
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);

		return parent;
	}

	@Override
	public String getFIXValue() {
		return null;
	}

	@Override
	public String convertValue(String value) {
		return null;
	}

	@Override
	public ParameterT getParameter() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
	}

	@Override
	public List<Control> getControls() {
		return null;
	}
	

}
