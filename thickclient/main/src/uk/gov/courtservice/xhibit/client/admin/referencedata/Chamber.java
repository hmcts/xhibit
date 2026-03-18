package uk.gov.courtservice.xhibit.client.admin.referencedata;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;

public class Chamber extends RefChamberComplexValue {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	
	public Chamber() {
	}
	
	public Chamber(RefChamberBasicValue value) {
		setId(value.getId());
		setDxRef(value.getDxRef());
		setLocationCode(value.getLocationCode());
		setCrestChamberId(value.getCrestChamberId());
		setFirmName(value.getFirmName());
		setAddressId(value.getAddressId());
		setCourtId(value.getCourtId());
		setClerkName(value.getClerkName());
	}
	
	public Chamber(RefChamberComplexValue value) {
		setId(value.getId());
		setDxRef(value.getDxRef());
		setLocationCode(value.getLocationCode());
		setCrestChamberId(value.getCrestChamberId());
		setFirmName(value.getFirmName());
		setAddressId(value.getAddressId());
		setCourtId(value.getCourtId());
		setClerkName(value.getClerkName());
		setAddress(value.getAddress());
		setObsInd(value.getObsInd());
	}
	
	public String getFullAddress() {
		String fullAddress = null;
		String address1 = null;
		String address2 = null;

		if (getAddress() != null) {

			if (getAddress().getAddress1() != null) {
				address1 = getAddress().getAddress1();
			} else {
				address1 = " ";
			}
			if (getAddress().getAddress2() != null) {
				address2 = (", " + getAddress().getAddress2());
			} else {
				address2 = " ";
			}
			fullAddress = (address1 + address2);

			return fullAddress;
		} else {
			return " ";
		}
	}
	
	public Boolean IsDeleted() {
		String obsInd = null;
		Boolean deleted = false;

		if (getObsInd() != null) {
			obsInd = getObsInd();
			if (obsInd.equals("Y")) {
				deleted = true;
			} else {
				deleted = false;
			}
		} else {
			deleted = false;
		}

		return deleted;
	}
}
