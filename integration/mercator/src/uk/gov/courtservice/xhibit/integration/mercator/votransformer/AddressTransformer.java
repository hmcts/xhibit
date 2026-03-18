package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddressMVO;

/**
 * <p>
 * Title: AddressTransformer
 * </p>
 * <p>
 * Description:This class is taking in a CSValueObject (AddressValue) and
 * convert it to AddressMVO
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

public class AddressTransformer implements VOTransformer {
    public AddressTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instance of the CSValueObject before trying to cast it.
        if (!(valueObject instanceof AddressValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type AddressValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        AddressMVO addressMVO = new AddressMVO();
        AddressValue addressValue = (AddressValue) valueObject;

        // Set addressMVO values

        if (addressValue.getAddressID() != null) {
            addressMVO.setAddressID(addressValue.getAddressID().intValue());
        }
        addressMVO.setAddress1(addressValue.getAddress1());
        addressMVO.setAddress2(addressValue.getAddress2());
        addressMVO.setAddress3(addressValue.getAddress3());
        addressMVO.setAddress4(addressValue.getAddress4());

        addressMVO.setTown(addressValue.getTown());
        addressMVO.setCounty(addressValue.getCounty());
        addressMVO.setPostcode(addressValue.getPostcode());
        addressMVO.setCountry(addressValue.getCountry());
        addressMVO.setDirty(addressValue.isDirty());

        if (addressValue.getVersion() != null) {
            addressMVO.setVersion(addressValue.getVersion().intValue());
        }

        if (addressValue.getId() != null) {
            addressMVO.setId(addressValue.getId().intValue());
        }

        return addressMVO;
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