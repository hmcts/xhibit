package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;
import java.util.List;

public class RJSJudgeType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String judgeType;
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
