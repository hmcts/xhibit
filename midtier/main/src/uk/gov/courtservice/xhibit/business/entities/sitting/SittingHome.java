package uk.gov.courtservice.xhibit.business.entities.sitting;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface SittingHome extends EJBLocalHome {
    public Sitting create(Integer sittingSequenceNo, String isSittingJudge, Timestamp sittingTime, String sittingNote,
            Integer refJustice1Id, Integer refJustice2Id, Integer refJustice3Id, Integer refJustice4Id,
            String isFloating, Integer refJudgeId, Integer courtSiteId, Integer courtRoomId, String justiceName1,
            String justiceName2, String justiceName3, String justiceName4, String userDisplayName) throws CreateException;

    public Sitting findByPrimaryKey(Integer sittingId) throws FinderException;

    public Collection findByHearingId(Integer hearingId) throws FinderException;

    /**
     * Find the sittings that started in the court room and site after the
     * specified sitting time.
     * 
     * @param time
     * @param courtSiteId
     * @param courtRoomId
     * @return A <code>Collection</code> of <code>Sitting</code> objects
     * @throws FinderException
     */
    public Collection findByTimeCourtSiteRoomId(Timestamp time, Integer courtSiteId, Integer courtRoomId)
            throws FinderException;

    /**
     * Find the sittings that started in the court room, for the specified list
     * id after the passed in time
     * 
     * @param time
     * @param listId
     * @param courtRoomId
     * @return A <code>Collection</code> of <code>Sitting</code> objects
     * @throws FinderException
     */
    public Collection findByDateListAndCourt(Timestamp time, Integer listId, Integer courtRoomId)
            throws FinderException;
}