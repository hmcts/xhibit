package uk.gov.courtservice.xhibit.business.entities.pdda;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefStatusCodesHome extends javax.ejb.EJBLocalHome {
    public RefStatusCodes create(Integer refStatusCodeId, String statusCodeType,
			String statusCode, String statusCodeDescription, String obsInd,
			String userDisplayName) throws CreateException;

    public RefStatusCodes findByPrimaryKey(Integer id) throws FinderException;
    public RefStatusCodes findByCode(String statusCode) throws FinderException;
}