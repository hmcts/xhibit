package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class RRECReportValue implements Serializable {
	
	private static final long serialVersionUID = 1227014888998573013L;
	
	Integer section_num;
	String case_type;
	Integer case_number;
	String case_subhdg;
	Integer case_id;
	
	public Integer getSection_num() {
		return section_num;
	}
	public void setSection_num(Integer section_num) {
		this.section_num = section_num;
	}
	public String getCase_type() {
		return case_type;
	}
	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}
	public Integer getCase_number() {
		return case_number;
	}
	public void setCase_number(Integer case_number) {
		this.case_number = case_number;
	}
	public String getCase_subhdg() {
		return case_subhdg;
	}
	public void setCase_subhdg(String case_subhdg) {
		this.case_subhdg = case_subhdg;
	}
	public Integer getCase_id() {
		return case_id;
	}
	public void setCase_id(Integer case_id) {
		this.case_id = case_id;
	}
}