package org.atdl4j.ui.swt.util;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.AmtT;
import org.atdl4j.atdl.core.BooleanT;
import org.atdl4j.atdl.core.CharT;
import org.atdl4j.atdl.core.CurrencyT;
import org.atdl4j.atdl.core.DataT;
import org.atdl4j.atdl.core.ExchangeT;
import org.atdl4j.atdl.core.FloatT;
import org.atdl4j.atdl.core.IntT;
import org.atdl4j.atdl.core.LengthT;
import org.atdl4j.atdl.core.LocalMktTimeT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.MultipleCharValueT;
import org.atdl4j.atdl.core.MultipleStringValueT;
import org.atdl4j.atdl.core.NumInGroupT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.PercentageT;
import org.atdl4j.atdl.core.PriceOffsetT;
import org.atdl4j.atdl.core.PriceT;
import org.atdl4j.atdl.core.QtyT;
import org.atdl4j.atdl.core.SeqNumT;
import org.atdl4j.atdl.core.StringT;
import org.atdl4j.atdl.core.TagNumT;
import org.atdl4j.atdl.core.UTCDateT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimeStampT;

/**
 * Utility class.
 */
public abstract class Tag959Helper {

	public static int toInteger(ParameterT parameter) throws JAXBException {
		if (parameter instanceof IntT) {
			return 1;
		} else if (parameter instanceof LengthT) {
			return 2;
		} else if (parameter instanceof NumInGroupT) {
			return 3;
		} else if (parameter instanceof SeqNumT) {
			return 4;
		} else if (parameter instanceof TagNumT) {
			return 5;
		} else if (parameter instanceof FloatT) {
			return 6;
		} else if (parameter instanceof QtyT) {
			return 7;
		} else if (parameter instanceof PriceT) {
			return 8;
		} else if (parameter instanceof PriceOffsetT) {
			return 9;
		} else if (parameter instanceof AmtT) {
			return 10;
		} else if (parameter instanceof PercentageT) {
			return 11;
		} else if (parameter instanceof CharT) {
			return 12;
		} else if (parameter instanceof BooleanT) {
			return 13;
		} else if (parameter instanceof StringT) {
			return 14;
		} else if (parameter instanceof MultipleCharValueT) {
			return 15;
		} else if (parameter instanceof CurrencyT) {
			return 16;
		} else if (parameter instanceof ExchangeT) {
			return 17;
		} else if (parameter instanceof MonthYearT) {
			return 18;
		} else if (parameter instanceof UTCTimeStampT) {
			return 19;
		} else if (parameter instanceof UTCTimeOnlyT) {
			return 20;
		} else if (parameter instanceof LocalMktTimeT) {
			return 21;
		} else if (parameter instanceof UTCDateT) {
			return 22;
		} else if (parameter instanceof DataT) {
			return 23;
		} else if (parameter instanceof MultipleStringValueT) {
			return 24;
		}
		throw new JAXBException("Unsupported Parameter type.");
	}
}
