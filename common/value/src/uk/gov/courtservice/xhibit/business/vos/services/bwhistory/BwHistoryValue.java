package uk.gov.courtservice.xhibit.business.vos.services.bwhistory;

import java.util.Comparator;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;

public class BwHistoryValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private BwHistoryBasicValue bwHistoryBV;
	
	private Integer bwHistoryId;
	private Integer defendantOnCaseId;
	private Date bwIssueDate;
	private Date bwEndDate;
	private String bcStatusBwIssued;
	private String bcStatusBwEnded;
	private String withdrawn;
	private String absconding;
	private String obsInd;
	private Date creationDate;
	
	public BwHistoryValue() {
	}
	
	public BwHistoryBasicValue getBwHistoryBV() {
		return bwHistoryBV;
	}

	public void setBwHistoryBV(BwHistoryBasicValue bwHistoryBV) {
		this.bwHistoryBV = bwHistoryBV;
	}

	public Integer getBwHistoryId() {
		return bwHistoryId;
	}

	public void setBwHistoryId(Integer bwHistoryId) {
		this.bwHistoryId = bwHistoryId;
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Date getBwIssueDate() {
		return bwIssueDate;
	}

	public void setBwIssueDate(Date bwIssueDate) {
		this.bwIssueDate = bwIssueDate;
	}

	public Date getBwEndDate() {
		return bwEndDate;
	}

	public void setBwEndDate(Date bwEndDate) {
		this.bwEndDate = bwEndDate;
	}

	public String getBcStatusBwIssued() {
		return bcStatusBwIssued;
	}

	public void setBcStatusBwIssued(String bcStatusBwIssued) {
		this.bcStatusBwIssued = bcStatusBwIssued;
	}

	public String getBcStatusBwEnded() {
		return bcStatusBwEnded;
	}

	public void setBcStatusBwEnded(String bcStatusBwEnded) {
		this.bcStatusBwEnded = bcStatusBwEnded;
	}

	public String getWithdrawn() {
		return withdrawn;
	}

	public void setWithdrawn(String withdrawn) {
		this.withdrawn = withdrawn;
	}

	public String getAbsconding() {
		return absconding;
	}

	public void setAbsconding(String absconding) {
		this.absconding = absconding;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public BwHistoryValue(Integer bwHistoryId, Integer version) {
		super(bwHistoryId, version);
	}

	public BwHistoryValue(Integer defendantOnCaseId, Date bwIssueDate, Date bwEndDate, 
							   String bcStatusBwIssued, String bcStatusBwEnded, String withdrawn, String absconding,
							   String obsInd, Date creationDate) {	
		setDefendantOnCaseId(defendantOnCaseId);
		setBwIssueDate(bwIssueDate);
		setBwEndDate(bwEndDate);
		setBcStatusBwIssued(bcStatusBwIssued);
		setBcStatusBwEnded(bcStatusBwEnded);
		setWithdrawn(withdrawn);
		setAbsconding(absconding);
		setObsInd(obsInd);
		setCreationDate(creationDate);
	}

	public static Comparator<BwHistoryValue> getSortByIssueDateDESC() {
		return sortByIssueDateDESC;
	}

	private static Comparator<BwHistoryValue> sortByIssueDateDESC = new Comparator<BwHistoryValue>() {
		@Override
		public int compare(BwHistoryValue o1, BwHistoryValue o2) {
			int diff = getDiff(o2.getBwIssueDate(), o1.getBwIssueDate());
			if (Integer.valueOf(0).equals(diff)) {
				diff = getDiff(o2.getBwEndDate(), o1.getBwEndDate());
			}
			return diff;
		}
		
		// Cater for null objects
		private int getDiff(Date o1, Date o2) {
			if (o1 != null && o2 != null) {
				return o1.compareTo(o2);
			} else if (o1 == null && o2 == null) {
				return 0;
			} else {	
				return o1 != null ? 1 : -1;
			}
		}
	};

}