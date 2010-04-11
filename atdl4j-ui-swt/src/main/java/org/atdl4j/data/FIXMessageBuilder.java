package org.atdl4j.data;

/* Comment - testing commit */

public interface FIXMessageBuilder {

	public void onStart();

	public void onField(int field, String value);

	public void onEnd();

}
