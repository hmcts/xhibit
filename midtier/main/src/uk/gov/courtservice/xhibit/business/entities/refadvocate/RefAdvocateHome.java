package uk.gov.courtservice.xhibit.business.entities.refadvocate;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;

public interface RefAdvocateHome extends EJBLocalHome {// weblogic.ejb.QueryLocalHome//

    public RefAdvocate create(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName) throws CreateException;
    
    public RefAdvocate create(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName, Integer refChamberId) throws CreateException;
    
	public RefAdvocate create(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
			String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName,
			RefLegalRepresentative refLegalRep, RefChamber refChamber) throws CreateException;

    public RefAdvocate findByPrimaryKey(Integer refAdvocateId) throws FinderException;
    // public Collection findByCourtId(Integer courtId) throws
    // FinderException;
    
    public RefAdvocate findByRefLegalRepresentativeId(Integer refLegalRepresentativeId)
    throws FinderException;
    
    public Collection findCounselBySurnameInitialsCourtId(String surname, String initials, Integer courtId) throws FinderException;
    
    public Collection findCounselByBarNumberCourtId(Integer barNumber, Integer courtId) throws FinderException;
}