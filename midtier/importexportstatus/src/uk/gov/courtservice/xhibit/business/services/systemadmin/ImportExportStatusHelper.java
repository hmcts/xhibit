package uk.gov.courtservice.xhibit.business.services.systemadmin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.importexportstatus.GetByCaseIdQuery;
import uk.gov.courtservice.xhibit.business.database.query.importexportstatus.GetByCourtIdQuery;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportCaseCourtDetailsValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportStatusVO;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportStatusValue;

/**
 * <p>
 * Title: Import/Export notification status helper
 * </p>
 * <p>
 * Description: Helper class to compose the import and export notification
 * statuses for a particular case and court.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version $Id: ImportExportStatusHelper.java,v 1.5 2005/08/16 14:47:44 szn20z
 *          Exp $
 */
public class ImportExportStatusHelper {
    private static final String CASE_TYPE = "T";

    private static final String CASE_NUMBER = "N";

    // The query that will retrieve import/export status records by court ID
    private GetByCourtIdQuery getByCourtIdQuery = new GetByCourtIdQuery();

    // The query that will retrieve import/export status records by case ID
    private GetByCaseIdQuery getByCaseIdQuery = new GetByCaseIdQuery();

    private static final Logger log = CSServices.getLogger(ImportExportStatusHelper.class);

    public ImportExportStatusHelper() {
    }

    /**
     * This method gets the import/export statuses for the particular court and
     * case.
     * 
     * @param courtId -
     *            the court id
     * @param caseId -
     *            the case id
     * @return - a ImportExportStatusHelper Business Value object
     * @todo Change Integer parameters to Integer when full pk int -> long
     *       changes are made
     */
    public ImportExportStatusValue getImportExportStatuses(Integer courtId, Integer caseId)
            throws ImportExportStatusesControllerException {
        log.debug("getImportExportStatuses(" + courtId + ", " + caseId + ")");

        // create a new ImportExportStatusValue and populate values
        ImportExportStatusValue importExportStatusVO = new ImportExportStatusValue();

        // if we have a case then populate the importExportStatusVO with case
        // data.
        if (caseId != null) {
            // get the case basic value using internal method using caseid
            XhbCaseBasicValue caseBasicValue = getCaseByCaseIdOrCaseTypeAndNumber(courtId, caseId, null);
            log.debug("Setting the casetype = " + caseBasicValue.getCaseType() + " and caseNumber = "
                    + caseBasicValue.getCaseNumber());
            importExportStatusVO.setCaseType(caseBasicValue.getCaseType());
            importExportStatusVO.setCaseNumber(caseBasicValue.getCaseNumber().toString());
            // get the notification statuses for the case
            importExportStatusVO.setCaseStatuses(getImportExportStatusesByCaseId(caseId));
        }

        // get the notification statuses for the court
        importExportStatusVO.setCourtStatuses(getImportExportStatusesByCourtId(courtId));

        // return the VO
        log.debug("getImportExportStatuses(courtId, caseId) exited");
        return importExportStatusVO;
    }

    /**
     * This method gets the import/export statuses for the particular court and
     * the case based on casetype and casenumber.
     * 
     * @param courtId -
     *            the court id
     * @param caseTypeAndNumber -
     *            the case type and case number
     * @return - a ImportExportStatusHelper Business Value object
     * @todo Change Integer parameter to Integer when full pk int -> long
     *       changes are made
     */
    public ImportExportStatusValue getImportExportStatuses(Integer courtId, String caseTypeAndNumber)
            throws ImportExportStatusesControllerException {
        // get the case basic value using internal method using
        // casetypeandnumber
        log.debug("getImportExportStatuses(" + courtId + ", " + caseTypeAndNumber
                + ") using caseType and number entered");

        // get the case basic value using internal method using caseid
        XhbCaseBasicValue caseBasicValue = getCaseByCaseIdOrCaseTypeAndNumber(courtId, null, caseTypeAndNumber);

        // create a new ImportExportStatusValue and populate values
        ImportExportStatusValue importExportStatusValue = new ImportExportStatusValue();

        log.debug("Setting the caseid = " + caseBasicValue.getPrimaryKey() + " casetype = "
                + caseBasicValue.getCaseType() + " and caseNumber = " + caseBasicValue.getCaseNumber());
        Integer caseId = caseBasicValue.getPrimaryKey();
        importExportStatusValue.setCaseType(caseBasicValue.getCaseType());
        importExportStatusValue.setCaseNumber(caseBasicValue.getCaseNumber().toString());

        // get the notification statuses for the court
        importExportStatusValue.setCourtStatuses(getImportExportStatusesByCourtId(courtId));

        // get the notification statuses for the case
        importExportStatusValue.setCaseStatuses(getImportExportStatusesByCaseId(caseId));

        // return the VO
        log.debug("getImportExportStatuses using caseType and number exited");
        return importExportStatusValue;

    }

    /**
     * Utility method to get the import/export statuses basic value objects for
     * a particular court id.
     * 
     * @param courtId -
     *            the court id
     * @return Arralist - a collection of ??
     */
    private ArrayList getImportExportStatusesByCourtId(Integer courtId) {
        String methodName = "getImportExportStatusesByCourtId(" + courtId + ")";
        log.debug(methodName + " entered");

        // arraylist to return
        ArrayList values = new ArrayList();

        Collection rows = getByCourtIdQuery.getResult(courtId);

        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            ImportExportStatusVO item = (ImportExportStatusVO) iter.next();
            // Pass this is as an argument to the business value object,
            // that contains defendant details
            ImportExportCaseCourtDetailsValue impExpValue = new ImportExportCaseCourtDetailsValue(item);
            values.add(impExpValue);
        }

        // turn into an array list to return
        log.debug(methodName + " exited ");
        return values;
    }

    /**
     * Utility method to get the import/export statuses basic value objects for
     * a particular case id.
     * 
     * @param caseId -
     *            the case id
     * @return Arralist - a collection of ??
     */
    private ArrayList getImportExportStatusesByCaseId(Integer caseId) throws ImportExportStatusesControllerException {
        String methodName = "getImportExportStatusesByCaseId(" + caseId + ")";
        log.debug(methodName + " entered");

        // arraylist to return
        ArrayList values = new ArrayList();

        Collection rows = getByCaseIdQuery.getResult(caseId);

        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            ImportExportStatusVO item = (ImportExportStatusVO) iter.next();

            // Pass this is as an argument to the business value object,
            // that contains defendant details
            ImportExportCaseCourtDetailsValue impExpValue = new ImportExportCaseCourtDetailsValue(item);

            // if the defendant on case id is populated then find the
            // defendant
            if (item.getDefendantOnCaseId() != null) {
                log.debug("*** DefendnatOnCase Exisits find using defendantOnCaseId = " + item.getDefendantOnCaseId()
                        + "  ***");
                try {
                    XhbDefendantOnCaseBasicValue defOnCaseBVO = XhbDefendantOnCaseBeanHelper.findByPrimaryKeyValue(item
                            .getDefendantOnCaseId());

                    log.debug("*** DefendantOnCase found = " + defOnCaseBVO + " ***");

                    // find the defendant using the defendant id
                    XhbDefendantBasicValue defBVO = XhbDefendantBeanHelper.findByPrimaryKeyValue(defOnCaseBVO
                            .getDefendantId());

                    log.debug("*** Defendant found = " + defBVO + " ***");

                    impExpValue.setDefFirstname(defBVO.getFirstName());
                    impExpValue.setDefSurname(defBVO.getSurname());
                    impExpValue.setDefMiddlename(defBVO.getMiddleName());
                    impExpValue.setDefInitials(defBVO.getInitials());
                } catch (ObjectNotFoundException ex) {
                    CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
                    throw new ImportExportStatusesControllerException(
                            "import_export_status.defendant_on_case_or_defendant_not_found",
                            "The Defendant Cannot be found, either due to the defendantOnCaseId being NULL or the DefendantId being NULL",
                            ex);
                }
            }

            // Add to arraylist to be returned
            values.add(impExpValue);
        }

        log.debug(methodName + " exited ");
        return values;
    }

    /**
     * Utility method to get the case basic value object. You can either specify
     * caseid, or casetypeAndNumber. Uses the case id if populated else uses the
     * casetypeAndNumber.
     * 
     * @param courtId -
     *            the court id
     * @param caseId -
     *            the case id
     * @param caseTypeAndNumber -
     *            the case type and number
     * @return - a CaseBasicValue object
     */
    private XhbCaseBasicValue getCaseByCaseIdOrCaseTypeAndNumber(Integer courtId, Integer caseId,
            String caseTypeAndNumber) throws ImportExportStatusesControllerException {
        String methodName = "getCaseByCaseIdOrCaseTypeAndNumber( " + courtId + ", " + caseId + ", " + caseTypeAndNumber
                + " )";
        log.debug(methodName + " entered ");

        XhbCaseBasicValue caseBasicVO = null;

        // both values cannot be null
        if (caseId == null && caseTypeAndNumber == null) {
            log.debug(" Unexpected null arguments, caseId and casetypeAndNumber cannot null ");
            throw new IllegalArgumentException("Unexpected null arguments, caseId and casetypeAndNumber cannot null");
        }

        try {
            // if caseid is populated then use this
            if (caseId != null) {
                log.debug(" Finding case using CaseId = " + caseId);
                // find case using the primary key case id
                caseBasicVO = XhbCaseBeanHelper.findByPrimaryKeyValue(caseId);
            } else // else use the caseTypeAndNumber
            {
                try {
                    // split the case type and the case number out.
                    String caseType = getCaseTypeOrNumber(caseTypeAndNumber, CASE_TYPE);
                    Integer caseNumber = new Integer(getCaseTypeOrNumber(caseTypeAndNumber, CASE_NUMBER));

                    log.debug(" Finding case using caseType (" + caseType + ") and caseNumber(" + caseNumber + ")");
                    // find case using the case type and number

                    caseBasicVO = XhbCaseBeanHelper.findByNumberTypeAndCourtValue(caseNumber, caseType, courtId);

                } catch (NumberFormatException nfex) {
                    CSServices.getDefaultErrorHandler().handleError(nfex, this.getClass());
                    throw new ImportExportStatusesControllerException(
                            "import_export_status.case_type_and_number_invalid", "The caseTypeAndNumber("
                                    + caseTypeAndNumber + ") were invalid.", nfex);
                }
            }
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new ImportExportStatusesControllerException("import_export_status.case_not_found",
                    "Case with caseId (" + caseId + ") or caseNumber (" + caseTypeAndNumber + ") cannot be found.", e);
        }

        log.debug(methodName + " exited. ");
        return caseBasicVO;
    }

    /**
     * Utility method to get the caseType or caseNumber from the
     * caseTypeAndNumber String.
     * 
     * @param caseTypeAndNumber -
     *            caseTypeAndNumber as a string
     * @param typeOrNumber -
     *            flag to determine whether you want type or number returned
     * @return Object - will either be a string for case type or an Integer for
     *         number
     */
    private String getCaseTypeOrNumber(String caseTypeAndNumber, String typeOrNumber) {
        // if CASE_TYPE then take the first char
        if (typeOrNumber.equals(CASE_TYPE)) {
            return caseTypeAndNumber.substring(0, 1);
        }
        // else take all chars apart from the first
        else {
            return caseTypeAndNumber.substring(1, caseTypeAndNumber.length());
        }
    }
}