package uk.gov.courtservice.xhibit.business.entities.refappresult;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

abstract public class RefAppResultBean extends uk.gov.courtservice.framework.business.entities.CSEntityBean implements
        EntityBean {

    public java.lang.Integer ejbCreate(java.lang.String appResultCode, java.lang.String appResultDescr1,
            java.lang.String appResultDescr2, java.lang.Integer hoCode, java.lang.String lesserOffInd,
            java.lang.String obsInd, java.lang.Integer refAppResultId, java.lang.String varySentence)
            throws CreateException {
        setAppResultCode(appResultCode);
        setAppResultDescr1(appResultDescr1);
        setAppResultDescr2(appResultDescr2);
        setHoCode(hoCode);
        setLesserOffInd(lesserOffInd);
        setObsInd(obsInd);
        setRefAppResultId(refAppResultId);
        setVarySentence(varySentence);
        return null;
    }

    public void ejbPostCreate(java.lang.String appResultCode, java.lang.String appResultDescr1,
            java.lang.String appResultDescr2, java.lang.Integer hoCode, java.lang.String lesserOffInd,
            java.lang.String obsInd, java.lang.Integer refAppResultId, java.lang.String varySentence)
            throws CreateException {
        /** @todo Complete this method */
    }

    public abstract void setAppResultCode(java.lang.String appResultCode);

    public abstract void setAppResultDescr1(java.lang.String appResultDescr1);

    public abstract void setAppResultDescr2(java.lang.String appResultDescr2);

    public abstract void setCourtId(java.lang.Integer courtId);

    public abstract void setHoCode(java.lang.Integer hoCode);

    public abstract void setLesserOffInd(java.lang.String lesserOffInd);

    public abstract void setObsInd(java.lang.String obsInd);

    public abstract void setRefAppResultId(java.lang.Integer refAppResultId);

    public abstract void setVarySentence(java.lang.String varySentence);

    public abstract void setCourt(uk.gov.courtservice.xhibit.business.entities.court.Court court);

    public abstract java.lang.String getAppResultCode();

    public abstract java.lang.String getAppResultDescr1();

    public abstract java.lang.String getAppResultDescr2();

    public abstract java.lang.Integer getCourtId();

    public abstract java.lang.Integer getHoCode();

    public abstract java.lang.String getLesserOffInd();

    public abstract java.lang.String getObsInd();

    public abstract java.lang.Integer getRefAppResultId();

    public abstract java.lang.String getVarySentence();

    public abstract uk.gov.courtservice.xhibit.business.entities.court.Court getCourt();

    // CMR Fields

}