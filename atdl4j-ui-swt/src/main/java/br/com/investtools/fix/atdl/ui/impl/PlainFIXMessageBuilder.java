package br.com.investtools.fix.atdl.ui.impl;

import br.com.investtools.fix.atdl.ui.FIXMessageBuilder;

public class PlainFIXMessageBuilder implements FIXMessageBuilder {

	private static char delimiter = '\001';

	private StringBuffer sb;

	private StringBuffer repeating;

	private int repeatingCount;

	@Override
	public void onStart() {
		sb = new StringBuffer();
		repeating = new StringBuffer();
		repeatingCount = 0;
	}

	@Override
	public void onField(int field, String value) {
		if (field == 958 || field == 959 || field == 960) {
			repeating.append(field).append('=').append(value).append(delimiter);
			if (field == 958) {
				repeatingCount++;
			}
		} else {
			sb.append(field).append('=').append(value).append(delimiter);
		}
	}

	@Override
	public void onEnd() {
		// append repeating group count
		sb.append(957).append('=').append(repeatingCount).append(delimiter);
		// append repeating group content
		sb.append(repeating);
	}

	public String getMessage() {
		return sb.toString();
	}

}
