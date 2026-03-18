package uk.gov.courtservice.xhibit.courtlog.vos;

/**
 * <p>
 * Title: MultiCaseCourtLogCRUDValue
 * </p>
 * <p>
 * Description: An extension of the CourtLogCRUDValue which allows for multiple
 * case ids. Currently used for the Joinder court log event where the same court
 * log event is created for each case invloved in the Joinder.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: MultiCaseCourtLogCRUDValue.java,v 1.2 2004/04/21 13:09:10
 *          tz0d5m Exp $
 */
public class MultiCaseCourtLogCRUDValue extends CourtLogCRUDValue {
    
	private static final long serialVersionUID = -4070603215325686881L;
	private Integer[] caseIds;

    public MultiCaseCourtLogCRUDValue() {
        super();
    }

    public Integer[] getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(Integer[] caseIds) {
        if ((caseIds == null) || (caseIds.length == 0)) {
            throw new IllegalArgumentException("There must be at least one caseId in the caseIds array provided.");
        }

        this.caseIds = caseIds;
        // set the first caseId as the 'lead' or 'main' case, this will be
        // looked for when creating the CourtLogViewValue
        setCaseId(caseIds[0]);
    }
}
