/*
 * $Id: SWTNumberFormatVerifyListener.java,v 1.1 2010/09/30 17:46:24 swl Exp $
 *
 * Copyright 2006 Investtools Tecnologia em Informatica LTDA.
 * Todos os direitos reservados.
 * http://www.investtools.com.br
 */

package org.atdl4j.ui.swt.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Listener class which validates a text field entry based on a Number format.
 * If the entered text is valid, the VerifyEvent is allowed to proceed, otherwise
 * the VerifyEvent is interrupted.
 * 
 * @author tuler
 * 
 */
public class SWTNumberFormatVerifyListener implements VerifyListener {

	protected NumberFormat format;

	protected boolean allowEmpty;

	/**
	 * 
	 * @param formatter
	 *            Format used to validate the text entered by the user.
	 * @param allowEmpty
	 *            Flag which indicates whether an empty field is valid.
	 */
	public SWTNumberFormatVerifyListener(NumberFormat format, boolean allowEmpty) {
		this.format = format;
		this.allowEmpty = allowEmpty;
	}

	public void verifyText(VerifyEvent e) {
		if (e.widget instanceof Text) {
			try {
				String value = getFutureText(e);
				if (value.length() == 0 && allowEmpty) {
					return;
				}

				ParsePosition parsePosition = new ParsePosition(0);
				format.parse(value, parsePosition);
				if (parsePosition.getIndex() < value.length()) {
					// Throw an exception which indicates the text position where the parse failed
					throw new ParseException("Invalid value", parsePosition.getIndex());
				}
			} catch (ParseException e1) {
				e.doit = false;
			}

		}
	}

	public String getFutureText(VerifyEvent e) {
		if (e.widget instanceof Text) {
			Text text = (Text) e.widget;
			String old = text.getText();
			String start = old.substring(0, e.start);
			String end = old.substring(e.end);
			String value = start + e.text + end;
			return value;
		}
		return "";
	}

}
