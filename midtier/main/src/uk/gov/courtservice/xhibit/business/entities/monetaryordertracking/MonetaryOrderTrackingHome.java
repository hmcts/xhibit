package uk.gov.courtservice.xhibit.business.entities.monetaryordertracking;

import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.math.BigDecimal;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;

public interface MonetaryOrderTrackingHome extends javax.ejb.EJBLocalHome {
	public MonetaryOrderTracking create(Date acknowledgementDate, Case caze, Integer collectMagistratesCourtId,
			BigDecimal compensation, BigDecimal costs, String createdBy, Date creationDate, DefendantOnCase defOnCase,
			BigDecimal fined, String lastUpdatedBy, Date lastUpdateDate, Integer monetaryOrderTrackingId, String obsInd,
			Date orderDate, Integer version) throws CreateException;

    public MonetaryOrderTracking findByPrimaryKey(Integer monetaryOrderTrackingId) throws FinderException;
    
    public Collection findByCaseId(Integer caseId, Integer courtId) throws FinderException;

    public Collection findByDefendantOnCaseId(Integer defendantOnCaseId, Integer courtId) throws FinderException;
}
