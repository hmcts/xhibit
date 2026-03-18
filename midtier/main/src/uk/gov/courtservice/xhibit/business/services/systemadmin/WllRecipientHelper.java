package uk.gov.courtservice.xhibit.business.services.systemadmin;


import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.xhibit.business.entities.refprosecutoragency.RefProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipient;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;

public class WllRecipientHelper {

    protected final Logger log = Logger.getLogger(getClass());

	/**
	 * Updates the given xhb wll recipient value
	 * 
	 * @param XhbWllRecipientBasicValue 
	 *            bv
	 * @return XhbWllRecipientBasicValue
	 * @throws SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void updateWLLRecipient(Object cv, XhbWllRecipientBasicValue bv, String email, String fax, String username, XhbWllRecipient wllRecipient) throws IllegalArgumentException, OptimisticLockException {		
		if(! (cv instanceof RefProsecutorAgency) && !(cv instanceof RefSolicitorFirmComplexValue)) {
	    	log.error("Unexpected type  " + cv.getClass());
			throw new IllegalArgumentException("Unexpected type:" + cv.getClass());
	    }
		// copy across relevant info
		XhbWllRecipientBasicValue wllRecipEdit = new XhbWllRecipientBasicValue();
		if(cv instanceof RefProsecutorAgency) {
			bv = updateProsWll(bv, (RefProsecutorAgency)cv);
		} else {
			bv = updateSolWll(bv, (RefSolicitorFirmComplexValue)cv);
		}
	            
	    // If any fields actually need updating
	    if (stringEquals(bv.getSolicitorFirmName(), wllRecipient.getSolicitorFirmName()) && 
	    	stringEquals(bv.getSolictiorFirmAddress(), wllRecipient.getSolictiorFirmAddress()) && 
	        stringEquals(fax, wllRecipient.getSolicitorFirmFax()) && 
	        stringEquals(email, wllRecipient.getSolicitorFirmEmail())) {
	        return;
	    } else {
	    	if (!bv.getVersion().equals(wllRecipient.getVersion())) {
			    throw new OptimisticLockException("Optimistic Lock Error");
	    	} else {
	    		log.debug("update() updating");
	    		wllRecipient.setCrestSolicitorFirmId(bv.getCrestSolicitorFirmId());
	    		wllRecipient.setSolicitorFirmEmail(email);
	    		wllRecipient.setSolicitorFirmFax(fax);
	    		wllRecipient.setSolicitorFirmName(bv.getSolicitorFirmName());
	    		wllRecipient.setSolictiorFirmAddress(bv.getSolictiorFirmAddress());
	    		wllRecipient.setLastUpdatedBy(username);
	    	}
	    }
	}

	/**
	 * Update the prosecutor related info
	 * @param wllRecipEdit
	 * @param prosComplex
	 * @return XhbWllRecipientBasicValue
	 */
	private XhbWllRecipientBasicValue updateProsWll(XhbWllRecipientBasicValue wllRecipEdit, RefProsecutorAgency prosComplex) {
		StringBuffer solName = new StringBuffer();
		String [] names = {prosComplex.getTitle(), prosComplex.getProsecutorName1(), prosComplex.getProsecutorName2(), prosComplex.getProsecutorName3()};
		for(int i=0;i<names.length;i++) {
			if(i==0) {
				if(names[i]!=null && !names[i].equals("")){
					solName.append(names[i]);
				}
			} else {
				if(names[i]!=null && !names[i].equals("")){
					solName.append(" "+names[i]);
				}
			}
		}
		wllRecipEdit.setSolicitorFirmName(solName.toString());
		
		String[] addresses = { prosComplex.getAddress().getAddress1(), prosComplex.getAddress().getAddress2(), prosComplex.getAddress().getAddress3(),
				prosComplex.getAddress().getAddress4(), prosComplex.getAddress().getTown(), prosComplex.getAddress().getCounty(), 
				prosComplex.getAddress().getPostcode() };
		
		StringBuffer fullAddress = new StringBuffer();
		for (int i = 0; i < addresses.length; i++) { 
			if(i==0) {
				//don't have to check if its null as its a mandatory field
				fullAddress.append(addresses[i]);
			} else {
				if(addresses[i] != null && !addresses[i].equals("")) {
					fullAddress.append(", "+addresses[i]);
				}
			}
		}
		wllRecipEdit.setSolictiorFirmAddress(fullAddress.toString());
		return wllRecipEdit;
	}

	/**
	 * Update the solicitor related information
	 * @param wllRecipEdit XhbWllRecipientBasicValue
	 * @param cv RefSolicitorFirmComplexValue
	 * @return the wllRecipEdit
	 */
	private XhbWllRecipientBasicValue updateSolWll(XhbWllRecipientBasicValue wllRecipEdit, RefSolicitorFirmComplexValue cv) {
		
		wllRecipEdit.setSolicitorFirmName(cv.getSolicitorFirmName());
				
		String[] addresses = { cv.getAddress1(), cv.getAddress2(), cv.getAddress3(), cv.getAddress4(), cv.getTown(), cv.getCounty(), cv.getPostcode() };
		StringBuffer fullAddress = new StringBuffer();
		for (int i = 0; i < addresses.length; i++) { 
			if(i==0) {
				//don't have to check if its null as its a mandatory field
				fullAddress.append(addresses[i]);
			} else {
				if(addresses[i] != null && !addresses[i].equals("")) {
					fullAddress.append(", "+addresses[i]);
				}
			}
		}
		wllRecipEdit.setSolictiorFirmAddress(fullAddress.toString());
		return wllRecipEdit;
	}
	
	/**
	 * returns true if the two strings are equal, used to know whether any
	 * of the fields used in wll recipient table have been updated 
	 * @param str1 String
	 * @param str2 String
	 * @return true/false
	 */
	 private static boolean stringEquals(String str1, String str2) {
	   	if (isNullOrEmpty(str1) && isNullOrEmpty(str2)) {
	   		return true;
	   	}
	   	if (str1 != null && str2 != null && str1.equals(str2)) {
	   		return true;
	   	}
	   	
	   	return false;
	 }
	 
	 /**
	  * returns true if string is null or empty , used in 
	  * conjunction with the string equals method to determine 
	  * if any fields have changed
	  * @param str String
	  * @return true/false
	  */
	 private static boolean isNullOrEmpty(String str) {
	    	if (str != null && !str.isEmpty()) {
	    		return false;
	    	} 
	    	return true;
	    }
}
