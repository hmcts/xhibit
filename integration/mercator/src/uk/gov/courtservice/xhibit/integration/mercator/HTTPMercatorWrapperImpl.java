package uk.gov.courtservice.xhibit.integration.mercator;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;
import org.exolab.castor.types.Time;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.business.vos.CSValueMarker;
import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.InputData;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.MercatorRequest;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.MercatorResponse;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsSequenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ReturnMVO;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;
import uk.gov.courtservice.xhibit.integration.services.MercatorExecutionException;
import uk.gov.courtservice.xhibit.integration.services.MercatorValidationException;

/**
 * <p>
 * Title: HTTPMercatorWrapperImpl
 * </p>
 * <p>
 * Description: Passes the MVO to Mercator as XML over an HTTP connection to the
 * Mercator Web Service (aka Event Agent)
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

public class HTTPMercatorWrapperImpl extends MercatorWrapper {
    // Test and Error directory locations
    private static final String XML_TEST_FOLDER = "." + File.separatorChar + "mercator_xml_test";

    private static final String XML_ERROR_FOLDER = "." + File.separatorChar + "mercator_xml_error";

    // Map properties
    private static final String FORM_A_MAP_NAME = "exportHR";

    private static final String METHOD_NAME_PREFIX = "set";

    // Used for testing the fileCounter will be used when naming the test
    // XML files
    private static int fileCounter = 0;

    // Used for debug to count the number of recoonections attempted
    private static int reattemptedConnections = 0;

    // Used for debug to count the number of recoonections failed (including
    // failures of the initial connection)
    private static int reattemptedConnectionsFailed = 0;

    // Determines how many HTTP connection retries have been attempted for
    // this instance
    private int retryCounter = 0;

    // Used for performance stats so the elapsed time can be calculated for
    // a transaction
    private Calendar requestTime = null;

    // Used to determine if the connection has been made - if it has we
    // cannot attempt a reconnect
    private boolean isConnectionOk = false;

    // Tracks the URL used for the previous HTTP connection
    private String previousUrl = null;

    // the logger
    private static Logger log = CSServices.getLogger(HTTPMercatorWrapperImpl.class);

    /**
     * Method to set the required settings and send the map as XML to the
     * Mercator Event Agent over a HTTP connection.
     * 
     * @param logicalMapName
     *            String the name of the logical name used.
     * @param obj
     *            Object - this is any MercatorValueObject that will be passed
     *            in to the map.
     * @return Object extraInfo will be returned if successful
     * @throws MercatorException
     */
    public Object runMap(String logicalMapName, Object obj) throws MercatorException {
        isConnectionOk = false;

        log.debug("HTTPMercatorWrapperImpl Running Map:" + logicalMapName + "(" + getMapName(logicalMapName) + ")");

        if (!(obj instanceof CSValueMarker) && !(obj instanceof Integer)) {
            throw new CSConfigurationException(
                    "The Object passed to runMap is not of the expected type: CSValueMarker or Integer. Class is of type :"
                            + obj.getClass().toString() + " with vos :" + obj.toString());
        }

        if (obj instanceof Integer) {
            if (!logicalMapName.trim().equals(FORM_A_MAP_NAME)) {
                throw new CSConfigurationException(
                        "The Object passed to runMap is an integer but does not have the expected logicalMapName. The logicalMapName passed is :"
                                + logicalMapName);
            }
        }

        MercatorRequest mercatorRequest = null;
        MercatorResponse mercatorResponse = null;

        try {
            mercatorRequest = new MercatorRequest();

            String mapName = getMapName(logicalMapName);

            if (mapName.indexOf("/") != -1) {
                // chop off package name ("/software/mercator/eds/Maps/") as
                // requested by Mercator just leaving the mmc name
                mapName = mapName.substring(mapName.lastIndexOf("/") + 1);
            }

            log.debug("Mercator mapName:" + mapName);

            mercatorRequest.setMapName(mapName);

            mercatorRequest.setInputCardNbr(getInputCard(logicalMapName));
            mercatorRequest.setOutputCardNbr(getOutputCard(logicalMapName));
            mercatorRequest.setResultOutputCardNbr(getResultOutputCard(logicalMapName));

            Calendar currentTime = Calendar.getInstance();

            mercatorRequest.setMsgDate(new Date(currentTime.getTime()));
            mercatorRequest.setMsgTime(new Time(getTime(currentTime)));

            InputData inputData = new InputData();

            if (obj instanceof CSValueMarker) {
                execute(obj, determineInputDataMethodName(obj), inputData);
            } else if (obj instanceof Integer) {
                // Must be an Integer due to configuration check at start and we
                // only have one transaction taking an int: FormA
                log.debug("InputData on MercatorRequest setFormA set to: " + ((Integer) obj).intValue());
                inputData.setFormA(((Integer) obj).intValue());
            }

            mercatorRequest.setInputData(inputData);

            inputLocalLogging(logicalMapName, mercatorRequest, false, null);

            mercatorResponse = sendXmlMessage(logicalMapName, mercatorRequest);

            outputLocalLogging(logicalMapName, mercatorResponse, false, null);

            if (MercatorWrapperFactory.IS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGED) {
                int returnCodeOfResultsSequenceMVO = examineResultsSequenceMVO(mercatorResponse);
                if (returnCodeOfResultsSequenceMVO != 0) {
                    log.debug("returnCodeOfResultsSequenceMVO: " + returnCodeOfResultsSequenceMVO);

                    // Mercator business exception in setResults
                    // These are not thrown as exceptions here as they are
                    // converted to an appropriate display message higher
                    // up, however we can log them here
                    String errorMessage = "Mercator business exception in setResults, returnCode: "
                            + (new Integer(returnCodeOfResultsSequenceMVO)).toString();
                    handleMercatorException(logicalMapName, mercatorRequest, mercatorResponse, new Exception(
                            errorMessage));
                }
            }

            return examineReturnValue(mercatorResponse);
        } catch (MercatorValidationException mve) {
            // examineReturnValue failed.
            handleMercatorException(logicalMapName, mercatorRequest, mercatorResponse, mve);

            throw mve;
        } catch (MercatorExecutionException mee) {
            // examineReturnValue failed
            handleMercatorException(logicalMapName, mercatorRequest, mercatorResponse, mee);

            throw mee;
        } catch (IOException ie) {
            // sendXmlMessage failed
            handleMercatorException(logicalMapName, mercatorRequest, mercatorResponse, ie);

            throw new CSUnrecoverableException("IOException Error Accessing Mercator server.", ie);
        } catch (Exception e) {
            // this is an unexpected exception, not a business exception
            handleMercatorException(logicalMapName, mercatorRequest, mercatorResponse, e);

            throw new CSUnrecoverableException("Unexpected Exception Accessing Mercator server.", e);
        }
    }

    /**
     * Determnines the method name we need to executed on InputData of the
     * MercatorRequest to set the payload
     * 
     * @param Object -
     *            will be passed in to the map.
     * @return String - the method name
     */
    public String determineInputDataMethodName(Object obj) {
        String methodName = null;

        String className = obj.getClass().getName();
        int lastDot = className.lastIndexOf(".");
        methodName = METHOD_NAME_PREFIX + className.substring(lastDot + 1);

        log.debug("InputData on MercatorRequest payload methodName is: " + methodName);

        return methodName;
    }

    /**
     * Send the XML message to the Mercator Agent and returning the XML
     * response.
     * 
     * @param MercatorRequest
     *            xml: input xml message to send to the Mercator Agent over HTTP
     * @param logicalMapName:
     *            map name of the transaction
     * @return MercatorResponse This will be the output xml message read from
     *         the Mercator Agent over HTTP
     * @throws IOException
     *             failed to send message
     */
    public MercatorResponse sendXmlMessage(String logicalMapName, MercatorRequest xml) throws IOException {
        URL url = null;
        MercatorResponse mercatorResponse = null;

        try {
            // first time thru the previousUrl will be null
            String urlString = getMercatorAgentUrl(previousUrl);
            url = new URL(urlString);
            previousUrl = urlString;
            // If we get a problem in here then the previousUrl will be
            // passed into
            // getMercatorAgentUrl on the retry and will be ignored when
            // determining the next URL
            mercatorResponse = createHttpConnection(url, xml);
            // Reset to null if a successful response returns.
            previousUrl = null;
        } catch (java.net.SocketTimeoutException stex) {
            // JDK 1.5 to deal with SocketTimeouts
            mercatorResponse = doHttpReconnect(logicalMapName, xml, stex);
        } catch (IOException ioex) {
            // HTTP connection failed
            mercatorResponse = doHttpReconnect(logicalMapName, xml, ioex);
        } catch (ValidationException ve) {
            // XML not valid (could be request or response)
            mercatorResponse = doHttpReconnect(logicalMapName, xml, ve);
        } catch (MarshalException me) {
            // XML marshall (request) or unmarshall (response) failed
            mercatorResponse = doHttpReconnect(logicalMapName, xml, me);
        } catch (Exception ex) {
            // Unexpected exception
            mercatorResponse = doHttpReconnect(logicalMapName, xml, ex);
        }

        return mercatorResponse;
    }

    /**
     * Handles HTTP retries
     * 
     * Although this appears to be a cyclic call between this method and
     * sendXmlMessage if sendXmlMessage fails again and handleException is
     * called for a second time where no more reconnects are to be attempted the
     * exception will be logged and an IOException thrown. This will be thrown
     * straight back by the original invocation of handleException without
     * performing further logging
     * 
     * @param URL
     *            url to the Mercator Agent
     * @param MercatorRequest
     *            xml
     * @return MercatorResponse read from the HTTP connection from the Mercator
     *         Agent
     * @throws IOException
     */
    private MercatorResponse doHttpReconnect(String logicalMapName, MercatorRequest xml, Exception e)
            throws IOException {
        MercatorResponse mercatorResponse = null;

        if (!isConnectionOk && retryCounter < MercatorWrapperFactory.MERCATOR_CONNECTION_RETRY_NBR) {
            retryCounter++;
            reattemptedConnections++;
            log.debug("Retry HTTP connection to mercator following failure, attempt number: " + retryCounter);
            mercatorResponse = sendXmlMessage(logicalMapName, xml);
            retryCounter = 0;
            log.debug("Successful HTTP connection to mercator following failure, retry reset: " + retryCounter);
        } else {
            reattemptedConnectionsFailed++;

            log
                    .debug("HTTP re-connection exceeded retryCounter or was not attempted as failure was after a successul conection."
                            + " ***** logicalMapName was: "
                            + logicalMapName
                            + " ***** retryCounter was: "
                            + retryCounter
                            + " ***** weblogic instance failure occured on was: "
                            + System.getProperty("weblogic.Name")
                            + " ***** Overall (for this weblogic instance) reattemptedConnections was: "
                            + reattemptedConnections
                            + " ***** Overall (for this weblogic instance) reattemptedConnectionsFailed was: "
                            + reattemptedConnectionsFailed + " ***** Connection Exception was: " + e.getMessage());

            logMercatorException(e);

            throw new IOException(e.getMessage());
        }

        return mercatorResponse;
    }

    /**
     * Performs a HTTP POST request of the XML message to the Mercator Agent and
     * reads data from the Mercator Agent
     * 
     * This converts the MercatorRequest to XML using Castor which is then
     * written to the OutputStream of the HTTP connection The MercatorResponse
     * object can be generated using Castor from the XML read from the
     * InputStream from the same HTTP connection
     * 
     * @param URL
     *            url to the Mercator Agent
     * @param MercatorRequest
     *            xml
     * @return MercatorResponse read from the HTTP connection from the Mercator
     *         Agent
     * @throws IOException,
     *             ValidationException, MarshalException
     */
    private MercatorResponse createHttpConnection(URL url, MercatorRequest xml)
            throws IOException, ValidationException, MarshalException, java.net.SocketTimeoutException
    {
        HttpURLConnection conn = null;
        MercatorResponse mercatorResponse = null;

        try {
            log.debug("create connection");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/XML; charset=" + MercatorWrapperFactory.MERCATOR_XML_ENCODING);
            // JDK 1.5 Timeout Functionality
            // This will timeout the connection after a specified period,
            // default is 0 for infinite timeout
            conn.setConnectTimeout(MercatorWrapperFactory.MERCATOR_CONNECTION_TIMEOUT_VALUE);

            // timeout on the InputStream read from Mercator, default is 0
            // for infinite timeout
            conn.setReadTimeout(MercatorWrapperFactory.MERCATOR_CONNECTION_TIMEOUT_VALUE);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);

            conn.connect();
            isConnectionOk = true;

            log.debug("Write XML using CASTOR");
            marshal(conn.getOutputStream(), xml);
            log.debug("Finished writing XML using CASTOR.");
            
            log.debug("Read XML using CASTOR");
            
            // if (log.isDebugEnabled()) {
            if (false) {
                // Copy all the input to a string and log it before attempting to unmarshal.
                // Broker WTX now seems to return error messages in plain text, not XML
                // formatted.  When this happens all we get is an 'unmarshal' error and
                // the underlying error message is lost.  Turn this on to capture the Broker
                // output before unmarshaling.  This is not turned on by default because it
                // has performance implications.
                InputStream inputStream = conn.getInputStream();
                StringBuffer input = new StringBuffer();
                byte[] buffer = new byte[100];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    for (int i = 0; i < bytesRead; i++) {
                        input.append((char)buffer[i]);
                    }
                }

                log.debug("Return value: " + input.toString());
                
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.toString().getBytes());
                mercatorResponse = (MercatorResponse) unmarshal(byteArrayInputStream, MercatorResponse.class);
            } else {
                mercatorResponse = (MercatorResponse) unmarshal(conn.getInputStream(), MercatorResponse.class);
            }
            log.debug("Finished reading XML using CASTOR.");
        } finally {
            try {
                if (conn != null) {
                    log.debug("Disconnect Connection");
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.debug("Failed to Disconnect Connection: " + e);
            }
        }

        log.debug("Return MercatorResponse object read from InputStream of HTTP connection to Mercator");
        return mercatorResponse;
    }

    /**
     * Method to examine the returned XML read in from the HTTP connection to
     * Mercator.
     * 
     * @param MercatorResponse
     * @return Object
     * @throws MercatorExecutionException
     * @throws MercatorValidationException
     */
    private Object examineReturnValue(MercatorResponse mercatorResponse) throws MercatorExecutionException,
            MercatorValidationException {
        log.debug("examineReturnValue entered (MercatorResponse)");

        try {
            // OutputData is a mandatory return from Mercator and so won't
            // be null
            ReturnMVO returnMVO = mercatorResponse.getOutputData().getReturnMVO();

            respond(returnMVO.getMercatorReturnCode(), returnMVO);

            // ResultOutputData is optional and you will never get more than
            // one of the objects at any one time
            if (mercatorResponse.getResultOutputData() != null) {
                if (mercatorResponse.getResultOutputData().getResultsMVO() != null) {
                    return mercatorResponse.getResultOutputData().getResultsMVO();
                } else if (mercatorResponse.getResultOutputData().getAddCaseMVO() != null) {
                    return mercatorResponse.getResultOutputData().getAddCaseMVO();
                } else if (mercatorResponse.getResultOutputData().getPrimaryKeysMVO() != null) {
                    return mercatorResponse.getResultOutputData().getPrimaryKeysMVO();
                } else {
                    if (returnMVO.getExtraInfo() != null) {
                        log.debug("ExtraInfo returned from Mercator: " + returnMVO.getExtraInfo());
                        return returnMVO.getExtraInfo();
                    } else {
                        log.debug("No ExtraInfo returned from Mercator so return an empty String");
                        return new String("");
                    }
                }
            } else {
                if (returnMVO.getExtraInfo() != null) {
                    log.debug("ExtraInfo returned from Mercator: " + returnMVO.getExtraInfo());
                    return returnMVO.getExtraInfo();
                } else {
                    log.debug("No ExtraInfo returned from Mercator so return an empty String");
                    return new String("");
                }
            }
        } catch (MercatorValidationException mve) {
            throw mve;
        } catch (MercatorExecutionException mee) {
            throw mee;
        } catch (Exception e) {
            logMercatorException(e);
            throw new CSConfigurationException("Unable to read return value from Mercator", e);
        }
    }

    /**
     * ERROR HANDLING METHODS
     */

    /**
     * Logs exceptions from Mercator transactions to midtier error log.
     * 
     * @param Throwable
     */
    private void logMercatorException(Throwable t) {
        CSServices.getDefaultErrorHandler().handleError(t, HTTPMercatorWrapperImpl.class);
    }

    /**
     * Logs failures of mercator transactions.
     * 
     * @param logicalMapName
     * @param MercatorRequest
     * @return MercatorResponse
     */
    public void handleMercatorException(String logicalMapName, MercatorRequest request, MercatorResponse response,
            Exception e) {
        try {
            logMercatorException(e);
            inputLocalLogging(logicalMapName, request, true, createStackTraceList(e));
            outputLocalLogging(logicalMapName, response, true, createStackTraceList(e));
        } catch (Exception ee) {
            logMercatorException(ee);
        }
    }

    /**
     * Puts the stack trace of an Exception into an ArrayList.
     * 
     * @param Exception
     * @return List
     */
    private List createStackTraceList(Exception e) {
        List<String> frames = null;
        ListWriter out = null;

        try {
            frames = new ArrayList<String>();
            out = new ListWriter(frames);

            e.printStackTrace(out);
        } catch (Exception ex) {
            log.debug("<< ********** Exception message in createStackTraceList: ********** " + ex.getMessage() + " >>");
            logMercatorException(ex);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch (Exception exc) {
                log.debug("<< ********** Failed to close ListWriter in createStackTraceList ********** >>");
                logMercatorException(exc);
            }
        }

        return frames;
    }

    /**
     * Inner class to manage an ArrayList in a PrintWriter.
     */
    private static class ListWriter extends PrintWriter {
        private List<String> lines = new ArrayList<String>();

        public ListWriter() {
            super(new NullWriter());
        }

        public ListWriter(List<String> lines) {
            super(new NullWriter());
            this.lines = lines;
        }

        public List getList() {
            return lines;
        }

        public void println(Object o) {
            lines.add(o.toString());
        }

        public void println(char[] s) {
            lines.add(new String(s));
        }

        public void println(String s) {
            lines.add(s);
        }
    }

    /**
     * Inner class to manage a NullWriter.
     */
    private static class NullWriter extends Writer {
        public void close() {
            // emtpy
        }

        public void flush() {
            // empty
        }

        public void write(char[] cbuf, int off, int len) {
            // empty
        }
    }

    /**
     * Method to examine the ResultsSequenceMVOs of the ResultsMVO which will
     * return a non-zero value if any of the ResultsSequenceMVOs return codes
     * are non zero which will be logged to file as an exception
     * 
     * @param MercatorResponse
     * @return int representing the return code of the resultsSequenceMVO
     */
    private int examineResultsSequenceMVO(MercatorResponse mercatorResponse) {
        log.debug("examineResultsSequenceMVO entered");

        int returnCode = 0;
        ResultsMVO resultsMVO = null;
        ResultsSequenceMVO[] resultsSequenceMVOArray = null;
        ResultsSequenceMVO resultsSequenceMVOEntry = null;

        try {
            if (mercatorResponse != null && mercatorResponse.getResultOutputData() != null
                    && mercatorResponse.getResultOutputData().getResultsMVO() != null) {
                resultsMVO = mercatorResponse.getResultOutputData().getResultsMVO();

                if (resultsMVO != null) {
                    resultsSequenceMVOArray = resultsMVO.getResultsSequenceMVO();

                    if (resultsSequenceMVOArray != null && resultsSequenceMVOArray.length != 0) {
                        for (int i = 0; i < resultsSequenceMVOArray.length; i++) {
                            resultsSequenceMVOEntry = resultsSequenceMVOArray[i];
                            returnCode = 0;

                            if (resultsSequenceMVOEntry.hasReturnCode()) {
                                returnCode = resultsSequenceMVOEntry.getReturnCode();
                            }

                            if (returnCode != 0) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log the exception but don't propagate as this is only logging
            logMercatorException(e);
        }

        log.debug("ResultsSequence returnCode is:" + returnCode);
        return returnCode;
    }

    /**
     * LOGGING (DEBUGGING) METHODS
     */

    /**
     * Method to log input data locally to a log or/and file This will be done
     * for testing on when an error occurs
     * 
     * @param logicalMapName
     * @param MercatorRequest
     * @param isError
     * @param errorMessage
     */
    private void inputLocalLogging(String logicalMapName, MercatorRequest mercatorRequest, boolean isError,
            List errorMessage) {
        OutputStream outputStream = null;

        String directoryName = null;
        String fileName = null;
        File integrationFile = null;
        File file = null;
        BufferedWriter bw = null;

        String errorString = null;
        String volumeString = null;
        int size = 0;

        log.debug("<< ********** START Mercator Request XML Logging for : " + logicalMapName + " where isError: "
                + isError + " ********** >>");

        try {
            Calendar calendar = Calendar.getInstance();

            // We'll already have the requestTime if an error has occurred
            // so don't reset it!
            if (!isError || requestTime == null) {
                requestTime = calendar;
            }

            if (isError && errorMessage != null && !errorMessage.isEmpty()) {
                errorString = "<ErrorData errorMessage=\"";

                for (int i = 0; i < errorMessage.size(); i++) {
                    errorString = errorString + errorMessage.get(i);
                }

                errorString = errorString + "\" />";

                log.debug("<< Error on MercatorRequest is >>" + errorString);
            }

            if (mercatorRequest != null && MercatorWrapperFactory.IS_MERCATOR_VOLUME_STATS_LOGGED) {
                size = MercatorTransactionStatistics.getSizeOf(mercatorRequest);
                volumeString = "<VolumeData size=\"" + size + "\" />";
                log.debug("<< Size of MercatorRequest is >>" + volumeString);
            }

            if (log.isDebugEnabled() || isError) {
                if (MercatorWrapperFactory.IS_MERCATOR_TO_DEBUG_LOG && mercatorRequest != null) {
                    if (size != 0) {
                        outputStream = new ByteArrayOutputStream(size);
                    } else {
                        outputStream = new ByteArrayOutputStream();
                    }
                    marshal(outputStream, mercatorRequest);
                    log.debug(outputStream);
                }

                if (MercatorWrapperFactory.IS_MERCATOR_TO_XML_FILE || isError) {
                    fileCounter++;

                    if (isError) {
                        directoryName = XML_ERROR_FOLDER;
                        fileName = getXmlFileName(logicalMapName + "Request_Error", calendar);
                    } else {
                        directoryName = XML_TEST_FOLDER;
                        fileName = getXmlFileName(logicalMapName + "Request", calendar);
                    }

                    integrationFile = createXmlDirectory(directoryName, calendar);

                    if (mercatorRequest != null) {
                        file = new File(integrationFile, fileName);
                        log.debug("<< Write Mercator Request to XML file: >>" + fileName);

                        OutputStream out;
                        if (size != 0) {
                        	out = new BufferedOutputStream(new FileOutputStream(file), size);
                        } else {
                        	out = new FileOutputStream(file);
                        }                        
                        try {
                        	marshal(out, mercatorRequest);
                        } finally {
                        	out.close();		
                        }
                    }

                    if (volumeString != null || errorString != null) {
                        fileName = getXmlFileName(logicalMapName + "RequestStatsAndErrors", calendar);
                        file = new File(integrationFile, fileName);
                        log.debug("<< Write Mercator Request Stats and Errors to XML file: >>" + fileName);
                        bw = new BufferedWriter(new FileWriter(file));

                        bw
                                .write("<MercatorRequestStatsAndErrors xmlns=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/mercator\"><GenericData logicalMapName=\""
                                        + logicalMapName
                                        + "\" timeStamp=\""
                                        + calendar.getTime().toString()
                                        + "\" weblogicInstance=\""
                                        + System.getProperty("weblogic.Name")
                                        + "\" reattemptedConnections=\""
                                        + reattemptedConnections
                                        + "\" reattemptedConnectionsFailed=\"" + reattemptedConnectionsFailed + "\" />");

                        if (volumeString != null) {
                            bw.write(volumeString);
                        }

                        if (errorString != null) {
                            bw.write(errorString);
                        }

                        bw.write("</MercatorRequestStatsAndErrors>");
                    }

                    log.debug("<< Completed Writing XML message to fileOS >>");
                }

                log.debug("<< ********** END Mercator Request XML logging for: " + logicalMapName + " ********** >>");
            }
        } catch (Exception e) {
            // Log exception and suppress
            log.debug("<< ********** Start Exception in inputLocalLogging of Mercator Request for : " + logicalMapName
                    + " ********** >>");
            log.debug("<< ********** Exception message is: ********** " + e.getMessage() + " >>");
            logMercatorException(e);
            log
                    .debug("<< ********** End Exception in inputLocalLogging of Mercator Request, Exception suppressed ********** >>");
        } finally {

            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            } catch (Exception e) {
                log
                        .debug("<< ********** Failed to close outputStream in inputLocalLogging of Mercator Request ********** >>");
                logMercatorException(e);
            }

            try {
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
            } catch (Exception e) {
                log
                        .debug("<< ********** Failed to close BufferedWriter in inputLocalLogging of Mercator Request ********** >>");
                logMercatorException(e);
            }
        }
    }

    /**
     * Method to log output data locally to a log or/and file This will be done
     * for testing on when an error occurs
     * 
     * @param logicalMapName
     * @param mercatorResponse
     * @param isError
     * @param errorMessage
     */
    private void outputLocalLogging(String logicalMapName, MercatorResponse mercatorResponse, boolean isError,
            List errorMessage) {
        OutputStream outputStream = null;

        String directoryName = null;
        String fileName = null;
        File integrationFile = null;
        File file = null;
        BufferedWriter bw = null;

        String elapsedTimeString = null;
        String errorString = null;
        String volumeString = null;
        int size = 0;

        log.debug("<< ********** START Mercator Response XML Logging for: " + logicalMapName + " isError: " + isError
                + " ********** >>");

        try {
            Calendar responseTime = Calendar.getInstance();

            if (MercatorWrapperFactory.IS_MERCATOR_PERF_STATS_LOGGED || isError) {
                elapsedTimeString = "<ElapsedTimeData elapsedTime=\""
                        + MercatorTransactionStatistics.getElapsedTime(requestTime, responseTime) + "\" requestTime=\""
                        + requestTime.getTime().toString() + "\" responseTime=\"" + responseTime.getTime().toString()
                        + "\" />";
                log.debug("<< ElapsedTime of MercatorTransaction is >>" + elapsedTimeString);
            }

            if (isError && errorMessage != null && !errorMessage.isEmpty()) {
                errorString = "<ErrorData errorMessage=\"";

                for (int i = 0; i < errorMessage.size(); i++) {
                    errorString = errorString + errorMessage.get(i);
                }

                errorString = errorString + "\" />";

                log.debug("<< Error on MercatorRequest is >>" + errorString);
            }

            if (mercatorResponse != null && MercatorWrapperFactory.IS_MERCATOR_VOLUME_STATS_LOGGED) {
                size = MercatorTransactionStatistics.getSizeOf(mercatorResponse);
                volumeString = "<VolumeData size=\"" + size + "\" />";
                log.debug("<< Size of MercatorResponse is >>" + volumeString);
            }

            if (log.isDebugEnabled() || isError) {
                if (MercatorWrapperFactory.IS_MERCATOR_TO_DEBUG_LOG && mercatorResponse != null) {
                    if (size != 0) {
                        outputStream = new ByteArrayOutputStream(size);
                    } else {
                        outputStream = new ByteArrayOutputStream();
                    }
                    marshal(outputStream, mercatorResponse);
                    log.debug(outputStream);
                }

                if (MercatorWrapperFactory.IS_MERCATOR_TO_XML_FILE || isError) {
                    // Don't advance fileCounter so we can match requests to
                    // responses!

                    if (isError) {
                        directoryName = XML_ERROR_FOLDER;
                        fileName = getXmlFileName(logicalMapName + "Response_Error", responseTime);
                    } else {
                        directoryName = XML_TEST_FOLDER;
                        fileName = getXmlFileName(logicalMapName + "Response", responseTime);
                    }

                    integrationFile = createXmlDirectory(directoryName, responseTime);

                    if (mercatorResponse != null) {
                        file = new File(integrationFile, fileName);
                        log.debug("<< Write Mercator Response to XML file: >>" + fileName);

                        OutputStream out;
                        if (size != 0) {
                            out = new BufferedOutputStream(new FileOutputStream(file), size);                            
                        } else {
                        	out = new FileOutputStream(file);
                        }
                        try {
                        	marshal(out, mercatorResponse);	
                        } finally {
                        	out.close();
                        }                        
                    }

                    if (volumeString != null || elapsedTimeString != null || errorString != null) {
                        fileName = getXmlFileName(logicalMapName + "ResponseStatsAndErrors", responseTime);
                        file = new File(integrationFile, fileName);
                        log.debug("<< Write Mercator Response Stats and Errors to XML file: >>" + fileName);
                        bw = new BufferedWriter(new FileWriter(file));

                        bw
                                .write("<MercatorResponseStatsAndErrors xmlns=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/mercator\"><GenericData logicalMapName=\""
                                        + logicalMapName
                                        + "\" timeStamp=\""
                                        + responseTime.getTime().toString()
                                        + "\" weblogicInstance=\""
                                        + System.getProperty("weblogic.Name")
                                        + "\" reattemptedConnections=\""
                                        + reattemptedConnections
                                        + "\" reattemptedConnectionsFailed=\"" + reattemptedConnectionsFailed + "\" />");

                        if (elapsedTimeString != null) {
                            bw.write(elapsedTimeString);
                        }

                        if (volumeString != null) {
                            bw.write(volumeString);
                        }

                        if (errorString != null) {
                            bw.write(errorString);
                        }

                        bw.write("</MercatorResponseStatsAndErrors>");
                    }

                    log.debug("<< Completed Writing XML message to fileOS >>");
                }

                log.debug("<< ********** END Mercator Response XML Logging for : " + logicalMapName + " ********** >>");
            }
        } catch (Exception e) {
            // Log exception and suppress
            log.debug("<< ********** Start Exception in outputLocalLogging of Mercator Response for : "
                    + logicalMapName + " ********** >>");
            log.debug("<< ********** Exception message is: ********** " + e.getMessage() + " >>");
            logMercatorException(e);
            log
                    .debug("<< ********** End Exception in outputLocalLogging of Mercator Response, Exception suppressed ********** >>");
        } finally {

            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }
            } catch (Exception e) {
                log
                        .debug("<< ********** Failed to close outputStream in outputLocalLogging of Mercator Response ********** >>");
                logMercatorException(e);
            }

            try {
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
            } catch (Exception e) {
                log
                        .debug("<< ********** Failed to close BufferedWriter in outputLocalLogging of Mercator Response ********** >>");
                logMercatorException(e);
            }
        }
    }

    /**
     * HELPER METHODS
     */

    /**
     * <p>
     * Title: getMercatorAgentUrl
     * </p>
     * <p>
     * Description: Delegates to the MercatorWrapperFactory to retrieve the HTTP
     * URL
     * </p>
     * 
     * @param String
     *            failedUrl represents a URL already tried (this is optional and
     *            if null will indicate this the first attempt at a connection)
     * @return String representing the Mercator Agent URL
     */
    private static String getMercatorAgentUrl(String failedUrl) {
        return MercatorWrapperFactory.getMercatorAgentUrl(failedUrl);
    }

    /**
     * <p>
     * Title: createXmlDirectory
     * </p>
     * <p>
     * Description: Creates dated directory to put XML files in for testing or
     * errors
     * </p>
     * 
     * @param directoryName
     * @param calendar
     *            (current time)
     * @return File
     */
    private File createXmlDirectory(String directoryName, Calendar calendar) {
        File logDir = new File(System.getProperty("log4j.log.dir"));

        File directory = null;

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        // Note: MONTH starts with January at 0 we need to add 1
        String dayOfMonth = new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString();
        String month = new Integer(calendar.get(Calendar.MONTH) + 1).toString();
        String year = new Integer(calendar.get(Calendar.YEAR)).toString();

        if (dayOfMonth.length() == 1) {
            dayOfMonth = "0" + dayOfMonth;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }

        directoryName = directoryName + "_" + System.getProperty("weblogic.Name") + "_" + dayOfMonth + "-" + month
                + "-" + year;

        directory = new File(logDir, directoryName);

        if (!directory.exists()) {
            log.debug("<< Creating directory >>: " + directoryName);
            boolean isDirCreated = directory.mkdir();
            if (!isDirCreated) {
                log.debug("<< Directory not created >>: " + directoryName);
            }
        }

        return directory;
    }

    /**
     * Returns a filename appended with the time and a counter
     * 
     * @return String
     */
    private String getXmlFileName(String name, Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        String hourOfDay = new Integer(calendar.get(Calendar.HOUR_OF_DAY)).toString();
        String minute = new Integer(calendar.get(Calendar.MINUTE)).toString();
        String second = new Integer(calendar.get(Calendar.SECOND)).toString();
        String milliSecond = new Integer(calendar.get(Calendar.MILLISECOND)).toString();

        if (hourOfDay.length() == 1) {
            hourOfDay = "0" + hourOfDay;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        if (milliSecond.length() == 1) {
            milliSecond = "00" + milliSecond;
        }
        if (milliSecond.length() == 2) {
            milliSecond = "0" + milliSecond;
        }

        String fileName = name + "_" + hourOfDay + "-" + minute + "-" + second + "-" + milliSecond + "_"
                + new Integer(fileCounter).toString() + ".xml";

        return fileName;
    }

    /**
     * Method to retrieve the Time in a format suitable for
     * org.exolab.castor.types.Time (hh:mm:ss.mmm)
     * 
     * @param calendar
     * @return String
     */
    protected String getTime(Calendar currentTime) {
        String hh = new Integer(currentTime.get(Calendar.HOUR_OF_DAY)).toString();
        if (hh.length() == 1) {
            hh = "0" + hh;
        }

        String mm = new Integer(currentTime.get(Calendar.MINUTE)).toString();
        if (mm.length() == 1) {
            mm = "0" + mm;
        }

        String ss = new Integer(currentTime.get(Calendar.SECOND)).toString();
        if (ss.length() == 1) {
            ss = "0" + ss;
        }

        String mmm = new Integer(currentTime.get(Calendar.MILLISECOND)).toString();
        if (mmm.length() == 1) {
            mmm = "00" + mmm;
        }
        if (mmm.length() == 2) {
            mmm = "0" + mmm;
        }

        String time = hh + ":" + mm + ":" + ss + "." + mmm;

        log.debug("time:" + time);

        return time;
    }

    protected Object unmarshal(final InputStream in, final Class clazz) throws IOException, MarshalException, ValidationException {
    	return unmarshal(in, clazz, MercatorWrapperFactory.MERCATOR_XML_ENCODING);    	
    }

    protected Object unmarshal(final InputStream in, Class clazz, final String encoding) throws IOException, MarshalException, ValidationException {
        Reader reader = new InputStreamReader(in, encoding);
        Unmarshaller unmarshaller = new Unmarshaller(clazz);
        return unmarshaller.unmarshal(reader);
    }
    
    protected void marshal(final OutputStream out, final Object obj) throws IOException,MarshalException, ValidationException  {
    	marshal(out, obj, MercatorWrapperFactory.MERCATOR_XML_ENCODING);
    }
    
    protected void marshal(final OutputStream out, final Object obj, final String encoding) throws IOException, MarshalException, ValidationException {
    	Writer writer = new OutputStreamWriter(out, encoding);
    	Marshaller marshaller = new Marshaller(writer);
    	marshaller.setEncoding(encoding);
      	marshaller.marshal(obj);
      	writer.flush();
    }
}