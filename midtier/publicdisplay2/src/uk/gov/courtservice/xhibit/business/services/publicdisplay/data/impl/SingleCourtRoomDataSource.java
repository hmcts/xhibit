package uk.gov.courtservice.xhibit.business.services.publicdisplay.data.impl;

import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.PublicDisplayQuery;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions.DataRetrievalException;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayFailureException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.8 $
 */
public class SingleCourtRoomDataSource extends GenericPublicDisplayDataSource {
    public SingleCourtRoomDataSource(DisplayDocumentURI uri, PublicDisplayQuery query) {
        super(uri, query);
    }

    /**
     * If there is any data for the query place it into the data object
     * otherwise leave it empty.
     * 
     * @pre getUri() != null
     * @pre getData() != null
     */
    public void retrieve() throws DataRetrievalException {
        super.retrieve();
        setCourtRoomNumber(getUri().getCourtRoomIds()[0]);
    }

    /**
     * Sets the court name in the data object for the court that we're
     * retrieving data for.
     * 
     * @param courtId
     */
    private void setCourtRoomNumber(int courtId) {
        XhbCourtRoom courtRoom = null;
        try {
            courtRoom = XhbCourtRoomBeanHelper.findByPrimaryKey(new Integer(courtId));
        } catch (ObjectNotFoundException e) {
            throw new PublicDisplayFailureException(e);
        }

        if (courtRoom != null) {
            getData().setCourtSiteShortName(courtRoom.getXhbCourtSite().getShortName());
            getData().setCourtRoomName(courtRoom.getCourtRoomName());
        }
    }
}
