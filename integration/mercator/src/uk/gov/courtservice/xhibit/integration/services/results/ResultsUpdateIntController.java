package uk.gov.courtservice.xhibit.integration.services.results;

// Log4j
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntController;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: ResultsUpdateIntController
 * </p>
 * <p>
 * Description: This interface is managing the databases updates (CREST and
 * XHBIT) for pre-hearing related data.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.1
 */

public class ResultsUpdateIntController extends IntController {
    private static Logger log = CSServices.getLogger(ResultsUpdateIntController.class);

    public ResultsUpdateIntController() {
        super();
    }

    /**
     * This method is used to create/update/delete results for a case. These
     * include Pleas, Verdicts, Disposals and Case results.
     * 
     * @param ResultsValue
     */
    public ResultsSaveValue setResults(ResultsSaveValue resultValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("setResults Input (resultValue):" + resultValue.toString());

        Object returnResultsValue = executeUpdate("setResults", resultValue);

        if (returnResultsValue != null || returnResultsValue instanceof ResultsSaveValue) {
            return (ResultsSaveValue) returnResultsValue;
        } else {
            throw new CSConfigurationException("returnResultsValue is either empty or not of the correct return type");
        }
    }

}
