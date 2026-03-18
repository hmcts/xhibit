package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public class AllCourtStatusCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
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

        String heading = get(documentI18n, "All Court Status");
        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner-acs", heading, 800);

        if (!isEmpty(displayDocument)) {
            appendln(buffer, "<div id=\"bodyArea\" class=\"body-area\">");
            appendln(buffer,
                    "<table id=\"resultTable\" class=\"results\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">");
            appendln(buffer, "<thead>");
            appendln(buffer, "<tr class=\"column-headers\">");

            append(buffer, "<td width=\"25%\">");
            append(buffer, get(documentI18n, "Court"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"10%\">");
            append(buffer, get(documentI18n, "Case No."));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"45%\">");
            append(buffer, get(documentI18n, "Name"));
            appendln(buffer, "</td>");

            append(buffer, "<td width=\"20%\">");
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

                appendCourtRoomName(buffer, getCourtSiteRoomName(item, documentI18n));

                if (hasInformationForDisplay(item)) {
                    appendCaseNumber(buffer, item);
                    appendDefendantNamesRestrictedSize(buffer, documentI18n, getDefendantNames(item));

                    append(buffer, "<td class=\"live-status\">");

                    if (hasEvent(item)) {
                        append(buffer, "<div class=\"liveStatusRestrictedWidth\">");
                        appendEvent(buffer, item, documentI18n);
                        append(buffer, " ");
                        append(buffer, get(documentI18n, "at", "time"));
                        append(buffer, " ");
                        append(buffer, getEventTimeAsString(item), "${item.eventTimeAsString}");
                        append(buffer, "</div>");
                    } else {
                        appendSpace(buffer, "");
                    }
                    appendln(buffer, "</td>");
                } else {
                    append(buffer, "<td>");
                    appendSpace(buffer, "");
                    appendln(buffer, "</td>");

                    appendNoInfomationRow(buffer, documentI18n);

                    append(buffer, "<td>");
                    appendSpace(buffer, "");
                    appendln(buffer, "</td>");
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

    private boolean hasInformationForDisplay(Object item) {
        if (item instanceof AllCourtStatusValue) {
            return ((AllCourtStatusValue) item).hasInformationForDisplay();
        }
        return false;
    }

}
