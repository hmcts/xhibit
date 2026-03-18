package uk.gov.courtservice.xhibit.integration.services.stub;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: CaseStub
 * </p>
 * <p>
 * Description: Mimics mercator methods which fall into the case area
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 */
public class CaseStub {
    private static final Logger log = CSServices.getLogger(CaseStub.class);

    private static final int LOWER_LIMIT = 1;

    private static final int UPPER_LIMIT = 9999999;

    public CaseStub() {
    }

    public AddCaseValue addNewUCase(AddCaseValue addCaseValue) throws MercatorException {
        if (log.isDebugEnabled()) {
            log.debug("getCase(AddCaseValue addCaseValue) started with " + addCaseValue);
        }

        HashSet allUCasesInSameCourt = new HashSet();
        Collection allUCases = XhbCaseBeanHelper2.findByCaseType("U");
        Iterator iter = allUCases.iterator();
        while (iter.hasNext()) {
            XhbCase temp = (XhbCase) iter.next();
            if (temp.getCourtId().compareTo(addCaseValue.getCourtID()) == 0) {
                allUCasesInSameCourt.add(temp.getCaseNumber());
            }
        }

        int caseNumber = generateNumberInRange(LOWER_LIMIT, UPPER_LIMIT);
        while (allUCasesInSameCourt.contains(new Integer(caseNumber))) {
            caseNumber = generateNumberInRange(LOWER_LIMIT, UPPER_LIMIT);
        }

        XhbCaseBasicValue caseBasicValue = new XhbCaseBasicValue();
        caseBasicValue.setCourtId(addCaseValue.getCourtID());
        caseBasicValue.setCaseType("U");
        caseBasicValue.setCaseNumber(new Integer(caseNumber));
        caseBasicValue.setCaseTitle(addCaseValue.getCaseTitle());
        caseBasicValue.setBailMagCode(addCaseValue.getCaseTitle());
        caseBasicValue.setChargeImportIndicator("C");

        XhbCaseBeanHelper2.createLocal(caseBasicValue);

        addCaseValue.setCaseType("U");
        addCaseValue.setCaseNumber(new Integer(caseNumber));

        return addCaseValue;
    }

    private int generateNumberInRange(int aLowerLimit, int aUpperLimit) {
        Random generator = new Random();
        // get the range, casting to long to avoid overflow problems
        long range = (long) aUpperLimit - (long) aLowerLimit + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * generator.nextDouble());
        return ((int) (fraction + aLowerLimit)) + 10000000;
    }
}
