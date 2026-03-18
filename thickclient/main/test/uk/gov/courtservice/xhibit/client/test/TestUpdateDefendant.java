//package uk.gov.courtservice.xhibit.client.test;
//
//import java.awt.event.ActionEvent;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
//import uk.gov.courtservice.xhibit.client.util.XAction;
//import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;
//
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author unascribed
// * @version 1.0
// */
//
//public class TestUpdateDefendant
//{
//	private Logger log;
//
//    public TestUpdateDefendant()
//    {
//		try
//		{
//			log = CSServices.getLogger(TestUpdateDefendant.class);
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating logger");
//			System.exit(-1);
//		}
//
//		XhibitApplicationController xac = null;
//		try
//		{
//			xac = new XhibitApplicationControllerImpl();
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating xhibitapplicationcontroller");
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
//		XAction xAction = null;
//		try
//		{
//			xAction = XhibitActions.getAction(xac,  XhibitActions.OpenAmendDenfendant);
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating UpdateCase action");
//			System.exit(-1);
//		}
//
//		ActionEvent actionEvent = new ActionEvent(this, 0, "open case properties action");
//		xAction.actionPerformed(null);
//
//    }
//
//	public static void main(String[] args)
//	{
//		TestUpdateDefendant x = new TestUpdateDefendant();
//	}
//}