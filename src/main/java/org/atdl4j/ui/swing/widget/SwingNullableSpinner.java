package org.atdl4j.ui.swing.widget;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


public class SwingNullableSpinner extends JSpinner
{
	private static final long			serialVersionUID	= 2947835451995064559L;
	public static final BigDecimal MIN_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal( -Integer.MAX_VALUE );
	public static final BigDecimal MAX_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal( Integer.MAX_VALUE );
	
	public SwingNullableSpinner() {
		super();
		setModel(new SpinnerNumberModelNull());
		setValue(null);
	}

	@Override
	protected JComponent createEditor(SpinnerModel model) {
		return new NumberEditorNull(this);
	}
	
	public class SpinnerNumberModelNull extends AbstractSpinnerModel implements Serializable {
	    	private static final long serialVersionUID = -7274426043990492783L;
		private Number stepSize, value;
		private Comparable minimum, maximum;
		
		public SpinnerNumberModelNull(Number value, Comparable minimum, Comparable maximum, Number stepSize) {
			this.value = value;
			this.minimum = minimum;
			this.maximum = maximum;
			this.stepSize = stepSize;
		}
		
		public SpinnerNumberModelNull(int value, int minimum, int maximum, int stepSize) {
			this(Integer.valueOf(value), Integer.valueOf(minimum), Integer.valueOf(maximum), Integer.valueOf(stepSize));
		}
		
		public SpinnerNumberModelNull(double value, double minimum, double maximum, double stepSize)
		{
			this( BigDecimal.valueOf(value), BigDecimal.valueOf(minimum), BigDecimal.valueOf(maximum), BigDecimal.valueOf(stepSize));
		}
		
		public SpinnerNumberModelNull() {
			this(BigDecimal.ZERO, null, null, BigDecimal.ONE);
		}
		
		public void setMinimum(Comparable minimum) {
			if ((minimum == null) ? (this.minimum != null) : !minimum.equals(this.minimum)) {
				this.minimum = minimum;
				fireStateChanged();
			}
		}
		
		public Comparable getMinimum() {
			return minimum;
		}
		
		public void setMaximum(Comparable maximum) {
			if ((maximum == null) ? (this.maximum != null) : !maximum.equals(this.maximum)) {
				this.maximum = maximum;
				fireStateChanged();
			}
		}
		
		public Comparable getMaximum() {
			return maximum;
		}
		
		public void setStepSize(Number stepSize) {
			if (stepSize == null) {
				throw new IllegalArgumentException("null stepSize");
			}
			if (!stepSize.equals(this.stepSize)) {
				this.stepSize = stepSize;
				fireStateChanged();
			}
		}
		
		public Number getStepSize() {
			return stepSize;
		}
		
		private Number incrValue(int dir) {
			Number newValue;
			if ((value instanceof Long) || (value instanceof Integer) || (value instanceof Short) || (value instanceof Byte))
			{
				long v = value.longValue() + (stepSize.longValue() * (long) dir);
				
				if (value instanceof Long) {
					newValue = Long.valueOf(v);
				}
				else if (value instanceof Integer) {
					newValue = Integer.valueOf((int) v);
				}
				else if (value instanceof Short) {
					newValue = Short.valueOf((short) v);
				}
				else {
					newValue = Byte.valueOf((byte) v);
				}
			}
			else
			{
				if (value instanceof Float)
				{
					double v = value.doubleValue() + (stepSize.doubleValue() * (double) dir);
					newValue = Float.valueOf(Double.valueOf(v).floatValue());
				}
				else if (value instanceof Double)
				{
					double v = value.doubleValue() + (stepSize.doubleValue() * (double) dir);
					newValue = Double.valueOf(v);
				}
				else
				{
					if (value == null) value = 0.;
					double v = value.doubleValue() + (stepSize.doubleValue() * (double) dir);
					newValue = BigDecimal.valueOf(v);
				}
			}
			
			if ((maximum != null) && (maximum.compareTo(newValue) < 0.)) {
				return null;
			}
			if ((minimum != null) && (minimum.compareTo(newValue) > 0.)) {
				return null;
			}
			else {
				return newValue;
			}
		}
		
		public Object getNextValue() {
			return incrValue(+1);
		}
		
		public Object getPreviousValue() {
			return incrValue(-1);
		}
		
		public Number getNumber() {
			return value;
		}
		
		public Object getValue() {
			return value;
		}
		
		public void setValue(Object value) {
			if ((this.value != null) && (value == null)){
				this.value = null;
				fireStateChanged();
			}
			else if ((value != null) && !value.equals(this.value)) {
				if (value instanceof Number) {
					this.value = (Number)value;
				}
				fireStateChanged();
			}
		}
	}
	
	public static class NumberEditorNull extends DefaultEditor {
	    	private static final long serialVersionUID = 4927264073154690869L;

		private static DecimalFormat getDefaultPattern(Locale locale) {
			return (DecimalFormat) NumberFormat.getNumberInstance(locale);
		}
		
		public NumberEditorNull(JSpinner spinner) {
			this(spinner, getDefaultPattern(spinner.getLocale()));
		}
		
		public NumberEditorNull(JSpinner spinner, String decimalFormatPattern)
		{
			this(spinner, new DecimalFormat("0.######"));
		}
		
		private NumberEditorNull(JSpinner spinner, DecimalFormat format) {
			super(spinner);
			if (!(spinner.getModel() instanceof SpinnerNumberModelNull)) {
				return;
			}
			
			SpinnerNumberModelNull model = (SpinnerNumberModelNull) spinner.getModel();
			NumberFormatter formatter = new NumberEditorFormatterNull(model, format);
			DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
			JFormattedTextField ftf = getTextField();
			ftf.setEditable(true);
			ftf.setFormatterFactory(factory);
			ftf.setHorizontalAlignment(SwingConstants.RIGHT);
			
			try {
				String maxString = formatter.valueToString(model.getMinimum());
				String minString = formatter.valueToString(model.getMaximum());
				ftf.setColumns(Math.max(maxString.length(), minString.length()));
			}
			catch (ParseException e) {
				// TBD should throw a chained error here
			}
		}
		
		public DecimalFormat getFormat() {
			return (DecimalFormat) ((NumberFormatter) (getTextField().getFormatter())).getFormat();
		}
		
		public SpinnerNumberModel getModel() {
			return (SpinnerNumberModel) (getSpinner().getModel());
		}
	}
	
	private static class NumberEditorFormatterNull extends NumberFormatter {
	   	private static final long serialVersionUID = 4731911867350591824L;
		private final SpinnerNumberModelNull	model;
		
		NumberEditorFormatterNull(SpinnerNumberModelNull model, NumberFormat format) {
			super(format);
			this.model = model;
			setValueClass(model.getValue().getClass());
			
			setCommitsOnValidEdit(true);
		}

		@Override
		public void setMinimum(Comparable min)
		{
			model.setMinimum(min);
		}

		@Override
		public Comparable getMinimum()
		{
			return model.getMinimum();
		}

		@Override
		public void setMaximum(Comparable max)
		{
			model.setMaximum(max);
		}

		@Override
		public Comparable getMaximum()
		{
			return model.getMaximum();
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.InternationalFormatter#valueToString(java.lang.Object)
		 */
		@Override
		public String valueToString(Object value) throws ParseException {
			if (value == null) {
    		getFormattedTextField().setBackground(Color.white);
				return "";
			}
			String retVal = null;
			try {
				retVal = super.valueToString(value);
				
       	getFormattedTextField().setBackground(Color.white);
			}
			catch(ParseException ex){
				getFormattedTextField().setBackground(Color.red);
				throw ex;
			}
			
			return retVal; 
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.InternationalFormatter#stringToValue(java.lang.String)
		 */
		@Override
		public Object stringToValue(String text) throws ParseException {
			if ("".equals(text)) {
    		getFormattedTextField().setBackground(Color.white);
				return null;
			}
			
			Object retVal = null;
			try {
				retVal = super.stringToValue(text);
				
				try {
					Double.parseDouble(text);
					if (!text.contains("f") && !text.contains("F") && !text.contains("d") && !text.contains("D")) {
						getFormattedTextField().setBackground(Color.white);
					}
					else {
						throw new NumberFormatException();
					}
				}
				catch(NumberFormatException exe){
					getFormattedTextField().setBackground(Color.red);
				}
			}
			catch(ParseException ex){
				getFormattedTextField().setBackground(Color.red);
				throw ex;
			}
			
			return retVal; 
		}
	}
	
	
}
