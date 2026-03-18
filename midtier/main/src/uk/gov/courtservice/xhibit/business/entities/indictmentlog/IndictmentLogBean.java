package uk.gov.courtservice.xhibit.business.entities.indictmentlog;

//jdk
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;

abstract public class IndictmentLogBean extends CSEntityBean implements EntityBean {

    // EntityContext entityContext;

    public java.lang.Integer ejbCreate(Integer caseId, Integer sequenceNo, String indictmentInfo, String userDisplayName) throws CreateException {
        setCaseId(caseId);
        setSequenceNo(sequenceNo);
        setIndictmentInfo(indictmentInfo);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer caseID, Integer sequenceNo, String indictmentInfo, String userDisplayName)
            throws CreateException {

    }

    // ------------------------------CMP Fields - set
    // ------------------------------------
    public abstract void setIndictmentLogId(java.lang.Integer indictmentLogId);

    public abstract void setCaseId(java.lang.Integer caseId);

    public abstract void setSequenceNo(java.lang.Integer sequenceNo);

    public abstract void setIndictmentInfo(java.lang.String indictmentInfo);

    public abstract void setObsInd(java.lang.String obsInd);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    // public abstract void setVersion(java.lang.Integer version);
    // public abstract void setCreationDate(java.sql.Date creationDate);
    // public abstract void setLastUpdateDate(java.sql.Date lastUpdateDate);

    // ------------------------------CMP Fields - get
    // ------------------------------------
    public abstract java.lang.Integer getIndicmtenLogId();

    public abstract java.lang.String getIndictmentInfo();

    public abstract java.lang.String getObsInd();

    public abstract java.lang.Integer getCaseId();

    public abstract java.lang.Integer getSequenceNo();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();
    
    public abstract java.util.Collection<IndictmentLogValue> getIndictmentLogs();
    // public abstract java.lang.Integer getVersion();
    // public abstract java.sql.Date getCreationDate();
    // public abstract java.sql.Date getLastUpdateDate();

}