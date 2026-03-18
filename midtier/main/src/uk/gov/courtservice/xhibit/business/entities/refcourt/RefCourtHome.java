package uk.gov.courtservice.xhibit.business.entities.refcourt;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RefCourtHome extends EJBLocalHome {

    public RefCourt create(String courtFullName, String courtShortName, String namePrefix, String courtType,
            String crestCode, String obsInd, String dxRef, String isPsd, String userDisplayName) throws CreateException;

    public RefCourt findByPrimaryKey(Integer refCourtId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
    
    public RefCourt findByRefCourtId(Integer refCourtId) throws FinderException;
    
    public RefCourt findByPSDCTCodeAndCourtId(java.lang.String psdCode, Integer courtId) throws FinderException;
    
    public Collection findByCourtIdAndIsPSD(String isPSD, Integer courtId) throws FinderException;
    /*
     * public Collection findByCourtType(java.lang.String courtType) throws
     * FinderException;
     */
    /*
     * <query> <query-method> <method-name>findByCourtType</method-name>
     * <method-params> <method-param>java.lang.String</method-param>
     * </method-params> </query-method> <ejb-ql>SELECT DISTINCT OBJECT(o) FROM
     * RefCourt o WHERE o.courtType = ?1</ejb-ql> </query>
     */
}