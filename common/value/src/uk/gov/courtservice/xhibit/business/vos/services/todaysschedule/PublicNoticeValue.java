package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class PublicNoticeValue extends CSAbstractValue {
	private static final long serialVersionUID = -3607797250196407377L;
    private Integer courtRoomId;

    private ArrayList publicNotices = new ArrayList();

    /**
     * @roseuid 3DC96C1B032B
     */
    public PublicNoticeValue() {

    }

    public PublicNoticeValue(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public ArrayList getPublicNotices() {
        return publicNotices;
    }

    public void addPublicNotice(PublicNoticeDetailValue publicNotice) {
        publicNotices.add(publicNotice);
    }
}