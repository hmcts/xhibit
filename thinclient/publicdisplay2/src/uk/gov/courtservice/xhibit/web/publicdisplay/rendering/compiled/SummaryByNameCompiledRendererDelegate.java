package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public class SummaryByNameCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
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

        String heading = get(documentI18n, "Summary By Name");
        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner-sbn", heading, 800);

        if (!isEmpty(displayDocument)) {
            appendln(buffer, "<div id=\"bodyArea\" class=\"body-area\">");
            appendln(buffer,
                    "<table id=\"resultTable\" class=\"results\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">");
            appendln(buffer, "<thead>");
            appendln(buffer, "<tr class=\"column-headers\">");

            append(buffer, "<td width=\"60%\">");
            append(buffer, get(documentI18n, "Name"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"25%\">");
            append(buffer, get(documentI18n, "In Court"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Not Before"));
            appendln(buffer, "</td>");

            appendln(buffer, "</tr>");
            appendln(buffer, "</thead>");
            appendln(buffer, "<tbody>");

            String rowType = getRowType(null);
            boolean restrictionsApplyToThisList = false;

            for (Object item : getTable(displayDocument)) {
                
                if (isHideInPublicDisplay(item)) {
                    continue;
                }

                append(buffer, "<tr class=\"");
                append(buffer, rowType);
                appendln(buffer, "\">");
                appendDefendantName(buffer, item);

                String movedFromCourtSiteRoomName = getMovedFromCourtSiteRoomName(item, documentI18n);
                if (movedFromCourtSiteRoomName != null) {
                    append(buffer, "<td>");
                    // Below left as ${item.courtRoomName} because court
                    // site short name can be null
                    append(buffer, getCourtSiteRoomName(item, documentI18n), "${item.courtRoomName}");
                    append(buffer, "<div class=\"moved-highliht\">");
                    append(buffer, get(documentI18n, "Moved from"));
                    append(buffer, " ");
                    append(buffer, movedFromCourtSiteRoomName);
                    appendln(buffer, "</div></td>");
                } else {
                    appendCourtRoomNameOrUnassigned(buffer, documentI18n, item);
                }

                appendNotBeforeTime(buffer, getNotBeforeTimeAsString(item));

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

}