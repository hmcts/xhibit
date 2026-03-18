package uk.gov.courtservice.xhibit.business.entities.solicitor;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
abstract public class SolicitorBean extends CSEntityBean {

    public Integer ejbCreate(String crestName, String isInCrest, String obsInd) throws CreateException {

        setCrestSolicitorName(crestName);
        setIsInCrest(isInCrest);
        setObsInd(obsInd);
        return null;
    }

    public void ejbPostCreate(String crestName, String isInCrest, String obsInd) throws CreateException {
    }

    public abstract Integer getSolicitorId();

    public abstract void setSolicitorId(Integer solicitorId);

    /**
     * @todo remove when JBuilder stops playing silly buggers & respects the
     *       'none' method flag in the designer!
     */

    public abstract Integer getRefLegalRepId();

    public abstract String getCrestSolicitorName();

    public abstract String getIsInCrest();

    public abstract String getObsInd();

    public abstract Integer getFirmId();

    public abstract void setCrestSolicitorName(String crestSolicitorName);

    public abstract void setIsInCrest(String isInCrest);

    public abstract void setObsInd(String obsInd);

    public abstract void setRefLegalRepId(Integer refLegalRepId);

    public abstract void setFirmId(Integer firmId);

    public abstract uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative getRefLegalRepresentative();

    public abstract void setRefLegalRepresentative(
            uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative refLegalRepresentative);

    public abstract uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm getRefSolicitorFirm();

    public abstract void setRefSolicitorFirm(
            uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm refSolicitorFirm);

}