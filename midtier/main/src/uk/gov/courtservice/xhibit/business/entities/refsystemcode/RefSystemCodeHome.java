package uk.gov.courtservice.xhibit.business.entities.refsystemcode;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
 
public interface RefSystemCodeHome extends EJBLocalHome {

    public RefSystemCode create(String code, String codeType, String codeTitle, String decode, Integer refCodeOrder,
            String obsInd, String userDisplayName) throws CreateException;

    public RefSystemCode findByPrimaryKey(Integer refSystemCodeId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
    
    public Collection findHOProcCodeType(String codeType, Integer courtId) throws FinderException;

}