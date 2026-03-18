package uk.gov.courtservice.xhibit.integration.services.hearingschedule;

// Third Party
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntController;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

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
 */

public class HearingScheduleUpdateIntController extends IntController {

    private static Logger log = CSServices.getLogger(HearingScheduleUpdateIntController.class);

    public HearingScheduleUpdateIntController() {
        super();
    }

    public AddCaseValue getCase(AddCaseValue addCaseValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("addCase Input (addCaseValue):" + addCaseValue.toString());
        Object returnAddCaseValue = executeUpdate("addCase", addCaseValue);

        if (returnAddCaseValue != null && returnAddCaseValue instanceof AddCaseValue) {
            return (AddCaseValue) returnAddCaseValue;
        } else {
            throw new CSConfigurationException("returnAddCaseValue is either empty or not of the correct return type");
        }
    }
}