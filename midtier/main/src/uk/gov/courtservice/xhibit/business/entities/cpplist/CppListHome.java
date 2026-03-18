package uk.gov.courtservice.xhibit.business.entities.cpplist;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CppListHome extends javax.ejb.EJBLocalHome {
    public CppList create (Integer cppListId, Integer courtCode, String listType, Date timeLoaded,
			Date listStartDate, Date listEndDate, Long listClobId, Long mergedClobId,
			String status, String errorMessage, String userDisplayName) throws CreateException;

    public CppList findByPrimaryKey(Integer cppListId) throws FinderException;
    public Collection findByCourtCodeAndListTypeAndListDate(final Integer courtCode, final String listType, 
    		final Date listStartDate) throws FinderException;
    
    public Collection findByCourtCodeAndListTypeAndListStartAndEndDate(final Integer courtCode, final String listType, 
    		final Date listStartDate, final Date listEndDate) throws FinderException;
}