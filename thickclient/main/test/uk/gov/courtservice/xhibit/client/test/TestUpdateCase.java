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
// * $Log: TestUpdateCase.java,v $
// * Revision 1.6  2006/07/13 12:58:04  xzfdtb
// * Unit test work - a broken unit test will now fail the build.
// *
// * Revision 1.5  2003/10/08 17:02:29  sz0t7n
// * splitting out the thickclient_framework
// *
// * Revision 1.4  2003/08/18 07:02:47  bzw8gp
// * Jon Powell
// *
// * organise imports (remove unused)
// * unused imports cause misleading dependencies
// * remove unused variables / associated imports
// *
// * Revision 1.3  2003/04/24 14:50:04  nz5zpz
// * update to test runner
// *
// */
//
//public class TestUpdateCase
//{
//	private Logger log;
//    public TestUpdateCase()
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
//		XAction xAction = null;
//		try
//		{
//			xAction = XhibitActions.getAction(xac,  XhibitActions.CaseProps);
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating UpdateCase action");
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
//		ActionEvent actionEvent = new ActionEvent(this, 0, "open case properties action");
//		xAction.actionPerformed(actionEvent);
//    }
//
//	public static void main(String[] args)
//	{
//		TestUpdateCase x = new TestUpdateCase();
//	}
//}