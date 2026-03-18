package uk.gov.courtservice.xhibit.business.services.charge;

// third party

import java.util.Properties;

import javax.ejb.ObjectNotFoundException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtHome;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CrnValue;

/**
 * <p>
 * Title: CrnHelper
 * </p>
 * <p>
 * Description: Generates a CRN based on the year, courtcode, system code,
 * database generated sequence number and a fixed sequence number
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: CrnHelper.java,v 1.6 2006/06/05 12:29:33 bzjrnl Exp $
 */

public class CrnHelper {

    private static String SQL = "SELECT XHB_CRN_SEQ.NEXTVAL FROM DUAL";

    private Logger log = CSServices.getLogger(CrnHelper.class);

    /**
     * Generates a CRN based on the year, courtcode, system code, database
     * generated sequence number and a fixed sequence number
     * 
     * @param courtId
     * @return
     * @throws ChargeControllerException
     */
    public CrnValue[] getNextCrnBatch(Integer courtId, int batchSize) throws ChargeControllerException {
        String methodName = "getNextCrn(Integer courtId):";
        log.debug(methodName + " entered, courtId = " + courtId);

        // get ff from court table
        String ffuu = getFfuu(courtId);

        // get ss and seqNo
        Properties prop = getCrnProperties();
        String ss = prop.getProperty("SS");
        String seqNo = prop.getProperty("SEQ_NO");

        try {
            // create crnValue batch and populate using a different
            // generated sequence number for each
            CrnValue[] crnValueBatch = new CrnValue[batchSize];

            for (int i = 0; i < batchSize; i++) {
                // create valid CRN
                crnValueBatch[i] = CrnValue.newInstance(ffuu, ss, getNextGeneratedSeqNo(), seqNo);
            }

            log.debug(methodName + " exited.");
            return crnValueBatch;
        } catch (InstantiationException ex) {
            throw new ChargeControllerException("charge.couldnotcreateCRN", "Could not instantiate CRN", ex);
        }
    }

    /**
     * Get generated sequence no from database
     * 
     * @return generated sequence no.
     */
    private long getNextGeneratedSeqNo() {
        String methodName = "getNextGeneratedSeqNo(): ";
        log.debug(methodName + "entered, SQL = " + SQL);

        try {
            DataSource ds = CSServices.getServiceLocator().getDataSource();
            CrnRowProcessor processor = new CrnRowProcessor();
            QueryOperation queryOp = new QueryOperation(ds, SQL);
            queryOp.setRowProcessor(processor);
            queryOp.execute(new Object[] {});

            long sqNo = processor.getGeneratedSeqNo();

            log.debug("generatedSeqNo=" + sqNo);
            log.debug(methodName + " exited.");
            return sqNo;

        } catch (DataAccessException ex) {
            log.error("Could not generate next value for sequence using: " + CrnHelper.SQL);
            throw new CSConfigurationException("Could not generate next value for sequence using: " + CrnHelper.SQL, ex);
        }

    }

    private class CrnRowProcessor extends AbstractRowProcessor {

        private long generatedSeqNo;

        public void processRow(Row row) {
            generatedSeqNo = row.getLong("NEXTVAL");
        }

        public long getGeneratedSeqNo() {
            return generatedSeqNo;
        }
    }

    private String getFfuu(Integer courtId) throws ChargeControllerException {
        String methodName = "getFfuu(Integer courtId) :";
        log.debug(methodName + "entered, courtId = " + courtId);

        Court court = null;
        try {
            court = (Court) CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtHome.class, courtId);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new ChargeControllerException("charge.courtnotfound", new Object[] { courtId },
                    "Could not find court with courtId " + courtId, ex);
        }
        String ffuu = court.getCourtCode();

        if (ffuu == null || ffuu.length() != CrnValue.FFUU_LENGTH) {
            log.error("Court Code not set for courtId: " + courtId + " Code code must be exactly 4 characters");
            throw new CSConfigurationException("Court Code not set for courtId: " + courtId);
        }

        log.debug("Court Code (ffuu)=" + ffuu);
        log.debug(methodName + " exited.");
        return ffuu;
    }

    private Properties getCrnProperties() {
        // get ss and seqNo from crn.properties file
        Properties prop = CSServices.getConfigServices().getProperties("crn");
        String ss = prop.getProperty("SS");
        String seqNo = prop.getProperty("SEQ_NO");

        if (ss == null || ss.length() != CrnValue.SS_LENGTH) {
            log.error("Crn system code not set in crn.properties. Code code must be exactly 2 characters");
            throw new CSConfigurationException(
                    "Crn system code not set in crn.properties. Code code must be exactly 2 characters");
        }

        if (seqNo == null || seqNo.length() != CrnValue.SEQ_NO_LENGTH) {
            log
                    .error("Crn seq number (default charge sheet number), not set in crn.properties. Sequence number must be exactly 3 characters");
            throw new CSConfigurationException(
                    "Crn seq number (default charge sheet number), not set in crn.properties. Sequence number must be exactly 3 characters");
        }

        return prop;

    }

}