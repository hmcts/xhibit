package uk.gov.courtservice.xhibit.business.vos.entities;

//framework
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefOffenceValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class RefOffenceValue extends CSAbstractValue {
	
	static final long serialVersionUID = 7750234420765363191L;

    private Integer refOffenceId;
    private String offenceCode;
    private String offenceDesc;
    private String hoProcType;
    private String hoClass;
    private String hoSubclass;
    private String dvlcCode;
    private String offenceType;
    private String statute;
    private String offenceClass;
    private String actSection;
    private String obsInd;
    private String offenceGroup;
    private String xhbVersion;
    private String offenceDesc2;
    private Integer courtId;
    private String bailAct;


    /**
     *  Default constructor
     */
    public RefOffenceValue() {
    }

    /**
     * 
     * @param refOffenceId
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
     * @param xhbVersion
     */
    public RefOffenceValue(Integer refOffenceId, String offenceCode, String offenceDesc, String hoProcType,
            String hoClass, String hoSubclass, String dvlcCode, String offenceType, String statute,
            String offenceClass, String actSection, String obsInd, String offenceGroup, String xhbVersion,
            String offenceDesc2, Integer courtId) {
        this.refOffenceId = refOffenceId;
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
        this.xhbVersion = xhbVersion;
        this.courtId = courtId;
        this.offenceDesc2 = offenceDesc2;
    }
    
   /**
    * Parameterised constructor with bailAct
    * 
    * @param refOffenceId
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
    * @param xhbVersion
    * @param offenceDesc2
    * @param courtId
    * @param bailAct
    */
    public RefOffenceValue(Integer refOffenceId, String offenceCode, String offenceDesc, String hoProcType,
            String hoClass, String hoSubclass, String dvlcCode, String offenceType, String statute,
            String offenceClass, String actSection, String obsInd, String offenceGroup, String xhbVersion,
            String offenceDesc2, Integer courtId, String bailAct) {
        this.refOffenceId = refOffenceId;
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
        this.xhbVersion = xhbVersion;
        this.courtId = courtId;
        this.offenceDesc2 = offenceDesc2;
        this.bailAct = bailAct;
    }
    
    /**
     * 
     * @return
     */
    public Integer getRefOffenceId() {
        return refOffenceId;
    }

    /**
     * 
     * @param refOffenceId
     */
    public void setRefOffenceID(Integer refOffenceId) {
        this.refOffenceId = refOffenceId;
    }

    /**
     * 
     * @return
     */
    public String getOffenceCode() {
        return offenceCode;
    }

    /**
     * 
     * @param offenceCode
     */
    public void setOffenceCode(String offenceCode) {
        this.offenceCode = offenceCode;
    }

    /**
     * 
     * @return
     */
    public String getOffenceDesc() {
        return offenceDesc;
    }

    /**
     * 
     * @param offenceDesc
     */
    public void setOffenceDesc(String offenceDesc) {
        this.offenceDesc = offenceDesc;
    }

    /**
     * 
     * @return
     */
    public String getOffenceDesc2() {
        return offenceDesc2;
    }

    /**
     * 
     * @param offenceDesc
     */
    public void setOffenceDesc2(String offenceDesc2) {
        this.offenceDesc2 = offenceDesc2;
    }

    /**
     * 
     * @return
     */
    public String getHoProcType() {
        return hoProcType;
    }

    /**
     * 
     * @param hoProcType
     */
    public void setHoProcType(String hoProcType) {
        this.hoProcType = hoProcType;
    }

    /**
     * 
     * @return
     */
    public String getHoClass() {
        return hoClass;
    }

    /**
     * 
     * @param hoClass
     */
    public void setHoClass(String hoClass) {
        this.hoClass = hoClass;
    }

    /**
     * 
     * @return
     */
    public String getHoSubClass() {
        return hoSubclass;
    }

    /**
     * 
     * @param hoSubClass
     */
    public void setHoSubClass(String hoSubClass) {
        this.hoSubclass = hoSubClass;
    }

    /**
     * 
     * @return
     */
    public String getDvlcCode() {
        return dvlcCode;
    }

    /**
     * 
     * @param dvlcCode
     */
    public void setDvlcCode(String dvlcCode) {
        this.dvlcCode = dvlcCode;
    }

    /**
     * 
     * @return
     */
    public String getOffenceType() {
        return offenceType;
    }

    /**
     * 
     * @param offenceType
     */
    public void setOffenceType(String offenceType) {
        this.offenceType = offenceType;
    }

    /**
     * 
     * @return
     */
    public String getStatute() {
        return statute;
    }

    /**
     * 
     * @param statute
     */
    public void setStatute(String statute) {
        this.statute = statute;
    }

    /**
     * 
     * @return
     */
    public String getOffenceClass() {
        return offenceClass;
    }

    /**
     * 
     * @param offenceClass
     */
    public void setOffenceClass(String offenceClass) {
        this.offenceClass = offenceClass;
    }

    /**
     * 
     * @return
     */
    public String getActSection() {
        return actSection;
    }

    /**
     * 
     * @param actSection
     */
    public void setActSection(String actSection) {
        this.actSection = actSection;
    }

    /**
     * 
     * @return
     */
    public String getObsInd() {
        return obsInd;
    }

    /**
     * 
     * @param obsInd
     */
    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    /**
     * 
     * @return
     */
    public String getOffenceGroup() {
        return offenceGroup;
    }

    /**
     * 
     * @param offenceGroup
     */
    public void setOffenceGroup(String offenceGroup) {
        this.offenceGroup = offenceGroup;
    }

    /**
     * 
     * @param xhbVersion
     */
    public void setXhbVersion(String xhbVersion) {
        this.xhbVersion = xhbVersion;
    }

    /**
     * 
     * @return
     */
    public String getXhbVersion() {
        return xhbVersion;
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
