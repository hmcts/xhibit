package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class DefendantReferenceBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 4750838560961652868L;
	
	private Integer defendantID;

    private String referenceValue;

    private String referenceName;

    private String category;

    public DefendantReferenceBasicValue() {
    }

    public DefendantReferenceBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    public DefendantReferenceBasicValue(Integer defRefID, Integer version, Integer defendantID, String referenceValue,
            String referenceName, String category) {
        this(defRefID, version);
        this.defendantID = defendantID;
        this.referenceValue = referenceValue;
        this.referenceName = referenceName;
        this.category = category;

    }

    public void setDefendantID(Integer defendantID) {
        this.defendantID = defendantID;
    }

    public Integer getDefendantID() {
        return defendantID;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
