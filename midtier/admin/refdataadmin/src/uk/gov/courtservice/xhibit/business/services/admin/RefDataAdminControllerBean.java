package uk.gov.courtservice.xhibit.business.services.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.database.listdistribution.ListDistribution2Database;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import.XhbCrestImportBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import.XhbCrestImportBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_formatting.XhbFormattingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_formatting.XhbFormattingBeanHelper2;

/**
 * <p>
 * Title: RefDataAdminControllerBean
 * </p>
 * <p>
 * Description: Bean class for the Reference Data Admin controller
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * This class is used by the thin client Admin war to allow the EDS support team
 * to maintain the start times of the various reference data loads.
 * 
 * @ejb.bean name="RefDataAdminController" description="Reference Data Admin
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="RefDataAdminControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @version $Id: RefDataAdminControllerBean.java,v 1.4 2005/12/07 13:17:35
 *          zzzz48 Exp $
 */
public class RefDataAdminControllerBean extends CSSessionBean implements SessionBean {

    private static final Logger log = CSServices.getLogger(RefDataAdminControllerBean.class);

    /**
     * Get all courts listed in the XHB_CREST_IMPORT table
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @return XhbCrestImportBasicValue All the entries in this table sorted by
     *         court id then import type.
     */
    public XhbCrestImportBasicValue[] getAllCrestImportCourts() {
        XhbCrestImportBasicValue[] all = XhbCrestImportBeanHelper2.findAllValue();
        Sorter.sort(all, new String[] { "courtId", "importType" }, Sorter.ASCENDING);
        return all;
    }

    /**
     * Sets the updated Crest Import start values
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param importValues
     *            Values to save (update only, no new items)
     */
    public void setCrestImportValues(XhbCrestImportBasicValue[] importValues) {
        log.debug("setCrestImportValues() - Begin");

        for (int i = 0; i < importValues.length; i++) {
            XhbCrestImportBeanHelper2.update(importValues[i]);
        }
    }

    /**
     * Sets the updated Crest Import start values
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param importValues
     *            Values to save (update only, no new items)
     */
    public HashMap getCourtNames() {
        Iterator iter = XhbCourtBeanHelper2.findAll().iterator();
        HashMap map = new HashMap();
        while (iter.hasNext()) {
            XhbCourt item = (XhbCourt) iter.next();
            map.put(item.getCourtId(), item.getDisplayName());
        }

        return map;
    }

    /**
     * Gets the court details
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param importValues
     *            Values to save (update only, no new items)
     */
    public XhbCourtBasicValue[] getCourts() {
        XhbCourtBasicValue[] courts = XhbCourtBeanHelper2.findAllValue();
        Sorter.sort(courts, new String[] { "courtName" }, Sorter.ASCENDING);
        return courts;
    }

    /**
     * Get all internet web page documents from XHB_FORMATTING
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @return XhbFormattingBasicValue
     */
    public XhbFormattingBasicValue[] getIWPDocuments(int day, int month, int year, Integer courtId) {
        GregorianCalendar myCal = new GregorianCalendar(year, month, day);
        myCal.set(Calendar.HOUR_OF_DAY, myCal.getMinimum(Calendar.HOUR_OF_DAY));
        myCal.set(Calendar.MINUTE, myCal.getMinimum(Calendar.MINUTE));
        myCal.set(Calendar.SECOND, myCal.getMinimum(Calendar.SECOND));
        myCal.set(Calendar.MILLISECOND, myCal.getMinimum(Calendar.MILLISECOND));
        Date startDate = myCal.getTime();
        myCal.set(Calendar.HOUR_OF_DAY, myCal.getMaximum(Calendar.HOUR_OF_DAY));
        myCal.set(Calendar.MINUTE, myCal.getMaximum(Calendar.MINUTE));
        myCal.set(Calendar.SECOND, myCal.getMaximum(Calendar.SECOND));
        myCal.set(Calendar.MILLISECOND, myCal.getMaximum(Calendar.MILLISECOND));
        Date endDate = myCal.getTime();

        XhbFormattingBasicValue[] data = XhbFormattingBeanHelper2.findByDocTypeCourtIdDateRangeValue("IWP", courtId,
                startDate, endDate);
        Sorter.sort(data, new String[] { "lastUpdateDate", "creationDate" }, Sorter.DESCENDING);

        ArrayList ar = new ArrayList();

        for (int i = 0; i < data.length; i++) {
            XhbFormattingBasicValue value = (XhbFormattingBasicValue) data[i];
            if (value.getFormatStatus().equals("DA") || value.getFormatStatus().equals("DR")) {
                ar.add(value);
            }
        }
        return (XhbFormattingBasicValue[]) ar.toArray(new XhbFormattingBasicValue[ar.size()]);
    }

    /**
     * Get an internet web page document from XHB_BLOB
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @return String
     */
    public String getIWPHTML(int blobId) {
        String returnHtml = new String("");
        Integer value = new Integer(blobId);
        ListDistribution2Database database = new ListDistribution2Database();
        String[] data = database.getBlobData(value);
        // will always be only one string returned
        if (data.length == 1) {
            returnHtml = data[0];
        }
        return returnHtml;
    }
}
