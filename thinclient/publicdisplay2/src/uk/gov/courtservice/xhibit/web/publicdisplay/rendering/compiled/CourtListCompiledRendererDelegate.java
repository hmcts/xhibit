package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtListValue;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public class CourtListCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
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
        String courtSiteRoomName = getCourtSiteRoomName(displayDocument, documentI18n);

        // Below left as ${document.data.courtRoomName} because court site short
        // name can be null
        String heading = (courtSiteRoomName == null ? "${document.data.courtRoomName}" : courtSiteRoomName) + " "
                + get(documentI18n, "List", "Court List");

        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner-dl", heading, 800);

        if (!isEmpty(displayDocument)) {
            appendln(buffer, "<div id=\"bodyArea\" class=\"body-area\">");
            appendln(buffer,
                    "<table id=\"resultTable\" class=\"results\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">");
            appendln(buffer, "<thead>");
            appendln(buffer, "<tr class=\"column-headers\">");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Case No."));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"45%\">");
            append(buffer, get(documentI18n, "Name"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Type"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"10%\">");
            append(buffer, get(documentI18n, "Not Before"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Status"));
            appendln(buffer, "</td>");

            appendln(buffer, "</tr>");
            appendln(buffer, "</thead>");
            appendln(buffer, "<tbody>");

            String rowType = getRowType(null);
            boolean restrictionsApplyToThisList = false;

            Iterator items = getTable(displayDocument).iterator();
            while (items.hasNext()) {
                Object item = items.next();

                append(buffer, "<tr class=\"");
                append(buffer, rowType);
                appendln(buffer, "\">");

                appendCaseNumber(buffer, item);
                appendDefendantsOrTitle(buffer, documentI18n, item);
                appendHearingDescription(buffer, getHearingDescription(documentI18n, item));
                appendNotBeforeTime(buffer, getNotBeforeTimeAsString(item));

                String movedFromCourtSiteRoomName = getMovedFromCourtSiteRoomName(item, documentI18n);
                if (movedFromCourtSiteRoomName != null) {
                    append(buffer, "<td>");
                    append(buffer, "<div class=\"hearing-progress\">");
                    appendHearingProgressText(buffer, documentI18n, getHearingProgress(item));
                    append(buffer, "</div>");
                    if (isListedInThisCourtRoom(item)) {
                        append(buffer, "<div class=\"moved-highlight\">");
                        append(buffer, get(documentI18n, "Moved from"));
                        append(buffer, " ");
                        append(buffer, movedFromCourtSiteRoomName);
                        append(buffer, "</div>");
                    } else {
                        append(buffer, "<div class=\"moved-highlight\">");
                        append(buffer, get(documentI18n, "Moved to"));
                        append(buffer, " ");
                        // Below left as ${item.courtRoomName} because court
                        // site short name can be null
                        append(buffer, getCourtSiteRoomName(item, documentI18n), "${item.courtRoomName}");
                        append(buffer, "</div>");
                    }
                    appendln(buffer, "</td>");
                } else {
                    appendHearingProgress(buffer, documentI18n, getHearingProgress(item));
                }
                appendln(buffer, "</tr>");

                appendDefendantOverspill(buffer, item, rowType);

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

    private boolean isListedInThisCourtRoom(Object item) {
        if (item instanceof CourtListValue) {
            return ((CourtListValue) item).isListedInThisCourtRoom();
        }
        return false;
    }

}
