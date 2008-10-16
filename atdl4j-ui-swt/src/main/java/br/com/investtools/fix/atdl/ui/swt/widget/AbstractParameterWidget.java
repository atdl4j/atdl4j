package br.com.investtools.fix.atdl.ui.swt.widget;

import br.com.investtools.fix.atdl.ui.FIXMessageBuilder;
import br.com.investtools.fix.atdl.ui.impl.PlainFIXMessageBuilder;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;

/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 * 
 * @author renato.gallart
 * 
 */
public abstract class AbstractParameterWidget<E extends Comparable<E>>
		implements ParameterUI<E> {

	public String getFIXValue() {
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		builder.onStart();
		getFIXValue(builder);
		builder.onEnd();
		return builder.getMessage();
	}

	public void getFIXValue(FIXMessageBuilder builder) {
		String value = getValueAsString();
		if (value != null) {
			if (getParameter().getFixTag() != null) {
				builder.onField(getParameter().getFixTag().intValue(), value
						.toString());
			} else {
				String name = getParameter().getName();
				String type = Integer.toString(getParameter().getType());
				builder.onField(958, name);
				builder.onField(959, type);
				builder.onField(960, value.toString());
			}
		}
	}

}
