package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

public class Notes implements Serializable {
	
	private static final long serialVersionUID = 1L;	

	               
 	private  String ln;
 	private  String predefinedln; 
	
	public String getLn() {
		return ln;
	}
	public void setLn(String ln) {
		this.ln = ln;
	}
	public String getPredefinedln() {
		return predefinedln;
	}
	public void setPredefinedln(String predefinedln) {
		this.predefinedln = predefinedln;
	}


}
