package uk.gov.courtservice.xhibit.business.entities.cppformatting;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.court.Court;

public interface CppFormattingHome extends javax.ejb.EJBLocalHome {
    public CppFormatting create (Integer stagingTableId, Date dateIn,
			String formatStatus, String documentType, Court court, Long xmlDocumentClobId, String userDisplayName) throws CreateException;

    public CppFormatting findByPrimaryKey(Integer cppFormattingId) throws FinderException;
    
    public Collection findByCourtAndDocType(Integer courtId, String documentType, Date creationDate) throws FinderException;
	
	public CppFormatting findLatestByCourtDateInDoc(Integer courtId, Date dateIn, String documentType) throws FinderException;
	
	public Collection findAllNewByDocType(String documentType, Date creationDate) throws FinderException;

}