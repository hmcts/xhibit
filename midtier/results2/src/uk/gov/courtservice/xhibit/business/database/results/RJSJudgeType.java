package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.List;

public class RJSJudgeType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String judgeType;
	private String deCode;
	private Integer totalSatInChambers;
	private Integer totalSatInCourt;
	private Integer totalAllSittings;

	private List<RJSJudges> judges;
	
	public final String getJudgeType() {
		return judgeType;
	}
	public final void setJudgeType(String judgeType) {
		this.judgeType = judgeType;
	}
	
	public final void setDeCode(String deCode) {
		this.deCode = deCode;
	}
	
	public final String getDeCode() {
		return deCode;
	}
	
	public final Integer getTotalSatInChambers() {
		return totalSatInChambers;
	}
	public final void setTotalSatInChambers(Integer totalSatInChambers) {
		this.totalSatInChambers = totalSatInChambers;
	}
	public final Integer getTotalSatInCourt() {
		return totalSatInCourt;
	}
	public final void setTotalSatInCourt(Integer totalSatInCourt) {
		this.totalSatInCourt = totalSatInCourt;
	}
	
	public Integer getTotalAllSittings() {
		return totalAllSittings;
	}
	public void setTotalAllSittings(Integer totalAllSittings) {
		this.totalAllSittings = totalAllSittings;
	}
	
	public List<RJSJudges> getJudges() {
		return judges;
	}
	public void setJudges(List<RJSJudges> judges) {
		this.judges = judges;
	}

}
