package uk.gov.courtservice.xhibit.business.services.orders;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentre;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBeanHelper2;

/**
 * <p>
 * Title: The Collection Centres Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides services required for the Collection Centres.
 * </p>
 * <p/>
 * 
 * @author SA
 * @ejb.bean name="CollectionCentreController" description="Collection Centre
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="CollectionCentreControllerHome" <p/>
 *           <p>
 *           Copyright: Copyright (c) 2015
 *           </p>
 *           <p>
 *           Company: CGI
 *           </p>
 */
public class CollectionCentreControllerBean extends uk.gov.courtservice.framework.business.services.CSSessionBean
        implements SessionBean {

    private Logger log = CSServices.getLogger(CollectionCentreControllerBean.class);

    /**
     * Returns reference data for the collection centres
     * 
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public XhbCollectionCentreBasicValue[] getCollectionCentres() throws RemoteException {
         Collection selected = XhbCollectionCentreBeanHelper2.findAll();

         XhbCollectionCentreBasicValue[] retval = new XhbCollectionCentreBasicValue[selected.size()];
         int count=0;
         for(Iterator i = selected.iterator(); i.hasNext();) {
             retval[count++]= (((XhbCollectionCentre)i.next()).getData());
         }

         return retval;
    }
    
    /**
     * Returns reference data for the collection centres
     * 
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public XhbCollectionCentreBasicValue getCollectionCentre(Integer collectionCentreId) throws RemoteException {
        XhbCollectionCentre thisCollectionCentre = XhbCollectionCentreBeanHelper2.findByPrimaryKey(collectionCentreId);
        // Due to ejb security we cannot just return the "XhbCollectionCentre" so convert into something returnable

        XhbCollectionCentreBasicValue xccbv = new XhbCollectionCentreBasicValue();
        xccbv.setCollectionCentreId(thisCollectionCentre.getCollectionCentreId());
        xccbv.setDescription(thisCollectionCentre.getDescription());
        xccbv.setDisplayName(thisCollectionCentre.getDisplayName());
        xccbv.setEmailAddress(thisCollectionCentre.getEmailAddress());
        xccbv.setFullName(thisCollectionCentre.getFullName());
        xccbv.setAddressId(thisCollectionCentre.getAddressId());
        
        return xccbv;
    }
    
    /**
     * Returns reference data for the collection centres
     * 
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public Collection findByFullName(String fullName) throws RemoteException {
        Collection allCollectionCentres = XhbCollectionCentreBeanHelper2.findByFullName(fullName);
        
        return allCollectionCentres;
    }
    
    /**
     * 
     * @param addressId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public XhbAddressBasicValue getCollectionCentreAddress(Integer addressId) throws RemoteException {
        XhbAddress thisAddress = XhbAddressBeanHelper2.findByPrimaryKey(addressId);
        // Due to ejb security we cannot just return the "XhbAddress" so convert into something returnable

        XhbAddressBasicValue xabv = new XhbAddressBasicValue();
        xabv.setAddress1(thisAddress.getAddress1());
        xabv.setAddress2(thisAddress.getAddress2());
        xabv.setAddress3(thisAddress.getAddress3());
        xabv.setAddress4(thisAddress.getAddress4());
        xabv.setCountry(thisAddress.getCountry());
        xabv.setCounty(thisAddress.getCounty());
        xabv.setPostcode(thisAddress.getPostcode());
        xabv.setTown(thisAddress.getTown());
        
        return xabv;
    }
}