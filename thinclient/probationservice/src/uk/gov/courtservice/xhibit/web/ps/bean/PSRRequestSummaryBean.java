package uk.gov.courtservice.xhibit.web.ps.bean;

/**
 * <p>Title: PSRRequestSummaryBean</p>
 * <p>Description: This holds summary information for a given PSR Request</p>
 * 
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.10 $
 * $Log: PSRRequestSummaryBean.java,v $
 * Revision 1.10  2006/06/05 12:32:27  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.9  2006/05/31 14:26:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.8  2006/04/26 09:01:56  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade
 *
 * Revision 1.7  2004/03/11 16:42:23  qzd3k3
 * Code linting.
 *
 * Revision 1.5.86.1  2004/03/03 13:25:16  tzj8k5
 * 55563 PSR Performance Fix
 *
 * Revision 1.6  2004/02/24 08:13:10  tzj8k5
 * PSR Request update to use FLR
 *
 * Revision 1.5  2003/03/26 16:54:52  fz0n8j
 * Bug fixes.
 *
 * Revision 1.4  2003/03/17 11:32:21  fz0n8j
 * Added revision cvs comments. ecawley
 *
 * Revision 1.3  2003/03/11 16:31:45  fz0n8j
 * Added CVS log comments - ecawley
 *
 */

import uk.gov.courtservice.xhibit.web.framework.bean.AbstractBean;

public class PSRRequestSummaryBean extends AbstractBean implements Comparable {
    /**
     * The name of the defendant
     */
    private String defendantName;

    /**
     * The case number
     */
    private String caseNumber;

    /**
     * The court number
     */
    private String courtNumber;

    /**
     * The Status of the request
     */
    private String status;

    /**
     * Construct a request summary from the defendant name, the case number, the
     * court number and the status
     * 
     * @param newId
     *            the id for the bean
     * @param newDefendantName
     *            name of the defendant
     * @param newCaseNumber
     *            the case number
     * @param newCourtNumber
     *            the number of the court
     * @param newStatus
     *            the status of the request
     * @throws IllegalArgumentException
     *             if any of the String arguments are null
     */
    public PSRRequestSummaryBean(long newId, String newDefendantName, String newCaseNumber, String newCourtNumber,
            String newStatus) throws IllegalArgumentException {
        super(newId);
        setDefendantName(newDefendantName);
        setCaseNumber(newCaseNumber);
        if (newCourtNumber == null) {
            setCourtNumber("");
        } else {
            setCourtNumber(newCourtNumber);
        }
        setStatus(newStatus);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the name of the defendant
     */
    public String getDefendantName() {
        return defendantName;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantName
     *            the name of the defendant
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantName(String newDefendantName) throws IllegalArgumentException {
        if (newDefendantName == null) {
            throw new IllegalArgumentException("newDefendantName");
        }
        defendantName = newDefendantName;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the case number
     */
    public String getCaseNumber() {
        return caseNumber;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCaseNumber
     *            the case number
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCaseNumber(String newCaseNumber) throws IllegalArgumentException {
        if (newCaseNumber == null) {
            throw new IllegalArgumentException("newCaseNumber");
        }
        caseNumber = newCaseNumber;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the court number
     */
    public String getCourtNumber() {
        return courtNumber;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCourtNumber
     *            the court number
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCourtNumber(String newCourtNumber) throws IllegalArgumentException {
        if (newCourtNumber == null) {
            throw new IllegalArgumentException("newCourtNumber");
        }
        courtNumber = newCourtNumber;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the status of the request
     */
    public String getStatus() {
        return status;
    }

    /**
     * Standard java bean setter
     * 
     * @param newStatus
     *            the status
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setStatus(String newStatus) throws IllegalArgumentException {
        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus");
        }
        status = newStatus;
    }

    public int compareTo(Object o) {
        if (o instanceof PSRRequestSummaryBean) {
            PSRRequestSummaryBean bean = (PSRRequestSummaryBean) o;
            return defendantName.compareToIgnoreCase(bean.getDefendantName());
        }
        return 0;
    }

}
