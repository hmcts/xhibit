package uk.gov.courtservice.xhibit.business.entities.d20offencelink;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;


public interface D20OffenceLinkHome extends javax.ejb.EJBLocalHome {
    public D20OffenceLink create (Integer defendantOnCaseId, Integer seqNo, String dvlaOffenceCode, Integer refOffenceId, Date convictionDate, String intD20, Date intD20Date, String finalD20, Date finalD20Date, String userDisplayName) throws CreateException;

    public D20OffenceLink findByPrimaryKey(Integer d20OffenceLinkId) throws FinderException;
    
    public Collection findByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException;
    
    public Collection findByDefOnCaseIdAndOffenceCode(Integer defendantOnCaseId, String dvlaOffenceCode) throws FinderException;
    
    public Collection findByDefOnCaseIdAndRefOffenceId(Integer defendantOnCaseId, Integer refOffenceId) throws FinderException;  
	
}