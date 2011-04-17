package org.atdl4j.data.exception;

/**
 * @author John Sields
 * 
 * Atdl4jClassLoadException is thrown by AbstractAtdl4jConfiguration when a class
 * cannot be dynamically loaded
 */
public class Atdl4jClassLoadException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public Atdl4jClassLoadException(String className)
    {
	super(message(className));
    }
    
    public Atdl4jClassLoadException(String className, Throwable e)
    {
	super(message(className), e);
    }
    
    private static String message(String className)
    {
	return "Exception attempting to load Class.forName( " + className + " )";
    }
}