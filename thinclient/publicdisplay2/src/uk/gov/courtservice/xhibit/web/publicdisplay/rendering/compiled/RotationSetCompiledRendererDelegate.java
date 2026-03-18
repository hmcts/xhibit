package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.rotationset.DisplayRotationSet;

public class RotationSetCompiledRendererDelegate extends AbstractCompiledRendererDelegate {
    private static final Logger log = CSServices.getLogger(RotationSetCompiledRendererDelegate.class);

    /**
     * Get the html for the rotation set
     * 
     * @param rotationSet
     * @return the html
     */
    public String getDisplayRotationSetHtml(DisplayRotationSet rotationSet) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            String html = _getDisplayRotationSetHtml(rotationSet);
            log.debug("Rendering \"" + rotationSet.getUri() + "\"" + " took "
                    + (System.currentTimeMillis() - starttime) + " ms to generated " + html.length() + " chars.");
            return html;
        }
        return _getDisplayRotationSetHtml(rotationSet);
    }

    private String _getDisplayRotationSetHtml(DisplayRotationSet rotationSet) {
        StringBuffer buffer = new StringBuffer(1024);
        appendDisplayRotationSetHtml(buffer, rotationSet);
        String html = buffer.toString();
        return html;
    }

    /**
     * Append the html generated for the rotation set to the buffer
     * 
     * @param buffer
     *            the buffer to populate
     * @param rotationSet
     *            the rotation set to process
     * 
     */
    protected void appendDisplayRotationSetHtml(StringBuffer buffer, DisplayRotationSet rotationSet) {
        appendln(buffer, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">");
        appendln(buffer, "<html>");
        appendln(buffer, "<head>");
        appendln(buffer, "<script type=\"text/javascript\" src=\"/PublicDisplay/js/common.js\">");
        appendln(buffer, "</script>");
        appendln(buffer, "<script type=\"text/javascript\" src=\"/PublicDisplay/js/rotation_set.js\">");
        appendln(buffer, "</script>");
        appendln(buffer, "<script type=\"text/javascript\">");
        // The new rotation set.
        appendln(buffer, "var rotationSet = null;");
        // This is used by other frames to check if the manager is present.
        appendln(buffer, "var allOkay=true;");
        appendln(buffer, "function initialize(){");
        appendln(buffer, "log('Manager initializing...');");
        appendln(buffer, "initializeRotationSet();");
        appendln(buffer, "setTimeout('reloadPage()',managerReload);");
        appendln(buffer, "log('Manager initialized.');");
        appendln(buffer, "top.lhs.controller.rotator.serverOkay();");
        appendln(buffer, "}");
        // This is used to constantly check for new rotation sets.
        appendln(buffer, "function reloadPage(){");
        appendln(buffer, "window.location.href=\"./FileServlet?uri=\"+getDisplayId();");
        appendln(buffer, "}");
        appendln(buffer, "window.onload = initialize;");
        appendln(buffer, "</script>");
        appendln(buffer, "<script type=\"text/javascript\">");
        appendln(buffer, "function initializeRotationSet(){");
        appendln(buffer, "var rotationSet = new RotationSet();");
        appendln(buffer, "var tempDocument = null;");
        appendln(buffer, "log('Initializing RotationSet');");
        append(buffer, "rotationSet.displayType= '");
        append(buffer, getDisplayType(rotationSet), "${set.displayType}");
        appendln(buffer, "';");

        RotationSetDisplayDocument[] documents = getRotationSetDisplayDocuments(rotationSet);
        for (int i = 0; i < documents.length; i++) {
            append(buffer, "tempDocument = new DocumentReference('");
            append(buffer, getDisplayDocumentUri(documents[i]), "${doc.displayDocumentURI}");
            append(buffer, "',");
            append(buffer, getPageDelay(documents[i]), "${doc.pageDelay}");
            appendln(buffer, ");");

            appendln(buffer, "rotationSet.add(tempDocument);");
        }

        appendln(buffer, "if(window.parent.controller.rotator){");
        appendln(buffer, "window.parent.controller.rotator.changeRotationSet(rotationSet);");
        appendln(buffer, "log('Initialized RotationSet');");
        appendln(buffer, "}else{");
        appendln(buffer, "log('Could not initialize rotator as controller frame not present.');");
        appendln(buffer, "}");
        appendln(buffer, "}");
        appendln(buffer, "</script>");
        appendln(buffer, "<title>manager</title>");
        appendln(buffer, "</head>");
        appendln(buffer, "<body bgcolor=\"lightgreen\">");
        appendln(buffer, "<h3>Manager</h3>");
        appendln(buffer, "<pre id=\"log\"></pre>");
        appendln(buffer, "</body>");
        appendln(buffer, "</html>");
    }

    //
    // Utilites to imitate the behaviour of the template engine. All values
    // need
    // to
    // be checked for null!
    //       

    private RotationSetDisplayDocument[] getRotationSetDisplayDocuments(DisplayRotationSet rotationSet) {
        if (rotationSet != null) {
            RotationSetDisplayDocument[] documents = rotationSet.getRotationSetDisplayDocuments();
            if (documents != null) {
                return documents;
            }
        }
        return new RotationSetDisplayDocument[0];
    }

    private String getDisplayType(DisplayRotationSet rotationSet) {
        if (rotationSet != null) {
            String displayType = rotationSet.getDisplayType();
            if (displayType != null) {
                return rotationSet.getDisplayType();
            }
        }
        return null;
    }

    private String getDisplayDocumentUri(RotationSetDisplayDocument document) {
        if (document != null) {
            AbstractURI uri = document.getDisplayDocumentURI();
            if (uri != null) {
                return uri.toString();
            }
        }
        return null;
    }

    private String getPageDelay(RotationSetDisplayDocument document) {
        if (document != null) {
            return String.valueOf(document.getPageDelay());
        }
        return null;
    }
}