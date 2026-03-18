package uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationDisplayDocument;

/**
 * <p>
 * Title: VIP Display Document Row Processor
 * </p>
 * <p>
 * Description: Builds an array of VIPDisplayConfigurationDisplayDocuments
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: VIPDisplayDocumentProcessor.java,v 1.3 2005/11/17 10:55:48
 *          bzjrnl Exp $
 */

public class VIPDisplayDocumentProcessor extends AbstractRowProcessor {
    private static final Logger log = CSServices.getLogger(VIPDisplayDocumentProcessor.class);

    private static final String DESCRIPTION_CODE = "DESCRIPTION_CODE";

    private static final String MULTIPLE_COURT_YN = "MULTIPLE_COURT_YN";

    private static final String LANGUAGE = "LANGUAGE";

    private static final String COUNTRY = "COUNTRY";

    private List<VIPDisplayConfigurationDisplayDocument> values = new ArrayList<VIPDisplayConfigurationDisplayDocument>();

    private boolean multipleCourt = false;

    private String descriptionCode;

    private String language;

    private String country;

    /**
     * For each row extract the description and store in array
     * 
     * @param row
     */
    public void processRow(Row row) {
        log.debug("Process Row called");

        multipleCourt = "Y".equalsIgnoreCase(row.getString(MULTIPLE_COURT_YN));
        descriptionCode = row.getString(DESCRIPTION_CODE);
        language = row.getString(LANGUAGE);
        country = row.getString(COUNTRY);

        VIPDisplayConfigurationDisplayDocument vipDisplayConfigurationDisplayDocument = new VIPDisplayConfigurationDisplayDocument(
                descriptionCode, multipleCourt, language, country);

        if (log.isDebugEnabled()) {
            log.debug("Adding vipDisplayConfigurationDisplayDocument to collection with " + "Description Code: "
                    + descriptionCode + "and multipleCourt = " + multipleCourt);
        }

        values.add(vipDisplayConfigurationDisplayDocument);
    }

    /**
     * This method returns the array of VIPDisplayConfigurationDisplayDocuments
     * 
     * @return VIPDisplayConfigurationDisplayDocument[]
     *         VIPDisplayConfigurationDisplayDocuments
     */
    public Collection<VIPDisplayConfigurationDisplayDocument> getData() {
        return values;
    }
}