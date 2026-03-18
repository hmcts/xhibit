package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicNoticeValue;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public class CourtDetailCompiledRendererDelegate extends DisplayDocumentCompiledRendererDelegate {
	
	private static final Logger log = CSServices.getLogger(CourtDetailCompiledRendererDelegate.class);
	
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
                + get(documentI18n, "Detail");

        appendHeader(buffer, heading);
        appendHeading(buffer, displayDocument, documentI18n, date, "banner-cdtl", heading, 800);

        if (!isEmpty(displayDocument)) {
            append(buffer, "<div class=\"court-detail\">");
            appendln(buffer, "<div id=\"bodyArea\" class=\"body-area\">");

            Iterator items = getTable(displayDocument).iterator();
            while (items.hasNext()) {
                Object item = items.next();

                appendln(buffer, "<table class=\"non-scrolling-results\">");
                appendln(buffer, "<thead>");
                appendln(buffer, "<tr class=\"column-headers\">");
                append(buffer, "<td>");
                append(buffer, get(documentI18n, "Judge"));
                appendln(buffer, "</td>");
                appendln(buffer, "<tr>");
                appendln(buffer, "<thead>");
                appendln(buffer, "<tbody>");
                appendln(buffer, "<tr class=\"oddRow\">");
                appendJudgeName(buffer, getJudgeName(documentI18n, item));
                appendln(buffer, "</tr>");
                appendln(buffer, "</tbody>");
                appendln(buffer, "</table>");

                appendln(buffer, "<div class=\"inter-table-padding\"/>");

                appendln(buffer, "<table id=\"initialResultTable\" class=\"non-scrolling-results\">");
                appendln(buffer, "<thead>");
                appendln(buffer, "<tr class=\"column-headers\">");

                append(buffer, "<td>");
                append(buffer, get(documentI18n, "Case No."));
                appendln(buffer, "</td>");

                append(buffer, "<td>");
                append(buffer, get(documentI18n, "Name"));
                appendln(buffer, "</td>");

                append(buffer, "<td>");
                append(buffer, get(documentI18n, "Hearing Type"));
                appendln(buffer, "</td>");

                appendln(buffer, "</tr>");
                appendln(buffer, "</thead>");
                appendln(buffer, "<tbody>");
                appendln(buffer, "<tr class=\"oddRow\">");

                appendCaseNumberNoRestrictions(buffer, item);
                append(buffer, "<td class=\"defendant-names\">");
                appendln(buffer, "<div class=\"defendant-place-holder\">");
                appendScrollingDefendantNames(buffer, getDefendantNames(item));
                append(buffer, "</div>");
                appendln(buffer, "</td>");
                appendHearingDescription(buffer, getHearingDescription(documentI18n, item));
                appendln(buffer, "</tr>");
                appendln(buffer, "</tbody>");
                appendln(buffer, "</table>");

                if (hasEvent(item)) {
                    appendln(buffer, "<div class=\"inter-table-padding\"/>");

                    appendln(buffer, "<table class=\"case-progress\">");
                    appendln(buffer, "<tbody>");
                    appendln(buffer, "<tr>");

                    append(buffer, "<td class=\"case-progress-time\">");
                    append(buffer, get(documentI18n, "Case progress as of"));
                    append(buffer, "<br/>");

                    append(buffer, getEventTimeAsString(item), "${item.eventTimeAsString}");
                    appendln(buffer, "</td>");

                    append(buffer, "<td class=\"case-progress-status\">");
                    appendEvent(buffer, item, documentI18n);
                    appendln(buffer);

                    appendln(buffer, "</td>");

                    appendln(buffer, "</tr>");
                    appendln(buffer, "</tbody>");
                    appendln(buffer, "</table>");
                }

                if (hasPublicNotices(item)) {
                    appendln(buffer, "<div class=\"inter-table-padding\"/>");

                    appendln(buffer, "<table id=\"resultTable\" class=\"results\">");
                    appendln(buffer, "<thead><tr class=\"column-headers\">");

                    append(buffer, "<td width=\"100%\">");
                    append(buffer, get(documentI18n, "Notices"));
                    appendln(buffer, "</td>");
                    appendln(buffer, "</tr></thead>");
                    appendln(buffer, "<tbody>");

                    PublicNoticeValue[] notices = getPublicNotices(item);
                    for (int i = 0; i < notices.length; i++) {
                        appendln(buffer, "<tr class=\"public-notice\">");
                        append(buffer, "<td class=\"public-notice\">");
                        append(buffer, getPublicNoticeDesc(documentI18n, notices[i]), "${notice.publicNoticeDesc}");
                        appendln(buffer, "</td>");
                        appendln(buffer, "</tr>");
                    }

                    appendln(buffer, "</tbody>");
                    appendln(buffer, "</table>");
                }
            }
            appendln(buffer, "</div></div>");
        } else {
            appendNoInformation(buffer, documentI18n);
        }

        appendln(buffer, "<script language=\"JavaScript\">");
        appendln(buffer, "var defendantDocumentScroller;");
        appendln(buffer, "function overriddenInitialise() {");
        appendln(buffer, "initialise();");
        appendln(buffer, "try {");
        appendln(buffer, "defendantsTable.style.visibility='hidden';");
        appendln(buffer,
                "defendantDocumentScroller = new ScrollingDocument(defendantScroller, defendantDisplayArea, defendantsTable);");
        appendln(buffer, "defendantDocumentScroller.cycle();");
        appendln(buffer, "defendantsTable.style.visibility='visible';");
        appendln(buffer, "setInterval('defendantDocumentScroller.cycle()',3000);");
        appendln(buffer, "} catch(e) {");
        appendln(buffer, "error(e.message);");
        appendln(buffer, "}");
        appendln(buffer, "}");
        appendln(buffer, "window.onload= overriddenInitialise;");
        appendln(buffer, "</script>");

        appendFooter(buffer);
    }

    private void appendScrollingDefendantNames(StringBuffer buffer, Collection<DefendantName> nameCollection) {
        appendln(buffer, "<div class=\"defendant-scrolling-area\" id=\"defendantScroller\">");
        appendln(buffer, "<div class=\"defendant-display-area\" id=\"defendantDisplayArea\">");
        appendln(buffer, "<table class=\"defendant-names\" id=\"defendantsTable\">");
        appendln(buffer, "<tbody>");

        if (nameCollection != null) {
            for (Object name : nameCollection) {
                if (isHideInPublicDisplay(name)) {
                    continue;
                }
                append(buffer, "<tr><td id=\"defendant\" class=\"scrolling-defendant-name\">");
                append(buffer, "<div class=\"defendant-name-restricted-size\">");
                append(buffer, name == null ? "${name}" : name.toString());
                append(buffer, "</div>");
                appendln(buffer, "</td></tr>");
            }
        }

        appendln(buffer, "</tbody>");
        appendln(buffer, "</table>");
        appendln(buffer, "</div>");
        appendln(buffer, "</div>");
    }

    //
    // Utils
    //    

    private String getPublicNoticeDesc(TranslationBundle documeni18n, PublicNoticeValue value) {
        if (value != null) {
            String desc = value.getPublicNoticeDesc();
            if (desc != null) {
                return get(documeni18n, desc);
            }
            else {
            	log.debug("Null Public Notice Description entered.");
            }
        } else {
        	log.debug("Null PublicNoticeValue entered.");
        }

        return "";
    }

    private PublicNoticeValue[] getPublicNotices(Object item) {
        if (item instanceof CourtDetailValue) {
            PublicNoticeValue[] notices = ((CourtDetailValue) item).getPublicNotices();
            if (notices != null) {
                return notices;
            }
        }
        return new PublicNoticeValue[0];
    }

    private boolean hasPublicNotices(Object item) {
        if (item instanceof CourtDetailValue) {
            return ((CourtDetailValue) item).hasPublicNotices();
        }
        return false;
    }
}
