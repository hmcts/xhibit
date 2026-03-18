package uk.gov.courtservice.xhibit.integration.mercator;

import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddCaseMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.MercatorRequest;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.MercatorResponse;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OutputData;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.PrimaryKeysMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultOutputData;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsSequenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ReturnMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.VerdictMVO;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.integration.services.MercatorExecutionException;
import uk.gov.courtservice.xhibit.integration.services.MercatorValidationException;

/**
 * <p>
 * Title: StubMercatorWrapperImpl
 * </p>
 * <p>
 * Description: For development: dumps the MVO contents to a the debugger
 * without needing a connection to Mercator
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author GJS
 * @version 1.0
 */

public class StubMercatorWrapperImpl extends HTTPMercatorWrapperImpl {
    private static final String ADD_CASE = "addcase";

    private static final String RESULTS = "results";

    private static final String CHARGE = "charge";

    // Mercator Testing Exceptions
    private static final String IS_MERCATOR_EXCEPTION_TESTING = "IS_MERCATOR_EXCEPTION_TESTING";

    private static final String MERCATOR_EXCEPTION_TYPE = "MERCATOR_EXCEPTION_TYPE";

    private static final String MERCATOR_EXCEPTION_RETURN_CODE = "MERCATOR_EXCEPTION_RETURN_CODE";

    // the logger
    private static Logger log = CSServices.getLogger(StubMercatorWrapperImpl.class);

    /**
     * Creates a dummy mercatorResponse with stubbed values
     * 
     * This is so we can run tests when the Mercator backend is not available.
     * 
     * Only to be used when testing the HTTP connection
     * 
     * @param Applicationcomms
     *            xml (not used): input xml message to send to the Mercator
     *            Agent over HTTP
     * @param logicalMapName:
     *            map name of the transaction
     * @return mercatorResponse This will be the output xml message read from
     *         the Mercator Agent over HTTP
     * @throws IOException
     *             failed to send message
     */
    public MercatorResponse sendXmlMessage(String logicalMapName, MercatorRequest xml) throws IOException {
        log.debug("<< ********** STUB: START createStubmercatorResponse ********** " + logicalMapName + " >>");

        MercatorResponse mercatorResponse = new MercatorResponse();
        OutputData outputData = new OutputData();
        ResultOutputData resultOutputData = new ResultOutputData();

        ReturnMVO returnMVO = new ReturnMVO();
        AddCaseMVO addCaseMVO = null;
        ResultsMVO resultsMVO = null;
        PrimaryKeysMVO primaryKeysMVO = null;
        ResultsSequenceMVO[] resultsSequenceMVOArray = null;

        // Set date and time on the mercatorResponse
        Calendar currentDate = Calendar.getInstance();
        String currentTime = getTime(currentDate);

        log.debug("<< ********** STUB: currentTime ********** " + currentTime + " >>");

        try {
            mercatorResponse.setMsgDate(new org.exolab.castor.types.Date(currentDate.getTime()));
            mercatorResponse.setMsgTime(new org.exolab.castor.types.Time(currentTime));
        } catch (Exception e) {
            log.debug("<< ********** STUB: Exception setting time ********** " + logicalMapName + " >>");
        }

        // Set mandatory returnMVO in outputData on the mercatorResponse to
        // stubbed values
        returnMVO.setDescription("STUB: ReturnMVO_description");
        returnMVO.setErrorIdentifier("STUB: ReturnMVO_errorIdentifier");
        returnMVO.setExtraInfo("STUB: ReturnMVO_ExtraInfo");
        returnMVO.setMercatorReturnCode(0);
        returnMVO.setSeverity("STUB: ReturnMVO_severity");
        returnMVO.setSource("STUB: ReturnMVO_source");
        returnMVO.setSourceInterface("STUB: ReturnMVO_sourceInterface");
        returnMVO.setTargetInterface("STUB: ReturnMVO_targetInterface");
        returnMVO.setTimestamp(currentTime);
        returnMVO.setTypeOfTransaction("STUB: ReturnMVO_typeOfTransaction");
        returnMVO.setValidationCode("STUB: ReturnMVO_validationCode");

        log.debug("Stub: Setting the Output of mercatorResponse to returnMVO");
        outputData.setReturnMVO(returnMVO);
        mercatorResponse.setOutputData(outputData);

        // Set optional MVO in resultOutputData where applicable on the
        // mercatorResponse to stubbed values
        if (logicalMapName.toLowerCase().indexOf(ADD_CASE) != -1) {
            log.debug("Stub: Create AddCaseMVO");
            addCaseMVO = new AddCaseMVO();

            addCaseMVO.setCaseNumber(9999);
            addCaseMVO.setCaseTitle("STUB: AddCaseMVO_caseTitle");
            addCaseMVO.setCaseType("STUB: AddCaseMVO_caseType");
            addCaseMVO.setCourtID(8888);
            addCaseMVO.setCreateCaseOnCrest(false);
            addCaseMVO.setDirty(false);
            addCaseMVO.setHearingType("STUB: AddCaseMVO_hearingType");
            addCaseMVO.setId(0);
            addCaseMVO.setVersion(1);

            log.debug("Stub: Setting the ResultOutput of mercatorResponse to addCaseMVO");
            resultOutputData.setAddCaseMVO(addCaseMVO);
            mercatorResponse.setResultOutputData(resultOutputData);
        } else if (logicalMapName.toLowerCase().indexOf(CHARGE) != -1) {
            log.debug("Stub: Create PrimaryKeysMVO");
            primaryKeysMVO = new PrimaryKeysMVO();

            primaryKeysMVO.setPrimaryKeys(new int[] { 1, 2, 3 });

            log.debug("Stub: Setting the ResultOutput of mercatorResponse to primaryKeysMVO");
            resultOutputData.setPrimaryKeysMVO(primaryKeysMVO);
            mercatorResponse.setResultOutputData(resultOutputData);
        } else if (logicalMapName.toLowerCase().indexOf(RESULTS) != -1) {
            log.debug("Stub: Create ResultsMVO (Dummy Verdict only added)");
            resultsMVO = new ResultsMVO();

            XhbVerdictBasicValue xhbVerdictBasicValue1 = new XhbVerdictBasicValue(new Integer(1121),
                    "STUB: Verdict ObsInd1", "STUB: Verdict defOnChargeOrOffence1", new Integer(1122),
                    new Integer(1123), new Integer(1124), currentDate.getTime(), new Integer(1125), new Integer(1126),
                    "STUB: Verdict lastUpdatedBy1", "STUB: Verdict otherVerdictText1", "STUB: Verdict createdBy1",
                    currentDate.getTime(), currentDate.getTime(), new Integer(1127), new Integer(1128), new Integer(
                            1129), new Integer(1130), "STUB: Verdict appLesserOff1",
                    "STUB: Verdict altUncodedOffenceDesc1", new Integer(1131), new Integer(1132));

            XhbVerdictBasicValue xhbVerdictBasicValue2 = new XhbVerdictBasicValue(new Integer(2121),
                    "STUB: Verdict ObsInd2", "STUB: Verdict defOnChargeOrOffence2", new Integer(2122),
                    new Integer(2123), new Integer(2124), currentDate.getTime(), new Integer(2125), new Integer(2126),
                    "STUB: Verdict lastUpdatedBy2", "STUB: Verdict otherVerdictText2", "STUB: Verdict createdBy2",
                    currentDate.getTime(), currentDate.getTime(), new Integer(2127), new Integer(2128), new Integer(
                            2129), new Integer(2130), "STUB: Verdict appLesserOff2",
                    "STUB: Verdict altUncodedOffenceDesc2", new Integer(2131), new Integer(2132));

            XhbVerdictBasicValue xhbVerdictBasicValue3 = new XhbVerdictBasicValue(new Integer(3121),
                    "STUB: Verdict ObsInd3", "STUB: Verdict defOnChargeOrOffence3", new Integer(3122),
                    new Integer(3123), new Integer(3124), currentDate.getTime(), new Integer(3125), new Integer(3126),
                    "STUB: Verdict lastUpdatedBy3", "STUB: Verdict otherVerdictText3", "STUB: Verdict createdBy3",
                    currentDate.getTime(), currentDate.getTime(), new Integer(3127), new Integer(3128), new Integer(
                            3129), new Integer(3130), "STUB: Verdict appLesserOff3",
                    "STUB: Verdict altUncodedOffenceDesc3", new Integer(3131), new Integer(3132));

            VerdictSaveValue verdictSaveValue1 = new VerdictSaveValue(new VerdictValue(xhbVerdictBasicValue1),
                    "STUB: Verdict Operation1", new Integer(1111), new Integer(1222), "STUB: Verdict caseType1",
                    new Integer(1333), "STUB: Verdict caseSubType1", new Integer(1444), new Integer(1555), new Integer(
                            1666), "STUB: Verdict chargeType1", new Integer(1777), new Integer(1888),
                    new Integer(1999), "STUB: Verdict defendantName1", "STUB: Verdict offenceDescription1", false,
                    currentDate.getTime());

            VerdictSaveValue verdictSaveValue2 = new VerdictSaveValue(new VerdictValue(xhbVerdictBasicValue2),
                    "STUB: Verdict Operation2", new Integer(2111), new Integer(2222), "STUB: Verdict caseType2",
                    new Integer(2333), "STUB: Verdict caseSubType2", new Integer(2444), new Integer(2555), new Integer(
                            2666), "STUB: Verdict chargeType2", new Integer(2777), new Integer(2888),
                    new Integer(2999), "STUB: Verdict defendantName2", "STUB: Verdict offenceDescription2", false,
                    currentDate.getTime());

            VerdictSaveValue verdictSaveValue3 = new VerdictSaveValue(new VerdictValue(xhbVerdictBasicValue3),
                    "STUB: Verdict Operation3", new Integer(3111), new Integer(3222), "STUB: Verdict caseType3",
                    new Integer(3333), "STUB: Verdict caseSubType3", new Integer(3444), new Integer(3555), new Integer(
                            3666), "STUB: Verdict chargeType3", new Integer(3777), new Integer(3888),
                    new Integer(3999), "STUB: Verdict defendantName3", "STUB: Verdict offenceDescription3", false,
                    currentDate.getTime());

            VerdictMVO[] verdicts = new VerdictMVO[3];

            verdicts[0] = createVerdict(verdictSaveValue1);
            verdicts[1] = createVerdict(verdictSaveValue2);
            verdicts[2] = createVerdict(verdictSaveValue3);

            resultsMVO.setVerdictMVO(verdicts);

            resultsSequenceMVOArray = new ResultsSequenceMVO[2];

            ResultsSequenceMVO resultsSequenceMVO1 = new ResultsSequenceMVO();

            resultsSequenceMVO1.setDataIndex(0);
            resultsSequenceMVO1.setDataType("DataType resultsSequenceMVO1");
            resultsSequenceMVO1.setOraCode(0);
            resultsSequenceMVO1.setReturnCode(0);
            resultsSequenceMVO1.setTransType("setTransType resultsSequenceMVO1");
            resultsSequenceMVO1.setXhibitSequenceNo(0);

            ResultsSequenceMVO resultsSequenceMVO2 = new ResultsSequenceMVO();

            resultsSequenceMVO2.setDataIndex(0);
            resultsSequenceMVO2.setDataType("DataType resultsSequenceMVO2");
            resultsSequenceMVO2.setOraCode(0);
            resultsSequenceMVO2.setReturnCode(0);
            resultsSequenceMVO2.setTransType("setTransType resultsSequenceMVO2");
            resultsSequenceMVO2.setXhibitSequenceNo(0);

            resultsSequenceMVOArray[0] = resultsSequenceMVO1;
            resultsSequenceMVOArray[1] = resultsSequenceMVO2;

            resultsMVO.setResultsSequenceMVO(resultsSequenceMVOArray);

            log.debug("Stub: Setting the ResultOutput of mercatorResponse to resultsMVO");
            resultOutputData.setResultsMVO(resultsMVO);
            mercatorResponse.setResultOutputData(resultOutputData);
        } else {
            log.debug("Stub: Setting the ResultOutput of mercatorResponse to null");
            mercatorResponse.setResultOutputData(null);
        }

        if (!isExceptionTesting()) {
            log.debug("isExceptionTesting false in sendXmlMessage so return Response as normal");
        } else {
            if (getMercatorExceptionType() != null && getMercatorExceptionType().trim() != "") {
                String exceptionType = getMercatorExceptionType().trim();
                log.debug("isExceptionTesting true in sendXmlMessage, exceptionType: " + exceptionType);

                if (exceptionType.equals("IO")) {
                    log.debug("STUB Exception Test to throw IOException from runMap");
                    throw new IOException("STUB IOException");
                } else if (exceptionType.equals("EX")) {
                    log.debug("STUB Exception Test to throw RuntimeException from runMap");
                    throw new RuntimeException("STUB RuntimeException");
                } else {
                    try {
                        if (resultsSequenceMVOArray != null && resultsSequenceMVOArray.length > 1) {
                            log
                                    .debug("STUB Exception Test to set the returnCode of the second resultsSequenceMVO in ResultsMVO in ResultOutputData of MercatorResponse to non-zero in runMap");
                            int sequenceReturnCode = (new Integer(exceptionType)).intValue();

                            ResultsSequenceMVO resultsSequenceMVOSecondEntry = resultsSequenceMVOArray[1];
                            resultsSequenceMVOSecondEntry.setReturnCode(sequenceReturnCode);
                            resultsSequenceMVOArray[1] = resultsSequenceMVOSecondEntry;

                            resultsMVO.setResultsSequenceMVO(resultsSequenceMVOArray);
                            resultOutputData.setResultsMVO(resultsMVO);
                            mercatorResponse.setResultOutputData(resultOutputData);
                        }
                    } catch (Exception e) {
                        log.debug("Exception (probably NumberFormat) converting exceptionType to int: " + exceptionType
                                + e);
                    }
                }
            } else {
                log
                        .debug("STUB Exception Test to set mercatorReturnCode of the ReturnMVO in OutputData of MercatorResponse to non-zero in runMap");
                log
                        .debug("isExceptionTesting true in sendXmlMessage, ExceptionReturnCode: "
                                + getExceptionReturnCode());
                returnMVO.setMercatorReturnCode(getExceptionReturnCode());
                outputData.setReturnMVO(returnMVO);
                mercatorResponse.setOutputData(outputData);
            }
        }

        log.debug("<< ********** STUB: END createStubmercatorResponse ********** " + logicalMapName + " >>");

        return mercatorResponse;
    }

    private VerdictMVO createVerdict(VerdictSaveValue verdictSaveValue) {
        VerdictMVO verdictMVO = new VerdictMVO();

        verdictMVO.setAssJurors(verdictSaveValue.getJurorsAssenting().intValue());
        verdictMVO.setCaseNo(verdictSaveValue.getEntityCaseNumber().intValue());
        verdictMVO.setCaseType(verdictSaveValue.getEntityCaseType());
        verdictMVO.setCouId(verdictSaveValue.getCrestOffenceId().intValue());
        verdictMVO.setDateType(verdictSaveValue.getCrestDateType());
        verdictMVO.setDissJurors(verdictSaveValue.getJurorsDissenting().intValue());
        verdictMVO.setLessOffVerdict(verdictSaveValue.getCrestLessOffVerdict());
        verdictMVO.setLessOffVerdictDesc(verdictSaveValue.getCrestLessOffVerdictDesc());
        verdictMVO.setOtherVerdict(verdictSaveValue.getCrestOtherVerdict());
        verdictMVO.setSubId(verdictSaveValue.getCrestDefendantId().intValue());
        verdictMVO.setVerdict(verdictSaveValue.getCrestVerdict());
        verdictMVO.setVerdictDate(verdictSaveValue.getCrestVerdictDate());

        return verdictMVO;
    }

    /**
     * Method to validate the respond code from Mercator. Will call the
     * superclass method normally unless isExceptionTesting is enabled
     * 
     * @param rc
     *            the return code from mercator
     * @param returnValue
     *            ReturnMVO
     * @throws MercatorExecutionException
     * @throws MercatorValidationException
     */
    protected void respond(int rc, ReturnMVO returnValue) throws MercatorExecutionException,
            MercatorValidationException {
        log.debug("STUB isExceptionTesting: " + isExceptionTesting());

        if (!isExceptionTesting()) {
            log.debug("isExceptionTesting false in respond respond handled as normal");
            super.respond(rc, returnValue);
        } else {
            if (getMercatorExceptionType() != null && getMercatorExceptionType().trim() != "") {
                String exceptionType = getMercatorExceptionType();
                log.debug("isExceptionTesting true in respond, exceptionType: " + exceptionType);

                if (exceptionType.equals("ME")) {
                    log.debug("STUB Exception Test to throw MercatorExecutionException from respond");
                    throw new MercatorExecutionException(EXECUTION, "STUB MercatorExecutionException", returnValue);
                } else if (exceptionType.equals("MV")) {
                    log.debug("STUB Exception Test to throw MercatorValidationException from respond");
                    throw this.createValidationException(returnValue);
                } else if (exceptionType.equals("RX")) {
                    log.debug("STUB Exception Test to throw RuntimeException from respond");
                    throw new RuntimeException("STUB RuntimeException");
                }
            } else {
                log.debug("STUB Exception Test to set returnCode to non-zero in respond");
                log.debug("isExceptionTesting true in respond, ExceptionReturnCode: " + getExceptionReturnCode());
                super.respond(getExceptionReturnCode(), returnValue);
            }
        }
    }

    /**
     * Return true Stub is enabled to test exception handling
     */
    private boolean isExceptionTesting() {
        String property = System.getProperty(IS_MERCATOR_EXCEPTION_TESTING);
        return property != null && property.equalsIgnoreCase("TRUE");
    }

    /**
     * Returns the type of exception to be tested.
     * 
     * @return String
     */
    protected String getMercatorExceptionType() {
        String exceptionType = CSServices.getConfigServices().getProperty(MERCATOR_EXCEPTION_TYPE, "");

        log.debug("<< exceptionType: " + exceptionType + " >>");

        return exceptionType;
    }

    /**
     * Returns the number of retries that will be attempted to connect to
     * Mercator
     * 
     * @return int
     */
    private int getExceptionReturnCode() {
        int exceptionReturnCode = 0;

        try {
            exceptionReturnCode = new Integer(CSServices.getConfigServices().getProperty(
                    MERCATOR_EXCEPTION_RETURN_CODE, "0")).intValue();
        } catch (Exception e) {
            log.debug("<< ********** exceptionReturnCode lookup failed default to 0: " + e + " >>");
        }

        log.debug("<< ********** exceptionReturnCode: " + exceptionReturnCode + " >>");

        return exceptionReturnCode;
    }
}