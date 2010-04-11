package org.atdl4j.data.converter;

import org.atdl4j.data.ControlTypeConverter;
import org.atdl4j.data.ParameterTypeConverter;
import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.PercentageT;

/**
 * Base class for ParameterTypeConverter and ControlTypeConverter interfaces
 * 
 * @author john.shields
 */
public abstract class AbstractTypeConverter<E extends Comparable<?>> 
	implements ParameterTypeConverter<E>, ControlTypeConverter<E>
{
	private ParameterT parameter;  // used by ParameterTypeConverter
	private ParameterTypeConverter<?> parameterTypeConverter;  // used by ControlTypeConverter
	
	
	/**
	 * Used to construct instances of ControlTypeConverter.
	 * @param aParameter
	 */
	public AbstractTypeConverter(ParameterT aParameter)
	{
		setParameter( aParameter );
	}

	/**
	 * Used to construct instances of ControlTypeConverter.
	 * @param aParameterTypeConverter
	 */
	public AbstractTypeConverter(ParameterTypeConverter<?> aParameterTypeConverter)
	{
		setParameterTypeConverter( aParameterTypeConverter );
	}
	

	protected void setParameter( ParameterT aParameter ) 
	{
		this.parameter = aParameter;
	}
	
	public ParameterT getParameter() 
	{
		return parameter;
	}
	
	protected String getParameterName()
	{
		if ( getParameter() != null )
		{
			return getParameter().getName();
		}
		else if ( ( getParameterTypeConverter() != null ) && ( getParameter() != null ) )
		{
			return getParameterTypeConverter().getParameter().getName();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the value of Parameter.getMultiplyBy100() for PercentageT assuming
	 * it has been set, otherwise returns false.
	 * 
	 * @return
	 */
	public boolean isParameterMultiplyBy100()
	{
		if ( getParameter() instanceof PercentageT )
		{
			return ( (PercentageT) getParameter() ).isMultiplyBy100();
		}
		else
		{
			// -- Return null if Parameter does not have this value set --
			return false;
		}
	}

	/**
	 * Returns true if Parameter instanceof PercentageT otherwise returns false.
	 * 
	 * @return
	 */
	public boolean isControlMultiplyBy100()
	{
		if ( getParameter() instanceof PercentageT )
		{
			return true;
		}
		else if ( ( getParameterTypeConverter() != null ) &&
				    ( getParameterTypeConverter().getParameter() instanceof PercentageT ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.data.ParameterTypeConverter#convertParameterValueToComparisonString(java.lang.Object)
	 */
	@Override
	public String convertParameterValueToComparisonString(Object aParameterValue)
	{
		E tempComparable = convertParameterValueToParameterComparable( aParameterValue );
		
		if ( tempComparable != null )
		{
			return tempComparable.toString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param parameterTypeConverter the parameterTypeConverter to set
	 */
	protected void setParameterTypeConverter(ParameterTypeConverter<?> parameterTypeConverter)
	{
		this.parameterTypeConverter = parameterTypeConverter;
	}

	/**
	 * @return the parameterTypeConverter
	 */
	public ParameterTypeConverter<?> getParameterTypeConverter()
	{
		return parameterTypeConverter;
	}


	/**
	 * Part of ControlTypeConverter (if using ParameterTypeConverter use no-arg method).
	 * Returns an Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc)
	 * Returns aDatatypeIfNull if Parameter is null
	 * @param aDatatypeIfNull
	 * @return
	 */
	public Object getParameterDatatype( Object aDatatypeIfNull )
	{
		if ( getParameterTypeConverter() != null )
		{
			return getParameterTypeConverter().getParameterDatatype();
		}
		else
		{
			return aDatatypeIfNull;
		}
	}

	/**
	 * Part of ParameterTypeConverter, also supported by ControlTypeConverter.
	 * Returns an Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc)
	 */
	public Object getParameterDatatype()
	{
		ParameterT tempParameter = getParameter();
		
		if ( ( tempParameter == null ) && ( getParameterTypeConverter() != null ) )
		{
			tempParameter = getParameterTypeConverter().getParameter();;
		}
		
		if ( tempParameter != null )
		{
			return TypeConverterFactory.getParameterDatatype( tempParameter );
		}
		else
		{
			return null;
		}
		
	}

}
