package uk.gov.courtservice.xhibit.business.entities.refhearingtype;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RefHearingTypeHome extends EJBLocalHome {

    /*
     * create replaced with that from Bean... not sure if I should replace the
     * Bean with this one commented out! It is 02:00 ;o( public RefHearingType
     * create(String hearingTypeCode, String hearingTypeDesc, String category,
     * Integer seqNo, Integer listSequence, Integer courtId, String obsInd)
     * throws CreateException;
     */
    public RefHearingType create(Integer courtId, String category, String hearingTypeCode, String hearingTypeDesc,
            Integer listSequence, String obsInd, Integer seqNo, String userDisplayName) throws CreateException;

    public RefHearingType findByPrimaryKey(Integer refHearingTypeId) throws FinderException;

    public Collection findByCourtIdAndCategory(Integer courtId, String category) throws FinderException;    
}