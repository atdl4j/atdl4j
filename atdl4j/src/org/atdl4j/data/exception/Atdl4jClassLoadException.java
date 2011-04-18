package org.atdl4j.data.exception;

/**
 * @author John Sields
 * 
 * Atdl4jClassLoadException is thrown by AbstractAtdl4jConfiguration when a class
 * cannot be dynamically loaded. This is thrown as a RuntimeException, however, users have
 * the highly recommended option to prevalidate that all classes can be loaded at the
 * time the config is initialized (see AbstractAtdl4jConfiguration::testClassLoaders() method)
 */
public class Atdl4jClassLoadException extends RuntimeException {

    private static final long serialVersionUID = -5703141767631866394L;

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