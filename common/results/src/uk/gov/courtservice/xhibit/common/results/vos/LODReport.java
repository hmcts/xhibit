package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class LODReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String dateOfRequest;
	private String diaryDate;
	
	private ArrayList<LODReportValue> lodReportValues;
	private ArrayList lodDiaryInfo;

	public LODReport(){
		setLodReportValues(new ArrayList<LODReportValue>());
    }

	/**
	 * @return the lodReportValues
	 */
	public ArrayList<LODReportValue> getLodReportValues() {
		return lodReportValues;
	}

	/**
	 * @param lodReportValues the lodReportValues to set
	 */
	public void setLodReportValues(ArrayList<LODReportValue> lodReportValues) {
		this.lodReportValues = lodReportValues;
	}

	/**
	 * @return the dateOfRequest
	 */
	public String getDateOfRequest() {
		return dateOfRequest;
	}

	/**
	 * @param dateOfRequest the dateOfRequest to set
	 */
	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	/**
	 * @return the diaryDate
	 */
	public String getDiaryDate() {
		return diaryDate;
	}

	/**
	 * @param diaryDate the diaryDate to set
	 */
	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
	}

	public ArrayList getLodDiaryInfo() {
		return lodDiaryInfo;
	}

	public void setLodDiaryInfo(ArrayList lodDiaryInfo) {
		this.lodDiaryInfo = lodDiaryInfo;
	}

	@Override
	public String getShortReportCode() {
		
		return "LODR";
	}
}
