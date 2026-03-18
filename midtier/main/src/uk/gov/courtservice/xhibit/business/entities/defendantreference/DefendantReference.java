package uk.gov.courtservice.xhibit.business.entities.defendantreference;

// framework
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;

public interface DefendantReference extends CSEntityLocal {
    public Integer getDefRefId();

    public void setReferenceValue(String referenceValue);

    public String getReferenceValue();

    public void setReferenceName(String referenceName);

    public String getReferenceName();

    public void setCategory(String category);

    public String getCategory();

    public void setDefendantId(Integer defendantId);

    public Integer getDefendantId();

    public void setDefendant(Defendant defendant);

    public Defendant getDefendant();
}