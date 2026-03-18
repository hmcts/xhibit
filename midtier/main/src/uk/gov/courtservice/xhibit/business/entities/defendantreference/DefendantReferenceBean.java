package uk.gov.courtservice.xhibit.business.entities.defendantreference;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;

abstract public class DefendantReferenceBean extends CSEntityBean {
    public Integer ejbCreate(String referenceValue, String referenceName, String category, Integer defendantId,
            CSEntityLocal defendant, String userDisplayName) throws CreateException {
        setReferenceValue(referenceValue);
        setReferenceName(referenceName);
        setCategory(category);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        // Note don't set defendant or defendantId in they are CMRs
        return null;
    }

    public void ejbPostCreate(String referenceValue, String referenceName, String category, Integer defendantId,
            CSEntityLocal defendant, String userDisplayName) throws CreateException {
        // Note this is a CMR setting and will automatically set defendantId
        // (the foreign key) as well
        setDefendant((Defendant) defendant);
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setDefRefId(Integer defRefId);

    public abstract void setReferenceValue(String referenceValue);

    public abstract void setReferenceName(String referenceName);

    public abstract void setCategory(String category);

    public abstract void setDefendantId(Integer defendantId);

    public abstract void setCreatedBy(String createdBy);

    public abstract void setLastUpdatedBy(String lastUpdatedBy);

    public abstract Integer getDefRefId();

    public abstract String getReferenceValue();

    public abstract String getReferenceName();

    public abstract String getCategory();

    public abstract Integer getDefendantId();

    public abstract String getCreatedBy();

    public abstract String getLastUpdatedBy();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setDefendant(Defendant defendant);

    public abstract Defendant getDefendant();
}