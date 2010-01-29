package org.atdl4j.data;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.BooleanT;
import org.atdl4j.atdl.core.CharT;
import org.atdl4j.atdl.core.CurrencyT;
import org.atdl4j.atdl.core.DataT;
import org.atdl4j.atdl.core.ExchangeT;
import org.atdl4j.atdl.core.IntT;
import org.atdl4j.atdl.core.LengthT;
import org.atdl4j.atdl.core.LocalMktTimeT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.MultipleCharValueT;
import org.atdl4j.atdl.core.MultipleStringValueT;
import org.atdl4j.atdl.core.NumInGroupT;
import org.atdl4j.atdl.core.NumericT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.SeqNumT;
import org.atdl4j.atdl.core.StringT;
import org.atdl4j.atdl.core.TagNumT;
import org.atdl4j.atdl.core.UTCDateT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimeStampT;
import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.ClockT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.DoubleSpinnerT;
import org.atdl4j.atdl.layout.DropDownListT;
import org.atdl4j.atdl.layout.EditableDropDownListT;
import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.atdl.layout.LabelT;
import org.atdl4j.atdl.layout.MultiSelectListT;
import org.atdl4j.atdl.layout.RadioButtonGroupT;
import org.atdl4j.atdl.layout.RadioButtonT;
import org.atdl4j.atdl.layout.SingleSelectListT;
import org.atdl4j.atdl.layout.SingleSpinnerT;
import org.atdl4j.atdl.layout.SliderT;
import org.atdl4j.atdl.layout.TextFieldT;
import org.atdl4j.data.converter.BooleanConverter;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.data.converter.NumberConverter;
import org.atdl4j.data.converter.StringConverter;

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
	 * Create adapter based on ParameterT
	 */
	public static TypeConverter<?> create(ParameterT parameter)
			throws JAXBException {
		if (parameter instanceof StringT || parameter instanceof CharT
				|| parameter instanceof MultipleCharValueT
				|| parameter instanceof MultipleStringValueT
				|| parameter instanceof CurrencyT
				|| parameter instanceof ExchangeT || parameter instanceof DataT) {
			return new StringConverter(parameter);
		} else if (parameter instanceof NumericT) {
			return new NumberConverter(parameter); // Float field
		} else if (parameter instanceof IntT
				|| parameter instanceof NumInGroupT
				|| parameter instanceof SeqNumT || parameter instanceof TagNumT
				|| parameter instanceof LengthT) {
			return new NumberConverter(parameter); // Integer field
		} else if (parameter instanceof BooleanT) {
			return new BooleanConverter((BooleanT) parameter);
		} else if (parameter instanceof MonthYearT
				|| parameter instanceof UTCTimeStampT
				|| parameter instanceof UTCTimeOnlyT
				|| parameter instanceof LocalMktTimeT
				|| parameter instanceof UTCDateT) {
			return new DateTimeConverter(parameter);
		} else
			throw new JAXBException("Unsupported ParameterT type: "
					+ parameter.getClass().getName());
	}

	/*
	 * Create adapter based on ControlT (native type for each control)
	 */
	public static TypeConverter<?> create(ControlT control, ParameterT parameter)
			throws JAXBException {
		if (control instanceof TextFieldT
				|| control instanceof SingleSelectListT
				|| control instanceof MultiSelectListT
				|| control instanceof CheckBoxListT
				|| control instanceof DropDownListT
				|| control instanceof EditableDropDownListT
				|| control instanceof RadioButtonGroupT
				|| control instanceof HiddenFieldT || control instanceof LabelT) {
			return new StringConverter();
		} else if (control instanceof SliderT
				|| control instanceof SingleSpinnerT
				|| control instanceof DoubleSpinnerT) {
			return new NumberConverter();
		} else if (control instanceof CheckBoxT
				|| control instanceof RadioButtonT) {
			return new BooleanConverter();
		} else if (control instanceof ClockT) {
			return new DateTimeConverter(parameter);
		} else
			throw new JAXBException("Unsupported ControlT type: "
					+ control.getClass().getName());
	}
}
