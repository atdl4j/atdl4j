package org.atdl4j.data.exception;

/**
 * @author John Sields
 * 
 * Atdl4jConfigNotFoundException is thrown by Atdl4jConfig when its static
 * config variable is not initialized. This is subclass of RuntimeException,
 * meaning that it does not need to be caught.
 */
public class Atdl4jConfigNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public Atdl4jConfigNotFoundException()
    {
	super("Application Error: Atdl4j configuration not specified");
    }
}