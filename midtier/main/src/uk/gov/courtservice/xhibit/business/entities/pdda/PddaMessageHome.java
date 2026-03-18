package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface PddaMessageHome extends javax.ejb.EJBLocalHome {
        
    public PddaMessage create(Integer pddaMessageId, Integer courtId, Integer courtRoomId,
			String pddaMessageGuid, Integer pddaMessageTypeId, 
			Long pddaMessageDataId, Integer pddaBatchId, Date timeSent,
			String cpDocumentName, String cpDocumentStatus,
			String cpResponseGenerated, Integer cpStagingInboundId,
			String obsInd, String userDisplayName) throws CreateException;

    public PddaMessage findByPrimaryKey(Integer id) throws FinderException;
    public Collection<PddaMessage> findByBatchId(Integer pddaBatchId) throws FinderException;
}