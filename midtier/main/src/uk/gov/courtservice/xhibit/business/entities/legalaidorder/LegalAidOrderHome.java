package uk.gov.courtservice.xhibit.business.entities.legalaidorder;

import java.util.Collection;
import java.util.Date; 

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface LegalAidOrderHome extends EJBLocalHome {

    public LegalAidOrder create(
            Integer crestLeoId, Integer defendantOnCaseId, String userDisplayName) throws CreateException;
    
    /**
     * used for defendants in public rep
     */
    public LegalAidOrder create(Integer defendantOnCaseId, Integer caseProsAgencyId, Date orderDate, String grantedBy, String psdRoRef, Integer numberOfAdvocates, Integer numberOfQcs, Integer crestLeoId, String userDisplayName) throws CreateException;


    public LegalAidOrder findByPrimaryKey(Integer legalAidOrderId) throws FinderException;
    
    public Collection findByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException;
    
    public Collection findAllByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException;
    
    public Collection findByCaseProsAgencyId(Integer caseProsAgencyId) throws FinderException;
    
    public Collection findAllByCaseProsAgencyId(Integer caseProsAgencyId) throws FinderException;

}
