package uk.gov.courtservice.framework.business.services;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface CSSessionHome extends EJBHome {

    CSSession create() throws CreateException, RemoteException;

}
