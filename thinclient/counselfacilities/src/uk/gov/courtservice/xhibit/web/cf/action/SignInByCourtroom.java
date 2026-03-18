package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import uk.gov.courtservice.xhibit.business.cf.services.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.LegalRepSignInValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

/**
 * <p>
 * Title: SignInByCourtroom
 * </p>
 * <p>
 * Description: Provided the column names and formatted rows for the
 * CouselSignIn page
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class SignInByCourtroom extends CounselSignInRowGenerator {
    public SignInByCourtroom() {
        init();
    }

    /**
     * Creates the javasript method call for the table row. printRow(col1, col2,
     * col3, col4, selectId, disable, caseNumber, legalRep) see also
     * CounselSignInRowGenerator
     * 
     * @param index
     *            row number
     * @param party
     *            data for the table row
     * @return
     */
    public String getTableRow(int index, PartyOnCaseValue party) {
        String courtRoomName = party.getCourtRoomDescription();
        String timeString = CounselFacilitiesHelper.dateToTimeString(party.getTimeListed());
        String role = party.getPartyRole();
        Collection defCol = party.getDefendants();
        Integer caseNum = party.getCaseNumber();
        String caseType = party.getCaseType();
        Collection repCol = party.getRepresentatives();
        Integer selectId = new Integer(index);
        Integer shDefId = party.getScheduledHearingDefendantId();
        String disable = "";
        String isFloating = party.getIsFloating();
        String caseTitle = party.getCaseTitle();

        if (isFloating != null && isFloating.trim().equals("1")) {
            courtRoomName = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                    "assignlegalrep.unassigned");
        }

        if (isCheckboxDisabled(caseType, role, shDefId)) {
            disable = "disabled";
        }

        // construct the row data
        String sep = "\", \"";
        StringBuffer tableRowData = new StringBuffer("printRow(\"");
        tableRowData.append(courtRoomName);
        tableRowData.append(sep);
        tableRowData.append(timeString);
        tableRowData.append(sep);
        tableRowData.append(roleConverter(role));
        tableRowData.append(sep);

        if (caseType == null
                || (!caseType.trim().equals("A") && !caseType.trim().equals("S") && !caseType.trim().equals("T"))) {
            tableRowData.append(formatCaseTitle(caseTitle));
        } else {
            Iterator defIt = defCol.iterator();
            while (defIt.hasNext()) {
                DefendantValue defVal = (DefendantValue) defIt.next();
                tableRowData.append(formatDefendantName(defVal));
            }
        }

        tableRowData.append(sep);
        tableRowData.append(selectId);
        tableRowData.append(sep);
        tableRowData.append(disable);
        tableRowData.append(sep);
        tableRowData.append(caseType);
        tableRowData.append(caseNum);
        tableRowData.append(sep);

        if (caseType == null
                || (!caseType.trim().equals("A") && !caseType.trim().equals("S") && !caseType.trim().equals("T"))) {
            tableRowData.append(ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                    "assignlegalrep.registerWithCourtClerk"));
        }

        Iterator repIt = repCol.iterator();
        while (repIt.hasNext()) {
            LegalRepSignInValue repVal = (LegalRepSignInValue) repIt.next();
            tableRowData.append(formatLegalRepName(repVal));
        }

        tableRowData.append(sep);
        tableRowData.append(selectId);
        tableRowData.append("\");");

        return tableRowData.toString();
    }

    /**
     * Get the column names for this table see also CounselSignInRowGenerator
     * 
     * @return column names
     */
    public String[] getColumnNames() {
        String[] colNames = {
                ResourceUtil
                        .getPropertyForResourceAndLocale("Messages", Locale.getDefault(), "assignlegalrep.courtCol"),
                ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(), "assignlegalrep.timeCol"),
                ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(), "assignlegalrep.roleCol"),
                ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.defendantCol"),
                ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.selectCol") };
        return colNames;
    }
}