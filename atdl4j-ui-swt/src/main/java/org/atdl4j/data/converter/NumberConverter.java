package org.atdl4j.data.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.fixprotocol.atdl_1_1.core.AmtT;
import org.fixprotocol.atdl_1_1.core.FloatT;
import org.fixprotocol.atdl_1_1.core.IntT;
// import org.fixprotocol.atdl_1_1.core.LengthT; doesn't have min/max value?
import org.fixprotocol.atdl_1_1.core.NumericT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.core.PercentageT;
import org.fixprotocol.atdl_1_1.core.PriceOffsetT;
import org.fixprotocol.atdl_1_1.core.PriceT;
import org.fixprotocol.atdl_1_1.core.QtyT;

public class NumberConverter extends AbstractTypeConverter<BigDecimal> {

	public NumberConverter() {
	}
	
	public NumberConverter(ParameterT parameter) {
		this.parameter = parameter;
	}

	// TODO: this doesn't currently return null
	public BigDecimal convertValueToComparable(Object value)
	{
		// TODO: need to handle precision, integers, etc
		if (value instanceof BigDecimal)
		{
			return (BigDecimal) value;
		}
		else if (value instanceof String)
		{
			String str = (String) value;
			try {
				return new BigDecimal(str);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		else if (value instanceof Boolean)
		{
			Boolean bool = (Boolean) value;
			if (bool != null)
			{
				if (bool) return new BigDecimal(1);
				else return new BigDecimal(0);
			}
			else return null;
		}
		return null;
	}
	
	public String convertValueToString(Object value)
	{
		// TODO: need to handle precision, integers, etc
		BigDecimal num = convertValueToComparable(value); // TODO: this doesn't currently return null
		if (num == null)
			return null;
		else
			return num.toPlainString();
	}
		
	public Integer getPrecision() {

		BigInteger precision;

		if (parameter instanceof NumericT) {
			NumericT numeric = (NumericT) parameter;
			// default precision is 2 decimal places in case of FloatT
			precision = BigInteger.valueOf(2);

			// override precision if defined by user
			if (numeric.getPrecision() != null) {
				precision = numeric.getPrecision();
			}
		} else {
			precision = BigInteger.ZERO;
		}

		return precision.intValue();

	}
	
	public Integer getMinimum() {
		
		if (parameter instanceof FloatT) {
			// get value defined by user
			FloatT floatT = (FloatT) parameter;

			if (floatT.getMinValue() != null) {
				float minValue = floatT.getMinValue();

				// adjust according to precision
				int minValueAdjusted = (int) (minValue * Math.pow(10, getPrecision()));
				return minValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;

			if (amtT.getMinValue() != null) {
				BigDecimal minValue = amtT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(getPrecision());
				return minValue.intValue();
			}

		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;

			if (percentageT.getMinValue() != null) {
				BigDecimal minValue = percentageT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(getPrecision());
				return minValue.intValue();
			}

		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;

			if (priceOffsetT.getMinValue() != null) {
				BigDecimal minValue = priceOffsetT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(getPrecision());
				return minValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.getMinValue() != null) {
				BigDecimal minValue = priceT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(getPrecision());
				return minValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.getMinValue() != null) {
				BigDecimal minValue = priceT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(getPrecision());
				return minValue.intValue();
			}

		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;

			if (qtyT.getMinValue() != null) {
				BigDecimal minValue = qtyT.getMinValue();
				minValue = minValue.scaleByPowerOfTen(getPrecision());
				return minValue.intValue();
			}

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;

			if (intT.getMinValue()!= null) {
				return intT.getMinValue();
			}

		}

		return null;
	}
	
	public Integer getMaximum() {

		if (parameter instanceof FloatT) {
			// get value defined by user
			FloatT floatT = (FloatT) parameter;

			if (floatT.getMaxValue() != null) {
				float maxValue = floatT.getMaxValue();

				// adjust according to precision
				int maxValueAdjusted = (int) (maxValue * Math.pow(10, getPrecision()));
				return maxValueAdjusted;
			}
		} else if (parameter instanceof AmtT) {
			AmtT amtT = (AmtT) parameter;

			if (amtT.getMaxValue() != null) {
				BigDecimal maxValue = amtT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(getPrecision());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PercentageT) {
			PercentageT percentageT = (PercentageT) parameter;

			if (percentageT.getMaxValue() != null) {
				BigDecimal maxValue = percentageT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(getPrecision());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PriceOffsetT) {
			PriceOffsetT priceOffsetT = (PriceOffsetT) parameter;

			if (priceOffsetT.getMaxValue() != null) {
				BigDecimal maxValue = priceOffsetT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(getPrecision());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.getMaxValue() != null) {
				BigDecimal maxValue = priceT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(getPrecision());
				return maxValue.intValue();
			}

		} else if (parameter instanceof PriceT) {
			PriceT priceT = (PriceT) parameter;

			if (priceT.getMaxValue() != null) {
				BigDecimal maxValue = priceT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(getPrecision());
				return maxValue.intValue();
			}

		} else if (parameter instanceof QtyT) {
			QtyT qtyT = (QtyT) parameter;

			if (qtyT.getMaxValue() != null) {
				BigDecimal maxValue = qtyT.getMaxValue();
				maxValue = maxValue.scaleByPowerOfTen(getPrecision());
				return maxValue.intValue();
			}

		} else if (parameter instanceof IntT) {
			IntT intT = (IntT) parameter;

			if (intT.getMaxValue() != null) {
				return intT.getMaxValue();
			}

		}

		return null;
	}
}