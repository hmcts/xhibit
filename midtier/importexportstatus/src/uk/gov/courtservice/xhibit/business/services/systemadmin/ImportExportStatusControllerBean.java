package uk.gov.courtservice.xhibit.business.services.systemadmin;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportStatusValue;

/**
 * <p>
 * Title: Import/Export notification status session bean
 * </p>
 * <p>
 * Description: Session bean containing the methods to retreive notification
 * statuses for import and export.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="ImportExportStatusController" description="Import Export
 *           Status Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="ImportExportStatusControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Ian Hannaford - Original Author.
 * @author tz0d5m - Modified to use Xdoclet templates.
 * @version $Id: ImportExportStatusControllerBean.java,v 1.2 2005/01/24 15:41:56
 *          tz0d5m Exp $
 */
public class ImportExportStatusControllerBean extends CSSessionBean implements SessionBean {
    private final ImportExportStatusHelper helper = new ImportExportStatusHelper();

    /**
     * This method gets the import/export statuses for the particular court and
     * case.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtId
     *            The court id
     * @param caseId
     *            The case id
     * @return A <code>ImportExportStatusValue</code> Business Value object
     */
    public ImportExportStatusValue getImportExportStatuses(final Integer courtId, final Integer caseId)
            throws ImportExportStatusesControllerException {
        return helper.getImportExportStatuses(courtId, caseId);
    }

    /**
     * This method gets the import/export statuses for the particular court and
     * the case based on casetype and casenumber.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtId
     *            The court id
     * @param caseTypeAndNumber
     *            The case type and case number
     * @return A <code>ImportExportStatusValue</code> Business Value object
     */
    public ImportExportStatusValue getImportExportStatuses(final Integer courtId, final String caseTypeAndNumber)
            throws ImportExportStatusesControllerException {
        return helper.getImportExportStatuses(courtId, caseTypeAndNumber);
    }
}