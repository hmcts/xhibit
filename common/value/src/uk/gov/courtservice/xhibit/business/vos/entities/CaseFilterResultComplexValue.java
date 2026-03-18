package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

public class CaseFilterResultComplexValue implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String APPEAL = "A";
	private static final String TRIAL = "T";
	private static final String SENTENCE = "S";

	private CaseComplexValue caseComplexValue;
	private CaseListingEntryComplexValue caseListingEntryComplexValue;
	
	public CaseFilterResultComplexValue() {
		super();
	}
	
	/**
	 * @return the caseComplexValue
	 */
	public CaseComplexValue getCaseComplexValue() {
		return caseComplexValue;
	}

	/**
	 * @param caseComplexValue
	 */
	public void setCaseComplexValue(CaseComplexValue caseComplexValue) {
		this.caseComplexValue = caseComplexValue;
	}

	/**
	 * @return the caseListingEntryComplexValue 
	 */
	public CaseListingEntryComplexValue getCaseListingEntryComplexValue() {
		return caseListingEntryComplexValue;
	}

	/**
	 * @param caseListingEntryComplexValue 
	 */
	public void setCaseListingEntryComplexValue(CaseListingEntryComplexValue caseListingEntryComplexValue) {
		this.caseListingEntryComplexValue = caseListingEntryComplexValue;
	}

	/**
	 * @return the case type 
	 */
	public String getCaseType() {
		return getCaseComplexValue().getCaseType();
	}

	/**
	 * @return the sent for committal date (as per case summary logic) 
	 */
	public Timestamp getSentCommittalDate() {	
		if (APPEAL.equals(getCaseType())) {
			return getCaseComplexValue().getAppealLodgedDate();
		} else if (getCaseComplexValue().getCommittalDate() != null) {
			return getCaseComplexValue().getCommittalDate();
		} else {
			return getCaseComplexValue().getSentForTrialDate();
		}
	}
	
	public static final Comparator<CaseFilterResultComplexValue> SortByCaseType = new Comparator<CaseFilterResultComplexValue>() {
		
		private final Integer HIGHER = Integer.valueOf(-1);
		private final Integer SAME = Integer.valueOf(0);
		private final Integer LOWER = Integer.valueOf(1);

		private Integer compareNullableDates(Timestamp o1Date, Timestamp o2Date) {
			Integer diff = SAME;
			if (o1Date != null && o2Date != null) {
				diff = o1Date.compareTo(o2Date);
			} else if (o1Date != null || o2Date != null) {
				diff = o1Date != null ? HIGHER : LOWER;
			}
			return diff;
		}
		
		private Integer getCaseTypePriority(String caseType) {
			if (TRIAL.equals(caseType)) {
				return Integer.valueOf(1);
			} else if (SENTENCE.equals(caseType)) {
				return Integer.valueOf(2);
			} else if (APPEAL.equals(caseType)) {
				return Integer.valueOf(3);
			} 
			return Integer.valueOf(99);
		}

		public int compare(CaseFilterResultComplexValue o1, CaseFilterResultComplexValue o2) {
			// Sort by case type priority (T, S, A then others) ...
			final Integer o1CaseTypePriority = getCaseTypePriority(o1.getCaseType());
			final Integer o2CaseTypePriority = getCaseTypePriority(o2.getCaseType());
			Integer diff = o1CaseTypePriority.compareTo(o2CaseTypePriority);
			
			// ...then Sort by case Type (to sort out the other case types) ...
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1.getCaseType().compareTo(o2.getCaseType());
			}

			// ...then Sort by committal date
			if (Integer.valueOf(0).equals(diff)) {
				diff = compareNullableDates(o1.getSentCommittalDate(), o2.getSentCommittalDate());
			}

			// ...then by case number (so cases on the same date will appear in order)
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1.getCaseComplexValue().getCaseNumber().compareTo(o2.getCaseComplexValue().getCaseNumber());
			}
			return diff; 
		}
	};
}
