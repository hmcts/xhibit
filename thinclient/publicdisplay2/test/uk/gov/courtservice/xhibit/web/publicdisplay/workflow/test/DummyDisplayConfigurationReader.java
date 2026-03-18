package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.test;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DisplayConfigurationReader;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.RenderChanges;

import java.util.Locale;


/**
 * <p/>
 * Title:
 * </p>
 * <p/>
 * <p/>
 * Description:
 * </p>
 * <p/>
 * <p/>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p/>
 * <p/>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class DummyDisplayConfigurationReader extends DisplayConfigurationReader
{
    private RenderChanges testRenderChanges;

    /**
     * Creates a new DummyDisplayConfigurationReader object.
     *
     * @param testRenderChanges TODO:
     */
    public DummyDisplayConfigurationReader(RenderChanges testRenderChanges)
    {
        this.testRenderChanges = testRenderChanges;
    }

    /**
     * TODO:
     *
     * @param change TODO:
     * @return TODO:
     */
    public RenderChanges getRenderChanges(CourtConfigurationChange change)
    {
        return testRenderChanges;
    }

    /**
     * TODO:
     *
     * @param change TODO:
     * @return TODO:
     */
    public RenderChanges getRenderChanges(DisplayDocumentType[] parm1, CourtRoomIdentifier parm2)
    {
        System.out.println("CourtRoom:" + parm2.getCourtRoomId().toString());

        RenderChanges renderChanges = new RenderChanges();

        for (int i = 0; i < parm1.length; i++)
        {
            System.out.println("Document Type:" + parm1[i].toString());
            DisplayDocumentURI inDoc = new DisplayDocumentURI(Locale.ENGLISH, parm2.getCourtId().intValue(), parm1[i], new int[]
            {
                parm2.getCourtRoomId().intValue()
            });
            renderChanges.addStartDocument(inDoc);
        }

        return renderChanges;
    }

    /* (non-Javadoc)
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DisplayConfigurationReader#getConfiguredCourtIds()
     */
    public int[] getConfiguredCourtIds()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
