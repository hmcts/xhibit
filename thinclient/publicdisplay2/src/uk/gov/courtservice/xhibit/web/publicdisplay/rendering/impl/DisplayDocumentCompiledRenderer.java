package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.impl;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.common.publicdisplay.data.Data;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.AllCaseStatusCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.AllCourtStatusCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.CourtDetailCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.CourtListCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.DailytListCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.DisplayDocumentCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.JuryCurrentStatusCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.SummaryByNameCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.exceptions.DelegateNotFoundException;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.exceptions.DocumentHasNotHadDataAddedException;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.exceptions.RenderingException;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;

/**
 * <p/> Title: DisplayDocumentCompiledRenderer is the renderer supplied by the
 * RenderFactory for a display document if template rendering is NOT being used.
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Revision: 1.3 $
 */
public class DisplayDocumentCompiledRenderer implements Renderer {
    private final Map _delegateMap = new HashMap();

    /**
     * Instantiate the delegates
     */
    public DisplayDocumentCompiledRenderer() {
        _delegateMap.put("allcasestatus", new AllCaseStatusCompiledRendererDelegate());
        _delegateMap.put("allcourtstatus", new AllCourtStatusCompiledRendererDelegate());
        _delegateMap.put("courtdetail", new CourtDetailCompiledRendererDelegate());
        _delegateMap.put("courtlist", new CourtListCompiledRendererDelegate());
        _delegateMap.put("dailylist", new DailytListCompiledRendererDelegate());
        _delegateMap.put("jurycurrentstatus", new JuryCurrentStatusCompiledRendererDelegate());
        _delegateMap.put("summarybyname", new SummaryByNameCompiledRendererDelegate());
    }

    /**
     * Render the display document
     * 
     * @param renderable
     *            the rotation set (cast to Renderable) to render.
     * 
     * @throws RenderingException
     *             if the renderer fails to render.
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer#render(uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable)
     */
    public void render(final Renderable renderable) throws RenderingException {
        renderDisplayDocument((DisplayDocument) renderable);
    }

    /**
     * Process a display document set and provide a RenderedRotationSet. Please
     * note the template name is not supplied this is decided by the engine
     * itself.
     * </p>
     * 
     * @param set
     *            the rotation set to be processed.
     */
    private void renderDisplayDocument(final DisplayDocument displayDocument) {
        Data data = displayDocument.getData();
        if (data == null) {
            throw new DocumentHasNotHadDataAddedException(displayDocument);
        }

        displayDocument.setRenderedString(getDelegate(displayDocument).getDisplayDocumentHtml(displayDocument));
    }

    //
    // Utilities
    //   

    private DisplayDocumentCompiledRendererDelegate getDelegate(final DisplayDocument displayDocument) {
        String documentType = ((DisplayDocumentURI) displayDocument.getUri()).getDocumentTypeAsLowerCaseString();
        DisplayDocumentCompiledRendererDelegate delegate = (DisplayDocumentCompiledRendererDelegate) _delegateMap
                .get(documentType);
        if (delegate != null) {
            return delegate;
        }
        throw new DelegateNotFoundException(documentType);
    }
}
