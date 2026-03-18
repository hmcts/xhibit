package uk.gov.courtservice.xhibit.business.entities.refjustice;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RefJusticeHome extends EJBLocalHome {

    public RefJustice create(String justiceName, Integer crestJusticeId, Integer courtId, String obsInd,
			String initials, String psdCourtCode, String title, String userDisplayName) throws CreateException;

    public RefJustice findByPrimaryKey(Integer refJusticeId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}