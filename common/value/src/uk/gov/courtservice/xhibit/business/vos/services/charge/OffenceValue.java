package uk.gov.courtservice.xhibit.business.vos.services.charge;

// jdk
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;


/**
 * <p>
 * Title: OffenceValue
 * </p>
 * <p>
 * Description: The OffenceValue is intended to represent counts on indictments,
 * section 41 offences, committal for sentence offences and breach offences.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Laurent Bossard
 * @author Sarah Tong
 * @version $Id: OffenceValue.java,v 1.10 2010/03/21 16:20:30 hewittm Exp $
 */
/*
 * Ref Date Author Description
 * 
 * 112 01-04-2003 AW Daley Statute and actSection added as an 52098 attribute so
 * that they can be displayed in the Add Defendants To Offence Dialog. CR58
 * 19-10-2004 Cag Onganer Uncoded Offence Amendments
 */

public class OffenceValue extends CSAbstractValue {
    
    private static final long serialVersionUID = 2L;

    private String statute;

    private String actSection;

    private Integer chargeID;

    private String crestOffenceFreeText;

    private Integer crestOffenceID;

    private Integer crestOffenceSeqNo;

    private Integer multiple;

    private Integer offenceID;

    private Integer refOffenceID;

    private String offenceDescription;

    private String offenceCode;

    private Integer courtID;

    private String plea;
    
    private String forceLocationCode;
    
    private Calendar offenceStartDateTime;
    
    private Calendar offenceEndDateTime;
    
    private AddressValue addressValue;
    
    private Integer addressId;
    
    private boolean updatingAdditionalInfo;
    
    private Integer defendantOnOffenceID;        
    /**
     * This is a Collection of defendant IDs (Integers)
     */
    private Collection defendantIDs; 

    /**
     * This is a Collection of DefendantValue objects
     */
    private Collection defendantValues; 
    
    /**
     * Holds DefendantOnOffenceComplexValues keyed on defendantId
     */
    private HashMap<Integer, DefendantOnOffenceComplexValue> defOnOffenceComplexValues = 
        new HashMap<Integer, DefendantOnOffenceComplexValue>(); 

    private Integer caseID;

    private boolean inCourt;

    private Integer refSystemCodeID;

    /**
     * set to true if results have been found for this offence and the court
     * clerk has confirmed they are to be deleted when the offence is updated
     */ 
    private boolean deleteResults;

    private Calendar courtLogDate;

    // CR58 Additions
    private String crestHOClass;

    private String crestHOSubclass;
    
    // ctx-501 addition
    private String appealType;

    
    public OffenceValue() {
        // Empty
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
    
    public OffenceValue(OffenceValue offenceValue) {
        if (offenceValue == null) {
            return;
        }
        this.statute = offenceValue.getStatute();
        this.actSection = offenceValue.getActSection();
        this.chargeID = offenceValue.getChargeID();
        this.crestOffenceFreeText = offenceValue.crestOffenceFreeText;
        this.crestOffenceID = offenceValue.getCrestOffenceID();
        this.crestOffenceSeqNo = offenceValue.getCrestOffenceSeqNo();
        this.multiple = offenceValue.getMultiple();
        this.offenceID = offenceValue.getOffenceID();
        this.refOffenceID = offenceValue.getRefOffenceID();
        this.offenceDescription = offenceValue.getOffenceDescription();
        this.offenceCode = offenceValue.getOffenceCode();
        this.courtID = offenceValue.getCourtID();
        this.defendantOnOffenceID = offenceValue.getDefendantOnOffenceID();
        this.plea = offenceValue.getPlea();
        this.forceLocationCode = offenceValue.getForceLocationCode();
        this.offenceStartDateTime = cloneCalendar(offenceValue.getOffenceStartDateTime());
        this.offenceEndDateTime = cloneCalendar(offenceValue.getOffenceEndDateTime());
        
        this.addressValue = 
            (offenceValue.getAddressValue() != null 
                    ? new AddressValue(offenceValue.getAddressValue())
                    : null);

        this.addressId = offenceValue.getAddressId();
        this.updatingAdditionalInfo = offenceValue.isUpdatingAdditionalInfo();
        this.defendantIDs = offenceValue.getDefendantIDs();
        this.defendantValues = offenceValue.getDefendantValues();
        this.defOnOffenceComplexValues = offenceValue.defOnOffenceComplexValues;
        this.caseID = offenceValue.getCaseID();
        this.inCourt = offenceValue.isInCourt();
        this.refSystemCodeID = offenceValue.getRefSystemCodeID();
        setDirty(offenceValue.isDirty());
        this.deleteResults = offenceValue.isDeleteResults();
        this.courtLogDate = cloneCalendar(offenceValue.getCourtLogDate());
        this.crestHOClass = offenceValue.getCrestHOClass();
        this.crestHOSubclass = offenceValue.getCrestHOSubclass();
        this.appealType = offenceValue.getAppealType();
    }
    
    /**
     * All argument constructor, please maintain this when adding new
     * attributes. Includes additional offence details.
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param crestOffenceFreeText
     * @param crestOffenceID
     * @param crestOffenceSeqNo
     * @param multiple
     * @param offenceDescription
     * @param refSystemCodeID
     * @param courtID
     * @param caseID
     * @param offenceCode
     * @param actSection
     * @param statute
     * @param plea
     * @param crestHOClass
     * @param crestHOSubclass
     * @param forceLocationCode
     * @param offenceStartDateTime
     * @param offenceEndDateTime
     * @param addressValue
     */

    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String crestOffenceFreeText, Integer crestOffenceID, Integer crestOffenceSeqNo, Integer multiple,
            String offenceDescription, Integer refSystemCodeID, Integer courtID, Integer defendantOnOffenceID, Integer caseID, String offenceCode,
            String actSection, String statute, String plea, String crestHOClass, String crestHOSubclass, 
            String forceLocationCode, Calendar offenceStartDateTime, Calendar offenceEndDateTime, 
            AddressValue addressValue, String appealType) {
        
        this(offenceID, chargeID, refOffenceID, defendantIDs, crestOffenceFreeText, crestOffenceID, crestOffenceSeqNo, 
           multiple, offenceDescription, refSystemCodeID, courtID, defendantOnOffenceID, caseID, offenceCode, actSection, statute, plea, 
           crestHOClass,crestHOSubclass); 
        this.forceLocationCode = forceLocationCode;
        this.offenceStartDateTime = offenceStartDateTime;
        this.offenceEndDateTime = offenceEndDateTime;
        this.addressValue = addressValue;
        this.appealType = appealType;
    }

    /**
     * All argument constructor, please maintain this when adding new
     * attributes. Includes additional offence details.
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param crestOffenceFreeText
     * @param crestOffenceID
     * @param crestOffenceSeqNo
     * @param multiple
     * @param offenceDescription
     * @param refSystemCodeID
     * @param courtID
     * @param caseID
     * @param offenceCode
     * @param actSection
     * @param statute
     * @param plea
     * @param crestHOClass
     * @param crestHOSubclass
     * @param forceLocationCode
     * @param offenceStartDateTime
     * @param offenceEndDateTime
     * @param addressValue
     */

    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String crestOffenceFreeText, Integer crestOffenceID, Integer crestOffenceSeqNo, Integer multiple,
            String offenceDescription, Integer refSystemCodeID, Integer courtID, Integer defendantOnOffenceID, Integer caseID, String offenceCode,
            String actSection, String statute, String plea, String crestHOClass, String crestHOSubclass, 
            String forceLocationCode, Calendar offenceStartDateTime, Calendar offenceEndDateTime, 
            AddressValue addressValue) {
        
        this(offenceID, chargeID, refOffenceID, defendantIDs, crestOffenceFreeText, crestOffenceID, crestOffenceSeqNo, 
           multiple, offenceDescription, refSystemCodeID, courtID, defendantOnOffenceID, caseID, offenceCode, actSection, statute, plea, 
           crestHOClass,crestHOSubclass); 
        this.forceLocationCode = forceLocationCode;
        this.offenceStartDateTime = offenceStartDateTime;
        this.offenceEndDateTime = offenceEndDateTime;
        this.addressValue = addressValue;
    }
    
    /**
     * All argument constructor(minus additional offence details), please maintain this when adding new
     * attributes.
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param crestOffenceFreeText
     * @param crestOffenceID
     * @param crestOffenceSeqNo
     * @param multiple
     * @param offenceDescription
     * @param refSystemCodeID
     * @param courtID
     * @param caseID
     * @param offenceCode
     * @param actSection
     * @param statute
     * @param plea
     * @param crestHOClass
     * @param crestHOSubclass
     */ 
     
    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String crestOffenceFreeText, Integer crestOffenceID, Integer crestOffenceSeqNo, Integer multiple,
            String offenceDescription, Integer refSystemCodeID, Integer courtID, Integer defendantOnOffenceID, Integer caseID, String offenceCode,
            String actSection, String statute, String plea, String crestHOClass, String crestHOSubclass) {
        this(offenceID, chargeID, refOffenceID, defendantIDs);
        this.crestOffenceFreeText = crestOffenceFreeText;
        this.crestOffenceID = crestOffenceID;
        this.crestOffenceSeqNo = crestOffenceSeqNo;
        this.multiple = multiple;
        this.offenceDescription = offenceDescription;

        this.setRefSystemCodeID(refSystemCodeID);
        this.setCourtID(courtID);
        this.setCaseID(caseID);
        this.setOffenceCode(offenceCode);
        this.setActSection(actSection);
        this.setStatute(statute);
        this.setPlea(plea);
        this.setCrestHOClass(crestHOClass);
        this.setCrestHOSubclass(crestHOSubclass);
    }

    /**
     * Includes additional offence details.
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param crestOffenceFreeText
     * @param crestOffenceID
     * @param crestOffenceSeqNo
     * @param multiple
     * @param offenceDescription
     * @param forceLocationCode
     * @param offenceStartDateTime
     * @param offenceEndDateTime
     * @param addressValue
     */
    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String crestOffenceFreeText, Integer crestOffenceID, Integer crestOffenceSeqNo, Integer multiple,
            String offenceDescription, String forceLocationCode, Calendar offenceStartDateTime, Calendar offenceEndDateTime, 
            AddressValue addressValue) {
        this(offenceID, chargeID, refOffenceID, defendantIDs, crestOffenceFreeText, crestOffenceID,
                crestOffenceSeqNo, multiple, offenceDescription);
        this.forceLocationCode = forceLocationCode;
        this.offenceStartDateTime = offenceStartDateTime;
        this.offenceEndDateTime = offenceEndDateTime;
        this.addressValue = addressValue;
    }
    
    /**
     * 
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param crestOffenceFreeText
     * @param crestOffenceID
     * @param crestOffenceSeqNo
     * @param multiple
     * @param offenceDescription
     */
    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String crestOffenceFreeText, Integer crestOffenceID, Integer crestOffenceSeqNo, Integer multiple,
            String offenceDescription) {
        this(offenceID, chargeID, refOffenceID, defendantIDs);
        this.crestOffenceFreeText = crestOffenceFreeText;
        this.crestOffenceID = crestOffenceID;
        this.crestOffenceSeqNo = crestOffenceSeqNo;
        this.multiple = multiple;
        this.offenceDescription = offenceDescription;
    }

    /**
     *  Includes additional offence details.
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param forceLocationCode
     * @param offenceStartDateTime
     * @param offenceEndDateTime
     * @param addressValue
     */
    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String forceLocationCode, Calendar offenceStartDateTime, Calendar offenceEndDateTime, 
            AddressValue addressValue) {
        this(offenceID, chargeID, refOffenceID, defendantIDs);
        this.forceLocationCode = forceLocationCode;
        this.offenceStartDateTime = offenceStartDateTime;
        this.offenceEndDateTime = offenceEndDateTime;
        this.addressValue = addressValue;
    }
    
    /**
     * @param offenceID
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @roseuid 3DB913F20053
     */
    public OffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs) {
        this.offenceID = offenceID;
        this.chargeID = chargeID;
        this.refOffenceID = refOffenceID;
        this.defendantIDs = defendantIDs;
    }

    /**
     *  Includes additional offence details.
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @param forceLocationCode
     * @param offenceStartDateTime
     * @param offenceEndDateTime
     * @param addressValue
     */
    public OffenceValue(Integer chargeID, Integer refOffenceID, Collection defendantIDs, 
            String forceLocationCode, Calendar offenceStartDateTime, Calendar offenceEndDateTime, 
            AddressValue addressValue) {
        this(chargeID, refOffenceID, defendantIDs);
        this.forceLocationCode = forceLocationCode;
        this.offenceStartDateTime = offenceStartDateTime;
        this.offenceEndDateTime = offenceEndDateTime;
        this.addressValue = addressValue;
    }
    
    /**
     * @param chargeID
     * @param refOffenceID
     * @param defendantIDs
     * @roseuid 3DB913F20053
     */
    public OffenceValue(Integer chargeID, Integer refOffenceID, Collection defendantIDs) {
        this.chargeID = chargeID;
        this.refOffenceID = refOffenceID;
        this.defendantIDs = defendantIDs;
    }

    /**
     *  Includes additional offence details.
     * @param refOffenceID
     * @param defendantIDs
     * @param forceLocationCode
     * @param offenceStartDateTime
     * @param offenceEndDateTime
     * @param addressValue
     */
    public OffenceValue(Integer refOffenceID, Collection defendantIDs, String forceLocationCode, 
            Calendar offenceStartDateTime, Calendar offenceEndDateTime, AddressValue addressValue) {
        this(refOffenceID, defendantIDs);
        this.forceLocationCode = forceLocationCode;
        this.offenceStartDateTime = offenceStartDateTime;
        this.offenceEndDateTime = offenceEndDateTime;
        this.addressValue = addressValue;
        }
    /**
     * @param refOffenceID
     * @param defendantIDs
     * @roseuid 3DB7F56800AF
     */
    public OffenceValue(Integer refOffenceID, Collection defendantIDs) {
        this.refOffenceID = refOffenceID;
        this.defendantIDs = defendantIDs;
    }

  
    // added this method to support joinderindictments
    public void setOffenceId(Integer offenceId) {
        this.offenceID = offenceId;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F2740293
     */
    public Integer getChargeID() {
        return chargeID;
    }

    /**
     * @return java.lang.String
     * @roseuid 3DB7F2A4033C
     */
    public String getCrestOffenceFreeText() {
        return crestOffenceFreeText;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F2800005
     */
    public Integer getCrestOffenceID() {
        return crestOffenceID;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F29600C5
     */
    public Integer getCrestOffenceSeqNo() {
        return crestOffenceSeqNo;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F2B00311
     */
    public Integer getMultiple() {
        return multiple;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F24800E1
     */
    public Integer getOffenceID() {
        return offenceID;
    }

    /**
     * @return java.lang.Integer
     * @roseuid 3DB7F264022B
     */
    public Integer getRefOffenceID() {
        return refOffenceID;
    }

    /**
     * The Collection returned by this method is a Collection of defendantID
     * (Integer)
     * 
     * @return java.util.Collection
     * @roseuid 3DB7F50801D3
     */
    public Collection getDefendantIDs() {
        return defendantIDs;
    }

    /**
     * This method returns a collection of defendant value objects
     * 
     * @return java.util.Collection
     * @roseuid 3DBD444C0176
     */
    public Collection getDefendantValues() {
        return defendantValues;
    }

    /**
     * @param Integer
     * @roseuid 3DB7F2D0014A
     */
    public void setRefOffenceID(Integer refOffenceID) {
        this.refOffenceID = refOffenceID;
    }

    /**
     * defendantIDs is a Collection of defendantID (Integer)
     * 
     * @param Collection
     * @roseuid 3DB7F52800AD
     */
    public void setDefendantIDs(Collection defendantIDs) {
        this.defendantIDs = defendantIDs;
    }

    /**
     * This method takes of collection of defendant value objects as parameter
     * 
     * @param Collection
     * @roseuid 3DBD443C03A4
     */
    public void setDefendantValues(Collection defendantValues) {
        this.defendantValues = defendantValues;
    }

    /**
     * This method takes a caseID as a parameter
     * 
     * @param caseID
     */
    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    /**
     * This method takes an offence description as a parameter
     * 
     * @param offenceDescription
     */
    public void setOffenceDescription(String offenceDescription) {
        this.offenceDescription = offenceDescription;
    }

    /**
     * This method takes an crestOffenceFreeText as a parameter
     * 
     * @param crestOffenceFreeText
     */
    public void setCrestOffenceFreeText(String crestOffenceFreeText) {
        this.crestOffenceFreeText = crestOffenceFreeText;
    }

    /**
     * This method returns a caseID
     * 
     * @return
     */
    public Integer getCaseID() {
        return caseID;
    }

    /**
     * This method takes an isInCourt parameter
     * 
     * @param inCourt
     */
    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    /**
     * This method returns an isInCourt parameter
     * 
     * @return
     */
    public boolean isInCourt() {
        return inCourt;
    }

    public String getOffenceDescription() {
        return this.offenceDescription;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }
    
    public Integer getDefendantOnOffenceID() {
        return defendantOnOffenceID;
    }

    public void setDefendantOnOffenceID(Integer defendantOnOffenceId) {
        this.defendantOnOffenceID = defendantOnOffenceId;
    }

    public String getPlea() {
        return plea;
    }

    public void setPlea(String plea) {
        this.plea = plea;
    }

    public void setRefSystemCodeID(Integer refSystemCodeID) {
        this.refSystemCodeID = refSystemCodeID;
    }

    public Integer getRefSystemCodeID() {
        return refSystemCodeID;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public DefendantOnOffenceComplexValue getDefendantOnOffence(Integer defendantId) {
        return defOnOffenceComplexValues.get(defendantId);
    }

    public HashMap<Integer, DefendantOnOffenceComplexValue> getDefOnOffenceBasicValues() {
        return defOnOffenceComplexValues;
    }

    public void setDefOnOffenceBasicValues(HashMap defOnOffenceComplexValues) {
        this.defOnOffenceComplexValues = defOnOffenceComplexValues;
    }

    public void addDefOnOffenceComplexValue(Integer defendantId, DefendantOnOffenceComplexValue defOnOffenceComplexValue) {
        defOnOffenceComplexValues.put(defendantId, defOnOffenceComplexValue);
    }

    public boolean isDeleteResults() {
        return deleteResults;
    }

    public void setDeleteResults(boolean deleteResults) {
        this.deleteResults = deleteResults;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public void setCrestOffenceSeqNo(Integer crestOffenceSeqNo) {
        this.crestOffenceSeqNo = crestOffenceSeqNo;
    }

    public void setCrestOffenceID(Integer crestOffenceID) {
        this.crestOffenceID = crestOffenceID;
    }

    public String getOffenceCode() {
        return offenceCode;
    }

    public void setOffenceCode(String offenceCode) {
        this.offenceCode = offenceCode;
    }

    public String getStatute() {
        return statute;
    }

    public void setStatute(String statute) {
        this.statute = statute;
    }

    public String getActSection() {
        return actSection;
    }

    public void setActSection(String actSection) {
        this.actSection = actSection;
    }

    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }

    public String getCrestHOSubclass() {
        return crestHOSubclass;
    }

    public String getCrestHOClass() {
        return crestHOClass;
    }

    public void setCrestHOClass(String crestHOClass) {
        this.crestHOClass = crestHOClass;
    }

    public void setCrestHOSubclass(String crestHOSubclass) {
        this.crestHOSubclass = crestHOSubclass;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public AddressValue getAddressValue() {
        return addressValue;
    }

    public void setAddressValue(AddressValue addressValue) {
        this.addressValue = addressValue;
    }

    public String getForceLocationCode() {
        return forceLocationCode;
    }

    public void setForceLocationCode(String forceLocationCode) {
        this.forceLocationCode = forceLocationCode;
    }

    public Calendar getOffenceEndDateTime() {
        return offenceEndDateTime;
    }

    public void setOffenceEndDateTime(Calendar offenceEndDateTime) {
        this.offenceEndDateTime = offenceEndDateTime;
    }

    public Calendar getOffenceStartDateTime() {
        return offenceStartDateTime;
    }

    public void setOffenceStartDateTime(Calendar offenceStartDateTime) {
        this.offenceStartDateTime = offenceStartDateTime;
    }

    public void setOffenceID(Integer offenceID) {
        this.offenceID = offenceID;
    }
    
    public String getAppealType() {
    	return appealType;
    }
    
    public void setAppealType(String appealType) {
    	this.appealType = appealType;
    }
    
    public boolean isUpdatingAdditionalInfo(){
        return updatingAdditionalInfo;
    }
    
    public void setUpdatingAdditionalInfo(boolean updatingAdditionalInfo){
        this.updatingAdditionalInfo = updatingAdditionalInfo;
    }
}