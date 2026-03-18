package uk.gov.courtservice.framework.exception;

/**
 * Base exception class for system exceptions in the CS Hub framework. Supports
 * exception chaining, maintaining the cause and providing getCause() method.
 * Allows additional messages to be provided both for the user (use of Message
 * class) and the developer (String errorMessage). Creation date: (10/4/01
 * 1:15:43 PM)
 * 
 * @author: Pete Raymond
 * @author: Kevin Buckthorpe
 * @author: Bal Bhamra
 */
public class CSUnrecoverableException extends RuntimeException implements CSException {
    protected Throwable cause;

    protected boolean isLogged;

    private static int count = 0;

    private CSExceptionImpl csExceptionImpl = new CSExceptionImpl();

    /**
     * CSUnrecoverableException
     */
    public CSUnrecoverableException() {
        super();
        csExceptionImpl.createId();
    }

    /**
     * Overloaded constructor, taking in a Message as argument.
     * 
     * @param Message
     *            a Message obejct
     */
    public CSUnrecoverableException(Message newUserMessage) {
        csExceptionImpl.addMessage(newUserMessage);
        csExceptionImpl.createId();
    }

    /**
     * Overloaded constructor, taking in a Message and a string log message as
     * arguments.
     * 
     * @param Message
     *            a Message obejct
     */
    public CSUnrecoverableException(Message newUserMessage, String newLogMsg) {
        super(newLogMsg);
        csExceptionImpl.addMessage(newUserMessage);
        csExceptionImpl.createId();
    }

    /**
     * Overloaded constructor, taking in a Message and Throwable object as
     * arguments.
     * 
     * @param Message
     *            a Message obejct
     */
    public CSUnrecoverableException(Message newUserMessage, Throwable cause) {
        this.cause = cause;
        csExceptionImpl.addMessage(newUserMessage);
        csExceptionImpl.createId();
    }

    /**
     * Overloaded constructor, taking in a Message, Throwable object and a
     * string with the error messaage as arguments.
     * 
     * @param Message
     *            a Message obejct
     */
    public CSUnrecoverableException(Message newUserMessage, Throwable cause, String newLogMsg) {
        super(newLogMsg);
        this.cause = cause;
        csExceptionImpl.addMessage(newUserMessage);
        csExceptionImpl.createId();
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public CSUnrecoverableException(String logMessage) {
        super(logMessage);
        csExceptionImpl.createId();
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public CSUnrecoverableException(Throwable cause) {
        super();
        this.cause = cause;
        csExceptionImpl.createId();
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public CSUnrecoverableException(String logMessage, Throwable cause) {
        super(logMessage);
        this.cause = cause;
        csExceptionImpl.createId();
    }

    /**
     * 
     * @return Throwable: exception wrapped by this instance
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * 
     * @return String the message for user of application
     */
    public String getUserMessage() {
        return csExceptionImpl.getUserMessage();
    }

    /**
     * @return String[] the message for user of application
     */
    public String[] getUserMessages() {
        return csExceptionImpl.getUserMessages();
    }

    /**
     * 
     * @return Message the message for user of application
     */
    public Message getUserMessageAsMessage() {
        return csExceptionImpl.getUserMessageAsMessage();
    }

    /**
     * @return Message[] the messages for user of application
     */
    public Message[] getUserMessagesAsMessages() {
        return csExceptionImpl.getUserMessagesAsMessages();
    }

    /**
     * 
     * @return the id number of this exception instance
     */
    public String getErrorID() {
        return csExceptionImpl.getErrorID();
    }

    /**
     * Gets the logged flag
     * 
     * @return boolean: true indicates that the exception has been logged
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * Sets the logged flag
     * 
     * @param isLogged
     *            true indicates that the exception has been logged
     */
    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    // THIS METHOD HAS MOVED TO CSExceptionImpl AS A PROTECTED METHOD.
    // It is not in CSException interface however (cannot have protected
    // methods)
    // private void createID()
    // {
    // idNum = "[error: "+System.currentTimeMillis()+"."+ count++ +" ] ";
    // }

    public void printStackTrace() {

        super.printStackTrace();
        System.err.println("Caused by:");
        if (cause != null)
            cause.printStackTrace();

    }

    public void printStackTrace(java.io.PrintStream stream) {

        super.printStackTrace(stream);
        stream.println("Caused by:");
        if (cause != null)
            cause.printStackTrace(stream);
    }

    public void printStackTrace(java.io.PrintWriter writer) {

        super.printStackTrace(writer);
        writer.println("Caused by:");
        if (cause != null)
            cause.printStackTrace(writer);
    }

    /**
     * Add additional message
     * 
     * @param Message
     */
    public void addMessage(Message message) {
        csExceptionImpl.addMessage(message);
    }

    /**
     * Add additional message
     * 
     * @param String
     *            errorKey
     */
    public void addMessage(String errorKey) {
        csExceptionImpl.addMessage(new Message(errorKey));
    }

    /**
     * Add additional message
     * 
     * @param String
     *            errorKey
     * @param Object[]
     *            parameters
     */
    public void addMessage(String errorKey, Object[] parameters) {
        csExceptionImpl.addMessage(new Message(errorKey, parameters));
    }
}