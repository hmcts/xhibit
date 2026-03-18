package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.LeafEventXMLNode;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;


public class AllCaseStatusCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
    /**
     * Append the html for the display document
     * 
     * @param buffer
     *            the buffer to append to
     * @param displayDocument
     *            the document containing the information to render
     */
    protected void appendDisplayDocumentHtml(StringBuffer buffer, DisplayDocument displayDocument,
            TranslationBundle documentI18n, Date date) {
        appendln(buffer, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">");

        String heading = get(documentI18n, "All Case Status");
        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner-dl", heading, 800);

        if (!isEmpty(displayDocument)) {
            appendln(buffer, "<div id=\"bodyArea\" class=\"body-area\">");
            appendln(buffer,
                    "<table id=\"resultTable\" class=\"results\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">");

            appendln(buffer, "<thead>");
            appendln(buffer, "<tr class=\"column-headers\">");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Court"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"10%\">");
            append(buffer, get(documentI18n, "Case No."));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"35%\">");
            append(buffer, get(documentI18n, "Name"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"17%\">");
            append(buffer, get(documentI18n, "Type"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"5%\">");
            append(buffer, get(documentI18n, "Not Before"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"18%\">");
            append(buffer, get(documentI18n, "Status"));
            appendln(buffer, "</td>");

            appendln(buffer, "</tr>");
            appendln(buffer, "</thead>");

            appendln(buffer, "<tbody>");

            String rowType = getRowType(null);
            boolean restrictionsApplyToThisList = false;
            
            Collection table = getTable(displayDocument);
            
            Map<String,Boolean> allDefendantsHidden = 
                checkForCasesWithAllDefendantsHidden(table);
            
            Set<String> caseNumbersProcessed = new TreeSet<String>();
            
            for (Object item : table) {

                if (isSkipThisRow(item, allDefendantsHidden, caseNumbersProcessed)) {
                    continue;
                }
                
                append(buffer, "<tr class=\"");
                append(buffer, rowType);
                appendln(buffer, "\">");

                appendCourtRoomNameOrUnassignedAndMovement(buffer, documentI18n, item);
                appendCaseNumberNoRestrictions(buffer, item);
                appendDefendantNameOrTitleRestrictedWidth(buffer, item);

                append(buffer, "<td class=\"hearing-description\">");
                appendSpace(buffer, getHearingDescription(documentI18n, item));
                appendln(buffer, "</td>");

                appendNotBeforeTime(buffer, getNotBeforeTimeAsString(item));
                appendCaseOrHearingStatus(buffer, documentI18n, item);

                appendln(buffer, "</tr>");

                // Update loop variables
                rowType = getRowType(rowType);
                if (isReportingRestricted(item)) {
                    restrictionsApplyToThisList = true;
                }
            }

            appendln(buffer, "</tbody>");
            appendln(buffer, "</table>");
            appendln(buffer, "</div>");

            appendShowRestrictions(buffer, documentI18n, restrictionsApplyToThisList);
        } else {
            appendNoInformation(buffer, documentI18n);
        }

        appendFooter(buffer);
    }

    private boolean isSkipThisRow(
            Object item, 
            Map<String,Boolean> allDefendantsHidden, 
            Set<String> caseNumbersProcessed) {
        
        String caseNumber = getCaseNumber(item);
        boolean newCaseNumber = !caseNumbersProcessed.contains(caseNumber);
        if (newCaseNumber) {
            caseNumbersProcessed.add(caseNumber);
        }
        if (!allDefendantsHidden.containsKey(caseNumber)) {
            return false;
        } else if (newCaseNumber 
                && allDefendantsHidden.get(caseNumber).booleanValue()) {
            // If all the defendants on the case are hidden then we still 
            // want to show one row for the case, so dont skip this item.  
            // If the case itself is hidden then no rows for the case 
            // will be returned from the database so we wont get here.
            return false;
        } else if (isHideInPublicDisplay(item)) {
            // otherwise skip this row if this defendant is hidden.
            return true;
        }
        return false;
    }
    
    /**
     * If all the defendants on a case are hidden from the public display
     * then we we should present a single row on the display for that case,
     * and that row should have an empty defendant column.  To do this we need to know
     * which cases have every defendant hidden.
     * 
     * @param table
     * @return Return a map of cases where the case number maps to a boolean
     * value.  TRUE if every defendant is hidden, FALSE otherwise.
     */
    private Map<String, Boolean> checkForCasesWithAllDefendantsHidden(Collection table) {
        HashMap<String, Boolean> cases = new HashMap<String, Boolean>();
        
        for (Object item : table) {
            String caseNumber = getCaseNumber(item);
            boolean hideThisDefendantInPublicDisplay = isHideInPublicDisplay(item);
            
            if (cases.containsKey(caseNumber)) {
                if (cases.get(caseNumber).booleanValue() 
                        && !hideThisDefendantInPublicDisplay) {
                    cases.put(caseNumber, Boolean.FALSE);
                }
            } else {
                cases.put(caseNumber, new Boolean(hideThisDefendantInPublicDisplay));
            }
        }
        
        return cases;
    }

    private void appendCourtRoomNameOrUnassignedAndMovement(StringBuffer buffer, TranslationBundle documentI18n,
            Object item) {
        if (isFloating(item)) {
            append(buffer, "<td class=\"court-room-name\">");
            append(buffer, get(documentI18n, "Unassigned"));
            appendln(buffer, "</td>");
        } else {
            append(buffer, "<td class=\"court-room-name\">");
            appendln(buffer,
                    "<div style=\"word-wrap:break-word;overflow:auto\" class=\"court_room_name-restricted-size120 \">");
            appendSpace(buffer, getCourtSiteRoomName(item, documentI18n));
            String movedFromCourtSiteRoomName = getMovedFromCourtSiteRoomName(item, documentI18n);
            if (movedFromCourtSiteRoomName != null) {
                append(buffer, "<div class=\"moved-highlight\">");
                append(buffer, get(documentI18n, "Moved from"));
                append(buffer, " ");
                append(buffer, movedFromCourtSiteRoomName);
                append(buffer, "</div>");
            }
            appendln(buffer, "</div>");
            appendln(buffer, "</td>");
        }
    }

    private void appendDefendantNameOrTitleRestrictedWidth(StringBuffer buffer, Object item) {
        // Note the global velocity variable restrictionsApplyToThisList is not
        // set here, This functionality is handled by the calling method.       
        append(buffer, "<td class=\"defendant-name\">");
        append(buffer, "<div class=\"defendant-name-restricted-size350\">");
        if (isHideInPublicDisplay(item)) {
            appendSpace(buffer, "");
        } else if (hasDefendant(item)) {
            if (isReportingRestricted(item)) {
                appendAsterix(buffer, getDefendantName(item));
            } else {
                appendSpace(buffer, getDefendantName(item));
            }
        } else {
            if (isReportingRestricted(item)) {
                appendAsterix(buffer, getCaseTitle(item));
            } else {
                appendSpace(buffer, getCaseTitle(item));
            }
        }
        appendln(buffer, "</div></td>");
    }

    private void appendCaseOrHearingStatus(StringBuffer buffer, TranslationBundle documentI18n, Object item) {
        if (hasEvent(item)) {
            append(buffer, "<td class=\"live-status\">");
            append(buffer, "<div class=\"liveStatusRestrictedWidth\">");
            appendEvent(buffer, item, documentI18n);
            append(buffer, " ");
            append(buffer, get(documentI18n, "at", "time"));
            append(buffer, " ");
            // TODO Call getEventTimeAsString
            append(buffer, getEventTimeAsString(item), "${item.eventTimeAsString}");
            append(buffer, "</div>");
            appendln(buffer, "</td>");
        } else {
            appendHearingProgress(buffer, documentI18n, getHearingProgress(item));
        }
    }

    /**
     * Overrides method in DisplayDocumentCompiledRendererDelegate to not
     * display defendant name
     */
    protected void appendEvent30200(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        BranchEventXMLNode laoOptions = (BranchEventXMLNode) node.get("E30200_Long_Adjourn_Options");
        String laoType = ((LeafEventXMLNode) (laoOptions.get("E30200_LAO_Type"))).getValue();

        // Add LAO Type text
        if ("E30200_Case_to_be_listed_in_week_commencing".equals(laoType)) {
            append(buffer, get(documentI18n, "Case to be listed in week commencing"));
            append(buffer, SPACE);
            appendFormattedDate(buffer, (LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), documentI18n);
        } else if ("E30200_Case_to_be_listed_on".equals(laoType)) {
            append(buffer, get(documentI18n, "Case to be listed on"));
            append(buffer, SPACE);
            appendFormattedDate(buffer, (LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), documentI18n);
        } else if ("E30200_Case_to_be_listed_on_date_to_be_fixed".equals(laoType)) {
            append(buffer, get(documentI18n, "Case to be listed on date to be fixed"));
        } else if ("E30200_Case_to_be_listed_for_Sentence".equals(laoType)) {
            append(buffer, get(documentI18n, "Case to be listed for Sentence on"));
            append(buffer, SPACE);
            appendFormattedDate(buffer, (LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), documentI18n);
        } else if ("E30200_Case_to_be_listed_for_Further_Mention/PAD".equals(laoType)) {
            append(buffer, get(documentI18n, "Case to be listed for Further Mention/PAD on"));
            append(buffer, SPACE);
            appendFormattedDate(buffer, (LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), documentI18n);
        } else if ("E30200_Case_to_be_listed_for_trial".equals(laoType)) {
            append(buffer, get(documentI18n, "Case to be listed for Trial on"));
            append(buffer, SPACE);
            appendFormattedDate(buffer, (LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), documentI18n);
        } else {
            append(buffer, get(documentI18n, "Adjourned"));
            append(buffer, "getEvent30300(node, TranslationBundle documentI18n) not created yet");
        }

        // Separate listing text from below
        append(buffer, SEMI_COLON);
        append(buffer, SPACE);

        // PSR Required
        if (laoOptions.get("E30200_LAO_PSR_Required") != null
                && ((LeafEventXMLNode) laoOptions.get("E30200_LAO_PSR_Required")).getValue().equalsIgnoreCase("true")) {
            append(buffer, get(documentI18n, "PSR Required"));
            append(buffer, SEMI_COLON);
            append(buffer, SPACE);
        }

        // Reserved?
        if (laoOptions.get("E30200_LAO_Not_Reserved") != null
                && ((LeafEventXMLNode) laoOptions.get("E30200_LAO_Not_Reserved")).getValue().equalsIgnoreCase("true")) {
            append(buffer, get(documentI18n, "Not Reserved"));
            append(buffer, SEMI_COLON);
            append(buffer, SPACE);
        } else if (laoOptions.get("E30200_LAO_Reserved_To_Judge_Name") != null
                && ((LeafEventXMLNode) laoOptions.get("E30200_LAO_Reserved_To_Judge_Name")).getValue() != "") {
            append(buffer, get(documentI18n, "Reserved to"));
            append(buffer, SPACE);
            append(buffer, get(documentI18n, ((LeafEventXMLNode) laoOptions.get("E30200_LAO_Reserved_To_Judge_Name"))
                    .getValue()));
            append(buffer, SEMI_COLON);
            append(buffer, SPACE);
        }
    }

    protected boolean hasDefendant(Object item) {
        if (item instanceof AllCaseStatusValue) {
            return ((AllCaseStatusValue) item).hasDefendant();
        }
        return false;
    }
}
