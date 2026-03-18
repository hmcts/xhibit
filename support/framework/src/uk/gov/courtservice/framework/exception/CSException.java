package uk.gov.courtservice.framework.exception;

/**
 * <p>
 * Title: CSException
 * </p>
 * <p>
 * Description: Interface for all CS Hub framework exceptions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public interface CSException {
    public Throwable getCause();

    public String getMessage();

    public String getUserMessage();

    public Message getUserMessageAsMessage();

    public String[] getUserMessages();

    public Message[] getUserMessagesAsMessages();

    public boolean isLogged();

    public void setIsLogged(boolean isLogged);

    public String getErrorID();
}