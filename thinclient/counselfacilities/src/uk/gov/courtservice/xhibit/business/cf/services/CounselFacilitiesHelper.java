package uk.gov.courtservice.xhibit.business.cf.services;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;

/**
 * <p>
 * Title: CounselFacilitiesHelper
 * </p>
 * <p>
 * Description: Helper class for counsel facilities application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */
public class CounselFacilitiesHelper {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("h:mm");

    private static Logger log = CSServices.getLogger(CounselFacilitiesHelper.class);

    public static String getFullName(String fullName, String firstName, String surname) {
        StringBuffer buf = new StringBuffer();

        if (tidyUp(fullName).length() == 0) {
            buf.append(tidyUp(firstName));
            if (buf.length() > 0) {
                buf.append(" ");
            }
            buf.append(tidyUp(surname));
        } else {
            buf.append(tidyUp(fullName));
        }

        return buf.toString();
    }

    public static String getSurnameFirstName(String firstName, String surname) {
        StringBuffer buf = new StringBuffer();

        buf.append(tidyUp(surname));

        if (tidyUp(firstName).length() > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }

            buf.append(tidyUp(firstName));
        }

        return buf.toString();
    }

    public static String tidyUp(String param) {
        return (param == null ? "" : param);
    }

    public static String dateToTimeString(Date date) {
        return ((date != null) ? formatter.format(date) : "");
    }

    /**
     * Get set of court room ids for a given site id
     * 
     * @param siteId
     * @return Integer Set of court room ids
     */
    public HashSet getCourtRoomsOnSite(Integer siteId) {
        HashSet courtRoomIds = new HashSet();
        Collection roomIds = CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                .getCourtRoomIds(siteId);

        for (Iterator i = roomIds.iterator(); i.hasNext();) {
            Integer courtRoomId = (Integer) i.next();
            courtRoomIds.add(courtRoomId);
        }
        return courtRoomIds;
    }

    /**
     * 
     * @param partiesOnCase
     *            collection of PartyOnCaseValue
     * @param courtRoomIds
     *            court room ids
     * @return collection of PartyOnCaseValue
     */
    public Collection filterByCourtRooms(Collection partiesOnCase, HashSet courtRoomIds) {
        Vector siteCol = new Vector();
        Iterator it = partiesOnCase.iterator();

        while (it.hasNext()) {
            PartyOnCaseValue p = (PartyOnCaseValue) it.next();
            Integer roomid = p.getCourtRoomId();
            if (courtRoomIds.contains(roomid)) {
                siteCol.add(p);
            }
        }

        return siteCol;
    }

    /**
     * Filters a collection of PartyOnCaseValues and returns those for the
     * siteid stored in the cookie
     * 
     * @param partyOnCaseValues
     *            collection of partyOnCaseValues to be filtered.
     * @param courtSiteId
     *            the id of the site
     * @return Collection of PartyOnCaseValues for the site id in the cookie
     */
    public Collection getPartiesOnCaseForSite(Collection partyOnCaseValues, Integer courtSiteId) {
        HashSet courtRoomIdsOnSite = getCourtRoomsOnSite(courtSiteId);
        return filterByCourtRooms(partyOnCaseValues, courtRoomIdsOnSite);
    }
}
