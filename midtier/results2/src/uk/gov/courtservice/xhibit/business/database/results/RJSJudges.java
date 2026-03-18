package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

public class RJSJudges implements Serializable {
	
	private static final long serialVersionUID = 1L;	

	private String judgeName;
	private String deCode;

	private Integer satInChambers;
	private Integer satInCourt;
	private Integer total;
	
	public final String getJudgeName() {
		return judgeName;
	}
	
	public final void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}	

	public final void setDeCode(String deCode) {
		this.deCode = deCode;
	}
	
	public final String getDeCode() {
		return deCode;
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
