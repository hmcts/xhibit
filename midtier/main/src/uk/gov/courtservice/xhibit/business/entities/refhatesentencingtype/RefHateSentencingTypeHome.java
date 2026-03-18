package uk.gov.courtservice.xhibit.business.entities.refhatesentencingtype;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;


public interface RefHateSentencingTypeHome extends javax.ejb.EJBLocalHome
{
			   public RefHateSentencingType create(String hateSentType, String title, String description, String cjsQualifier, String obsInd)
			      throws javax.ejb.CreateException;


			   public RefHateSentencingType findByPrimaryKey(java.lang.Integer pk)
			      throws javax.ejb.FinderException;
			   
			    public Collection findByCourtId(java.lang.Integer courtId) throws FinderException;



}
