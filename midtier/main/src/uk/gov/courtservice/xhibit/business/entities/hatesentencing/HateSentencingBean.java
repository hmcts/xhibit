package uk.gov.courtservice.xhibit.business.entities.hatesentencing;

//jdk
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;

abstract public class HateSentencingBean extends CSEntityBean implements EntityBean {

    // EntityContext entityContext;

    public java.lang.Integer ejbCreate(Integer defOnCaseId, Integer refHateSentTypeId, String userDisplayName) throws CreateException {
        setDefendantOnCaseId(defOnCaseId);
        setRefHateSentTypeId(refHateSentTypeId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer defOnCaseID, Integer refHateSentTypeId, String userDisplayName)
            throws CreateException {

    }

    // ------------------------------CMP Fields - set
    // ------------------------------------
    public abstract void setHateSentencingId(java.lang.Integer hateSentencingId);

    public abstract void setDefendantOnCaseId(java.lang.Integer defOnCaseId);

    public abstract void setRefHateSentTypeId(java.lang.Integer refHateSentTypeId);

    public abstract void setObsInd(java.lang.String obsInd);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);
    
    public abstract void setDefendantOnCase(DefendantOnCase defendantOnCase);

    // public abstract void setVersion(java.lang.Integer version);
    // public abstract void setCreationDate(java.sql.Date creationDate);
    // public abstract void setLastUpdateDate(java.sql.Date lastUpdateDate);

    // ------------------------------CMP Fields - get
    // ------------------------------------
    public abstract java.lang.Integer getHateSentencingId();

    public abstract java.lang.String getObsInd();

    public abstract java.lang.Integer getDefendantOnCaseId();

    public abstract java.lang.Integer getRefHateSentTypeId();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();
    
    public abstract DefendantOnCase getDefendantOnCase();
    
    //public abstract java.util.Collection<IndictmentLogValue> getIndictmentLogs();
    // public abstract java.lang.Integer getVersion();
    // public abstract java.sql.Date getCreationDate();
    // public abstract java.sql.Date getLastUpdateDate();

}
