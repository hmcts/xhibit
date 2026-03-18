package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CaseDescription  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String code;
	private String description;
	private String calcmonth;
	private List <CaseNumber>casenumbers;
	private Integer total;
	private String trialCodeType;
	 
	/**
	 * @return the trialCodeType
	 */
	public String getTrialCodeType() {
		return trialCodeType;
	}

	/**
	 * @param trialCodeType the trialCodeType to set
	 */
	public void setTrialCodeType(String trialCodeType) {
		this.trialCodeType = trialCodeType;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<CaseNumber> getCasenumbers() {
		return casenumbers;
	}
	
	public void setCasenumbers(List<CaseNumber> casenumbers) {
		if (casenumbers != null && !casenumbers.isEmpty() &&
				!(casenumbers.size() == 1 && casenumbers.get(0).getCaseNumber() == null)) {
			this.casenumbers = casenumbers;
	 	} else {
	 		this.casenumbers = new ArrayList<CaseNumber>();
	 	}
		
	}
	 
   public void setTotal(Integer total) {
	   this.total = total;
   }
public Integer getTotal() {
	return total;
}

public String getCalcmonth() {
	return calcmonth;
}

public void setCalcmonth(String calcmonth) {
	this.calcmonth = calcmonth;
}
	
}
