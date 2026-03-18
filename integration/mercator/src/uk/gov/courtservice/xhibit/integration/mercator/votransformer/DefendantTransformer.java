package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddressMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefendantMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefOnCaseMVO;

/**
 * <p>
 * Title: DefendantTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (DefendantValue) and
 * convert it to DefendantMVO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 * @author GJS - updated for generated MVOs
 * @version 1.1
 */

public class DefendantTransformer implements VOTransformer {
    public DefendantTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }
        // check the instance of the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type DefendantValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        DefendantMVO defendantMVO = new DefendantMVO();
        DefendantValue defendantValue = (DefendantValue) valueObject;

        AddressMVO addressMVO = null;
        AddressTransformer addressTransformer = null;

        DefOnCaseMVO defOnCaseMVO = null;
        DefendantOnCaseTransformer defendantOnCaseTransformer = null;
        DefOnCaseMVO[] defOnCaseMVOArray = new DefOnCaseMVO[1];
        
        String dob = null;
        String lastConvDate = null;

        // get and transform the AddressValue to AddressMVO
        if (defendantValue.getAddressValue() != null) {
            addressTransformer = new AddressTransformer();
            addressMVO = (AddressMVO) addressTransformer.transformVO(defendantValue.getAddressValue());
        }

        // check if the dates are null, if not set the Strings
        if (defendantValue.getDateOfBirth() != null) {
            dob = defendantValue.getDateOfBirth().getTime().toString();
        }
        if (defendantValue.getLastConvictionDate() != null) {
            lastConvDate = defendantValue.getLastConvictionDate().getTime().toString();
        }

        // Set defendantMVO values

        if (defendantValue.getDefendantID() != null) {
            defendantMVO.setDefendantID(defendantValue.getDefendantID().intValue());
        }
        if (defendantValue.getCrestDefendantID() != null) {
            defendantMVO.setCrestDefendantID(defendantValue.getCrestDefendantID().intValue());
        }
        defendantMVO.setFirstName(defendantValue.getFirstName());
        defendantMVO.setMiddleName(defendantValue.getMiddleName());
        defendantMVO.setSurName(defendantValue.getSurName());
        defendantMVO.setInitials(defendantValue.getInitials());
        defendantMVO.setDateOfBirth(dob);
        if (defendantValue.getGender() != null) {
            defendantMVO.setGender(defendantValue.getGender().intValue());
        }
        defendantMVO.setLastConvictionDate(lastConvDate);
        if (defendantValue.getCourtID() != null) {
            defendantMVO.setCourtID(defendantValue.getCourtID().intValue());
        }
        defendantMVO.setAddressMVO(addressMVO);
        defendantMVO.setDirty(defendantValue.isDirty());
        if (defendantValue.getVersion() != null) {
            defendantMVO.setVersion(defendantValue.getVersion().intValue());
        }
        if (defendantValue.getId() != null) {
            defendantMVO.setId(defendantValue.getId().intValue());
        }
        
        //Only going to get one DOC as defendantValue only has one
        //get and transform the DOC to DefOnCaseMVO        
        if(defendantValue.getDefOnCaseBasicValue()!=null)
        {
            defendantOnCaseTransformer = new DefendantOnCaseTransformer();
            //Most of the DOC attributes are in DefOnCaseBasicValue, except the Court ID
            DefendantOnCaseValue defendantOnCaseValue = new DefendantOnCaseValue();
            defendantOnCaseValue.setCourtId(defendantValue.getCourtID());            
            defendantOnCaseValue.setDefendantOnCaseBVO(defendantValue.getDefOnCaseBasicValue());
            
            
            defOnCaseMVO = (DefOnCaseMVO) defendantOnCaseTransformer.transformVO(defendantOnCaseValue);            
            defOnCaseMVOArray[0] = defOnCaseMVO;            
            defendantMVO.setDefOnCaseMVO(defOnCaseMVOArray);
        }
        return defendantMVO;
    }

    public Object transformOutput(Object result) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer
         *       method
         */
        return result;
    }
}