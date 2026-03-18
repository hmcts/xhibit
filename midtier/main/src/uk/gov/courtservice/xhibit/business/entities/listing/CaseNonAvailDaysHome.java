package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface CaseNonAvailDaysHome extends EJBLocalHome {
    public CaseNonAvailDays create(Integer caseId, Date startDate,
			Date endDate, String reason, String obsInd, String userDisplayName) throws CreateException;

    public CaseNonAvailDays findByPrimaryKey(Integer pk) throws FinderException;

    public Collection findByCaseId(Integer caseId) throws FinderException;

    public Collection findByCaseIdAndDate(Integer caseId, Date date) throws FinderException;

    public Collection findByCaseIdAndDate(Integer caseId, Date fromDate, Date toDate) throws FinderException;
}