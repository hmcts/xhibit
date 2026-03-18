package uk.gov.courtservice.xhibit.business.entities.pdda;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefPddaMessageTypeHome extends javax.ejb.EJBLocalHome {
    public RefPddaMessageType create(Integer refPddaMessageTypeId,
    		String pddaMessageType, String pddaMessageTypeDescription, 
    		String obsInd, String userDisplayName) throws CreateException;

    public RefPddaMessageType findByPrimaryKey(Integer id) throws FinderException;
    public RefPddaMessageType findByMessageType(String messageType) throws FinderException;
}