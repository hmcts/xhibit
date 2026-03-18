package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class WLLRecipientBean extends CSEntityBean {
    public java.lang.Integer ejbCreate(Integer crestSolicitorFirmID, String solicitorFirmName,
            String solicitorFirmAddress, String solicitorFirmFax, String solicitorFirmEmail, Integer courtId, String userDisplayName)
            throws CreateException {

        setCrestSolicitorFirmId(crestSolicitorFirmID);
        setSolicitorFirmName(solicitorFirmName);
        setSolictiorFirmAddress(solicitorFirmAddress);
        setSolicitorFirmFax(solicitorFirmFax);
        setSolicitorFirmEmail(solicitorFirmEmail);
        setCourtId(courtId);

        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer crestSolicitorFirmID, String solicitorFirmName, String solicitorFirmAddress,
            String solicitorFirmFax, String solicitorFirmEmail, Integer courtId, String userDisplayName) throws CreateException {
    }

    /**
     * CMP fields
     */
    public abstract void setWllRecipientId(java.lang.Integer wllRecipientId);

    public abstract void setCrestSolicitorFirmId(java.lang.Integer crestSolicitorFirmId);

    public abstract void setCourtId(java.lang.Integer courtId);

    public abstract void setSolicitorFirmName(java.lang.String solicitorFirmName);

    public abstract void setSolictiorFirmAddress(java.lang.String solictiorFirmAddress);

    public abstract void setSolicitorFirmFax(java.lang.String solicitorFirmFax);

    public abstract void setSolicitorFirmEmail(java.lang.String solicitorFirmEmail);

    public abstract void setDocumentDistribution(
            uk.gov.courtservice.xhibit.business.entities.listdistribution.DocumentDistribution documentDistribution);

    public abstract java.lang.Integer getWllRecipientId();

    public abstract java.lang.Integer getCrestSolicitorFirmId();

    public abstract java.lang.Integer getCourtId();

    public abstract java.lang.String getSolicitorFirmName();

    public abstract java.lang.String getSolictiorFirmAddress();

    public abstract java.lang.String getSolicitorFirmFax();

    public abstract java.lang.String getSolicitorFirmEmail();

    public abstract uk.gov.courtservice.xhibit.business.entities.listdistribution.DocumentDistribution getDocumentDistribution();

}