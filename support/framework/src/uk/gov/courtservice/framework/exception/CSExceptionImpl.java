package uk.gov.courtservice.framework.exception;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>
 * Title: CSExceptionImpl
 * </p>
 * <p>
 * Description: Implementation class for CSExeption interface. Base exception
 * class for CSRecoverableException and CSUnrecoverableException in CS Hub
 * framework. Contains implementations for interface methods which can be
 * overidden by exception sub classes. Allows additional messages to be provided
 * both for the user (use of Message class) and the developer (String
 * errorMessage).
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class CSExceptionImpl implements CSException, Serializable {
    protected static final Message UNEXPECTED_ERROR_MESSAGE = new Message(Message.UNEXPECTED_ERROR);

    protected final ArrayList userMessages = new ArrayList();

    protected Throwable cause;

    protected Message userMessage;

    protected boolean isLogged;

    protected String idNum;

    private static int count = 0;

    public CSExceptionImpl() {
    }

    /**
     * This requires implementation but not called. Potential calling classes
     * extend Exception so deal with casue differently
     * 
     * @return Throwable
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return String: This is a temp implement and will be overridden
     */
    public String getMessage() {
        return "";
    }

    /**
     * @return String the message for user of application
     */
    public String getUserMessage() {
        return getUserMessageAsMessage().getMessage();
    }

    /**
     * @return String[] the message for user of application
     */
    public String[] getUserMessages() {

        Message[] messages = getUserMessagesAsMessages();
        String[] strings = new String[messages.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = messages[i].getMessage();
        }
        return strings;
    }

    /**
     * @return Message the message for user of application or null if not
     *         present
     */
    public Message getUserMessageAsMessage() {
        if (userMessages.size() > 0) {
            return (Message) userMessages.get(0);
        } else {
            return UNEXPECTED_ERROR_MESSAGE;
        }
    }

    /**
     * @return Message[] the messages for user of application
     */
    public Message[] getUserMessagesAsMessages() {
        if (userMessages.size() > 0) {
            // convert the ArrayList to an Array to return
            return (Message[]) userMessages.toArray(new Message[userMessages.size()]);
        }
        return new Message[] { UNEXPECTED_ERROR_MESSAGE };
    }

    /**
     * Add additional message
     * 
     * @param Message
     */
    public void addMessage(Message message) {
        userMessages.add(message);
    }

    /**
     * Add additional message
     * 
     * @param String
     *            errorKey
     */
    public void addMessage(String errorKey) {
        userMessages.add(new Message(errorKey));
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
        userMessages.add(new Message(errorKey, parameters));
    }

    /**
     * Gets the logged flag
     * 
     * @return boolean
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
     * @return the error ID of this exception instance
     */
    public String getErrorID() {
        return this.idNum;
    }

    /**
     * THIS IS A PROTECTED METHOD. It is not in CSException interface however
     * (cannot have protected methods)
     */
    protected void createId() {
        idNum = "[error: " + System.currentTimeMillis() + "." + count++ + " ] ";
    }
}