package uk.gov.courtservice.xhibit.business.entities.solicitor;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface Solicitor extends CSEntityLocal {

    public Integer getSolicitorId();

    public String getCrestSolicitorName();

    public String getIsInCrest();

    public String getObsInd();

    public Integer getFirmId();

    public Integer getRefLegalRepId();

    public void setCrestSolicitorName(String crestSolicitorName);

    public void setIsInCrest(String isInCrest);

    public void setObsInd(String obsInd);

    public void setFirmId(Integer id);

    public void setRefLegalRepId(Integer id);

    public abstract RefLegalRepresentative getRefLegalRepresentative();

    public abstract void setRefLegalRepresentative(RefLegalRepresentative refLegalRepresentative);

    public abstract RefSolicitorFirm getRefSolicitorFirm();

    public abstract void setRefSolicitorFirm(RefSolicitorFirm newValue);
}