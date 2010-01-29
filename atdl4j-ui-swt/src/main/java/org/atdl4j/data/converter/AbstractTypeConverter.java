package org.atdl4j.data.converter;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.data.TypeConverter;

/**
 * This class is a container class for ParameterT
 * 
 * @author john.shields
 */
public abstract class AbstractTypeConverter<E extends Comparable<?>> implements
		TypeConverter<E> {

	protected ParameterT parameter;

	public ParameterT getParameter() {
		return parameter;
	}
}
