package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//JDK
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRLinkedCaseListValue;

/**
 * <p>
 * Title: HRLinkedCaseListValueHelper
 * </p>
 * <p>
 * Description: Class to find and transfrom cases to HRLinkedCaseListValues.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class HRLinkedCaseListValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRLinkedCaseListValueHelper.class);

    private CaseMaintainer maintainer;

    /**
     * Default constructor that intantiate a CaseMaintainer.
     */
    public HRLinkedCaseListValueHelper() {
        maintainer = new CaseMaintainer();
    }

    /**
     * This method will find the cases from the caseIds and then transform them
     * to HRLinkedCaseListValues
     * 
     * @param caseIDs
     *            Collection
     * @return Collection of HRLinkedCaseListValue
     * @throws HearingRecordException
     */

    public Collection getLinkedCaseList(Collection caseIDs) throws HearingRecordException {
        log.debug("HRLinkedCaseListValueHelper.getLinkedCaseList(Collection caseIDs) called");
        Collection cases = null;

        cases = this.findLinkedCases(caseIDs);

        log.debug("HRLinkedCaseListValueHelper.getLinkedCaseList(Collection caseIDs) finished");
        return cases;
    }

    // ----------------------------- Private Methods
    // ----------------------------//

    /**
     * This will use the CaseMaintainer to find each case by primary key.
     * 
     * @param caseIDs
     *            Collection
     * @return Collection of Case entities
     * @throws HearingRecordException
     */
    private Collection findLinkedCases(Collection caseIDs) throws HearingRecordException {
        Vector cases = null;
        log.debug("HRLinkedCaseListValueHelper.findLinkedCases(Collection caseIDs) called");

        if (caseIDs.isEmpty()) {
            return cases;
        }
        cases = new Vector();
        Iterator it = caseIDs.iterator();
        while (it.hasNext()) {
            try {
                // find the case, transform to basic and then transfrom to
                // HrValue.
                Case caze = maintainer.findByPrimaryKey((Integer) it.next());
                CaseBasicValue basicValue = maintainer.getCaseBasicValue(caze);
                HRLinkedCaseListValue hrCase = this.buildHrLinkedCaseValue(basicValue);
                cases.addElement(hrCase);
            } catch (ObjectNotFoundException ex) {
                // the case could not be found
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
                throw hex;
            }
        }
        log.debug("HRLinkedCaseListValueHelper.findLinkedCases(Collection caseIDs) finished");
        return cases;
    }

    /**
     * This will build a HRLinkedCaseListValue from a CaseBasicValue
     * 
     * @param basicValue
     *            CaseBasicValue
     * @return HRLinkedCaseListValue
     */
    private HRLinkedCaseListValue buildHrLinkedCaseValue(CaseBasicValue basicValue) {
        log.debug("HRLinkedCaseListValueHelper.buildHrLinkedCaseValue(CaseBasicValue basicValue) called");

        HRLinkedCaseListValue value = new HRLinkedCaseListValue(basicValue.getId());
        value.setCaseNumber(basicValue.getCaseNumber());
        value.setCaseType(basicValue.getCaseType());

        log.debug("HRLinkedCaseListValueHelper.buildHrLinkedCaseValue(CaseBasicValue basicValue) finished");
        return value;
    }
}
