package uk.gov.courtservice.xhibit.business.vos.services.charge;

/**
 * Class handles the data thrown back from the offence link validation process
 * 
 * @author Ross McArthur
 *
 */
public class OffenceValidationReturnValue {
	private Integer defendantOnCase;
	private boolean disposalsMissing = false;
	private boolean drivingOffencesWithoutDisposals = false;
	private boolean noDisINTFound = false;
	private boolean noOffences = false;
	private boolean noDVLA = false;
	private boolean hasDrivingDisposals = false;
	private boolean caseIsD20Valid = false;

	/**
	 * 
	 * Getters and setters
	 */
	public boolean isDisposalsMissing() {
		return disposalsMissing;
	}

	public void setDisposalsMissing(boolean disposalsMissing) {
		this.disposalsMissing = disposalsMissing;
	}

	public boolean isDrivingOffencesWithoutDisposals() {
		return drivingOffencesWithoutDisposals;
	}

	public void setDrivingOffencesWithoutDisposals(boolean drivingOffencesWithoutDisposals) {
		this.drivingOffencesWithoutDisposals = drivingOffencesWithoutDisposals;
	}

	public boolean isNoDisINTFound() {
		return noDisINTFound;
	}

	public void setNoDisINTFound(boolean noDisINTFound) {
		this.noDisINTFound = noDisINTFound;
	}

	public boolean isNoOffences() {
		return noOffences;
	}

	public void setNoOffences(boolean noOffences) {
		this.noOffences = noOffences;
	}

	public boolean isNoDVLA() {
		return noDVLA;
	}

	public void setNoDVLA(boolean noDVLA) {
		this.noDVLA = noDVLA;
	}

	public boolean hasDrivingDisposals() {
		return hasDrivingDisposals;
	}

	public void setHasDrivingDisposals(boolean hasDrivingDisposals) {
		this.hasDrivingDisposals = hasDrivingDisposals;
	}

	public boolean isCaseIsD20Valid() {
		return caseIsD20Valid;
	}

	public void setCaseIsD20Valid(boolean caseIsD20Valid) {
		this.caseIsD20Valid = caseIsD20Valid;
	}

	public Integer getDefendantOnCase() {
		return defendantOnCase;
	}

	public void setDefendantOnCase(Integer defendantOnCase) {
		this.defendantOnCase = defendantOnCase;
	}
}
