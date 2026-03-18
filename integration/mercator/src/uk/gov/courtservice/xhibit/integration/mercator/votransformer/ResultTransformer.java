package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AppealJudgeCommentLineMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.CrimAppealOffenceResultMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.CrimAppealResultMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DisposalLineMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DisposalMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.GenDisAppealResultMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.MiscAppealResultMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.PleaOnBreachMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.PleaOnCountMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.PleaOnSumMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsSequenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.VerdictMVO;
import uk.gov.courtservice.xhibit.common.results.vos.CaseAppReasonSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;

/**
 * <p>
 * Title: ResultTransformer
 * </p>
 * <p>
 * Description: Class for transforming a ResultsSaveValue object graph into a
 * ResultsMVO object graph.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Revision: 1.44 $
 * @author GJS - updated for generated MVOs
 * @version $Revision: 1.44 $
 */
public class ResultTransformer implements VOTransformer {
    private static final String PLEA_ON_COUNT = "PleaOnCount";

    private static final String PLEA_ON_SUM = "PleaOnSum";

    private static final String PLEA_ON_BREACH = "PleaOnBreach";

    private static final String VERDICT = "Verdict";

    private static final String CRIM_APPEAL_RESULT = "CrimAppealResult";

    private static final String CRIM_APPEAL_OFFENCE_RESULT = "CrimAppealOffenceResult";

    private static final String GEN_DIS_APPEAL_RESULT = "GenDisAppealResult";

    private static final String APPEAL_JUDGE_COMMENT_LINE = "AppealJudgeCommentLine";

    private static final String MISC_APPEAL_RESULT = "MiscAppealResult";

    private static final String DISPOSAL = "Disposal";

    // *** MAKES THE TRANSFORMER STATEFUL ***
    private ResultsSaveValue input;

    // The logger :)
    private static final Logger log = CSServices.getLogger(ResultTransformer.class);

    public ResultTransformer() {
    }

    public Object transformVO(Object newInput) throws TransformationException {
        if (newInput == null) {
            return null;
        } else if (newInput instanceof ResultsSaveValue) {
            input = (ResultsSaveValue) newInput;
            ResultsMVO output = transformVO();

            if (log.isDebugEnabled()) {
                // Logging of import ResultsMVO done in inputLocalLogging of
                // HTTPMercatorWrapperImpl
                input.recordMvoBeforeExportDump(output);
                log.debug(input.getMvoBeforeExportDump());
            }
            return output;
        } else {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type ResultsSaveValue. Class is of type :"
                            + newInput.getClass().toString() + " with vos :" + newInput.toString());
        }
    }

    // VOTransformer implementation
    public Object transformOutput(Object output) throws OutputTransformationException {
        if (output == null) {
            return null;
        } else if (output instanceof ResultsMVO) {
            ResultsMVO resultsMVO = (ResultsMVO) output;

            if (log.isDebugEnabled()) {
                // Note: Logging of export ResultsMVO done in outputLocalLogging
                // of HTTPMercatorWrapperImpl
                input.recordMvoAfterExportDump(resultsMVO);
                log.debug(input.getMvoAfterExportDump());
            }
            return transformOutput(resultsMVO);
        } else {
            throw new CSConfigurationException("The Object is not of the expected type ResultsMVO. Class is of type :"
                    + output.getClass().toString() + " with vos :" + output.toString());
        }
    }

    private ResultsMVO transformVO() {
        XhbCourtBasicValue court = XhbCourtBeanHelper2.findByPrimaryKeyValue(input.getCourtId());

        DynamicResultsMVO dynamicResultsMVO = new DynamicResultsMVO(court.getCrestCourtId(), court.getCrestIpAddress());

        for (int i = 0, c = input.getResultSaveValueCount(); i < c; i++) {
            ResultSaveValue resultSaveValue = input.getResultSaveValue(i);
            if (resultSaveValue instanceof PleaSaveValue) {
                PleaSaveValue pleaSaveValue = (PleaSaveValue) resultSaveValue;
                if (pleaSaveValue.isOnCount()) {
                    dynamicResultsMVO.addPleaOnCount(pleaSaveValue.getOperation(), createPleaOnCount(pleaSaveValue));
                } else if (pleaSaveValue.isOnSummary()) {
                    dynamicResultsMVO.addPleaOnSum(pleaSaveValue.getOperation(), createPleaOnSum(pleaSaveValue));
                } else if (pleaSaveValue.isOnBreach()) {
                    dynamicResultsMVO.addPleaOnBreach(pleaSaveValue.getOperation(), createPleaOnBreach(pleaSaveValue));
                }
            } else if (resultSaveValue instanceof VerdictSaveValue) {
                VerdictSaveValue verdictSaveValue = (VerdictSaveValue) resultSaveValue;
                if (verdictSaveValue.isMiscAppeal()) {
                    dynamicResultsMVO.addMiscAppealResult(verdictSaveValue.getOperation(),
                            createMiscAppealResult(verdictSaveValue));
                } else if (verdictSaveValue.isCriminalAppealOnCase()) {
                    dynamicResultsMVO.addCrimAppealResult(verdictSaveValue.getOperation(),
                            createCrimAppealResult(verdictSaveValue));
                } else if (verdictSaveValue.isCriminalAppealOnOffence()) {
                    dynamicResultsMVO.addCrimAppealOffenceResult(verdictSaveValue.getOperation(),
                            createCrimAppealOffenceResult(verdictSaveValue));
                } else if (verdictSaveValue.isResultForMagistrateGeneralDisposal()) {
                    dynamicResultsMVO.addGenDisAppealResult(verdictSaveValue.getOperation(),
                            createGenDisAppealResult(verdictSaveValue));
                } else {
                    dynamicResultsMVO.addVerdict(verdictSaveValue.getOperation(), createVerdict(verdictSaveValue));
                }
            } else if (resultSaveValue instanceof CaseAppReasonSaveValue) {
                dynamicResultsMVO.addAppealJudgeCommentLine(resultSaveValue.getOperation(),
                        createCaseAppReason((CaseAppReasonSaveValue) resultSaveValue));
            } else if (resultSaveValue instanceof DisposalSaveValue) {
                dynamicResultsMVO.addDisposal(resultSaveValue.getOperation(),
                        createDisposal((DisposalSaveValue) resultSaveValue));
            } else {
                throw new IllegalArgumentException("ResultSaveValue [" + i + "] type: "
                        + resultSaveValue.getClass().getName() + " is not supported");
            }
        }

        return dynamicResultsMVO.createResultsMVO();
    }

    private ResultsSaveValue transformOutput(ResultsMVO output) {
        ResultsSequenceMVO[] sequence = output.getResultsSequenceMVO();
        ResultSaveValue resultSaveValue = null;

        for (int i = 0; i < sequence.length; i++) {
            resultSaveValue = input.getResultSaveValue(sequence[i].getXhibitSequenceNo());

            resultSaveValue.setDataType(sequence[i].getDataType());
            resultSaveValue.setReturnCode(new Integer(sequence[i].getReturnCode()));
            resultSaveValue.setOraCode(new Integer(sequence[i].getOraCode()));

            if (resultSaveValue instanceof CaseAppReasonSaveValue) {
                setCrestKeys((CaseAppReasonSaveValue) resultSaveValue, output.getAppealJudgeCommentLineMVO(sequence[i]
                        .getDataIndex()));
            } else if (resultSaveValue instanceof DisposalSaveValue) {
                setCrestKeys((DisposalSaveValue) resultSaveValue, output.getDisposalMVO(sequence[i].getDataIndex()));
            }
        }

        return input;
    }

    // Plea On Count
    private static PleaOnCountMVO createPleaOnCount(PleaSaveValue pleaSaveValue) {
        PleaOnCountMVO pleaOnCountMVO = new PleaOnCountMVO();

        if (pleaSaveValue.getCrestOffenceId() != null) {
            pleaOnCountMVO.setCouId(pleaSaveValue.getCrestOffenceId().intValue());
        }
        pleaOnCountMVO.setCaseType(pleaSaveValue.getEntityCaseType());
        if (pleaSaveValue.getEntityCaseNumber() != null) {
            pleaOnCountMVO.setCaseNo(pleaSaveValue.getEntityCaseNumber().intValue());
        }
        if (pleaSaveValue.getCrestDefendantId() != null) {
            pleaOnCountMVO.setSubId(pleaSaveValue.getCrestDefendantId().intValue());
        }

        if (!pleaSaveValue.isDeleteOperation()) {
            String lessOffPlea;
            String lessOffPleaDesc;

            if (pleaSaveValue.isCrestLessOffPleaUncoded()) {
                lessOffPlea = null;
                lessOffPleaDesc = pleaSaveValue.getCrestLessOffPleaDesc();
            } else {
                lessOffPlea = pleaSaveValue.getCrestLessOffPlea();
                lessOffPleaDesc = null;
            }

            pleaOnCountMVO.setArraignmentDate(pleaSaveValue.getCrestArraignmentDate());
            pleaOnCountMVO.setPlea(pleaSaveValue.getCrestPlea());
            pleaOnCountMVO.setLessOffPlea(lessOffPlea);
            pleaOnCountMVO.setLessOffPleaDesc(lessOffPleaDesc);
            pleaOnCountMVO.setOtherPlea(pleaSaveValue.getCrestOtherPlea());
            pleaOnCountMVO.setDateType(pleaSaveValue.getCrestDateType());
            pleaOnCountMVO.setVerdictDate(pleaSaveValue.getCrestVerdictDate());
        }

        return pleaOnCountMVO;
    }

    // Plea On Summary
    private static PleaOnSumMVO createPleaOnSum(PleaSaveValue pleaSaveValue) {
        PleaOnSumMVO pleaOnSumMVO = new PleaOnSumMVO();

        pleaOnSumMVO.setCaseType(pleaSaveValue.getEntityCaseType());
        if (pleaSaveValue.getEntityCaseNumber() != null) {
            pleaOnSumMVO.setCaseNo(pleaSaveValue.getEntityCaseNumber().intValue());
        }
        if (pleaSaveValue.getCrestDefendantId() != null) {
            pleaOnSumMVO.setSubId(pleaSaveValue.getCrestDefendantId().intValue());
        }
        // Charge type is always O for summary offences
        pleaOnSumMVO.setChargeType("O");
        if (pleaSaveValue.getCrestOffenceSeqNo() != null) {
            // CREST chg_seq_no is stored in xhb_offence.crest_seq_no
            pleaOnSumMVO.setChargeNo(pleaSaveValue.getCrestOffenceSeqNo().intValue());
        }
        if (pleaSaveValue.getCrestOffenceId() != null) {
            // CREST chg_id is stored in xhb_offence.crest_offence_id
            pleaOnSumMVO.setChgId(pleaSaveValue.getCrestOffenceId().intValue());
        }

        if (!pleaSaveValue.isDeleteOperation()) {
            pleaOnSumMVO.setPlea(pleaSaveValue.getCrestPlea());
        }

        return pleaOnSumMVO;
    }

    // Plea On Breach
    private static PleaOnBreachMVO createPleaOnBreach(PleaSaveValue pleaSaveValue) {
        PleaOnBreachMVO pleaOnBreachMVO = new PleaOnBreachMVO();

        pleaOnBreachMVO.setCaseType(pleaSaveValue.getEntityCaseType());
        if (pleaSaveValue.getEntityCaseNumber() != null) {
            pleaOnBreachMVO.setCaseNo(pleaSaveValue.getEntityCaseNumber().intValue());
        }
        if (pleaSaveValue.getCrestDefendantId() != null) {
            pleaOnBreachMVO.setSubId(pleaSaveValue.getCrestDefendantId().intValue());
        }
        if (pleaSaveValue.getCrestDefendantId() != null) {
            pleaOnBreachMVO.setSubId(pleaSaveValue.getCrestDefendantId().intValue());
        }
        if (pleaSaveValue.getCrestBchId() != null) {
            pleaOnBreachMVO.setBchId(pleaSaveValue.getCrestBchId().intValue());
        }

        if (!pleaSaveValue.isDeleteOperation()) {
            pleaOnBreachMVO.setDatePut(pleaSaveValue.getCrestDatePut());
            pleaOnBreachMVO.setPlea(pleaSaveValue.getCrestBreachAdmitted());
        }

        return pleaOnBreachMVO;
    }

    // Misc Appeal Result
    private static MiscAppealResultMVO createMiscAppealResult(VerdictSaveValue verdictSaveValue) {
        MiscAppealResultMVO miscAppealResultMVO = new MiscAppealResultMVO();

        miscAppealResultMVO.setCaseType(verdictSaveValue.getEntityCaseType());
        if (verdictSaveValue.getEntityCaseNumber() != null) {
            miscAppealResultMVO.setCaseNo(verdictSaveValue.getEntityCaseNumber().intValue());
        }

        if (!verdictSaveValue.isDeleteOperation()) {
            miscAppealResultMVO.setAppResult(verdictSaveValue.getCrestCaseAppResult());
            miscAppealResultMVO.setAppResultDate(verdictSaveValue.getCrestAppResultDate());
            miscAppealResultMVO.setHearingDate(verdictSaveValue.getCrestHearingDate());
            if (verdictSaveValue.getCrestDuration() != null) {
                miscAppealResultMVO.setDuration(verdictSaveValue.getCrestDuration().intValue());
            }
            miscAppealResultMVO.setCccTransTo(verdictSaveValue.getCccTransToRefCourtCode());
        }

        return miscAppealResultMVO;
    }

    // Criminal Appeal Result
    private static CrimAppealResultMVO createCrimAppealResult(VerdictSaveValue verdictSaveValue) {
        CrimAppealResultMVO crimAppealResultMVO = new CrimAppealResultMVO();

        crimAppealResultMVO.setCaseType(verdictSaveValue.getEntityCaseType());
        if (verdictSaveValue.getEntityCaseNumber() != null) {
            crimAppealResultMVO.setCaseNo(verdictSaveValue.getEntityCaseNumber().intValue());
        }

        if (!verdictSaveValue.isDeleteOperation()) {
            crimAppealResultMVO.setAppResult(verdictSaveValue.getCrestCaseAppResult());
            crimAppealResultMVO.setAppResultDate(verdictSaveValue.getCrestAppResultDate());
        }

        return crimAppealResultMVO;
    }

    // Criminal Appeal Offence Result
    private static CrimAppealOffenceResultMVO createCrimAppealOffenceResult(VerdictSaveValue verdictSaveValue) {
        CrimAppealOffenceResultMVO crimAppealOffenceResultMVO = new CrimAppealOffenceResultMVO();

        crimAppealOffenceResultMVO.setCaseType(verdictSaveValue.getEntityCaseType());
        if (verdictSaveValue.getEntityCaseNumber() != null) {
            crimAppealOffenceResultMVO.setCaseNo(verdictSaveValue.getEntityCaseNumber().intValue());
        }
        if (verdictSaveValue.getCrestDefendantId() != null) {
            crimAppealOffenceResultMVO.setSubId(verdictSaveValue.getCrestDefendantId().intValue());
        }
        // Charge type is allways A for criminal appeals
        crimAppealOffenceResultMVO.setChargeType("A");
        if (verdictSaveValue.getCrestOffenceSeqNo() != null) {
            // CREST chg_seq_no is stored in xhb_offence.crest_seq_no
            crimAppealOffenceResultMVO.setChargeNo(verdictSaveValue.getCrestOffenceSeqNo().intValue());
        }
        if (verdictSaveValue.getCrestOffenceId() != null) {
            // CREST chg_id is stored in xhb_offence.crest_offence_id
            crimAppealOffenceResultMVO.setChgId(verdictSaveValue.getCrestOffenceId().intValue());
        }

        if (!verdictSaveValue.isDeleteOperation()) {
            crimAppealOffenceResultMVO.setAppResultCode(verdictSaveValue.getCrestOffenceAppResult());
            crimAppealOffenceResultMVO.setLesserOff(verdictSaveValue.getCrestLessOffVerdict());
        }

        return crimAppealOffenceResultMVO;
    }

    // Other Verdicts
    private static VerdictMVO createVerdict(VerdictSaveValue verdictSaveValue) {
        VerdictMVO verdictMVO = new VerdictMVO();

        if (verdictSaveValue.getCrestOffenceId() != null) {
            verdictMVO.setCouId(verdictSaveValue.getCrestOffenceId().intValue());
        }
        verdictMVO.setCaseType(verdictSaveValue.getEntityCaseType());
        if (verdictSaveValue.getEntityCaseNumber() != null) {
            verdictMVO.setCaseNo(verdictSaveValue.getEntityCaseNumber().intValue());
        }
        if (verdictSaveValue.getCrestDefendantId() != null) {
            verdictMVO.setSubId(verdictSaveValue.getCrestDefendantId().intValue());
        }

        if (!verdictSaveValue.isDeleteOperation()) {
            String lessOffVerdict;
            String lessOffVerdictDesc;
            if (verdictSaveValue.isCrestLessOffVerdictUncoded()) {
                lessOffVerdict = null;
                lessOffVerdictDesc = verdictSaveValue.getCrestLessOffVerdictDesc();
            } else {
                lessOffVerdict = verdictSaveValue.getCrestLessOffVerdict();
                lessOffVerdictDesc = null;
            }

            verdictMVO.setDateType(verdictSaveValue.getCrestDateType());
            verdictMVO.setVerdictDate(verdictSaveValue.getCrestVerdictDate());
            verdictMVO.setVerdict(verdictSaveValue.getCrestVerdict());
            verdictMVO.setOtherVerdict(verdictSaveValue.getCrestOtherVerdict());
            verdictMVO.setLessOffVerdict(lessOffVerdict);
            verdictMVO.setLessOffVerdictDesc(lessOffVerdictDesc);

            if (verdictSaveValue.getJurorsAssenting() != null) {
                verdictMVO.setAssJurors(verdictSaveValue.getJurorsAssenting().intValue());
            }
            if (verdictSaveValue.getJurorsDissenting() != null) {
                verdictMVO.setDissJurors(verdictSaveValue.getJurorsDissenting().intValue());
            }

        }

        return verdictMVO;
    }

    // Other Verdicts
    private static AppealJudgeCommentLineMVO createCaseAppReason(CaseAppReasonSaveValue caseAppReasonSaveValue) {
        AppealJudgeCommentLineMVO appealJudgeCommentLineMVO = new AppealJudgeCommentLineMVO();

        appealJudgeCommentLineMVO.setCaseType(caseAppReasonSaveValue.getEntityCaseType());
        if (caseAppReasonSaveValue.getEntityCaseNumber() != null) {
            appealJudgeCommentLineMVO.setCaseNo(caseAppReasonSaveValue.getEntityCaseNumber().intValue());
        }

        if (caseAppReasonSaveValue.isDeleteOperation() || caseAppReasonSaveValue.isUpdateOperation()) {
            if (caseAppReasonSaveValue.getCarId() != null) {
                appealJudgeCommentLineMVO.setCarId(caseAppReasonSaveValue.getCarId().intValue());
            }
        }
        if (!caseAppReasonSaveValue.isDeleteOperation()) {
            appealJudgeCommentLineMVO.setAppReason(caseAppReasonSaveValue.getAppReason());
        }

        return appealJudgeCommentLineMVO;
    }

    // Appeal Result for Magistrates Court General Disposal
    private static GenDisAppealResultMVO createGenDisAppealResult(VerdictSaveValue verdictSaveValue) {
        GenDisAppealResultMVO genDisAppealResultMVO = new GenDisAppealResultMVO();

        genDisAppealResultMVO.setCaseType(verdictSaveValue.getEntityCaseType());
        if (verdictSaveValue.getEntityCaseNumber() != null) {
            genDisAppealResultMVO.setCaseNo(verdictSaveValue.getEntityCaseNumber().intValue());
        }
        if (verdictSaveValue.getDisposalSaveValue() != null
                && verdictSaveValue.getDisposalSaveValue().getDisId() != null) {
            genDisAppealResultMVO.setDisId(verdictSaveValue.getDisposalSaveValue().getDisId().intValue());
        }

        if (!verdictSaveValue.isDeleteOperation()) {
            genDisAppealResultMVO.setAppResultCode(verdictSaveValue.getCrestOffenceAppResult());
        }

        return genDisAppealResultMVO;
    }

    // Disposals
    private static DisposalMVO createDisposal(DisposalSaveValue disposalSaveValue) {
        DisposalMVO disposalMVO = new DisposalMVO();

        disposalMVO.setDisposalCode(disposalSaveValue.getDisposalCode());
        disposalMVO.setCaseType(disposalSaveValue.getEntityCaseType());
        if (disposalSaveValue.getEntityCaseNumber() != null) {
            disposalMVO.setCaseNo(disposalSaveValue.getEntityCaseNumber().intValue());
        }
        if (disposalSaveValue.getTemplateVersion() != null) {
            disposalMVO.setTemplateVersion(disposalSaveValue.getTemplateVersion().intValue());
        }
        if (disposalSaveValue.getCrestDefendantId() != null) {
            disposalMVO.setSubId(disposalSaveValue.getCrestDefendantId().intValue());
        }

        if (disposalSaveValue.isDeleteOperation() || disposalSaveValue.isUpdateOperation()) {
            if (disposalSaveValue.getDisId() != null) {
                disposalMVO.setDisId(disposalSaveValue.getDisId().intValue());
            }
        }

        if (!disposalSaveValue.isDeleteOperation()) {
            // Indictments have couId not chgId
            Integer chgId;
            Integer couId;

            if (disposalSaveValue.isUnrelatedDisposal()) {
                chgId = null;
                couId = null;
            } else if (disposalSaveValue.isOnCount()) {
                chgId = null;
                couId = disposalSaveValue.getCrestOffenceId();
            } else {
                chgId = disposalSaveValue.getCrestOffenceId();
                couId = null;
            }

            disposalMVO.setCourtType(disposalSaveValue.getCourtType());
            if (chgId != null) {
                disposalMVO.setChgId(chgId.intValue());
            }
            if (disposalSaveValue.getPsdDisId() != null) {
                disposalMVO.setPsdDisId(disposalSaveValue.getPsdDisId().intValue());
            }
            if (couId != null) {
                disposalMVO.setCouId(couId.intValue());
            }
            disposalMVO.setDisposalLineMVO(createDisposalLines(disposalSaveValue.getDisposalLineSaveValues()));
        }
        
        return disposalMVO;
    }

    private static DisposalLineMVO[] createDisposalLines(DisposalLineSaveValue[] disposalLineSaveValues) {
        DisposalLineMVO[] disposalLineMVOs = new DisposalLineMVO[disposalLineSaveValues.length];

        DisposalLineMVO disposalLineMVO = null;

        for (int i = 0; i < disposalLineMVOs.length; i++) {
            disposalLineMVO = new DisposalLineMVO();

            if (disposalLineSaveValues[i].getDilSeqNo() != null) {
                disposalLineMVO.setDilSeqNo(disposalLineSaveValues[i].getDilSeqNo().intValue());
            } else {
                disposalLineMVO.setDilSeqNo(0);
            }

            disposalLineMVO.setData(disposalLineSaveValues[i].getData());
            disposalLineMVO.setDelData(disposalLineSaveValues[i].getDelData());

            if (disposalLineSaveValues[i].getDelG1() != null) {
                disposalLineMVO.setDelG1(disposalLineSaveValues[i].getDelG1().booleanValue());
            } else {
                disposalLineMVO.setDelG1(false);
            }
            if (disposalLineSaveValues[i].getDelG2() != null) {
                disposalLineMVO.setDelG2(disposalLineSaveValues[i].getDelG2().booleanValue());
            } else {
                disposalLineMVO.setDelG2(false);
            }

            if (disposalLineSaveValues[i].getLineInsertNo() != null) {
                disposalLineMVO.setLineInsertNo(disposalLineSaveValues[i].getLineInsertNo().intValue());
            } else {
                disposalLineMVO.setLineInsertNo(0);
            }

            disposalLineMVOs[i] = disposalLineMVO;
        }

        return disposalLineMVOs;
    }

    // copy crest keys
    private void setCrestKeys(CaseAppReasonSaveValue caseAppReasonSaveValue,
            AppealJudgeCommentLineMVO appealJudgeCommentLine) {
        caseAppReasonSaveValue.setCarId(new Integer(appealJudgeCommentLine.getCarId()));
    }

    private void setCrestKeys(DisposalSaveValue disposalSaveValue, DisposalMVO disposalMVO) {
        disposalSaveValue.setDisId(new Integer(disposalMVO.getDisId()));
    }

    /**
     * Class to hold the objects as we build the MVO
     */
    private static class DynamicResultsMVO {
        // Empty Array Constants
        private static final PleaOnCountMVO[] EMPTY_PLEA_ON_COUNTS = new PleaOnCountMVO[0];

        private static final PleaOnSumMVO[] EMPTY_PLEA_ON_SUMS = new PleaOnSumMVO[0];

        private static final PleaOnBreachMVO[] EMPTY_PLEA_ON_BREACHES = new PleaOnBreachMVO[0];

        private static final VerdictMVO[] EMPTY_VERDICTS = new VerdictMVO[0];

        private static final CrimAppealResultMVO[] EMPTY_CRIM_APPEAL_RESULTS = new CrimAppealResultMVO[0];

        private static final CrimAppealOffenceResultMVO[] EMPTY_CRIM_APPEAL_OFFENCE_RESULTS = new CrimAppealOffenceResultMVO[0];

        private static final GenDisAppealResultMVO[] EMPTY_GEN_DIS_APPEAL_RESULTS = new GenDisAppealResultMVO[0];

        private static final AppealJudgeCommentLineMVO[] EMPTY_APPEAL_JUDGE_COMMENT_LINES = new AppealJudgeCommentLineMVO[0];

        private static final MiscAppealResultMVO[] EMPTY_MISC_APPEAL_RESULTS = new MiscAppealResultMVO[0];

        private static final DisposalMVO[] EMPTY_DISPOSALS = new DisposalMVO[0];

        // Data
        private final List sequence = new ArrayList();

        private final String crestCourtId;

        private final String crestIPAddress;

        private List pleaOnCounts;

        private List pleaOnSums;

        private List pleaOnBreaches;

        private List verdicts;

        private List crimAppealResults;

        private List crimAppealOffenceResults;

        private List genDisAppealResults;

        private List appealJudgeCommentLines;

        private List miscAppealResults;

        private List disposals;

        public DynamicResultsMVO(String crestCourtId, String crestIPAddress) {
            if (crestCourtId == null) {
                throw new IllegalArgumentException("crestCourtId: null");
            }
            if (crestIPAddress == null) {
                throw new IllegalArgumentException("crestIPAddress: null");
            }
            this.crestCourtId = crestCourtId;
            this.crestIPAddress = crestIPAddress;
        }

        public void addPleaOnCount(String transType, PleaOnCountMVO pleaOnCount) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (pleaOnCount == null) {
                throw new IllegalArgumentException("pleaOnCount: null");
            }
            // Check Array
            if (pleaOnCounts == null) {
                pleaOnCounts = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(pleaOnCounts.size());
            resultsSequenceMVO.setDataType(PLEA_ON_COUNT);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            pleaOnCounts.add(pleaOnCount);
        }

        public void addPleaOnSum(String transType, PleaOnSumMVO pleaOnSum) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (pleaOnSum == null) {
                throw new IllegalArgumentException("pleaOnSum: null");
            }
            // Check Array
            if (pleaOnSums == null) {
                pleaOnSums = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(pleaOnSums.size());
            resultsSequenceMVO.setDataType(PLEA_ON_SUM);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            pleaOnSums.add(pleaOnSum);
        }

        public void addPleaOnBreach(String transType, PleaOnBreachMVO pleaOnBreach) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (pleaOnBreach == null) {
                throw new IllegalArgumentException("pleaOnBreach: null");
            }
            // Check Array
            if (pleaOnBreaches == null) {
                pleaOnBreaches = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(pleaOnBreaches.size());
            resultsSequenceMVO.setDataType(PLEA_ON_BREACH);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            pleaOnBreaches.add(pleaOnBreach);
        }

        public void addMiscAppealResult(String transType, MiscAppealResultMVO miscAppealResult) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (miscAppealResult == null) {
                throw new IllegalArgumentException("miscAppealResult: null");
            }
            // Check Array
            if (miscAppealResults == null) {
                miscAppealResults = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(miscAppealResults.size());
            resultsSequenceMVO.setDataType(MISC_APPEAL_RESULT);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            miscAppealResults.add(miscAppealResult);
        }

        public void addCrimAppealResult(String transType, CrimAppealResultMVO crimAppealResult) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (crimAppealResult == null) {
                throw new IllegalArgumentException("crimAppealResult: null");
            }
            // Check Array
            if (crimAppealResults == null) {
                crimAppealResults = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(crimAppealResults.size());
            resultsSequenceMVO.setDataType(CRIM_APPEAL_RESULT);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            crimAppealResults.add(crimAppealResult);
        }

        public void addCrimAppealOffenceResult(String transType, CrimAppealOffenceResultMVO crimAppealOffenceResult) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (crimAppealOffenceResult == null) {
                throw new IllegalArgumentException("crimAppealOffenceResult: null");
            }
            // Check Array
            if (crimAppealOffenceResults == null) {
                crimAppealOffenceResults = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(crimAppealOffenceResults.size());
            resultsSequenceMVO.setDataType(CRIM_APPEAL_OFFENCE_RESULT);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            crimAppealOffenceResults.add(crimAppealOffenceResult);
        }

        public void addGenDisAppealResult(String transType, GenDisAppealResultMVO genDisAppealResult) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (genDisAppealResult == null) {
                throw new IllegalArgumentException("genDisAppealResult: null");
            }
            // Check Array
            if (genDisAppealResults == null) {
                genDisAppealResults = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(genDisAppealResults.size());
            resultsSequenceMVO.setDataType(GEN_DIS_APPEAL_RESULT);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            genDisAppealResults.add(genDisAppealResult);
        }

        public void addVerdict(String transType, VerdictMVO verdict) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (verdict == null) {
                throw new IllegalArgumentException("verdict: null");
            }
            // Check Array
            if (verdicts == null) {
                verdicts = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(verdicts.size());
            resultsSequenceMVO.setDataType(VERDICT);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            verdicts.add(verdict);
        }

        public void addDisposal(String transType, DisposalMVO disposal) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (disposal == null) {
                throw new IllegalArgumentException("disposal: null");
            }
            // Check Array
            if (disposals == null) {
                disposals = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(disposals.size());
            resultsSequenceMVO.setDataType(DISPOSAL);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            disposals.add(disposal);
        }

        public void addAppealJudgeCommentLine(String transType, AppealJudgeCommentLineMVO appealJudgeCommentLine) {
            // Check Arguments
            if (transType == null) {
                throw new IllegalArgumentException("transType: null");
            }
            if (appealJudgeCommentLine == null) {
                throw new IllegalArgumentException("appealJudgeCommentLine: null");
            }
            // Check Array
            if (appealJudgeCommentLines == null) {
                appealJudgeCommentLines = new ArrayList();
            }

            // Create Sequence
            ResultsSequenceMVO resultsSequenceMVO = new ResultsSequenceMVO();
            resultsSequenceMVO.setXhibitSequenceNo(sequence.size());
            resultsSequenceMVO.setTransType(transType);
            resultsSequenceMVO.setDataIndex(appealJudgeCommentLines.size());
            resultsSequenceMVO.setDataType(APPEAL_JUDGE_COMMENT_LINE);

            sequence.add(resultsSequenceMVO);

            // Add MVO
            appealJudgeCommentLines.add(appealJudgeCommentLine);
        }

        public ResultsMVO createResultsMVO() {
            ResultsMVO resultsMVO = new ResultsMVO();

            // Court data
            resultsMVO.setCrestCourtId(getCrestCourtId());
            resultsMVO.setCrestIPAddress(getCrestIPAddress());

            // Meta data
            resultsMVO.setResultsSequenceMVO(getSequence());

            // Results data
            resultsMVO.setPleaOnCountMVO(getPleaOnCounts());
            resultsMVO.setPleaOnSumMVO(getPleaOnSums());
            resultsMVO.setPleaOnBreachMVO(getPleaOnBreaches());
            resultsMVO.setMiscAppealResultMVO(getMiscAppealResults());
            resultsMVO.setCrimAppealResultMVO(getCrimAppealResults());
            resultsMVO.setCrimAppealOffenceResultMVO(getCrimAppealOffenceResults());
            resultsMVO.setGenDisAppealResultMVO(getGenDisAppealResults());
            resultsMVO.setVerdictMVO(getVerdicts());
            resultsMVO.setAppealJudgeCommentLineMVO(getAppealJudgeCommentLines());
            resultsMVO.setDisposalMVO(getDisposals());

            return resultsMVO;
        }

        private String getCrestCourtId() {
            return crestCourtId;
        }

        private String getCrestIPAddress() {
            return crestIPAddress;
        }

        private ResultsSequenceMVO[] getSequence() {
            return (ResultsSequenceMVO[]) sequence.toArray(new ResultsSequenceMVO[sequence.size()]);
        }

        private PleaOnCountMVO[] getPleaOnCounts() {
            return pleaOnCounts == null ? EMPTY_PLEA_ON_COUNTS : (PleaOnCountMVO[]) pleaOnCounts
                    .toArray(new PleaOnCountMVO[pleaOnCounts.size()]);
        }

        private PleaOnSumMVO[] getPleaOnSums() {
            return pleaOnSums == null ? EMPTY_PLEA_ON_SUMS : (PleaOnSumMVO[]) pleaOnSums
                    .toArray(new PleaOnSumMVO[pleaOnSums.size()]);
        }

        private PleaOnBreachMVO[] getPleaOnBreaches() {
            return pleaOnBreaches == null ? EMPTY_PLEA_ON_BREACHES : (PleaOnBreachMVO[]) pleaOnBreaches
                    .toArray(new PleaOnBreachMVO[pleaOnBreaches.size()]);
        }

        private VerdictMVO[] getVerdicts() {
            return verdicts == null ? EMPTY_VERDICTS : (VerdictMVO[]) verdicts.toArray(new VerdictMVO[verdicts.size()]);
        }

        private CrimAppealResultMVO[] getCrimAppealResults() {
            return crimAppealResults == null ? EMPTY_CRIM_APPEAL_RESULTS : (CrimAppealResultMVO[]) crimAppealResults
                    .toArray(new CrimAppealResultMVO[crimAppealResults.size()]);
        }

        private CrimAppealOffenceResultMVO[] getCrimAppealOffenceResults() {
            return crimAppealOffenceResults == null ? EMPTY_CRIM_APPEAL_OFFENCE_RESULTS
                    : (CrimAppealOffenceResultMVO[]) crimAppealOffenceResults
                            .toArray(new CrimAppealOffenceResultMVO[crimAppealOffenceResults.size()]);
        }

        private GenDisAppealResultMVO[] getGenDisAppealResults() {
            return genDisAppealResults == null ? EMPTY_GEN_DIS_APPEAL_RESULTS
                    : (GenDisAppealResultMVO[]) genDisAppealResults
                            .toArray(new GenDisAppealResultMVO[genDisAppealResults.size()]);
        }

        private AppealJudgeCommentLineMVO[] getAppealJudgeCommentLines() {
            return appealJudgeCommentLines == null ? EMPTY_APPEAL_JUDGE_COMMENT_LINES
                    : (AppealJudgeCommentLineMVO[]) appealJudgeCommentLines
                            .toArray(new AppealJudgeCommentLineMVO[appealJudgeCommentLines.size()]);
        }

        private MiscAppealResultMVO[] getMiscAppealResults() {
            return miscAppealResults == null ? EMPTY_MISC_APPEAL_RESULTS : (MiscAppealResultMVO[]) miscAppealResults
                    .toArray(new MiscAppealResultMVO[miscAppealResults.size()]);
        }

        private DisposalMVO[] getDisposals() {
            return disposals == null ? EMPTY_DISPOSALS : (DisposalMVO[]) disposals.toArray(new DisposalMVO[disposals
                    .size()]);
        }
    }
}