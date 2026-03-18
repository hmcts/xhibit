//package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;
//
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.RenderChanges;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtDisplayConfigurationChange;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtRotationSetConfigurationChange;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// * <p>Title: Unit test to check the correct function of the
// * <code>DisplayConfigurationWorker</code> class.</p>
// * <p>Description: </p>
// * <p>
// * This unit test is intended to establish the correct function of the
// * <code>DisplayConfigurationWorker</code> class, by making changes to the
// * contents of the database and checking that the appropriate render changes
// * are generated.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestDisplayConfigurationWorker extends TestCase
//{
//    private final DatabaseTestScript configurationScript =
//            new DatabaseTestScript("sql/configuration_create.sql", "sql/configuration_drop.sql");
//
//    /**
//     * Creates and instance of the test case with an instance of
//     * <code>DatabaseTestScript</code> available.
//     * @param s The name of the test.
//     * @see uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DatabaseTestScript
//     */
//    public TestDisplayConfigurationWorker(String s)
//    {
//        super(s);
//    }
//
//    /**
//     * Test various permutations of <code>CourtConfigurationChange</code>
//     * <ul>
//     *     <li>
//     * No force recreate with no cached configuration.
//     *     </li>
//     *     <li>
//     * Force recreate with cached configuration.
//     *     </li>
//     *     <li>
//     * No force recreate with up to date configuration.
//     *     </li>
//     * </ul>
//     */
//    public void testCourtConfigurationChange()
//    {
//        //check changes from start with empty configuration, not using the
//        //default force recreate.
//        CourtConfigurationChange courtChange = new CourtConfigurationChange(1, false);
//        RenderChanges renderChanges = configurationScript.displayConfigurationWorker1.getRenderChanges(courtChange);
//        assertRenderChangesSizes(renderChanges,13,0,4,0);
//        System.out.println(renderChanges.toString());
//
//        //Force recreate with cached configuration.
//        courtChange = new CourtConfigurationChange(1);
//        renderChanges = configurationScript.displayConfigurationWorker1.getRenderChanges(courtChange);
//        assertRenderChangesSizes(renderChanges,13,0,4,0);
//        System.out.println(renderChanges.toString());
//
//        //No force recreate with up to date configuration.
//        courtChange = new CourtConfigurationChange(1, false);
//        renderChanges = configurationScript.displayConfigurationWorker1.getRenderChanges(courtChange);
//        assertRenderChangesSizes(renderChanges,0,0,0,0);
//        System.out.println(renderChanges.toString());
//
//        //Check render changes with court 3
//        courtChange = new CourtConfigurationChange(3);
//        renderChanges = configurationScript.displayConfigurationWorker3.getRenderChanges(courtChange);
//        assertRenderChangesSizes(renderChanges, 3, 0, 1, 0);
//        System.out.println(renderChanges.toString());
//    }
//
//    /**
//     * Test various permutations of <code>CourtDisplayConfigurationChange</code>.
//     * <ul>
//     *     <li>
//     * Change to a display's courts.
//     *     </li>
//     *     <li>
//     * Undo change to a display's courts.
//     *     </li>
//     *     <li>
//     * Change to the rotation set attached to a display.
//     *     </li>
//     *     <li>
//     * Undo change to the rotation set attached to a display.
//     *     </li>
//     *     <li>
//     * No changes.
//     *     </li>
//     * </ul>
//     * @throws SQLException When not able to make changes to the database.
//     */
//    public void testCourtDisplayConfigurationChange() throws SQLException
//    {
//        //Initialise the court.
//        CourtConfigurationChange courtChange = new CourtConfigurationChange(1);
//        RenderChanges renderChanges = configurationScript.displayConfigurationWorker1.getRenderChanges(courtChange);
//        assertRenderChangesSizes(renderChanges,13,0,4,0);
//
//        //Make a change to the courts of a display..
//        Statement statement = configurationScript.connection.createStatement();
//        statement.executeUpdate("INSERT INTO XHB_DISPLAY_COURT_ROOM\n" +
//                                "(\n" +
//                                "    DISPLAY_ID,\n" +
//                                "    COURT_ROOM_ID\n" +
//                                ")\n" +
//                                "VALUES\n" +
//                                "(\n" +
//                                "    -2,\n" +
//                                "    4\n" +
//                                ")");
//        configurationScript.connection.commit();
//
//        CourtDisplayConfigurationChange displayChange =
//                new CourtDisplayConfigurationChange(1,-2);
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(displayChange);
//        assertRenderChangesSizes(renderChanges,3,0,1,0);
//        System.out.println(renderChanges.toString());
//
//        //Change back
//        statement.executeUpdate("DELETE FROM XHB_DISPLAY_COURT_ROOM\n" +
//                                "WHERE  COURT_ROOM_ID = 4\n" +
//                                "AND    DISPLAY_ID = -2");
//        configurationScript.connection.commit();
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(displayChange);
//        assertRenderChangesSizes(renderChanges,0,3,1,0);
//        System.out.println(renderChanges.toString());
//
//        //Change a display's rotation set
//        statement.executeUpdate("UPDATE XHB_DISPLAY\n" +
//                                "SET    ROTATION_SET_ID = -2\n" +
//                                "WHERE  DISPLAY_ID = -2");
//        configurationScript.connection.commit();
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(displayChange);
//        assertRenderChangesSizes(renderChanges,3,0,1,0);
//        System.out.println(renderChanges.toString());
//
//        //Change a display's rotation set back
//        statement.executeUpdate("UPDATE XHB_DISPLAY\n" +
//                                "SET    ROTATION_SET_ID = -1\n" +
//                                "WHERE  DISPLAY_ID = -2");
//        configurationScript.connection.commit();
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(displayChange);
//        assertRenderChangesSizes(renderChanges,0,3,1,0);
//        System.out.println(renderChanges.toString());
//
//        //Check the null case.
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(displayChange);
//        assertRenderChangesSizes(renderChanges,0,0,0,0);
//        System.out.println(renderChanges.toString());
//
//        //Check the force recreate..
//        displayChange =
//                new CourtDisplayConfigurationChange(1,-2, true);
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(displayChange);
//        assertRenderChangesSizes(renderChanges,7,0,1,0);
//        System.out.println(renderChanges.toString());
//    }
//
//    /**
//     * <ul>
//     *     <li>
//     * Add a display document to a rotation set.
//     *     </li>
//     *     <li>
//     * Remove the display document from a rotation set.
//     *     </li>
//     *     <li>
//     * No changes.
//     *     </li>
//     *     <li>
//     * Change the page delay on a display document.
//     *     </li>
//     *     <li>
//     * Check force recreate.
//     *     </li>
//     * </ul>
//     * @throws SQLException When not able to make changes to the database.
//     */
//    public void testCourtRotationSetDisplayConfigurationChange() throws SQLException
//    {
//        //Initialise the court.
//        CourtConfigurationChange courtChange = new CourtConfigurationChange(1);
//        RenderChanges renderChanges = configurationScript.displayConfigurationWorker1.getRenderChanges(courtChange);
//        assertRenderChangesSizes(renderChanges,13,0,4,0);
//
//        //Add a display document to a rotation set.
//        Statement statement = configurationScript.connection.createStatement();
//        statement.executeUpdate("INSERT INTO xhb_rotation_set_dd\n"  +
//                                "(\n" +
//                                "    ROTATION_SET_DD_ID,\n" +
//                                "    ROTATION_SET_ID,\n" +
//                                "    DISPLAY_DOCUMENT_ID,\n" +
//                                "    PAGE_DELAY,\n" +
//                                "    ORDERING\n" +
//                                ")\n" +
//                                "VALUES\n" +
//                                "(\n" +
//                                "    -12,\n" +
//                                "    -2,\n" +
//                                "    1,\n" +
//                                "    17,\n" +
//                                "    4\n" +
//                                ")");
//        configurationScript.connection.commit();
//
//        CourtRotationSetConfigurationChange rotationSetChange =
//                new CourtRotationSetConfigurationChange(1, -2);
//
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(
//                rotationSetChange);
//        assertRenderChangesSizes(renderChanges,17,0,2,0);
//        System.out.println(renderChanges.toString());
//
//        //Undo the change and check that we see the appropriate changes.
//        statement.executeUpdate("DELETE FROM xhb_rotation_set_dd\n"  +
//                                "WHERE rotation_set_dd_id = -12");
//        configurationScript.connection.commit();
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(
//                rotationSetChange);
//        assertRenderChangesSizes(renderChanges,0,17,2,0);
//        System.out.println(renderChanges.toString());
//
//        //Check the null case.
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(
//                rotationSetChange);
//        assertRenderChangesSizes(renderChanges,0,0,0,0);
//        System.out.println(renderChanges.toString());
//
//        //Change the page delay of a display document in a rotation set
//        statement.executeUpdate("UPDATE xhb_rotation_set_dd\n"  +
//                                "SET PAGE_DELAY='25'\n" +
//                                "WHERE ROTATION_SET_DD_ID=-4");
//        configurationScript.connection.commit();
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(
//                rotationSetChange);
//        assertRenderChangesSizes(renderChanges,0,0,2,0);
//        System.out.println(renderChanges.toString());
//
//        //Force recreate.
//        rotationSetChange=new CourtRotationSetConfigurationChange(1, -2 , true);
//        renderChanges =
//                configurationScript.displayConfigurationWorker1.getRenderChanges(
//                rotationSetChange);
//        assertRenderChangesSizes(renderChanges,6,0,2,0);
//        System.out.println(renderChanges.toString());
//
//    }
//
//    private void assertRenderChangesSizes(RenderChanges changes,
//            int numberOfStartDocuments, int numberOfStopDocuments,
//            int numberOfStartRotationSets, int numberOfStopRotationSets)
//    {
//       assertEquals("Wrong number of 'documents to start rendering'.",
//                     numberOfStartDocuments,
//                     changes.getDocumentsToStartRendering().length);
//       assertEquals("Wrong number of 'documents to stop rendering'.",
//                     numberOfStopDocuments,
//                     changes.getDocumentsToStopRendering().length);
//       assertEquals("Wrong number of 'rotation sets to start rendering'.",
//                     numberOfStartRotationSets,
//                     changes.getDisplayRotationSetsToStartRendering().length);
//       assertEquals("Wrong number of 'rotation sets to stop rendering'.",
//                     numberOfStopRotationSets,
//                     changes.getDisplayRotationSetsToStopRendering().length);
//    }
//
//    protected void setUp() throws Exception {
//        super.setUp();
//        configurationScript.create();
//    }
//
//    protected void tearDown() throws Exception {
//        super.tearDown();
//        configurationScript.drop();
//    }
//}