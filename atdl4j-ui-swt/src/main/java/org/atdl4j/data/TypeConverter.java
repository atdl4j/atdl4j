package org.atdl4j.data;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.ParameterT;

/**
 * An interface for an algorithmic parameter container class. Classes which
 * implement this interface hold parameter descriptor data but do not store a
 * value (see the ControlUI class which stores the underlying FIX value.)
 */
public interface TypeConverter<E extends Comparable<?>> {

	public E convertValueToComparable(Object value) throws JAXBException;

	public String convertValueToString(Object value) throws JAXBException;

	public ParameterT getParameter();
}