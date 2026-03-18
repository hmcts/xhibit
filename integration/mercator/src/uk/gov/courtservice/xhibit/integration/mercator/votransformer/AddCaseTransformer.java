package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddCaseMVO;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Cag Onganer
 * @version 1.0
 * @author GJS - updated for generated MVOs
 * @version 1.1
 */

public class AddCaseTransformer implements VOTransformer {
    public AddCaseTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instance of the CSValueObject before trying to cast it.
        if (!(valueObject instanceof AddCaseValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type AddCaseValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        AddCaseMVO addCaseMVO = new AddCaseMVO();
        AddCaseValue addCase = (AddCaseValue) valueObject;

        // Set addCaseMVO values

        if (addCase.getCaseNumber() != null) {
            addCaseMVO.setCaseNumber(addCase.getCaseNumber().intValue());
        }
        addCaseMVO.setCaseType(addCase.getCaseType());
        if (addCase.getCourtID() != null) {
            addCaseMVO.setCourtID(addCase.getCourtID().intValue());
        }
        addCaseMVO.setDirty(addCase.isDirty());
        addCaseMVO.setCaseTitle(addCase.getCaseTitle());
        addCaseMVO.setCreateCaseOnCrest(addCase.isCreateCaseOnCrest());
        addCaseMVO.setHearingType(addCase.getHearingType());

        if (addCase.getVersion() != null) {
            addCaseMVO.setVersion(addCase.getVersion().intValue());
        }
        if (addCase.getId() != null) {
            addCaseMVO.setId(addCase.getId().intValue());
        }

        return addCaseMVO;
    }

    public Object transformOutput(Object result) {
        if (result == null) {
            return null;
        } else if (result instanceof AddCaseMVO) {
            AddCaseMVO addCaseMVO = (AddCaseMVO) result;
            return transformOutput(addCaseMVO);
        } else {
            throw new CSConfigurationException("The Object is not of the expected type AddCaseMVO. Class is of type :"
                    + result.getClass().toString() + " with vos :" + result.toString());
        }
    }

    private AddCaseValue transformOutput(AddCaseMVO addCaseMVO) {
        AddCaseValue result = new AddCaseValue();

        result.setCaseNumber(new Integer(addCaseMVO.getCaseNumber()));
        result.setCaseTitle(addCaseMVO.getCaseTitle());
        result.setCaseType(addCaseMVO.getCaseType());
        result.setCourtID(new Integer(addCaseMVO.getCourtID()));
        result.setCreateCaseOnCrest(addCaseMVO.getCreateCaseOnCrest());
        result.setDirty(addCaseMVO.getDirty());
        result.setHearingType(addCaseMVO.getHearingType());
        result.setVersion(new Integer(addCaseMVO.getVersion()));

        return result;
    }
}
