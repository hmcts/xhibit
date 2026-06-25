package uk.gov.courtservice.xhibit.business.services.migration;


import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2026
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Owain Greener
 * @version 1.0
 * @ejb.bean name="MigrateCaseController" description="Migrate Case
 *           Controller Bean" type="Stateless" view-type="both"
 *           jndi-name="MigrateCaseControllerHome"
 *           local-jndi-name="MigrateCaseControllerLocalHome"
 * @ejb.transaction type="Required"
 */
public class MigrateCaseControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

    private MigrateCaseHelper migrateCaseHelper = new MigrateCaseHelper();
	
    /**
     * Checks if the case is migrated and returns the details
     * 
     * @return MigrationDetail object.
     * @ejb.interface-method view-type="both"
     */
    public MigrationDetail getMigrationDetails(int caseId, MigrationMessageType messageType) {
    	return migrateCaseHelper.getMigrationDetails(caseId, messageType);
    }
    
    /**
     * Checks if the case is migrated and is being viewed in read only
     * 
     * @return a boolean.
     * @ejb.interface-method view-type="both"
     */
    public boolean isCaseMigratedAndInReadOnly(boolean isInEditMode, int caseId) {
    	return migrateCaseHelper.isCaseMigratedAndInReadOnly(isInEditMode, caseId);
    }
    
    /**
     * Checks if the case is migrated.
     * 
     * @return a boolean.
     * @ejb.interface-method view-type="both"
     */
    public boolean isCaseMigrated(int caseId) {
    	return migrateCaseHelper.isCaseMigrated(caseId);
    }
}