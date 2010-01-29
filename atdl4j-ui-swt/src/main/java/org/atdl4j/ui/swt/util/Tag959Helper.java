package org.atdl4j.ui.swt.util;

import org.fixprotocol.atdl_1_1.core.AmtT;
import org.fixprotocol.atdl_1_1.core.BooleanT;
import org.fixprotocol.atdl_1_1.core.CharT;
import org.fixprotocol.atdl_1_1.core.CurrencyT;
import org.fixprotocol.atdl_1_1.core.DataT;
import org.fixprotocol.atdl_1_1.core.ExchangeT;
import org.fixprotocol.atdl_1_1.core.FloatT;
import org.fixprotocol.atdl_1_1.core.IntT;
import org.fixprotocol.atdl_1_1.core.LengthT;
import org.fixprotocol.atdl_1_1.core.LocalMktTimeT;
import org.fixprotocol.atdl_1_1.core.MonthYearT;
import org.fixprotocol.atdl_1_1.core.MultipleCharValueT;
import org.fixprotocol.atdl_1_1.core.MultipleStringValueT;
import org.fixprotocol.atdl_1_1.core.NumInGroupT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.core.PercentageT;
import org.fixprotocol.atdl_1_1.core.PriceT;
import org.fixprotocol.atdl_1_1.core.PriceOffsetT;
import org.fixprotocol.atdl_1_1.core.QtyT;
import org.fixprotocol.atdl_1_1.core.SeqNumT;
import org.fixprotocol.atdl_1_1.core.StringT;
import org.fixprotocol.atdl_1_1.core.TagNumT;
import org.fixprotocol.atdl_1_1.core.UTCDateT;
import org.fixprotocol.atdl_1_1.core.UTCTimeOnlyT;
import org.fixprotocol.atdl_1_1.core.UTCTimeStampT;

import javax.xml.bind.JAXBException;

/**
 * Utility class.
 */
public abstract class Tag959Helper {

	public static int toInteger(ParameterT parameter) throws JAXBException 
	{
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
		} throw new JAXBException("Unsupported Parameter type.");
	}
}
