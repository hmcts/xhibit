package uk.gov.courtservice.xhibit.business.entities.cppformattingmerge;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.cppformatting.CppFormatting;

public interface CppFormattingMergeHome extends javax.ejb.EJBLocalHome {
    public CppFormattingMerge create (Integer formattingId, Long xhibitClobId, String language, Court court, CppFormatting cppFormatting,
			String userDisplayName) throws CreateException;

    public CppFormattingMerge findByPrimaryKey(Integer cppFormattingMergeId) throws FinderException;

}