package org.atdl4j.data.converter;

import org.atdl4j.data.TypeConverter;
import org.fixprotocol.atdl_1_1.core.ParameterT;

/**
 * This class is a container class for ParameterT
 * 
 * @author john.shields
 */
public abstract class AbstractTypeConverter<E extends Comparable<?>> implements TypeConverter<E> {

	protected ParameterT parameter;
	
	public ParameterT getParameter() {
		return parameter;
	}
}
