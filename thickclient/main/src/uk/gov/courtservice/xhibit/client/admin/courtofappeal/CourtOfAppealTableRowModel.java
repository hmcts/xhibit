package uk.gov.courtservice.xhibit.client.admin.courtofappeal;

import java.sql.Timestamp;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

public class CourtOfAppealTableRowModel {
 
	private String defendantName;
	
	private Integer defOnCaseID;
 
	private Timestamp dateOfReceipt;
    
    private Timestamp datePapersSent;
    
	private Timestamp dateOfResult;
 	
	private Boolean selected;
	
	private Boolean reselected;
	
	private String status;
	 
	private String appealResult;
     
	
    // get methods
     

    public Integer getDefOnCaseID() {
		return defOnCaseID;
	}


    public String getStatus() {
		return status;
	}

	public String getDefendantName() {
        return defendantName;
    }

    public Timestamp getDateOfReceipt() {
        return dateOfReceipt;
    }
  
    public Boolean isSelected() {
        return selected;
    }
    
    public Boolean isReSelected() {
        return reselected;
    }
  
    public Boolean getSelected() {
		return selected;
	}
 
	public Boolean getReselected() {
		return reselected;
	}
 	 
    public Timestamp getDatePapersSent() {
		return datePapersSent;
	}
    
	public Timestamp getDateOfResult() {
		return dateOfResult;
	}
	
	
    // set methods
    

	public void setDefOnCaseID(Integer defOnCaseId) {
		this.defOnCaseID = defOnCaseId;
	}
	
    public void setDefendantName(String param) {
        defendantName = param;
    }
 
	public void setStatus(String param) {
		status = param;
	}
 
    public void setDateOfReceipt(Timestamp param) {
        dateOfReceipt = param;
    }
    

    public void setSelected(Boolean param) {
        selected = param;
    }
    
    public void setReSelected(Boolean param) {
        reselected = param;
    }
 

	public void setDatePapersSent(Timestamp datePapersSent) {
		this.datePapersSent = datePapersSent;
	}

	public void setDateOfResult(Timestamp dateOfResult) {
		this.dateOfResult = dateOfResult;
	}
 	
	public void setReselected(Boolean reselected) {
		this.reselected = reselected;
	}

	public String getAppealResult() {
		return appealResult;
	}

	public void setAppealResult(String appealResult) {
		this.appealResult = appealResult;
	}
 

     
    // utility
    public void printModel() {
        XHIBITConstant.info("CourtOfAppealTableRowModel");
        XHIBITConstant.info("-----------------------------");
        XHIBITConstant.info("defendantName       : " + getDefendantName());
        XHIBITConstant.info("dateOfReceipt       : " + getDateOfReceipt());
        XHIBITConstant.info("datePapersSent		 : " + getDatePapersSent());  
        XHIBITConstant.info("selected            : " + (isSelected() == null ? false : isSelected().booleanValue()));
        XHIBITConstant.info("appealResult		 : " + getAppealResult());  
        XHIBITConstant.info("status				 : " + getStatus());  
     }

}

