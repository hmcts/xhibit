package uk.gov.courtservice.framework.security;

import javax.security.auth.Subject;

import weblogic.security.Security;

/**
 * <p>
 * Title: SecurityAccessInfoFactory
 * </p>
 * <p>
 * Description: Use the security configuration to get the current user! This
 * should only be used on the server.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: SecuritySubjectManager.java,v 1.1 2006/05/19 08:06:05 bzjrnl
 *          Exp $
 */
public class SecuritySubjectManager extends SubjectManager {
    /**
     * Get the access info from the security wrapper
     */
    public Subject getCurrentSubject() {
        // The following requires a weblogic subject manager to be installed,
        // this does not work from the client when using the wlclient.jar!
        return Security.getCurrentSubject();
    }
}
