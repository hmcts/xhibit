package uk.gov.courtservice.xhibit.business.vos.entities;

//framework
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefOffenceBasicValue.
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
public class RefOffenceBasicValue extends CSAbstractValue {
	
	static final long serialVersionUID = -4884174336870643701L;
	
    private String offenceCode = null;

    private String offenceDesc = null;

    private String hoProcType = null;

    private String hoClass = null;

    private String hoSubclass = null;

    private String dvlcCode = null;

    private String offenceType = null;

    private String statute = null;

    private String offenceClass = null;

    private String actSection = null;

    private String obsInd = null;

    private String offenceGroup = null;

    private String xhbVersion = null;

    private String offenceDesc2 = null;

    private Integer courtId = null;
    
    private String bailAct = null;


    /**
     * Default constructor.
     */
    public RefOffenceBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefOffenceBasicValue(Integer id, Integer version) {
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
    public RefOffenceBasicValue(Integer id, Integer version, String offenceCode, String offenceDesc, String hoProcType,
            String hoClass, String hoSubclass, String dvlcCode, String offenceType, String statute,
            String offenceClass, String actSection, String obsInd, String offenceGroup, String offenceDesc2,
            Integer courtId) {
        this(id, version);
        this.offenceCode = offenceCode;
        this.offenceDesc = offenceDesc;
        this.hoProcType = hoProcType;
        this.hoClass = hoClass;
        this.hoSubclass = hoSubclass;
        this.dvlcCode = dvlcCode;
        this.offenceType = offenceType;
        this.statute = statute;
        this.offenceClass = offenceClass;
        this.actSection = actSection;
        this.obsInd = obsInd;
        this.offenceGroup = offenceGroup;
        this.courtId = courtId;
        this.offenceDesc2 = offenceDesc2;
    }

    
   /**
    *  Parameter constructor with bailAct
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
    public RefOffenceBasicValue(Integer id, Integer version, String offenceCode, String offenceDesc, String hoProcType,
            String hoClass, String hoSubclass, String dvlcCode, String offenceType, String statute,
            String offenceClass, String actSection, String obsInd, String offenceGroup, String offenceDesc2,
            Integer courtId, String bailAct) {
        this(id, version);
        this.offenceCode = offenceCode;
        this.offenceDesc = offenceDesc;
        this.hoProcType = hoProcType;
        this.hoClass = hoClass;
        this.hoSubclass = hoSubclass;
        this.dvlcCode = dvlcCode;
        this.offenceType = offenceType;
        this.statute = statute;
        this.offenceClass = offenceClass;
        this.actSection = actSection;
        this.obsInd = obsInd;
        this.offenceGroup = offenceGroup;
        this.courtId = courtId;
        this.offenceDesc2 = offenceDesc2;
        this.bailAct = bailAct;
    }
    public String getOffenceCode() {
        return offenceCode;
    }

    public void setOffenceCode(String offenceCode) {
        this.offenceCode = offenceCode;
    }

    public String getOffenceDesc() {
        return offenceDesc;
    }

    public void setOffenceDesc(String offenceDesc) {
        this.offenceDesc = offenceDesc;
    }

    public String getOffenceDesc2() {
        return offenceDesc2;
    }

    public void setOffenceDesc2(String offenceDesc2) {
        this.offenceDesc2 = offenceDesc2;
    }

    public String getHoProcType() {
        return hoProcType;
    }

    public void setHoProcType(String hoProcType) {
        this.hoProcType = hoProcType;
    }

    public String getHoClass() {
        return hoClass;
    }

    public void setHoClass(String hoClass) {
        this.hoClass = hoClass;
    }

    public String getHoSubClass() {
        return hoSubclass;
    }

    public void setHoSubClass(String hoSubClass) {
        this.hoSubclass = hoSubClass;
    }

    public String getDvlcCode() {
        return dvlcCode;
    }

    public void setDvlcCode(String dvlcCode) {
        this.dvlcCode = dvlcCode;
    }

    public String getOffenceType() {
        return offenceType;
    }

    public void setOffenceType(String offenceType) {
        this.offenceType = offenceType;
    }

    public String getStatute() {
        return statute;
    }

    public void setStatute(String statute) {
        this.statute = statute;
    }

    public String getOffenceClass() {
        return offenceClass;
    }

    public void setOffenceClass(String offenceClass) {
        this.offenceClass = offenceClass;
    }

    public String getActSection() {
        return actSection;
    }

    public void setActSection(String actSection) {
        this.actSection = actSection;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getOffenceGroup() {
        return offenceGroup;
    }

    public void setOffenceGroup(String offenceGroup) {
        this.offenceGroup = offenceGroup;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setBailAct(String bailAct) {
        this.bailAct = bailAct;
    }
 
    public String getBailAct() {
        return bailAct;
    }
}
