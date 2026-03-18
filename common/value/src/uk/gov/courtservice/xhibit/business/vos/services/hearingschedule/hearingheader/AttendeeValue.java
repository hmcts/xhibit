package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version $Id: AttendeeValue.java,v 1.3 2006/06/05 12:28:46 bzjrnl Exp $
 *
 * <Change History/>
 *
 * <P>25/02/03 - ARH - First issue.</P>
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class AttendeeValue extends CSAbstractValue {
	private static final long serialVersionUID = 670837448368763952L;
    private Boolean isAttendingSubsequentSH;

    private PersonValue person;

    public AttendeeValue() {
    }

    public Boolean getIsAttendingSubsequentSH() {
        return isAttendingSubsequentSH;
    }

    public void setIsAttendingSubsequentSH(Boolean isAttendingSubsequentSH) {
        this.isAttendingSubsequentSH = isAttendingSubsequentSH;
    }

    public void setPerson(PersonValue person) {
        this.person = person;
    }

    public PersonValue getPerson() {
        return person;
    }
}