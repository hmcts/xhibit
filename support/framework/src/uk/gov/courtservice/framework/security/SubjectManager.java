package uk.gov.courtservice.framework.security;

import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.Subject;

import uk.gov.courtservice.framework.security.exception.SubjectManagerException;
import uk.gov.courtservice.framework.services.CSServices;
import weblogic.security.Security;

/**
 * <p>
 * Title: AccessInfoFactory
 * </p>
 * <p>
 * Description: Concrete instances of this class are found using the discovery
 * pattern.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: SubjectManager.java,v 1.4 2014/06/20 18:03:08 atwells Exp $
 */
public abstract class SubjectManager {

    private static SubjectManager instance;

    /**
     * Get the singleton using the discovery pattern.
     * 
     * @return the singleton instance.
     */
    public static synchronized SubjectManager getInstance() {
        if (instance == null) {
            instance = createSubjectManager();
        }
        return instance;
    }

    /**
     * Runs a privileged exception action as the current subject
     * 
     * @param user
     * @param action
     * @return
     * @throws PrivilegedActionException
     */
    public Object runAs(PrivilegedExceptionAction action) throws PrivilegedActionException {
        return runAs(getCurrentSubject(), action);
    }

    /**
     * Runs a privileged action
     * 
     * @param user
     * @param action
     * @return
     */
    public Object runAs(PrivilegedAction action) {
        return runAs(getCurrentSubject(), action);
    }

    /**
     * Runs a privileged exception action as the current subject
     * 
     * @param user
     * @param action
     * @return
     * @throws PrivilegedActionException
     */
    public Object runAs(Subject user, PrivilegedExceptionAction action) throws PrivilegedActionException {
//        if (user != null) {
//            return Security.runAs(user, action);
//        } else {
            try {
                return action.run();
            } catch (Exception e) {
                throw new PrivilegedActionException(e);
            }
//        }
    }

    /**
     * Runs a privileged action
     * 
     * @param user
     *            the subject to use
     * @param action
     *            the action to execute
     * @return the value from the action
     */
    public Object runAs(Subject user, PrivilegedAction action) {
        if (user != null) {
            return Security.runAs(user, action);
        } else {
            return action.run();
        }
    }

    /**
     * Get the current current subject.
     */
    public abstract Subject getCurrentSubject();

    /**
     * Use the discovery pattern to discover a new instance of the subject
     * manager.
     * 
     * @return a new instance of the subject manager
     * @throws SubjectManagerException
     *             if an error occures
     */
    private static SubjectManager createSubjectManager() {
        SubjectManager subjectManager = (SubjectManager) CSServices.getDiscoveryServices().createInstance(
                SubjectManager.class);
        if (subjectManager != null) {
            return subjectManager;
        }
        throw new SubjectManagerException("Could not discover a concrete implementation of SubjectManager.");
    }
}
