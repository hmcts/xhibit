package uk.gov.courtservice.xhibit.integration.services.stub;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AppealJudgeCommentLineMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DisposalMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsSequenceMVO;
import uk.gov.courtservice.xhibit.common.results.vos.CaseAppReasonSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformerFactory;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: ResultsUpdateStub
 * </p>
 * <p>
 * Description: Test Class for Results testing.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems</p
 * 
 * @author GJS - updated for generated MVOs
 * @version 1.1
 */
public class ResultsUpdateStub {
    private static final String APPEAL_JUDGE_COMMENT_LINE = "AppealJudgeCommentLine";

    private static final String DISPOSAL = "Disposal";

    private static final Logger log = CSServices.getLogger(ResultsUpdateStub.class);

    public ResultsSaveValue setResults(ResultsSaveValue vo) throws MercatorException, TransformationException,
            OutputTransformationException {

        // Create the transformer and transform the code
        VOTransformer vot = VOTransformerFactory.getInstance().createVOTransformer(vo.getClass());
        ResultsMVO mvo = (ResultsMVO) vot.transformVO(vo);

        /*
         * // Simulate Mercator Error
         * 
         * ReturnMVO returnMVO = new ReturnMVO();
         * 
         * returnMVO.setDescription("STUB: One or more inputs was invalid");
         * returnMVO.setErrorIdentifier("STUB: ReturnMVO_errorIdentifier");
         * returnMVO.setExtraInfo("STUB: Input Data: FOO");
         * returnMVO.setMercatorReturnCode(999); returnMVO.setSeverity("STUB:
         * ERROR"); returnMVO.setSource("STUB: ReturnMVO_source");
         * returnMVO.setSourceInterface("STUB: ReturnMVO_sourceInterface");
         * returnMVO.setTargetInterface("STUB: ReturnMVO_targetInterface");
         * returnMVO.setTimestamp(Calendar.getInstance().getTime().toString());
         * returnMVO.setTypeOfTransaction("STUB: ReturnMVO_typeOfTransaction");
         * returnMVO.setValidationCode("STUB: ReturnMVO_validationCode");
         * 
         * throw new MercatorExecutionException( "integ.mercator.execution",
         * "STUB: Mercator Execution Errors", returnMVO);
         */

        // Simulate Mercator
        ResultsSequenceMVO[] sequence = mvo.getResultsSequenceMVO();

        for (int i = 0; i < sequence.length; i++) {
            // Return Code
            sequence[i].setReturnCode(0);

            // Crest Keys (second condition should always be true if first
            // is true)
            ResultSaveValue rsv = vo.getResultSaveValue(i);
            if (isDisposal(sequence[i]) && rsv instanceof DisposalSaveValue) {
                setDisId(getDisposal(mvo, sequence[i].getDataIndex()), (DisposalSaveValue) rsv);
            } else if (isAppealJudgeCommentLine(sequence[i]) && rsv instanceof CaseAppReasonSaveValue) {
                setCarId(getAppealJudgeCommentLine(mvo, sequence[i].getDataIndex()), (CaseAppReasonSaveValue) rsv);
            }
        }

        // Transform the result and return
        return (ResultsSaveValue) vot.transformOutput(mvo);
    }

    //
    // Helper methods not on MVO to keep it clean (do not want to pollute
    // mercator interface)
    //
    private static void setCarId(AppealJudgeCommentLineMVO carmvo, CaseAppReasonSaveValue carsv) {
        if (carsv.getCaseAppReasonId() != null) {
            carmvo.setCarId(carsv.getCaseAppReasonId().intValue());
        }
        if (log.isDebugEnabled()) {
            log.debug("CaseAppReason carId set to " + carmvo.getCarId());
        }
    }

    private static boolean isAppealJudgeCommentLine(ResultsSequenceMVO resultsSequenceMVO) {
        return APPEAL_JUDGE_COMMENT_LINE.equals(resultsSequenceMVO.getDataType());
    }

    private static AppealJudgeCommentLineMVO getAppealJudgeCommentLine(ResultsMVO mvo, int index) {
        return mvo.getAppealJudgeCommentLineMVO(index);
    }

    private static boolean isDisposal(ResultsSequenceMVO resultsSequenceMVO) {
        return DISPOSAL.equals(resultsSequenceMVO.getDataType());
    }

    private static void setDisId(DisposalMVO dmvo, DisposalSaveValue dsv) {
        if (dsv.getDisposal2Id() != null) {
            dmvo.setDisId(dsv.getDisposal2Id().intValue());
        }
        if (log.isDebugEnabled()) {
            log.debug("Disposal disId set to " + dmvo.getDisId());
        }
    }

    private static DisposalMVO getDisposal(ResultsMVO mvo, int index) {
        return mvo.getDisposalMVO(index);
    }
}
