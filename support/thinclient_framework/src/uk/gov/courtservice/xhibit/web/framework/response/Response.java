package uk.gov.courtservice.xhibit.web.framework.response;

import uk.gov.courtservice.xhibit.web.framework.control.Control;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Response
 * </p>
 * <p>
 * Description: This interface describes a response that can be performed to an
 * action
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
 * $Revision: 1.3 $ $Log: Response.java,v $
 * $Revision: 1.3 $ Revision 1.3  2006/06/05 12:30:25  bzjrnl
 * $Revision: 1.3 $ Change: TI901
 * $Revision: 1.3 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * $Revision: 1.3 $ $Revision: 1.3 $ Revision 1.2
 * 2006/05/31 14:23:54 bzjrnl $Revision: 1.3 $ Change: TI901 $Revision: 1.3 $
 * Comment: Weblogic Upgrade - Standadise code formatting $Revision: 1.3 $
 * Revision 1.1 2003/03/21 11:48:23 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.2 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * Revision 1.1 2003/03/19 11:38:09 fz0n8j Promoted response to a first class
 * object.
 * 
 * 
 */
public interface Response extends Control {
    /**
     * <p>
     * This method is invoked by the framework to perform the requested
     * response.
     * </p>
     * 
     * @param responseEnvironment
     *            the environment to evaluate the response in
     * @throws IllegalArgumentException
     *             if the environment is null
     * @throws FrameworkException
     *             if an error occurs
     */
    public void performResponse(ResponseEnvironment responseEnvironment) throws IllegalArgumentException,
            FrameworkException;
}