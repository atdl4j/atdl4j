package br.com.investtools.fix.atdl.ui.swt.widget;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.BooleanT;
import br.com.investtools.fix.atdl.core.xmlbeans.CharT;
import br.com.investtools.fix.atdl.core.xmlbeans.CurrencyT;
import br.com.investtools.fix.atdl.core.xmlbeans.DataT;
import br.com.investtools.fix.atdl.core.xmlbeans.ExchangeT;
import br.com.investtools.fix.atdl.core.xmlbeans.IntT;
import br.com.investtools.fix.atdl.core.xmlbeans.LengthT;
import br.com.investtools.fix.atdl.core.xmlbeans.LocalMktTimeT;
import br.com.investtools.fix.atdl.core.xmlbeans.MonthYearT;
import br.com.investtools.fix.atdl.core.xmlbeans.MultipleStringValueT;
import br.com.investtools.fix.atdl.core.xmlbeans.NumInGroupT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.SeqNumT;
import br.com.investtools.fix.atdl.core.xmlbeans.StringT;
import br.com.investtools.fix.atdl.core.xmlbeans.TagNumT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCDateT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCTimeOnlyT;
import br.com.investtools.fix.atdl.core.xmlbeans.UTCTimeStampT;
import br.com.investtools.fix.atdl.ui.swt.ParameterUI;

public class HiddenParameterWidget implements ParameterUI<String> {

	private ParameterT parameter;

	private String value;

	public HiddenParameterWidget(ParameterT parameter) {
		this.parameter = parameter;
		this.value = getConstInitValue();
	}

	private String getConstInitValue() {

		if (parameter instanceof BooleanT) {
			BooleanT booleanT = (BooleanT) parameter;
			return String.valueOf(booleanT.getInitValue());
		} else if (parameter instanceof CharT) {
			CharT charT = (CharT) parameter;
			return charT.getInitValue();
		} else if (parameter instanceof CurrencyT) {
			CurrencyT currencyT = (CurrencyT) parameter;
			return currencyT.getInitValue();
		} else if (parameter instanceof DataT) {
			DataT dataT = (DataT) parameter;
			return dataT.getInitValue();
		} else if (parameter instanceof ExchangeT) {
			ExchangeT exchangeT = (ExchangeT) parameter;
			return exchangeT.getInitValue();
		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;
			return String.valueOf(intT.getInitValue());
		} else if (parameter instanceof LengthT) {
			LengthT lengthT = (LengthT) parameter;
			return lengthT.getInitValue().toString();
		} else if (parameter instanceof LocalMktTimeT) {
			LocalMktTimeT localMktTimeT = (LocalMktTimeT) parameter;
			return localMktTimeT.getInitValue().toString();
		} else if (parameter instanceof MonthYearT) {
			MonthYearT monthYearT = (MonthYearT) parameter;
			return monthYearT.getInitValue().toString();
		} else if (parameter instanceof MultipleStringValueT) {
			MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
			return multipleStringValueT.getInitValue();
		} else if (parameter instanceof NumInGroupT) {
			NumInGroupT numInGroupT = (NumInGroupT) parameter;
			return numInGroupT.getInitValue().toString();
		} else if (parameter instanceof SeqNumT) {
			SeqNumT seqNumT = (SeqNumT) parameter;
			return seqNumT.getInitValue().toString();
		} else if (parameter instanceof StringT) {
			StringT stringT = (StringT) parameter;
			return stringT.getInitValue();
		} else if (parameter instanceof TagNumT) {
			TagNumT tagNumT = (TagNumT) parameter;
			return tagNumT.getInitValue().toString();
		} else if (parameter instanceof UTCDateT) {
			UTCDateT utcDateT = (UTCDateT) parameter;
			return utcDateT.getInitValue().toString();
		} else if (parameter instanceof UTCTimeOnlyT) {
			UTCTimeOnlyT utcTimeOnlyT = (UTCTimeOnlyT) parameter;
			return utcTimeOnlyT.getInitValue().toString();
		} else if (parameter instanceof UTCTimeStampT) {
			UTCTimeStampT utcTimeStampT = (UTCTimeStampT) parameter;
			return utcTimeStampT.getInitValue().toString();
		}
		return "";
	}

	@Override
	public String convertValue(String value) {
		return value;
	}

	@Override
	public Widget createWidget(Composite parent, ParameterT parameter, int style) {
		return null;
	}

	@Override
	public String getFIXValue() {
		if (parameter.getFixTag() != null) {
			return Integer.toString(parameter.getFixTag().intValue()) + "="
					+ getValue();
		} else {
			String name = parameter.getName();
			String type = Integer.toString(parameter.getType());
			String value = getValue();
			char delimiter = '\001';
			return "958=" + name + delimiter + "959=" + type + delimiter
					+ "960=" + value;
		}
	}

	@Override
	public ParameterT getParameter() {
		return parameter;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void generateStateRuleListener(Listener listener) {
	}

	@Override
	public List<Control> getControls() {
		return null;
	}

	@Override
	public void addListener(Listener listener) {
		// do nothing
	}

	@Override
	public void removeListener(Listener listener) {
		// do nothing
	}

}
