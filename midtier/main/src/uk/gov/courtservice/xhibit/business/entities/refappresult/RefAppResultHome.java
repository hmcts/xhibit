package uk.gov.courtservice.xhibit.business.entities.refappresult;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefAppResultHome extends javax.ejb.EJBLocalHome {
    public RefAppResult create(String appResultCode, String appResultDescr1, String appResultDescr2, Integer hoCode,
            String lesserOffInd, String obsInd, Integer refAppResultId, String varySentence) throws CreateException;

    public RefAppResult findByPrimaryKey(Integer refAppResultId) throws FinderException;
}