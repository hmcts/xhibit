package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * HateSentencingType Reference Data - Basic Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public class RefHateSentencingTypeBasicValue extends CSAbstractValue {


    private String obsInd = null;

    private Integer courtId = null;
    
    private String  hateSentType = null ;
    private String  title = null;
    private String  description = null;
    private String  cjsQualifier = null;

    /**
     * Default constructor.
     */
    public RefHateSentencingTypeBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param Integer
     *            id
     * @param Integer
     *            version
     */
    public RefHateSentencingTypeBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Param constructor.
     * @param id
     * @param version
     * @param hateSentType
     * @param title
     * @param description
     * @param cjsQualifier
     * @param obsInd
     * @param courtId
     */
    public RefHateSentencingTypeBasicValue(Integer id, Integer version, String hateSentType, String title, String description,
    		String cjsQualifier, String obsInd, Integer courtId) {

        this(id, version);
        this.hateSentType = hateSentType;
        this.title = title;
        this.description = description;
        this.cjsQualifier = cjsQualifier;
        this.obsInd = obsInd;
        this.courtId = courtId;
    }

    public String getHateSentType() {
        return hateSentType;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getCjsQualifier() {
        return cjsQualifier;
    }
    public String getObsInd() {
        return obsInd;
    }
    public Integer getCourtId() {
        return courtId;
    }

    public void setCjsQualifier(String cjsQualifier) {
        this.cjsQualifier = cjsQualifier;
    }    
    public void setHateSentType(String hateSentType) {
        this.hateSentType = hateSentType;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }
    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }
}