package uk.gov.courtservice.xhibit.business.entities.refsystemcode;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Ref System Code bean.
 */
abstract public class RefSystemCodeBean extends CSEntityBean {

    public Integer ejbCreate(String code, String codeType, String codeTitle, String decode, Integer refCodeOrder,
            String obsInd, String userDisplayName) throws CreateException {
        setCode(code);
        setCodeType(codeType);
        setCodeTitle(codeTitle);
        setDecode(decode);
        setRefCodeOrder(refCodeOrder);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        return null;
    }

    public void ejbPostCreate(String code, String codeType, String codeTitle, String decode, Integer refCodeOrder,
            String obsInd, String userDisplayName) throws CreateException {
    }

    public abstract Integer getRefSystemCodeId(); // PK

    public abstract void setRefSystemCodeId(Integer newValue); // PK - this

    // setter is
    // temporary to
    // get around
    // JBs
    // insistence
    // that it be
    // here

    public abstract String getCode();

    public abstract String getCodeType();

    public abstract String getCodeTitle();

    public abstract String getDecode();

    public abstract Integer getRefCodeOrder();

    public abstract String getObsInd();

    public abstract void setCode(String code);

    public abstract void setCodeType(String codeType);

    public abstract void setCodeTitle(String codeTitle);

    public abstract void setDecode(String decode);

    public abstract void setRefCodeOrder(Integer refCodeOrder);

    public abstract void setObsInd(String obsInd);

    public abstract uk.gov.courtservice.xhibit.business.entities.court.Court getCourt();

    public abstract void setCourt(uk.gov.courtservice.xhibit.business.entities.court.Court court);
}