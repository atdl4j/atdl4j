package br.com.investtools.fix.atdl.ui.swt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.ui.swt.ParameterWidget;

public class LabelParameterWidget implements ParameterWidget {

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		
		// label
		Label l = new Label(parent, SWT.NONE);
		l.setText(getLabelText(parameter));

		return l;
	}

	public String getLabelText(ParameterT parameter) {
		if (parameter.getUiRep() != null) {
			return parameter.getUiRep();
		}
		return parameter.getName();
	}

	@Override
	public String getFIXValue() {
		return null;
	}

}
