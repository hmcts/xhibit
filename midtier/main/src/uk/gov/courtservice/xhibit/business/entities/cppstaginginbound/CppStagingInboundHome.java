package uk.gov.courtservice.xhibit.business.entities.cppstaginginbound;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;


public interface CppStagingInboundHome extends javax.ejb.EJBLocalHome {
    public CppStagingInbound create (String documentName, Integer courtCode, String documentType, Date timeLoaded, Long clobId,
			String validationStatus, String acknowledgmentStatus, String processingStatus, String validationErrorMessage, String userDisplayName) throws CreateException;

    public CppStagingInbound findByPrimaryKey(Integer cppStagingInboundId) throws FinderException;
    
    public Collection findNextDocumentByValidationStatus(Date creationDate, String validationStatus) throws FinderException;
    
    public Collection findNextDocumentByValidationAndProcessingStatus(Date creationDate, String validationStatus, String processingStatus) throws FinderException;
    
    public Collection findNextDocumentByProcessingStatus(Date creationDate, String processingStatus) throws FinderException;
	
}