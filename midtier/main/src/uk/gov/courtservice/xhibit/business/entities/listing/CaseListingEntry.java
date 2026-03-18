package uk.gov.courtservice.xhibit.business.entities.listing;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CaseListingEntry extends CSEntityLocal {
    
	   public java.lang.Integer getCaseListingEntryId(  ) ;
	   public void setCaseListingEntryId( java.lang.Integer caseListingEntryId ) ;
	   
	   public java.lang.Integer getCaseId(  ) ;
	   public void setCaseId(java.lang.Integer caseId);
	
	   public java.lang.Integer getRefJudgeTypeId(  ) ;
	   public void setRefJudgeTypeId(java.lang.Integer refJudgeTypeId);

	   public java.lang.Integer getCourtId(  ) ;
	   public void setCourtId(java.lang.Integer courtId);

	   public java.lang.Integer getCourtSiteId(  ) ;
	   public void setCourtSiteId(java.lang.Integer courtSiteId);

	   public java.lang.Integer getJudgeId(  ) ;
	   public void setJudgeId(java.lang.Integer judgeId);
	   
	   public java.lang.String getObsInd(  ) ;
	   public void setObsInd(java.lang.String obsInd);

}