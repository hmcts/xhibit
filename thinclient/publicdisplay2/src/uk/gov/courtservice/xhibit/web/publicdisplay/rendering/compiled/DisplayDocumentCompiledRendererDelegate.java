package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.translation.TranslationBundlesCache;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.Data;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JudgeName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JuryStatusDailyListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicDisplayValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.LeafEventXMLNode;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

public abstract class DisplayDocumentCompiledRendererDelegate extends AbstractCompiledRendererDelegate {
    private static final String MISSING_TRANSLATION_MARKER = "**";

    private static final Logger log = CSServices.getLogger(DisplayDocumentCompiledRendererDelegate.class);

    /**
     * Get the html for the display document
     * 
     * @param rotationSet
     * @return the html
     */
    public String getDisplayDocumentHtml(DisplayDocument displayDocument) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            String html = _getDisplayDocumentHtml(displayDocument);
            log.debug("Rendering \"" + displayDocument.getUri() + "\"" + " took "
                    + (System.currentTimeMillis() - starttime) + " ms to generated " + html.length() + " chars.");
            return html;
        }
        return _getDisplayDocumentHtml(displayDocument);
    }

    private String _getDisplayDocumentHtml(DisplayDocument displayDocument) {
        StringBuffer buffer = new StringBuffer(1024);
        appendDisplayDocumentHtml(buffer, displayDocument, geDocumentI18n(displayDocument), getDate());
        String html = buffer.toString();
        return html;
    }

    /**
     * This method is implemented by the concret documents to create the html
     * 
     * @param buffer
     * @param displayDocument
     */
    protected abstract void appendDisplayDocumentHtml(StringBuffer buffer, DisplayDocument displayDocument,
            TranslationBundle documentI18n, Date date);

    //  
    // header.vm
    //           

    /**
     * Append the document header
     * 
     * @param buffer
     */
    protected void appendHeader(StringBuffer buffer, String heading) {
        appendln(buffer, "<html>");
        appendln(buffer, "<head>");
        appendln(buffer, "<meta http-equiv=\"Page-Enter\" content=\"revealTrans(Duration=0.1,Transition=5)\"/>");

        append(buffer, "<link rel=\"stylesheet\" type=\"text/css\" href=\"");
        append(buffer, getPathRoot());
        appendln(buffer, "/css/general.css\"/>");

        append(buffer, "<link rel=\"stylesheet\" type=\"text/css\" href=\"");
        append(buffer, getPathRoot());
        appendln(buffer, "/css/table.css\"/>");

        appendln(buffer, "<script type=\"text/javascript\" src=\"/PublicDisplay/js/common.js\">");
        appendln(buffer, "</script>");
        appendln(buffer, "<script type=\"text/javascript\" src=\"/PublicDisplay/js/display_document.js\">");
        appendln(buffer, "</script>");

        append(buffer, "<title>Public Display: ");
        append(buffer, heading);
        appendln(buffer, "</title>");
        appendln(buffer, "</head>");
        appendln(buffer, "<body>");
    }

    //
    // heading.vm
    //    
    /**
     * Append the document heading
     * 
     * @param buffer
     */
    protected void appendHeading(StringBuffer buffer, DisplayDocument displayDocument, TranslationBundle documentI18n,
            Date date, String headerImageName, String heading, @SuppressWarnings("unused")
            int headingWidth) {
        appendln(buffer, "<form name=\"displayform\">");

        append(buffer, "<input type=\"hidden\" name=\"listTitleText\" value=\"");
        append(buffer, get(documentI18n, "List"));
        appendln(buffer, "\">");

        append(buffer, "<input type=\"hidden\" name=\"ofTitleText\" value=\"");
        append(buffer, get(documentI18n, "of"));
        appendln(buffer, "\">");

        append(buffer, "<input type=\"hidden\" name=\"pageTitleText\" value=\"");
        append(buffer, get(documentI18n, "Page"));
        appendln(buffer, "\">");

        appendln(buffer, "</form>");
        appendln(buffer, "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">");

        append(buffer, "<tr class=\"header-image\" height=\"58\" valign=\"middle\" style=\"background-image: url(");

        // Start headerImage inline

        append(buffer, "/PublicDisplay/header/images/");
        append(buffer, headerImageName);
        append(buffer, ".gif?x=-1&y=45&size=50&text=");
        try {
            append(buffer, URLEncoder.encode(get(documentI18n, "The Crown Court at"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // UTF-8 will always be supported
            throw new CSUnrecoverableException("Unsupported encode type UTF-8", e);
        }
        append(buffer, "+");
        try {
            append(buffer, URLEncoder.encode(getCourtName(displayDocument, documentI18n), "UTF-8"), "${document.data.courtName}");
        } catch (UnsupportedEncodingException e) {
            // UTF-8 will always be supported
            throw new CSUnrecoverableException("Unsupported encode type UTF-8", e);
        }
        append(buffer, "&width=1280");

        // End headerImage inline

        appendln(buffer, ");margin: 0px\">");

        appendln(buffer, "<td colspan=\"3\">&nbsp;</td>");
        appendln(buffer, "</tr>");
        appendln(buffer, "<tr class=\"navbar\">");

        append(buffer, "<td align=\"left\" width=\"33.33%\" class=\"last-updated-date\">");
        append(buffer, get(documentI18n, "Last updated:"));
        append(buffer, " ");

        appendLastUpdatedDate(buffer, documentI18n, date);

        appendln(buffer, "</td>");

        append(buffer, "<td align=\"center\" width=\"33.33%\" class=\"list-info\" id=\"listInfo\">");
        append(buffer, "<span class=\"list-info\" id=\"listText\">&nbsp;</span>");
        append(buffer, "<span class=\"list-info\" id=\"listInfoDoc\">&nbsp;</span>");
        append(buffer, "<span class=\"list-info\" id=\"ofText\">&nbsp;</span>");
        append(buffer, "<span class=\"list-info\" id=\"listInfoDocTotal\">&nbsp;</span>");
        append(buffer, "</td>");

        append(buffer, "<td align=\"right\" width=\"33.33%\" class=\"pageInfo\">");
        append(buffer, "<div align=\"right\" id=\"pageInfo\">");
        append(buffer, get(documentI18n, "Page"));
        append(buffer, " 1 ");
        append(buffer, get(documentI18n, "of"));
        append(buffer, " 1</div>");
        appendln(buffer, "</td>");

        appendln(buffer, "<td width=\"10\">&nbsp;</td>");
        appendln(buffer, "</tr>");
        appendln(buffer, "</table>");
        appendln(buffer, "<br/>");
        appendln(buffer, "<center>");

        // Start headingText Inline

        append(buffer, "<div class=\"heading\">");
        append(buffer, heading);
        appendln(buffer, "</div>");

        // append(buffer, "<img src=\"heading?width=");
        // append(buffer, headingWidth);
        // append(buffer, "&height=80&x=5&y=60&fontSize=50&text=");
        // append(buffer, heading);
        // appendln(buffer, "/>");

        // End headingText Inline

        appendln(buffer, "</center>");
        appendln(buffer, "<br/>");
        appendln(buffer, "</div>");
    }

    //        
    // footer.vm
    // 

    /**
     * Append the document footer
     * 
     * @param buffer
     */
    protected void appendFooter(StringBuffer buffer) {
        appendln(buffer, "</body>");
        appendln(buffer, "</html>");
    }

    //
    // def.vm
    //        

    /**
     * @return the path root
     */
    protected String getPathRoot() {
        return "/PublicDisplay";
    }

    /**
     * @return true if the document contains no data
     */
    protected boolean isEmpty(DisplayDocument document) {
        if (document != null) {
            Data data = document.getData();
            if (data != null && !data.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the number of defendants than can be rendered without overspill
     */
    protected int getMaxDefendantsWithoutOverspill() {
        return 16;
    }
    
    protected int displayableDefendants(Collection<DefendantName> nameCollection) {
        int displayableDefendants = 0;
        for (DefendantName defendantName : nameCollection) {
            if (!defendantName.isHideInPublicDisplay()) {
                displayableDefendants++;
            }
        }
        return displayableDefendants;
    }
    
    protected boolean isDefendantNamesShouldOverspill(Collection<DefendantName> nameCollection) {
        return displayableDefendants(nameCollection) > getMaxDefendantsWithoutOverspill();
    }

    /**
     * Append no information text to buffer
     */
    protected void appendNoInformation(StringBuffer buffer, TranslationBundle documentI18n) {
        append(buffer, "<div class=\"no-information\">");
        append(buffer, get(documentI18n, "No Information To Display"));
        appendln(buffer, "</div>");
    }

    /**
     * Append the judges name
     */
    protected void appendJudgeName(StringBuffer buffer, String name) {
        append(buffer, "<td class=\"judge\">");
        appendSpace(buffer, name);
        appendln(buffer, "</td>");
    }

    /**
     * Append the case number
     */
    protected void appendCaseNumberNoRestrictions(StringBuffer buffer, Object item) {
        append(buffer, "<td class=\"case-number\">");
        appendSpace(buffer, getCaseNumber(item));
        appendln(buffer, "</td>");
    }

    protected void appendCaseNumber(StringBuffer buffer, Object item) {
        append(buffer, "<td class=\"case-number\">");
        appendCaseNumberText(buffer, item);
        appendln(buffer, "</td>");
    }

    protected void appendCaseNumberText(StringBuffer buffer, Object item) {
        // Note this does not update the global $restrictionsApplyToThisList
        // This functionality should be implemented in the calling method
        if (isReportingRestricted(item)) {
            appendAsterix(buffer, getCaseNumber(item));
        } else {
            appendSpace(buffer, getCaseNumber(item));
        }
    }

    /**
     * Append the hearing description
     */
    protected void appendHearingDescription(StringBuffer buffer, String description) {
        append(buffer, "<td class=\"hearing-description\">");
        appendSpace(buffer, description);
        appendln(buffer, "</td>");
    }

    /**
     * Append the space if text is null
     */
    protected void appendSpace(StringBuffer buffer, Object text) {
        if (text != null) {
            String actualText = text.toString();
            if (!actualText.equals("")) {
                append(buffer, actualText);
            } else {
                append(buffer, "&nbsp;");
            }
        } else {
            append(buffer, "&lt;null&gt;");
        }
    }

    /**
     * Append thew show restrictions text if flag set
     */
    protected void appendShowRestrictions(StringBuffer buffer, TranslationBundle documentI18n,
            boolean restrictionsApplyToThisList) {
        if (restrictionsApplyToThisList) {
            append(buffer, "<div id=\"reportingRestrictions\" class=\"reporting-restrictions\">* ");
            append(buffer, get(documentI18n, "Reporting Restrictions apply see Court Manager for details."));
            appendln(buffer, "</div>");
        }
    }

    /**
     * Append the text followed by an asterix
     * 
     * @param buffer
     * @param text
     */
    protected void appendAsterix(StringBuffer buffer, Object text) {
        append(buffer, text, "${text}");
        appendln(buffer, "<span class=\"asterix\">*</span>");
    }

    /**
     * Append the not before time text
     * 
     * @param buffer
     * @param text
     */
    protected void appendNotBeforeTime(StringBuffer buffer, String time) {
        append(buffer, "<td class=\"not-before-time\">");
        appendSpace(buffer, time);
        appendln(buffer, "</td>");
    }

    /**
     * Append the not before judges name
     * 
     * @param buffer
     * @param name
     */
    protected void appendJudgeNameRestrictedWidth(StringBuffer buffer, String name) {
        append(buffer, "<td class=\"judge\"><div class=\"judge-name-restricted-size\">");
        appendSpace(buffer, name);
        appendln(buffer, "</div></td>");
    }

    /**
     * Emphasize the text
     */
    protected void appendEmphasize(StringBuffer buffer, String text) {
        append(buffer, "<div class=\"emphasized\">");
        append(buffer, text);
        append(buffer, "</div>");
    }

    protected void appendHearingProgress(StringBuffer buffer, TranslationBundle documentI18n, int hearingProgress) {
        append(buffer, "<td class=\"hearing-progress\">");
        appendHearingProgressText(buffer, documentI18n, hearingProgress);
        appendln(buffer, "</td>");
    }

    /**
     * Append the hearing progress
     * 
     * @param buffer
     * @param hearingProgress
     */
    protected void appendHearingProgressText(StringBuffer buffer, TranslationBundle documentI18n, int hearingProgress) {
        switch (hearingProgress) {
        case 0:
            append(buffer, get(documentI18n, "To be heard"));
            break;
        case 5:
            appendEmphasize(buffer, get(documentI18n, "In Progress"));
            break;
        case 8:
            appendEmphasize(buffer, get(documentI18n, "Adjourned"));
            break;
        case 9:
            appendEmphasize(buffer, get(documentI18n, "Finished"));
            break;
        default:
        	log.debug("Invalid hearing progress of " + hearingProgress + " specified.");
            appendSpace(buffer, "");
            break;
        }
    }

    /**
     * Append the court room name if assigned else append unassigned text
     * 
     * @param buffer
     * @param documentI18n
     * @param item
     */
    protected void appendCourtRoomNameOrUnassigned(StringBuffer buffer, TranslationBundle documentI18n, Object item) {
        if (isFloating(item)) {
            append(buffer, "<td class=\"court-room-name\">");
            appendSpace(buffer, get(documentI18n, "Unassigned"));
            appendln(buffer, "</td>");
        } else {
            append(buffer, "<td class=\"court-room-name\">");
            appendln(buffer,
                    "<div style=\"word-wrap:break-word;overflow:auto\" class=\"court_room_name_restricted_size\">");
            appendSpace(buffer, getCourtSiteRoomName(item, documentI18n));
            appendln(buffer, "</div>");
            appendln(buffer, "</td>");
        }
    }

    /**
     * Append defendant overspill if required
     * 
     * @param buffer
     * @param item
     * @param rowType
     */
    protected void appendDefendantOverspill(StringBuffer buffer, Object item, String rowType) {
        Collection<DefendantName> nameCollection = getDefendantNames(item);
        if (isDefendantNamesShouldOverspill(nameCollection)) {
            append(buffer, "<tr class=\"");
            append(buffer, rowType);
            appendln(buffer, "\">");
            append(buffer, "<td class=\"defendant-overspill\" colspan=\"100\">");
            boolean firstDefendant = true;
            for (DefendantName defendantName : nameCollection) {
                if (defendantName.isHideInPublicDisplay()) {
                    continue;
                }
                if (firstDefendant) {
                    firstDefendant = false;
                } else {
                    append(buffer, ", ");
                }
                appendDefendantOverspillName(buffer, defendantName);
            }
            appendln(buffer, "</td>");
            appendln(buffer, "</tr>");
        }
    }

    private void appendDefendantOverspillName(StringBuffer buffer, Object name) {
        append(buffer, name, "${name}");
    }

    /**
     * Append the court room name
     * 
     * @param buffer
     * @param hearingProgress
     */
    protected void appendCourtRoomName(StringBuffer buffer, String name) {

        append(buffer, "<td class=\"court-room-name\">");
        appendln(buffer, "<div style=\"word-wrap:break-word;overflow:auto\" class=\"court_room_name-restricted-size\">");
        appendSpace(buffer, name);
        appendln(buffer, "</div>");
        appendln(buffer, "</td>");

    }

    /**
     * Append the no information row
     * 
     * @param buffer
     * @param hearingProgress
     */
    protected void appendNoInfomationRow(StringBuffer buffer, TranslationBundle documentI18n) {
        append(buffer, "<td class=\"no-information-row\">");
        appendSpace(buffer, get(documentI18n, "No Information To Display"));
        appendln(buffer, "</td>");
    }

    protected void appendDefendantName(StringBuffer buffer, Object item) {      
        // Name is passed in template, but it then checks item for reporting
        // restrictions!
        String name = getNameWithSurnameFirst(item);

        // Note this method does not set $defendantNamesRequireOverspill this
        // should be delt with by the calling method
        append(buffer, "<td class=\"defendant-name\">");
        if (isHideInPublicDisplay(item)) {
            append(buffer, "&nbsp;");
        } else if (isReportingRestricted(item)) {
            appendAsterix(buffer, name);
        } else {
            appendSpace(buffer, name);
        }
        appendln(buffer, "</td>");
    }

    protected void appendDefendantNamesRestrictedSize(StringBuffer buffer, TranslationBundle documentI18n,
            Collection<DefendantName> nameCollection) {
        append(buffer, "<td>");
        appendDefendants(buffer, documentI18n, nameCollection);
        appendln(buffer, "</td>");
    }

    protected void appendDefendants(
            StringBuffer buffer, 
            TranslationBundle documentI18n, 
            Collection<DefendantName> nameCollection) {
        // Note this method does not set $defendantNamesRequireOverspill this
        // should be delt with by the calling method
        if (nameCollection != null) {
            if (isDefendantNamesShouldOverspill(nameCollection)) {
                appendEmphasize(buffer, get(documentI18n, "SEE BELOW"));
                appendln(buffer);
            } else {
                int displayedNames = 0;
                for (DefendantName name : nameCollection) {
                    if (name.isHideInPublicDisplay()) {
                        continue;
                    }
                    displayedNames++;
                    append(buffer,
                            "<div align= 'left' style=\"word-wrap:break-word;overflow:auto\" class=\"defendant-name-restricted-size-250\">");
                    append(buffer, name, "${name}");
                    appendln(buffer, "</div>");
                }
                if (displayedNames == 0) {
                    appendln(buffer, "&nbsp;");
                }
            }
        } else {
            appendln(buffer, "&nbsp;");
        }
    }

    protected void appendDefendantsOrTitle(StringBuffer buffer, TranslationBundle documentI18n, Object item) {
        if (hasDefendants(item)) {
            appendDefendantNamesRestrictedSize(buffer, documentI18n, getDefendantNames(item));
        } else {
            String caseTitle = getCaseTitle(item);
            if (caseTitle != null) {
                append(buffer, "<td class=\"case-title\">");
                appendSpace(buffer, caseTitle);
                appendln(buffer, "</td>");
            } else {
                append(buffer, "<td class=\"case-title\">");
                appendSpace(buffer, "");
                appendln(buffer, "</td>");
            }
        }
    }

    //
    // Odd even vm
    //
    /**
     * Get the row type, even if even otherwise odd.
     */
    protected String getRowType(String rowType) {
        // WDF: This will give the first row as even as per original then
        // alternate! Logically the first row should be odd, depends if the
        // table header is included.
        return "evenRow".equals(rowType) ? "oddRow" : "evenRow";
    }

    //
    // Misc utils Display Document
    //

    /**
     * Get the date, this is added to the velocity context in
     * AbstractTemplateRenderer. Should be called once per render!
     * 
     * @return the current date
     */
    protected Date getDate() {
        return new Date();
    }

    protected String get(TranslationBundle documentI18n, String key, String context) {
        if (key == null) {
            return null;
        }
        if (key.equals("")) {
            return "";
        }
        String value = documentI18n.getTranslation(key, context);
        if (value != null) {
            return value;
        }
        // Default behaviour is to return the key with missing translation
        // marker
        return key + MISSING_TRANSLATION_MARKER;
    }

    protected String get(TranslationBundle documentI18n, String key) {
        if (key == null) {
            return null;
        }
        if (key.equals("")) {
            return "";
        }
        String value = documentI18n.getTranslation(key);
        if (value != null) {
            return value;
        }
        // Default behaviour is to return the key with missing translation
        // marker
        return key + MISSING_TRANSLATION_MARKER;
    }

    /**
     * Get the resouce bundle to use, this is added to the velocity context in
     * AbstractTemplateRenderer. Should be called once per render!
     * 
     * @return the resource bundle
     */
    protected TranslationBundle geDocumentI18n(DisplayDocument displayDocument) {
        TranslationBundle translationBundle = TranslationBundlesCache.getInstance().getTranslationBundles()
                .getTranslationBundle(getLocale(displayDocument));
        if (log.isDebugEnabled()) {
            log.debug("Using bundle " + translationBundle + " for document \"" + displayDocument.getUri()
                    + "\" in Locale \"" + getLocale(displayDocument) + "\".");
        }
        return translationBundle;
    }

    private Locale getLocale(DisplayDocument displayDocument) {
        if (displayDocument != null) {
            AbstractURI uri = displayDocument.getUri();
            if (uri != null) {
                Locale locale = uri.getLocale();
                if (locale != null) {
                    return locale;
                }
            }
        }
        return Locale.getDefault();
    }

    /**
     * Court Name
     * 
     * @param displayDocument
     * @return the court name from the display document date
     */
    protected String getCourtName(DisplayDocument displayDocument, TranslationBundle documentI18n) {
        if (displayDocument != null) {
            Data data = displayDocument.getData();
            if (data != null) {
                String courtName = data.getCourtName();
                if (courtName != null) {
                    return get(documentI18n, courtName);
                }
            }
        }
        return null;
    }

    /**
     * Court Room Name
     * 
     * @param displayDocument
     * @param documentI18n
     * @return the court room name from the display document
     */
    protected String getCourtRoomName(DisplayDocument displayDocument, TranslationBundle documentI18n) {
        if (displayDocument != null) {
            Data data = displayDocument.getData();
            if (data != null) {
                String courtRoomName = data.getCourtRoomName();
                if (courtRoomName != null) {
                    return get(documentI18n, courtRoomName);
                }
            }
        }
        return null;
    }

    /**
     * Court Site Short Name
     * 
     * @param displayDocument
     * @param documentI18n
     * @return The court site short name from the display document
     */
    protected String getCourtSiteShortName(DisplayDocument displayDocument, TranslationBundle documentI18n) {
        if (displayDocument != null) {
            Data data = displayDocument.getData();
            if (data != null) {
                String courtSiteShortName;
                courtSiteShortName = data.getCourtSiteShortName();
                if (courtSiteShortName != null) {
                    return get(documentI18n, courtSiteShortName);
                }
            }
        }
        return null;
    }

    /**
     * Court Site Room Name
     * 
     * @param displayDocument
     * @param documentI18n
     * @return The Court room name prefixed with site if exists
     */
    protected String getCourtSiteRoomName(DisplayDocument displayDocument, TranslationBundle documentI18n) {
        String courtSiteShortName = getCourtSiteShortName(displayDocument, documentI18n);
        if (courtSiteShortName != null) {
            return courtSiteShortName + " - " + getCourtRoomName(displayDocument, documentI18n);
        } else {
            return getCourtRoomName(displayDocument, documentI18n);
        }
    }

    /**
     * 
     * @param displayDocument
     * @return the table from the display document data
     */
    protected Collection getTable(DisplayDocument displayDocument) {
        if (displayDocument != null) {
            Data data = displayDocument.getData();
            if (data != null) {
                Collection table = data.getTable();
                if (table != null) {
                    return table;
                }
            }
        }
        return new ArrayList(0);
    }

    //
    // Mixed Access
    //        

    protected String getCaseNumber(Object item) {
        if (item instanceof AllCourtStatusValue) {
            String caseNumber = ((AllCourtStatusValue) item).getCaseNumber();
            if (caseNumber != null) {
                return caseNumber;
            }
        } else if (item instanceof AllCaseStatusValue) {
            String caseNumber = ((AllCaseStatusValue) item).getCaseNumber();
            if (caseNumber != null) {
                return caseNumber;
            }
        } else if (item instanceof CourtListValue) {
            String caseNumber = ((CourtListValue) item).getCaseNumber();
            if (caseNumber != null) {
                return caseNumber;
            }
        }
        return null;
    }

    protected int getHearingProgress(Object item) {
        if (item instanceof AllCaseStatusValue) {
            return ((AllCaseStatusValue) item).getHearingProgress();
        } else if (item instanceof CourtListValue) {
            return ((CourtListValue) item).getHearingProgress();
        }
        return -1;
    }

    protected Collection<DefendantName> getDefendantNames(Object item) {
        if (item instanceof AllCourtStatusValue) {
            Collection<DefendantName> defendantNames = ((AllCourtStatusValue) item).getDefendantNames();
            if (defendantNames != null) {
                return defendantNames;
            }
        } else if (item instanceof CourtListValue) {
            Collection<DefendantName> defendantNames = ((CourtListValue) item).getDefendantNames();
            if (defendantNames != null) {
                return defendantNames;
            }
        }
        return new ArrayList<DefendantName>(0);
    }

    protected String getCaseTitle(Object item) {
        if (item instanceof AllCaseStatusValue) {
            String caseTitle = ((AllCaseStatusValue) item).getCaseTitle();
            if (caseTitle != null) {
                return caseTitle;
            }
        } else if (item instanceof CourtListValue) {
            String caseTitle = ((CourtListValue) item).getCaseTitle();
            if (caseTitle != null) {
                return caseTitle;
            }
        }
        // Acceptable for both case title and defendants not to exist
        return "";
    }

    protected String getHearingDescription(TranslationBundle documenti18n, Object item) {
        if (item instanceof AllCaseStatusValue) {
            String description = ((AllCaseStatusValue) item).getHearingDescription();
            if (description != null) {
                return get(documenti18n, description);
            }
        } else if (item instanceof CourtDetailValue) {
            String description = ((CourtDetailValue) item).getHearingDescription();
            if (description != null) {
                return get(documenti18n, description);
            }
        } else if (item instanceof CourtListValue) {
            String description = ((CourtListValue) item).getHearingDescription();
            if (description != null) {
                return get(documenti18n, description);
            }
        }
        return null;
    }

    protected String getJudgeName(TranslationBundle documenti18n, Object item) {
        if (item instanceof CourtDetailValue) {
            JudgeName name = ((CourtDetailValue) item).getJudgeName();
            if (name != null) {
                return get(documenti18n, name.toString());
            }
        } else if (item instanceof JuryStatusDailyListValue) {
            JudgeName name = ((JuryStatusDailyListValue) item).getJudgeName();
            if (name != null) {
                return get(documenti18n, name.toString());
            }
        }
        return null;
    }

    /**
     * @param item
     *            the SummaryByNameValue to check
     * @return true if reporting is restricted for this item
     */
    protected boolean isReportingRestricted(Object item) {
        if (item instanceof SummaryByNameValue) {
            return ((SummaryByNameValue) item).isReportingRestricted();
        } else if (item instanceof CourtListValue) {
            return ((CourtListValue) item).isReportingRestricted();
        } else if (item instanceof AllCourtStatusValue) {
            return ((AllCourtStatusValue) item).isReportingRestricted();
        }
        return false;
    }

    //
    // SummaryByNameValue Access
    //    

    /**
     * @param item
     *            the SummaryByNameValue to check
     * @return get the name with surname first
     */
    protected String getNameWithSurnameFirst(Object item) {
        if (item instanceof SummaryByNameValue) {
            DefendantName name = ((SummaryByNameValue) item).getDefendantName();
            if (name != null) {
                String nameWithSurnameFirst = name.getNameWithSurnameFirst();
                if (nameWithSurnameFirst != null) {
                    return nameWithSurnameFirst;
                }
            }
        }
        return null;
    }

    /**
     * @param item
     *            the SummaryByNameValue to check
     * @return true if this item is floating
     */
    protected boolean isFloating(Object item) {
        if (item instanceof SummaryByNameValue) {
            return ((SummaryByNameValue) item).isFloating();
        } else if (item instanceof JuryStatusDailyListValue) {
            return ((JuryStatusDailyListValue) item).isFloating();
        }
        return false;
    }

    /**
     * @param item
     * 
     * @return the defendant name.
     */
    protected String getDefendantName(Object item) {
        if (item instanceof SummaryByNameValue) {
            DefendantName defendantName = ((SummaryByNameValue) item).getDefendantName();
            if (defendantName != null) {
                return defendantName.toString();
            }
        }
        return null;
    }
    
    protected boolean isHideInPublicDisplay(Object item) {
        if (item instanceof SummaryByNameValue) {
            DefendantName defendantName = ((SummaryByNameValue) item).getDefendantName();
            if (defendantName != null) {
                return defendantName.isHideInPublicDisplay();
            }
        } else if (item instanceof DefendantName) {
            return ((DefendantName)item).isHideInPublicDisplay();
        }
        
        return false;
    }
    
    protected boolean isHideInPublicDisplay(Integer defOnCaseId, Collection <DefendantName> names){
    	if(names != null && defOnCaseId != null){
    		for (Object object : names){
    			DefendantName name = (DefendantName) object;
    			if (defOnCaseId.intValue() == name.getDefendantOnCaseId()){
    				return name.isHideInPublicDisplay();
    			}
    		}
    	}
    	return false;
    }

    protected String getEventTimeAsString(Object item) {
        if (item instanceof PublicDisplayValue) {
            String eventTime = ((PublicDisplayValue) item).getEventTimeAsString();
            if (eventTime != null) {
                return eventTime;
            }
        }
        return null;
    }

    protected BranchEventXMLNode getEvent(Object item) {
        if (item instanceof PublicDisplayValue) {
            Object event = ((PublicDisplayValue) item).getEvent();
            if (event != null) {
                return (BranchEventXMLNode) event;
            }
        }
        return null;
    }

    protected boolean hasEvent(Object item) {
        if (item instanceof PublicDisplayValue) {
            return ((PublicDisplayValue) item).getEvent() != null;
        }
        return false;
    }

    protected void appendLastUpdatedDate(StringBuffer buffer, TranslationBundle documentI18n, Date date) {
        // Example format Thu Nov 17 12:33:51 GMT 2005
        if (date != null) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            // Append Day
            appendDay(buffer, documentI18n, calendar);
            append(buffer, SPACE);
            appendMonth(buffer, documentI18n, calendar);
            append(buffer, SPACE);
            append(buffer, calendar.get(Calendar.DAY_OF_MONTH));
            append(buffer, SPACE);
            append(buffer, calendar.get(Calendar.HOUR_OF_DAY));
            append(buffer, COLON);
            append(buffer, get2digits(calendar.get(Calendar.MINUTE)));
            append(buffer, COLON);
            append(buffer, get2digits(calendar.get(Calendar.SECOND)));
            append(buffer, SPACE);
            append(buffer, get(documentI18n, calendar.getTimeZone().getDisplayName(false, TimeZone.SHORT)));
            append(buffer, SPACE);
            append(buffer, calendar.get(Calendar.YEAR));
        } else {
            // Do we need through exception or not appenda anything??
        }
    }

    private String get2digits(int valueIn) {
        log.debug("getMinutes = " + (100 + valueIn + "").substring(1, 2));
        return (100 + valueIn + "").substring(1, 3);
    }

    // Append translated Day
    private void appendDay(StringBuffer buffer, TranslationBundle documentI18n, Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.MONDAY:
            append(buffer, get(documentI18n, "Mon"));
            break;
        case Calendar.TUESDAY:
            append(buffer, get(documentI18n, "Tue"));
            break;
        case Calendar.WEDNESDAY:
            append(buffer, get(documentI18n, "Wed"));
            break;
        case Calendar.THURSDAY:
            append(buffer, get(documentI18n, "Thu"));
            break;
        case Calendar.FRIDAY:
            append(buffer, get(documentI18n, "Fri"));
            break;
        case Calendar.SATURDAY:
            append(buffer, get(documentI18n, "Sat"));
            break;
        case Calendar.SUNDAY:
            append(buffer, get(documentI18n, "Sun"));
            break;
        default:
        }
    }

    // Append translated Month
    private void appendMonth(StringBuffer buffer, TranslationBundle documentI18n, Calendar calendar) {

        switch (calendar.get(Calendar.MONTH)) {
        case Calendar.JANUARY:
            append(buffer, get(documentI18n, "Jan"));
            break;
        case Calendar.FEBRUARY:
            append(buffer, get(documentI18n, "Feb"));
            break;
        case Calendar.MARCH:
            append(buffer, get(documentI18n, "Mar"));
            break;
        case Calendar.APRIL:
            append(buffer, get(documentI18n, "Apr"));
            break;
        case Calendar.MAY:
            append(buffer, get(documentI18n, "May"));
            break;
        case Calendar.JUNE:
            append(buffer, get(documentI18n, "Jun"));
            break;
        case Calendar.JULY:
            append(buffer, get(documentI18n, "Jul"));
            break;
        case Calendar.AUGUST:
            append(buffer, get(documentI18n, "Aug"));
            break;
        case Calendar.SEPTEMBER:
            append(buffer, get(documentI18n, "Sep"));
            break;
        case Calendar.OCTOBER:
            append(buffer, get(documentI18n, "Oct"));
            break;
        case Calendar.NOVEMBER:
            append(buffer, get(documentI18n, "Nov"));
            break;
        case Calendar.DECEMBER:
            append(buffer, get(documentI18n, "Dec"));
            break;
        default:
            append(buffer, "???");
            break;
        }
    }

    protected void appendEvent(StringBuffer buffer, Object item, TranslationBundle documentI18n) {
        Object event = null;
        Collection <DefendantName>defendantNames = null;
        // Retrieve event
        if (item instanceof AllCourtStatusValue) {
            AllCourtStatusValue value = (AllCourtStatusValue) item;
        	event = value.getEvent();
            defendantNames = ((AllCourtStatusValue) item).getDefendantNames();
        } else if (item instanceof AllCaseStatusValue) {
            log.debug("All Case Status item");
            AllCaseStatusValue value = ((AllCaseStatusValue) item); 
            event = value.getEvent();
            defendantNames = new ArrayList<DefendantName>();
            defendantNames.add(value.getDefendantName());
            log.debug("Event: " + event);
        }
        
        // Process event
        if (event != null && event instanceof BranchEventXMLNode) {
            BranchEventXMLNode branchNode = (BranchEventXMLNode) event;
            // Check event type
            if (branchNode.get("type") != null) {
                String type = ((LeafEventXMLNode) branchNode.get("type")).getValue();

                log.debug("Type: " + type);

                if ("10100".equals(type)) {
                    appendEvent10100(buffer, branchNode, documentI18n);
                } else if ("10500".equals(type)) {
                    appendEvent10500(buffer, branchNode, documentI18n);
                } else if ("20502".equals(type)) {
                    appendEvent20502(buffer, branchNode, documentI18n);
                } else if ("20602".equals(type)) {
                    appendEvent20602(buffer, branchNode, documentI18n);
                } else if ("20603".equals(type)) {
                    appendEvent20603(buffer, branchNode, documentI18n);
                } else if ("20604".equals(type)) {
                    appendEvent20604(buffer, branchNode, documentI18n);
                } else if ("20605".equals(type)) {
                    appendEvent20605(buffer, branchNode, documentI18n);
                } else if ("20606".equals(type)) {
                    appendEvent20606(buffer, branchNode, documentI18n,defendantNames);
                } else if ("20607".equals(type)) {
                    appendEvent20607(buffer, branchNode, documentI18n);
                } else if ("20608".equals(type)) {
                    appendEvent20608(buffer, branchNode, documentI18n);
                } else if ("20609".equals(type)) {
                    appendEvent20609(buffer, branchNode, documentI18n);
                } else if ("20610".equals(type)) {
                    appendEvent20610(buffer, branchNode, documentI18n);
                } else if ("20611".equals(type)) {
                    appendEvent20611(buffer, branchNode, documentI18n);
                } else if ("20612".equals(type)) {
                    appendEvent20612(buffer, branchNode, documentI18n);
                } else if ("20613".equals(type)) {
                    appendEvent20613(buffer, branchNode, documentI18n);
                } else if ("20901".equals(type)) {
                    appendEvent20901(buffer, branchNode, documentI18n);
                } else if ("20902".equals(type)) {
                    appendEvent20902(buffer, branchNode, documentI18n);
                } else if ("20903".equals(type)) {
                    appendEvent20903(buffer, branchNode, documentI18n);
                } else if ("20904".equals(type)) {
                    appendEvent20904(buffer, branchNode, documentI18n);
                } else if ("20905".equals(type)) {
                    appendEvent20905(buffer, branchNode, documentI18n);
                } else if ("20906".equals(type)) {
                    appendEvent20906(buffer, branchNode, documentI18n, defendantNames);
                } else if ("20907".equals(type)) {
                    appendEvent20907(buffer, branchNode, documentI18n);
                } else if ("20908".equals(type)) {
                    appendEvent20908(buffer, branchNode, documentI18n);
                } else if ("20909".equals(type)) {
                    appendEvent20909(buffer, branchNode, documentI18n, defendantNames);
                } else if ("20910".equals(type)) {
                    appendEvent20910(buffer, branchNode, documentI18n, defendantNames);
                } else if ("20911".equals(type)) {
                    appendEvent20911(buffer, branchNode, documentI18n);
                } else if ("20912".equals(type)) {
                    appendEvent20912(buffer, branchNode, documentI18n);
                } else if ("20914".equals(type)) {
                    appendEvent20914(buffer, branchNode, documentI18n);
                } else if ("20916".equals(type)) {
                    appendEvent20916(buffer, branchNode, documentI18n);
                } else if ("20917".equals(type)) {
                    appendEvent20917(buffer, branchNode, documentI18n);
                } else if ("20918".equals(type)) {
                    appendEvent20918(buffer, branchNode, documentI18n);
                } else if ("20919".equals(type)) {
                    appendEvent20919(buffer, branchNode, documentI18n);
                } else if ("20920".equals(type)) {
                    appendEvent20920(buffer, branchNode, documentI18n);
                } else if ("20931".equals(type)) {
                    appendEvent20931(buffer, branchNode, documentI18n);
                } else if ("20932".equals(type)) {
                    appendEvent20932(buffer, branchNode, documentI18n);
                } else if ("20935".equals(type)) {
                    appendEvent20935(buffer, branchNode, documentI18n);
                } else if ("21100".equals(type)) {
                    appendEvent21100(buffer, branchNode, documentI18n);
                } else if ("21200".equals(type)) {
                    appendEvent21200(buffer, branchNode, documentI18n);
                } else if ("21201".equals(type)) {
                    appendEvent21201(buffer, branchNode, documentI18n);
                } else if ("30100".equals(type)) {
                    appendEvent30100(buffer, branchNode, documentI18n);
                } else if ("30200".equals(type)) {
                    appendEvent30200(buffer, branchNode, documentI18n, defendantNames);
                } else if ("30300".equals(type)) {
                    appendEvent30300(buffer, branchNode, documentI18n);
                } else if ("30400".equals(type)) {
                    appendEvent30400(buffer, branchNode, documentI18n);
                } else if ("30500".equals(type)) {
                    appendEvent30500(buffer, branchNode, documentI18n);
                } else if ("30600".equals(type)) {
                    appendEvent30600(buffer, branchNode, documentI18n, defendantNames);
                } else if ("31000".equals(type)) {
                    appendEvent31000(buffer, branchNode, documentI18n);
                } else if ("32000".equals(type)) {
                    appendEvent32000(buffer, branchNode, documentI18n);
                } else if ("40601".equals(type)) {
                    appendEvent40601(buffer, branchNode, documentI18n);
                } else if ("CPP".equals(type)) {
                    appendEventCPP(buffer, branchNode, documentI18n);
                } else {
                    log.error("Unrecognised Event Type: " + type);
                }
            }
        }
    }

    protected void appendFormattedDate(StringBuffer buffer, LeafEventXMLNode leaf, TranslationBundle documentI18n) {
    	append(buffer, leaf.getDay());
        append(buffer, "-");
        append(buffer, get(documentI18n, leaf.getMonth()));
        append(buffer, "-");
        append(buffer, leaf.getYear());
    }

    // OK
    protected void appendEvent10100(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Case Started"));
    }

    // OK
    protected void appendEvent10500(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Resume"));
    }

    // Not used . Not tested
    protected void appendEvent20502(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        // ${document.get('Pleas and Directions Hearing on')}
        // #formatDate($event.E20502_P_And_D_Hearing_On_Date)
        append(buffer, get(documentI18n, "Pleas and Directions Hearing on"));
        append(buffer, SPACE);
        appendFormattedDate(buffer, (LeafEventXMLNode) node.get("E20502_P_And_D_Hearing_On_Date"), documentI18n);
    }

    // OK
    protected void appendEvent20602(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Respondent Case Opened"));
    }

    protected void appendEvent20603(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        BranchEventXMLNode wsOptions = (BranchEventXMLNode) node.get("E20603_Witness_Sworn_Options");
        String wsList = ((LeafEventXMLNode) wsOptions.get("E20603_WS_List")).getValue();

        if ("E20603_Appellant_Sworn".equals(wsList)) {
            append(buffer, get(documentI18n, "Appellant Sworn"));
        } else if ("E20603_Interpreter_Sworn".equals(wsList)) {
            append(buffer, get(documentI18n, "Interpreter Sworn"));
        } else {
            if (((LeafEventXMLNode) wsOptions.get("E20603_Witness_No")).getValue() != "") {
                append(buffer, get(documentI18n, "Witness Number"));
                append(buffer, SPACE);
                append(buffer, ((LeafEventXMLNode) wsOptions.get("E20603_Witness_No")).getValue());
                append(buffer, SPACE);
                append(buffer, get(documentI18n, "Sworn"));
            } else {
                append(buffer, get(documentI18n, "Witness Sworn"));
            }
        }
    }

    // OK
    protected void appendEvent20604(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness evidence concluded"));
    }

    // OK
    protected void appendEvent20605(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Respondent Case Closed"));
    }

    // OK
    protected void appendEvent20606(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n, Collection<DefendantName> nameCollection) {
        // ${document.get('Appellant')} $event.E20606_Appellant_CO_Name.value
        // ${document.get('Case Opened')}
        append(buffer, get(documentI18n, "Appellant"));
        append(buffer, SPACE);
        
        // Try to read the defendant_on_case_id node and parse it as an Integer.
        Integer defOnCaseId = null;
        Object defOnCaseObj = node.get("defendant_on_case_id");
        if (defOnCaseObj instanceof LeafEventXMLNode) {
            LeafEventXMLNode leaf = (LeafEventXMLNode) defOnCaseObj;
            String raw = leaf.getValue();
            if (raw != null && raw.trim().length() > 0) {
                try {
                    // Use valueOf/parseInt, but guard NumberFormatException
                    defOnCaseId = Integer.valueOf(raw.trim());
                } catch (NumberFormatException nfe) {
                    // Malformed number: treat as missing rather than throwing.
                    log.warn("Invalid defendant_on_case_id: " + raw, nfe);
                    defOnCaseId = null;
                }
            }
        } else if (defOnCaseObj != null) {
            // There is an object, but it's not the expected type: ignore it (and optionally log).
            log.debug("appendEvent20606: defendant_on_case_id present but not LeafEventXMLNode: " + defOnCaseObj.getClass());
        }
        
        if (defOnCaseId != null) {
	        if(!isHideInPublicDisplay(defOnCaseId, nameCollection)){
	        	append(buffer, ((LeafEventXMLNode) node.get("E20606_Appellant_CO_Name")).getValue());
	        }
        } else {
        	log.warn("appendEvent20606: defOnCaseId is null");
        }
        
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Case Opened"));
    }

    // OK
    protected void appendEvent20607(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Appellant Submissions"));
    }

    // OK
    protected void appendEvent20608(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Appellant Case Closed"));
    }

    // OK
    protected void appendEvent20609(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Bench Retire to consider Judgment"));
    }

    // OK
    protected void appendEvent20610(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Judgement"));
    }

    protected void appendEvent20611(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Legal Submissions"));
    }

    protected void appendEvent20612(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Interpreter Sworn"));
    }

    // OK
    protected void appendEvent20613(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness Number"));
        append(buffer, SPACE);
        append(buffer, ((LeafEventXMLNode) node.get("E20613_Witness_Number")).getValue());
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Continues"));
    }

    // Not sure if no longer used
    protected void appendEvent20901(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        BranchEventXMLNode teOptions = (BranchEventXMLNode) node.get("E20901_Time_Estimate_Options");
        String teoUnit = ((LeafEventXMLNode) teOptions.get("E20901_TEO_units")).getValue();

        if ("E20901_days".equals(teoUnit)) {
            append(buffer, get(documentI18n, "Trial Time Estimate:"));
            append(buffer, SPACE);
            append(buffer, ((LeafEventXMLNode) teOptions.get("E20901_TEO_time")).getValue());
            append(buffer, SPACE);
            append(buffer, get(documentI18n, "Day(s)"));
        } else if ("E20901_weeks".equals(teoUnit)) {
            append(buffer, get(documentI18n, "Trial Time Estimate:"));
            append(buffer, SPACE);
            append(buffer, ((LeafEventXMLNode) teOptions.get("E20901_TEO_time")).getValue());
            append(buffer, SPACE);
            append(buffer, get(documentI18n, "Week(s)"));
        } else if ("E20901_months".equals(teoUnit)) {
            append(buffer, get(documentI18n, "Trial Time Estimate:"));
            append(buffer, SPACE);
            append(buffer, ((LeafEventXMLNode) teOptions.get("E20901_TEO_time")).getValue());
            append(buffer, SPACE);
            append(buffer, get(documentI18n, "Month(s)"));
        } else {
        	log.debug("Invalid E20901_TEO_units: " + teoUnit + " entered.");
        }
    }

    // OK
    protected void appendEvent20902(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Jury Sworn In"));
    }

    // OK
    protected void appendEvent20903(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        if ("E20903_Prosecution_Opening".equals(((LeafEventXMLNode) ((BranchEventXMLNode) node
                .get("E20903_Prosecution_Case_Options")).get("E20903_PCO_Type")).getValue())) {
            append(buffer, get(documentI18n, "Prosecution Opening"));
        } else {
            append(buffer, get(documentI18n, "Prosecution Case"));
        }
    }

    // OK
    protected void appendEvent20904(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        BranchEventXMLNode wsOptions = (BranchEventXMLNode) node.get("E20904_Witness_Sworn_Options");
        String wsoType = ((LeafEventXMLNode) wsOptions.get("E20904_WSO_Type")).getValue();

        if ("E20904_Defendant_sworn".equals(wsoType)) {
            append(buffer, get(documentI18n, "Defendant Sworn"));
        } else if ("E20904_Interpreter_sworn".equals(wsoType)) {
            append(buffer, get(documentI18n, "Interpreter Sworn"));
        } else {
            if (!"".equals(((LeafEventXMLNode) wsOptions.get("E20904_WSO_Number")).getValue())) {
                append(buffer, get(documentI18n, "Witness Number"));
                append(buffer, SPACE);
                append(buffer, ((LeafEventXMLNode) wsOptions.get("E20904_WSO_Number")).getValue());
                append(buffer, SPACE);
                append(buffer, get(documentI18n, "Sworn"));
            }
        }
    }

    // OK
    protected void appendEvent20905(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness evidence concluded"));
    }

    // OK
    protected void appendEvent20906(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n, Collection<DefendantName> nameCollection) {
        append(buffer, get(documentI18n, "Defence"));
        append(buffer, SPACE);
        
        // Try to read the defendant_on_case_id node and parse it as an Integer.
        Integer defOnCaseId = null;
        Object defOnCaseObj = node.get("defendant_on_case_id");
        if (defOnCaseObj instanceof LeafEventXMLNode) {
            LeafEventXMLNode leaf = (LeafEventXMLNode) defOnCaseObj;
            String raw = leaf.getValue();
            if (raw != null && raw.trim().length() > 0) {
                try {
                    // Use valueOf/parseInt, but guard NumberFormatException
                    defOnCaseId = Integer.valueOf(raw.trim());
                } catch (NumberFormatException nfe) {
                    // Malformed number: treat as missing rather than throwing.
                    log.warn("Invalid defendant_on_case_id: " + raw, nfe);
                    defOnCaseId = null;
                }
            }
        } else if (defOnCaseObj != null) {
            // There is an object, but it's not the expected type: ignore it (and optionally log).
            log.debug("appendEvent20906: defendant_on_case_id present but not LeafEventXMLNode: " + defOnCaseObj.getClass());
        }
        
        if (defOnCaseId != null) {
	        if(!isHideInPublicDisplay(defOnCaseId, nameCollection)){
	        	append(buffer, ((LeafEventXMLNode) node.get("E20906_Defence_CO_Name")).getValue());
	        	append(buffer, SPACE);
	        }                
        } else {
        	log.warn("appendEvent20906: defOnCaseId is null");
        }
        append(buffer, get(documentI18n, "Case Opened", "Defence"));
    }

    // OK
    protected void appendEvent20907(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Prosecution Closing Speech"));
    }

    // OK
    protected void appendEvent20908(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Prosecution Case Closed"));
    }

    // OK
    protected void appendEvent20909(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n, Collection<DefendantName> nameCollection) {

    	// Try to read the defendant_on_case_id node and parse it as an Integer.
        Integer defOnCaseId = null;
        Object defOnCaseObj = node.get("defendant_on_case_id");
        if (defOnCaseObj instanceof LeafEventXMLNode) {
            LeafEventXMLNode leaf = (LeafEventXMLNode) defOnCaseObj;
            String raw = leaf.getValue();
            if (raw != null && raw.trim().length() > 0) {
                try {
                    // Use valueOf/parseInt, but guard NumberFormatException
                    defOnCaseId = Integer.valueOf(raw.trim());
                } catch (NumberFormatException nfe) {
                    // Malformed number: treat as missing rather than throwing.
                    log.warn("Invalid defendant_on_case_id: " + raw, nfe);
                    defOnCaseId = null;
                }
            }
        } else if (defOnCaseObj != null) {
            // There is an object, but it's not the expected type: ignore it (and optionally log).
            log.debug("appendEvent20909: defendant_on_case_id present but not LeafEventXMLNode: " + defOnCaseObj.getClass());
        }
        
        if (defOnCaseId != null) {
	        if(!isHideInPublicDisplay(defOnCaseId, nameCollection)){
	        	append(buffer, ((LeafEventXMLNode) node.get("defendant_name")).getValue());
	            append(buffer, SEMI_COLON);
	            append(buffer, SPACE);
	        }
        } else {
        	log.warn("appendEvent20909: defOnCaseId is null");
        }
        append(buffer, get(documentI18n, "Defence Closing Speech"));
    }

    // OK
    protected void appendEvent20910(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n, Collection<DefendantName> nameCollection) {
        append(buffer, get(documentI18n, "Defence"));
        append(buffer, SPACE);

        // Try to read the defendant_on_case_id node and parse it as an Integer.
        Integer defOnCaseId = null;
        Object defOnCaseObj = node.get("defendant_on_case_id");
        if (defOnCaseObj instanceof LeafEventXMLNode) {
            LeafEventXMLNode leaf = (LeafEventXMLNode) defOnCaseObj;
            String raw = leaf.getValue();
            if (raw != null && raw.trim().length() > 0) {
                try {
                    // Use valueOf/parseInt, but guard NumberFormatException
                    defOnCaseId = Integer.valueOf(raw.trim());
                } catch (NumberFormatException nfe) {
                    // Malformed number: treat as missing rather than throwing.
                    log.warn("Invalid defendant_on_case_id: " + raw, nfe);
                    defOnCaseId = null;
                }
            }
        } else if (defOnCaseObj != null) {
            // There is an object, but it's not the expected type: ignore it (and optionally log).
            log.debug("appendEvent20910: defendant_on_case_id present but not LeafEventXMLNode: " + defOnCaseObj.getClass());
        }
        
        if (defOnCaseId != null) {
	        if(!isHideInPublicDisplay(defOnCaseId, nameCollection)){
	        	append(buffer, ((LeafEventXMLNode) node.get("E20910_Defence_CC_Name")).getValue());
	        	append(buffer, SPACE);
	        }
        } else {
        	log.warn("appendEvent20910: defOnCaseId is null");
        }
        
        append(buffer, get(documentI18n, "Case Closed"));
    }

    // OK
    protected void appendEvent20911(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Summing Up"));
    }

    // OK
    protected void appendEvent20912(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Legal Submissions"));
    }

    // OK
    protected void appendEvent20914(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Jury retire to consider verdict"));
    }

    protected void appendEvent20916(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Legal Submissions"));
    }

    // OK
    protected void appendEvent20917(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Interpreter Sworn"));
    }

    // OK
    protected void appendEvent20918(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Trial Ineffective"));
    }

    // OK
    protected void appendEvent20919(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Verdict to be taken"));
    }

    // OK
    protected void appendEvent20920(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness Number"));
        append(buffer, SPACE);
        append(buffer, ((LeafEventXMLNode) node.get("E20920_Witness_Number")).getValue());
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Continues"));
    }
    
    //  OK
    protected void appendEvent20931(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness Number"));
        append(buffer, SPACE);
        append(buffer, ((LeafEventXMLNode) node.get("E20931_Witness_Number")).getValue());
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Cross Examination"));
    }
    
    //  OK
    protected void appendEvent20932(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness Number"));
        append(buffer, SPACE);
        append(buffer, ((LeafEventXMLNode) node.get("E20932_Witness_Number")).getValue());
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Re-examination"));
    }
    
    // OK
    protected void appendEvent20935(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        BranchEventXMLNode wsOptions = (BranchEventXMLNode) node.get("E20935_Witness_Read_Options");
        String wsoType = ((LeafEventXMLNode) wsOptions.get("E20935_WR_Type")).getValue();

        if ("E20935_Defendant_Read".equals(wsoType)) {
            append(buffer, get(documentI18n, "Defendant Read"));
        } else if ("E20904_Interpreter_sworn".equals(wsoType)) {
            append(buffer, get(documentI18n, "Interpreter Read"));
        } else {
            if (!"".equals(((LeafEventXMLNode) wsOptions.get("E20935_WR_Number")).getValue())) {
                append(buffer, get(documentI18n, "Witness Number"));
                append(buffer, SPACE);
                append(buffer, ((LeafEventXMLNode) wsOptions.get("E20935_WR_Number")).getValue());
                append(buffer, SPACE);
                append(buffer, get(documentI18n, "Read"));
            }
        }
    }

    protected void appendEvent21100(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Legal Submissions"));
    }

    // OK
    protected void appendEvent21200(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Reporting Restrictions. For details please contact the Court Manager"));
    }

    // OK
    protected void appendEvent21201(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Reporting Restrictions Lifted"));
    }

    // OK
    protected void appendEvent30100(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        BranchEventXMLNode saOptions = (BranchEventXMLNode) node.get("E30100_Short_Adjourn_Options");
        String saoType = ((LeafEventXMLNode) saOptions.get("E30100_SAO_Type")).getValue();

        if ("E30100_Case_released_until".equals(saoType)) {
            append(buffer, get(documentI18n, "Case released until"));
            append(buffer, SPACE);
            append(buffer, ((LeafEventXMLNode) saOptions.get("E30100_SAO_Time")).getValue());
        } else if ("E30100_Case_adjourned_until".equals(saoType)) {
            append(buffer, get(documentI18n, "Case adjourned until"));
            append(buffer, SPACE);
            append(buffer, ((LeafEventXMLNode) saOptions.get("E30100_SAO_Time")).getValue());
        } else {
            append(buffer, get(documentI18n, "Case Adjourned"));
        }
    }
    
    // OK
    protected void appendEvent30200(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n, Collection<DefendantName> nameCollection) {
    	// Try to read the defendant_on_case_id node and parse it as an Integer.
        Integer defOnCaseId = null;
        Object defOnCaseObj = node.get("defendant_on_case_id");
        if (defOnCaseObj instanceof LeafEventXMLNode) {
            LeafEventXMLNode leaf = (LeafEventXMLNode) defOnCaseObj;
            String raw = leaf.getValue();
            if (raw != null && raw.trim().length() > 0) {
                try {
                    // Use valueOf/parseInt, but guard NumberFormatException
                    defOnCaseId = Integer.valueOf(raw.trim());
                } catch (NumberFormatException nfe) {
                    // Malformed number: treat as missing rather than throwing.
                    log.warn("Invalid defendant_on_case_id: " + raw, nfe);
                    defOnCaseId = null;
                }
            }
        } else if (defOnCaseObj != null) {
            // There is an object, but it's not the expected type: ignore it (and optionally log).
            log.debug("appendEvent30200: defendant_on_case_id present but not LeafEventXMLNode: " + defOnCaseObj.getClass());
        }
        
        if (defOnCaseId != null) {
	        if(!isHideInPublicDisplay(defOnCaseId, nameCollection)){         
	        	// Add Defendant name
	            append(buffer, ((LeafEventXMLNode) node.get("defendant_name")).getValue());
	            append(buffer, SEMI_COLON);
	            append(buffer, SPACE);
	        }
        } else {
        	log.warn("appendEvent30200: defOnCaseId is null");
        }

        BranchEventXMLNode laoOptions = (BranchEventXMLNode) node.get("E30200_Long_Adjourn_Options");
        String laoType = ((LeafEventXMLNode) (laoOptions.get("E30200_LAO_Type"))).getValue();

        log.debug("laoType: " + laoType);

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

    // OK
    protected void appendEvent30300(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Case Closed"));
    }

    // OK
    protected void appendEvent30400(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Court Closed"));
    }

    // OK
    protected void appendEvent30500(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Hearing finished"));
    }

    protected void appendEvent30600(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n, Collection<DefendantName> nameCollection) {
    	// Try to read the defendant_on_case_id node and parse it as an Integer.
        Integer defOnCaseId = null;
        Object defOnCaseObj = node.get("defendant_on_case_id");
        if (defOnCaseObj instanceof LeafEventXMLNode) {
            LeafEventXMLNode leaf = (LeafEventXMLNode) defOnCaseObj;
            String raw = leaf.getValue();
            if (raw != null && raw.trim().length() > 0) {
                try {
                    // Use valueOf/parseInt, but guard NumberFormatException
                    defOnCaseId = Integer.valueOf(raw.trim());
                } catch (NumberFormatException nfe) {
                    // Malformed number: treat as missing rather than throwing.
                    log.warn("Invalid defendant_on_case_id: " + raw, nfe);
                    defOnCaseId = null;
                }
            }
        } else if (defOnCaseObj != null) {
            // There is an object, but it's not the expected type: ignore it (and optionally log).
            log.debug("appendEvent30600: defendant_on_case_id present but not LeafEventXMLNode: " + defOnCaseObj.getClass());
        }
        
        if (defOnCaseId != null) {
	        if(!isHideInPublicDisplay(defOnCaseId, nameCollection)){         
	        	// Add Defendant name
	        	append(buffer, get(documentI18n, "Hearing finished for"));
	        	append(buffer, SPACE);
	            append(buffer, ((LeafEventXMLNode) node.get("defendant_name")).getValue());
	        }else{
	        	//don't add defendant name
	        	append(buffer, get(documentI18n, "Hearing finished"));
	        }
        } else {
        	append(buffer, get(documentI18n, "Hearing finished"));
        	log.warn("appendEvent30600: defOnCaseId is null");
        }
        
    }

//  OK
    protected void appendEvent31000(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness Number"));
        append(buffer, SPACE);
        append(buffer, ((LeafEventXMLNode) node.get("E31000_Witness_Number")).getValue());
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Cross Examination"));
    }
    
    //  OK
    protected void appendEvent32000(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Witness Number"));
        append(buffer, SPACE);
        append(buffer, ((LeafEventXMLNode) node.get("E32000_Witness_Number")).getValue());
        append(buffer, SPACE);
        append(buffer, get(documentI18n, "Re-examination"));
    }
    
    protected void appendEvent40601(StringBuffer buffer, @SuppressWarnings("unused")
    BranchEventXMLNode node, TranslationBundle documentI18n) {
        append(buffer, get(documentI18n, "Judge's directions"));
    }
    
    protected void appendEventCPP(StringBuffer buffer, BranchEventXMLNode node, TranslationBundle documentI18n) {
    	String freeText = new String(((LeafEventXMLNode) node.get("free_text")).getValue());
    	append(buffer, get(documentI18n, freeText));
    }

    protected boolean hasDefendants(Object item) {
        if (item instanceof CourtListValue) {
            return ((CourtListValue) item).hasDefendants();
        }
        return false;
    }

    //
    // PublicDisplayValue Access
    //

    /**
     * 
     */
    protected String getCourtRoomName(Object item, TranslationBundle documentI18n) {
        if (item instanceof PublicDisplayValue) {
            String courtRoomName = ((PublicDisplayValue) item).getCourtRoomName();
            if (courtRoomName != null) {
                return get(documentI18n, courtRoomName);
            }
        }
        return null;
    }

    /**
     * Court Site Short Name
     * 
     * @param item
     * @param documentI18n
     * @return
     */
    protected String getCourtSiteShortName(Object item, TranslationBundle documentI18n) {
        if (item instanceof PublicDisplayValue) {
            String courtSiteShortName = ((PublicDisplayValue) item).getCourtSiteShortName();
            if (courtSiteShortName != null) {
                return get(documentI18n, courtSiteShortName);
            }
        }
        return null;
    }

    /**
     * Court Site Room Name
     * 
     * @param object
     * @param documentI18n
     * @return The Court room name prefixed with site if exists
     */
    protected String getCourtSiteRoomName(Object object, TranslationBundle documentI18n) {
        String courtSiteShortName = getCourtSiteShortName(object, documentI18n);
        if (courtSiteShortName != null) {
            return courtSiteShortName + " - " + getCourtRoomName(object, documentI18n);
        } else {
            return getCourtRoomName(object, documentI18n);
        }
    }

    /**
     * Moved from Court Site Short Name
     * 
     * @param item
     * @param documentI18n
     * @return
     */
    protected String getMovedFromCourtSiteShortName(Object item, TranslationBundle documentI18n) {
        if (item instanceof PublicDisplayValue) {
            String movedFromCourtSiteShortName = ((PublicDisplayValue) item).getMovedFromCourtSiteShortName();
            if (movedFromCourtSiteShortName != null) {
                return get(documentI18n, movedFromCourtSiteShortName);
            }
        }
        return null;
    }

    /**
     * Moved From Court Site Room Name
     * 
     * @param object
     * @param documentI18n
     * @return The Court room name prefixed with site if exists
     */
    protected String getMovedFromCourtSiteRoomName(Object item, TranslationBundle documentI18n) {
        String movedFromCourtSiteShortName = getMovedFromCourtSiteShortName(item, documentI18n);
        if (movedFromCourtSiteShortName != null) {
            return movedFromCourtSiteShortName + " - " + getMovedFromCourtRoomName(item, documentI18n);
        } else {
            return getMovedFromCourtRoomName(item, documentI18n);
        }
    }

    protected String getMovedFromCourtRoomName(Object item, TranslationBundle documentI18n) {
        if (item instanceof PublicDisplayValue) {
            String movedFromCourtRoomName = ((PublicDisplayValue) item).getMovedFromCourtRoomName();
            if (movedFromCourtRoomName != null) {
                return get(documentI18n, movedFromCourtRoomName);
            }
        }
        return null;
    }

    protected String getNotBeforeTimeAsString(Object item) {
        if (item instanceof PublicDisplayValue) {
            String notBeforeTimeAsString = ((PublicDisplayValue) item).getNotBeforeTimeAsString();
            if (notBeforeTimeAsString != null) {
                return notBeforeTimeAsString;
            }
        }
        return null;
    }

    //
    // Mics util
    //

    private static final int READING_TEXT = 0;

    private static final int READING_WHITESPACE = 1;

    private static final int READING_LEADING_WHITESPACE = 2;

    protected static final String tidyWhitespace(String text) {
        if (text == null) {
            return null;
        }

        char[] source = text.toCharArray();
        char[] buffer = new char[source.length]; // Safe as must be less

        int bufferIndex = 0;

        int state = READING_LEADING_WHITESPACE;
        for (int sourceIndex = 0; sourceIndex < source.length; sourceIndex++) {
            if (state == READING_TEXT) {
                if (!Character.isWhitespace(source[sourceIndex])) {
                    buffer[bufferIndex++] = source[sourceIndex];
                } else {
                    state = READING_WHITESPACE;
                }
            } else if (state == READING_WHITESPACE) {
                if (!Character.isWhitespace(source[sourceIndex])) {
                    buffer[bufferIndex++] = ' ';
                    buffer[bufferIndex++] = source[sourceIndex];
                    state = READING_TEXT;
                }
            } else if (state == READING_LEADING_WHITESPACE) {
                if (!Character.isWhitespace(source[sourceIndex])) {
                    buffer[bufferIndex++] = source[sourceIndex];
                    state = READING_TEXT;
                }
            }
        }
        return new String(buffer, 0, bufferIndex);
    }
}
