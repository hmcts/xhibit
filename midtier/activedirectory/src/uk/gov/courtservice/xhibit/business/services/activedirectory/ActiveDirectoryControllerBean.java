package uk.gov.courtservice.xhibit.business.services.activedirectory;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryGroup;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryGroupHierarchy;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryService;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryTerminal;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryUser;

/**
 * <p>
 * Title: User session controller bean class
 * </p>
 * <p>
 * Description: This class allows access to the ActiveDirectoryService which
 * requires system admin level security.
 * </p>
 * 
 * @ejb.bean name="ActiveDirectoryController" description="Active Directory
 *           Bean" type="Stateless" view-type="local"
 *           jndi-name="ActiveDirectoryControllerHome"
 *           local-jndi-name="ActiveDirectoryControllerLocalHome"
 * @ejb.transaction type="Required"
 * @ejb.security-identity run-as="XHBWeblogic"
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * @author Will Fardell, Xdevelopment
 * @version $Id: ActiveDirectoryControllerBean.java,v 1.3 2006/04/26 09:01:44
 *          bzjrnl Exp $
 */
public class ActiveDirectoryControllerBean extends CSSessionBean implements SessionBean {
    private static final long serialVersionUID = 7719593240067655208L;

    private ActiveDirectoryService activeDirectoryService;

    /**
     * Reads the environment entries and looks up the home interface
     */
    public void ejbCreate() {
        activeDirectoryService = ActiveDirectoryServiceUtil.getActiveDirectoryService();
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public ActiveDirectoryUser getUser(String principalName) {
        return activeDirectoryService.getUser(principalName);
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public ActiveDirectoryGroup getRootGroup() {
        return activeDirectoryService.getRootGroup();
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public ActiveDirectoryGroupHierarchy getGroupHierarchy() {
        return activeDirectoryService.getGroupHierarchy();
    }
}