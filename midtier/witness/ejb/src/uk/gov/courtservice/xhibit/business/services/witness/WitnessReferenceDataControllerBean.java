package uk.gov.courtservice.xhibit.business.services.witness;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingController;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerHome;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.TrialSessionImpl;

/**
 * <p>
 * Title: The Witness Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides core business
 * services required for the Witness Facilities part of XHIBIT .
 * </p>
 * 
 * @ejb.bean name="WitnessReferenceDataController" description="Witness
 *           Reference Data Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="WitnessReferenceDataControllerHome"
 * 
 * 
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis
 */
public class WitnessReferenceDataControllerBean extends CSSessionBean implements SessionBean {
    public static final boolean cacheValues = true;

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @ejb.create-method
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * @return
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String[] getPagerNetworks() {
        try {
            // Awaiting this code being available from instant messaging.
            final MessagingControllerHome messagingControllerHome = (MessagingControllerHome) CSServices
                    .getServiceLocator().getRemoteHome(MessagingControllerHome.class);
            final MessagingController messagingController = messagingControllerHome.create();
            return messagingController.getDeviceTypes();
        } catch (CreateException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        } catch (RemoteException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * return the valid witness statuses. (Profesional, Jouvernile etc)
     * 
     * @return
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String[] getWitnessStatuses() {
        return getPropertyAsArray("witness.status.values");

    }

    private String[] getPropertyAsArray(final String propertyName) {
        final ArrayList list = new ArrayList();
        final String property = CSServices.getConfigServices().getProperties("witness").getProperty(propertyName);
        for (StringTokenizer stringTokenizer = new StringTokenizer(property, ",", false); stringTokenizer
                .hasMoreTokens();) {
            final String s = stringTokenizer.nextToken();
            list.add(s);
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * return the valid witness types. (Defendant and Prosecution)
     * 
     * @return
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String[] getWitnessTypes() {
        return getPropertyAsArray("witness.types.values");
    }

    /**
     * @return
     * @ejb.interface-method view-type="remote"
     */
    public String[] getTrialSessionTypes() {

        return TrialSessionImpl.getSessionTypes();
    }
}