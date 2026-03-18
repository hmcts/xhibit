package uk.gov.courtservice.xhibit.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: XHIBIT 2 Case Helper
 * </p>
 * <p>
 * Description: Standard place to hold any utilities that can retrieve
 * information pertaining to a case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class CaseHelper {

    public CaseHelper() {
    }

    public Collection getScheduledHearings(Integer caseId) throws CaseControllerException {
        return XhibitDelegateHelper.getCaseDelegate().getScheduledHearings(caseId);
    }
    
    public Collection getScheduledHearings(Integer caseId, Date fromDate, Date toDate) throws CaseControllerException {
        return XhibitDelegateHelper.getCaseDelegate().getScheduledHearings(caseId, fromDate, toDate);
    }

    public Collection getScheduledHearings(String caseType, Integer caseNumber, Integer courtId)
            throws CaseControllerException {
        return XhibitDelegateHelper.getCaseDelegate().getScheduledHearings(caseType, caseNumber, courtId);
    }

    public Collection getScheduledHearingDates(Collection listOfShv) {
        ArrayList al = new ArrayList();
        Iterator iter = listOfShv.iterator();
        while (iter.hasNext()) {
            ScheduledHearingValue item = (ScheduledHearingValue) iter.next();
            if (item.getScheduledHearingDate() != null) {
                al.add(item.getScheduledHearingDate().getTime());
            }
        }
        return al;
    }
}