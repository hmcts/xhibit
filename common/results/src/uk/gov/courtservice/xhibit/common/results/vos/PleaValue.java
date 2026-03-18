package uk.gov.courtservice.xhibit.common.results.vos;

// jdk

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.Parameterizeable;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;

public class PleaValue extends ResultValue implements Parameterizeable, Serializable, UncodedOffenceInterface {
    
	static final long serialVersionUID = 8152691673788408118L;
	
	private static final String DEFENDANT_ON_CHARGE = "C";

    private static final String DEFENDANT_ON_OFFENCE = "O";

    private XhbPleaBasicValue pleaBasicValue;

    // Original plea and verdict ref ids used for VCO calculation
    private Integer originalRefPleaId;

    private Date originalPleaDate;

    private Integer originalRefVerdictId;

    private Date originalVerdictDate;

    // Cached refPleaId data
    private String refPleaDesc;

    private String refPleaCode;

    // Cached altRefOffenceId data
    private String altRefOffenceCode;

    private String altRefOffenceDesc;

    private java.util.Date datePut;

    // Constructors and wrapper support
    public PleaValue() {
        this.pleaBasicValue = new XhbPleaBasicValue();
        this.pleaBasicValue.setObsInd("N");

    }

    public PleaValue(XhbPleaBasicValue pleaBasicValue) throws IllegalArgumentException {
        if (pleaBasicValue == null) {
            throw new IllegalArgumentException("pleaBasicValue: null");
        }
        this.pleaBasicValue = pleaBasicValue;
        this.pleaBasicValue.setObsInd("N");
    }

    public XhbPleaBasicValue getPleaBasicValue() {
        return pleaBasicValue;
    }

    public void setPleaBasicValue(XhbPleaBasicValue pleaBasicValue) {
        this.pleaBasicValue = pleaBasicValue;
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        /**
         * @todo decide what parameters to pass in message
         */
        return new String[0];
    }

    // Accessors
    public String getRefPleaCode() {
        return refPleaCode;
    }

    public void setRefPleaCode(String refPleaCode) {
        this.refPleaCode = refPleaCode;
    }

    public String getRefPleaDesc() {
        return refPleaDesc;
    }

    public void setRefPleaDesc(String refPleaDesc) {
        this.refPleaDesc = refPleaDesc;
    }

    public String getAltRefOffenceCode() {
        return altRefOffenceCode;
    }

    /**
     * Sets the alternate ref offence code. This should be called from the
     * midtier when populating this VO.
     * 
     * @param altRefOffenceCode
     *            alternate ref offence code.
     */
    public void setViewAltRefOffenceCode(String altRefOffenceCode) {
        this.altRefOffenceCode = altRefOffenceCode;
    }

    public String getAltRefOffenceDesc() {
        return altRefOffenceDesc;
    }

    /**
     * Sets the alternate ref offence description. This should be called from
     * the midtier when populating this VO.
     * 
     * @param altRefOffenceDesc
     *            alternate ref offence description.
     */
    public void setViewAltRefOffenceDesc(String altRefOffenceDesc) {
        this.altRefOffenceDesc = altRefOffenceDesc;
    }

    /**
     * Sets the alternate ref offence code and description. This should be
     * called from the GUI to update the alternate offence details.
     * 
     * @param altRefOffenceCode
     *            the alternate ref offence code.
     * @param altRefOffenceDesc
     *            the alternate ref offence description.
     */
    public void setAltRefOffenceCodeAndDesc(final String altRefOffenceCode, final String altRefOffenceDesc) {
        setViewAltRefOffenceCode(altRefOffenceCode);
        setViewAltRefOffenceDesc(altRefOffenceDesc);

        if (altRefOffenceCode == null || !altRefOffenceCode.equalsIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE)) {
            pleaBasicValue.setAltUncodedOffenceDesc(null);
        } else {
            pleaBasicValue.setAltUncodedOffenceDesc(altRefOffenceDesc);
        }
    }

    public Integer getOriginalRefPleaId() {
        return originalRefPleaId;
    }

    public void setOriginalRefPleaId(Integer originalRefPleaId) {
        this.originalRefPleaId = originalRefPleaId;
    }

    public Date getOriginalPleaDate() {
        return originalPleaDate;
    }

    public void setOriginalPleaDate(Date originalPleaDate) {
        this.originalPleaDate = originalPleaDate;
    }

    public Integer getOriginalRefVerdictId() {
        return originalRefVerdictId;
    }

    public void setOriginalRefVerdictId(Integer originalRefVerdictId) {
        this.originalRefVerdictId = originalRefVerdictId;
    }

    public Date getOriginalVerdictDate() {
        return originalVerdictDate;
    }

    public void setOriginalVerdictDate(Date originalVerdictDate) {
        this.originalVerdictDate = originalVerdictDate;
    }

    // Basic Accessors
    public String getOtherPleaText() {
        return pleaBasicValue.getOtherPleaText();
    }

    public void setOtherPleaText(String otherPleaText) {
        pleaBasicValue.setOtherPleaText(otherPleaText);
    }

    public Integer getAltRefOffenceId() {
        return pleaBasicValue.getAltRefOffenceId();
    }

    public void setAltRefOffenceId(Integer altRefOffenceId) {
        pleaBasicValue.setAltRefOffenceId(altRefOffenceId);
    }

    public Integer getRefPleaId() {
        return pleaBasicValue.getRefPleaId();
    }

    public void setRefPleaId(Integer refPleaId) {
        if (refPleaId != null && refPleaId.equals(new Integer(0))) {
            pleaBasicValue.setRefPleaId(null);
        } else {
            pleaBasicValue.setRefPleaId(refPleaId);
        }
    }

    public boolean isBreachAdmitted() {
        Boolean breachAdmitted = getBreachAdmitted();
        return breachAdmitted != null && breachAdmitted.booleanValue();
    }

    public Boolean getBreachAdmitted() {
        if (pleaBasicValue.getBreachAdmitted() == null || pleaBasicValue.getBreachAdmitted().equals("")) {
            return null;
        } else if (pleaBasicValue.getBreachAdmitted().equals("Y")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void setBreachAdmitted(Boolean breachAdmitted) {
        if (breachAdmitted == null) {
            pleaBasicValue.setBreachAdmitted(null);
        } else if (breachAdmitted.booleanValue()) {
            pleaBasicValue.setBreachAdmitted("Y");
        } else {
            pleaBasicValue.setBreachAdmitted("N");
        }
    }

    public Date getPleaDate() {
        return pleaBasicValue.getArraignmentDate();
    }

    public void setPleaDate(Date pleaDate) {
        pleaBasicValue.setArraignmentDate(pleaDate);
    }

    public Calendar getArraignmentDateCalendar() {
        Date arraignmentDate = pleaBasicValue.getArraignmentDate();
        if (arraignmentDate == null) {
            return Calendar.getInstance();
        } else {
            Calendar arraignmentCalendar = Calendar.getInstance();
            arraignmentCalendar.setTime(arraignmentDate);
            return arraignmentCalendar;
        }
    }

    public Date getArraignmentDate() {
        Date arraignmentDate = pleaBasicValue.getArraignmentDate();
        if (arraignmentDate == null) {
            return new Date();
        } else {
            return arraignmentDate;
        }
    }

    public void setArraignmentDate(Calendar arraignmentCalendar) {
        if (arraignmentCalendar == null) {
            pleaBasicValue.setArraignmentDate(null);
        } else {
            pleaBasicValue.setArraignmentDate(arraignmentCalendar.getTime());
        }
    }

    public void setDatePut(Date datePut) {
        this.datePut = datePut;
    }

    public Date getDatePut() {
        return datePut;
    }

    public Calendar getDatePutCalendar() {
        Calendar datePutCalendar = Calendar.getInstance();
        datePutCalendar.setTime(getDatePut());
        return datePutCalendar;
    }

    // THIS METHOD IS REQUIRED BY thickclient\caseprogress
    /**
     * This method has been added for backward compatability as some classes are
     * useing this method to return the defcharge or defonoffence id. This
     * method will check the value of DefendantOnOffenceID, if it is populated
     * it will return it else it will return DefendantCharID.
     * 
     * @return Integer either the DefendantOnOffenceId, DefendantChargeId or
     *         NULL
     */
    public Integer getDefOnChargeOrOffenceID() {
        Integer retValue = null;
        if (this.getDefendantOnOffenceId() != null) {
            retValue = this.getDefendantOnOffenceId();
        } else {
            retValue = this.getDefendantChargeId();
        }

        return retValue;
    }

    public Integer getDefendantOnOffenceId() {
        return pleaBasicValue.getDefendantOnOffenceId();
    }

    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        pleaBasicValue.setDefendantOnOffenceId(defendantOnOffenceId);
        pleaBasicValue.setDefendantChargeId(null);
        pleaBasicValue.setDefOnChargeOrOffence(DEFENDANT_ON_OFFENCE);
    }

    public Integer getDefendantChargeId() {
        return pleaBasicValue.getDefendantChargeId();
    }

    public void setDefendantChargeId(Integer defendantChargeId) {
        pleaBasicValue.setDefendantChargeId(defendantChargeId);
        pleaBasicValue.setDefendantOnOffenceId(null);
        pleaBasicValue.setDefOnChargeOrOffence(DEFENDANT_ON_CHARGE);
    }

    public boolean getObsInd() {
        return "Y".equals(pleaBasicValue.getObsInd());
    }

    public void setObsInd(boolean obsInd) {
        // pleaBasicValue.setObsInd(obsInd ? "Y" : null);
        pleaBasicValue.setObsInd(obsInd ? "Y" : "N");
    }

    public void setPleaId(Integer pleaId) {
        pleaBasicValue.setPleaId(pleaId);
    }

    public Integer getPleaId() {
        return pleaBasicValue.getPleaId();
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(PleaValue.class.getName());
        buffer.append(" {originalRefPleaId=");
        buffer.append(originalRefPleaId);
        buffer.append(", originalPleaDate=");
        buffer.append(originalPleaDate);
        buffer.append(", originalRefVerdictId=");
        buffer.append(originalRefVerdictId);
        buffer.append(", originalVerdictDate=");
        buffer.append(originalVerdictDate);
        buffer.append(", refPleaDesc=");
        buffer.append(refPleaDesc);
        buffer.append(", refPleaCode=");
        buffer.append(refPleaCode);
        buffer.append(", altRefOffenceCode=");
        buffer.append(altRefOffenceCode);
        buffer.append(", altRefOffenceDesc=");
        buffer.append(altRefOffenceDesc);
        buffer.append(", datePut=");
        buffer.append(datePut);
        buffer.append(", pleaBasicValue=");
        buffer.append(pleaBasicValue);
        buffer.append("}");
    }
    
       
    public boolean isGuilty()
    {
		return PleaTypeEvent.CPGJ.equals(refPleaCode) || PleaTypeEvent.CPG.equals(refPleaCode)
				|| PleaTypeEvent.G.equals(refPleaCode) || PleaTypeEvent.GAO.equals(refPleaCode) 
				|| PleaTypeEvent.GLO.equals(refPleaCode);
	}
    
    public boolean isGuiltyOfLesser()
    {
    	return PleaTypeEvent.GAO.equals(refPleaCode) || PleaTypeEvent.GLO.equals(refPleaCode);
    }

    // Business
    public boolean isOnOffence() {
        String defOnChargeOrOffence = pleaBasicValue.getDefOnChargeOrOffence();
        if (defOnChargeOrOffence == null) {
            return getDefendantOnOffenceId() != null;
        } else {
            return DEFENDANT_ON_OFFENCE.equals(defOnChargeOrOffence);
        }
    }

    public boolean isOnCharge() {
        String defOnChargeOrOffence = pleaBasicValue.getDefOnChargeOrOffence();
        if (defOnChargeOrOffence == null) {
            return getDefendantChargeId() != null;
        } else {
            return DEFENDANT_ON_CHARGE.equals(defOnChargeOrOffence);
        }
    }

}
