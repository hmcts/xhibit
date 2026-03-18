package uk.gov.courtservice.xhibit.web.publicdisplay.test.framework;

//import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.RenderContext;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StorerContext;

import java.util.Date;
import java.util.GregorianCalendar;


/**
 * <p/>
 * Title: </p>
 * <p/>
 * <p/>
 * Description: </p>
 * <p/>
 * <p/>
 * Copyright: Copyright (c) 2003 </p>
 * <p/>
 * <p/>
 * Company: Electronic Data Systems </p>
 *
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public interface UsefulConstants
{
    Date START_DATE = new GregorianCalendar(2003, 05, 13).getTime();
//    RenderContext RENDER_CONTEXT = new RenderContext();
//    StorerContext BAD_CONFIG1 = new StorerContext("wibble", "wibble");
//    StorerContext BAD_CONFIG2 = new StorerContext("d:\\", "wibble:");
    String BASE_WEB_URL = "http://localhost:7001/PublicDisplay/FileServ";
    String STORE_LOCATION = "d:\\projects\\PublicDisplayRewrite\\thinclient\\docroot\\store";
//    StorerContext STORER_CONTEXT = new StorerContext(STORE_LOCATION, BASE_WEB_URL);
    String RENDERED_STRING = "Blad-de-blah.";
    String STORE_URL = "http://localhost:7001/PublicDisplay/store";
    int[] COURT_ROOM_IDS = new int[]{31, 32, 33, 34, 35, 40, 44, 45};
	int COURT_ID = 3;
}
