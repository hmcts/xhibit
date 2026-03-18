package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public class DailytListCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
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

        String heading = get(documentI18n, "Daily List");

        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner-dl", heading, 800);

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

            append(buffer, "<td width=\"45%\">");
            append(buffer, get(documentI18n, "Name/Case No."));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"15%\">");
            append(buffer, get(documentI18n, "Type"));
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
                appendDefendantAndCaseNumber(buffer, documentI18n, item);

                // Append Type Start
                append(buffer, "<td class=\"hearing-description\">");
                appendSpace(buffer, getHearingDescription(documentI18n, item));
                String movedFromCourtSiteRoomName = getMovedFromCourtSiteRoomName(item, documentI18n);
                if (movedFromCourtSiteRoomName != null) {
                    append(buffer, "<div class=\"moved-highlight\">");
                    append(buffer, get(documentI18n, "Moved from"));
                    append(buffer, " ");
                    append(buffer, movedFromCourtSiteRoomName);
                    append(buffer, "<div>");
                }
                appendln(buffer, "</td>");
                // Append Type End

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

    private void appendDefendantAndCaseNumber(StringBuffer buffer, TranslationBundle documentI18n, Object item) {
        append(buffer, "<td class=\"defendant-and-case-number\">");

        if (hasDefendants(item)) {
            appendln(buffer, "<span class=\"defendant-names\">");
            appendDefendants(buffer, documentI18n, getDefendantNames(item));
            append(buffer, "</span> / ");
        } else {
            append(buffer, "<span class=\"case-title\">");
            append(buffer, getCaseTitle(item), "${item.caseTitle}");
            append(buffer, "</span> / ");
        }

        append(buffer, "<span class=\"case-number\">");
        if (isReportingRestricted(item)) {
            appendAsterix(buffer, getCaseNumber(item));
        } else {
            append(buffer, getCaseNumber(item), "${item.caseNumber}");
        }
        append(buffer, "</span>");
        appendln(buffer, "</td>");
    }
}