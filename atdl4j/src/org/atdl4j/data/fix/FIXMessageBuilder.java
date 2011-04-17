package org.atdl4j.data.fix;

public interface FIXMessageBuilder {

	public void onStart();

	public void onField(int field, String value);

	public void onEnd();

}
