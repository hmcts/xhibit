package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.util.ArrayList;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingSummaryValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */
public class HearingRecordModel {
	private XPanel callingClass;

	private CourtReportersTableModel courtRepTableModel; // = new

	// CourtReportersTableModel();

	private ProsecutorTableModel prosecutorTableModel; // = new

	// ProsecutorTableModel();

	private DefenceRepTableModel defenceRepTableModel; // = new

	// DefenceRepTableModel();

	private ScheduledHearingsTableModel shTableModel; // = new

	// ScheduledHearingsTableModel();

	// LinkedHearingsTableModel();

	private HearingSummaryValue hearingSummaryVal;

	private HearingRecordValue hearingRecordVal;

	// creating the new instance, which is immediately overwritten created
	// hundreds of unused objects every time!
	private XhibitApplicationController xac; // = new
	private DefendantOnCaseBasicValue defOnCaseBasicValue;
	// XhibitApplicationController();

	private HearingRecordPanel hearingRecordPanel;

	private Integer caseId;

	private Integer leadHearingId;

	private Integer hearingId;

	private Integer defendantId;

	private boolean legallyAided = false;

	private String courtClerk;

	private Integer scheduledHearingId = null;

	private String searchCase;

	private String caseNumber;

	private boolean updated = false;

	private boolean durationUpdated = false;

	private Long duration;

	private boolean representationUpdated = false;

	private Integer[] categories;

	private ArrayList defCatCodes;

	public static final int PRINT_SELECTED = 1;

	public static final int PRINT_ALL = 2;

	private int hearingsToPrint = 0;

	private Vector linkResultsTableData = null;

	private Vector defenceReps;

	// to determine if the case is an appeal - used since the defedendant
	// will be
	// appellant etc.
	private boolean isAppealType;

	private final static char APPEAL_CHAR = 'A';

	private final static String CASE_ABANDONED_BEFORE_TRIAL = "AB";

	private boolean justiceEdited = false;

	public HearingRecordModel() {
		// empty
	}

	/**
	 * Method to set the boolean isAppealType. It will be set to true if the
	 * case passed in is of type Appeal, in all other cases it will be set to
	 * false.
	 * 
	 * @param caseTypeAndNumber
	 *            String - the case type and number
	 */
	public void setIsAppealType(String caseTypeAndNumber) {
		if (caseTypeAndNumber == null || caseTypeAndNumber.length() == 0) {
			this.isAppealType = false;
		} else if (caseTypeAndNumber.charAt(0) == APPEAL_CHAR) {
			this.isAppealType = true;
		} else {
			this.isAppealType = false;
		}
	}

	public boolean isAppealType() {
		return this.isAppealType;
	}

	public CourtReportersTableModel getCourtRepTableModel() {
		return courtRepTableModel;
	}

	public ProsecutorTableModel getProsecutorTableModel() {
		return prosecutorTableModel;
	}

	public DefenceRepTableModel getDefenceRepTableModel() {
		return defenceRepTableModel;
	}

	public ScheduledHearingsTableModel getShTableModel() {
		return shTableModel;
	}

	public HearingRecordUpdateValue getHearingRecordUpdateVal() {
		return this.hearingRecordVal.getHearingRecordUpdateValue();
	}

	public HearingSummaryValue getHearingSummaryVal() {
		return hearingSummaryVal;
	}

	public void setHearingSummaryVal(HearingSummaryValue val) {
		this.hearingSummaryVal = val;
	}

	public HearingRecordValue getHearingRecordVal() {
		return hearingRecordVal;
	}

	public void setHearingRecordVal(HearingRecordValue val) {
		this.hearingRecordVal = val;
	}

	public DefendantOnCaseBasicValue getDefOnCaseBasicValue() {
		return defOnCaseBasicValue;
	}

	public void setDefOnCaseBasicValue(DefendantOnCaseBasicValue defOnCaseBasicValue) {
		this.defOnCaseBasicValue = defOnCaseBasicValue;
	}

	public XhibitApplicationController getXac() {
		return xac;
	}

	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getLeadHearingId() {
		return leadHearingId;
	}

	public void setLeadHearingId(Integer leadHearingId) {
		this.leadHearingId = leadHearingId;
	}

	public Integer getHearingId() {
		return hearingId;
	}

	public void setHearingId(Integer hearingId) {
		this.hearingId = hearingId;
	}

	public Integer getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}

	public boolean getLegallyAided() {
		return this.legallyAided;
	}

	public void setLegallyAided(boolean legallyAided) {
		this.legallyAided = legallyAided;
	}

	public String getCourtClerk() {
		return this.courtClerk;
	}

	public void setCourtClerk(String courtClerk) {
		this.courtClerk = courtClerk;
	}

	public void setScheduledHearingId(Integer id) {
		this.scheduledHearingId = id;
	}

	public Integer getScheduledHearingId() {
		return this.scheduledHearingId;
	}

	public void setSearchCase(String searchCase) {
		this.searchCase = searchCase;
	}

	public String getSearchCase() {
		return searchCase;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setHearingRecordPanel(HearingRecordPanel panel) {
		this.hearingRecordPanel = panel;
	}

	public HearingRecordPanel getHearingRecordPanel() {
		return this.hearingRecordPanel;
	}

	public HearingDetails getHearingDetailsPanel() {
		return hearingRecordPanel.getHearingDetails();
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
		uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("setting updated to " + updated);
	}

	public boolean isDurationUpdated() {
		return durationUpdated;
	}

	public void setDurationUpdated(boolean durUpdated) {
		this.durationUpdated = durUpdated;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public boolean isRepresentationUpdated() {
		return representationUpdated;
	}

	public void setRepresentationUpdated(boolean representationUpdated) {
		this.representationUpdated = representationUpdated;
	}

	public Integer[] getCategories() {
		return this.categories;
	}

	public void setCategories(Integer[] cat) {
		this.categories = cat;
	}

	public ArrayList getDefCatCodes() {
		return this.defCatCodes;
	}

	public void setDefCatCodes(ArrayList codes) {
		this.defCatCodes = codes;
	}

	public int getHearingsToPrint() {
		return this.hearingsToPrint;
	}

	public void setHearingsToPrint(int hearingsToPrint) {
		this.hearingsToPrint = hearingsToPrint;
	}

	public Vector getLinkResultsTableData() {
		return this.linkResultsTableData;
	}

	public void setLinkResultsTableData(Vector v) {
		this.linkResultsTableData = v;
	}

	public Vector getDefenceReps() {
		return this.defenceReps;
	}

	public void setDefenceReps(Vector defenceReps) {
		this.defenceReps = defenceReps;
	}

	public boolean isCaseAbandonedBeforeTrial() {
		return (isAppealType() && getHearingRecordVal() != null
				&& getHearingRecordVal().getHearingRecordDisplayValue() != null
				&& getHearingRecordVal().getHearingRecordDisplayValue().getVerdictValue() != null
				&& getHearingRecordVal().getHearingRecordDisplayValue().getVerdictValue().getVerdictCode() != null
				&& getHearingRecordVal().getHearingRecordDisplayValue().getVerdictValue().getVerdictCode()
						.equalsIgnoreCase(CASE_ABANDONED_BEFORE_TRIAL));
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	/**
	 * @return the justiceEdited
	 */
	public boolean getJusticeEdited() {
		return justiceEdited;
	}

	/**
	 * @param justiceEdited
	 *            the justiceEdited to set
	 */
	public void setJusticeEdited(boolean justiceEdited) {
		this.justiceEdited = justiceEdited;
	}
}
