package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class PddaMessageBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer pddaMessageId, Integer courtId, Integer courtRoomId,
			String pddaMessageGuid, Integer pddaMessageTypeId, 
			Long pddaMessageDataId, Integer pddaBatchId, Date timeSent,
			String cpDocumentName, String cpDocumentStatus,
			String cpResponseGenerated, Integer cpStagingInboundId,
			String obsInd, String userDisplayName) throws CreateException {
		setPddaMessageId(pddaMessageId);
		setCourtId(courtId);
		setCourtRoomId(courtRoomId);
		setPddaMessageGuid(pddaMessageGuid);
		setPddaMessageTypeId(pddaMessageTypeId);
		setPddaMessageDataId(pddaMessageDataId);
		setPddaBatchId(pddaBatchId);
		setTimeSent(timeSent);
		setCpDocumentName(cpDocumentName);
		setCpDocumentStatus(cpDocumentStatus);
		setCpResponseGenerated(cpResponseGenerated);
		setCpStagingInboundId(cpStagingInboundId);
		setObsInd(obsInd);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer pddaMessageId, Integer courtId, Integer courtRoomId,
			String pddaMessageGuid, Integer pddaMessageTypeId, 
			Long pddaMessageDataId, Integer pddaBatchId, Date timeSent,
			String cpDocumentName, String cpDocumentStatus,
			String cpResponseGenerated, Integer cpStagingInboundId,
			String obsInd, String userDisplayName) throws CreateException {
	}

	public abstract Integer getPddaMessageId();
	public abstract void setPddaMessageId(Integer pddaMessageId);
	public abstract Integer getCourtId();
	public abstract void setCourtId(Integer courtId);
	public abstract Integer getCourtRoomId();
	public abstract void setCourtRoomId(Integer courtRoomId);
	public abstract String getPddaMessageGuid();
	public abstract void setPddaMessageGuid(String pddaMessageGuid);
	public abstract Integer getPddaMessageTypeId();
	public abstract void setPddaMessageTypeId(Integer pddaMessageTypeId);
	public abstract Long getPddaMessageDataId();	
	public abstract void setPddaMessageDataId(Long pddaMessageDataId);
	public abstract Integer getPddaBatchId();
	public abstract void setPddaBatchId(Integer pddaBatchId);
	public abstract Date getTimeSent();
	public abstract void setTimeSent(Date timeSent);
	public abstract java.lang.String getCpDocumentName();
	public abstract void setCpDocumentName(String cpDcoumentName);
	public abstract java.lang.String getCpDocumentStatus();
	public abstract void setCpDocumentStatus(String cpDcoumentStatus);
	public abstract java.lang.String getCpResponseGenerated();
	public abstract void setCpResponseGenerated(String cpResponseGenerated);
	public abstract java.lang.Integer getCpStagingInboundId();
	public abstract void setCpStagingInboundId(Integer cpStagingInboundId);
	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy) ;
}