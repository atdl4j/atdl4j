package org.atdl4j.data;

import javax.xml.bind.JAXBException;

import org.atdl4j.data.TypeConverter;
import org.atdl4j.data.converter.BooleanConverter;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.data.converter.NumberConverter;
import org.atdl4j.data.converter.StringConverter;
import org.fixprotocol.atdl_1_1.core.BooleanT;
import org.fixprotocol.atdl_1_1.core.CharT;
import org.fixprotocol.atdl_1_1.core.CurrencyT;
import org.fixprotocol.atdl_1_1.core.DataT;
import org.fixprotocol.atdl_1_1.core.ExchangeT;
import org.fixprotocol.atdl_1_1.core.IntT;
import org.fixprotocol.atdl_1_1.core.LengthT;
import org.fixprotocol.atdl_1_1.core.LocalMktTimeT;
import org.fixprotocol.atdl_1_1.core.MonthYearT;
import org.fixprotocol.atdl_1_1.core.MultipleCharValueT;
import org.fixprotocol.atdl_1_1.core.MultipleStringValueT;
import org.fixprotocol.atdl_1_1.core.NumInGroupT;
import org.fixprotocol.atdl_1_1.core.NumericT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.core.SeqNumT;
import org.fixprotocol.atdl_1_1.core.StringT;
import org.fixprotocol.atdl_1_1.core.TagNumT;
import org.fixprotocol.atdl_1_1.core.UTCDateT;
import org.fixprotocol.atdl_1_1.core.UTCTimeOnlyT;
import org.fixprotocol.atdl_1_1.core.UTCTimeStampT;
import org.fixprotocol.atdl_1_1.layout.CheckBoxListT;
import org.fixprotocol.atdl_1_1.layout.CheckBoxT;
import org.fixprotocol.atdl_1_1.layout.ClockT;
import org.fixprotocol.atdl_1_1.layout.ControlT;
import org.fixprotocol.atdl_1_1.layout.DoubleSpinnerT;
import org.fixprotocol.atdl_1_1.layout.DropDownListT;
import org.fixprotocol.atdl_1_1.layout.EditableDropDownListT;
import org.fixprotocol.atdl_1_1.layout.HiddenFieldT;
import org.fixprotocol.atdl_1_1.layout.LabelT;
import org.fixprotocol.atdl_1_1.layout.MultiSelectListT;
import org.fixprotocol.atdl_1_1.layout.RadioButtonGroupT;
import org.fixprotocol.atdl_1_1.layout.RadioButtonT;
import org.fixprotocol.atdl_1_1.layout.SingleSelectListT;
import org.fixprotocol.atdl_1_1.layout.SingleSpinnerT;
import org.fixprotocol.atdl_1_1.layout.SliderT;
import org.fixprotocol.atdl_1_1.layout.TextFieldT;

/*
 * NumericT subclasses
import org.fixprotocol.atdl_1_1.core.AmtT;
import org.fixprotocol.atdl_1_1.core.FloatT;
import org.fixprotocol.atdl_1_1.core.PercentageT;
import org.fixprotocol.atdl_1_1.core.PriceT;
import org.fixprotocol.atdl_1_1.core.PriceOffsetT;
import org.fixprotocol.atdl_1_1.core.QtyT;
 */

/**
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.
 * 
 */
public abstract class TypeConverterFactory {

	/*
	 *  Create adapter based on ParameterT
	 */
	public static TypeConverter<?> create(ParameterT parameter) throws JAXBException {
		if (parameter instanceof StringT ||
				parameter instanceof CharT ||
				parameter instanceof MultipleCharValueT ||
				parameter instanceof MultipleStringValueT ||
				parameter instanceof CurrencyT ||
				parameter instanceof ExchangeT ||
				parameter instanceof DataT)
		{
			return new StringConverter(parameter);
		}
		else if (parameter instanceof NumericT)
		{
			return new NumberConverter(parameter);	// Float field		
		}
		else if (parameter instanceof IntT ||
				   parameter instanceof NumInGroupT ||
				   parameter instanceof SeqNumT ||
				   parameter instanceof TagNumT ||
				   parameter instanceof LengthT)
		{
			return new NumberConverter(parameter);  // Integer field
		}
		else if (parameter instanceof BooleanT)
		{
			return new BooleanConverter((BooleanT)parameter);
		}
		else if (parameter instanceof MonthYearT ||
				   parameter instanceof UTCTimeStampT ||
				   parameter instanceof UTCTimeOnlyT ||
				   parameter instanceof LocalMktTimeT ||
				   parameter instanceof UTCDateT)
		{
			return new DateTimeConverter(parameter);
		}
		else throw new JAXBException("Unsupported ParameterT type: " + parameter.getClass().getName());
	}

	/*
	 *  Create adapter based on ControlT (native type for each control)
	 */
	public static TypeConverter<?> create(ControlT control, ParameterT parameter) throws JAXBException {
		if (control instanceof TextFieldT ||
				control instanceof SingleSelectListT ||
				control instanceof MultiSelectListT ||
				control instanceof CheckBoxListT ||
				control instanceof DropDownListT ||
				control instanceof EditableDropDownListT ||
				control instanceof RadioButtonGroupT ||
				control instanceof HiddenFieldT ||
				control instanceof LabelT)
		{
			return new StringConverter();
		}
		else if (control instanceof SliderT ||
				control instanceof SingleSpinnerT ||
				control instanceof DoubleSpinnerT)
		{
			return new NumberConverter();		
		}
		else if (control instanceof CheckBoxT ||
				control instanceof RadioButtonT)
		{
			return new BooleanConverter();
		}
		else if (control instanceof ClockT)
		{
			return new DateTimeConverter(parameter);
		}
		else throw new JAXBException("Unsupported ControlT type: " + control.getClass().getName());
	}
}
