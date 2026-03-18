package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface WLLRecipientHome extends javax.ejb.EJBLocalHome {
    public WLLRecipient create(Integer crestSolicitorFirmID, String solicitorFirmName, String solicitorFirmAddress,
            String solicitorFirmFax, String solicitorFirmEmail, Integer courtId, String userDisplayName) throws CreateException;

    public WLLRecipient findByPrimaryKey(Integer wllRecipientId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;

    public WLLRecipient findByCourtIdAndRefSolId(Integer courtId, Integer solFirmId) throws FinderException;
}