package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.config.ConfigServicesImpl;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_location.XhbDisplayLocation;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDd;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBeanHelper;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.StringUtilities;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.CourtSitePDComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayBasicValueSortAdapter;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayLocationComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

/**
 * <p>
 * Title: Display Location Helper
 * </p>
 * <p>
 * Description: Helper methods to get relevant information regarding locations
 * and displays
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisplayLocationDataHelper.java,v 1.9 2005/11/17 10:55:46 bzjrnl
 *          Exp $
 */

public class DisplayLocationDataHelper {
    private static final String PRE_DISPLAY = "pd.displaydescription.";

    private static final String PublicDisplayConfiguration = "XHIBITPublicDisplayConfigurationResources";

    private static final Logger log = CSServices.getLogger(DisplayLocationDataHelper.class);

    /**
     * Returns the Court Sites, Locations within the Site and Displays within
     * the Site.
     * 
     * @param courtId
     *            The court being maintained
     * @return Details of site, location and screen within a court
     */
    public static CourtSitePDComplexValue[] getDisplaysForCourt(Integer courtId) {
        log.debug("getDisplaysForCourt called for courtId " + courtId);
        ArrayList results = new ArrayList();

        CourtSitePDComplexValue sitePdComplex;

        // Find all the court sites for the court room
        Collection siteCol = XhbCourtSiteBeanHelper.findByCourtId(courtId);
        Iterator siteIter = siteCol.iterator();

        while (siteIter.hasNext()) {
            // Create an instance of the court site complex value
            sitePdComplex = new CourtSitePDComplexValue();

            XhbCourtSite siteLocal = (XhbCourtSite) siteIter.next();

            // Set the court site data
            sitePdComplex.setCourtSiteBasicValue(siteLocal.getData());

            // Add display locations
            addDisplayLocation(sitePdComplex, siteLocal);

            results.add(sitePdComplex);
        }

        log.debug("getDisplaysForCourt exitted");
        return ((CourtSitePDComplexValue[]) results.toArray(new CourtSitePDComplexValue[results.size()]));
    }

    /**
     * The Rotation sets, pages within each set and the screens the rotation
     * sets are assigned to are returned.
     */
    public static RotationSetComplexValue[] getRotationSetsDetailForCourt(Integer courtId, Locale locale) {
        ResourceBundle rb = ConfigServicesImpl.getInstance().getBundle(PublicDisplayConfiguration, locale);

        log.debug("getRotationSetsDetailForCourt called for courtId " + courtId);
        ArrayList results = new ArrayList();

        RotationSetComplexValue complex;

        // Get all the rotation sets for the court
        Collection rotationSetCol = XhbRotationSetBeanHelper.findByCourtId(courtId);
        Iterator rotationSetIter = rotationSetCol.iterator();

        while (rotationSetIter.hasNext()) {
            complex = new RotationSetComplexValue();

            XhbRotationSet rotationSetLocal = (XhbRotationSet) rotationSetIter.next();

            // Set the rotation set data
            complex.setRotationSetBasicValue(rotationSetLocal.getData());
            // Set the display data
            complex.setDisplayBasicValues(getDisplayAdapters(rb, rotationSetLocal));

            // Add the rotation set dds for this rotation set
            addRotationSetDd(complex, rotationSetLocal);

            // Add the rotation set to the list
            results.add(complex);
        }

        log.debug("getRotationSetsDetailForCourt exitted");
        // Return the list of rotation sets
        return ((RotationSetComplexValue[]) results.toArray(new RotationSetComplexValue[results.size()]));
    }

    private static DisplayBasicValueSortAdapter[] getDisplayAdapters(ResourceBundle rb, XhbRotationSet rotationSetLocal) {
        // Wrap the XhbDisplays in the sort adapter. getting the display text
        // from the resource bundle.
        XhbDisplayBasicValue[] displays = rotationSetLocal.getXhbDisplaysData();
        ArrayList displayAdapterArray = new ArrayList();
        for (int i = 0; i < displays.length; i++) {
            String displayText;
            try {
                displayText = rb.getString(PRE_DISPLAY.concat(displays[i].getDescriptionCode()));
            } catch (MissingResourceException ex) {
                String key = displays[i].getDescriptionCode();
                String temp = key.substring(key.lastIndexOf('.') + 1);
                temp = temp.replace('_', ' ');
                displayText = StringUtilities.toSentenceCase(temp);
            }
            displayAdapterArray.add(new DisplayBasicValueSortAdapter(displays[i], displayText));
        }
        DisplayBasicValueSortAdapter[] displayAdapters = (DisplayBasicValueSortAdapter[]) displayAdapterArray
                .toArray(new DisplayBasicValueSortAdapter[displayAdapterArray.size()]);
        return displayAdapters;
    }

    /**
     * Adds display locations
     * 
     * @param sitePdComplex
     * @param siteLocal
     */
    private static void addDisplayLocation(CourtSitePDComplexValue sitePdComplex, XhbCourtSite siteLocal) {
        log.debug("addDisplayLocation called");
        DisplayLocationComplexValue displayLocationComplex;

        // get the locations
        Iterator iterLocation = siteLocal.getXhbDisplayLocations().iterator();

        while (iterLocation.hasNext()) {
            // for every location, find the displays and
            // create a complex VO
            displayLocationComplex = new DisplayLocationComplexValue();

            XhbDisplayLocation locationLocal = (XhbDisplayLocation) iterLocation.next();

            displayLocationComplex.setDisplayLocationBasicValue(locationLocal.getData());
            displayLocationComplex.setDisplayBasicValues(locationLocal.getXhbDisplaysData());

            sitePdComplex.addDisplayLocationComplexValue(displayLocationComplex);
        }
        log.debug("addDisplayLocation exitted");
    }

    /**
     * Adds rotation set dd to the rotation set
     * 
     * @param complex
     * @param rotationSetDDCol
     */
    private static void addRotationSetDd(RotationSetComplexValue complex, XhbRotationSet rotationSetLocal) {
        log.debug("addRotationSetDd called");
        RotationSetDDComplexValue ddComplex;

        // Get the rotation set dds for the current rotation set
        Iterator rotationSetDDIter = rotationSetLocal.getXhbRotationSetDds().iterator();

        while (rotationSetDDIter.hasNext()) {
            // for every rotation set DD, get the display document and
            // create a complex VO
            XhbRotationSetDd rotationSetDDLocal = (XhbRotationSetDd) rotationSetDDIter.next();
            ddComplex = new RotationSetDDComplexValue(rotationSetDDLocal.getData(), rotationSetDDLocal
                    .getXhbDisplayDocumentData());
            complex.addRotationSetDDComplexValue(ddComplex);
        }
        log.debug("addRotationSetDd exitted");
    }
}