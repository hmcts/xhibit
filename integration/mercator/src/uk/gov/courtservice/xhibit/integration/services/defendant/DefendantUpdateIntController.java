package uk.gov.courtservice.xhibit.integration.services.defendant;

// Court Service
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntController;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * This is managing the databases updates (CREST and XHBIT) for defendants data.
 */
/**
 * <p>
 * Title: DefendantUpdateIntController
 * </p>
 * <p>
 * Description: This is managing the databases updates (CREST and XHBIT) for
 * defendants data.
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
 */
public class DefendantUpdateIntController extends IntController {
    private static Logger log = CSServices.getLogger(DefendantUpdateIntController.class);

    /**
     * @roseuid 3DD8FBB603D3
     */
    public DefendantUpdateIntController() {
        super();
    }

    /**
     * This method will update the defendant details.
     * 
     * @param defendantValue
     * @roseuid 3DBD6B0200DD
     */
    public void updateDefendant(uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue)
            throws MercatorException, TransformationException, OutputTransformationException {
        log.debug("updateDefendant Input (defendantValue):" + defendantValue.toString());
        executeUpdate("updateDefendant", defendantValue);
    }
    
    
    /**
     * This method will update the DOC details.
     * 
     * @param DefendantOnCaseBasicValue
     * @roseuid 3DBD6B0200DD
     */
    public void updateDefendantOnCase(uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue defendantOnCaseValue)
            throws MercatorException, TransformationException, OutputTransformationException {
        log.debug("updateDefendantOnCase Input (defendantOnCaseValue):" + defendantOnCaseValue.toString());
        executeUpdate("updateDefendantOnCase", defendantOnCaseValue);
    }    
}
