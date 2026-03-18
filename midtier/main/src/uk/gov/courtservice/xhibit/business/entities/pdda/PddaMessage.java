package uk.gov.courtservice.xhibit.business.entities.pdda;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface PddaMessage extends CSEntityLocal {
    
		public java.lang.Integer getPddaMessageId();
		public void setPddaMessageId(java.lang.Integer pddaMessageId);
		public java.lang.Integer getCourtId();
		public void setCourtId(java.lang.Integer courtId);
		public java.lang.Integer getCourtRoomId();
		public void setCourtRoomId(java.lang.Integer courtRoomId);
		public java.lang.String getPddaMessageGuid();
		public void setPddaMessageGuid(java.lang.String pddaMessageGuid);
		public java.lang.Integer getPddaMessageTypeId();
		public void setPddaMessageTypeId(java.lang.Integer pddaMessageTypeId);
		public java.lang.Long getPddaMessageDataId();	
		public void setPddaMessageDataId(java.lang.Long pddaMessageDataId);
		public java.lang.Integer getPddaBatchId();
		public void setPddaBatchId(java.lang.Integer pddaBatchId);
		public java.util.Date getTimeSent();
		public void setTimeSent(java.util.Date timeSent);
		public java.lang.String getCpDocumentName();
		public void setCpDocumentName(java.lang.String cpDocumentName);
		public java.lang.String getCpDocumentStatus();
		public void setCpDocumentStatus(java.lang.String cpDocumentStatus);
		public java.lang.String getCpResponseGenerated();
		public void setCpResponseGenerated(java.lang.String cpResponseGenerated);
		public java.lang.Integer getCpStagingInboundId();
		public void setCpStagingInboundId(java.lang.Integer cpStagingInboundId);
		public java.lang.String getObsInd();
		public void setObsInd(java.lang.String obsInd);
		public java.lang.String getLastUpdatedBy();
		public void setLastUpdatedBy(java.lang.String lastUpdatedBy);
		public java.util.Date getLastUpdateDate();
		public void setLastUpdateDate(java.util.Date lastUpdateDate);
		public java.util.Date getCreationDate();
		public void setCreationDate(java.util.Date creationDate);
		public java.lang.String getCreatedBy();
		public void setCreatedBy(java.lang.String createdBy);
}