package uk.gov.courtservice.framework.exception;

/**
 * Base exception class for application exceptions in the CS Hub framework.
 * Supports exception chaining, maintaining the cause and providing getCause()
 * method. Allows additional messages to be provided both for the user (use of
 * Message class) and the developer (String errorMessage). Creation date:
 * (10/4/01 1:15:43 PM)
 * 
 * @author: Pete Raymond
 * @author: Kevin Buckthorpe
 * @author: Bal Bhamra
 */
public class CSRecoverableException extends Exception implements CSException {
    protected Throwable cause;

    protected boolean isLogged;

    protected String idNum;

    private static int count = 0;

    private CSExceptionImpl csExceptionImpl = new CSExceptionImpl();

    public CSRecoverableException() {
        super();
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CSRecoverableException(Throwable cause) {
        super(cause.getMessage());
        this.cause = cause;
        csExceptionImpl.createId();
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CSRecoverableException(String errorKey, String logMessage, Throwable cause) {
        super(logMessage);
        this.cause = cause;
        csExceptionImpl.addMessage(new Message(errorKey));
        csExceptionImpl.createId();
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CSRecoverableException(String errorKey, String logMessage) {
        super(logMessage);
        csExceptionImpl.addMessage(new Message(errorKey));
        csExceptionImpl.createId();
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CSRecoverableException(String errorKey, Object[] parameters, String logMessage) {
        super(logMessage);
        csExceptionImpl.addMessage(new Message(errorKey, parameters));
        csExceptionImpl.createId();
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CSRecoverableException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(logMessage);
        this.cause = cause;
        csExceptionImpl.addMessage(new Message(errorKey, parameters));
        csExceptionImpl.createId();
    }

    /**
     * @return Throwable: exception wrapped by this instance
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return String: the error message for logging
     */
    public String getMessage() {
        String msg = super.getMessage();

        if (msg != null) {
            return idNum + msg;
        } else {
            return idNum;
        }
    }

    /**
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
     * @return Message the message for user of application or null if not
     *         present
     */
    public Message getUserMessageAsMessage() {
        return csExceptionImpl.getUserMessageAsMessage();
    }

    /**
     * This method never returns null
     * 
     * @return Message[] the messages for user of application
     */
    public Message[] getUserMessagesAsMessages() {
        return csExceptionImpl.getUserMessagesAsMessages();
    }

    /**
     * @return the error ID of this exception instance
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

    // THIS METHOD HAS MOVED TO CSExceptionImpl AS A PROTECTED METHOD.
    // It is not in CSException interface however (cannot have protected
    // methods)
    // private void createID()
    // {
    // idNum = "[error: " + System.currentTimeMillis() + "." + count++ + " ]
    // ";
    // }
}