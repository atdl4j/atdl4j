package br.com.investtools.fix.atdl.ui.swt.util;

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

	public static String invertOnWire(String text) {
		StringBuffer invertedString = new StringBuffer();

		int startIndex = text.lastIndexOf(" ");
		int endIndex = text.length();

		do {
			invertedString.append(text.substring(startIndex + 1, endIndex));
			if (startIndex == -1) {
				return invertedString.toString();
			} else {
				invertedString.append(" ");
			}
			endIndex = startIndex;
			startIndex = (text.substring(0, endIndex)).lastIndexOf(" ");
		} while (endIndex != -1);

		return invertedString.toString();
	}

}
