package org.atdl4j.data;

import java.math.BigInteger;

import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.fixatdl.core.AmtT;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.core.CharT;
import org.atdl4j.fixatdl.core.CountryT;
import org.atdl4j.fixatdl.core.CurrencyT;
import org.atdl4j.fixatdl.core.DataT;
import org.atdl4j.fixatdl.core.ExchangeT;
import org.atdl4j.fixatdl.core.FloatT;
import org.atdl4j.fixatdl.core.IntT;
import org.atdl4j.fixatdl.core.LanguageT;
import org.atdl4j.fixatdl.core.LengthT;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.NumInGroupT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.PercentageT;
import org.atdl4j.fixatdl.core.PriceOffsetT;
import org.atdl4j.fixatdl.core.PriceT;
import org.atdl4j.fixatdl.core.QtyT;
import org.atdl4j.fixatdl.core.SeqNumT;
import org.atdl4j.fixatdl.core.StringT;
import org.atdl4j.fixatdl.core.TZTimeOnlyT;
import org.atdl4j.fixatdl.core.TZTimestampT;
import org.atdl4j.fixatdl.core.TagNumT;
import org.atdl4j.fixatdl.core.TenorT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.timezones.Timezone;

public class ParameterHelper
{
	public static Object getConstValue(ParameterT parameter)
	{
		if ( parameter instanceof IntT )
		{
			return ( (IntT) parameter ).getConstValue();
		}
		else if ( parameter instanceof LengthT )
		{
			return ( (LengthT) parameter ).getConstValue();
		}
		else if ( parameter instanceof NumInGroupT )
		{
			return ( (NumInGroupT) parameter ).getConstValue();
		}
		else if ( parameter instanceof SeqNumT )
		{
			return ( (SeqNumT) parameter ).getConstValue();
		}
		else if ( parameter instanceof TagNumT )
		{
			return ( (TagNumT) parameter ).getConstValue();
		}
		else if ( parameter instanceof FloatT )
		{
			return ( (FloatT) parameter ).getConstValue();
		}
		else if ( parameter instanceof QtyT )
		{
			return ( (QtyT) parameter ).getConstValue();
		}
		else if ( parameter instanceof PriceT )
		{
			return ( (PriceT) parameter ).getConstValue();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) parameter ).getConstValue();
		}
		else if ( parameter instanceof AmtT )
		{
			return ( (AmtT) parameter ).getConstValue();
		}
		else if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).getConstValue();
		}
		else if ( parameter instanceof CharT )
		{
			return ( (CharT) parameter ).getConstValue();
		}
		else if ( parameter instanceof BooleanT )
		{
			return ( (BooleanT) parameter ).getConstValue();
		}
		else if ( parameter instanceof StringT )
		{
			return ( (StringT) parameter ).getConstValue();
		}
		else if ( parameter instanceof MultipleCharValueT )
		{
			return ( (MultipleCharValueT) parameter ).getConstValue();
		}
		else if ( parameter instanceof CurrencyT )
		{
			return ( (CurrencyT) parameter ).getConstValue();
		}
		else if ( parameter instanceof ExchangeT )
		{
			return ( (ExchangeT) parameter ).getConstValue();
		}
		else if ( parameter instanceof MonthYearT )
		{
			return ( (MonthYearT) parameter ).getConstValue();
		}
		else if ( parameter instanceof UTCTimestampT )
		{
			if ( parameter != null )
			{
				return DateTimeConverter.convertDailyValueToValue( ( (UTCTimestampT) parameter ).getConstValue(), getLocalMktTz( parameter) );
			}
		}
		else if ( parameter instanceof UTCTimeOnlyT )
		{
			return ( (UTCTimeOnlyT) parameter ).getConstValue();
		}
		else if ( parameter instanceof LocalMktDateT )
		{
			return ( (LocalMktDateT) parameter ).getConstValue();
		}
		else if ( parameter instanceof UTCDateOnlyT )
		{
			return ( (UTCDateOnlyT) parameter ).getConstValue();
		}
		else if ( parameter instanceof DataT )
		{
			return ( (DataT) parameter ).getConstValue();
		}
		else if ( parameter instanceof MultipleStringValueT )
		{
			return ( (MultipleStringValueT) parameter ).getConstValue();
		}
		else if (parameter instanceof CountryT) 
		{ 
			return ((CountryT)parameter).getConstValue(); 
		}
		else if ( parameter instanceof LanguageT )
		{
			return ( (LanguageT) parameter ).getConstValue();
		}
		else if ( parameter instanceof TZTimeOnlyT )
		{
			return ( (TZTimeOnlyT) parameter ).getConstValue();
		}
		else if ( parameter instanceof TZTimestampT )
		{
			if ( parameter != null )
			{
				return DateTimeConverter.convertDailyValueToValue( ( (TZTimestampT) parameter ).getConstValue(), null );
			}
		}
		else if ( parameter instanceof TenorT )
		{
			return ( (TenorT) parameter ).getConstValue();
		}
		return null;
	}
	
	
	public static Object getMaxValue(ParameterT parameter)
	{
		if ( parameter instanceof IntT )
		{
			return ( (IntT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof LengthT )
		{
//			return ( (LengthT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof NumInGroupT )
		{
//			return ( (NumInGroupT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof SeqNumT )
		{
//			return ( (SeqNumT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof TagNumT )
		{
//			return ( (TagNumT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof FloatT )
		{
			return ( (FloatT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof QtyT )
		{
			return ( (QtyT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof PriceT )
		{
			return ( (PriceT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof AmtT )
		{
			return ( (AmtT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof CharT )
		{
//			return ( (CharT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof BooleanT )
		{
//			return ( (BooleanT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof StringT )
		{
//			return ( (StringT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof MultipleCharValueT )
		{
//			return ( (MultipleCharValueT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof CurrencyT )
		{
//			return ( (CurrencyT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof ExchangeT )
		{
//			return ( (ExchangeT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof MonthYearT )
		{
			return ( (MonthYearT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof UTCTimestampT )
		{
			if ( parameter != null )
			{
				return DateTimeConverter.convertDailyValueToValue( ( (UTCTimestampT) parameter ).getMaxValue(), getLocalMktTz( parameter) );
			}
		}
		else if ( parameter instanceof UTCTimeOnlyT )
		{
			return ( (UTCTimeOnlyT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof LocalMktDateT )
		{
			return ( (LocalMktDateT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof UTCDateOnlyT )
		{
			return ( (UTCDateOnlyT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof DataT )
		{
//			return ( (DataT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof MultipleStringValueT )
		{
//			return ( (MultipleStringValueT) parameter ).getMaxValue();
		}
		else if (parameter instanceof CountryT) 
		{ 
//			return ( (CountryT) parameter).getMaxValue(); 
		}
		else if ( parameter instanceof LanguageT )
		{
//			return ( (LanguageT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof TZTimeOnlyT )
		{
			return ( (TZTimeOnlyT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof TZTimestampT )
		{
			if ( parameter != null )
			{
				return DateTimeConverter.convertDailyValueToValue( ( (TZTimestampT) parameter ).getMaxValue(), null );
			}
		}
		else if ( parameter instanceof TenorT )
		{
//			return ( (TenorT) parameter ).getMaxValue();
		}
		
		return null;
	}	
	
	
	public static Object getMinValue(ParameterT parameter)
	{
		if ( parameter instanceof IntT )
		{
			return ( (IntT) parameter ).getMinValue();
		}
		else if ( parameter instanceof LengthT )
		{
//			return ( (LengthT) parameter ).getMinValue();
		}
		else if ( parameter instanceof NumInGroupT )
		{
//			return ( (NumInGroupT) parameter ).getMinValue();
		}
		else if ( parameter instanceof SeqNumT )
		{
//			return ( (SeqNumT) parameter ).getMinValue();
		}
		else if ( parameter instanceof TagNumT )
		{
//			return ( (TagNumT) parameter ).getMinValue();
		}
		else if ( parameter instanceof FloatT )
		{
			return ( (FloatT) parameter ).getMinValue();
		}
		else if ( parameter instanceof QtyT )
		{
			return ( (QtyT) parameter ).getMinValue();
		}
		else if ( parameter instanceof PriceT )
		{
			return ( (PriceT) parameter ).getMinValue();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) parameter ).getMinValue();
		}
		else if ( parameter instanceof AmtT )
		{
			return ( (AmtT) parameter ).getMinValue();
		}
		else if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).getMinValue();
		}
		else if ( parameter instanceof CharT )
		{
//			return ( (CharT) parameter ).getMinValue();
		}
		else if ( parameter instanceof BooleanT )
		{
//			return ( (BooleanT) parameter ).getMinValue();
		}
		else if ( parameter instanceof StringT )
		{
//			return ( (StringT) parameter ).getMinValue();
		}
		else if ( parameter instanceof MultipleCharValueT )
		{
//			return ( (MultipleCharValueT) parameter ).getMinValue();
		}
		else if ( parameter instanceof CurrencyT )
		{
//			return ( (CurrencyT) parameter ).getMinValue();
		}
		else if ( parameter instanceof ExchangeT )
		{
//			return ( (ExchangeT) parameter ).getMinValue();
		}
		else if ( parameter instanceof MonthYearT )
		{
			return ( (MonthYearT) parameter ).getMinValue();
		}
		else if ( parameter instanceof UTCTimestampT )
		{
			if ( parameter != null )
			{
				return DateTimeConverter.convertDailyValueToValue( ( (UTCTimestampT) parameter ).getMinValue(), getLocalMktTz( parameter) );
			}
		}
		else if ( parameter instanceof UTCTimeOnlyT )
		{
			return ( (UTCTimeOnlyT) parameter ).getMinValue();
		}
		else if ( parameter instanceof LocalMktDateT )
		{
			return ( (LocalMktDateT) parameter ).getMinValue();
		}
		else if ( parameter instanceof UTCDateOnlyT )
		{
			return ( (UTCDateOnlyT) parameter ).getMinValue();
		}
		else if ( parameter instanceof DataT )
		{
//			return ( (DataT) parameter ).getMinValue();
		}
		else if ( parameter instanceof MultipleStringValueT )
		{
//			return ( (MultipleStringValueT) parameter ).getMinValue();
		}
		else if (parameter instanceof CountryT) 
		{ 
//			return ( (CountryT) parameter).getMinValue(); 
		}
		else if ( parameter instanceof LanguageT )
		{
//			return ( (LanguageT) parameter ).getMinValue();
		}
		else if ( parameter instanceof TZTimeOnlyT )
		{
			return ( (TZTimeOnlyT) parameter ).getMinValue();
		}
		else if ( parameter instanceof TZTimestampT )
		{
			if ( parameter != null )
			{
				return DateTimeConverter.convertDailyValueToValue( ( (TZTimestampT) parameter ).getMinValue(), null );
			}
		}
		else if ( parameter instanceof TenorT )
		{
//			return ( (TenorT) parameter ).getMinValue();
		}
		
		return null;
	}	
	
	public static Timezone getLocalMktTz( ParameterT parameter )
	{
		if ( parameter instanceof UTCTimestampT )
		{
			return ( (UTCTimestampT) parameter ).getLocalMktTz();
		}
		else if ( parameter instanceof UTCTimeOnlyT )
		{
			return ( (UTCTimeOnlyT) parameter ).getLocalMktTz();
		}
		
		return null;
	}
	
	
	public static BigInteger getMaxLength(ParameterT parameter)
	{
		if ( parameter instanceof IntT )
		{
//			return ( (IntT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof LengthT )
		{
//			return ( (LengthT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof NumInGroupT )
		{
//			return ( (NumInGroupT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof SeqNumT )
		{
//			return ( (SeqNumT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof TagNumT )
		{
//			return ( (TagNumT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof FloatT )
		{
//			return ( (FloatT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof QtyT )
		{
//			return ( (QtyT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof PriceT )
		{
//			return ( (PriceT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
//			return ( (PriceOffsetT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof AmtT )
		{
//			return ( (AmtT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof PercentageT )
		{
//			return ( (PercentageT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof CharT )
		{
//			return ( (CharT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof BooleanT )
		{
//			return ( (BooleanT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof StringT )
		{
			return ( (StringT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof MultipleCharValueT )
		{
			return ( (MultipleCharValueT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof CurrencyT )
		{
//			return ( (CurrencyT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof ExchangeT )
		{
//			return ( (ExchangeT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof MonthYearT )
		{
//			return ( (MonthYearT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof UTCTimestampT )
		{
//			return ( (UTCTimestampT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof UTCTimeOnlyT )
		{
//			return ( (UTCTimeOnlyT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof LocalMktDateT )
		{
//			return ( (LocalMktDateT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof UTCDateOnlyT )
		{
//			return ( (UTCDateOnlyT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof DataT )
		{
			return ( (DataT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof MultipleStringValueT )
		{
			return ( (MultipleStringValueT) parameter ).getMaxLength();
		}
		else if (parameter instanceof CountryT) 
		{ 
//			return ( (CountryT) parameter).getMaxLength(); 
		}
		else if ( parameter instanceof LanguageT )
		{
//			return ( (LanguageT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof TZTimeOnlyT )
		{
//			return ( (TZTimeOnlyT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof TZTimestampT )
		{
//			return ( (TZTimestampT) parameter ).getMaxLength();
		}
		else if ( parameter instanceof TenorT )
		{
//			return ( (TenorT) parameter ).getMaxLength();
		}
		
		return null;
	}	
	
	
	public static BigInteger getMinLength(ParameterT parameter)
	{
		if ( parameter instanceof IntT )
		{
//			return ( (IntT) parameter ).getMinLength();
		}
		else if ( parameter instanceof LengthT )
		{
//			return ( (LengthT) parameter ).getMinLength();
		}
		else if ( parameter instanceof NumInGroupT )
		{
//			return ( (NumInGroupT) parameter ).getMinLength();
		}
		else if ( parameter instanceof SeqNumT )
		{
//			return ( (SeqNumT) parameter ).getMinLength();
		}
		else if ( parameter instanceof TagNumT )
		{
//			return ( (TagNumT) parameter ).getMinLength();
		}
		else if ( parameter instanceof FloatT )
		{
//			return ( (FloatT) parameter ).getMinLength();
		}
		else if ( parameter instanceof QtyT )
		{
//			return ( (QtyT) parameter ).getMinLength();
		}
		else if ( parameter instanceof PriceT )
		{
//			return ( (PriceT) parameter ).getMinLength();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
//			return ( (PriceOffsetT) parameter ).getMinLength();
		}
		else if ( parameter instanceof AmtT )
		{
//			return ( (AmtT) parameter ).getMinLength();
		}
		else if ( parameter instanceof PercentageT )
		{
//			return ( (PercentageT) parameter ).getMinLength();
		}
		else if ( parameter instanceof CharT )
		{
//			return ( (CharT) parameter ).getMinLength();
		}
		else if ( parameter instanceof BooleanT )
		{
//			return ( (BooleanT) parameter ).getMinLength();
		}
		else if ( parameter instanceof StringT )
		{
			return ( (StringT) parameter ).getMinLength();
		}
		else if ( parameter instanceof MultipleCharValueT )
		{
			return ( (MultipleCharValueT) parameter ).getMinLength();
		}
		else if ( parameter instanceof CurrencyT )
		{
//			return ( (CurrencyT) parameter ).getMinLength();
		}
		else if ( parameter instanceof ExchangeT )
		{
//			return ( (ExchangeT) parameter ).getMinLength();
		}
		else if ( parameter instanceof MonthYearT )
		{
//			return ( (MonthYearT) parameter ).getMinLength();
		}
		else if ( parameter instanceof UTCTimestampT )
		{
//			return ( (UTCTimestampT) parameter ).getMinLength();
		}
		else if ( parameter instanceof UTCTimeOnlyT )
		{
//			return ( (UTCTimeOnlyT) parameter ).getMinLength();
		}
		else if ( parameter instanceof LocalMktDateT )
		{
//			return ( (LocalMktDateT) parameter ).getMinLength();
		}
		else if ( parameter instanceof UTCDateOnlyT )
		{
//			return ( (UTCDateOnlyT) parameter ).getMinLength();
		}
		else if ( parameter instanceof DataT )
		{
			return ( (DataT) parameter ).getMinLength();
		}
		else if ( parameter instanceof MultipleStringValueT )
		{
			return ( (MultipleStringValueT) parameter ).getMinLength();
		}
		else if (parameter instanceof CountryT) 
		{ 
//			return ( (CountryT) parameter).getMinLength(); 
		}
		else if ( parameter instanceof LanguageT )
		{
//			return ( (LanguageT) parameter ).getMinLength();
		}
		else if ( parameter instanceof TZTimeOnlyT )
		{
//			return ( (TZTimeOnlyT) parameter ).getMinLength();
		}
		else if ( parameter instanceof TZTimestampT )
		{
//			return ( (TZTimestampT) parameter ).getMinLength();
		}
		else if ( parameter instanceof TenorT )
		{
//			return ( (TenorT) parameter ).getMinLength();
		}
		
		return null;
	}	
}
