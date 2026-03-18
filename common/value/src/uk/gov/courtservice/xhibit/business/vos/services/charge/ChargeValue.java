package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ChargeValue
 * </p>
 * <p>
 * Description: ChargeValue is intended to represent charge entities as stored
 * in the Charge table. It contains a Collection of OffenceValue objects.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Laurent Bossard
 * @version $Id: ChargeValue.java,v 1.5 2010/03/21 16:19:18 hewittm Exp $
 */

public class ChargeValue extends CSAbstractValue {
    
    private static final long serialVersionUID = 1L;

    private Integer caseID;

    private Integer chargeID;

    private String chargeType;

    private String chargeTypeDescription;

    private Integer crestChargeID;

    private Integer crestChargeSeqNo;

    private Calendar prosPaperServedDate;

    private BreachValue breachValue;

    private Collection offenceValues; // This is a Collection of OffenceValue

    // objects

    private Integer defendantID; // Populated only for breaches and

    // miscellaneous appeals

    private Integer defendantOnChargeID; // Populated only for breaches

    // and miscellaneous appeals

    private Integer courtID;

    private Calendar dateIndRec;

    private String indResp;

    private Calendar indSignedDate;

    private boolean inCourt;

    private Calendar courtLogDate;

    private JoinderChargeInfoValue[] joinderChargeInfoValues;

    
    public ChargeValue() {
        // empty
    }

    /**
     * Copy constructor
     */
    private Calendar cloneCalendar(Calendar calendar) {
        if (calendar == null) {
            return null;
        } else {
            return (Calendar)calendar.clone();
        }
    }
    
    public ChargeValue(ChargeValue chargeValue) {
        if (chargeValue == null) {
            return;
        }
        this.caseID = chargeValue.getCaseID();
        this.chargeID = chargeValue.getChargeID();
        this.chargeType = chargeValue.getChargeType();
        this.chargeTypeDescription = chargeValue.getChargeTypeDescription();
        this.crestChargeID = chargeValue.getCrestChargeID();
        this.crestChargeSeqNo = chargeValue.getCrestChargeSeqNo();
        this.prosPaperServedDate = cloneCalendar(chargeValue.getProsPaperServedDate());
        this.breachValue = chargeValue.getBreachValue();
         
        if (chargeValue.getOffenceValues() != null) {
            Collection<OffenceValue> copiedOffences = new ArrayList<OffenceValue>();
            Iterator itr = chargeValue.getOffenceValues().iterator();
            while (itr.hasNext()) {
                OffenceValue offenceValue = (OffenceValue)itr.next();
                copiedOffences.add(new OffenceValue(offenceValue));
            }
            this.offenceValues = copiedOffences;
        } else {
            this.offenceValues = null;
        }

        this.defendantID = chargeValue.getDefendantID();
        this.defendantOnChargeID = chargeValue.getDefendantOnChargeID();
        this.courtID = chargeValue.getCourtID();
        this.dateIndRec = cloneCalendar(chargeValue.getDateIndRec());
        this.indResp = chargeValue.getIndResp();
        this.indSignedDate = cloneCalendar(chargeValue.getIndSignedDate());
        setDirty(chargeValue.isDirty());
        this.inCourt = chargeValue.isInCourt();
        this.courtLogDate = cloneCalendar(chargeValue.getCourtLogDate());
        this.joinderChargeInfoValues = chargeValue.getJoinderChargeInfoValues();
    }
    
    /**
     * @param caseID
     * @param chargeType
     * @roseuid 3DB7FFDE01F0
     */
    public ChargeValue(Integer caseID, ChargeType chargeType) {
        this.caseID = caseID;
        // bug fix

        if (chargeType != null) {
            this.chargeType = chargeType.getChargeType();
            this.chargeTypeDescription = chargeType.toString();
        }
    }

    /**
     * @param chargeID
     * @param caseID
     * @param chargeType
     */
    public ChargeValue(Integer chargeID, Integer caseID, ChargeType chargeType) {
        this(caseID, chargeType);
        this.chargeID = chargeID;
    }

    /**
     * This constructor is specific to Breaches.
     * 
     * @param chargeID
     * @param caseID
     * @param chargeType
     */
    public ChargeValue(Integer chargeID, Integer caseID, ChargeType chargeType, BreachValue breachValue) {
        this(chargeID, caseID, chargeType);
        this.breachValue = breachValue;
    }

    public ChargeValue(Integer chargeID, Integer caseID, ChargeType chargeType, Integer crestChargeID,
            Integer crestChargeSeqNo) {
        this(chargeID, caseID, chargeType);
        this.crestChargeID = crestChargeID;
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public ChargeValue(Integer chargeID, Integer caseID, ChargeType chargeType, Integer crestChargeID,
            Integer crestChargeSeqNo, Calendar prosPaperServedDate, BreachValue breachValue, Collection offenceValues,
            Integer defendantID, Integer courtID, Calendar dateIndRec, Calendar indSignedDate, String indResp) {
        this(chargeID, caseID, chargeType, crestChargeID, crestChargeSeqNo);
        this.prosPaperServedDate = prosPaperServedDate;
        this.breachValue = breachValue;
        this.offenceValues = offenceValues;
        this.defendantID = defendantID;
        this.courtID = courtID;
        this.dateIndRec = dateIndRec;
        this.indSignedDate = indSignedDate;
        this.indResp = indResp;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F47E0365
     */
    public Integer getCaseID() {
        return caseID;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F4530101
     */
    public Integer getChargeID() {
        return chargeID;
    }

    /**
     * @return charge type code (as represented in database)
     * @roseuid 3DB7F45C00F0
     */
    public String getChargeType() {
        if (chargeType != null)
            return chargeType;
        else
            return "";
    }

    /**
     * 
     * @return a description of the the charge type
     */
    public String getChargeTypeDescription() {
        if (chargeTypeDescription != null)
            return chargeTypeDescription;
        else
            return "";
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F4690315
     */
    public Integer getCrestChargeID() {
        return crestChargeID;
    }

    /**
     * @return java.lang.String
     * @roseuid 3DB7F4890086
     */
    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    /**
     * @return java.util.Calendar
     */
    public Calendar getProsPaperServedDate() {
        return prosPaperServedDate;
    }

    /**
     * This method returns a Collection of Offencevalue object.
     * 
     * @return java.util.Collection
     * @roseuid 3DB9100700F1
     */
    public Collection getOffenceValues() {
        return offenceValues;
    }

    /**
     * This method returns a BreachValue object if the charge is a breach, null
     * otherwise.
     * 
     * @return java.util.Collection
     */
    public BreachValue getBreachValue() {
        return breachValue;
    }

    /**
     * @param Collection
     * @roseuid 3DB7F4380260
     */
    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType.getChargeType();
        this.chargeTypeDescription = chargeType.toString();
    }

    /**
     * @param java.util.Calendar
     */
    public void setProsPaperServedDate(Calendar prosPaperServedDate) {
        this.prosPaperServedDate = prosPaperServedDate;
    }

    /**
     * offenceValue is a Collection of OffenceValue object.
     * 
     * @param Collection
     * @roseuid 3DB90FF70199
     */
    public void setOffenceValues(Collection offenceValues) {
        this.offenceValues = offenceValues;
    }

    /**
     * @param BreachValue
     */
    public void setBreachValue(BreachValue breachValue) {
        this.breachValue = breachValue;
    }

    public void setCrestChargeSeqNo(Integer crestChargeSeqNo) {
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public void setCrestChargeID(Integer crestChargeID) {
        this.crestChargeID = crestChargeID;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public Calendar getDateIndRec() {
        return dateIndRec;
    }

    public void setDateIndRec(Calendar dateIndRec) {
        this.dateIndRec = dateIndRec;
    }

    public Integer getDefendantID() {
        return defendantID;
    }

    public void setDefendantID(Integer defendantID) {
        this.defendantID = defendantID;
    }

    public Integer getDefendantOnChargeID() {
        return defendantOnChargeID;
    }

    public void setDefendantOnChargeID(Integer defendantOnChargeID) {
        this.defendantOnChargeID = defendantOnChargeID;
    }

    public String getIndResp() {
        return indResp;
    }

    public void setIndResp(String indResp) {
        this.indResp = indResp;
    }

    public Calendar getIndSignedDate() {
        return indSignedDate;
    }

    public void setIndSignedDate(Calendar indSignedDate) {
        this.indSignedDate = indSignedDate;
    }

    public boolean isInCourt() {
        return inCourt;
    }

    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }

    public void setJoinderChargeInfoValues(JoinderChargeInfoValue[] joinderChargeInfoValues) {
        this.joinderChargeInfoValues = joinderChargeInfoValues;
    }

    public JoinderChargeInfoValue[] getJoinderChargeInfoValues() {
        return joinderChargeInfoValues;
    }
}
