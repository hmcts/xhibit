package uk.gov.courtservice.xhibit.common.publicdisplay.setup.drilldown;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;

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
 * @version $Revision: 1.3 $
 */
public class DisplayLocationDrillDown extends DrillDown {
	
	static final long serialVersionUID = 8650102203081397337L;

    public DisplayLocationDrillDown(String courtRoomName) {
        super(courtRoomName);
    }

    public void addDisplay(DisplayURI uri) {
        add(uri);
    }

}
