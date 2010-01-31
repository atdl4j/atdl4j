
package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ControlT;

/**
 * An interface for an UI widget which will be 
 * stores its underlying value in a parameter field.
 */
public interface ControlUI<E extends Comparable<?>> {

	public ParameterT getParameter();
	
	public ControlT getControl();
		
	// Control value accessor methods
	
	public E getControlValue();
	
	public String getControlValueAsString() throws JAXBException;
	
	public Comparable<?> getControlValueAsComparable() throws JAXBException;
	
	// Parameter value accessor methods
	
	public Object getParameterValue();
	
	public String getParameterValueAsString() throws JAXBException;
	
	public Comparable<?> getParameterValueAsComparable() throws JAXBException;
	
	// Value mutator methods
	
	public void setValue(E value);
		
	public void setValueAsString(String value) throws JAXBException;

	// Helper methods
	
	public Comparable<?> convertStringToComparable(String string) throws JAXBException;
	
	// FIX methods
	
	public String getFIXValue() throws JAXBException;
	
	public void getFIXValue(FIXMessageBuilder builder) throws JAXBException;

	// UI methods
	
	public void setVisible(boolean visible);
	
	public void setEnabled(boolean enabled);	
	
//TODO Scott Atwell 1/14/2010 Added
	public int getFIXType() throws JAXBException;
}