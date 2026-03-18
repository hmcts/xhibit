package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

public enum StatsCodeRange {
	HJ ("HJ", 10000, 19999),
	DH ("DH", 20000, 29999),
	CJ ("CJ", 30000, 39999),
	R ("R", 40000, 49999),
	DJ ("DJ", 50000, 59999),
	AR ("AR", 60000, 69999);
	
	private final String judgeType;
	private final int rangeStart;
	private final int rangeEnd;
	
	StatsCodeRange(String judgeType, int rangeStart, int rangeEnd) {
		this.judgeType = judgeType;
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
	}
	
	/**
	 * @return the judgeType
	 */
	public String getJudgeType() {
		return judgeType;
	}

	/**
	 * @return the rangeStart
	 */
	public int getRangeStart() {
		return rangeStart;
	}

	/**
	 * @return the rangeEnd
	 */
	public int getRangeEnd() {
		return rangeEnd;
	}
}
