package uk.gov.courtservice.xhibit.courtlog.vos;

/**
 * <p>
 * Title: MultiCaseCourtLogViewValue
 * </p>
 * <p>
 * Description: An extension of the CourtLogViewValue which supports multiple
 * case Ids. Currently this is used for Joinder Indictments to indicate which
 * cases are involved in the join.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: MultiCaseCourtLogViewValue.java,v 1.1 2004/04/08 15:36:49
 *          pznwc5 Exp $
 */
public class MultiCaseCourtLogViewValue extends CourtLogViewValue {
    
	private static final long serialVersionUID = -2585194052304434137L;
	Integer[] caseIds;

    public MultiCaseCourtLogViewValue() {
        super();
    }

    public MultiCaseCourtLogViewValue(Integer version) {
        super(version);
    }

    public Integer[] getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(Integer[] caseIds) {
        this.caseIds = caseIds;
    }
}
