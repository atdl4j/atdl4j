package org.atdl4j.data;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.AmtT;
import org.atdl4j.atdl.core.BooleanT;
import org.atdl4j.atdl.core.CharT;
import org.atdl4j.atdl.core.CurrencyT;
import org.atdl4j.atdl.core.DataT;
import org.atdl4j.atdl.core.ExchangeT;
import org.atdl4j.atdl.core.FloatT;
import org.atdl4j.atdl.core.IntT;
import org.atdl4j.atdl.core.LanguageT;
import org.atdl4j.atdl.core.LengthT;
import org.atdl4j.atdl.core.LocalMktDateT;
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
import org.atdl4j.atdl.core.TZTimeOnlyT;
import org.atdl4j.atdl.core.TZTimestampT;
import org.atdl4j.atdl.core.TagNumT;
import org.atdl4j.atdl.core.TenorT;
import org.atdl4j.atdl.core.UTCDateOnlyT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimestampT;

public class ParameterHelper
{
    public static Object getConstValue(ParameterT parameter)
	   // throws JAXBException
    {
	if (parameter instanceof IntT)
	{
	    return ((IntT) parameter).getConstValue();
	} else if (parameter instanceof LengthT)
	{
	    return ((LengthT) parameter).getConstValue();
	} else if (parameter instanceof NumInGroupT)
	{
	    return ((NumInGroupT) parameter).getConstValue();
	} else if (parameter instanceof SeqNumT)
	{
	    return ((SeqNumT) parameter).getConstValue();
	} else if (parameter instanceof TagNumT)
	{
	    return ((TagNumT) parameter).getConstValue();
	} else if (parameter instanceof FloatT)
	{
	    return ((FloatT) parameter).getConstValue();
	} else if (parameter instanceof QtyT)
	{
	    return ((QtyT) parameter).getConstValue();
	} else if (parameter instanceof PriceT)
	{
	    return ((PriceT) parameter).getConstValue();
	} else if (parameter instanceof PriceOffsetT)
	{
	    return ((PriceOffsetT) parameter).getConstValue();
	} else if (parameter instanceof AmtT)
	{
	    return ((AmtT) parameter).getConstValue();
	} else if (parameter instanceof PercentageT)
	{
	    return ((PercentageT) parameter).getConstValue();
	} else if (parameter instanceof CharT)
	{
	    return ((CharT) parameter).getConstValue();
	} else if (parameter instanceof BooleanT)
	{
	    return ((BooleanT) parameter).getConstValue();
	} else if (parameter instanceof StringT)
	{
	    return ((StringT) parameter).getConstValue();
	} else if (parameter instanceof MultipleCharValueT)
	{
	    return ((MultipleCharValueT) parameter).getConstValue();
	} else if (parameter instanceof CurrencyT)
	{
	    return ((CurrencyT) parameter).getConstValue();
	} else if (parameter instanceof ExchangeT)
	{
	    return ((ExchangeT) parameter).getConstValue();
	} else if (parameter instanceof MonthYearT)
	{
	    return ((MonthYearT) parameter).getConstValue();
	} else if (parameter instanceof UTCTimestampT)
	{
	    return ((UTCTimestampT) parameter).getConstValue();
	} else if (parameter instanceof UTCTimeOnlyT)
	{
	    return ((UTCTimeOnlyT) parameter).getConstValue();
	} else if (parameter instanceof LocalMktDateT)
	{
	    return ((LocalMktDateT) parameter).getConstValue();
	} else if (parameter instanceof UTCDateOnlyT)
	{
	    return ((UTCDateOnlyT) parameter).getConstValue();
	} else if (parameter instanceof DataT)
	{
	    return ((DataT) parameter).getConstValue();
	} else if (parameter instanceof MultipleStringValueT)
	{
	    return ((MultipleStringValueT) parameter).getConstValue();
	}
	// XXX: Country is not supported in ATDL due to conflict in schema
	/*
	 * else if (parameter instanceof CountryT) { return
	 * ((CountryT)parameter).getConstValue(); }
	 */
	else if (parameter instanceof LanguageT)
	{
	    return ((LanguageT) parameter).getConstValue();
	} else if (parameter instanceof TZTimeOnlyT)
	{
	    return ((TZTimeOnlyT) parameter).getConstValue();
	} else if (parameter instanceof TZTimestampT)
	{
	    return ((TZTimestampT) parameter).getConstValue();
	} else if (parameter instanceof TenorT)
	{
	    return ((TenorT) parameter).getConstValue();
	}
	return null;
	//throw new JAXBException("Unsupported Parameter type.");
    }
}
