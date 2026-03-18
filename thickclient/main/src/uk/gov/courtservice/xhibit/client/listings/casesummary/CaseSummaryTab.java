package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.ejb.FinderException;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSolicitorFirmCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public abstract class CaseSummaryTab extends JPanel {

	private static final long serialVersionUID = 1L;
	protected static final String YES = "Y";
	protected boolean dataLoaded;

	public CaseSummaryTab() {
		super();
		dataLoaded = false;
	}

	protected abstract void moveModelToScreen();
	
	protected void loadDataFirstTime() {
		if ( !dataLoaded ) {
			moveModelToScreen();
			dataLoaded = true;
		}
	}
	
	protected void setTableColumnWidths(JTable table, Integer[] customColumnWidths) {
		if (customColumnWidths != null) {
			table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			for (int columnNo = 0; columnNo < table.getModel().getColumnCount(); columnNo++) {
				if (customColumnWidths[columnNo] != null) {
					table.getColumnModel().getColumn(columnNo).setPreferredWidth(customColumnWidths[columnNo]);		
				}
			}
		}
	}

	protected String getResourceBundle(String key) {
		return XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,key);
	}
	
	protected String getResourceBundle(String resourceKey, Object[] objects) {
		return MessageFormat.format(getResourceBundle(resourceKey), objects);
	}
	
	/**
	 * Get the contact details
	 */
	private RefSolicitorFirmComplexValue getContactDetails(Integer addressId) {
		RefSolicitorFirmComplexValue contactDetails = null;
		if (addressId != null) {
			try {
				contactDetails = XhibitDelegateHelper.getContactDetailDelegate().findByAddressId(addressId);
				return contactDetails;
			} catch (FinderException ex) {
				contactDetails = null;
			}
		}
		return contactDetails;
	}
	
	protected class ProsecutorAgency {
		private String cpsCode;
		private String prosecutorName;
		private String prosecutorFullName;
		
		public ProsecutorAgency(Integer prosecutorAgencyId) throws SysRefControllerException {
			if (prosecutorAgencyId != null) {
				RefProsecutorAgencyComplexValue prosecutor = XhibitDelegateHelper.getBizRefDelegate().findByRefProsecutorAgencyId(prosecutorAgencyId);
				if(prosecutor != null)	{
					this.cpsCode = prosecutor.getCpsCode();
					this.prosecutorName = StringUtils.join(new String[] {prosecutor.getInitials(), prosecutor.getProsecutorName1(),
							 								prosecutor.getProsecutorName2(), prosecutor.getProsecutorName3(), prosecutor.getTelephoneNumber()}, ' ');
					this.prosecutorFullName = prosecutor.getFullName();			
				}
			}
		}

		public String getCpsCode() {
			return cpsCode;
		}

		public String getProsecutorName() {
			return prosecutorName;
		}
		
		public String getProsecutorFullName() {
			return prosecutorFullName;
		}
	}

	/**
	 * Get the prosecution solicitor details
	 */
	protected class ProsecutorSolicitorFirm {
		
		private SolicitorFirm solicitorFirm;

		public ProsecutorSolicitorFirm(Integer prosecutorAgencyId) throws FinderException, BisRefControllerException {
			if (prosecutorAgencyId != null) {
				ArrayList<ProsecutorRefSolFirmValue> prosRefSolFirmValue = (ArrayList<ProsecutorRefSolFirmValue>)XhibitDelegateHelper.getProsRefSolFirmDelegate().findPrivateRepByCaseProsAgency(prosecutorAgencyId);
				if (prosRefSolFirmValue.size()>0) {
					// Ensure that the latest representative is still valid
					if ( prosRefSolFirmValue.get(0).getRepEndDate() == null ) {
						this.solicitorFirm = new SolicitorFirm(prosRefSolFirmValue.get(0).getRefSolicitorFirmId());
					}
				}
			}
		}
			
		public String getSolicitorText() {
			return solicitorFirm != null ? solicitorFirm.getSolicitorText() : null;
		}
	}
	
	/**
	 * Get the defendant solicitor details
	 */
	protected class DefendantSolicitorFirm {
	
		private SolicitorFirm solicitorFirm;
		
		public DefendantSolicitorFirm(Integer defendantOnCaseId) throws FinderException, BisRefControllerException {
			if (defendantOnCaseId != null) {
				ArrayList<DefOnCaseRefSolFirmValue> defOnCaseRefSolFirm = (ArrayList<DefOnCaseRefSolFirmValue>)XhibitDelegateHelper.getDefOnCaseRefSolFirmDelegate().findPrivateRepByDefendantOnCaseId(
						defendantOnCaseId);
				if (defOnCaseRefSolFirm.size()>0) {
					// Ensure that the latest representative is still valid
					if ( defOnCaseRefSolFirm.get(0).getRepEndDate() == null ) {
						this.solicitorFirm = new SolicitorFirm(defOnCaseRefSolFirm.get(0).getRefSolicitorFirmId());
					}
				}	
			}
		}
		
		public String getSolicitorText() {
			return solicitorFirm != null ? solicitorFirm.getSolicitorText() : null;
		}
	}
	
	/**
	 * Get the solicitor details
	 */
	protected class SolicitorFirm {
		
		private String solicitorName;
		private String phoneNumber;
		
		@SuppressWarnings("unchecked")
		public SolicitorFirm(Integer refSolicitorFirmId) throws BisRefControllerException {
			if (refSolicitorFirmId != null) {
				RefSolicitorFirmCriteria solicitorFirmCriteria = new RefSolicitorFirmCriteria();
				solicitorFirmCriteria.setPrimaryKey(refSolicitorFirmId);
				ArrayList<RefSolicitorFirmBasicValue> solicitorFirmList = 
				(ArrayList<RefSolicitorFirmBasicValue>) XhibitDelegateHelper.getBizRefDelegate().findSolicitorFirms(solicitorFirmCriteria );
				
				if(solicitorFirmList.size() == 1 && solicitorFirmList.get(0) != null){
					RefSolicitorFirmBasicValue solicitorFirm = solicitorFirmList.get(0);
					this.solicitorName = solicitorFirm.getSolicitorFirmName();
					// Get the contact details for the solicitor
					RefSolicitorFirmComplexValue contactDetails = getContactDetails(solicitorFirm.getAddressId());
					if (contactDetails != null) {
						this.phoneNumber = contactDetails.getTelephoneNumber();
					}
				}
			}
		}
		
		public String getSolicitorText() {
			return StringUtils.join(new String[] {solicitorName, phoneNumber}, ' ');
		}
	}

	/**
	 * Get the put back info
	 */
	protected class PutBack {
		
		private String putBackText;
		private String toText;
		
		@SuppressWarnings("unchecked")
		public PutBack(Integer defendantOnCaseId) throws BisRefControllerException {
			if (defendantOnCaseId != null) {
				XhbDefHearingRecordBasicValue[] defHearingRecords = XhibitDelegateHelper.getHearingDelegate().getDefHearingRecordByDefOnCaseId(defendantOnCaseId);
				
				if(defHearingRecords != null && defHearingRecords.length > 0)		
				{
					Arrays.sort(defHearingRecords, new XhbDefHearingRecordHearingEndDateComparator());
					XhbDefHearingRecordBasicValue mostRecentDefHearingRecord = defHearingRecords[0];
					if(YES.equals(mostRecentDefHearingRecord.getIsAdjourned()))
					{
						if (mostRecentDefHearingRecord.getRefAdjournmentId() != null) {
							RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
				            criteria.setPrimaryKey(mostRecentDefHearingRecord.getRefAdjournmentId());
							ArrayList<RefSystemCodeBasicValue> putBackRecords = (ArrayList<RefSystemCodeBasicValue>) XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria);
							if(putBackRecords != null && putBackRecords.size() > 0)
							{
								this.putBackText = putBackRecords.get(0).getDecode();
							}
						}
						this.toText = XDateFormat.format(mostRecentDefHearingRecord.getAdjournedDate(), XDateFormat.DATEFORMAT);
					}
				}
			}
		}

		public String getPutBackText() {
			return putBackText;
		}

		public String getToText() {
			return toText;
		}
	}
	
	private class XhbDefHearingRecordHearingEndDateComparator implements Comparator<XhbDefHearingRecordBasicValue>
	{
		// Code taken from full version of org.apache.commons.lang.ObjectUtils (our copy is a cut down version)
		private <T extends Comparable<? super T>> int compare(final T o1, final T o2, final boolean nullGreater) {
			if (o1 == o2) {
				return 0;
			} else if (o1 == null) {
				return nullGreater ? 1 : -1;
			} else if (o2 == null) {
				return nullGreater ? -1 : 1;
			}
			return o1.compareTo(o2);
		}
		
		@Override
		public int compare(XhbDefHearingRecordBasicValue o1, XhbDefHearingRecordBasicValue o2) {
			return compare(o2.getHearingEndDate(),o1.getHearingEndDate(), false);
		}	
	}
}

