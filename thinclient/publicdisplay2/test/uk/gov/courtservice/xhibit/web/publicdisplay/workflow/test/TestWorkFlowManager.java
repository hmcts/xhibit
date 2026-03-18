//package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.test;
//
//import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataContext;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.MoveCaseEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
//import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DatabaseTestScript;
//import uk.gov.courtservice.xhibit.web.publicdisplay.test.framework.TestCaseWithInitialization;
//import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.WorkFlowContext;
//import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.WorkFlowManager;
//
//
///**
// * <p>
// * Title: Test Workflow
// * </p>
// *
// * <p>
// * Description: These tests do not have any asserts as all the implementation
// * is hidden away. Success is demostrated by no exceptions being thrown and
// * using a tool to check code coverage.
// * </p>
// *
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// *
// * <p>
// * Company: Electronic Data Systems
// * </p>
// *
// * @author Neil Ellis, Rakesh Lakhani
// * @version $Revision: 1.5 $
// */
//public class TestWorkFlowManager extends TestCaseWithInitialization
//{
//    private final DatabaseTestScript configurationScript = new DatabaseTestScript("sql/configuration_create.sql", "sql/configuration_drop.sql");
//    private final DatabaseTestScript renderingScript = new DatabaseTestScript("sql/rendering_create.sql", "sql/rendering_drop.sql");
//
//
//    private static final Integer courtId = new Integer(3);
//
//    /**
//     * Creates a new TestWorkFlowManager object.
//     *
//     * @param s TODO:
//     */
//    public TestWorkFlowManager(String s)
//        throws Exception
//    {
//        super(s);
//    }
//
//    /**
//     * TODO:
//     */
//    public void testConfigurationChangeFirstTime()
//    {
//        System.err.println("CONFIG CHANGE (w/o RS) TEST STARTED");
//        WorkFlowContext workFlowContext = getWFContext();
//        WorkFlowManager workFlowManager = WorkFlowManager.getInstance(workFlowContext);
//        CourtConfigurationChange courtConfigurationChange = new CourtConfigurationChange(3);
//        workFlowManager.process(new ConfigurationChangeEvent(courtConfigurationChange));
//    }
//
//    /**
//     * TODO:
//     */
//    public void testConfigurationChangeSecondTime()
//    {
//        System.err.println("CONFIG CHANGE (with RS) TEST STARTED");
//        WorkFlowContext workFlowContext = getWFContext();
//        WorkFlowManager instance = WorkFlowManager.getInstance(workFlowContext);
//        CourtConfigurationChange courtConfigurationChange = new CourtConfigurationChange(3);
//        instance.process(new ConfigurationChangeEvent(courtConfigurationChange));
//    }
//
//    /**
//     * @todo
//     */
//    public void testMoveCaseEvent()
//    {
//        System.err.println("MOVE CASE TEST STARTED");
//
//        WorkFlowContext workFlowContext = getWFContext();
//        WorkFlowManager instance = WorkFlowManager.getInstance(workFlowContext);
//        MoveCaseEvent moveCaseEvent =
//                new MoveCaseEvent(
//                new CourtRoomIdentifier(courtId, new Integer(10)),
//                new CourtRoomIdentifier(courtId, new Integer(12)),
//                new CaseChangeInformation(true));
//        instance.process(moveCaseEvent);
//
//        System.out.println("MOVE CASE TEST ENDED");
//    }
//
//    /**
//     * @todo
//     */
//    public void testPublicNoticeEvent()
//    {
//        System.err.println("PUBLIC NOTICE TEST STARTED");
//
//        WorkFlowContext workFlowContext = getWFContext();
//        WorkFlowManager instance = WorkFlowManager.getInstance(workFlowContext);
//        PublicNoticeEvent publicNoticeEvent = new PublicNoticeEvent(new CourtRoomIdentifier(courtId, new Integer(9)));
//        instance.process(publicNoticeEvent);
//    }
//
//    /**
//     * @todo
//     */
//    public void testUpdateCaseEvent()
//    {
//        System.err.println("UPDATE CASE TEST STARTED");
//
//        WorkFlowContext workFlowContext = getWFContext();
//        WorkFlowManager instance = WorkFlowManager.getInstance(workFlowContext);
//        UpdateCaseEvent updateCaseEvent = new UpdateCaseEvent(
//                new CourtRoomIdentifier(courtId, new Integer(3)),
//                new CaseChangeInformation(false) );
//        instance.process(updateCaseEvent);
//    }
//
//    /**
//     * TODO:
//     *
//     * @throws java.lang.Exception TODO:
//     */
//    protected void setUp()
//        throws Exception
//    {
//        super.setUp();
//        configurationScript.create();
//        renderingScript.create();
//    }
//
//
//    /**
//     * Sets up a context with render changes that contain only one document
//     *
//     * @return
//     */
//    private WorkFlowContext getWFContext()
//    {
//        WorkFlowContext workFlowContext = WorkFlowContext.newInstance();
//        DataContext dataContext = new DataContext(START_DATE);
//        workFlowContext.setDataContext(dataContext);
//        return workFlowContext;
//
//    }
//}
//