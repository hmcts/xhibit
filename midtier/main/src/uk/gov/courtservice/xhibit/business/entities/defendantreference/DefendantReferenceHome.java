package uk.gov.courtservice.xhibit.business.entities.defendantreference;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DefendantReferenceHome extends javax.ejb.EJBLocalHome {
	public DefendantReference create(String referenceValue, String referenceName, String category, Integer defendantId,
			CSEntityLocal defendant, String userDisplayName) throws CreateException;

	public DefendantReference findByPrimaryKey(Integer defRefId) throws FinderException;

	public DefendantReference findByKeyAndVersion(Integer defendantReferenceId, Integer version) throws FinderException;

	public Collection findByDefendantIdAndReferenceName(Integer defendantId, String refName) throws FinderException;

	public Collection findByRefNameValue(String referenceName, String referenceValue) throws FinderException;

}