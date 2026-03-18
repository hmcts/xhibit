package uk.gov.courtservice.xhibit.business.services.orders;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtHome;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetail;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.orders.query.OrderQueries;

/**
 * <p>
 * Title: The Orders Reference Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides reference
 * services required for the Orders templating system.
 * </p>
 * <p/>
 * 
 * @author Neil Ellis
 * @ejb.bean name="OrdersReferenceController" description="Orders Reference
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="OrdersReferenceControllerHome" <p/>
 *           <p>
 *           Copyright: Copyright (c) 2003
 *           </p>
 *           <p>
 *           Company: EDS
 *           </p>
 */
public class OrdersReferenceControllerBean extends uk.gov.courtservice.framework.business.services.CSSessionBean
        implements SessionBean {

    private Logger log = CSServices.getLogger(OrdersReferenceControllerBean.class);

    private XhbRefCourtHome courtHome;

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @throws CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();

        log.debug("Entering ejbCreate().");

        // Set up the required home interfaces.
        CSServices.getServiceLocator();
        courtHome = (XhbRefCourtHome) CSServices.getServiceLocator().getLocalHome(XhbRefCourtHome.class);

        // Retrieve the more general orders properties.
        Properties orderSessionProps = CSServices.getConfigServices().getProperties("orders");

        log.debug("Exiting ejbCreate");

    }

    /**
     * Returns reference data for the Court Widgets.
     * 
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public XhbRefCourtValue[] getCourts(Integer courtId) throws RemoteException {
        return OrderQueries.getRefCourts(courtId);
    }

    /**
     * Return contact data for the Court Widgets
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public String getTelephoneForAddress(Integer addressId)throws RemoteException {

        log.debug("Contact Details for address Id = " + addressId);
        String telephone = null;
        Collection details = XhbContactDetailBeanHelper2.findByAddressId(addressId);
        for (Iterator it = details.iterator(); it.hasNext();) {
            XhbContactDetail contact = (XhbContactDetail) it.next();
            log.debug("Contact Type: " + contact.getContactType());
            log.debug("Contact Value: " + contact.getContactValue());
            // LOGIC TO CHECK THE TELEPHONE TYPE
            if (contact.getContactType().equals("Phone") && contact.getContactValue() != null) {
                telephone = contact.getContactValue();
            }
        }
        log.debug("RETURN TELEPHONE NUMBER FROM ORDER REF CONTROLLER BEAN >> " + telephone);
        return telephone;
    }
}