package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public class JuryCurrentStatusCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
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

        String heading = get(documentI18n, "Jury Current Status");
        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner", heading, 800);

        if (!isEmpty(displayDocument)) {
            appendln(buffer, "<div id=\"bodyArea\" class=\"body-area\">");
            appendln(buffer,
                    "<table id=\"resultTable\" class=\"results\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">");
            appendln(buffer, "<thead>");
            appendln(buffer, "<tr class=\"column-headers\">");

            append(buffer, "<td width=\"10%\">");
            append(buffer, get(documentI18n, "Court"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Judge"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"35%\">");
            append(buffer, get(documentI18n, "Name"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Case No."));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Hearing Type"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"10%\">");
            append(buffer, get(documentI18n, "Not Before"));
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

                appendCourtRoomNameOrUnassigned(buffer, documentI18n, item);
                appendJudgeNameRestrictedWidth(buffer, getJudgeName(documentI18n, item));
                appendDefendantsOrTitle(buffer, documentI18n, item);
                appendCaseNumber(buffer, item);
                appendHearingDescription(buffer, getHearingDescription(documentI18n, item));
                appendNotBeforeTime(buffer, getNotBeforeTimeAsString(item));

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
}
