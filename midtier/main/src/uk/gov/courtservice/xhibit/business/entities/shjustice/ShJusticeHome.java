package uk.gov.courtservice.xhibit.business.entities.shjustice;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ShJusticeHome extends javax.ejb.EJBLocalHome {
    public ShJustice create(String justiceName, Integer hearingId, String userDisplayName) throws CreateException;

    public ShJustice findByPrimaryKey(Integer shJusticeId) throws FinderException;

	public Collection findByHearingId(Integer hearingId) throws FinderException;
}