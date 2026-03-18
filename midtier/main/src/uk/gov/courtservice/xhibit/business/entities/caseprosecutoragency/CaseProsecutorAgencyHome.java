package uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CaseProsecutorAgencyHome extends javax.ejb.EJBLocalHome {
	
	public CaseProsecutorAgency create(String prosecutorType, Integer caseId, Integer refProsecutorAgencyId, String respondentStatus, String userDisplayName) throws CreateException ;
	
    public CaseProsecutorAgency findByPrimaryKey(Integer id) throws FinderException;

    public Collection findByCaseId(Integer caseId) throws FinderException;
    
    public Collection findByRefProsecutorAgencyId(Integer refProsecutorAgencyId) throws FinderException;
    
    public Collection findByRefProsecutorAgencyIdAndCaseId(Integer refProsecutorAgencyId, Integer caseId) throws FinderException;

   }