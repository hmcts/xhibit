package uk.gov.courtservice.xhibit.client.admin.referencedata;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;

public class Counsel extends RefAdvocateComplexValue {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	
	public Counsel() {
	}
	
	public Counsel (RefAdvocateBasicValue refAdvocateBasicValue) {
		setLegalRepId(refAdvocateBasicValue.getLegalRepId());
		setId(refAdvocateBasicValue.getId());
		setCrestChamberId(refAdvocateBasicValue.getCrestChamberId());
		setSurname(refAdvocateBasicValue.getSurname());
		setInitials(refAdvocateBasicValue.getInitials());
		setFirstName(refAdvocateBasicValue.getFirstName());
		setMiddleName(refAdvocateBasicValue.getMiddleName());
		setTitle(refAdvocateBasicValue.getTitle());
		setHonours(refAdvocateBasicValue.getHonours());
		setAdvTypeInd(refAdvocateBasicValue.getAdvTypeInd());
		setLegalRepType(refAdvocateBasicValue.getLegalRepType());
		setyearOfCall(refAdvocateBasicValue.getyearOfCall());
		setbarNo(refAdvocateBasicValue.getbarNo());
		setAvailable(refAdvocateBasicValue.getAvailable());
		setCourtId(refAdvocateBasicValue.getCourtId());
		setCrestAdvCategory(refAdvocateBasicValue.getCrestAdvCategory());
		setCrestAdvocateId(refAdvocateBasicValue.getCrestAdvocateId());
		setCrestPostNumber(refAdvocateBasicValue.getCrestPostNumber());
		setIsGlobal(refAdvocateBasicValue.getIsGlobal());
		setObsInd(refAdvocateBasicValue.getObsInd());
		setRefChamberId(refAdvocateBasicValue.getRefChamberId());
		setVatNo(refAdvocateBasicValue.getVatNo());
	}
	
	public Counsel (RefAdvocateComplexValue refAdvocateComplexValue) {
		setLegalRepId(refAdvocateComplexValue.getLegalRepId());
		setId(refAdvocateComplexValue.getId());
		setCrestChamberId(refAdvocateComplexValue.getCrestChamberId());
		setSurname(refAdvocateComplexValue.getSurname());
		setInitials(refAdvocateComplexValue.getInitials());
		setFirstName(refAdvocateComplexValue.getFirstName());
		setMiddleName(refAdvocateComplexValue.getMiddleName());
		setTitle(refAdvocateComplexValue.getTitle());
		setHonours(refAdvocateComplexValue.getHonours());
		setAdvTypeInd(refAdvocateComplexValue.getAdvTypeInd());
		setLegalRepType(refAdvocateComplexValue.getLegalRepType());
		setyearOfCall(refAdvocateComplexValue.getyearOfCall());
		setbarNo(refAdvocateComplexValue.getbarNo());
		setAvailable(refAdvocateComplexValue.getAvailable());
		setCourtId(refAdvocateComplexValue.getCourtId());
		setCrestAdvCategory(refAdvocateComplexValue.getCrestAdvCategory());
		setCrestAdvocateId(refAdvocateComplexValue.getCrestAdvocateId());
		setCrestPostNumber(refAdvocateComplexValue.getCrestPostNumber());
		setIsGlobal(refAdvocateComplexValue.getIsGlobal());
		setObsInd(refAdvocateComplexValue.getObsInd());
		setRefChamberId(refAdvocateComplexValue.getRefChamberId());
		setVatNo(refAdvocateComplexValue.getVatNo());
		setFirmName(refAdvocateComplexValue.getFirmName());
		setdxRef(refAdvocateComplexValue.getdxRef());
		setAddress1(refAdvocateComplexValue.getAddress1());
		setAddress2(refAdvocateComplexValue.getAddress2());
		setAddress3(refAdvocateComplexValue.getAddress3());
		setAddress4(refAdvocateComplexValue.getAddress4());
		setTown(refAdvocateComplexValue.getTown());
		setCounty(refAdvocateComplexValue.getCounty());
		setPostcode(refAdvocateComplexValue.getPostcode());
		setVersion(refAdvocateComplexValue.getVersion());
	}
	
	public String getFullAddress() {
		String fullAddress = null;
		String address1 = null;
		String address2 = null;

		if (getAddress1() != null) {
			address1 = getAddress1();
		} else {
			address1 = " ";
		}
		if (getAddress2() != null) {
			address2 = (", " + getAddress2());
		} else {
			address2 = " ";
		}
		fullAddress = (address1 + address2);

		return fullAddress;
	}
	
	public String getFullName() {
		String fullName = null;
		String firstName = null;
		String surname = null;
		
		if (getFirstName() != null) {
			firstName = getFirstName();
		} else {
			firstName = " ";
		}
		if (getSurname() != null) {
			surname = getSurname();
		} else {
			surname = " ";
		}
		fullName = (firstName + " " + surname);
		
		return fullName;
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
