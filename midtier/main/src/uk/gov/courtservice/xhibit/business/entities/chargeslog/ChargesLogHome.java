package uk.gov.courtservice.xhibit.business.entities.chargeslog;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ChargesLogHome extends javax.ejb.EJBLocalHome {
      public ChargesLog create(Integer caseId, Integer sequenceNo, String chargesInfo, String userDisplayName)
      throws javax.ejb.CreateException;
      
   public ChargesLog findByPrimaryKey(Integer pk)
      throws javax.ejb.FinderException;
   
}