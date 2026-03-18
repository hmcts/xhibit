package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.test;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.EventWork;

/**
 * @author pznwc5
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestEventWork extends TestCase
{

	private PublicDisplayEvent event;

	public TestEventWork(String s)
		throws Exception
	{
		super(s);
	}

	public void setUp()
		throws Exception
	{
		CourtRoomIdentifier id = new CourtRoomIdentifier(new Integer(1), new Integer(31), "Test Court", new Integer(31),
				new DisplayablePublicNoticeValue[0]);
		event = new PublicNoticeEvent(id);
	}

	public void testRun()
		throws Exception
	{
		EventWork eventWork = new EventWork(event);
		eventWork.run();
	}

}
