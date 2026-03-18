package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;

/**
 * <p>
 * Title: DisposalReferenceValue
 * </p>
 * <p>
 * Description: This object represents the reference data for a disposal
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class DisposalReferenceValue extends ResultValue implements Cloneable, Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * The code for the total category
     */
    public static final String TOTAL_CATEGORY = "T";

    // Data
    private int refDisposalTypeId;

    private String disposalCode;

    private int templateVersion;

    private String title;

    private int lineAvail;

    private String category;

    // copy set to true when making a copy, can only update copies
    private boolean copy = false;
    
    //CCN0400 - KD - Deportation Reasons
    private String custodial="";
    
    private String suspended="";
    
    private String seriousDrugOffence ="";
    
    private String recommendedDeportation ="";
    
    private boolean deportationVisibilty =true;
    
    private boolean hateCrimeTabVisibility;
    
    private boolean aggravatingTabVisibility;
    
    private boolean generalDisability;
    
    private boolean generalSexual;
    
    private boolean generalTransgender;
    
    private boolean hateCrimeFlag;
    
    private boolean raceAndReligionAggravated;
    
    private boolean racialAggravated;
    
    private boolean religionAggravated;
    
    private boolean victimDisability;
    
    private boolean victimSexual;
    
    private boolean victimTransgender;

    // Aggravating reasons
    private boolean aggravatingAssaultOnWorkers;
	private boolean aggravatingTerroristConnection;
    private boolean aggravatingEmergencyWorkers;
    private boolean aggravatingHostility;
    private boolean aggravatingSexualOrientation;
    private boolean aggravatingSexualOrientationOfVictim;
    private boolean aggravatingTransgender;
    private boolean aggravatingTransgenderOfVictim;
    
    /**
     * Disposal Line Data (we always have some lines) automatically sorted
     */
    private final SortedList lines = new SortedList();

    /**
     * Construct a disposal reference with the specified data.
     */
    public DisposalReferenceValue(Integer refDisposalTypeId, String disposalCode, Integer templateVersion,
            String title, Integer lineAvail, String category) {
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId: null");
        }
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        if (templateVersion == null) {
            throw new IllegalArgumentException("templateVersion: null");
        }
        if (title == null) {
            throw new IllegalArgumentException("title: null");
        }

        this.refDisposalTypeId = refDisposalTypeId.intValue();
        this.disposalCode = disposalCode;
        this.templateVersion = templateVersion.intValue();
        this.title = title;
        this.lineAvail = lineAvail == null ? -1 : lineAvail.intValue();
        this.category = category;
    }
    /**
     * Construct a disposal reference with the specified data.
     */
    public DisposalReferenceValue(Integer refDisposalTypeId, String disposalCode, Integer templateVersion,
            String title, Integer lineAvail, String category, String custodial, String suspended, 
            String seriousDrugOffence, String recommendedDeportation, boolean visibility, boolean hateCrimeTabVisibility,
            boolean generalDisability, boolean generalSexual, boolean generalTransgender, boolean hateCrimeFlag, 
            boolean raceAndReligionAggravated, boolean racialAggravated, boolean religionAggravated, boolean victimDisability,
            boolean victimSexual, boolean victimTransgender) {
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId: null");
        }
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        if (templateVersion == null) {
            throw new IllegalArgumentException("templateVersion: null");
        }
        if (title == null) {
            throw new IllegalArgumentException("title: null");
        }

        this.refDisposalTypeId = refDisposalTypeId.intValue();
        this.disposalCode = disposalCode;
        this.templateVersion = templateVersion.intValue();
        this.title = title;
        this.lineAvail = lineAvail == null ? -1 : lineAvail.intValue();
        this.category = category;
        this.custodial = custodial;
        this.suspended = suspended;
        this.seriousDrugOffence = seriousDrugOffence;
        this.recommendedDeportation = recommendedDeportation;
        this.deportationVisibilty = visibility;
        this.hateCrimeTabVisibility = hateCrimeTabVisibility;
        this.generalDisability = generalDisability;
        this.generalSexual = generalSexual;
        this.generalTransgender = generalTransgender;
        this.hateCrimeFlag = hateCrimeFlag;
        this.raceAndReligionAggravated = raceAndReligionAggravated;
        this.racialAggravated = racialAggravated;
        this.religionAggravated = religionAggravated;
        this.victimDisability = victimDisability;
        this.victimSexual = victimSexual;
        this.victimTransgender = victimTransgender;
    }

    /**
     * Construct a disposal reference with the specified data.
     */
    public DisposalReferenceValue(int refDisposalTypeId, String disposalCode, int templateVersion, String title,
            int lineAvail, String category, String custodial, String suspended, 
            String seriousDrugOffence, String recommendedDeportation, boolean visibility, boolean hateCrimeTabVisibility,
            boolean generalDisability, boolean generalSexual, boolean generalTransgender, boolean hateCrimeFlag,
            boolean raceAndReligionAggravated, boolean racialAggravated, boolean religionAggravated, boolean victimDisability,
            boolean victimSexual, boolean victimTransgender,
            boolean aggravatingTabVisibility,
            boolean aggravatingAssaultOnWorkers, boolean aggravatingTerroristConnection,
            boolean aggravatingEmergencyWorkers, boolean aggravatingHostility,
            boolean aggravatingSexualOrientation, boolean aggravatingSexualOrientationOfVictim,
            boolean aggravatingTransgender, boolean aggravatingTransgenderOfVictim) {
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        if (title == null) {
            throw new IllegalArgumentException("title: null");
        }

        this.refDisposalTypeId = refDisposalTypeId;
        this.disposalCode = disposalCode;
        this.templateVersion = templateVersion;
        this.title = title;
        this.lineAvail = lineAvail;
        this.category = category;
        this.custodial = custodial;
        this.suspended = suspended;
        this.seriousDrugOffence = seriousDrugOffence;
        this.recommendedDeportation = recommendedDeportation;
        this.deportationVisibilty = visibility;
        this.hateCrimeTabVisibility = hateCrimeTabVisibility;
        this.generalDisability = generalDisability;
        this.generalSexual = generalSexual;
        this.generalTransgender = generalTransgender;
        this.hateCrimeFlag = hateCrimeFlag;
        this.raceAndReligionAggravated = raceAndReligionAggravated;
        this.racialAggravated = racialAggravated;
        this.religionAggravated = religionAggravated;
        this.victimDisability = victimDisability;
        this.victimSexual = victimSexual;
        this.victimTransgender = victimTransgender;
        setAggravatingTabVisibility(aggravatingTabVisibility);
        setAggravatingAssaultOnWorkers(aggravatingAssaultOnWorkers);
    	setAggravatingTerroristConnection(aggravatingTerroristConnection);
        setAggravatingEmergencyWorkers(aggravatingEmergencyWorkers);
        setAggravatingHostility(aggravatingHostility);
        setAggravatingSexualOrientation(aggravatingSexualOrientation);
        setAggravatingSexualOrientationOfVictim(aggravatingSexualOrientationOfVictim);
        setAggravatingTransgender(aggravatingTransgender);
        setAggravatingTransgenderOfVictim(aggravatingTransgenderOfVictim);
    }

    // getters
    public int getRefDisposalTypeId() {
        return refDisposalTypeId;
    }

    public String getDisposalCode() {
        return disposalCode;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }

    public String getTitle() {
        return title;
    }

    public int getLineAvail() {
        return lineAvail;
    }

    public String getCategory() {
        return category;
    }
    
    public String getSuspended() {
        return suspended;
    }
    
    public String getCustodial() {
        return custodial;
    }
    
    public String getRecommendedDeportation() {
        return recommendedDeportation;
    }

    public String getSeriousDrugOffence() {
        return seriousDrugOffence;
    }


    // setters
    public void setRecommendedDeportation(String recommendedDeportation) {
        this.recommendedDeportation = recommendedDeportation;
    }

    public void setSeriousDrugOffence(String seriousDrugOffence) {
        this.seriousDrugOffence = seriousDrugOffence;
    }
    public void setRefDisposalTypeId(int refDisposalTypeId) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.refDisposalTypeId = refDisposalTypeId;
    }

    public void setDisposalCode(String disposalCode) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        this.disposalCode = disposalCode;
    }

    public void setTemplateVersion(int templateVersion) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.templateVersion = templateVersion;
    }

    public void setTitle(String title) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        if (title == null) {
            throw new IllegalArgumentException("title: null");
        }
        this.title = title;
    }

    public void setLineAvail(int lineAvail) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.lineAvail = lineAvail;
    }

    public void setCategory(String category) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.category = category;
    }
    
    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }
    
    public void setCustodial(String custodial) {
        this.custodial = custodial;
    }

    public boolean getDeportationVisibilty() {
        return deportationVisibilty;
    }
    public void setDeportationVisibilty(boolean deportationVisibilty) {
        this.deportationVisibilty = deportationVisibilty;
    }
    
    public boolean isAggravatingTabVisible() {
        return aggravatingTabVisibility;
    }
    public void setAggravatingTabVisibility(boolean aggravatingTabVisibility) {
        this.aggravatingTabVisibility = aggravatingTabVisibility;
    }
    
    public boolean isHateCrimeTabVisible() {
        return hateCrimeTabVisibility;
    }
    public void setHateCrimeTabVisibility(boolean hateCrimeTabVisibility) {
        this.hateCrimeTabVisibility = hateCrimeTabVisibility;
    }
    // business

    /**
     * Clone this object (this is a deep copy)
     */
    public Object clone() {
        DisposalReferenceValue disposalReferenceValue = new DisposalReferenceValue(getRefDisposalTypeId(),
                getDisposalCode(), getTemplateVersion(), getTitle(), getLineAvail(), getCategory(), getCustodial(), getSuspended(),
                getSeriousDrugOffence(), getRecommendedDeportation(), getDeportationVisibilty(), isHateCrimeTabVisible(),
                isGeneralDisability(), isGeneralSexual(), isGeneralTransgender(), isHateCrimeFlag(), isRaceAndReligionAggravated(), isRacialAggravated(),
                isReligionAggravated(), isVictimDisability(), isVictimSexual(), isVictimTransgender(),
                isAggravatingTabVisibility(),
                isAggravatingAssaultOnWorkers(), isAggravatingTerroristConnection(),
                isAggravatingEmergencyWorkers(), isAggravatingHostility(),
                isAggravatingSexualOrientation(), isAggravatingSexualOrientationOfVictim(),
                isAggravatingTransgender(), isAggravatingTransgenderOfVictim());
        for (int i = 0, c = getLineCount(); i < c; i++) {
            disposalReferenceValue.addLine(getLine(i).copy());
        }

        disposalReferenceValue.copy = true; // Allow the object to be
        // updated

        return disposalReferenceValue;
    }

    /**
     * Copy this object
     */
    public DisposalReferenceValue copy() {
        return (DisposalReferenceValue) clone();
    }

    /**
     * Add a disposal line
     * 
     * @param disposalLine
     *            the new disposalline
     */
    public void addLine(DisposalLineReferenceValue disposalLine) {
        if (disposalLine == null || disposalLine.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("disposalLine: " + disposalLine);
        }
        lines.add(disposalLine);
    }

    /**
     * Get the number of lines
     * 
     * @return the line count
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * Get the indexed item
     * 
     * @param return
     *            the indexed item or null if not found
     */
    public DisposalLineReferenceValue getLine(int index) {
        return (DisposalLineReferenceValue) lines.get(index);
    }

    /**
     * Get the line using for the specified dilSeqNo
     * 
     * @return the request row
     * @throws IllegalArgumentException
     *             if the row can not be found
     */
    public DisposalLineReferenceValue getLineByDilSeqNo(int dilSeqNo) {
        for (int i = 0, s = getLineCount(); i < s; i++) {
            DisposalLineReferenceValue value = getLine(i);
            if (value.getDilSeqNo() == dilSeqNo) {
                return value;
            }
        }
        throw new IllegalArgumentException("dilSeqNo: " + dilSeqNo);
    }

    /**
     * Get the line using for the specified refDisposalLineId
     * 
     * @return the request row
     * @throws IllegalArgumentException
     *             if the row can not be found
     */
    public DisposalLineReferenceValue getLineByRefDisposalLineId(Integer refDisposalLineId) {
        if (refDisposalLineId == null) {
            throw new IllegalArgumentException("refDisposalLineId: null");
        }
        return getLineByRefDisposalLineId(refDisposalLineId.intValue());
    }

    public DisposalLineReferenceValue getLineByRefDisposalLineId(int refDisposalLineId) {
        for (int i = 0, s = getLineCount(); i < s; i++) {
            DisposalLineReferenceValue value = getLine(i);
            if (value.getRefDisposalTypeId() == refDisposalLineId) {
                return value;
            }
        }
        throw new IllegalArgumentException("refDisposalLineId: " + refDisposalLineId);
    }

    /**
     * Get the dil seq no for the specified line
     * 
     * @return the dil seq no of the specified line
     */
    public int getDilSeqNo(int refDisposalLineId) {
        return getLineByRefDisposalLineId(refDisposalLineId).getDilSeqNo();
    }

    public int getDilSeqNo(Integer refDisposalLineId) {
        if (refDisposalLineId == null) {
            throw new IllegalArgumentException("refDisposalLineId: null");
        }
        return getDilSeqNo(refDisposalLineId.intValue());
    }

    /**
     * Create a related criminal court disposal for the specified
     * defendantOnOffence
     */

    public DisposalValue createDisposal(int defendantOnOffenceId) {
        return createDisposal(defendantOnOffenceId, true);
    }

    public DisposalValue createDisposal(Integer defendantOnOffenceId) {
        return createDisposal(defendantOnOffenceId, true);
    }

    /**
     * Create a criminal court disposal for the specified defendantOnOffecnce
     * defendantOnCase
     */
    public DisposalValue createDisposal(int id, boolean related) {
        return createDisposal(id, related, false);
    }

    public DisposalValue createDisposal(Integer id, boolean related) {
        return createDisposal(id, related, false);
    }

    /**
     * Create a magistrate disposal for the specified defendantOnOffecnce
     * defendantOnCase
     */
    public DisposalValue createDisposal(int id, boolean related, boolean magistrate) {
        return createDisposal(new Integer(id), related, magistrate);
    }

    public DisposalValue createDisposal(Integer id, boolean related, boolean magistrate) {
        if (id == null) {
            throw new IllegalArgumentException("id: null");
        }
        return (related ? new DisposalValue(getRefDisposalTypeId(), id, null, magistrate ? "M" : "C", null)
                : new DisposalValue(getRefDisposalTypeId(), null, id, magistrate ? "M" : "C", null));
    }

    /**
     * Create a criminal variation disposal for the specified
     * defendantOnOffecnce defendantOnCase
     */
    public DisposalValue createDisposal(int id, boolean related, int originalId) {
        return createDisposal(new Integer(id), related, new Integer(originalId));
    }

    public DisposalValue createDisposal(int id, boolean related, Integer originalId) {
        return createDisposal(new Integer(id), related, originalId);
    }

    public DisposalValue createDisposal(Integer id, boolean related, int originalId) {
        return createDisposal(id, related, new Integer(originalId));
    }

    public DisposalValue createDisposal(Integer id, boolean related, Integer originalId) {
        if (id == null) {
            throw new IllegalArgumentException("id: null");
        }
        if (originalId == null) {
            throw new IllegalArgumentException("originalId: null");
        }
        return (related ? new DisposalValue(getRefDisposalTypeId(), id, null, "C", originalId) : new DisposalValue(
                getRefDisposalTypeId(), null, id, "C", originalId));
    }

    /**
     * Copy the disposal to the specified defendantOnCase or defendantOnOffence
     */
    public DisposalValue copyDisposal(DisposalValue value, Integer id) {
        if (value == null) {
            throw new IllegalArgumentException("value: null");
        }
        if (id == null) {
            throw new IllegalArgumentException("id: null");
        }

        DisposalValue newValue = value.isRelatedDisposal() ? new DisposalValue(getRefDisposalTypeId(), id, null, value
                .getCourtType(), value.getPsdDisposal2Id()) : new DisposalValue(getRefDisposalTypeId(), null, id, value
                .getCourtType(), value.getPsdDisposal2Id());

        for (int i = 0, c = getLineCount(); i < c; i++) {
            DisposalLineReferenceValue refDisposalLine = getLine(i);
            Integer refDisposalLineId = new Integer(refDisposalLine.getRefDisposalLineId());
            for (int j = 0, s = value.getLineCount(refDisposalLineId); j < s; j++) {
                newValue.addLine(refDisposalLine.copyValue(value.getLine(refDisposalLineId, j)));
            }
        }

        return newValue;
    }

    /**
     * Get Disposal Text - Using Disposal Model
     */
    public String getDisposalText(DisposalValue value) {
        if (value == null || value.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("value: " + value);
        }
        return getCrestText(value);
    }

    /**
     * Get Case Progress Summary Text
     */
    public String getCaseProgressSummaryText(DisposalValue value) {
        if (value == null || value.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("value: " + value);
        }
        return getCrestText(value);
    }

    /**
     * Get Case Progress Detail Text
     */
    public String getCaseProgressDetailText(DisposalValue value) {
        if (value == null || value.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("value: " + value);
        }
        return disposalCode + " - " + getResultSheetText(value);
    }

    /**
     * Return the form bf text for the disposal
     */
    public String getFormBFText(DisposalValue value) {
        if (value == null || value.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("value: " + value);
        }
        StringBuffer buffer = new StringBuffer();
        String replaced = getReplacedCrestText(value);
        if (replaced != null) {
            buffer.append(replaced);
            buffer.append(" ");
        }
        buffer.append(disposalCode);
        buffer.append(" - ");
        buffer.append(getResultSheetText(value));
        return buffer.toString();
    }

    /**
     * Return the court log text for the disposal, iterate over the data adding
     * all the form data and any line inserts (free text!)
     */
    public String getResultSheetText(DisposalValue value) {
        if (value == null || value.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("value: " + value);
        }

        StringBuffer buffer = new StringBuffer();

        for (int refIndex = 0, refCount = getLineCount(); refIndex < refCount; refIndex++) {
            DisposalLineReferenceValue ref = getLine(refIndex);
            Integer refDisposalLineId = new Integer(ref.getRefDisposalLineId());

            if (ref.isFormPrint()) {
                int dataCount = value.getLineCount(refDisposalLineId);
                if (0 < dataCount) {
                    setCourtLogText(buffer, ref, value.getLine(refDisposalLineId, 0));
                    for (int dataIndex = 1; dataIndex < dataCount; dataIndex++) {
                        setCourtLogText(buffer, ref, value.getLine(refDisposalLineId, dataIndex));
                    }
                } else {
                    setCourtLogText(buffer, ref);
                }
            } else if (ref.isLineInsert()) {
                for (int dataIndex = 0, dataCount = value.getLineCount(refDisposalLineId); dataIndex < dataCount; dataIndex++) {
                    setCourtLogText(buffer, ref, value.getLine(refDisposalLineId, dataIndex));
                }
            }
        }

        return renderString(buffer.toString());
    }

    private static void setCourtLogText(StringBuffer buffer, DisposalLineReferenceValue ref) {
        setCourtLogText(buffer, ref.formatData());
    }

    private static void setCourtLogText(StringBuffer buffer, DisposalLineReferenceValue ref,
            XhbDisposalLineBasicValue data) {
        if (!(ref.isDeleted(data) || ref.isDeletedG1(data) || ref.isDeletedG2(data))) {
            setCourtLogText(buffer, ref.formatData(data));
        }
    }

    private static void setCourtLogText(StringBuffer buffer, String data) {
        if (data != null) {
            if (buffer.length() > 0) {
                buffer.append(' ');
            }
            buffer.append(data);
        }
    }

    /**
     * Return the crest log text for the disposal
     */
    public String getCrestText(DisposalValue value) {
        if (value == null || value.getRefDisposalTypeId() != getRefDisposalTypeId()) {
            throw new IllegalArgumentException("value: " + value);
        }

        StringBuffer buffer = new StringBuffer();

        String replaced = getReplacedCrestText(value);
        if (replaced != null) {
            buffer.append(replaced);
            buffer.append(" ");
        }
        
        // Append the Disposal Title
        buffer.append(title);
        
        String value1 = getValue1CrestText(value);
        if (value1 != null) {
            buffer.append(" ");
            buffer.append(value1);
        }
        
        // Driving specific disposal changes
        if (disposalCode.equals("DISDISC") || disposalCode.equals("DISOBLG") || disposalCode.equals("DISTOT")) {
        
	        // The life checkbox is part of a "Delete where appropriate" therefore we need to check for its non-checking!
	        boolean lifeChecked = isLifeChecked(value);
	        if (!lifeChecked) { // Not Deleted
	            buffer.append("** Life **");
	            buffer.append(" ");
	        }
	        
	        String extPeriodText = getExtensionPeriodCrestText(value);
            if (extPeriodText != null) {
                buffer.append(" ");
                buffer.append(extPeriodText);
            }
            
            boolean showReductionDataIfRehabCourseTaken = false;
	        
	        String dttp1 = getDTTP1(value);
	        if (dttp1 != null) {
	        	showReductionDataIfRehabCourseTaken = true;
	            buffer.append(dttp1);
	            buffer.append(" ");
	        }
	        
	        String dtetp4 = getDTETP4(value);
	        if (dtetp4 != null) {
	        	showReductionDataIfRehabCourseTaken = true;
	            buffer.append(dtetp4);
	            buffer.append(" ");
	            
	            String alcDrugLevel = getAlcDruglevel(value);
	            if (alcDrugLevel != null) {
	                buffer.append(" ");
	                buffer.append(alcDrugLevel);
	            }	            
	        }
	        
	        if (showReductionDataIfRehabCourseTaken) {
	        	String text = getDrivingAmountReductionRehab(value);
		        if (text != null) {
		            buffer.append(" Amount of reduction if Rehabiliation Course to be taken: ");
		            buffer.append(text);
		        }
	        }
            
            String prevInt = getPrevInterim(value);
	        if (prevInt != null) {
	            buffer.append(prevInt);
	            buffer.append(" ");
	        }
        
        } else {

	        String value2 = getValue2CrestText(value);
	        if (value2 != null) {
	            buffer.append(" ");
	            buffer.append(value2);
	        }
	        
	        // DISQ TDISQ Special
	        if (("DISQ".equals(disposalCode) || "TDISQ".equals(disposalCode)) && value1 == null && value2 == null) {
	            buffer.append(" LIFE");
	        }
        }
        

        buffer.append(".");

        String effective = getEffectiveCrestText(value);
        if (effective != null) {
            buffer.append(" ");
            buffer.append(effective);
        }

        return buffer.toString();
    }

    private String getValue1CrestText(DisposalValue value) {

        // CUSTEXT Exclusion
        if ("CUSTEXT".equals(disposalCode)) {
            return null;
        }

        // PASU TPASU Special
        if ("PASU".equals(disposalCode) || "TPASU".equals(disposalCode)) {
            return "[see detail]";
        }

        // Check Pounds
        String amount1 = getDBDestinData(value, "D12");
        if (amount1 != null) {
            return "£" + amount1;
        }
        // Check Generic
        amount1 = getDBDestinData(value, "D5");
        String unit1 = getDBDestinData(value, "D6");
        if (amount1 != null && unit1 != null) {
            return amount1 + " " + unit1;
        }
        // No Value
        return null;
    }

    private String getValue2CrestText(DisposalValue value) {
        // CUSTEXT Exclusion
        if ("CUSTEXT".equals(disposalCode)) {
            return null;
        }

        // PASU TPASU Exclusion
        if ("PASU".equals(disposalCode) || "TPASU".equals(disposalCode)) {
            return null;
        }

        // Non Total Exclusion
        if (!"T".equals(category)) {
            return null;
        }

        // Check Pounds
        String amount2 = getDBDestinData(value, "D13");
        if (amount2 != null) {
            return "£" + amount2;
        }

        // Check Generic
        amount2 = getDBDestinData(value, "D7");
        String unit2 = getDBDestinData(value, "D8");
        if (amount2 != null && unit2 != null) {
            return amount2 + " " + unit2;
        }

        // No Value
        return null;
    }
    
    
    
    /**
     * Introduced a 3rd Duration/Units field for DISOBLG
     * 
     * @param value
     * @return
     */
    private String getExtensionPeriodCrestText(DisposalValue value) {
        
        // Check Generic
        String amount3 = getDBDestinData(value, "D24");
        String unit3 = getDBDestinData(value, "D25");
        if (amount3 != null && unit3 != null) {
            return "Includes extension period:" + amount3 + " " + unit3;
        }

        // No Value
        return null;
    }

    private String getEffectiveCrestText(DisposalValue value) {
        String effective = getDBDestinData(value, "D9");
        if ("Y".equals(effective)) {
            return "(Effective Sentence)";
        } else if ("N".equals(effective)) {
            return "(Non-Effective Sentence)";
        } else {
            return null;
        }
    }

    private String getReplacedCrestText(DisposalValue value) {
        String replaced = getDBDestinData(value, "D11");
        if ("Y".equals(replaced)) {
            return "** Replaced **";
        } else {
            return null;
        }
    }
    
    private String getDTETP4(DisposalValue value) {
        String dtetp4 = getDBDestinData(value, "D20");
        if ("Y".equals(dtetp4)) {
            return "** DTETP-4 **";
        } else {
            return null;
        }
    }
    
    private String getDrivingAmountReductionRehab(DisposalValue value) {
        String amount = getDBDestinData(value, "D7");
        String unit = getDBDestinData(value, "D8");
        if (amount != null && unit != null) {
        	return amount + " " + unit;
        }
        return null;
    }
    
    private String getAlcDruglevel(DisposalValue value) {
        String alcDrugLevelValue = getDBDestinData(value, "D27");
        if (alcDrugLevelValue != null && alcDrugLevelValue.length() >0) {
            String retVal = "Alcohol/Drug Level: " + alcDrugLevelValue;
            String alcDrugLevelType = getDBDestinData(value, "D21");
            if (alcDrugLevelType.length() >0) {
            	retVal += " " + alcDrugLevelType;
            }
            return retVal;
        }
        return null;
    }
    
    private boolean isLifeChecked(DisposalValue value) {
        for (int i = 0, c = getLineCount(); i < c; i++) {
            DisposalLineReferenceValue ref = getLine(i);
            if ("D26".equals(ref.getDbDestin())) {
                if (0 < value.getLineCount(ref.getRefDisposalLineId())) {
                    XhbDisposalLineBasicValue data = value.getLine(ref.getRefDisposalLineId(), 0);
                    if (!(ref.isDeleted(data) || ref.isDeletedG2(data))) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
    
    private String getPrevInterim(DisposalValue value) {
        String prevInterim = getDBDestinData(value, "D22");
        if ("Y".equals(prevInterim)) {
            return "** Previous Interim **";
        } else {
            return null;
        }
    }
    
    private String getDTTP1(DisposalValue value) {
        String dttp1 = getDBDestinData(value, "D23");
        if ("Y".equals(dttp1)) {
            return "** DTTP-1 **";
        } else {
            return null;
        }
    }

    /**
     * Gets the disposal line data for the given disposal and prompt.
     * 
     * @param value
     *            DisposalValue.
     * @param prompt
     *            the disposal line prompt.
     * @return the data for the given disposal line prompt.
     */
    public String getDisposalLineDataByPrompt(final DisposalValue value, final String prompt) {
        for (int i = 0, c = getLineCount(); i < c; i++) {
            DisposalLineReferenceValue ref = getLine(i);
            if (ref.getPrompt() != null && prompt.trim().equals(ref.getPrompt().trim())) {
                if (0 < value.getLineCount(ref.getRefDisposalLineId())) {
                    XhbDisposalLineBasicValue data = value.getLine(ref.getRefDisposalLineId(), 0);
                    if (!(ref.isDeleted(data) || ref.isDeletedG1(data) || ref.isDeletedG2(data))) {
                        return data.getLineData();
                    }
                }
                return ref.getData();
            }
        }
        return null;
    }

    private String getDBDestinData(DisposalValue value, String dbDestin) {
        for (int i = 0, c = getLineCount(); i < c; i++) {
            DisposalLineReferenceValue ref = getLine(i);
            if (dbDestin.equals(ref.getDbDestin())) {
                if (0 < value.getLineCount(ref.getRefDisposalLineId())) {
                    XhbDisposalLineBasicValue data = value.getLine(ref.getRefDisposalLineId(), 0);
                    if (!(ref.isDeleted(data) || ref.isDeletedG1(data) || ref.isDeletedG2(data))) {
                        return data.getLineData();
                    }
                }
                return ref.getData();
            }
        }
        return null;
    }

    /**
     * Append debug information to the buffer
     */
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(DisposalReferenceValue.class.getName());
        buffer.append(" {refDisposalTypeId=");
        buffer.append(refDisposalTypeId);
        buffer.append(", disposalCode=");
        buffer.append(disposalCode);
        buffer.append(", templateVersion=");
        buffer.append(templateVersion);
        buffer.append(", title=");
        buffer.append(title);
        buffer.append(", lineAvail=");
        buffer.append(lineAvail);
        buffer.append(", category=");
        buffer.append(category);
        buffer.append(",");
        indent += 1;
        appendLine(buffer, indent);
        buffer.append("lines=");
        lines.appendDebug(buffer, indent);
        indent -= 1;
        appendLine(buffer, indent);
        buffer.append("}");
    }

    /**
     * <p>
     * Process the String as if it has been rendered to a console. Primarilly
     * this method processes backspaces. This method renders the text line by
     * line and handles the case where the cursor is moved into the margin
     * (negative). This is necesary as the court log text is printed from CREST
     * directly to the console or printer (not via XML or Swing!)
     * 
     * 
     * @param source
     *            the String to be rendered
     * @return the rendered string
     */
    private static String renderString(String source) {
        char[] sourceBuffer = source.toCharArray();
        char[] destBuffer = new char[sourceBuffer.length]; // Will be at
        // most
        // sourceBuffer.length
        // chars

        int lineIndex = 0; // Index of the start of the current line
        int columnIndex = 0; // Index of the current column

        for (int sourceIndex = 0; sourceIndex < sourceBuffer.length; sourceIndex++) {
            if (sourceBuffer[sourceIndex] == '\b') {
                columnIndex -= 1;
            } else {
                if (sourceBuffer[sourceIndex] == '\n') {
                    if (columnIndex >= 0) {
                        destBuffer[lineIndex + columnIndex] = '\n';
                        lineIndex += columnIndex + 1;
                    } else {
                        destBuffer[lineIndex] = '\n';
                        lineIndex += 1;
                    }
                    columnIndex = 0;
                } else {
                    if (columnIndex >= 0) {
                        destBuffer[lineIndex + columnIndex] = sourceBuffer[sourceIndex];
                    }
                    columnIndex += 1;
                }
            }
        }

        return new String(destBuffer, 0, columnIndex > 0 ? lineIndex + columnIndex : lineIndex);
    }
    public boolean isGeneralDisability() {
        return generalDisability;
    }
    public void setGeneralDisability(boolean generalDisability) {
        this.generalDisability = generalDisability;
    }
    public boolean isGeneralSexual() {
        return generalSexual;
    }
    public void setGeneralSexual(boolean generalSexual) {
        this.generalSexual = generalSexual;
    }
    public boolean isGeneralTransgender() {
        return generalTransgender;
    }
    public void setGeneralTransgender(boolean generalTransgender) {
        this.generalTransgender = generalTransgender;
    }
    public boolean isHateCrimeFlag() {
        return hateCrimeFlag;
    }
    public void setHateCrimeFlag(boolean hateCrimeFlag) {
        this.hateCrimeFlag = hateCrimeFlag;
    }
    public boolean isRaceAndReligionAggravated() {
        return raceAndReligionAggravated;
    }
    public void setRaceAndReligionAggravated(boolean raceAndReligionAggravated) {
        this.raceAndReligionAggravated = raceAndReligionAggravated;
    }
    public boolean isRacialAggravated() {
        return racialAggravated;
    }
    public void setRacialAggravated(boolean racialAggravated) {
        this.racialAggravated = racialAggravated;
    }
    public boolean isReligionAggravated() {
        return religionAggravated;
    }
    public void setReligionAggravated(boolean religionAggravated) {
        this.religionAggravated = religionAggravated;
    }
    public boolean isVictimDisability() {
        return victimDisability;
    }
    public void setVictimDisability(boolean victimDisability) {
        this.victimDisability = victimDisability;
    }
    public boolean isVictimSexual() {
        return victimSexual;
    }
    public void setVictimSexual(boolean victimSexual) {
        this.victimSexual = victimSexual;
    }
    public boolean isVictimTransgender() {
        return victimTransgender;
    }
    public void setVictimTransgender(boolean victimTransgender) {
        this.victimTransgender = victimTransgender;
    }
	public boolean isAggravatingTabVisibility() {
		return aggravatingTabVisibility;
	}
	public boolean isAggravatingAssaultOnWorkers() {
		return aggravatingAssaultOnWorkers;
	}
	public boolean isAggravatingTerroristConnection() {
		return aggravatingTerroristConnection;
	}
	public boolean isAggravatingEmergencyWorkers() {
		return aggravatingEmergencyWorkers;
	}
	public boolean isAggravatingHostility() {
		return aggravatingHostility;
	}
	public boolean isAggravatingSexualOrientation() {
		return aggravatingSexualOrientation;
	}
	public boolean isAggravatingSexualOrientationOfVictim() {
		return aggravatingSexualOrientationOfVictim;
	}
	public boolean isAggravatingTransgender() {
		return aggravatingTransgender;
	}
	public boolean isAggravatingTransgenderOfVictim() {
		return aggravatingTransgenderOfVictim;
	}
	public void setAggravatingAssaultOnWorkers(boolean aggravatingAssaultOnWorkers) {
		this.aggravatingAssaultOnWorkers = aggravatingAssaultOnWorkers;
	}
	public void setAggravatingTerroristConnection(boolean aggravatingTerroristConnection) {
		this.aggravatingTerroristConnection = aggravatingTerroristConnection;
	}
	public void setAggravatingEmergencyWorkers(boolean aggravatingEmergencyWorkers) {
		this.aggravatingEmergencyWorkers = aggravatingEmergencyWorkers;
	}
	public void setAggravatingHostility(boolean aggravatingHostility) {
		this.aggravatingHostility = aggravatingHostility;
	}
	public void setAggravatingSexualOrientation(boolean aggravatingSexualOrientation) {
		this.aggravatingSexualOrientation = aggravatingSexualOrientation;
	}
	public void setAggravatingSexualOrientationOfVictim(boolean aggravatingSexualOrientationOfVictim) {
		this.aggravatingSexualOrientationOfVictim = aggravatingSexualOrientationOfVictim;
	}
	public void setAggravatingTransgender(boolean aggravatingTransgender) {
		this.aggravatingTransgender = aggravatingTransgender;
	}
	public void setAggravatingTransgenderOfVictim(boolean aggravatingTransgenderOfVictim) {
		this.aggravatingTransgenderOfVictim = aggravatingTransgenderOfVictim;
	}






}
