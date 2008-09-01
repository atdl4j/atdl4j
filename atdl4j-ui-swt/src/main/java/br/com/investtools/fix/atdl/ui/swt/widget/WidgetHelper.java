package br.com.investtools.fix.atdl.ui.swt.widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;

/**
 * Utility class.
 * 
 */
public abstract class WidgetHelper {

	public static String getLabelText(ParameterT parameter) {
		if (parameter.getUiRep() != null) {
			return parameter.getUiRep();
		}
		return parameter.getName();
	}
}
