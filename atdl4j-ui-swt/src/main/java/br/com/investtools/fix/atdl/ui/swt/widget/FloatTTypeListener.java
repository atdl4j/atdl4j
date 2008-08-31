package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.NumberFormat;

import org.eclipse.swt.events.VerifyEvent;

public class FloatTTypeListener extends NumberFormatVerifyListener {

	private Float minValue, maxValue;

	public FloatTTypeListener(NumberFormat formatter, boolean allowEmpty,
			Float minValue, Float maxValue) {
		super(formatter, allowEmpty);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public FloatTTypeListener(NumberFormat formatter, boolean allowEmpty) {
		this(formatter, allowEmpty, null, null);
	}

	public void verifyText(VerifyEvent e) {
		super.verifyText(e);

		if (!e.doit) {
			return;
		}

		String futureText = this.getFutureText(e);

		if (!"".equals(futureText)) {

			int decimalSeparator = futureText.indexOf(",");

			int intPart = 0;
			int decPart = 0;
			float value = 0f;
			boolean separator = false;
			boolean fillerDec = false;
			boolean fillerCen = false;

			if (decimalSeparator != -1) {

				separator = true;
				if (decimalSeparator == futureText.length() - 1) {
					futureText += "00";
					fillerDec = true;
				}

				if (decimalSeparator == futureText.length() - 2) {
					futureText += "0";
					fillerCen = true;
				}

				intPart = new Integer(futureText.substring(0, decimalSeparator));
				decPart = new Integer(futureText.substring(
						decimalSeparator + 1, futureText.length() - 1));
				value = new Float(futureText.replace(",", "."));

			} else {
				intPart = new Integer(futureText);
			}

			if (this.minValue != null) {

				String minValueString = minValue.toString();
				int minValueDecimalSeparator = minValueString.indexOf(".");

				if (minValueDecimalSeparator == minValueString.length() - 2)
					minValueString += "0";

				int decimalSeparatorMinValue = minValueString.indexOf(".");
				int minValueIntPart = new Integer(minValueString.substring(0,
						decimalSeparatorMinValue));
				int minValueDecPart = new Integer(minValueString.substring(
						decimalSeparatorMinValue + 1,
						minValueString.length() - 1));
				float minValueCen = new Float(minValueString);

				if (intPart < minValueIntPart) {
					e.doit = false;
					return;
				}

				if (separator) {
					if (!fillerDec) {

						if (decPart < minValueDecPart) {
							e.doit = false;
							return;
						}

						if (!fillerCen) {
							if (value < minValueCen) {
								e.doit = false;
								return;
							}
						}
					}
				}
			}

			if (this.maxValue != null) {

				String maxValueString = maxValue.toString();
				int maxValueDecimalSeparator = maxValueString.indexOf(".");

				if (maxValueDecimalSeparator == maxValueString.length() - 2)
					maxValueString += "0";

				int decimalSeparatorMaxValue = maxValueString.indexOf(".");
				int maxValueIntPart = new Integer(maxValueString.substring(0,
						decimalSeparatorMaxValue));
				int maxValueDecPart = new Integer(maxValueString.substring(
						decimalSeparatorMaxValue + 1,
						maxValueString.length() - 1));
				float maxValue = new Float(maxValueString);

				if (intPart > maxValueIntPart) {
					e.doit = false;
					return;
				}

				if (separator) {
					if (!fillerDec) {

						if (decPart > maxValueDecPart) {
							e.doit = false;
							return;
						}

						if (!fillerCen) {
							if (value > maxValue) {
								e.doit = false;
								return;
							}
						}
					}
				}
			}

		}

	}

}
