package org.atdl4j.data.exception;

/**
 * @author John Sields
 * 
 * FIXatdlFormatException is thrown whenever the application detects than an
 * input FIXatdl file does not adhere to the FIXatdl or XML schemas.
 * 
 * This can occur both during the initial JAXB parsing (e.g. malformed XML)
 * and during second-pass parsing phases while building the screens.
 */
public class FIXatdlFormatException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public FIXatdlFormatException(String message)
    {
	super(message);
    }
}
