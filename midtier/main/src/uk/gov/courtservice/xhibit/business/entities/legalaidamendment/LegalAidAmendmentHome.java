package uk.gov.courtservice.xhibit.business.entities.legalaidamendment;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;


public interface LegalAidAmendmentHome extends EJBLocalHome {

    public LegalAidAmendment create(Date amendmentDate, LegalAidOrder legalAidOrder, String amendmentType,
             String userDisplayName) throws CreateException;
    
    public LegalAidAmendment findByPrimaryKey(Integer legalAidAmendmentId) throws FinderException;
    public Collection findByLegalAidOrderId(Integer legalAidOrderId) throws FinderException;
    

}
