package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface ListHome extends EJBLocalHome {
    public List create(Integer listTypeId, Integer listParentId, Integer courtId,
			String draftOrFinal, Integer listNumber, Date listStartDate, Date listEndDate, Date publishDate,
			String publishStatus, String publishErrorReason, String obsInd, String userDisplayName) 
					throws CreateException;

    public List findByPrimaryKey(Integer listId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}