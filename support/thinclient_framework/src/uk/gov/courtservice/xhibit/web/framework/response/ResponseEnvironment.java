package uk.gov.courtservice.xhibit.web.framework.response;

import uk.gov.courtservice.xhibit.web.framework.control.ControlEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Response Environment
 * </p>
 * <p>
 * Description: The environment to evaluate an response in.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * 
 */

public interface ResponseEnvironment extends ControlEnvironment {

    /**
     * <p>
     * Method for forwarding (server side) a request, through a
     * RequestDespatcher obtained from the ServletContext.
     * </p>
     * 
     * @param path
     *            a String specifying the location of the resource
     * @throws IllegalArgumentException
     *             if the path is null or an invalid location
     * @throws IllegalStateException
     *             if it or redirect has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    public void forward(String path) throws IllegalArgumentException, IllegalStateException, FrameworkException;

    /**
     * <p>
     * Method for redirecting (client side) a request, through the response it
     * provides symatry with the forward (server side) method. Note before
     * sending we check the path is valid.
     * </p>
     * 
     * @param location
     *            specifying the location of the resource
     * @throws IllegalArgumentException
     *             if the location is null or an invalid location
     * @throes IllegalStateException if it or forward has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    public void redirect(String location) throws IllegalArgumentException, IllegalStateException, FrameworkException;

    /**
     * <p>
     * Send bytes to the client with a response mime type
     * </p>
     * 
     * @param mimeType
     *            a String specifying the mimetype
     * @param content
     *            the bytes to send . . .
     * @throws FrameworkException
     *             if an error occurs
     */
    public void sendBytes(String mimeType, byte[] content) throws FrameworkException;

}
