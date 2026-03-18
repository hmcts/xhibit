package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: HateSentencingValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * HateSentencing enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 * 
 */

public class HateSentencingValue extends CSAbstractValue {

	private static final long serialVersionUID = -6902246409244344055L;
	// private Integer hateSentencingID;
    private Integer defendantOnCaseID;

    private Integer refHateSentencingTypeId;


    

    public HateSentencingValue() {
        super();
    }

    public HateSentencingValue(Integer version) {
        super(version);
    }

    public HateSentencingValue(Integer id, Integer version) {
        super(id, version);
    }

    public HateSentencingValue(Integer hateSentencingID, Integer defOnCaseID, Integer refHateSentencingTypeId, Integer version) {
        this(hateSentencingID, version);
        this.defendantOnCaseID = defOnCaseID;
        this.refHateSentencingTypeId = refHateSentencingTypeId;
        
    }


    /*
     * use super getID() instead. public Integer getHateSentencingID() { return
     * hateSentencingID; }
     */
    public Integer getDefendantOnCaseId() {
        return defendantOnCaseID;
    }

    public Integer getRefHateSentencingTypeId() {
        return refHateSentencingTypeId;
    }

 

    /*
     * use super setID() instead. public void setHateSentencingID(Integer hateSentencingID) {
     * this.indictmentLogID = indictmentLogID; }
     */
    public void setRefHateSentencingTypeId(Integer refHateSentencingTypeId) {
        this.refHateSentencingTypeId = refHateSentencingTypeId;
    }

    public void setDefendantOnCaseId(Integer defOnCaseID) {
        this.defendantOnCaseID = defOnCaseID;
    }

    
}
