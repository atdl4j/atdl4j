package br.com.investtools.fix.atdl.ui.swt.widget;

import java.text.NumberFormat;

import org.eclipse.swt.events.VerifyEvent;

public class IntTTypeListener extends NumberFormatVerifyListener {

	private Integer minValue, maxValue;

	public IntTTypeListener(NumberFormat formatter, boolean allowEmpty,
			Integer minValue, Integer maxValue) {
		super(formatter, allowEmpty);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public void verifyText(VerifyEvent e) {
		super.verifyText(e);

		if (!"".equals(this.getFutureText(e))) {
			int value = new Integer(this.getFutureText(e));

			if (this.minValue != null) {
				if (value < minValue)
					e.doit = false;
			}

			if (this.maxValue != null) {
				if (value > maxValue)
					e.doit = false;
			}

		}

	}

}
