package uk.gov.courtservice.xhibit.business.entities.defendanthistory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;
import java.util.Date;
/**
 * DefendantHistoryHome
 * @author waltersn
 *
 */
public interface DefendantHistoryHome extends javax.ejb.EJBLocalHome {
	public DefendantHistory create(Integer defendantHistoryId, Integer defendantId, Integer crestDefendantId,
        Integer courtId, String surname, String firstName, String middleName, Date dateOfBirth, Integer gender,
        String reasonDeleted, Date dateArchived) throws CreateException;

	public DefendantHistory findByPrimaryKey(Integer defendantHistoryId) throws FinderException;
	
	public Collection<DefendantHistory> findBySurname(String surname, Integer courtId) throws FinderException;

}
