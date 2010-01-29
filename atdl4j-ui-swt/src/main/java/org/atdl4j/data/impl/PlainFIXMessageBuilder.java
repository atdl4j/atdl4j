package org.atdl4j.data.impl;

/*import java.util.HashMap;
 import java.util.List;
 import java.util.Map;*/

import org.atdl4j.data.FIXMessageBuilder;

// TODO: Change to handle generic repeating groups
public class PlainFIXMessageBuilder implements FIXMessageBuilder {

	private static char delimiter = '\001';

	private StringBuffer sb;
	private StringBuffer repeating;
	// private Map<Integer,List<List<StringBuffer>>> repeating;

	private int repeatingCount;

	public void onStart() {
		sb = new StringBuffer();
		repeating = new StringBuffer();
		// repeating = new HashMap<Integer,List<StringBuffer>>();
		repeatingCount = 0;
	}

	public void onField(int field, String value) {
		if (field == 958 || field == 959 || field == 960) {
			// if (repeating.get(957) == null)
			repeating.append(field).append('=').append(value).append(delimiter);
			if (field == 958) {
				repeatingCount++;
			}
		} else {
			sb.append(field).append('=').append(value).append(delimiter);
		}
	}

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
