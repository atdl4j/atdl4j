package br.com.investtools.fix.atdl.ui;

public interface FIXMessageBuilder {

	public void onStart();

	public void onField(int field, String value);

	public void onEnd();

}
