package uk.gov.courtservice.xhibit.common.results.vos.common;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public abstract class ReportAbsttractValue extends CSAbstractValue implements IReportAction {

	private static final long serialVersionUID = 1L;
	
	private String timeOfReport;
	private String dateOfReport;
	private String courtName;
	private String userName;
	private String courtAddress;
	private String courtTelephone;
	private Map map;
	
	public final String getTimeOfReport() {
		return timeOfReport;
	}
	public final void setTimeOfReport(String timeOfReport) {
		this.timeOfReport = timeOfReport;
	}
	public final String getDateOfReport() {
		return dateOfReport;
	}
	public final void setDateOfReport(String dateOfReport) {
		this.dateOfReport = dateOfReport;
	}
	public final String getCourtName() {
		return courtName;
	}
	public final void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	public final String getUserName() {
		return userName;
	}
	
	public final void setUserName(String userName) {
		this.userName = userName;
	}
	
	public abstract String getShortReportCode();
	
	public String getCourtAddress() {
		return courtAddress;
	}
	public void setCourtAddress(String courtAddress) {
		this.courtAddress = courtAddress;
	}
	public String getCourtTelephone() {
		return courtTelephone;
	}
	public void setCourtTelephone(String courtTelephone) {
		this.courtTelephone = courtTelephone;
	}
	
	@SuppressWarnings("unchecked")
	public Map buildMap(String path, URL url) {
		map = new HashMap();
		String baseDir = url.toString();
        baseDir = baseDir.replace(path, "");
		map.put("basedir",baseDir.substring(0,baseDir.length()-1));
		map.put("mode", DISPLAY_MODE);
		
		return map;
	}
}
