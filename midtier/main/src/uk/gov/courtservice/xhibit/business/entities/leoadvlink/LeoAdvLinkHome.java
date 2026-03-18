package uk.gov.courtservice.xhibit.business.entities.leoadvlink;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;


public interface LeoAdvLinkHome extends EJBLocalHome {

    public LeoAdvLink create(
            Integer legalAidOrderId, 
            Integer defendantOnCaseId,
            Integer refAdvocateId,
            String crestAdvCategory,
            Integer crestPostNumber,
            String available,
            String newRowFlag,
            String obsInd, String userDisplayName) throws CreateException;

    public LeoAdvLink findByPrimaryKey(Integer leoAdvLinkId) throws FinderException;

    public LeoAdvLink findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber(
            Integer defendantOnCaseId,
            Integer refAdvocateId,
            Integer legalAidOrderId,
            Integer crestPostNumber) throws FinderException;
}

