package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class NTRSFReportValue implements Serializable {
	
	private static final long serialVersionUID = 1090233670583365361L;
	
	private String casenumber;
	private String defendantname;
	private String defaddress;
	private String defendantname2;
	private String prosecutorname;
	private String representative;
	private String prosecutoraddress;
	private String courttoaddress;
	private String courtfromaddress;
	private String incustody;
	private String courtfromtelephoneno;
	private String soladdress;
	private String courtto;
	private String courttobody;
	private String courtfrom;
	private String datetrans;
	private Integer rownumber;
	private String courtToDxRef;
	
	public String getDefendantname() {
		return defendantname;
	}
	public void setDefendantname(String defendantname) {
		this.defendantname = defendantname;
	}
	
	public String getProsecutorname() {
		return prosecutorname;
	}
	public void setProsecutorname(String prosecutorname) {
		this.prosecutorname = prosecutorname;
	}
	public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	public String getCourtto() {
		return courtto;
	}
	public void setCourtto(String courtto) {
		this.courtto = courtto;
	}
	public String getCourttobody() {
		return courttobody;
	}
	public void setCourttobody(String courttobody) {
		this.courttobody = courttobody;
	}
	public String getCourtToDxRef() {
		return courtToDxRef;
	}
	public void setCourtToDxRef(String courtToDxRef) {
		this.courtToDxRef = courtToDxRef;
	}
	public String getCourtfrom() {
		return courtfrom;
	}
	public void setCourtfrom(String courtfrom) {
		this.courtfrom = courtfrom;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDatetrans() {
		return datetrans;
	}
	public void setDatetrans(String datetrans) {
		this.datetrans = datetrans;
	}
	public String getCasenumber() {
		return casenumber;
	}
	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}
	public String getDefaddress() {
		return defaddress;
	}
	public void setDefaddress(String defaddress) {
		this.defaddress = defaddress;
	}
	public String getDefendantname2() {
		return defendantname2;
	}
	public void setDefendantname2(String defendantname2) {
		this.defendantname2 = defendantname2;
	}
	public String getProsecutoraddress() {
		return prosecutoraddress;
	}
	public void setProsecutoraddress(String prosecutoraddress) {
		this.prosecutoraddress = prosecutoraddress;
	}
	public String getCourttoaddress() {
		return courttoaddress;
	}
	public void setCourttoaddress(String courttoaddress) {
		this.courttoaddress = courttoaddress;
	}
	public String getSoladdress() {
		return soladdress;
	}
	public void setSoladdress(String soladdress) {
		this.soladdress = soladdress;
	}
	public Integer getRownumber() {
		return rownumber;
	}
	public void setRownumber(Integer rownumber) {
		this.rownumber = rownumber;
	}
	public String getCourtfromtelephoneno() {
		return courtfromtelephoneno;
	}
	public void setCourtfromtelephoneno(String courtfromtelephoneno) {
		this.courtfromtelephoneno = courtfromtelephoneno;
	}
	public String getCourtfromaddress() {
		return courtfromaddress;
	}
	public void setCourtfromaddress(String courtfromaddress) {
		this.courtfromaddress = courtfromaddress;
	}
	public String getIncustody() {
		return incustody;
	}
	public void setIncustody(String incustody) {
		this.incustody = incustody;
	}
}

