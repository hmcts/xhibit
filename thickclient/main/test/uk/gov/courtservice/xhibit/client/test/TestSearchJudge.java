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
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Frederik Vandendriessche
// * @version 1.0
// */
//
//public class TestSearchJudge
//{
//	private Logger log;
//
//    public TestSearchJudge()
//    {
//		try
//		{
//			log = CSServices.getLogger(TestUpdateCase.class);
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating logger");
//			e.printStackTrace();
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
//			xAction = XhibitActions.getAction(xac,  XhibitActions.OpenSearchJudge);
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating OpenSearchJudge action");
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
//		try
//		{
//			ActionEvent actionEvent = new ActionEvent(this, 0, "trigger OpenSearchJudge action");
//			xAction.actionPerformed(actionEvent);
//		}
//		catch(Exception ee)
//		{
//			XHIBITConstant.debug("trouble executing the OpenSearchJudgeActionn");
//			ee.printStackTrace();
//		}
//    }
//    public static void main(String[] args)
//    {
//        TestSearchJudge testSearchJudge1 = new TestSearchJudge();
//    }
//}