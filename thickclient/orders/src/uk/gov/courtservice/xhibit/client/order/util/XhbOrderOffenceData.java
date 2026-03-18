package uk.gov.courtservice.xhibit.client.order.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;



public class XhbOrderOffenceData {
	
	private Integer offenceId;
	private String offCode; 
	private Date convictionStartDate = new Date(); 
	private boolean interim = false; 
	private boolean hasDisint = false;
	private boolean isSelected = false; 
	private ArrayList<DisposalValue> offenceDisposal = null;
	private Date verdictDate = null;
	private String appealType = "";
	private String refAppCode = "";
	private Date disqRemovedDate;
	private Date disqReimposedDate;
	private Timestamp disqSuspendedDate;
	private Integer defendantOnOffenceId;
	
	public XhbOrderOffenceData(Integer offenceId, String offCode, Date convictionStartDate, boolean interim, boolean hasDisint,
			boolean isSelected, ArrayList<DisposalValue> offenceDisposal, Date verdictDate, String appealType,
			String refAppCode, Date disqRemovedDate, Date disqReimposedDate, Timestamp disqSuspendedDate,
			Integer defendantOnOffenceId) {
		super();
		setOffenceId(offenceId);
		this.offCode = offCode;
		this.convictionStartDate = convictionStartDate;
		this.interim = interim;
		this.hasDisint = hasDisint;
		this.isSelected = isSelected;
		this.offenceDisposal = offenceDisposal;
		this.verdictDate = verdictDate;
		this.appealType  = appealType;
		this.refAppCode  = refAppCode;
		this.disqRemovedDate = disqRemovedDate;
		this.disqReimposedDate = disqReimposedDate;
		this.disqSuspendedDate = disqSuspendedDate;
		setDefendantOnOffenceId(defendantOnOffenceId);
	}
	
	public Integer getOffenceId() {
		return offenceId;
	}
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}
	public String getOffCode() {
		return offCode;
	}
	public void setOffCode(String offCode) {
		this.offCode = offCode;
	}
	public Date getConvictionStartDate() {
		return convictionStartDate;
	}
	public void setConvictionStartDate(Date convictionStartDate) {
		this.convictionStartDate = convictionStartDate;
	}
	public boolean isInterim() {
		return interim;
	}
	public void setInterim(boolean interim) {
		this.interim = interim;
	}
	public boolean isHasDisint() {
		return hasDisint;
	}
	public void setHasDisint(boolean hasDisint) {
		this.hasDisint = hasDisint;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ArrayList<DisposalValue> getOffenceDisposal() {
		return offenceDisposal;
	}

	public Date getVerdictDate(){
		return verdictDate;
	}
	
	public String getAppealType(){
		return appealType;
	}
	
	public String getRefAppCode(){
		return refAppCode;
	}
	
	public Date getDisqRemovedDate(){
		return disqRemovedDate;
	}
	
	public Date getDisqReimposedDate(){
		return disqReimposedDate;
	}
	
	public Timestamp getDisqSuspendedDate(){
		return disqSuspendedDate;
	}
	
	public void setOffenceDisposal(ArrayList<DisposalValue> offenceDisposal) {
		this.offenceDisposal = offenceDisposal;
	}

	public Integer getDefendantOnOffenceId() {
		return defendantOnOffenceId;
	}
	public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
		this.defendantOnOffenceId = defendantOnOffenceId;
	}
}
