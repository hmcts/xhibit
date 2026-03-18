package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefOffenceComplexValue.
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
public class RefOffenceComplexValue extends RefOffenceBasicValue {
	
	static final long serialVersionUID = 1243612384840909394L;
	
    private CourtBasicValue court = null;

    /**
     * Default constructor.
     */
    public RefOffenceComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefOffenceComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param offenceDesc
     * @param hoProcType
     * @param hoClass
     * @param hoSubclass
     * @param dvlcCode
     * @param offenceType
     * @param statute
     * @param offenceClass
     * @param actSection
     * @param obsInd
     * @param offenceGroup
     * @param xhbVersion
     */
    public RefOffenceComplexValue(Integer id, Integer version, String offenceCode, String offenceDesc,
            String hoProcType, String hoClass, String hoSubclass, String dvlcCode, String offenceType, String statute,
            String offenceClass, String actSection, String obsInd, String offenceGroup, String offenceDesc2,
            Integer courtId) {
        super(id, version, offenceCode, offenceDesc, hoProcType, hoClass, hoSubclass, dvlcCode, offenceType, statute,
                offenceClass, actSection, obsInd, offenceGroup, offenceDesc2, courtId);
    }
    
    
   /**
    * Parameter constructor with bailAct
    * 
    * @param id
    * @param version
    * @param offenceCode
    * @param offenceDesc
    * @param hoProcType
    * @param hoClass
    * @param hoSubclass
    * @param dvlcCode
    * @param offenceType
    * @param statute
    * @param offenceClass
    * @param actSection
    * @param obsInd
    * @param offenceGroup
    * @param offenceDesc2
    * @param courtId
    * @param bailAct
    */
    public RefOffenceComplexValue(Integer id, Integer version, String offenceCode, String offenceDesc,
            String hoProcType, String hoClass, String hoSubclass, String dvlcCode, String offenceType, String statute,
            String offenceClass, String actSection, String obsInd, String offenceGroup, String offenceDesc2,
            Integer courtId, String bailAct) {
        super(id, version, offenceCode, offenceDesc, hoProcType, hoClass, hoSubclass, dvlcCode, offenceType, statute,
                offenceClass, actSection, obsInd, offenceGroup, offenceDesc2, courtId, bailAct);
    }
    
    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }

    public CourtBasicValue getCourt() {
        return court;
    }
}
