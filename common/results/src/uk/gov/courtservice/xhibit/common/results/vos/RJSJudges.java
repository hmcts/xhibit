package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class RJSJudges implements Serializable {
	
	private static final long serialVersionUID = 1L;	

	private String judgeName;

	private Integer satInChambers;
	private Integer satInCourt;
	private Integer total;
	
	public final String getJudgeName() {
		return judgeName;
	}
	
	public final void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}	
	
	public Integer getSatInChambers() {
		return satInChambers;
	}
	public void setSatInChambers(Integer satInChambers) {
		this.satInChambers = satInChambers;
	}
	public Integer getSatInCourt() {
		return satInCourt;
	}
	public void setSatInCourt(Integer satInCourt) {
		this.satInCourt = satInCourt;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
